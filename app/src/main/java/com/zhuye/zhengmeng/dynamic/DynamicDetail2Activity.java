package com.zhuye.zhengmeng.dynamic;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.webkit.CookieManager;
import android.webkit.CookieSyncManager;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.blankj.utilcode.util.SPUtils;
import com.bumptech.glide.Glide;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.flyco.dialog.listener.OnBtnClickL;
import com.flyco.dialog.listener.OnOperItemClickL;
import com.flyco.dialog.widget.ActionSheetDialog;
import com.flyco.dialog.widget.NormalDialog;
import com.google.gson.Gson;
import com.jude.easyrecyclerview.EasyRecyclerView;
import com.ksyun.media.player.IMediaPlayer;
import com.ksyun.media.player.KSYMediaPlayer;
import com.longsh.optionframelibrary.OptionCenterDialog;
import com.lzy.okgo.model.Response;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnLoadmoreListener;
import com.scwang.smartrefresh.layout.listener.OnRefreshListener;
import com.umeng.socialize.ShareAction;
import com.umeng.socialize.UMShareAPI;
import com.umeng.socialize.UMShareListener;
import com.umeng.socialize.bean.SHARE_MEDIA;
import com.umeng.socialize.media.UMImage;
import com.umeng.socialize.media.UMWeb;
import com.umeng.socialize.utils.Log;
import com.zhuye.zhengmeng.Constant;
import com.zhuye.zhengmeng.R;
import com.zhuye.zhengmeng.bangdan.SongsListActivity;
import com.zhuye.zhengmeng.bangdan.recording.QAudioActivity;
import com.zhuye.zhengmeng.bangdan.recording.QVideoActivity;
import com.zhuye.zhengmeng.base.BaseActivity;
import com.zhuye.zhengmeng.dynamic.adapter.CommentListAdapter;
import com.zhuye.zhengmeng.dynamic.bean.CommentListBean;
import com.zhuye.zhengmeng.dynamic.bean.FansContributionBean;
import com.zhuye.zhengmeng.http.DreamApi;
import com.zhuye.zhengmeng.http.MyCallBack;
import com.zhuye.zhengmeng.shop.adapter.GiftDetailPackageAdapter;
import com.zhuye.zhengmeng.shop.adapter.GiftDetailShopAdapter;
import com.zhuye.zhengmeng.shop.bean.GiftDetailBean;
import com.zhuye.zhengmeng.user.MyGoldActivity;
import com.zhuye.zhengmeng.user.ZuoPinJiActivity;
import com.zhuye.zhengmeng.user.adapter.FansContributionAdapter;
import com.zhuye.zhengmeng.utils.ToastManager;
import com.zhuye.zhengmeng.widget.RoundedCornerImageView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import cn.jzvd.JZVideoPlayerStandard;

import static com.zhuye.zhengmeng.R.id.song_name;

public class DynamicDetail2Activity extends BaseActivity implements OnRefreshListener, OnLoadmoreListener, View.OnClickListener {

    @BindView(R.id.videoplayer)
    JZVideoPlayerStandard videoplayer;
    @BindView(R.id.avatar)
    RoundedCornerImageView avatar;
    @BindView(R.id.nickname)
    TextView nickname;
    @BindView(R.id.tv_fans_number)
    TextView tvFansNumber;
    @BindView(R.id.follow_state)
    TextView followState;
    @BindView(R.id.tv_collect)
    TextView tvCollect;
    @BindView(R.id.sign_name)
    TextView signName;
    @BindView(R.id.recyclerView)
    EasyRecyclerView recyclerView;
    @BindView(R.id.refreshLayout)
    SmartRefreshLayout refreshLayout;
    @BindView(R.id.ll_bottom)
    LinearLayout llBottom;
    @BindView(R.id.rb_gift)
    TextView rbGift;
    @BindView(R.id.rb_comment)
    TextView rbComment;
    @BindView(R.id.rb_zhuanfa)
    TextView rbZhuanfa;
    @BindView(R.id.rb_perform)
    TextView rbPerform;
    @BindView(R.id.gridRecyclerView)
    EasyRecyclerView gridRecyclerView;
    @BindView(R.id.tv_gift_count)
    TextView tvGiftCount;
    @BindView(R.id.player_surface)
    SurfaceView playerSurface;
    @BindView(R.id.user_detail)
    RelativeLayout userDetail;
    private String production_id;
    private CommentListAdapter commentListAdapter;
    private String token;
    private PopupWindow popWindow;
    private PopupWindow popGiftWindow;
    private RecyclerView giftRecyclerView;
    FansContributionAdapter fansContributionAdapter;
    int page = 1;
    private KSYMediaPlayer ksyMediaPlayer;
    private SurfaceHolder surfaceHolder;

