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
import com.zhuye.zhengmeng.shop.fragment.ClothShopFragment;
import com.zhuye.zhengmeng.shop.fragment.GiftShopFragment;
import com.zhuye.zhengmeng.user.fragment.ReceiveFragment;
import com.zhuye.zhengmeng.user.fragment.SendFragment;
import com.zhuye.zhengmeng.view.MyAppTitle;
import com.zhuye.zhengmeng.widget.SmartTab.SmartTabLayout;
import com.zhuye.zhengmeng.widget.SmartTab.UtilsV4.v4.FragmentPagerItem;
import com.zhuye.zhengmeng.widget.SmartTab.UtilsV4.v4.FragmentPagerItemAdapter;
import com.zhuye.zhengmeng.widget.SmartTab.UtilsV4.v4.FragmentPagerItems;

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

    private View view;
    @Override
    protected View initView(LayoutInflater inflater, ViewGroup container) {

        view = inflater.inflate(R.layout.fragment_shop2, container, false);
        return view;
    }

    @Override
    protected void initListener() {
        setShopTitle();
        token = SPUtils.getInstance("userInfo").getString("token");
        tab.addView(LayoutInflater.from(getActivity()).inflate(R.layout.tab_top_layout, tab, false));
        SmartTabLayout viewPagerTab = (SmartTabLayout) view.findViewById(R.id.viewpagertab);
        FragmentPagerItems pages = new FragmentPagerItems(getActivity());
        pages.add(FragmentPagerItem.of("礼物", GiftShopFragment.class));
        pages.add(FragmentPagerItem.of("服装", ClothShopFragment.class));
//        pages.add(FragmentPagerItem.of("服装", GiftShopFragment.class));
        FragmentPagerItemAdapter adapter = new FragmentPagerItemAdapter(getChildFragmentManager(), pages);
        viewpager.setAdapter(adapter);
        viewPagerTab.setViewPager(viewpager);
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
