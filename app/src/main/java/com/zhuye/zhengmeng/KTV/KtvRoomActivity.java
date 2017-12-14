package com.zhuye.zhengmeng.KTV;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import com.blankj.utilcode.util.SPUtils;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.flyco.dialog.listener.OnBtnClickL;
import com.flyco.dialog.widget.NormalDialog;
import com.google.gson.Gson;
import com.jude.easyrecyclerview.EasyRecyclerView;
import com.lzy.okgo.OkGo;
import com.lzy.okgo.callback.FileCallback;
import com.lzy.okgo.model.Response;
import com.tencent.rtmp.TXLivePlayConfig;
import com.tencent.rtmp.TXLivePlayer;
import com.tencent.rtmp.TXLivePushConfig;
import com.tencent.rtmp.TXLivePusher;
import com.tencent.rtmp.ui.TXCloudVideoView;
import com.umeng.socialize.ShareAction;
import com.umeng.socialize.UMShareListener;
import com.umeng.socialize.bean.SHARE_MEDIA;
import com.umeng.socialize.media.UMImage;
import com.umeng.socialize.media.UMWeb;
import com.zhuye.zhengmeng.App;
import com.zhuye.zhengmeng.Constant;
import com.zhuye.zhengmeng.DataProvider;
import com.zhuye.zhengmeng.KTV.adapter.FushiAdapter;
import com.zhuye.zhengmeng.KTV.adapter.NumberListAdapter;
import com.zhuye.zhengmeng.KTV.bean.NextSongBean;
import com.zhuye.zhengmeng.KTV.bean.OnRoomBean;
import com.zhuye.zhengmeng.LiveKit;
import com.zhuye.zhengmeng.R;
import com.zhuye.zhengmeng.activity.register.RegisterActivity;
import com.zhuye.zhengmeng.bangdan.DianGeListActivity;
import com.zhuye.zhengmeng.chatRoom.controller.ChatListAdapter;
import com.zhuye.zhengmeng.chatRoom.fragment.BottomPanelFragment;
import com.zhuye.zhengmeng.chatRoom.message.GiftMessage;
import com.zhuye.zhengmeng.http.DreamApi;
import com.zhuye.zhengmeng.http.MyCallBack;
import com.zhuye.zhengmeng.shop.adapter.GiftDetailPackageAdapter;
import com.zhuye.zhengmeng.shop.adapter.GiftDetailShopAdapter;
import com.zhuye.zhengmeng.shop.bean.GiftDetailBean;
import com.zhuye.zhengmeng.user.MyGoldActivity;
import com.zhuye.zhengmeng.utils.ToastManager;
import com.zhuye.zhengmeng.utils.UIThread;
import com.zhuye.zhengmeng.view.MyAppTitle;
import com.zhuye.zhengmeng.widget.ChatListView;
import com.zhuye.zhengmeng.widget.InputPanel;
import com.zhuye.zhengmeng.widget.animation.HeartLayout;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.Timer;
import java.util.TimerTask;

import de.hdodenhof.circleimageview.CircleImageView;
import io.rong.imlib.RongIMClient;
import io.rong.imlib.model.MessageContent;
import io.rong.imlib.model.UserInfo;
import io.rong.message.InformationNotificationMessage;
import io.rong.message.TextMessage;
import me.wcy.lrcview.LrcView;

public class KtvRoomActivity extends FragmentActivity implements View.OnClickListener, Handler.Callback {

    public static final String LIVE_URL = "live_url";

    private ViewGroup background;
    private ChatListView chatListView;
    private BottomPanelFragment bottomPanel;
    private TextView btnGift;
    private TextView huanzhuang;
    private TextView rb_paimai;
    private ImageView img_show;
    private CircleImageView user_avatar;
    //    private ImageView btnHeart;
    private HeartLayout heartLayout;

