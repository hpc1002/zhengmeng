package com.zhuye.zhengmeng.bangdan;

import android.content.Context;
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
import com.zhuye.zhengmeng.bangdan.adapter.BangDanRankAdapter;
import com.zhuye.zhengmeng.bangdan.bean.BangDanListBean;
import com.zhuye.zhengmeng.base.BaseActivity;
import com.zhuye.zhengmeng.http.DreamApi;
import com.zhuye.zhengmeng.http.MyCallBack;
import com.zhuye.zhengmeng.view.MyAppTitle;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;

import butterknife.BindView;

public class BangdanRankActivity extends BaseActivity implements OnRefreshListener, OnLoadmoreListener {


    @BindView(R.id.titleBar)
    MyAppTitle titleBar;
    @BindView(R.id.recyclerView)
    EasyRecyclerView recyclerView;
    @BindView(R.id.refreshLayout)
    SmartRefreshLayout refreshLayout;
    private String token;
    int page = 1;
    private BangDanRankAdapter bangDanRankAdapter;

    @Override
    protected void processLogic() {

    }

    @Override
    protected void setListener() {
        setTitle();
        token = SPUtils.getInstance("userInfo").getString("token");
        refreshLayout.autoRefresh();
        refreshLayout.setOnRefreshListener(this);
        refreshLayout.setOnLoadmoreListener(this);
    }

    private void setTitle() {
        titleBar.initViewsVisible(true, true, false, false);
        titleBar.setAppTitle("歌曲列表");
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
        setContentView(R.layout.activity_bangdan_rank);
    }

    @Override
    protected Context getActivityContext() {
        return this;
    }

    @Override
    public void onRefresh(RefreshLayout refreshlayout) {
        page = 1;
        DreamApi.getBangdanList(0x001, token, myCallBack);
    }

    @Override
    public void onLoadmore(RefreshLayout refreshlayout) {
        page++;
        DreamApi.getBangdanList(0x001, token, new MyCallBack() {
            @Override
            public void onSuccess(int what, Response<String> result) {
                try {
                    JSONObject jsonObject = new JSONObject(result.body());
                    int code = jsonObject.getInt("code");
                    if (code == 200) {
                        BangDanListBean bangDanListBean = new Gson().fromJson(result.body(), BangDanListBean.class);
                        List<BangDanListBean.Data> data;
                        data = bangDanListBean.data;
                        if (data.size() == 0) {
                            refreshLayout.setLoadmoreFinished(true);
                        } else {
                            bangDanRankAdapter.addData(data);
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
                refreshLayout.finishLoadmore();
                refreshLayout.finishRefresh();
            }
        });
    }

    private MyCallBack myCallBack = new MyCallBack() {
        @Override
        public void onSuccess(int what, Response<String> result) {
            switch (what) {
                case 0x001:
                    try {
                        JSONObject jsonObject = new JSONObject(result.body());
                        int code = jsonObject.getInt("code");
                        if (code == 200) {
                            BangDanListBean bangDanListBean = new Gson().fromJson(result.body(), BangDanListBean.class);
                            List<BangDanListBean.Data> data;
                            data = bangDanListBean.data;
                            recyclerView.setLayoutManager(new LinearLayoutManager(BangdanRankActivity.this));
                            bangDanRankAdapter = new BangDanRankAdapter(R.layout.item_rank, data, BangdanRankActivity.this);
                            if (data.size() == 0) {
                                bangDanRankAdapter.setEmptyView(R.layout.empty, recyclerView);
                            }
                            recyclerView.setAdapter(bangDanRankAdapter);
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    break;
            }
        }

        @Override
        public void onFail(int what, Response<String> result) {

        }

        @Override
        public void onFinish(int what) {
            refreshLayout.finishLoadmore();
            refreshLayout.finishRefresh();
        }
    };

    @Override
    protected void onDestroy() {
        super.onDestroy();
        page = 1;
    }
}