    private UMShareListener mShareListener;
    private ShareAction mShareAction;
    private GiftDetailBean giftDetailBean;
    private String production_path;
    private String production_name;
    private String production_content;
    private String production_img;
    private NormalDialog dialog;
    private IMediaPlayer.OnPreparedListener onPreparedListener = new IMediaPlayer.OnPreparedListener() {
        @Override
        public void onPrepared(IMediaPlayer mp) {
            ksyMediaPlayer.setVideoScalingMode(KSYMediaPlayer.VIDEO_SCALING_MODE_SCALE_TO_FIT_WITH_CROPPING);
            ksyMediaPlayer.start();
        }
    };

    private final SurfaceHolder.Callback surfaceCallback = new SurfaceHolder.Callback() {
        @Override
        public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {
            if (ksyMediaPlayer != null && ksyMediaPlayer.isPlaying())
                ksyMediaPlayer.setVideoScalingMode(KSYMediaPlayer.VIDEO_SCALING_MODE_SCALE_TO_FIT_WITH_CROPPING);
        }

        @Override
        public void surfaceCreated(SurfaceHolder holder) {
            if (ksyMediaPlayer != null)
                ksyMediaPlayer.setDisplay(holder);
        }

        @Override
        public void surfaceDestroyed(SurfaceHolder holder) {
            if (ksyMediaPlayer != null) {
                ksyMediaPlayer.setDisplay(null);
            }
        }
    };

    @Override
    protected void processLogic() {

    }

