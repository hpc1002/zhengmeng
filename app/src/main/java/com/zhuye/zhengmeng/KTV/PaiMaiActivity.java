package com.zhuye.zhengmeng.KTV;

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
import com.zhuye.zhengmeng.KTV.adapter.PaiMaiAdapter;
import com.zhuye.zhengmeng.KTV.bean.PaiMaiBean;
import com.zhuye.zhengmeng.R;
import com.zhuye.zhengmeng.base.BaseActivity;
import com.zhuye.zhengmeng.http.DreamApi;
import com.zhuye.zhengmeng.http.MyCallBack;
import com.zhuye.zhengmeng.view.MyAppTitle;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class PaiMaiActivity extends BaseActivity implements OnRefreshListener, OnLoadmoreListener {

    @BindView(R.id.titleBar)
    MyAppTitle titleBar;
    @BindView(R.id.recyclerView)
    EasyRecyclerView recyclerView;
    @BindView(R.id.refreshLayout)
    SmartRefreshLayout refreshLayout;
    private String roomId;
    private String token;
    int page = 1;
    private PaiMaiAdapter paiMaiAdapter;

    @Override
    protected void processLogic() {

    }

    @Override
    protected void setListener() {
        token = SPUtils.getInstance("userInfo").getString("token");
        roomId = getIntent().getStringExtra("roomId");
        setTitle();
        refreshLayout.autoRefresh();
        refreshLayout.setOnRefreshListener(this);
        refreshLayout.setOnLoadmoreListener(this);
    }


    private void setTitle() {
        titleBar.initViewsVisible(true, true, false, false);
        titleBar.setAppTitle("点歌");
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
        setContentView(R.layout.activity_pai_mai);
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
        DreamApi.getPaiMaiList(0x001, token, roomId, new MyCallBack() {
            @Override
            public void onSuccess(int what, Response<String> result) {
                try {
                    JSONObject jsonObject = new JSONObject(result.body());
                    int code = jsonObject.getInt("code");
                    if (code == 200) {

                        PaiMaiBean paiMaiListBean = new Gson().fromJson(result.body(), PaiMaiBean.class);
                        List<PaiMaiBean.Data> data;
                        data = paiMaiListBean.data;
                        initUi(data);
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
                refreshlayout.finishLoadmore();
                refreshlayout.finishRefresh();
            }
        });
    }

    private void initUi(List<PaiMaiBean.Data> data) {
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        paiMaiAdapter = new PaiMaiAdapter(R.layout.item_song, data);
        recyclerView.setAdapter(paiMaiAdapter);
        if (data.size() == 0) {
            paiMaiAdapter.setEmptyView(R.layout.empty, recyclerView);
        }
    }

    @Override
    public void onLoadmore(final RefreshLayout refreshlayout) {
        page++;
        DreamApi.getPaiMaiList(0x001, token, roomId, new MyCallBack() {
            @Override
            public void onSuccess(int what, Response<String> result) {
                try {
                    JSONObject jsonObject = new JSONObject(result.body());
                    int code = jsonObject.getInt("code");
                    if (code == 200) {

                        PaiMaiBean paiMaiListBean = new Gson().fromJson(result.body(), PaiMaiBean.class);
                        List<PaiMaiBean.Data> data;
                        data = paiMaiListBean.data;
                        if (data.size() == 0) {
                            refreshLayout.setLoadmoreFinished(true);
                        } else {
                            paiMaiAdapter.addData(data);
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
                refreshlayout.finishLoadmore();
                refreshlayout.finishRefresh();
            }
        });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        page = 1;
    }
}