    private Random random = new Random();
    private Handler handler = new Handler(this);
    private ChatListAdapter chatListAdapter;
    private String roomId;
    private String rongcloudToken;
    private String user_nicename;
    private String chatroom_name;
    private String userId;
    private String token;
    private static final String TAG = "KtvRoomActivity";
    private Timer timer, timer2;
    private Task task;
    private OnLineTask onLineTask;
    private TXLivePlayer mLivePlayer;
    private TXCloudVideoView pullMedia;
    private TXCloudVideoView push_media;
    private TXLivePushConfig mLivePushConfig;
    private TXLivePusher mLivePusher;
    private TXLivePlayConfig mPlayConfig;
    private TextView tvNextSong;
    private TextView release;
    private TextView ktv_share;
    private String lyric_path;
    private String user_id;
    private String reception_id;
    private String song_path;
    private String is_play;
    //歌词
    private MediaPlayer mediaPlayer = new MediaPlayer();
    private boolean repared = false;//判断mediaPlayer是否初始化完成
    boolean mIsPlay = false;
    private SeekBar seekBar;
    private LrcView lrcView;
    private String avatar;
    private MyAppTitle titleBar;
    private String pullUrl;
    private String pushUrl;
    private RelativeLayout.LayoutParams layoutParams;
    private GiftDetailBean giftDetailBean;
    private NormalDialog dialog;
    private RecyclerView numberRecyclerView;
    private UMShareListener mShareListener;
    private ShareAction mShareAction;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mPlayConfig = new TXLivePlayConfig();
        //创建player对象
        mLivePlayer = new TXLivePlayer(this);
        mLivePusher = new TXLivePusher(this);
        mLivePushConfig = new TXLivePushConfig();
        mLivePusher.setConfig(mLivePushConfig);
        setContentView(R.layout.activity_ktv_room);
        checkPublishPermission();
        roomId = getIntent().getStringExtra("roomId");
        //通过浏览器打开
        Uri data = getIntent().getData();
        if (data != null) {
            String encodedQuery = data.getEncodedQuery();
            roomId = encodedQuery;
            Log.i(TAG, "onCreate: 房间id" + roomId);
            if (token == null) {
                startActivity(new Intent(KtvRoomActivity.this, RegisterActivity.class));
                finish();
            }
        }
        chatroom_name = getIntent().getStringExtra("chatroom_name");
        rongcloudToken = SPUtils.getInstance("userInfo").getString("rongcloudToken");
        token = SPUtils.getInstance("userInfo").getString("token");
        userId = SPUtils.getInstance("userInfo").getString("userId");
        user_nicename = SPUtils.getInstance("userInfo").getString("user_nicename");
        DreamApi.getPullUrl(0x004, roomId, myCallBack);
        DreamApi.getPushUrl(0x005, roomId, myCallBack);

        connect(rongcloudToken);
        setTitle();
        DreamApi.joinRoom(0x012, token, roomId, myCallBack);

