package com.zhuye.zhengmeng.user.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.blankj.utilcode.util.SPUtils;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.google.gson.Gson;
import com.lzy.okgo.model.Response;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnLoadmoreListener;
import com.scwang.smartrefresh.layout.listener.OnRefreshListener;
import com.zhuye.zhengmeng.R;
import com.zhuye.zhengmeng.base.BaseFragment;
import com.zhuye.zhengmeng.dynamic.DynamicDetailActivity;
import com.zhuye.zhengmeng.home.fragment.adapter.DynamicAdapter;
import com.zhuye.zhengmeng.home.fragment.model.DynamicModel;
import com.zhuye.zhengmeng.http.DreamApi;
import com.zhuye.zhengmeng.http.MyCallBack;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

/**
 * Created by lilei on 2017/11/1.
 */

public class ZuopinFragment extends BaseFragment {
    @BindView(R.id.rv_list)
    RecyclerView rvList;
    Unbinder unbinder;
    @BindView(R.id.refreshLayout)
    SmartRefreshLayout refreshLayout;

    private DynamicAdapter dynamicAdapter;
    int page = 1;
    private static final int ZUOPIN_WHAT = 0x001;

    @Override
    protected View initView(LayoutInflater inflater, ViewGroup container) {
        return inflater.inflate(R.layout.fragment_zuopin, container, false);
    }

    @Override
    protected void initListener() {

    }

    @Override
    protected void initData() {
        //获取个人作品
        refreshLayout.autoRefresh();//第一次进入触发自动刷新，演示效果
        initRes();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // TODO: inflate a fragment view
        View rootView = super.onCreateView(inflater, container, savedInstanceState);
        unbinder = ButterKnife.bind(this, rootView);
        return rootView;
    }

    private void initRes() {
        final String token = SPUtils.getInstance("userInfo").getString("token");
        final String user_id = SPUtils.getInstance("userInfo").getString("user_id");

        refreshLayout.setOnRefreshListener(new OnRefreshListener() {
            @Override
            public void onRefresh(final RefreshLayout refreshlayout) {
                //获取数据
                page = 1;
                DreamApi.conditionListNorefresh(ZUOPIN_WHAT, token, String.valueOf(page), user_id, myCallBack);
            }
        });
        refreshLayout.setOnLoadmoreListener(new OnLoadmoreListener() {
            @Override
            public void onLoadmore(final RefreshLayout refreshlayout) {
                ++page;
                DreamApi.conditionListNorefresh(ZUOPIN_WHAT, token, String.valueOf(page), user_id, new MyCallBack() {
                    @Override
                    public void onSuccess(int what, Response<String> result) {
                        Gson gson = new Gson();
                        DynamicModel dynamicModel = gson.fromJson(result.body(), DynamicModel.class);
                        List<DynamicModel.DataBean> datas;
                        datas = dynamicModel.getData();
                        if (datas.size() == 0) {
                            refreshLayout.setLoadmoreFinished(true);
                        } else {
                            dynamicAdapter.addData(datas);
                        }
                    }

                    @Override
                    public void onFail(int what, Response<String> result) {

                    }

                    @Override
                    public void onFinish(int what) {
                        refreshlayout.finishLoadmore();//解决加载更多一直显示
                        refreshlayout.finishRefresh();
                    }
                });

            }
        });

    }

    MyCallBack myCallBack = new MyCallBack() {
        @Override
        public void onSuccess(int what, Response<String> result) {
            switch (what) {
                case ZUOPIN_WHAT:
                    Gson gson = new Gson();
                    DynamicModel dynamicModel = gson.fromJson(result.body(), DynamicModel.class);
                    List<DynamicModel.DataBean> datas;
                    datas = dynamicModel.getData();
                    //创建布局管理
                    final LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity());
                    layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
                    rvList.setLayoutManager(layoutManager);
                    //创建适配器
                    dynamicAdapter = new DynamicAdapter(R.layout.fragment_dynamic_item1, datas, getActivity());
                    //开启动画（默认为渐显效果）
                    dynamicAdapter.openLoadAnimation();
                    //给RecyclerView设置适配器
                    rvList.setAdapter(dynamicAdapter);
                    dynamicAdapter.openLoadAnimation(BaseQuickAdapter.ALPHAIN);
                    dynamicAdapter.isFirstOnly(false);
                    dynamicAdapter.setCallBack(new DynamicAdapter.allCheck() {
                        @Override
                        public void OnItemClickListener(String id, String urlPath, String name) {
                            Intent intent = new Intent(getActivity(), DynamicDetailActivity.class);
                            intent.putExtra("production_id", id);
                            intent.putExtra("production_path", urlPath);
                            intent.putExtra("production_name", name);
                            startActivity(intent);
                        }
                    });
                    break;
            }
        }

        @Override
        public void onFail(int what, Response<String> result) {

        }

        @Override
        public void onFinish(int what) {
            switch (what) {
                case ZUOPIN_WHAT:
                    refreshLayout.finishRefresh();
                    break;
                default:
                    break;
            }
        }
    };

    @Override
    public void onDestroy() {
        super.onDestroy();
        page = 1;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }
}
