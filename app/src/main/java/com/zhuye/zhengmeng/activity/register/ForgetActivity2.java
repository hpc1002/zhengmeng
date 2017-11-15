package com.zhuye.zhengmeng.activity.register;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.blankj.utilcode.util.SPUtils;
import com.blankj.utilcode.util.ToastUtils;
import com.lzy.okgo.model.Response;
import com.zhuye.zhengmeng.R;
import com.zhuye.zhengmeng.base.BaseActivity;
import com.zhuye.zhengmeng.http.DreamApi;
import com.zhuye.zhengmeng.http.MyCallBack;
import com.zhuye.zhengmeng.view.MyAppTitle;

import org.json.JSONException;
import org.json.JSONObject;

import butterknife.BindView;

/**
 * Created by lilei on 2017/10/27.
 */

public class ForgetActivity2 extends BaseActivity implements View.OnClickListener {
    @BindView(R.id.button_complete)
    Button button_complete;
    @BindView(R.id.new_pwd)
    EditText new_pwd;
    @BindView(R.id.queren_pwd)
    EditText queren_pwd;

    private static final int CHANGE_PASSWORD_WHAT = 0x005;
    public static final String MOBILE = "mobile";
    public static final String VERIFY = "verify";

    private String mobile = "";
    private String verify = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    protected void processLogic() {
    }

    @Override
    protected void setListener() {
        titleBar();
        button_complete.setOnClickListener(this);
        //获取到ForgetActivity传过来的数据
        mobile = getIntent().getStringExtra(MOBILE);
        verify = getIntent().getStringExtra(VERIFY);
    }

    @Override
    protected void loadViewLayout() {
        setContentView(R.layout.activity_forget2);
    }

    @Override
    protected Context getActivityContext() {
        return null;
    }

    private void titleBar() {
        MyAppTitle mNewAppTitle = (MyAppTitle) findViewById(R.id.titleBar);
        mNewAppTitle.initViewsVisible(true, true, false, false);
        mNewAppTitle.setLeftIcon(R.mipmap.arrow_left);
        mNewAppTitle.setAppTitle("忘记密码");
        mNewAppTitle.setOnLeftButtonClickListener(new MyAppTitle.OnLeftButtonClickListener() {
            @Override
            public void onLeftButtonClick(View v) {
                finish();
            }
        });
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.button_complete:
                String new_password = new_pwd.getText().toString();
                String confirm_password = queren_pwd.getText().toString();
                if (new_password.equals("")) {
                    ToastUtils.showShort("新密码不能为空");
                } else if (new_password.length() > 6 && new_password.length() < 18) {
                    //密码格式正确
                    if (confirm_password.equals(new_password)) {
                        //两次密码正确
                        DreamApi.changePassword(this, CHANGE_PASSWORD_WHAT, mobile, verify, new_password, callBack);
                    } else {
                        ToastUtils.showShort("您两次输入的密码不一致");
                    }
                } else {
                    ToastUtils.showShort("密码不得小于6位或超过18位");
                }

                break;
            default:
                break;
        }
    }

    /**
     * 接口回调
     */
    MyCallBack callBack = new MyCallBack() {
        @Override
        public void onSuccess(int what, Response<String> result) {
            switch (what) {
                case CHANGE_PASSWORD_WHAT:
                    //找回密码接口回调
                    try {
                        JSONObject jsonObject = new JSONObject(result.body());
                        String code = jsonObject.optString("code");
                        if (code.equals("200")) {
                            //修改密码成功，将用户账号和新密码存在手机上，跳转到登录界面
                            ToastUtils.showShort(jsonObject.optString("msg"));
                            SPUtils.getInstance("userInfo").put("user_phone", mobile);
                            SPUtils.getInstance("userInfo").put("user_pwd", new_pwd.getText().toString());
                            startActivity(new Intent(ForgetActivity2.this, RegisterActivity.class));
                            finish();
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    break;
                default:
                    break;
            }
        }

        @Override
        public void onFail(int what, Response<String> result) {
            switch (what) {
                case CHANGE_PASSWORD_WHAT:
                    ToastUtils.showShort(result.body());
                    break;
                default:
                    break;
            }
        }

        @Override
        public void onFinish(int what) {

        }
    };
}
