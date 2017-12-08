package com.zhuye.zhengmeng.pay;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.alipay.sdk.app.PayTask;
import com.blankj.utilcode.util.SPUtils;
import com.lzy.okgo.model.Response;
import com.zhuye.zhengmeng.Constant;
import com.zhuye.zhengmeng.R;
import com.zhuye.zhengmeng.base.BaseActivity;
import com.zhuye.zhengmeng.http.DreamApi;
import com.zhuye.zhengmeng.http.MyCallBack;
import com.zhuye.zhengmeng.utils.ToastManager;
import com.zhuye.zhengmeng.view.MyAppTitle;
import com.zhuye.zhengmeng.weixin.WXPay;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;

public class PayActivity extends BaseActivity {


    private static final int SDK_PAY_FLAG = 1;
    private static final int SDK_AUTH_FLAG = 2;
    //    @BindView(R.id.actual_pay)
//    TextView actualPay;
    @BindView(R.id.rb_ali)
    RadioButton rbAli;
    @BindView(R.id.rl_ali_pay)
    RelativeLayout rlAliPay;
    @BindView(R.id.iv_wechat)
    ImageView ivWechat;
    @BindView(R.id.rb_wechat)
    RadioButton rbWechat;
    @BindView(R.id.rl_wechat_pay)
    RelativeLayout rlWechatPay;
    @BindView(R.id.btn_to_pay)
    Button btnToPay;
    @BindView(R.id.titleBar)
    MyAppTitle titleBar;
    @BindView(R.id.iv_ali)
    ImageView ivAli;
    private boolean byAli;
    private String orderId;
    private String buyType;
    private String timestamp;
    private String timestamps;
    private String out_trade_no;
    private String total_amount;
    private String nFactPrice;
    private String strOrdernum;
    private String token;
    @SuppressLint("HandlerLeak")
    private Handler mHandler = new Handler() {
        @SuppressWarnings("unused")
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case SDK_PAY_FLAG: {
                    @SuppressWarnings("unchecked")
//                    PayResult payResult = new PayResult((String) msg.obj);
                            PayResult payResult = new PayResult((Map<String, String>) msg.obj);

                    /**
                     * 对于支付结果，请商户依赖服务端的异步通知结果。同步通知结果，仅作为支付结束的通知。
                     */
                    String resultInfo = payResult.getResult();// 同步返回需要验证的信息
                    String resultStatus = payResult.getResultStatus();
                    try {
                        JSONObject jsonObject = new JSONObject(resultInfo);
                        String app_pay_response = jsonObject.getString("alipay_trade_app_pay_response");
                        JSONObject payData = new JSONObject(app_pay_response);

                        total_amount = payData.getString("total_amount");
                        out_trade_no = payData.getString("out_trade_no");
                        timestamp = payData.getString("timestamp");

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    //判断resultStatus 为9000则代表支付成功
                    if (TextUtils.equals(resultStatus, "9000")) {
                        // 该笔订单是否真实支付成功，需要依赖服务端的异步通知。
                        Toast.makeText(PayActivity.this, "支付成功", Toast.LENGTH_SHORT).show();
                        Intent intent = new Intent(PayActivity.this, PaySuccessActivity.class);
                        intent.putExtra("total_amount", total_amount);
                        intent.putExtra("out_trade_no", out_trade_no);
                        intent.putExtra("timestamp", timestamp);
                        intent.putExtra("tag", "ali");
                        startActivity(intent);
                    } else {
                        Toast.makeText(PayActivity.this, "支付失败", Toast.LENGTH_SHORT).show();
                        //                 该笔订单真实的支付结果，需要依赖服务端的异步通知。
                        Intent intent = new Intent(PayActivity.this, PayFailActivity.class);
                        startActivity(intent);
                    }
                    break;
                }
                default:
                    break;
            }
        }
    };

    @Override
    protected void processLogic() {

    }

    @Override
    protected void setListener() {
        token = SPUtils.getInstance("userInfo").getString("token");
        titleBar.initViewsVisible(true, true, false, false);
        titleBar.setAppTitle("支付");
        titleBar.setTitleSize(20);
        titleBar.setOnLeftButtonClickListener(new MyAppTitle.OnLeftButtonClickListener() {
            @Override
            public void onLeftButtonClick(View v) {
                finish();
            }
        });
        orderId = getIntent().getStringExtra("orderId");
        buyType = getIntent().getStringExtra("buyType");
        rbAli.setClickable(false);
        rbWechat.setClickable(false);
        rbAli.setChecked(true);
        rbWechat.setChecked(false);
        byAli = true;
        Button mTo_pay = (Button) findViewById(R.id.btn_to_pay);

        rlAliPay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                rbAli.setChecked(true);
                rbWechat.setChecked(false);
                byAli = true;
            }
        });
        rlWechatPay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                rbAli.setChecked(false);
                rbWechat.setChecked(true);
                byAli = false;
            }
        });
        mTo_pay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (byAli) {
//                    ToastManager.show("阿里支付");

//                        CloudApi.AliPay(0x001, Long.parseLong(orderId), 1, 0, myCallBack);
                    if (buyType.equals("gold")) {
                        DreamApi.AliPay(0x001, token, "", orderId, myCallBack);
                    } else if (buyType.equals("vip")) {
                        DreamApi.AliPay(0x001, token, orderId, "", myCallBack);
                    }
                } else {
//                    ToastManager.show("微信支付");
                    if (buyType.equals("gold")) {
                        DreamApi.WeixinPay(0x002, token, "", orderId, myCallBack);
                    } else if (buyType.equals("vip")) {
                        DreamApi.WeixinPay(0x002, token, orderId, "", myCallBack);
                    }

                }
            }
        });
    }

    private MyCallBack myCallBack = new MyCallBack() {
        @Override
        public void onSuccess(int what, Response<String> result) {
            switch (what) {
                case 0x001:
                    String body = result.body();
                    try {
                        JSONObject jsonObject = new JSONObject(body);

                         JSONObject data = jsonObject.getJSONObject("data");
                        JSONObject data1 = new JSONObject(data.toString());
                         final String payinfo = data1.getString("payinfo");
                        Runnable payRunnable = new Runnable() {
                            @Override
                            public void run() {
                                PayTask alipay = new PayTask(PayActivity.this);
//                                String result = alipay.pay(payinfo, true);
                                Map<String, String> result = alipay.payV2(payinfo, true);
                                Log.i("msp", result.toString());
                                Message msg = new Message();
                                msg.what = SDK_PAY_FLAG;
                                msg.obj = result;
                                mHandler.sendMessage(msg);
                            }
                        };
                        Thread payThread = new Thread(payRunnable);
                        payThread.start();

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    break;
                case 0x002:
                    try {
                        JSONObject jsonObject = new JSONObject(result.body());
                        int code = jsonObject.getInt("code");
                        if (code == 200) {
                            JSONObject weChatResult = jsonObject.getJSONObject("data");
                            doWXPay(weChatResult);
                            nFactPrice = weChatResult.getString("nFactPrice");

                            strOrdernum = weChatResult.getString("strOrdernum");
                            timestamps = weChatResult.getString("timestamp");

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

    private void doWXPay(JSONObject pay_param) {
        WXPay.init(this, Constant.WX_APP_ID);
        WXPay.getInstance().doPay(pay_param, new WXPay.WXPayResultCallBack() {
            @Override
            public void onSuccess() {
                Toast.makeText(getApplication(), "支付成功", Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(PayActivity.this, PaySuccessActivity.class);
                intent.putExtra("total_amount", nFactPrice + "");
                intent.putExtra("out_trade_no", strOrdernum);
                SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");//设置日期格式
                String date = df.format(new Date());// new Date()为获取当前系统时间，也可使用当前时间戳
                intent.putExtra("timestamp", date);
                intent.putExtra("tag", "wechat");
                startActivity(intent);
            }

            @Override
            public void onError(int error_code) {
                switch (error_code) {
                    case WXPay.NO_OR_LOW_WX:
                        Toast.makeText(getApplication(), "未安装微信或微信版本过低", Toast.LENGTH_SHORT).show();
                        break;

                    case WXPay.ERROR_PAY_PARAM:
                        Toast.makeText(getApplication(), "参数错误", Toast.LENGTH_SHORT).show();
                        break;

                    case WXPay.ERROR_PAY:
                        Toast.makeText(getApplication(), "支付失败", Toast.LENGTH_SHORT).show();
                        break;
                }
            }

            @Override
            public void onCancel() {
                Toast.makeText(getApplication(), "支付取消", Toast.LENGTH_SHORT).show();
//                Intent intent = new Intent(PayActivity.this, PayFailActivity.class);
//                startActivity(intent);
            }
        });
    }

    @Override
    protected void loadViewLayout() {
        setContentView(R.layout.activity_pay);
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
