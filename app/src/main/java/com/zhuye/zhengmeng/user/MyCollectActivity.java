package com.zhuye.zhengmeng.user;

import android.content.Context;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.view.View;

import com.blankj.utilcode.util.SPUtils;
import com.chad.library.adapter.base.BaseQuickAdapter;
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
import com.zhuye.zhengmeng.user.adapter.CollectListAdapter;
import com.zhuye.zhengmeng.user.bean.CollectListBean;
import com.zhuye.zhengmeng.view.MyAppTitle;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * 我的收藏
 */
public class MyCollectActivity extends BaseActivity implements OnRefreshListener, OnLoadmoreListener {


    @BindView(R.id.recyclerView)
    EasyRecyclerView recyclerView;
    @BindView(R.id.titleBar)
    MyAppTitle titleBar;
    @BindView(R.id.refreshLayout)
    SmartRefreshLayout refreshLayout;
    private CollectListAdapter collectListAdapter;
    private String token;
    int page = 1;

    @Override
    protected void processLogic() {
    }

    @Override
    protected void setListener() {
        token = SPUtils.getInstance("userInfo").getString("token");
        setTitle();
        refreshLayout.autoRefresh();
        refreshLayout.setOnRefreshListener(this);
        refreshLayout.setOnLoadmoreListener(this);
    }

    private void setTitle() {
        titleBar.initViewsVisible(true, true, false, false);
        titleBar.setAppTitle("我的收藏");
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
        setContentView(R.layout.activity_my_collect);
    }

    @Override
    protected Context getActivityContext() {
        return this;
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

                            CollectListBean collectListBean = new Gson().fromJson(result.body(), CollectListBean.class);
                            List<CollectListBean.Data> collectData;
                            collectData = collectListBean.data;

                            initUi(collectData);
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
            refreshLayout.finishLoadmore();//解决加载更多一直显示
            refreshLayout.finishRefresh();
        }
    };

    private void initUi(List<CollectListBean.Data> collectData) {
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        collectListAdapter = new CollectListAdapter(R.layout.item_collect, collectData, this);
        if (collectData == null) {
            collectListAdapter.setEmptyView(R.layout.empty, recyclerView);
        }
        recyclerView.setAdapter(collectListAdapter);
        collectListAdapter.openLoadAnimation(BaseQuickAdapter.ALPHAIN);
        collectListAdapter.isFirstOnly(false);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // TODO: add setContentView(...) invocation
        ButterKnife.bind(this);
    }

    @Override
    public void onRefresh(RefreshLayout refreshlayout) {
        page = 1;
        DreamApi.getCollectList(this, 0x001, token, 1, myCallBack);
    }

    @Override
    public void onLoadmore(RefreshLayout refreshlayout) {
        page++;
        DreamApi.getCollectList(this, 0x001, token, page, new MyCallBack() {
            @Override
            public void onSuccess(int what, Response<String> result) {
                try {
                    JSONObject jsonObject = new JSONObject(result.body());
                    int code = jsonObject.getInt("code");
                    if (code == 200) {
                        CollectListBean collectListBean = new Gson().fromJson(result.body(), CollectListBean.class);
                        List<CollectListBean.Data> collectData;
                        collectData = collectListBean.data;
                        if (collectData == null) {
                            refreshLayout.setLoadmoreFinished(true);
                        } else {
                            collectListAdapter.addData(collectData);
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
                refreshLayout.finishLoadmore();//解决加载更多一直显示
                refreshLayout.finishRefresh();
            }
        });
    }
}
