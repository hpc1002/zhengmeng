package com.zhuye.zhengmeng.user;

import android.content.Context;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.view.View;

import com.blankj.utilcode.util.SPUtils;
import com.google.gson.Gson;
import com.jude.easyrecyclerview.EasyRecyclerView;
import com.lzy.okgo.model.Response;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnLoadmoreListener;
import com.scwang.smartrefresh.layout.listener.OnRefreshListener;
import com.zhuye.zhengmeng.R;
import com.zhuye.zhengmeng.base.BaseActivity;
import com.zhuye.zhengmeng.http.DreamApi;
import com.zhuye.zhengmeng.http.MyCallBack;
import com.zhuye.zhengmeng.user.adapter.RankRecordAdapter;
import com.zhuye.zhengmeng.user.bean.RankListBean;
import com.zhuye.zhengmeng.view.MyAppTitle;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class RankRecordActivity extends BaseActivity implements OnRefreshListener, OnLoadmoreListener {
    @BindView(R.id.titleBar)
    MyAppTitle titleBar;
    @BindView(R.id.recyclerView)
    EasyRecyclerView recyclerView;
    @BindView(R.id.refreshLayout)
    SmartRefreshLayout refreshLayout;
    private String token;
    private String user_id;
    int page = 1;
    private RankRecordAdapter rankRecordAdapter;

    @Override
    protected void processLogic() {

    }

    @Override
    protected void setListener() {
        token = SPUtils.getInstance("userInfo").getString("token");
        user_id = SPUtils.getInstance("userInfo").getString("user_id");
        setTitle();
        refreshLayout.autoRefresh();
        refreshLayout.setOnRefreshListener(this);
        refreshLayout.setOnLoadmoreListener(this);
    }

    private void setTitle() {
        titleBar.initViewsVisible(true, true, false, false);
        titleBar.setAppTitle("上榜记录");
        titleBar.setTitleSize(20);
        titleBar.setOnLeftButtonClickListener(new MyAppTitle.OnLeftButtonClickListener() {
            @Override
            public void onLeftButtonClick(View v) {
                finish();
            }
        });
    }

    @Override
    protected void loadViewLayout() {
        setContentView(R.layout.activity_rank_record);
    }

    @Override
    protected Context getActivityContext() {
        return this;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // TODO: add setContentView(...) invocation
        ButterKnife.bind(this);
    }

    @Override
    public void onRefresh(final RefreshLayout refreshlayout) {
        page = 1;
        DreamApi.getRankList(0x001, token, user_id, new MyCallBack() {
            @Override
            public void onSuccess(int what, Response<String> result) {
                try {
                    JSONObject jsonObject = new JSONObject(result.body());
                    int code = jsonObject.getInt("code");
                    if (code == 200) {
                        RankListBean rankListBean = new Gson().fromJson(result.body(), RankListBean.class);
                        List<RankListBean.Data> data;
                        data = rankListBean.data;
                        recyclerView.setLayoutManager(new LinearLayoutManager(RankRecordActivity.this));

                        rankRecordAdapter = new RankRecordAdapter(R.layout.item_rank, data);
                        if (data.size()==0) {
                            rankRecordAdapter.setEmptyView(R.layout.empty, recyclerView);
                        }
                        recyclerView.setAdapter(rankRecordAdapter);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFail(int what, Response<String> result) {

            }

            @Override
            public void onFinish(int what) {
                refreshlayout.finishRefresh();
                refreshlayout.finishLoadmore();
            }
        });
    }

    @Override
    public void onLoadmore(final RefreshLayout refreshlayout) {
        page++;
        DreamApi.getRankList(0x001, token, user_id, new MyCallBack() {
            @Override
            public void onSuccess(int what, Response<String> result) {
                try {
                    JSONObject jsonObject = new JSONObject(result.body());
                    int code = jsonObject.getInt("code");
                    if (code == 200) {
                        RankListBean rankListBean = new Gson().fromJson(result.body(), RankListBean.class);
                        List<RankListBean.Data> data;
                        data = rankListBean.data;
                        if (data.size()==0) {
                            refreshlayout.setLoadmoreFinished(true);
                        } else {
                            rankRecordAdapter.addData(data);
                        }

                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFail(int what, Response<String> result) {

            }

            @Override
            public void onFinish(int what) {
                refreshlayout.finishRefresh();
                refreshlayout.finishLoadmore();
            }
        });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        page = 1;
    }
}
