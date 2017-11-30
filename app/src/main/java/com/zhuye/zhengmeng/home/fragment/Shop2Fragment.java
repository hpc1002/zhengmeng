package com.zhuye.zhengmeng.home.fragment;

import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;

import com.blankj.utilcode.util.SPUtils;
import com.lzy.okgo.model.Response;
import com.zhuye.zhengmeng.R;
import com.zhuye.zhengmeng.base.BaseFragment;
import com.zhuye.zhengmeng.http.MyCallBack;
import com.zhuye.zhengmeng.view.MyAppTitle;

import org.json.JSONException;
import org.json.JSONObject;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;
import cn.bingoogolapple.bgabanner.BGABanner;

/**
 * Created by hpc on 2017/10/26.
 */

public class Shop2Fragment extends BaseFragment {

    @BindView(R.id.titleBar)
    MyAppTitle titleBar;
    @BindView(R.id.shop_banner)
    BGABanner shopBanner;
    @BindView(R.id.tab)
    FrameLayout tab;
    @BindView(R.id.viewpager)
    ViewPager viewpager;
    Unbinder unbinder;
    private String token;


    @Override
    protected View initView(LayoutInflater inflater, ViewGroup container) {
        return inflater.inflate(R.layout.fragment_shop2, container, false);
    }

    @Override
    protected void initListener() {
        setShopTitle();
        token = SPUtils.getInstance("userInfo").getString("token");

    }

    @Override
    protected void initData() {

    }


    private void setShopTitle() {
        titleBar.initViewsVisible(false, true, false, false);
        titleBar.setAppTitle("商城");
        titleBar.setTitleSize(20);
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
                            JSONObject data = jsonObject.getJSONObject("data");

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

        }
    };


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
}
