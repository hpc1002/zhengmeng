package com.zhuye.zhengmeng.home;

import android.content.Context;
import android.support.annotation.IdRes;
import android.util.Log;
import android.widget.FrameLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import com.blankj.utilcode.util.SPUtils;
import com.tencent.rtmp.TXLiveBase;
import com.zhuye.zhengmeng.App;
import com.zhuye.zhengmeng.KTV.MessageEvent;
import com.zhuye.zhengmeng.R;
import com.zhuye.zhengmeng.base.BaseActivity;
import com.zhuye.zhengmeng.base.BaseNoActivity;
import com.zhuye.zhengmeng.home.fragment.FragmentController;
import com.zhuye.zhengmeng.http.DreamApi;
import com.zhuye.zhengmeng.utils.ToastManager;
import com.zhuye.zhengmeng.utils.UIThread;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import butterknife.BindView;

public class HomeActivity extends BaseNoActivity implements RadioGroup.OnCheckedChangeListener {


    @BindView(R.id.frame_layout)
    FrameLayout frameLayout;
    @BindView(R.id.rb_dynamic)
    RadioButton rbDynamic;
    @BindView(R.id.rb_list)
    RadioButton rbList;
    @BindView(R.id.rb_sing)
    RadioButton rbSing;
    @BindView(R.id.rb_shop)
    RadioButton rbShop;
    @BindView(R.id.rb_me)
    RadioButton rbMe;
    @BindView(R.id.hometab_radio)
    RadioGroup hometabRadio;
    private FragmentController controller;
    private static final String TAG = "HomeActivity";
    @Override
    protected void processLogic() {
    }



    @Override
    protected void setListener() {
        hometabRadio = (RadioGroup) findViewById(R.id.hometab_radio);
        hometabRadio.setOnCheckedChangeListener(this);
    }

    @Override
    protected void loadViewLayout() {
        setContentView(R.layout.activity_home);
        controller = FragmentController.getInstance(this, R.id.frame_layout);
        controller.showFragment(2);
        String sdkver = TXLiveBase.getSDKVersionStr();
        Log.d("liteavsdk", "liteav sdk version is : " + sdkver);
    }

    @Override
    protected Context getActivityContext() {
        return this;
    }

    @Override
    public void onCheckedChanged(RadioGroup radioGroup, @IdRes int checkedId) {
        switch (checkedId) {
            case R.id.rb_dynamic:
//                toolbarTitle.setText("动态");
                controller.showFragment(0);
                break;
            case R.id.rb_list:
                controller.showFragment(1);
                break;
            case R.id.rb_sing:
                controller.showFragment(2);
                break;
            case R.id.rb_shop:
                controller.showFragment(3);
                break;
            case R.id.rb_me:
                controller.showFragment(4);
                break;
            default:
                break;
        }
    }

    private boolean isDoubleClick = false;

    @Override
    public void onBackPressed() {
        if (isDoubleClick) {
            super.onBackPressed();
            App.getInstance().exit();
        } else {
            ToastManager.show("再次点击一次退出程序");
            isDoubleClick = true;
            UIThread.getInstance().postDelay(new Runnable() {
                @Override
                public void run() {
                    isDoubleClick = false;
                }
            }, 1000);
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        FragmentController.onDestroy();
    }
    @Override
    protected void onResume() {
        super.onResume();
    }
}
