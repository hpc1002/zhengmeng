package com.zhuye.zhengmeng.KTV;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.media.MediaPlayer;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentActivity;
import android.support.v7.widget.GridLayoutManager;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import com.blankj.utilcode.util.SPUtils;
import com.bumptech.glide.Glide;
import com.chad.library.adapter.base.BaseQuickAdapter;
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
import com.zhuye.zhengmeng.Constant;
import com.zhuye.zhengmeng.DataProvider;
import com.zhuye.zhengmeng.KTV.adapter.FushiAdapter;
import com.zhuye.zhengmeng.KTV.bean.NextSongBean;
import com.zhuye.zhengmeng.LiveKit;
import com.zhuye.zhengmeng.R;
import com.zhuye.zhengmeng.bangdan.DianGeListActivity;
import com.zhuye.zhengmeng.chatRoom.controller.ChatListAdapter;
import com.zhuye.zhengmeng.chatRoom.fragment.BottomPanelFragment;
import com.zhuye.zhengmeng.chatRoom.message.GiftMessage;
import com.zhuye.zhengmeng.http.DreamApi;
import com.zhuye.zhengmeng.http.MyCallBack;
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
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.Timer;
import java.util.TimerTask;

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
    private Timer timer;
    private Task task;
    private TXLivePlayer mLivePlayer;
    private TXCloudVideoView pullMedia;
    private TXCloudVideoView push_media;
    private TXLivePushConfig mLivePushConfig;
    private TXLivePusher mLivePusher;
    private TXLivePlayConfig mPlayConfig;
    private TextView tvNextSong;
    private Button release;
    private String lyric_path;
    private String user_id;
    private String song_path;
    private String is_play;
    //歌词
    private MediaPlayer mediaPlayer = new MediaPlayer();
    private boolean repared = false;//判断mediaPlayer是否初始化完成
    boolean mIsPlay = false;
    private SeekBar seekBar;
    private LrcView lrcView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_ktv_room);
        checkPublishPermission();
        roomId = getIntent().getStringExtra("roomId");
        chatroom_name = getIntent().getStringExtra("chatroom_name");
        rongcloudToken = SPUtils.getInstance("userInfo").getString("rongcloudToken");
        token = SPUtils.getInstance("userInfo").getString("token");
        userId = SPUtils.getInstance("userInfo").getString("userId");
        user_nicename = SPUtils.getInstance("userInfo").getString("user_nicename");


        connect(rongcloudToken);
        setTitle();


        //获取下首播放歌曲
        getNextSong();

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
        mPlayConfig = new TXLivePlayConfig();
        //创建player对象
        mLivePlayer = new TXLivePlayer(this);
        //关键player对象与界面view
        mLivePlayer.setPlayerView(pullMedia);
        mLivePlayer.setConfig(mPlayConfig);
        mLivePlayer.setAutoPlay(true);
        String flvUrl = "rtmp://15904.liveplay.myqcloud.com/live/15904_d38e58baec";
        mLivePlayer.startPlay(flvUrl, TXLivePlayer.PLAY_TYPE_LIVE_RTMP); //推荐FLV

    }

    private void getNextSong() {
        timer = new Timer();
        task = new Task();
        timer.schedule(task, 0, 10 * 1000);
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
                            if (song_path != null) {
                                tvNextSong.setText("下首歌曲" + Constant.BASE_URL2 + song_path);
                            }
//                            if (mLivePusher == null) {
//                                DreamApi.isSinging(0x002, token, roomId, "0", myCallBack);
//                            }else{
//                                DreamApi.isSinging(0x002, token, roomId, "1", myCallBack);
//                            }
//
                            if (is_play.equals("1")){
                                //拉流
                                pullMedia();
                            }
                            if (user_id != null && user_id.equals(userId) && is_play.equals("0")) {
                                //轮到自己，停止拉流，开始推流
                                stopPlayRtmp();
//                                ToastManager.show("该我推流了");
                                showPushDialog();


                                release.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View view) {
                                        mediaPlayer.stop();
                                        if (mLivePusher.isPushing()) {
                                            stopPublishRtmp();
                                            pullMedia();

                                            lrcView.setVisibility(View.GONE);
                                            DreamApi.isSinging(0x002, token, roomId, "0", myCallBack);
                                            DreamApi.isSongSinging(0x003, roomId, "1", myCallBack);
                                        }
                                    }
                                });
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
        mDialog = new AlertDialog.Builder(KtvRoomActivity.this)
                .setCustomTitle(title)
                .setCancelable(false)
                .setView(mOffTextView)
                .setPositiveButton("开始演唱", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {

                        //加载歌词
                        lrcView.setVisibility(View.VISIBLE);
                        mIsPlay = true;
                        try {
                            mediaPlayer.reset();
                            mediaPlayer.setDataSource(Constant.BASE_URL_PINJIE + song_path);
                            mediaPlayer.prepareAsync();
                            mediaPlayer.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
                                @Override
                                public void onPrepared(MediaPlayer mp) {
                                    seekBar.setMax(mediaPlayer.getDuration());
                                    seekBar.setProgress(0);
                                    repared = true;
                                    mediaPlayer.start();
                                    DreamApi.isSinging(0x002, token, roomId, "1", myCallBack);
                                }
                            });
                            mediaPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
                                @Override
                                public void onCompletion(MediaPlayer mp) {
                                    lrcView.updateTime(0);
                                    seekBar.setProgress(0);
                                    if (repared && !mediaPlayer.isPlaying()) {
                                        stopPublishRtmp();
                                        pullMedia();
                                        mediaPlayer.stop();
                                        DreamApi.isSinging(0x002, token, roomId, "0", myCallBack);
                                        DreamApi.isSongSinging(0x003, roomId, "1", myCallBack);
                                    }

                                }
                            });
                            mediaPlayer.setOnErrorListener(new MediaPlayer.OnErrorListener() {
                                @Override
                                public boolean onError(MediaPlayer mediaPlayer, int i, int i1) {

                                    mediaPlayer.start();
                                    DreamApi.isSinging(0x002, token, roomId, "1", myCallBack);
                                    return true;
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

                    }
                }).setNegativeButton("放弃演唱", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        dialogInterface.cancel();
                        mOffTime.cancel();
                        stopPublishRtmp();
                        pullMedia();
                    }
                }).create();
        mDialog.show();
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
                        pullMedia();
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

        mLivePusher = new TXLivePusher(this);
        mLivePushConfig = new TXLivePushConfig();
        mLivePusher.setConfig(mLivePushConfig);