        //获取下首播放歌曲
        getNextSong();
        ifOnRoom();
        numberRecyclerView = findViewById(R.id.numberRecyclerView);

    }

    private void ifOnRoom() {
        timer2 = new Timer();
        onLineTask = new OnLineTask();
        timer2.schedule(onLineTask, 0, 10 * 1000);
    }


    private boolean checkPublishPermission() {
        if (Build.VERSION.SDK_INT >= 23) {
            List<String> permissions = new ArrayList<>();
            if (PackageManager.PERMISSION_GRANTED != ActivityCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE)) {
                permissions.add(Manifest.permission.WRITE_EXTERNAL_STORAGE);
            }
            if (PackageManager.PERMISSION_GRANTED != ActivityCompat.checkSelfPermission(this, Manifest.permission.CAMERA)) {
                permissions.add(Manifest.permission.CAMERA);
            }
            if (PackageManager.PERMISSION_GRANTED != ActivityCompat.checkSelfPermission(this, Manifest.permission.RECORD_AUDIO)) {
                permissions.add(Manifest.permission.RECORD_AUDIO);
            }
            if (PackageManager.PERMISSION_GRANTED != ActivityCompat.checkSelfPermission(this, Manifest.permission.READ_PHONE_STATE)) {
                permissions.add(Manifest.permission.READ_PHONE_STATE);
            }
            if (permissions.size() != 0) {
                ActivityCompat.requestPermissions(this,
                        permissions.toArray(new String[0]),
                        100);
                return false;
            }
        }

        return true;
    }

    private void pullMedia() {
        if (isFinishing()) {
            return;
        }

        //关键player对象与界面view
        mLivePlayer.setPlayerView(pullMedia);
        mLivePlayer.setConfig(mPlayConfig);
        mLivePlayer.setAutoPlay(true);
        String flvUrl = pullUrl;
        mLivePlayer.startPlay(flvUrl, TXLivePlayer.PLAY_TYPE_LIVE_RTMP); //推荐FLV
        Log.i(TAG, "pullMedia: " + "拉流");
    }

    private void getNextSong() {
        timer = new Timer();
        task = new Task();
        timer.schedule(task, 0, 20 * 1000);
    }

    public class Task extends TimerTask {

        @Override
        public void run() {

            UIThread.getInstance().post(new Runnable() {
                @Override
                public void run() {
                    Log.i(TAG, "run: " + "轮询起来吧");
                    DreamApi.getNextSongPlay(0x001, token, roomId, myCallBack);
                }
            });
        }
    }

    public class OnLineTask extends TimerTask {

        @Override
        public void run() {

            UIThread.getInstance().post(new Runnable() {
                @Override
                public void run() {
                    Log.i(TAG, "run: " + "查询起来吧");
                    DreamApi.ifOnline(0x013, token, roomId, myCallBack);
                }
            });
        }
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
                            NextSongBean nextSongBean = new Gson().fromJson(result.body(), NextSongBean.class);
                            lyric_path = nextSongBean.data.lyric_path;
                            user_id = nextSongBean.data.user_id;
                            song_path = nextSongBean.data.song_path;
                            is_play = nextSongBean.data.is_play;
                            avatar = nextSongBean.data.avatar;
                            if (lyric_path != null && song_path != null && avatar != null) {
                                Glide.with(App.getInstance())
                                        .load(Constant.BASE_URL2 + avatar)
                                        .centerCrop()
                                        .into(user_avatar);

                            }
                            Log.i(TAG, "onSuccess: 歌词" + lyric_path);
                            Log.i(TAG, "onSuccess: 歌词路径" + song_path);

//                            if (mLivePusher == null) {
//                                DreamApi.isSinging(0x002, token, roomId, "0", myCallBack);
//                            }else{
//                                DreamApi.isSinging(0x002, token, roomId, "1", myCallBack);
//                            }
//
                            if (user_id != null && userId != null && is_play != null) {
                                if (!user_id.equals(userId) && is_play.equals("1")) {
                                    //拉流
                                    pullMedia();

                                } else if (user_id.equals(userId) && is_play.equals("0")) {
                                    pushMedia();
                                    if (timer != null) {

                                        timer.cancel();
                                    }
                                }
                            }
                            Log.i(TAG, "onSuccess:isplay状态 " + is_play);
                            Log.i(TAG, "onSuccess:user_id状态 " + user_id);
                            Log.i(TAG, "onSuccess:userId " + userId);
                            if (user_id != null && user_id.equals(userId) && is_play.equals("0")) {
                                //轮到自己，停止拉流，开始推流
                                stopPlayRtmp();
//                                ToastManager.show("该我推流了");
                                showPushDialog();


                            } else {

                                //什么也不做
                            }
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
                            Log.i(TAG, "onSuccess: " + "返回状态完成");
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
                            Log.i(TAG, "onSuccess: " + "返回状态完成");
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    break;
                case 0x004:
                    try {
                        JSONObject jsonObject = new JSONObject(result.body());
                        int code = jsonObject.getInt("code");
                        if (code == 200) {
                            pullUrl = jsonObject.getString("data");
                            Log.i(TAG, "onSuccess: pullUrl" + pullUrl);
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
                            pushUrl = jsonObject.getString("data");
                            Log.i(TAG, "onSuccess: pushUrl" + pushUrl);
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
                                    dialog = new NormalDialog(KtvRoomActivity.this);
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
                                                    DreamApi.shopGiftSendUrl(0x010, token, reception_id, "", "1",
                                                            giftDetailShopAdapter.getItem(position).gift_id, "1", myCallBack);
                                                }
                                            });

                                }
                            });
                            giftDetailPackageAdapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
                                @Override
                                public void onItemClick(BaseQuickAdapter adapter, View view, final int position) {
                                    dialog = new NormalDialog(KtvRoomActivity.this);
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
                                                    DreamApi.shopGiftSendUrl(0x010, token, reception_id, "", "1",
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
                                    Intent intent = new Intent(KtvRoomActivity.this, MyGoldActivity.class);
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
                case 0x012:
                    try {
                        JSONObject jsonObject = new JSONObject(result.body());
                        int code = jsonObject.getInt("code");
                        if (code == 200) {
                            Log.i(TAG, "onSuccess: " + "加入房间里了");
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    break;
                case 0x013:
                    try {
                        JSONObject jsonObject = new JSONObject(result.body());
                        int code = jsonObject.getInt("code");
                        if (code == 200) {
                            String msg = jsonObject.getString("msg");
                            titleBar.setAppTitle(chatroom_name + "(" + msg + ")人");
                            OnRoomBean onRoomBean = new Gson().fromJson(result.body(), OnRoomBean.class);
                            List<OnRoomBean.Data> numberData;
                            numberData = onRoomBean.data;
                            //创建布局管理
                            LinearLayoutManager layoutManager = new LinearLayoutManager(KtvRoomActivity.this);
                            layoutManager.setOrientation(LinearLayoutManager.HORIZONTAL);
//                            numberRecyclerView.setLayoutManager(new GridLayoutManager(KtvRoomActivity.this, 3));
                            numberRecyclerView.setLayoutManager(layoutManager);
                            NumberListAdapter numberListAdapter = new NumberListAdapter(R.layout.item_number_avatar, numberData);
                            numberRecyclerView.setAdapter(numberListAdapter);
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    break;
                case 0x014:
                    try {
                        JSONObject jsonObject = new JSONObject(result.body());
                        int code = jsonObject.getInt("code");
                        if (code == 200) {
                            String shareUrl = jsonObject.getString("data");
                            mShareListener = new CustomShareListener(KtvRoomActivity.this);
                            UMWeb web = new UMWeb(Constant.BASE_URL2 + shareUrl);
                            web.setTitle("征梦");//标题

                            web.setThumb(new UMImage(KtvRoomActivity.this, R.mipmap.logo));  //缩略图
//        web.setDescription(Defaultcontent.text);//描述
        /*无自定按钮的分享面板*/
                            mShareAction = new ShareAction(KtvRoomActivity.this).setDisplayList(
                                    SHARE_MEDIA.WEIXIN, SHARE_MEDIA.WEIXIN_CIRCLE, SHARE_MEDIA.WEIXIN_FAVORITE,
                                    SHARE_MEDIA.SINA, SHARE_MEDIA.QQ, SHARE_MEDIA.QZONE)
//                .withText(Defaultcontent.text + "来自友盟自定义分享面板")
                                    .withMedia(web)
                                    .setCallback(mShareListener);
                            mShareAction.open();
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
    private TextView mOffTextView;
    private Handler mOffHandler;
    private Timer mOffTime;
    private Dialog mDialog;

    @SuppressLint("HandlerLeak")
    private void showPushDialog() {
        mOffTextView = new TextView(this);
        mOffTextView.setTextSize(18);
        mOffTextView.setGravity(Gravity.CENTER);
        mOffTextView.setPadding(10, 10, 10, 10);
        TextView title = new TextView(this);
        title.setText("该你上麦了");
        title.setTextSize(22);
        title.setPadding(10, 10, 10, 10);
        title.setGravity(Gravity.CENTER);
        if (isFinishing()) {
            return;
        }
        mDialog = new AlertDialog.Builder(KtvRoomActivity.this)
                .setCustomTitle(title)
                .setCancelable(false)
                .setView(mOffTextView)
                .setPositiveButton("开始演唱", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        mIsPlay = true;
                        try {
                            mediaPlayer.reset();
                            mediaPlayer.setDataSource(Constant.BASE_URL_PINJIE + song_path.replace(" ", "%20"));
                            mediaPlayer.prepareAsync();
                            mediaPlayer.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
                                @Override
                                public void onPrepared(MediaPlayer mp) {
                                    Log.i(TAG, "onPrepared: 播放准备好了");
                                    lrcView.setVisibility(View.VISIBLE);
                                    mediaPlayer.start();
                                    seekBar.setMax(mediaPlayer.getDuration());
                                    seekBar.setProgress(0);
                                    repared = true;
                                    DreamApi.isSinging(0x002, token, roomId, "1", myCallBack);
                                }
                            });
                            mediaPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
                                @Override
                                public void onCompletion(MediaPlayer mp) {
                                    Log.i(TAG, "onCompletion: 播放结束");
                                    lrcView.updateTime(0);
                                    seekBar.setProgress(0);
                                    lrcView.setVisibility(View.INVISIBLE);
                                    if (repared && !mediaPlayer.isPlaying()) {
                                        mediaPlayer.stop();
                                        repared = false;
                                        getNextSong();
                                        DreamApi.isSinging(0x002, token, roomId, "0", myCallBack);
                                        DreamApi.isSongSinging(0x003, roomId, "1", myCallBack);
                                    }

                                }
                            });
                            mediaPlayer.setOnErrorListener(new MediaPlayer.OnErrorListener() {
                                @Override
                                public boolean onError(MediaPlayer mediaPlayer, int i, int i1) {
                                    Log.i(TAG, "onError: 播放出错");
//                                    mediaPlayer.start();
                                    repared = false;
                                    lrcView.setVisibility(View.VISIBLE);
                                    DreamApi.isSinging(0x002, token, roomId, "1", myCallBack);

                                    return false;
                                }
                            });
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                        //加载歌词
                        OkGo.<File>get(Constant.BASE_URL_PINJIE + lyric_path)
                                .tag(this).execute(new FileCallback() {
                            @Override
                            public void onSuccess(Response<File> response) {
                                String lrc = "";
                                File mFile = response.body();
                                FileInputStream is = null;
                                try {
                                    is = new FileInputStream(mFile);
                                    int size = is.available();
                                    byte[] buffer = new byte[size];
                                    is.read(buffer);
                                    lrc = new String(buffer);
                                    lrcView.loadLrc(lrc);
                                    is.close();
                                } catch (IOException e) {
                                    e.printStackTrace();
                                }
                            }
                        });

                        //**************播放背景音乐***************//
                        if (!mediaPlayer.isPlaying()) {
                            UIThread.getInstance().postDelay(new Runnable() {
                                @Override
                                public void run() {
                                    mediaPlayer.start();
                                    handler.post(runnable);

                                }
                            }, 500);

                        } else {
                            mediaPlayer.pause();
                            handler.removeCallbacks(runnable);
                        }
//                        if (!mIsPlay) {
//                            stopPublishRtmp();
//                            pullMedia();
//                        }

                        pushMedia();
                        stopPlayRtmp();
                    }
                }).setNegativeButton("放弃演唱", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        dialogInterface.cancel();
                        mOffTime.cancel();

                    }
                }).create();
        if (!isFinishing()) {
            mDialog.show();
        }

        mDialog.setCanceledOnTouchOutside(false);
        mOffHandler = new Handler() {
            @Override
            public void handleMessage(Message msg) {
                super.handleMessage(msg);
                if (msg.what > 0) {
                    //动态显示倒计时
                    String content = "请在" + msg.what + "秒内开始演唱，\n否则将自动放弃此次机会";
                    mOffTextView.setText(content.replace("\\n", "\n"));
                } else {
                    //倒计时结束自动关闭
                    if (mDialog != null) {
                        mDialog.cancel();
                    }
                    mOffTime.cancel();
                }
            }
        };
        //倒计时
        mOffTime = new Timer(true);
        TimerTask tt = new TimerTask() {
            int countTime = 15;

            public void run() {
                if (countTime > 0) {
                    countTime--;
                }
                Message msg = new Message();
                msg.what = countTime;
                mOffHandler.sendMessage(msg);
            }
        };
        mOffTime.schedule(tt, 1000, 1000);

    }

    private Runnable runnable = new Runnable() {
        @Override
        public void run() {
            if (mediaPlayer.isPlaying()) {
                long time = mediaPlayer.getCurrentPosition();
                lrcView.updateTime(time);
                seekBar.setProgress((int) time);
            }

            handler.postDelayed(this, 300);
        }
    };

    private void pushMedia() {

        if (isFinishing()) {
            return;
        }
//        mLivePushConfig.setPauseImg(300, 5);
        // 只有在推流启动前设置启动纯音频推流才会生效，推流过程中设置不会生效。
//        mLivePushConfig.enablePureAudioPush(false);   // true 为启动纯音频推流，而默认值是 false；

        mLivePusher.setConfig(mLivePushConfig);
        if (pushUrl != null) {
            String rtmpUrl = pushUrl;
            // 重新设置 config
//            mLivePusher.startCameraPreview(push_media);
            mLivePusher.startPusher(rtmpUrl);
            Log.i(TAG, "pushMedia: " + "推流");
        }

    }

    private void setTitle() {

        titleBar = findViewById(R.id.titleBar);
        titleBar.initViewsVisible(true, true, false, true);
        titleBar.setTitleSize(20);
        titleBar.setAppTitle(chatroom_name + "(0)人");
        titleBar.setRightTitle("排麦列表");
        titleBar.setOnLeftButtonClickListener(new MyAppTitle.OnLeftButtonClickListener() {
            @Override
            public void onLeftButtonClick(View v) {
                finish();
            }
        });
        titleBar.setOnRightButtonClickListener(new MyAppTitle.OnRightButtonClickListener() {
            @Override
            public void OnRightButtonClick(View v) {
                Intent intent = new Intent(KtvRoomActivity.this, PaiMaiListActivity.class);
                intent.putExtra("roomId", roomId);
                startActivity(intent);
            }
        });
    }

    private void connect(String token) {
        RongIMClient.connect(token, new RongIMClient.ConnectCallback() {

            /**
             * Token 错误。可以从下面两点检查 1.  Token 是否过期，如果过期您需要向 App Server 重新请求一个新的 Token
             *                  2.  token 对应的 appKey 和工程里设置的 appKey 是否一致
             */
            @Override
            public void onTokenIncorrect() {
                Log.i(TAG, "success: " + "hahhaa");
            }

            /**
             * 连接融云成功
             * @param userid 当前 token 对应的用户 id
             */
            @Override
            public void onSuccess(String userid) {
                Log.d("LoginActivity", "--onSuccess" + userid);
                reception_id = userid;
//                final UserInfo user = FakeServer.getLoginUser(userid, "");
                UserInfo userInfo = new UserInfo(userid, user_nicename, null);
                LiveKit.addEventHandler(handler);
                LiveKit.setCurrentUser(userInfo);
                initView();
                joinChatRoom(roomId);
            }

            /**
             * 连接融云失败
             * @param errorCode 错误码，可到官网 查看错误码对应的注释
             */
            @Override
            public void onError(RongIMClient.ErrorCode errorCode) {
                Log.i(TAG, "onError: " + errorCode);
            }
        });


    }

    private void initView() {

        background = (ViewGroup) findViewById(R.id.background);
        chatListView = (ChatListView) findViewById(R.id.chat_listview);
        bottomPanel = (BottomPanelFragment) getSupportFragmentManager().findFragmentById(R.id.bottom_bar);
        btnGift = bottomPanel.getView().findViewById(R.id.btn_gift);
        huanzhuang = bottomPanel.getView().findViewById(R.id.rb_huanzhuang);
        rb_paimai = bottomPanel.getView().findViewById(R.id.rb_paimai);
//        btnHeart = bottomPanel.getView().findViewById(R.id.btn_heart);
        heartLayout = findViewById(R.id.heart_layout);
        tvNextSong = findViewById(R.id.tv_next_song);
        release = bottomPanel.getView().findViewById(R.id.release);
        ktv_share = bottomPanel.getView().findViewById(R.id.ktv_share);
        img_show = findViewById(R.id.img_show);
        seekBar = findViewById(R.id.progress_bar);
        lrcView = findViewById(R.id.lrc_view);
        user_avatar = findViewById(R.id.user_avatar);

        chatListAdapter = new ChatListAdapter();
        chatListView.setAdapter(chatListAdapter);
        pullMedia = findViewById(R.id.pull_media);
        push_media = findViewById(R.id.push_media);
        Glide.with(KtvRoomActivity.this)
                .load(R.mipmap.fushi3)
                .placeholder(R.mipmap.fushi3)
                .diskCacheStrategy(DiskCacheStrategy.SOURCE)
                .centerCrop()
                .into(img_show);
        layoutParams = new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        layoutParams.setMargins(470, 430, 0, 0);
        layoutParams.width = 180;
        layoutParams.height = 180;
        user_avatar.setLayoutParams(layoutParams);
        background.setOnClickListener(this);
        btnGift.setOnClickListener(this);
//        btnHeart.setOnClickListener(this);
        bottomPanel.setInputPanelListener(new InputPanel.InputPanelListener() {
            @Override
            public void onSendClick(String text) {
                final TextMessage content = TextMessage.obtain(text);
                LiveKit.sendMessage(content);
            }
        });
        huanzhuang.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showDialog();
            }
        });

        rb_paimai.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //
                Intent intent = new Intent(KtvRoomActivity.this, DianGeListActivity.class);
                intent.putExtra("roomId", roomId);
                startActivity(intent);
            }
        });
        release.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                final NormalDialog dialog = new NormalDialog(KtvRoomActivity.this);
                dialog.content("确定要放麦吗")
                        .style(NormalDialog.STYLE_TWO)
                        .titleTextSize(23)
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
                                mediaPlayer.stop();
                                getNextSong();
                                DreamApi.isSinging(0x002, token, roomId, "0", myCallBack);
                                DreamApi.isSongSinging(0x003, roomId, "1", myCallBack);
