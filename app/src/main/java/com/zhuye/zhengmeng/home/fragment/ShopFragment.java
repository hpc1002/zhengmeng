package com.zhuye.zhengmeng.home.fragment;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.CookieManager;
import android.webkit.CookieSyncManager;
import android.webkit.ValueCallback;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;

import com.blankj.utilcode.util.SPUtils;
import com.lzy.okgo.cookie.store.SPCookieStore;
import com.lzy.okgo.model.Response;
import com.zhuye.zhengmeng.Constant;
import com.zhuye.zhengmeng.R;
import com.zhuye.zhengmeng.base.BaseFragment;
import com.zhuye.zhengmeng.http.DreamApi;
import com.zhuye.zhengmeng.http.MyCallBack;
import com.zhuye.zhengmeng.view.MyAppTitle;

import org.json.JSONException;
import org.json.JSONObject;

import butterknife.BindView;
import okhttp3.Cookie;

/**
 * Created by hpc on 2017/10/26.
 */

public class ShopFragment extends BaseFragment {
    @BindView(R.id.webView)
    WebView webView;
    @BindView(R.id.titleBar)
    MyAppTitle titleBar;
    private String token;
    private String cookie;
    private static final int FILE_SELECT_CODE = 0;
    private ValueCallback<Uri> mUploadMessage;//回调图片选择，4.4以下
    private ValueCallback<Uri[]> mUploadCallbackAboveL;//回调图片选择，5.0以上

    @Override
    protected View initView(LayoutInflater inflater, ViewGroup container) {
        return inflater.inflate(R.layout.fragment_shop, container, false);
    }

    @Override
    protected void initListener() {
        setShopTitle();
        token = SPUtils.getInstance("userInfo").getString("token");

    }

    private void initUi(String url) {
        WebSettings webSettings = webView.getSettings();
        //设置WebView属性，能够执行Javascript脚本
        webSettings.setJavaScriptEnabled(true);
        webSettings.setLoadsImagesAutomatically(true);
        webView.setVerticalScrollBarEnabled(false);
        //运行webview通过URI获取安卓文件
        webSettings.setAllowFileAccess(true);
        webSettings.setAllowFileAccessFromFileURLs(true);
        webSettings.setAllowUniversalAccessFromFileURLs(true);

        webView.setWebChromeClient(new MyWebChromeClient());//设置可以打开图片管理器
        if (!cookie.equals("")) {
//            Cookie cookie = new Cookie("PHPSESSID",token,"");
            synCookies(getActivity(), url, cookie);

//            synchronousWebCookies(getActivity(), url, cookie);
            webView.loadUrl(url);
        }

    }

    public static void synchronousWebCookies(Context context, String url, String cookies) {
        if (!TextUtils.isEmpty(url))
            if (!TextUtils.isEmpty(cookies)) {
                if (Build.VERSION.SDK_INT < Build.VERSION_CODES.LOLLIPOP) {
                    CookieSyncManager.createInstance(context);
                }
                CookieManager cookieManager = CookieManager.getInstance();
                cookieManager.setAcceptCookie(true);
                cookieManager.removeSessionCookie();// 移除
                cookieManager.removeAllCookie();
                StringBuilder sbCookie = new StringBuilder();//创建一个拼接cookie的容器,为什么这么拼接，大家查阅一下http头Cookie的结构
                sbCookie.append(cookies);//拼接sessionId
//                 sbCookie.append(String.format(";domain=%s", ""));
//                 sbCookie.append(String.format(";path=%s", ""));
                String cookieValue = sbCookie.toString();
                cookieManager.setCookie(url, cookieValue);//为url设置cookie
                CookieSyncManager.getInstance().sync();//同步cookie
                String newCookie = cookieManager.getCookie(url);

            }
    }

    private class MyWebChromeClient extends WebChromeClient {

        // For Android 3.0+
        public void openFileChooser(ValueCallback<Uri> uploadMsg) {

            mUploadMessage = uploadMsg;
            Intent i = new Intent(Intent.ACTION_GET_CONTENT);
            i.addCategory(Intent.CATEGORY_OPENABLE);
            i.setType("image/*");
            startActivityForResult(Intent.createChooser(i, "File Chooser"), FILE_SELECT_CODE);

        }