//        mLivePushConfig.setPauseImg(300, 5);
        // 只有在推流启动前设置启动纯音频推流才会生效，推流过程中设置不会生效。
        mLivePushConfig.enablePureAudioPush(true);   // true 为启动纯音频推流，而默认值是 false；
        mLivePusher.setConfig(mLivePushConfig);      // 重新设置 config
        String rtmpUrl = "rtmp://15904.livepush.myqcloud.com/live/15904_d38e58baec?bizid=15904&txSecret=ce8b00d6e1ebbe3816eb50244772666e&txTime=5A19937F";
        mLivePusher.startCameraPreview(push_media);
        mLivePusher.startPusher(rtmpUrl);
    }

    private void setTitle() {
        MyAppTitle titleBar = findViewById(R.id.titleBar);
        titleBar.initViewsVisible(true, true, false, true);
        titleBar.setTitleSize(20);
        titleBar.setRightTitle("排麦列表");
        titleBar.setAppTitle(chatroom_name);
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
        release = findViewById(R.id.release);
        img_show = findViewById(R.id.img_show);
        seekBar = findViewById(R.id.progress_bar);
        lrcView = findViewById(R.id.lrc_view);

        chatListAdapter = new ChatListAdapter();
        chatListView.setAdapter(chatListAdapter);
        pullMedia = findViewById(R.id.pull_media);
        push_media = findViewById(R.id.push_media);




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
