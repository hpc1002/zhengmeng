package com.zhuye.zhengmeng.user;

import android.content.Context;
import android.content.Intent;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.blankj.utilcode.util.ToastUtils;
import com.zhuye.zhengmeng.R;
import com.zhuye.zhengmeng.base.BaseActivity;
import com.zhuye.zhengmeng.view.MyAppTitle;

import butterknife.BindView;

/**
 * 设置
 */
public class SettingActivity extends BaseActivity implements View.OnClickListener {

    @BindView(R.id.titleBar)
    MyAppTitle titleBar;
    @BindView(R.id.cache_size)
    TextView cacheSize;
    @BindView(R.id.arrow_1)
    ImageView arrow1;
    @BindView(R.id.rl_clearCache)
    RelativeLayout rlClearCache;
    @BindView(R.id.help_feedback)
    RelativeLayout helpFeedback;
    @BindView(R.id.about_zhengmeng)
    RelativeLayout aboutZhengmeng;
    @BindView(R.id.logout)
    RelativeLayout logout;

    @Override
    protected void processLogic() {

    }

    @Override
    protected void setListener() {
        rlClearCache.setOnClickListener(this);
        helpFeedback.setOnClickListener(this);
        aboutZhengmeng.setOnClickListener(this);
        logout.setOnClickListener(this);
        setTitle();
        try {
            cacheSize.setText(com.zhuye.zhengmeng.utils.CacheUtils.getCacheSize(this));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void setTitle() {
        titleBar.initViewsVisible(true, true, false, false);
        titleBar.setAppTitle("设置");
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
        setContentView(R.layout.activity_setting);
    }

    @Override
    protected Context getActivityContext() {
        return this;
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.rl_clearCache:
                //清除缓存
                com.zhuye.zhengmeng.utils.CacheUtils.cleanCache(this);
                ToastUtils.showShort("缓存已清除");
                cacheSize.setText("0B");
                break;
            case R.id.help_feedback:
                //帮助和反馈
                startActivity(new Intent(SettingActivity.this, HelpFeedBackActivity.class));
                break;
            case R.id.about_zhengmeng:
                //关于征梦
                startActivity(new Intent(SettingActivity.this, AboutZhengmengActivity.class));
                break;
            case R.id.logout:
                //退出登录
                break;
            default:
                break;
        }
    }
}
