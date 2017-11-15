package com.zhuye.zhengmeng.user;

import android.content.Context;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.FrameLayout;

import com.zhuye.zhengmeng.R;
import com.zhuye.zhengmeng.base.BaseActivity;
import com.zhuye.zhengmeng.user.fragment.ReceiveFragment;
import com.zhuye.zhengmeng.user.fragment.SendFragment;
import com.zhuye.zhengmeng.view.MyAppTitle;
import com.zhuye.zhengmeng.widget.SmartTab.SmartTabLayout;
import com.zhuye.zhengmeng.widget.SmartTab.UtilsV4.v4.FragmentPagerItem;
import com.zhuye.zhengmeng.widget.SmartTab.UtilsV4.v4.FragmentPagerItemAdapter;
import com.zhuye.zhengmeng.widget.SmartTab.UtilsV4.v4.FragmentPagerItems;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * 我的礼物
 */
public class MyGiftActivity extends BaseActivity {


    @BindView(R.id.titleBar)
    MyAppTitle titleBar;
    @BindView(R.id.tab)
    FrameLayout tab;
    @BindView(R.id.viewpager)
    ViewPager viewpager;

    @Override
    protected void processLogic() {

    }

    @Override
    protected void setListener() {
        titleBar.setAppTitle("我的礼物");
        titleBar.initViewsVisible(true, true, false, false);
        titleBar.setOnLeftButtonClickListener(new MyAppTitle.OnLeftButtonClickListener() {
            @Override
            public void onLeftButtonClick(View v) {
                finish();
            }
        });
        tab.addView(LayoutInflater.from(this).inflate(R.layout.tab_top_layout, tab, false));
        SmartTabLayout viewPagerTab = (SmartTabLayout) findViewById(R.id.viewpagertab);
        FragmentPagerItems pages = new FragmentPagerItems(this);
        pages.add(FragmentPagerItem.of("我收到的", ReceiveFragment.class));
        pages.add(FragmentPagerItem.of("我送出的", SendFragment.class));
        FragmentPagerItemAdapter adapter = new FragmentPagerItemAdapter(getSupportFragmentManager(), pages);
        viewpager.setAdapter(adapter);
        viewPagerTab.setViewPager(viewpager);
    }

    @Override
    protected void loadViewLayout() {
        setContentView(R.layout.activity_my_gift);
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
}
