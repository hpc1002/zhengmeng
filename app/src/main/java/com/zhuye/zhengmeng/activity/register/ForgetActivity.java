package com.zhuye.zhengmeng.activity.register;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.blankj.utilcode.util.RegexUtils;
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
 * 忘记密码发送验证码界面
 */

public class ForgetActivity extends BaseActivity implements View.OnClickListener {

    @BindView(R.id.button_next)
    Button button_next;//下一步
    @BindView(R.id.forget_phone)
    EditText forget_phone;
    @BindView(R.id.forget_yanzhengma)
    EditText forget_yanzhengma;
    @BindView(R.id.button_yanzhengma_forget)
    Button button_yanzhengma_forget;

    private static final int FORGET_PHONE_WHAT = 0x004;
    private static final int VERIFIC_CODE_WHAT = 0x005;
    private MyCountDownTimer myCountDownTimer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    protected void processLogic() {

    }

    @Override
    protected void setListener() {
        setForgetTiltle();
        button_next.setOnClickListener(this);
        button_yanzhengma_forget.setOnClickListener(this);
        forget_phone.setOnClickListener(this);
        forget_yanzhengma.setOnClickListener(this);
        forget_phone.addTextChangedListener(mTextWatcher);
    }

    @Override
    protected void loadViewLayout() {
        setContentView(R.layout.activity_forget);
    }

    @Override
    protected Context getActivityContext() {
        return null;
    }

    private void setForgetTiltle() {
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
            case R.id.button_yanzhengma_forget:
                checkYanzhengma();
                break;
            case R.id.button_next:
                nextStep();//点击下一步按钮
                break;
            default:
                break;
        }
    }

    private void nextStep() {
        String phone = forget_phone.getText().toString();
        String verificationCode = forget_yanzhengma.getText().toString();
        if (phone.equals("")) {
            ToastUtils.showShort("手机号不能为空");
        } else if (RegexUtils.isMobileExact(phone)) {
            if (verificationCode.equals("")) {
                ToastUtils.showShort("验证码不能为空");
            } else if (verificationCode.equals(forgetVerific)) {
                //验证码正确，调验证验证码的接口
                DreamApi.verificCode(VERIFIC_CODE_WHAT, phone, verificationCode, callBack);
            } else {
                ToastUtils.showShort("验证码不正确");
            }
        } else {
            ToastUtils.showShort("您输入的手机号格式不正确");
        }
    }

    /**
     * 点击获取验证码判断手机号
     */
    private void checkYanzhengma() {
        String phone = forget_phone.getText().toString();
        if (phone.equals("")) {
            ToastUtils.showShort("手机号不能为空");
        } else if (RegexUtils.isMobileExact(phone)) {
            DreamApi.getSmsCode(this, FORGET_PHONE_WHAT,phone, callBack);
            myCountDownTimer = new MyCountDownTimer(60000, 1000);
            myCountDownTimer.start();
        } else {
            ToastUtils.showShort("您输入的手机号格式不正确");
        }
    }

    //监听输入的手机号
    TextWatcher mTextWatcher = new TextWatcher() {
        private CharSequence temp;

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {
            temp = s;
        }

        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {
        }

        @Override
        public void afterTextChanged(Editable s) {
            if (temp.length() == 11) {//改变验证码背景颜色
                button_yanzhengma_forget.setBackgroundResource(R.drawable.register_login_button_yanzhengma_shape);
                button_yanzhengma_forget.setEnabled(true);
                button_yanzhengma_forget.setClickable(true);
            } else {
                button_yanzhengma_forget.setBackgroundResource(R.drawable.register_login_button_yanzhengma_shape_nophone);
                button_yanzhengma_forget.setClickable(false);
            }
        }
    };

    //复写倒计时
    private class MyCountDownTimer extends CountDownTimer {

        public MyCountDownTimer(long millisInFuture, long countDownInterval) {
            super(millisInFuture, countDownInterval);
        }

        //计时过程
        @Override
        public void onTick(long l) {
            //防止计时过程中重复点击
            forget_phone.removeTextChangedListener(mTextWatcher);
            button_yanzhengma_forget.setBackgroundResource(R.drawable.register_login_button_yanzhengma_shape_nophone);
            button_yanzhengma_forget.setClickable(false);
            button_yanzhengma_forget.setText(l / 1000 + "s");
        }

        //计时完毕的方法
        @Override
        public void onFinish() {
            //重新给Button设置文字
            button_yanzhengma_forget.setText("重新获取");
            //设置可点击
            button_yanzhengma_forget.addTextChangedListener(mTextWatcher);
            button_yanzhengma_forget.setClickable(true);
            button_yanzhengma_forget.setBackgroundResource(R.drawable.register_login_button_yanzhengma_shape);
        }
    }

    private String forgetVerific = "";
    /**
     * 接口回调
     */
    MyCallBack callBack = new MyCallBack() {
        @Override
        public void onSuccess(int what, Response<String> result) {
            switch (what) {
                case FORGET_PHONE_WHAT:
                    //忘记密码验证码接口回调
                    try {
                        JSONObject jsonObject = new JSONObject(result.body());
                        String code = jsonObject.optString("code");
                        if (code.equals("200")) {
                            JSONObject data = jsonObject.optJSONObject("data");
                            forgetVerific = data.optString("number");
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    break;
                case VERIFIC_CODE_WHAT:
                    try {
                        JSONObject jsonObject = new JSONObject(result.body());
                        String code = jsonObject.optString("code");
                        if (code.equals("200")) {
                            //验证码正确
                            Intent intent = new Intent(ForgetActivity.this, ForgetActivity2.class);
                            intent.putExtra(ForgetActivity2.MOBILE, forget_phone.getText().toString());
                            intent.putExtra(ForgetActivity2.VERIFY, forget_yanzhengma.getText().toString());
                            startActivity(intent);
                        } else if (code.equals("100")) {
                            ToastUtils.showShort("验证码错误");
                        } else if (code.equals("101")) {
                            ToastUtils.showShort("用户已存在");
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
                case FORGET_PHONE_WHAT:
                    ToastUtils.showShort(result.body());
                    break;
                case VERIFIC_CODE_WHAT:
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
