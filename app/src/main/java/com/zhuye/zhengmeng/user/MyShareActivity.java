package com.zhuye.zhengmeng.user;

import android.content.Context;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.blankj.utilcode.util.SPUtils;
import com.blankj.utilcode.util.ToastUtils;
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
import com.zhuye.zhengmeng.user.adapter.ShareAdapter;
import com.zhuye.zhengmeng.user.bean.ShareListBean;
import com.zhuye.zhengmeng.view.MyAppTitle;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class MyShareActivity extends BaseActivity implements OnRefreshListener, OnLoadmoreListener {

    private static final int MYSHARE_WHAT1 = 0x001;
    private static final int MYSHARE_WHAT2 = 0x002;
    private static final int MYSHARE_WHAT3 = 0x003;

    @BindView(R.id.titleBar)
    MyAppTitle titleBar;
    @BindView(R.id.rv_list)
    EasyRecyclerView mRecyclerView;
    @BindView(R.id.refreshLayout)
    SmartRefreshLayout refreshLayout;
    private String token;
    private ShareAdapter mShareAdapter;
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

    @Override
    protected void loadViewLayout() {
        setContentView(R.layout.activity_my_share);
    }

    @Override
    protected Context getActivityContext() {
        return this;
    }

    private void setTitle() {
        titleBar.initViewsVisible(true, true, false, false);
        titleBar.setAppTitle("我的分享");
        titleBar.setTitleSize(20);
        titleBar.setOnLeftButtonClickListener(new MyAppTitle.OnLeftButtonClickListener() {
            @Override
            public void onLeftButtonClick(View v) {
                finish();
            }
        });
    }

    /**
     * 接口回调
     */
    MyCallBack callBack = new MyCallBack() {
        @Override
        public void onSuccess(int what, Response<String> result) {
            switch (what) {
                case MYSHARE_WHAT1:
                    try {
                        Gson gson = new Gson();
                        ShareListBean shareListBean = gson.fromJson(result.body(), ShareListBean.class);
                        List<ShareListBean.DataBean> datas;
                        datas = shareListBean.getData();
                        mRecyclerView.setLayoutManager(new LinearLayoutManager(MyShareActivity.this));
                        mShareAdapter = new ShareAdapter(R.layout.fragment_share_item1, datas, MyShareActivity.this);
                        mRecyclerView.setAdapter(mShareAdapter);
                        if (datas.size() == 0) {
                            mShareAdapter.setEmptyView(R.layout.empty,mRecyclerView);
                        }
                        mShareAdapter.openLoadAnimation(BaseQuickAdapter.ALPHAIN);
                        mShareAdapter.isFirstOnly(false);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    break;
                default:
                    break;
            }
        }

        @Override
        public void onFail(int what, Response<String> result) {
            switch (what) {
                case MYSHARE_WHAT1:
                    ToastUtils.showShort(result.body());
                    break;
                default:
                    break;
            }
        }

        @Override
        public void onFinish(int what) {
            refreshLayout.finishRefresh();
            refreshLayout.finishLoadmore();
        }
    };

    @Override
    public void onRefresh(RefreshLayout refreshlayout) {
        page = 1;
        DreamApi.getShareListUrl(this, MYSHARE_WHAT1, token, page, callBack);
    }

    @Override
    public void onLoadmore(final RefreshLayout refreshlayout) {
        page++;
        DreamApi.getShareListUrl(this, MYSHARE_WHAT1, token, page, new MyCallBack() {
            @Override
            public void onSuccess(int what, Response<String> result) {
                try {
                    Gson gson = new Gson();
                    ShareListBean shareListBean = gson.fromJson(result.body(), ShareListBean.class);
                    List<ShareListBean.DataBean> datas;
                    datas = shareListBean.getData();
                    if (datas.size() == 0) {
                        refreshLayout.setLoadmoreFinished(true);
                    } else {
                        mShareAdapter.addData(datas);
                    }
                } catch (Exception e) {
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
    public void onDestroy() {
        super.onDestroy();
        page = 1;
    }
}
