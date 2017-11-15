package com.zhuye.zhengmeng.activity.lanuch;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.TextView;

import com.blankj.utilcode.util.SPUtils;
import com.zhuye.zhengmeng.R;
import com.zhuye.zhengmeng.activity.lanuch.timer.BaseTimerTask;
import com.zhuye.zhengmeng.activity.lanuch.timer.ITimerListener;
import com.zhuye.zhengmeng.activity.register.RegisterActivity;
import com.zhuye.zhengmeng.home.HomeActivity;

import java.text.MessageFormat;
import java.util.Timer;

/**
 * Created by ll on 2017/10/11.
 * desc 闪屏页
 */

public class SplashActivity extends AppCompatActivity implements View.OnClickListener, ITimerListener {

    private Timer mTimer = null;
    private int mCount = 1;
    private TextView tv_launcher_timer;

    private ILauncherListener mILauncherListener = null;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.splash_launcher);
        //初始化倒计时控件
        tv_launcher_timer = (TextView) findViewById(R.id.tv_launcher_timer);
        tv_launcher_timer.setOnClickListener(this);
        //开始倒计时
        initTimer();
    }

    private void initTimer() {
        mTimer = new Timer();
        final BaseTimerTask task = new BaseTimerTask(this);
        mTimer.schedule(task, 0, 1000);
    }

    @Override
    public void onClick(View view) {
        if (mTimer != null) {
            mTimer.cancel();
            mTimer = null;
            //检查是否第一次进入APP，如果是显示轮播图，不是进入首页
            checkIsShowScroll();
        }
    }


    //判断是否显示滑动启动页
    private void checkIsShowScroll() {
        Intent intent;
        boolean is_guide_show = SPUtils.getInstance().getBoolean("is_guide_show", false);
        if (is_guide_show) {
            //已经做过新手导航，判断用户是否登陆
            String token = SPUtils.getInstance("userInfo").getString("token");
            if (!token.equals("")){
                intent = new Intent(this,HomeActivity.class);
            } else {
                intent = new Intent(this, RegisterActivity.class);
            }
        } else {
            //没有做过新手导航
            intent = new Intent(SplashActivity.this, GuideActivity.class);
        }
        startActivity(intent);
        this.finish();
    }

    @Override
    public void onTimer() {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                if (tv_launcher_timer != null) {
                    tv_launcher_timer.setText(MessageFormat.format("跳过\n{0}s", mCount));
                    mCount--;
                    if (mCount < 0) {
                        if (mTimer != null) {
                            mTimer.cancel();
                            mTimer = null;
                            //检查是否第一次进入APP，如果是显示轮播图，不是进入首页
                            checkIsShowScroll();
                        }
                    }
                }
            }
        });
    }
}
