package com.zhuye.zhengmeng.activity.register;

import android.animation.ObjectAnimator;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.blankj.utilcode.util.LogUtils;
import com.blankj.utilcode.util.RegexUtils;
import com.blankj.utilcode.util.SPUtils;
import com.blankj.utilcode.util.ToastUtils;
import com.lzy.okgo.model.Response;
import com.umeng.socialize.UMAuthListener;
import com.umeng.socialize.UMShareAPI;
import com.umeng.socialize.UMShareConfig;
import com.umeng.socialize.bean.SHARE_MEDIA;
import com.umeng.socialize.utils.Log;
import com.zhuye.zhengmeng.R;
import com.zhuye.zhengmeng.base.BaseActivity;
import com.zhuye.zhengmeng.home.HomeActivity;
import com.zhuye.zhengmeng.http.DreamApi;
import com.zhuye.zhengmeng.http.MyCallBack;
import com.zhuye.zhengmeng.utils.ToastManager;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Map;

import butterknife.BindView;

public class RegisterActivity extends BaseActivity implements View.OnClickListener {

    @BindView(R.id.tv_register)
    TextView tv_register;//选择注册按钮
    @BindView(R.id.tv_login)
    TextView tv_login;//选择登录按钮
    @BindView(R.id.login_rl)
    RelativeLayout login_rl;//登录的RelativeLayout
    @BindView(R.id.register_rl)
    RelativeLayout register_rl;//注册的RelativeLayout
    @BindView(R.id.button_register)
    Button register_btn;//注册按钮
    @BindView(R.id.button_login)
    Button login_btn;//登录按钮
    @BindView(R.id.forget_pas)
    TextView forget_password;
    @BindView(R.id.sanfang_ll)
    LinearLayout ll_line;//三方的横线
    @BindView(R.id.ll_button)
    LinearLayout ll_button;//三个按钮的父控件
    @BindView(R.id.qq)
    ImageView qq;
    @BindView(R.id.weixin)
    ImageView weixin;
    @BindView(R.id.weibo)
    ImageView weibo;

    @BindView(R.id.Iv_login_pic)
    ImageView Iv_login_pic;
    @BindView(R.id.Iv_register_pic)
    ImageView Iv_register_pic;

    @BindView(R.id.login_userName)
    EditText login_userName;
    @BindView(R.id.login_password)
    EditText login_password;

    @BindView(R.id.register_phone)
    EditText register_phone;
    @BindView(R.id.register_yanzhengma)
    EditText register_yanZhengma;
    @BindView(R.id.register_password)
    EditText register_password;
    @BindView(R.id.button_yanzhengma)
    Button button_yanzhengma;//验证码按钮

    private String TAG = this.getClass().getSimpleName();