    @Override
    protected void setListener() {
        ksyMediaPlayer = new KSYMediaPlayer.Builder(this).build();
        ksyMediaPlayer.setOnPreparedListener(onPreparedListener);
        ksyMediaPlayer.setScreenOnWhilePlaying(true);
        ksyMediaPlayer.setBufferTimeMax(5);
        ksyMediaPlayer.setTimeout(20, 100);
        production_id = getIntent().getStringExtra("production_id");

        production_path = getIntent().getStringExtra("production_path");
        production_name = getIntent().getStringExtra("production_name");
        production_img = getIntent().getStringExtra("production_img");
        production_content = getIntent().getStringExtra("production_content");

        token = SPUtils.getInstance("userInfo").getString("token");

        refreshLayout.autoRefresh();
        refreshLayout.setOnRefreshListener(this);
        refreshLayout.setOnLoadmoreListener(this);
        DreamApi.getDynamicDetail(0x002, token, production_id, myCallBack);
        DreamApi.getFansNumber(0x005, token, production_id, myCallBack);
        DreamApi.getFansContribution(0x006, token, "638", myCallBack);
        String url = Constant.BASE_URL2 + production_path;
//        playShow(url);
        videoplayer.setUp(Constant.BASE_URL2 + production_path
                , JZVideoPlayerStandard.SCREEN_LAYOUT_NORMAL, production_name);
        videoplayer.startVideo();
        rbGift.setOnClickListener(this);
        rbComment.setOnClickListener(this);
        rbZhuanfa.setOnClickListener(this);
        rbPerform.setOnClickListener(this);

        mShareListener = new CustomShareListener(this);
        UMWeb web = new UMWeb(Constant.BASE_URL2 + production_path);
        web.setTitle(production_name);//标题

        web.setThumb(new UMImage(DynamicDetail2Activity.this, production_img));  //缩略图
//        web.setDescription(Defaultcontent.text);//描述
        /*无自定按钮的分享面板*/
        mShareAction = new ShareAction(DynamicDetail2Activity.this).setDisplayList(
                SHARE_MEDIA.WEIXIN, SHARE_MEDIA.WEIXIN_CIRCLE, SHARE_MEDIA.WEIXIN_FAVORITE,
                SHARE_MEDIA.SINA, SHARE_MEDIA.QQ, SHARE_MEDIA.QZONE)
//                .withText(Defaultcontent.text + "来自友盟自定义分享面板")
                .withMedia(web)
                .setCallback(mShareListener);
        //收藏
//        tvCollect.setBackground(getResources().getDrawable(R.mipmap.collect));
        tvCollect.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DreamApi.addCollectList(0x011, token, production_id, myCallBack);
            }
        });
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // TODO: add setContentView(...) invocation
        ButterKnife.bind(this);
    }

    private static class CustomShareListener implements UMShareListener {

        private WeakReference<DynamicDetail2Activity> mActivity;

        private CustomShareListener(DynamicDetail2Activity activity) {
            mActivity = new WeakReference(activity);
        }

        @Override
        public void onStart(SHARE_MEDIA platform) {

        }

        @Override
        public void onResult(SHARE_MEDIA platform) {

            if (platform.name().equals("WEIXIN_FAVORITE")) {
                Toast.makeText(mActivity.get(), platform + " 收藏成功啦", Toast.LENGTH_SHORT).show();
            } else {
                if (platform != SHARE_MEDIA.MORE && platform != SHARE_MEDIA.SMS
                        && platform != SHARE_MEDIA.EMAIL
                        && platform != SHARE_MEDIA.FLICKR
                        && platform != SHARE_MEDIA.FOURSQUARE
                        && platform != SHARE_MEDIA.TUMBLR
                        && platform != SHARE_MEDIA.POCKET
                        && platform != SHARE_MEDIA.PINTEREST

                        && platform != SHARE_MEDIA.INSTAGRAM
                        && platform != SHARE_MEDIA.GOOGLEPLUS
                        && platform != SHARE_MEDIA.YNOTE
                        && platform != SHARE_MEDIA.EVERNOTE) {
                    Toast.makeText(mActivity.get(), platform + " 分享成功啦", Toast.LENGTH_SHORT).show();
                }

            }
        }

        @Override
        public void onError(SHARE_MEDIA platform, Throwable t) {
            if (platform != SHARE_MEDIA.MORE && platform != SHARE_MEDIA.SMS
                    && platform != SHARE_MEDIA.EMAIL
                    && platform != SHARE_MEDIA.FLICKR
                    && platform != SHARE_MEDIA.FOURSQUARE
                    && platform != SHARE_MEDIA.TUMBLR
                    && platform != SHARE_MEDIA.POCKET
                    && platform != SHARE_MEDIA.PINTEREST

                    && platform != SHARE_MEDIA.INSTAGRAM
                    && platform != SHARE_MEDIA.GOOGLEPLUS
                    && platform != SHARE_MEDIA.YNOTE
                    && platform != SHARE_MEDIA.EVERNOTE) {
                Toast.makeText(mActivity.get(), platform + " 分享失败啦", Toast.LENGTH_SHORT).show();
                if (t != null) {
                    Log.d("throw", "throw:" + t.getMessage());
                }
            }

        }

        @Override
        public void onCancel(SHARE_MEDIA platform) {

            Toast.makeText(mActivity.get(), platform + " 分享取消了", Toast.LENGTH_SHORT).show();
        }
    }

    private void playShow(String url) {

        try {
            ksyMediaPlayer.setDataSource(url);
            ksyMediaPlayer.prepareAsync();
        } catch (IOException e) {
            e.printStackTrace();
        }

        surfaceHolder = playerSurface.getHolder();
        surfaceHolder.addCallback(surfaceCallback);
    }

    @Override
    protected void loadViewLayout() {
        setContentView(R.layout.activity_dynamic2_detail);
    }

    @Override
    protected Context getActivityContext() {
        return this;
    }


    @Override
    protected void onDestroy() {
        ksyMediaPlayer.stop();
        super.onDestroy();
    }

    @Override
    public void onBackPressed() {
        if (videoplayer.backPress()) {
            return;
        }
        super.onBackPressed();
    }

    @Override
    protected void onPause() {
        super.onPause();
        videoplayer.releaseAllVideos();
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
                            CommentListBean commentListBean = new Gson().fromJson(result.body(), CommentListBean.class);
                            List<CommentListBean.Data> datas;
                            datas = commentListBean.data;
                            initUi(datas);
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    break;
                case 0x002:
                    try {
                        JSONObject jsonObject = new JSONObject(result.body());
                        int code = jsonObject.getInt("code");
                        if (code == 200) {
                            JSONArray data = jsonObject.getJSONArray("data");
                            JSONObject jsonObject1 = new JSONObject(data.get(0).toString());
                            String isatt = jsonObject1.getString("isatt");
                            int shouCang = jsonObject1.getInt("shoucang");
                            final String user_id = jsonObject1.getString("user_id");
//                            SPUtils.getInstance("userInfo").put("user_id", user_id);
                            String user_nicename = jsonObject1.getString("user_nicename");
                            String user_avatar = jsonObject1.getString("avatar");
                            String production_content = jsonObject1.getString("production_content");
//                            userDetail.setOnClickListener(new View.OnClickListener() {
//                                @Override
//                                public void onClick(View view) {
//                                    Intent intent = new Intent(DynamicDetail2Activity.this, ZuoPinJiActivity.class);
//                                    startActivity(intent);
//                                }
//                            });
                            nickname.setText(user_nicename);
                            signName.setText(production_content);
                            if (isatt.equals("0")) {
                                //未关注
                                followState.setText("关注");
                                followState.setBackgroundResource(R.drawable.frame_button);
                                Drawable img_on;
                                Resources res = getResources();
                                img_on = res.getDrawable(R.mipmap.add);
                                // 调用setCompoundDrawables时，必须调用Drawable.setBounds()方法,否则图片不显示
                                img_on.setBounds(0, 0, img_on.getMinimumWidth(), img_on.getMinimumHeight());

                                followState.setCompoundDrawables(img_on, null, null, null);
                            } else if (isatt.equals("1")) {
                                //已关注
                                followState.setText("取消关注");
                                followState.setBackgroundResource(R.drawable.frame_button_gray);
                                followState.setCompoundDrawables(null, null, null, null);
                            }
                            followState.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View view) {
                                    DreamApi.followOrNot(0x004, token, user_id, myCallBack);
                                }
                            });
                            if (shouCang == 1) {
                                //已收藏
                                tvCollect.setBackground(getResources().getDrawable(R.mipmap.anim_heart));
                            } else if (shouCang == 0) {
                                //未收藏
                                tvCollect.setBackground(getResources().getDrawable(R.mipmap.collect));
                            }
                            if (user_avatar.contains("http") && !isFinishing()) {
                                Glide.with(DynamicDetail2Activity.this)
                                        .load(user_avatar)
                                        .centerCrop()
                                        .placeholder(R.mipmap.touxiang_re)
                                        .into(avatar);
                            } else if (!isFinishing()) {
                                Glide.with(DynamicDetail2Activity.this)
                                        .load(Constant.BASE_URL2 + user_avatar)
                                        .centerCrop()
                                        .placeholder(R.mipmap.touxiang_re)
                                        .into(avatar);
                            }


                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    break;
                case 0x003:
                    try {
                        JSONObject jsonObject = new JSONObject(result.body());
                        int code = jsonObject.getInt("code");
                        if (code == 200) {
                            refreshLayout.autoRefresh();
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    break;
                case 0x004:
                    try {
                        JSONObject jsonObject = new JSONObject(result.body());
                        int code = jsonObject.getInt("code");
                        String msg = jsonObject.getString("msg");
                        if (code == 200) {
                            ToastManager.show(msg);
                            DreamApi.getDynamicDetail(0x002, token, production_id, myCallBack);
                            DreamApi.getFansNumber(0x005, token, production_id, myCallBack);
                            return;
                        } else if (code == 250) {
                            ToastManager.show(msg);
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    break;
                case 0x005:
                    try {
                        JSONObject jsonObject = new JSONObject(result.body());
                        int code = jsonObject.getInt("code");
                        if (code == 200) {
                            //粉丝数量
                            String data = jsonObject.getString("data");
                            tvFansNumber.setText(data + "粉丝");
                            Drawable img_on;
                            Resources res = getResources();
                            img_on = res.getDrawable(R.mipmap.dt_fans);
                            // 调用setCompoundDrawables时，必须调用Drawable.setBounds()方法,否则图片不显示
                            img_on.setBounds(0, 0, img_on.getMinimumWidth(), img_on.getMinimumHeight());
                            tvFansNumber.setCompoundDrawables(img_on, null, null, null);
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    break;
                case 0x006:
                    try {
                        JSONObject jsonObject = new JSONObject(result.body());
                        int code = jsonObject.getInt("code");
                        if (code == 200) {
                            //粉丝贡献榜
                            FansContributionBean fansContributionBean = new Gson().fromJson(result.body(), FansContributionBean.class);
                            Drawable img_on;
                            Resources res = getResources();
                            img_on = res.getDrawable(R.mipmap.gift);
                            // 调用setCompoundDrawables时，必须调用Drawable.setBounds()方法,否则图片不显示
                            img_on.setBounds(0, 0, img_on.getMinimumWidth(), img_on.getMinimumHeight());
                            tvGiftCount.setCompoundDrawables(img_on, null, null, null);
                            tvGiftCount.setText(fansContributionBean.getData().getGift_reception_count());
                            List<FansContributionBean.DataBean.GiftReceptionListBean> data;
                            data = fansContributionBean.getData().getGift_reception_list();
                            LinearLayoutManager layoutManager = new LinearLayoutManager(DynamicDetail2Activity.this);
                            layoutManager.setOrientation(LinearLayoutManager.HORIZONTAL);
                            gridRecyclerView.setLayoutManager(layoutManager);

                            fansContributionAdapter = new FansContributionAdapter(R.layout.item_fans_contribution, data);
                            gridRecyclerView.setAdapter(fansContributionAdapter);
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    break;
                case 0x007:
                    try {
                        JSONObject jsonObject = new JSONObject(result.body());
                        int code = jsonObject.getInt("code");
                        if (code == 200) {
                            refreshLayout.autoRefresh();
                            comment_content.setText("");
                            popupInputMethodWindow();
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    break;
                case 0x008:
                    try {
                        JSONObject jsonObject = new JSONObject(result.body());
                        int code = jsonObject.getInt("code");
                        if (code == 200) {


//                            String url = jsonObject.getString("data");
//                            WebSettings webSettings = webView.getSettings();
//                            //设置WebView属性，能够执行Javascript脚本
//                            webSettings.setJavaScriptEnabled(true);
//                            webSettings.setLoadsImagesAutomatically(true);
//                            webView.setVerticalScrollBarEnabled(false);
//                            //运行webview通过URI获取安卓文件
//                            webSettings.setAllowFileAccess(true);
//                            webSettings.setAllowFileAccessFromFileURLs(true);
//                            webSettings.setAllowUniversalAccessFromFileURLs(true);
//                            String cookie = SPUtils.getInstance("userInfo").getString("cookie");
//                            synchronousWebCookies(DynamicDetail2Activity.this, url, cookie);
//                            webView.loadUrl(url);
//                            popupInputMethodWindow();
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    break;
                case 0x009:
                    try {
                        JSONObject jsonObject = new JSONObject(result.body());
                        int code = jsonObject.getInt("code");
                        if (code == 200) {

                            giftDetailBean = new Gson().fromJson(result.body(), GiftDetailBean.class);
                            List<GiftDetailBean.Data.ShopList> shopData;
                            List<GiftDetailBean.Data.KnapsackLlist> knapsackData;
                            shopData = giftDetailBean.data.shop_list;
                            knapsackData = giftDetailBean.data.knapsack_list;
                            final GiftDetailShopAdapter giftDetailShopAdapter = new GiftDetailShopAdapter(R.layout.item_shop_detail_gift, shopData);
                            final GiftDetailPackageAdapter giftDetailPackageAdapter = new GiftDetailPackageAdapter(R.layout.item_shop_detail_gift, knapsackData);
                            giftRecyclerView.setAdapter(giftDetailShopAdapter);
                            gold_number.setText("金币金额:" + giftDetailBean.data.score);
                            giftDetailShopAdapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
                                @Override
                                public void onItemClick(BaseQuickAdapter adapter, View view, final int position) {
                                    dialog = new NormalDialog(DynamicDetail2Activity.this);
                                    dialog.isTitleShow(false)
                                            .bgColor(Color.parseColor("#ffffff"))
                                            .cornerRadius(5)
                                            .content("确定要赠送吗")
                                            .contentGravity(Gravity.CENTER)
                                            .contentTextColor(Color.parseColor("#99000000"))
                                            .dividerColor(Color.parseColor("#55000000"))
                                            .btnTextSize(15.5f, 15.5f)//
                                            .btnTextColor(Color.parseColor("#99000000"), Color.parseColor("#CCEA4F05"))
                                            .widthScale(0.85f)
                                            .show();

                                    dialog.setOnBtnClickL(
                                            new OnBtnClickL() {
                                                @Override
                                                public void onBtnClick() {
                                                    dialog.dismiss();
                                                }
                                            },
                                            new OnBtnClickL() {
                                                @Override
                                                public void onBtnClick() {
                                                    dialog.dismiss();
                                                    //确定送出
                                                    String score = giftDetailBean.data.score;
                                                    String gift_price = giftDetailShopAdapter.getItem(position).gift_price;
                                                    Double obj1 = new Double(gift_price);
                                                    Double obj2 = new Double(score);
                                                    int retval = obj1.compareTo(obj2);
                                                    if (retval < 0) {
                                                        DreamApi.shopGiftSendUrl(0x010, token, "0", production_id, "1",
                                                                giftDetailShopAdapter.getItem(position).gift_id, "1", myCallBack);
                                                    } else {
                                                        ToastManager.show("金币不足，请充值");
                                                    }

                                                }
                                            });

                                }
                            });
                            giftDetailPackageAdapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
                                @Override
                                public void onItemClick(BaseQuickAdapter adapter, View view, final int position) {
                                    dialog = new NormalDialog(DynamicDetail2Activity.this);
                                    dialog.isTitleShow(false)
                                            .bgColor(Color.parseColor("#ffffff"))
                                            .cornerRadius(5)
                                            .content("确定要赠送吗")
                                            .contentGravity(Gravity.CENTER)
                                            .contentTextColor(Color.parseColor("#99000000"))
                                            .dividerColor(Color.parseColor("#55000000"))
                                            .btnTextSize(15.5f, 15.5f)//
                                            .btnTextColor(Color.parseColor("#99000000"), Color.parseColor("#CCEA4F05"))
                                            .widthScale(0.85f)
                                            .show();

                                    dialog.setOnBtnClickL(
                                            new OnBtnClickL() {
                                                @Override
                                                public void onBtnClick() {
                                                    dialog.dismiss();
                                                }
                                            },
                                            new OnBtnClickL() {
                                                @Override
                                                public void onBtnClick() {
                                                    dialog.dismiss();
                                                    //确定送出
                                                    DreamApi.shopGiftSendUrl(0x010, token, "0", production_id, "1",
                                                            giftDetailPackageAdapter.getItem(position).gift_id, "1", myCallBack);
                                                }
                                            });

                                }
                            });
                            all_gift.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View view) {
                                    all_gift.setTextColor(getResources().getColor(R.color.colorPrimary));
                                    my_gift.setTextColor(getResources().getColor(R.color.white));
                                    giftRecyclerView.setAdapter(giftDetailShopAdapter);
                                }
                            });
                            my_gift.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View view) {
                                    all_gift.setTextColor(getResources().getColor(R.color.white));
                                    my_gift.setTextColor(getResources().getColor(R.color.colorPrimary));
                                    giftRecyclerView.setAdapter(giftDetailPackageAdapter);
                                }
                            });
                            to_charge.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View view) {
                                    Intent intent = new Intent(DynamicDetail2Activity.this, MyGoldActivity.class);
                                    startActivity(intent);
                                }
                            });
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    break;
                case 0x010:
                    try {
                        JSONObject jsonObject = new JSONObject(result.body());
                        int code = jsonObject.getInt("code");
                        if (code == 200) {
                            ToastManager.show("赠送成功");
                            popGiftWindow.dismiss();
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    break;
                case 0x011:
                    try {
                        JSONObject jsonObject = new JSONObject(result.body());
                        int code = jsonObject.getInt("code");
                        if (code == 200) {
//                            tvCollect.setBackground(getResources().getDrawable(R.mipmap.anim_heart));
                            DreamApi.getDynamicDetail(0x002, token, production_id, myCallBack);
                            String msg = jsonObject.getString("msg");
                            ToastManager.show(msg);
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
            switch (what) {
                case 0x001:
                    refreshLayout.finishLoadmore();//解决加载更多一直显示
                    refreshLayout.finishRefresh();
                    break;
            }

        }
    };

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

    private void initUi(final List<CommentListBean.Data> datas) {
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        commentListAdapter = new CommentListAdapter(R.layout.item_comment, datas, this);
        //给RecyclerView设置适配器
        recyclerView.setAdapter(commentListAdapter);
        if (datas.size() == 0) {
            commentListAdapter.setEmptyView(R.layout.empty, recyclerView);
        }
        commentListAdapter.openLoadAnimation(BaseQuickAdapter.ALPHAIN);
        commentListAdapter.isFirstOnly(false);
        commentListAdapter.setOnItemChildClickListener(new BaseQuickAdapter.OnItemChildClickListener() {
            @Override
            public void onItemChildClick(BaseQuickAdapter adapter, View view, int position) {
                switch (view.getId()) {
                    case R.id.iv_zan:
                        DreamApi.dianZan(0x003, token, commentListAdapter.getItem(position).comment_id, myCallBack);
                        break;
                    case R.id.reply_count:
//                        String sen_user_id = commentListAdapter.getItem(position).sen_user_id;
//                        Intent intent = new Intent(DynamicDetail2Activity.this, ErjiCommentActivity.class);
//                        intent.putExtra("sen_user_id", sen_user_id);
//                        intent.putExtra("production_id", production_id);
//                        startActivity(intent);
                        break;
                }
            }
        });
    }


    @Override
    public void onRefresh(RefreshLayout refreshlayout) {
        page = 1;
        DreamApi.getCommentList(0x001, token, page, production_id, myCallBack);
    }

    @Override
    public void onLoadmore(final RefreshLayout refreshlayout) {
        page++;
        DreamApi.getCommentList(0x001, token, page, production_id, new MyCallBack() {
            @Override
            public void onSuccess(int what, Response<String> result) {
                try {
                    JSONObject jsonObject = new JSONObject(result.body());
                    int code = jsonObject.getInt("code");
                    if (code == 200) {
                        CommentListBean commentListBean = new Gson().fromJson(result.body(), CommentListBean.class);
                        List<CommentListBean.Data> datas;
                        datas = commentListBean.data;
                        if (datas.size() == 0) {
                            refreshLayout.setLoadmoreFinished(true);
                        } else {
                            commentListAdapter.addData(datas);
                        }
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFail(int what, Response<String> result) {

            }

            @Override
            public void onFinish(int what) {
                refreshlayout.finishLoadmore();//解决加载更多一直显示
                refreshlayout.finishRefresh();
            }
        });
    }

    private void popupInputMethodWindow() {
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                InputMethodManager imm = (InputMethodManager) getSystemService(Service.INPUT_METHOD_SERVICE);
                imm.toggleSoftInput(0, InputMethodManager.HIDE_NOT_ALWAYS);
//                InputMethodManager imm = (InputMethodManager) rbComment.getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
//                imm.hideSoftInputFromWindow(getWindow().getDecorView().getWindowToken(), 0);
            }
        }, 0);
    }

    private EditText comment_content;
    private TextView fabiao;

    private void showPopup() {
        if (popWindow == null) {
            LayoutInflater layoutInflater = (LayoutInflater) this.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            View view = layoutInflater.inflate(R.layout.popwindow_pinglun, null);
            // 创建一个PopuWidow对象
            popWindow = new PopupWindow(view, LinearLayout.LayoutParams.FILL_PARENT, 140, true);

            comment_content = view.findViewById(R.id.pinglun);
            fabiao = view.findViewById(R.id.fabiao);
        }
        //popupwindow弹出时的动画		popWindow.setAnimationStyle(R.style.popupWindowAnimation);
        // 使其聚集 ，要想监听菜单里控件的事件就必须要调用此方法
        popWindow.setFocusable(true);
        // 设置允许在外点击消失
        popWindow.setOutsideTouchable(false);
        // 设置背景，这个是为了点击“返回Back”也能使其消失，并且并不会影响你的背景
        popWindow.setBackgroundDrawable(new BitmapDrawable());
        //软键盘不会挡着popupwindow
        popWindow.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);
        //设置菜单显示的位置
        popWindow.showAtLocation(rbComment, Gravity.BOTTOM, 0, 0);
        fabiao.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (comment_content.getText().toString().equals("")) {
                    ToastManager.show("评论不能为空");
                    return;
                }
                DreamApi.addComment(0x007, token, production_id, "0", comment_content.getText().toString(), myCallBack);
            }
        });
        //监听菜单的关闭事件
        popWindow.setOnDismissListener(new PopupWindow.OnDismissListener() {
            @Override
            public void onDismiss() {

            }
        });

        //监听触屏事件
        popWindow.setTouchInterceptor(new View.OnTouchListener() {
            public boolean onTouch(View view, MotionEvent event) {
                return false;
            }
        });
    }

    private void chooseRecordType() {
        final String[] stringItems = {"清唱录制", "伴唱录制"};
        final ActionSheetDialog dialog = new ActionSheetDialog(this, stringItems, null);
        dialog.title("录制").titleTextSize_SP(14.5f).show();
        dialog.setOnOperItemClickL(new OnOperItemClickL() {
            @Override
            public void onOperItemClick(AdapterView<?> parent, View view, int position, long id) {
                switch (position) {
                    case 0:
                        final ArrayList<String> list = new ArrayList<>();
                        list.add("清唱录音");
                        list.add("清唱视频");
                        final OptionCenterDialog optionCenterDialog = new OptionCenterDialog();
                        optionCenterDialog.show(DynamicDetail2Activity.this, list);
                        optionCenterDialog.setItemClickListener(new AdapterView.OnItemClickListener() {
                            @Override
                            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                                switch (position) {
                                    case 0:
                                        Intent intent1 = new Intent(DynamicDetail2Activity.this, QAudioActivity.class);
                                        intent1.putExtra("song_id", "");
                                        intent1.putExtra("song_name", song_name);
                                        intent1.putExtra("song_path", "");
                                        intent1.putExtra("type", "1");
                                        intent1.putExtra("song_type", "0");
                                        intent1.putExtra("lyric_path", "");
                                        intent1.putExtra("activity_id", "");
                                        startActivity(intent1);
                                        break;
                                    case 1:
                                        Intent intent = new Intent(DynamicDetail2Activity.this, QVideoActivity.class);
                                        intent.putExtra("song_id", "");
                                        intent.putExtra("song_name", song_name);
                                        intent.putExtra("song_path", "");
                                        intent.putExtra("type", "2");
                                        intent.putExtra("song_type", "0");
                                        intent.putExtra("lyric_path", "");
                                        intent.putExtra("activity_id", "");
                                        startActivity(intent);
                                        break;
                                }
                                optionCenterDialog.dismiss();
                            }
                        });
                        dialog.dismiss();

                        break;
                    case 1:
                        Intent intent = new Intent(DynamicDetail2Activity.this, SongsListActivity.class);
                        startActivity(intent);
                        dialog.dismiss();
                        break;
                    default:
                        break;
                }

            }
        });
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.rb_gift:
                //送礼物
