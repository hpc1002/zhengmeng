package com.zhuye.zhengmeng.user.fragment;

import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

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
import com.zhuye.zhengmeng.base.BaseFragment;
import com.zhuye.zhengmeng.http.DreamApi;
import com.zhuye.zhengmeng.http.MyCallBack;
import com.zhuye.zhengmeng.user.adapter.ReceiveGiftListAdapter;
import com.zhuye.zhengmeng.user.bean.GiftListBean;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

/**
 * Created by hpc on 2017/10/30.
 */

public class ReceiveFragment extends BaseFragment implements OnRefreshListener, OnLoadmoreListener {
    @BindView(R.id.recyclerView)
    EasyRecyclerView recyclerView;
    @BindView(R.id.refreshLayout)
    SmartRefreshLayout refreshLayout;
    Unbinder unbinder;
    private String token;
    private ReceiveGiftListAdapter receiveGiftListAdapter;
    int page = 1;

    @Override
    protected View initView(LayoutInflater inflater, ViewGroup container) {
        return inflater.inflate(R.layout.fragment_received, container, false);
    }

    @Override
    protected void initListener() {
        token = SPUtils.getInstance("userInfo").getString("token");
        refreshLayout.autoRefresh();
        refreshLayout.setOnRefreshListener(this);
        refreshLayout.setOnLoadmoreListener(this);
    }


    @Override
    protected void initData() {


    }


    MyCallBack myCallBack = new MyCallBack() {
        @Override
        public void onSuccess(int what, Response<String> result) {
            switch (what) {
                case 0x001:
                    try {
                        JSONObject jsonObject = new JSONObject(result.body());
                        int code = jsonObject.getInt("code");
                        if (code == 200) {
                            GiftListBean giftListBean = new Gson().fromJson(result.body(), GiftListBean.class);
                            List<GiftListBean.Data> datas;
                            datas = giftListBean.data;
                            initUi(datas);
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

    private void initUi(List<GiftListBean.Data> datas) {

        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));

        receiveGiftListAdapter = new ReceiveGiftListAdapter(R.layout.item_gift, datas, getActivity());
        recyclerView.setAdapter(receiveGiftListAdapter);
        if (datas == null) {
            receiveGiftListAdapter.setEmptyView(R.layout.empty, recyclerView);
        }
        receiveGiftListAdapter.openLoadAnimation(BaseQuickAdapter.ALPHAIN);
        receiveGiftListAdapter.isFirstOnly(false);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // TODO: inflate a fragment view
        View rootView = super.onCreateView(inflater, container, savedInstanceState);
        unbinder = ButterKnife.bind(this, rootView);
        return rootView;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }

    @Override
    public void onRefresh(RefreshLayout refreshlayout) {
        page = 1;
        DreamApi.getReceiveGiftList(0x001, getActivity(), token, page, myCallBack);
    }

    @Override
    public void onLoadmore(RefreshLayout refreshlayout) {
        page++;
        DreamApi.getReceiveGiftList(0x001, getActivity(), token, page, new MyCallBack() {
            @Override
            public void onSuccess(int what, Response<String> result) {
                try {
                    JSONObject jsonObject = new JSONObject(result.body());
                    int code = jsonObject.getInt("code");
                    if (code == 200) {
                        GiftListBean giftListBean = new Gson().fromJson(result.body(), GiftListBean.class);
                        List<GiftListBean.Data> datas;
                        datas = giftListBean.data;
                        if (datas == null) {
                            refreshLayout.setLoadmoreFinished(true);
                        } else {
                            receiveGiftListAdapter.addData(datas);
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

            }
        });
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        page = 1;
    }
}
