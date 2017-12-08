package com.zhuye.zhengmeng.weixin;

import android.content.Context;
import android.text.TextUtils;
import android.util.Log;

import com.tencent.mm.opensdk.constants.Build;
import com.tencent.mm.opensdk.modelpay.PayReq;
import com.tencent.mm.opensdk.openapi.IWXAPI;
import com.tencent.mm.opensdk.openapi.WXAPIFactory;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * 微信支付
 * Created by tsy on 16/6/1.
 */
public class WXPay {

    private static WXPay mWXPay;
    private IWXAPI mWXApi;
    private JSONObject mPayParam;
    private WXPayResultCallBack mCallback;

    public static final int NO_OR_LOW_WX = 1;   //未安装微信或微信版本过低
    public static final int ERROR_PAY_PARAM = 2;  //支付参数错误
    public static final int ERROR_PAY = 3;  //支付失败

    public interface WXPayResultCallBack {
        void onSuccess(); //支付成功

        void onError(int error_code);   //支付失败

        void onCancel();    //支付取消
    }

    public WXPay(Context context, String wx_appid) {
        mWXApi = WXAPIFactory.createWXAPI(context, null);
        mWXApi.registerApp(wx_appid);
    }

    public static void init(Context context, String wx_appid) {
        if (mWXPay == null) {
            mWXPay = new WXPay(context, wx_appid);
        }
    }

    public static WXPay getInstance() {
        return mWXPay;
    }

    public IWXAPI getWXApi() {
        return mWXApi;
    }

    /**
     * 发起微信支付
     */
    public void doPay(JSONObject pay_param, WXPayResultCallBack callback) {
        mPayParam = pay_param;
        mCallback = callback;

        if (!check()) {
            if (mCallback != null) {
                mCallback.onError(NO_OR_LOW_WX);
            }
            return;
        }
        try {
            String appid = mPayParam.getString("appid");
            String noncestr = mPayParam.getString("nonce_str");
            String timestamp = mPayParam.getString("timestamp");
//            String packages = mPayParam.getString("package");
            String packages = "Sign=WXPay";
            String prepayid = mPayParam.getString("prepay_id");
            String sign = mPayParam.getString("sign");
            String partnerid = mPayParam.getString("mch_id");
            if (TextUtils.isEmpty(appid) || TextUtils.isEmpty(partnerid)
                    || TextUtils.isEmpty(prepayid) || TextUtils.isEmpty(packages) ||
                    TextUtils.isEmpty(noncestr) || TextUtils.isEmpty(timestamp) ||
                    TextUtils.isEmpty(sign)) {
                if (mCallback != null) {
                    mCallback.onError(ERROR_PAY_PARAM);
                }
                return;
            }
            PayReq req = new PayReq();
            req.appId = appid;
            req.nonceStr = noncestr;
            req.partnerId = partnerid;
            req.prepayId = prepayid;
            req.packageValue = packages;
            req.timeStamp = timestamp;
            req.sign = sign;

            mWXApi.sendReq(req);
        } catch (JSONException e) {
            e.printStackTrace();
        }


    }

    //支付回调响应
    public void onResp(int error_code) {
        Log.i("status====mCallback----", mCallback + "");
        if (mCallback == null) {
            return;
        }

        if (error_code == 0) {   //成功
            mCallback.onSuccess();
        } else if (error_code == -1) {   //错误
            mCallback.onError(ERROR_PAY);
        } else if (error_code == -2) {   //取消
            mCallback.onCancel();
        }

        mCallback = null;
    }

    //检测是否支持微信支付
    private boolean check() {
        return mWXApi.isWXAppInstalled() && mWXApi.getWXAppSupportAPI() >= Build.PAY_SUPPORTED_SDK_INT;
    }
}