//                if (mLivePusher.isPushing()) {
                                lrcView.setVisibility(View.INVISIBLE);

//                }
                                dialog.dismiss();
                            }
                        });

            }
        });

        ktv_share.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //分享
                DreamApi.shareRoom(0x014, token, roomId, myCallBack);

            }
        });
    }

    private static class CustomShareListener implements UMShareListener {

        private WeakReference<KtvRoomActivity> mActivity;

        private CustomShareListener(KtvRoomActivity activity) {
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
                    com.umeng.socialize.utils.Log.d("throw", "throw:" + t.getMessage());
                }
            }

        }

        @Override
        public void onCancel(SHARE_MEDIA platform) {

            Toast.makeText(mActivity.get(), platform + " 分享取消了", Toast.LENGTH_SHORT).show();
        }
    }

    private void showDialog() {
        final Dialog dialog = new Dialog(this, R.style.style_dialog);
        View inflate = LayoutInflater.from(this).inflate(R.layout.dialog_layout, null);
        //初始化控件
        EasyRecyclerView dialogRecyclerView = inflate.findViewById(R.id.dialog_recyclerView);
        dialogRecyclerView.setLayoutManager(new GridLayoutManager(this, 4));
        final FushiAdapter fushiAdapter = new FushiAdapter(R.layout.item_fushi, DataProvider.getFushiList());
        dialogRecyclerView.setAdapter(fushiAdapter);
        fushiAdapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {

                if (position == 3) {
                    layoutParams.setMargins(470, 410, 0, 0);
                } else if (position == 1) {
                    layoutParams.setMargins(460, 510, 0, 0);
                } else if (position == 2) {
                    layoutParams.setMargins(455, 440, 0, 0);
                } else if (position == 4) {
                    layoutParams.setMargins(465, 495, 0, 0);
                } else if (position == 5) {
                    layoutParams.setMargins(490, 450, 0, 0);
                } else if (position == 6) {
                    layoutParams.setMargins(500, 370, 0, 0);
                } else if (position == 0) {
                    layoutParams.setMargins(465, 560, 0, 0);
                }
                layoutParams.width = 180;
                layoutParams.height = 180;
                user_avatar.setLayoutParams(layoutParams);
                Glide.with(KtvRoomActivity.this)
                        .load(fushiAdapter.getItem(position).image)
                        .centerCrop()
                        .into(img_show);

                dialog.dismiss();
            }
        });
        //将布局设置给Dialog
        dialog.setContentView(inflate);
        //获取当前Activity所在的窗体
        Window dialogWindow = dialog.getWindow();
        //设置Dialog从窗体底部弹出
        dialogWindow.setGravity(Gravity.BOTTOM);
        dialogWindow.getDecorView().setPadding(0, 0, 0, 0);
        //获得窗体的属性
        WindowManager.LayoutParams lp = dialogWindow.getAttributes();

        lp.width = LinearLayout.LayoutParams.MATCH_PARENT;//宽高可设置具体大小
        lp.height = LinearLayout.LayoutParams.WRAP_CONTENT;//宽高可设置具体大小

