package com.zhuye.zhengmeng.user;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.TextView;

import com.blankj.utilcode.util.SPUtils;
import com.bumptech.glide.Glide;
import com.zhuye.zhengmeng.Constant;
import com.zhuye.zhengmeng.R;
import com.zhuye.zhengmeng.base.BaseNoActivity;
import com.zhuye.zhengmeng.user.fragment.ZiLiaoFragment;
import com.zhuye.zhengmeng.user.fragment.ZuopinFragment;
import com.zhuye.zhengmeng.widget.RoundedCornerImageView;
import com.zhuye.zhengmeng.widget.SmartTab.SmartTabLayout;
import com.zhuye.zhengmeng.widget.SmartTab.UtilsV4.v4.FragmentPagerItem;
import com.zhuye.zhengmeng.widget.SmartTab.UtilsV4.v4.FragmentPagerItemAdapter;
import com.zhuye.zhengmeng.widget.SmartTab.UtilsV4.v4.FragmentPagerItems;

import butterknife.BindView;
import butterknife.ButterKnife;

import static com.zhuye.zhengmeng.R.id.user_avatar;


public class ZuoPinJiActivity extends BaseNoActivity implements View.OnClickListener {


    @BindView(R.id.tv_follow)
    TextView tvFollow;
    @BindView(R.id.tv_fans)
    TextView tvFans;
    @BindView(R.id.tv_shangbang)
    TextView tvShangbang;
    @BindView(R.id.tab)
    FrameLayout tab;
    @BindView(R.id.viewpager)
    ViewPager viewpager;
    @BindView(user_avatar)
    RoundedCornerImageView userAvatar;
    @BindView(R.id.userName)
    TextView userName;

    @Override
    protected void processLogic() {

    }

    @Override
    protected void setListener() {
        tab.addView(LayoutInflater.from(this).inflate(R.layout.tab_top_layout, tab, false));
        SmartTabLayout viewPagerTab = (SmartTabLayout) findViewById(R.id.viewpagertab);
        FragmentPagerItems pages = new FragmentPagerItems(this);
        pages.add(FragmentPagerItem.of("作品", ZuopinFragment.class));
        pages.add(FragmentPagerItem.of("资料", ZiLiaoFragment.class));
        FragmentPagerItemAdapter adapter = new FragmentPagerItemAdapter(getSupportFragmentManager(), pages);
        viewpager.setAdapter(adapter);
        viewPagerTab.setViewPager(viewpager);

        tvFollow.setOnClickListener(this);
        tvFans.setOnClickListener(this);
        tvShangbang.setOnClickListener(this);
        initUserInfo();
    }

    @Override
    protected void onResume() {
        super.onResume();
        initUserInfo();
    }

    /**
     * 从本地获取用户信息
     */
    private void initUserInfo() {
        String user_nicename = SPUtils.getInstance("userInfo").getString("user_nicename");
        String user_avatar = SPUtils.getInstance("userInfo").getString("avatar");
        String attention_sum = SPUtils.getInstance("userInfo").getString("attention_sum");//关注数
        String fans_sum = SPUtils.getInstance("userInfo").getString("fans_sum");//粉丝数
        //加载信息
        Glide.with(this).
                load(Constant.BASE_URL_PINJIE + user_avatar)
//        load(Constant.BASE_URL_PINJIE + avatar)
                .into(userAvatar);
        userName.setText(user_nicename);
        tvFollow.setText("关注 " + attention_sum);
        tvFans.setText("粉丝 " + fans_sum);
    }

    @Override
    protected void loadViewLayout() {
        setContentView(R.layout.activity_zuo_pin_ji);
    }

    @Override
    protected Context getActivityContext() {
        return this;
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.tv_fans:
                startActivity(new Intent(this, MyFansActivity.class));
                break;
            case R.id.tv_follow:
                startActivity(new Intent(this, MyFollowActivity.class));
                break;
            case R.id.tv_shangbang:
                startActivity(new Intent(this, RankRecordActivity.class));
                break;
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // TODO: add setContentView(...) invocation
        ButterKnife.bind(this);
    }
}