    private static final int MOBILE_VERIFY_WHAT = 0x001;//注册时获取验证码
    private static final int REGISTER_WHAT = 0x002;//注册
    private static final int LOGIN_WHAT = 0x003;//登陆

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
        initLoginVisbility();
        tv_login.setOnClickListener(this);
        tv_register.setOnClickListener(this);
        forget_password.setOnClickListener(this);
        qq.setOnClickListener(this);
        weixin.setOnClickListener(this);
        weibo.setOnClickListener(this);
        button_yanzhengma.setOnClickListener(this);
        register_btn.setOnClickListener(this);
        login_btn.setOnClickListener(this);
        register_phone.addTextChangedListener(mTextWatcher);
    }

    @Override
    protected void loadViewLayout() {
        setContentView(R.layout.activity_register);
        //刚开始进来是登录
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
                button_yanzhengma.setBackgroundResource(R.drawable.register_login_button_yanzhengma_shape);
                button_yanzhengma.setEnabled(true);
                button_yanzhengma.setClickable(true);
            } else {
                button_yanzhengma.setBackgroundResource(R.drawable.register_login_button_yanzhengma_shape_nophone);
                button_yanzhengma.setClickable(false);
            }
        }
    };

    private void initLoginVisbility() {
        tv_login.setBackgroundResource(R.drawable.register_login_button_shape);
        tv_login.setTextColor(Color.parseColor("#f75153"));
        tv_register.setBackgroundResource(R.drawable.register_login_button_shape_unselect);
        tv_register.setTextColor(Color.parseColor("#ffffff"));
        login_rl.setVisibility(View.VISIBLE);
        login_btn.setVisibility(View.VISIBLE);
        register_rl.setVisibility(View.INVISIBLE);
        register_btn.setVisibility(View.INVISIBLE);
        ll_line.setVisibility(View.INVISIBLE);
        ll_button.setVisibility(View.INVISIBLE);
        upAnimation(Iv_login_pic);
        downAnimation(Iv_register_pic);
    }

    private void initRegisterVisbility() {
        tv_register.setBackgroundResource(R.drawable.register_login_button_shape);
        tv_login.setTextColor(Color.parseColor("#ffffff"));
        tv_login.setBackgroundResource(R.drawable.register_login_button_shape_unselect);
        tv_register.setTextColor(Color.parseColor("#f75153"));
        login_rl.setVisibility(View.INVISIBLE);
        login_btn.setVisibility(View.INVISIBLE);
        register_rl.setVisibility(View.VISIBLE);
        register_btn.setVisibility(View.VISIBLE);
        ll_line.setVisibility(View.VISIBLE);
        ll_button.setVisibility(View.VISIBLE);
        upAnimation(Iv_register_pic);
        downAnimation(Iv_login_pic);
    }

    //向上的动画
    private void upAnimation(ImageView imageView) {
        ObjectAnimator animator = ObjectAnimator.ofFloat(imageView, "translationY", 0f, -60f);
        animator.setDuration(800);
        animator.start();
    }

    //向下的动画
    private void downAnimation(ImageView imageView) {
        ObjectAnimator animator = ObjectAnimator.ofFloat(imageView, "translationY", 0f, 60f);
        animator.setDuration(800);
        animator.start();
    }

    @Override
    protected Context getActivityContext() {
        return null;
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.tv_login:
                initLoginVisbility();
                break;
            case R.id.tv_register:
                initRegisterVisbility();
                break;
            case R.id.button_yanzhengma:
                //获取验证码接口
                checkYanzhengma();
                break;
            case R.id.button_register:
                //注册接口
                register();
                break;
            case R.id.button_login:
                //登录接口
                login();
                break;
            case R.id.forget_pas:
                //忘记密码
                startActivity(new Intent(this, ForgetActivity.class));
                break;
            //---------------三方登录-------------//
            case R.id.qq:
                authorization(SHARE_MEDIA.QQ);
                break;
            case R.id.weixin:
                ToastUtils.showShort("weixin");
                authorization(SHARE_MEDIA.WEIXIN);
                break;
            case R.id.weibo:
                authorization(SHARE_MEDIA.SINA);
                break;
            default:
                break;
        }
    }


    /**
     * 点击获取验证码判断手机号
     */
    private void checkYanzhengma() {
        String phone = register_phone.getText().toString();
        if (RegexUtils.isMobileExact(phone)) {
            DreamApi.getSmsCode(this, MOBILE_VERIFY_WHAT, phone, callBack);
            myCountDownTimer = new MyCountDownTimer(60000, 1000);
            myCountDownTimer.start();
        } else {
            ToastUtils.showShort("您输入的手机号格式不正确");
        }
    }

    /**
     * 登录接口
     */
    private void login() {
        String phone = login_userName.getText().toString();
        String password = login_password.getText().toString();
        if (phone.equals("")) {
            ToastUtils.showShort("手机号不能为空");
        } else if (RegexUtils.isMobileExact(phone)) {
            //手机号正确
            if (password.equals("")) {
                ToastUtils.showShort("密码不能为空");
            } else {
                DreamApi.login(this, LOGIN_WHAT, "1", phone, password, "", "", "", "", callBack);
            }
        } else {
            ToastUtils.showShort("您输入的手机号格式不正确");
        }
    }

    /**
     * 注册接口
     */
    private void register() {
        String phone = register_phone.getText().toString();
        String verificationCode = register_yanZhengma.getText().toString();
        String password = register_password.getText().toString();
        if (phone.equals("")) {
            ToastUtils.showShort("手机号不能为空");
        } else if (RegexUtils.isMobileExact(phone)) {
            //手机号正确
            if (verificationCode.equals("")) {
                ToastUtils.showShort("验证码不能为空");
            } else if (verificationCode.equals(verific)) {
                //验证码正确
                if (password.length() > 6 && password.length() < 18) {
                    //密码格式正确,开始请求
                    DreamApi.register(this, REGISTER_WHAT, phone, password, verificationCode, callBack);
                } else {
                    ToastUtils.showShort("密码不得小于6位或超过18位");
                }
            } else {
                ToastUtils.showShort("验证码不正确");
            }
        } else {
            ToastUtils.showShort("您输入的手机号格式不正确");
        }
    }

    //复写倒计时
    private class MyCountDownTimer extends CountDownTimer {

        public MyCountDownTimer(long millisInFuture, long countDownInterval) {
            super(millisInFuture, countDownInterval);
        }

        //计时过程
        @Override
        public void onTick(long l) {
            //防止计时过程中重复点击
            register_phone.removeTextChangedListener(mTextWatcher);
            button_yanzhengma.setBackgroundResource(R.drawable.register_login_button_yanzhengma_shape_nophone);
            button_yanzhengma.setClickable(false);
            button_yanzhengma.setText(l / 1000 + "s");
        }

        //计时完毕的方法
        @Override
        public void onFinish() {
            //重新给Button设置文字
            button_yanzhengma.setText("重新获取");
            //设置可点击
            register_phone.addTextChangedListener(mTextWatcher);
            button_yanzhengma.setClickable(true);
            button_yanzhengma.setBackgroundResource(R.drawable.register_login_button_yanzhengma_shape);
        }
    }

    String verific = "";
    //回调
    MyCallBack callBack = new MyCallBack() {
        @Override
        public void onSuccess(int what, Response<String> result) {
            switch (what) {
                case MOBILE_VERIFY_WHAT:
                    //注册时验证码接口回调
                    try {
                        JSONObject jsonObject = new JSONObject(result.body());
                        String code = jsonObject.optString("code");
                        if (code.equals("200")) {
                            JSONObject data = jsonObject.optJSONObject("data");
                            verific = data.optString("number");
                            ToastManager.show("验证码发送成功");
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    break;
                case REGISTER_WHAT:
                    //注册回调
                    try {
                        JSONObject jsonObject = new JSONObject(result.body());
                        String code = jsonObject.optString("code");
                        if (code.equals("200")) {
                            ToastUtils.showShort("注册成功");
                            initLoginVisbility();
                            //把登录信息存起来
                            SPUtils.getInstance("userInfo").put("user_phone", register_phone.getText().toString());
                            SPUtils.getInstance("userInfo").put("user_pwd", register_password.getText().toString());
                            myCountDownTimer.onFinish();
                            button_yanzhengma.setText("验证码");
                            register_phone.setText("");
                            register_yanZhengma.setText("");
                            register_password.setText("");
                            login_userName.setText(SPUtils.getInstance("userInfo").getString("user_phone"));
                            login_password.setText(SPUtils.getInstance("userInfo").getString("user_pwd"));
                        } else if (code.equals("100")) {
                            ToastUtils.showShort("验证码错误");
                        } else if (code.equals("101")) {
                            ToastUtils.showShort("用户已存在");
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    break;
                case LOGIN_WHAT:
                    //登录接口
                    try {
                        JSONObject jsonObject = new JSONObject(result.body());
                        int code = jsonObject.optInt("code");
                        String msg = jsonObject.optString("msg");
                        if (code == 200) {
                            JSONObject login_data = jsonObject.optJSONObject("data");
                            String token = login_data.optString("token");
                            String user_nicename = login_data.optString("user_nicename");
                            String avatar = login_data.optString("avatar");
                            JSONObject RongcloudJson = login_data.optJSONObject("Rongcloud");
                            int rongcloudCode = RongcloudJson.optInt("code");
                            String userId = "";
                            String rongcloudToken = "";
                            if (rongcloudCode == 200) {
                                //获取融云信息成功
                                userId = RongcloudJson.optString("userId");
                                android.util.Log.i(TAG, "onSuccess:登录userId+"+userId);
                                rongcloudToken = RongcloudJson.optString("token");
                            }
                            android.util.Log.i(TAG, "onSuccess:登录userId+"+userId);
                            if (!token.equals("")) {
                                SPUtils.getInstance("userInfo").put("token", token);
                            }
                            SPUtils.getInstance("userInfo").put("user_nicename", user_nicename);//用户名
                            SPUtils.getInstance("userInfo").put("avatar", avatar);//头像
                            SPUtils.getInstance("userInfo").put("userId", userId);//融云Id
                            SPUtils.getInstance("userInfo").put("rongcloudToken", rongcloudToken);//融云token

                            ToastUtils.showShort(msg);
                            startActivity(new Intent(RegisterActivity.this, HomeActivity.class));
                            finish();
                        } else if (code == 102) {
                            ToastUtils.showShort("账号密码错误");
                        }
                    } catch (JSONException e) {
                        ToastUtils.showShort("账号密码错误");
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
                case MOBILE_VERIFY_WHAT:
                    ToastUtils.showShort(result.body());
                    break;
                case REGISTER_WHAT:
                    ToastUtils.showShort(result.message());
                    break;
                case LOGIN_WHAT:
                    ToastUtils.showShort(result.message());
                default:
                    break;
            }
        }

        @Override
        public void onFinish(int what) {

        }
    };


    //授权
    private void authorization(SHARE_MEDIA share_media) {
        UMShareConfig config = new UMShareConfig();
        config.isNeedAuthOnGetUserInfo(true);
        UMShareAPI.get(RegisterActivity.this).setShareConfig(config);
        UMShareAPI.get(this).getPlatformInfo(this, share_media, new UMAuthListener() {
            @Override
            public void onStart(SHARE_MEDIA share_media) {
                Log.d(TAG, "onStart " + "授权开始");
            }

            @Override
            public void onComplete(SHARE_MEDIA share_media, int i, Map<String, String> map) {
                LogUtils.e("onComplete " + "授权完成");
                String open_id = null;
                String sex;
                //sdk是6.4.4的,但是获取值的时候用的是6.2以前的(access_token)才能获取到值,未知原因
                String uid = map.get("uid");
                String openid = map.get("openid");//微博没有
                String unionid = map.get("unionid");//微博没有
                String access_token = map.get("access_token");
                String refresh_token = map.get("refresh_token");//微信,qq,微博都没有获取到
                String expires_in = map.get("expires_in");
                String name = map.get("name");                  //用户昵称
                String gender = map.get("gender");              //用户性别
                String iconurl = map.get("iconurl");            //用户头像
                if (SHARE_MEDIA.QQ == share_media || SHARE_MEDIA.WEIXIN == share_media) {
                    open_id = openid;
                } else if (SHARE_MEDIA.SINA == share_media) {
                    open_id = uid;
                }
                if (gender.equals("男")) {
                    sex = "1";
                } else if (gender.equals("女")) {
                    sex = "2";
                } else {
                    sex = "0";
                }
                //拿到信息去请求登录接口。。。
                LogUtils.e("open_id=" + open_id + "name=" + name + ",sex=" + sex + ",iconurl=" + iconurl);
                DreamApi.login(RegisterActivity.this, LOGIN_WHAT, "0", "", "", open_id, iconurl, sex, name, callBack);
            }

            @Override
            public void onError(SHARE_MEDIA share_media, int i, Throwable throwable) {
                Log.d(TAG, "onError " + "授权失败");
            }

            @Override
            public void onCancel(SHARE_MEDIA share_media, int i) {
                Log.d(TAG, "onCancel " + "授权取消");
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        UMShareAPI.get(this).onActivityResult(requestCode, resultCode, data);
    }
}