//       将属性设置给窗体
        dialogWindow.setAttributes(lp);
        dialog.show();//显示对话框
    }

    private void joinChatRoom(final String roomId) {
        LiveKit.joinChatRoom(roomId, 2, new RongIMClient.OperationCallback() {
            @Override
            public void onSuccess() {
                final InformationNotificationMessage content = InformationNotificationMessage.obtain("加入了房间");
//                RongIMClient.getInstance().getChatRoomInfo(roomId, 10, ChatRoomInfo.ChatRoomMemberOrder.RC_CHAT_ROOM_MEMBER_ASC, new RongIMClient.ResultCallback<ChatRoomInfo>() {
//                    @Override
//                    public void onSuccess(ChatRoomInfo chatRoomInfo) {
//                        Log.i(TAG, "onSuccess: " + chatRoomInfo);
//                        List<ChatRoomMemberInfo> memberInfo = chatRoomInfo.getMemberInfo();
//                        int totalMemberCount = chatRoomInfo.getTotalMemberCount();
//                    }
//
//                    @Override
//                    public void onError(RongIMClient.ErrorCode errorCode) {
//                        Log.i(TAG, "onError: " + errorCode);
//                    }
//                });
                LiveKit.sendMessage(content);
            }

            @Override
            public void onError(RongIMClient.ErrorCode errorCode) {
                Toast.makeText(KtvRoomActivity.this, "聊天室加入失败! errorCode = " + errorCode, Toast.LENGTH_SHORT).show();
                Log.i(TAG, "onError: " + errorCode);
            }
        });
    }

    //    @Override