//                DreamApi.sendGift(0x008, token, production_id, myCallBack);
                showGiftPop();
                break;
            case R.id.rb_comment:
                //评论
                showPopup();
                popupInputMethodWindow();
                break;
            case R.id.rb_perform:
                //演唱
                chooseRecordType();
                break;
            case R.id.rb_zhuanfa:
                //转发
                mShareAction.open();
                break;
            default:
                break;
        }
    }

    private TextView all_gift;
    private TextView my_gift;
    private TextView gold_number;
    private TextView to_charge;

    private void showGiftPop() {
        if (popGiftWindow == null) {
            LayoutInflater layoutInflater = (LayoutInflater) this.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            View view = layoutInflater.inflate(R.layout.popwindow_gift, null);
            // 创建一个PopuWidow对象
            popGiftWindow = new PopupWindow(view, LinearLayout.LayoutParams.FILL_PARENT, 600, true);

            giftRecyclerView = view.findViewById(R.id.recyclerView);
            GridLayoutManager gridLayoutManager = new GridLayoutManager(DynamicDetail2Activity.this, 4);
//            gridLayoutManager.setOrientation(LinearLayoutManager.HORIZONTAL);
            giftRecyclerView.setLayoutManager(gridLayoutManager);
            all_gift = view.findViewById(R.id.all_gift);
            my_gift = view.findViewById(R.id.my_gift);
            gold_number = view.findViewById(R.id.gold_number);
            to_charge = view.findViewById(R.id.to_charge);
            all_gift.setTextColor(getResources().getColor(R.color.colorPrimary));
            my_gift.setTextColor(getResources().getColor(R.color.white));
            DreamApi.giftDetailListUrl(0x009, token, myCallBack);

        }
        //popupwindow弹出时的动画		popWindow.setAnimationStyle(R.style.popupWindowAnimation);
        // 使其聚集 ，要想监听菜单里控件的事件就必须要调用此方法
        popGiftWindow.setFocusable(true);
        // 设置允许在外点击消失
        popGiftWindow.setOutsideTouchable(false);
        // 设置背景，这个是为了点击“返回Back”也能使其消失，并且并不会影响你的背景
        popGiftWindow.setBackgroundDrawable(new BitmapDrawable());
//        //软键盘不会挡着popupwindow
        popGiftWindow.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);
        //设置菜单显示的位置
        popGiftWindow.showAtLocation(rbComment, Gravity.BOTTOM, 0, 0);


        //监听菜单的关闭事件
        popGiftWindow.setOnDismissListener(new PopupWindow.OnDismissListener() {
            @Override
            public void onDismiss() {

            }
        });

        //监听触屏事件
        popGiftWindow.setTouchInterceptor(new View.OnTouchListener() {
            public boolean onTouch(View view, MotionEvent event) {
                return false;
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        /** attention to this below ,must add this**/
        UMShareAPI.get(this).onActivityResult(requestCode, resultCode, data);
    }

    /**
     * 屏幕横竖屏切换时避免出现window leak的问题
     */
    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        mShareAction.close();
    }
}
