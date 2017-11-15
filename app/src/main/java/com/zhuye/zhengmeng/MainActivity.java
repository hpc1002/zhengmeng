package com.zhuye.zhengmeng;

import android.content.Context;

import com.zhuye.zhengmeng.base.BaseActivity;


public class MainActivity extends BaseActivity {

    @Override
    protected void processLogic() {

    }

    @Override
    protected void setListener() {

    }

    @Override
    protected void loadViewLayout() {
        setContentView(R.layout.activity_main);
    }

    @Override
    protected Context getActivityContext() {
        return this;
    }
}