        // For Android 3.0+
        public void openFileChooser(ValueCallback uploadMsg, String acceptType) {
            mUploadMessage = uploadMsg;
            Intent i = new Intent(Intent.ACTION_GET_CONTENT);
            i.addCategory(Intent.CATEGORY_OPENABLE);
            i.setType("*/*");
            startActivityForResult(Intent.createChooser(i, "File Browser"), FILE_SELECT_CODE);
        }

        // For Android 4.1
        public void openFileChooser(ValueCallback<Uri> uploadMsg, String acceptType, String capture) {
            mUploadMessage = uploadMsg;
            Intent i = new Intent(Intent.ACTION_GET_CONTENT);
            i.addCategory(Intent.CATEGORY_OPENABLE);
            i.setType("image/*");
            startActivityForResult(Intent.createChooser(i, "File Chooser"), FILE_SELECT_CODE);

        }

        // For Android 5.0+
        public boolean onShowFileChooser(WebView webView, ValueCallback<Uri[]> filePathCallback, WebChromeClient.FileChooserParams fileChooserParams) {
            mUploadCallbackAboveL = filePathCallback;
            Intent i = new Intent(Intent.ACTION_GET_CONTENT);
            i.addCategory(Intent.CATEGORY_OPENABLE);
            i.setType("*/*");
            startActivityForResult(
                    Intent.createChooser(i, "File Browser"),
                    FILE_SELECT_CODE);
            return true;
        }


    }

    /**
     * {
     * "data": {
     * "url": "changba/api/gift/shop_list"
     * },
     * "msg": "",
     * "code": 200
     * }
     */
    @Override
    protected void initData() {
        token = SPUtils.getInstance("userInfo").getString("token");
        if (!token.equals("")) {
            DreamApi.getShopWebUrl(0x001, token, myCallBack);
        }

    }

    private void setShopTitle() {
        titleBar.initViewsVisible(false, true, false, false);
        titleBar.setAppTitle("商城");
        titleBar.setTitleSize(20);
    }

    private MyCallBack myCallBack = new MyCallBack() {
        @Override
        public void onSuccess(int what, Response<String> result) {
            switch (what) {
                case 0x001:
                    try {
                        JSONObject jsonObject = new JSONObject(result.body());
                        int code = jsonObject.getInt("code");
                        if (code == 200) {
                            JSONObject data = jsonObject.getJSONObject("data");
                            String url = data.getString("url");
//                            webView.loadUrl(Constant.BASE_URL + url + "?token=" + token);
//                            initUi(Constant.BASE_URL + url + "?token=" + token);
                            initUi(Constant.BASE_URL + url);
                            webView.setWebViewClient(new MyWebViewClient());
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

    public class MyWebViewClient extends android.webkit.WebViewClient {

        @Override
        public void onPageFinished(WebView view, String url) {
            super.onPageFinished(view, url);
            view.loadUrl("javascript:(function() { " +
                    "document.getElementsByClassName('footer')[0].style.display='none'; " +
                    "})()");
        }
//        @SuppressLint("NewApi")
//        @Override
//        public WebResourceResponse shouldInterceptRequest(WebView view, String url) {
//            String ajaxUrl = url;
//            // 如标识:req=ajax
//            if (url.contains("req=ajax")) {
//                ajaxUrl += "&imei=" + ajaxUrl;
//            }
//
//            return super.shouldInterceptRequest(view, ajaxUrl);
//        }
    }

    /**
     * 同步cookie
     *
     * @param context
     * @param url
     */
    public static void synCookies(Context context, String url, String cookieValue) {
        CookieSyncManager.createInstance(context);
        CookieManager cookieManager = CookieManager.getInstance();
        cookieManager.setCookie(url, cookieValue);
        CookieSyncManager.getInstance().sync();
    }

    @Override
    public void onHiddenChanged(boolean hidden) {
        super.onHiddenChanged(hidden);
        cookie = SPUtils.getInstance("userInfo").getString("cookie");
        DreamApi.getShopWebUrl(0x001, token, myCallBack);
    }
}
