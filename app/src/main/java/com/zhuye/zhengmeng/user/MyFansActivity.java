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
import com.zhuye.zhengmeng.user.adapter.FansListAdapter;
import com.zhuye.zhengmeng.user.bean.FansListBean;
import com.zhuye.zhengmeng.view.MyAppTitle;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * 我的粉丝
 */
public class MyFansActivity extends BaseActivity {


    @BindView(R.id.titleBar)
    MyAppTitle titleBar;
    @BindView(R.id.recyclerView)
    EasyRecyclerView recyclerView;
    @BindView(R.id.refreshLayout)
    SmartRefreshLayout refreshLayout;
    private String token;
    //    private RecyclerArrayAdapter<FansListBean.Data> fansListAdapter;
    private FansListAdapter fansListAdapter;
    int page = 1;

    @Override
    protected void processLogic() {
        token = SPUtils.getInstance("userInfo").getString("token");

    }

    @Override
    protected void setListener() {
        setTitle();
        refreshLayout.autoRefresh();
        initRes();
    }

    private void initRes() {
        refreshLayout.setOnRefreshListener(new OnRefreshListener() {
            @Override
            public void onRefresh(final RefreshLayout refreshlayout) {
                //获取数据
                page = 1;
                DreamApi.getFansList(MyFansActivity.this, 0x001, token, "0", page, new MyCallBack() {
                    @Override
                    public void onSuccess(int what, Response<String> result) {
                        try {
                            JSONObject jsonObject = new JSONObject(result.body());
                            int code = jsonObject.getInt("code");
                            if (code == 200) {
                                FansListBean fansListBean = new Gson().fromJson(result.body(), FansListBean.class);
                                List<FansListBean.Data> data;
                                data = fansListBean.data;
                                recyclerView.setLayoutManager(new LinearLayoutManager(MyFansActivity.this));
                                //创建适配器
                                fansListAdapter = new FansListAdapter(R.layout.item_fan, data, MyFansActivity.this);
                                //给RecyclerView设置适配器
                                recyclerView.setAdapter(fansListAdapter);
                                fansListAdapter.openLoadAnimation(BaseQuickAdapter.ALPHAIN);
                                fansListAdapter.isFirstOnly(false);
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
                        switch (what) {
                            case 0x001:
                                refreshLayout.finishRefresh();
                                break;
                            default:
                                break;
                        }
                    }
                });
            }
        });
        refreshLayout.setOnLoadmoreListener(new OnLoadmoreListener() {
            @Override
            public void onLoadmore(final RefreshLayout refreshlayout) {
                ++page;
                DreamApi.getFansList(MyFansActivity.this, 0x001, token, "0", page, new MyCallBack() {
                    @Override
                    public void onSuccess(int what, Response<String> result) {
                        FansListBean fansListBean = new Gson().fromJson(result.body(), FansListBean.class);
                        List<FansListBean.Data> data;
                        data = fansListBean.data;
                        if (data.size() == 0) {
                            refreshlayout.setLoadmoreFinished(true);
                        } else {
                            fansListAdapter.addData(data);
                        }
                    }

                    @Override
                    public void onFail(int what, Response<String> result) {
                        fansListAdapter.setEmptyView(R.layout.empty, recyclerView);
                    }

                    @Override
                    public void onFinish(int what) {
                        refreshLayout.finishLoadmore();//解决加载更多一直显示
                        refreshLayout.finishRefresh();
                    }
                });

            }
        });
    }

    private void setTitle() {

        titleBar.initViewsVisible(true, true, false, false);
        titleBar.setAppTitle("我的粉丝");
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
        setContentView(R.layout.activity_my_fans);
    }

    @Override
    protected Context getActivityContext() {
        return this;
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        page=1;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // TODO: add setContentView(...) invocation
        ButterKnife.bind(this);
    }
}