//    public void onBackPressed() {
//        if (!bottomPanel.onBackAction()) {
//            finish();
//            return;
//        }
//    }
    @Override
    public void onClick(View v) {
        if (v.equals(background)) {
            bottomPanel.onBackAction();
        } else if (v.equals(btnGift)) {
            showGiftPop();
            GiftMessage msg = new GiftMessage("2", "送您一个礼物");
            LiveKit.sendMessage(msg);
        }
//        else if (v.equals(btnHeart)) {
//            heartLayout.post(new Runnable() {
//                @Override
//                public void run() {
//                    int rgb = Color.rgb(random.nextInt(255), random.nextInt(255), random.nextInt(255));
//                    heartLayout.addHeart(rgb);
//                }
//            });
//            GiftMessage msg = new GiftMessage("1", "为您点赞");
//            LiveKit.sendMessage(msg);
//        }
    }


    private TextView all_gift;
    private TextView my_gift;
    private TextView gold_number;
    private TextView to_charge;
    private PopupWindow popWindow;
    private PopupWindow popGiftWindow;
    private RecyclerView giftRecyclerView;

    private void showGiftPop() {
        if (popGiftWindow == null) {
            LayoutInflater layoutInflater = (LayoutInflater) this.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            View view = layoutInflater.inflate(R.layout.popwindow_gift, null);
            // 创建一个PopuWidow对象
            popGiftWindow = new PopupWindow(view, LinearLayout.LayoutParams.FILL_PARENT, 600, true);

            giftRecyclerView = view.findViewById(R.id.recyclerView);
            GridLayoutManager gridLayoutManager = new GridLayoutManager(KtvRoomActivity.this, 4);
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
        popGiftWindow.showAtLocation(btnGift, Gravity.BOTTOM, 0, 0);


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
    public boolean handleMessage(android.os.Message msg) {
        switch (msg.what) {
            case LiveKit.MESSAGE_ARRIVED: {
                MessageContent content = (MessageContent) msg.obj;
                chatListAdapter.addMessage(content);
                break;
            }
            case LiveKit.MESSAGE_SENT: {
                MessageContent content = (MessageContent) msg.obj;
                chatListAdapter.addMessage(content);
                break;
            }
            case LiveKit.MESSAGE_SEND_ERROR: {
                break;
            }
            default:
        }
        chatListAdapter.notifyDataSetChanged();
        return false;
    }

    @Override
    protected void onDestroy() {
        //关闭聊天室
        LiveKit.quitChatRoom(new RongIMClient.OperationCallback() {
            @Override
            public void onSuccess() {
                LiveKit.removeEventHandler(handler);
                LiveKit.logout();
                Toast.makeText(KtvRoomActivity.this, "退出聊天室成功", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onError(RongIMClient.ErrorCode errorCode) {
                LiveKit.removeEventHandler(handler);
                LiveKit.logout();
                Toast.makeText(KtvRoomActivity.this, "退出聊天室失败! errorCode = " + errorCode, Toast.LENGTH_SHORT).show();
            }
        });
        //关闭轮询
        if (timer != null) {
            timer.cancel();
            timer = null;
        }
        if (task != null) {
            task.cancel();
            task = null;
        }
        //关闭轮询
        if (timer2 != null) {
            timer2.cancel();
            timer2 = null;
        }
        if (onLineTask != null) {
            onLineTask.cancel();
            onLineTask = null;
        }
        //停止拉流
        if (mLivePlayer != null) {
            mLivePlayer.stopPlay(true);
            mLivePlayer = null;
        }
        if (pullMedia != null) {
            pullMedia.onDestroy();
            pullMedia = null;
        }

        //停止推流
        if (mLivePusher != null && mLivePusher.isPushing()) {
            stopPublishRtmp();
        }
        //停止唱歌
        if (mediaPlayer != null && mediaPlayer.isPlaying()) {
            mediaPlayer.stop();
        }
        DreamApi.isSinging(0x002, token, roomId, "0", myCallBack);
        DreamApi.isSongSinging(0x003, roomId, "1", myCallBack);
        if (dialog != null) {
            dialog.cancel();
        }
        super.onDestroy();
    }

    @Override
    public void onBackPressed() {
        stopPlayRtmp();
        if (mLivePusher != null && mLivePusher.isPushing()) {
            stopPublishRtmp();
        }
        super.onBackPressed();
    }

    private void stopPublishRtmp() {
        if (mLivePusher != null) {
            mLivePusher.stopCameraPreview(true);
            mLivePusher.stopScreenCapture();
            mLivePusher.setPushListener(null);
            mLivePusher.stopPusher();
            Log.i(TAG, "stopPublishRtmp: " + "停止推流");
        }

    }

    private void stopPlayRtmp() {
        if (mLivePlayer != null) {
            mLivePlayer.stopRecord();
            mLivePlayer.setPlayListener(null);
            mLivePlayer.stopPlay(true);
        }
        if (pullMedia != null) {
            pullMedia.onDestroy();
            pullMedia = null;
        }
    }
}
