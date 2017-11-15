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
import com.zhuye.zhengmeng.user.adapter.FollowListAdapter;
import com.zhuye.zhengmeng.user.bean.FansListBean;
import com.zhuye.zhengmeng.view.MyAppTitle;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * 我的关注
 */
public class MyFollowActivity extends BaseActivity implements OnRefreshListener, OnLoadmoreListener {


    @BindView(R.id.titleBar)
    MyAppTitle titleBar;
    @BindView(R.id.recyclerView)
    EasyRecyclerView recyclerView;
    @BindView(R.id.refreshLayout)
    SmartRefreshLayout refreshLayout;
    private String token;
    private FollowListAdapter followListAdapter;
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
        titleBar.setAppTitle("我的关注");
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
        setContentView(R.layout.activity_my_follow);
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
    public void onRefresh(RefreshLayout refreshlayout) {
        page = 1;
        DreamApi.getAttentionList(0x001, token, page, "0", myCallBack);
    }

    @Override
    public void onLoadmore(RefreshLayout refreshlayout) {
        page++;
        DreamApi.getAttentionList(0x001, token, page, "0", new MyCallBack() {
            @Override
            public void onSuccess(int what, Response<String> result) {
                try {
                    JSONObject jsonObject = new JSONObject(result.body());
                    int code = jsonObject.getInt("code");
                    if (code == 200) {
                        FansListBean followListBean = new Gson().fromJson(result.body(), FansListBean.class);
                        List<FansListBean.Data> data = followListBean.data;
                        if (data.size()==0) {
                            refreshLayout.setLoadmoreFinished(true);
                        } else {
                            followListAdapter.addData(data);
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

    private MyCallBack myCallBack = new MyCallBack() {
        @Override
        public void onSuccess(int what, Response<String> result) {
            try {
                JSONObject jsonObject = new JSONObject(result.body());
                int code = jsonObject.getInt("code");
                if (code == 200) {
                    FansListBean followListBean = new Gson().fromJson(result.body(), FansListBean.class);
                    List<FansListBean.Data> data;
                    data = followListBean.data;
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
            refreshLayout.finishLoadmore();//解决加载更多一直显示
            refreshLayout.finishRefresh();
        }
    };

    private void initUi(List<FansListBean.Data> data) {
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        followListAdapter = new FollowListAdapter(R.layout.item_fan, data, this);
        recyclerView.setAdapter(followListAdapter);
        if (data.size()==0) {
            followListAdapter.setEmptyView(R.layout.empty, recyclerView);
        }
        followListAdapter.openLoadAnimation(BaseQuickAdapter.ALPHAIN);
        followListAdapter.isFirstOnly(false);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        page = 1;
    }
}
