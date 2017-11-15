package com.zhuye.zhengmeng.KTV;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.blankj.utilcode.util.SPUtils;
import com.ksyun.media.player.KSYMediaPlayer;
import com.zhuye.zhengmeng.LiveKit;
import com.zhuye.zhengmeng.R;
import com.zhuye.zhengmeng.bangdan.DianGeListActivity;
import com.zhuye.zhengmeng.bangdan.SongsListActivity;
import com.zhuye.zhengmeng.chatRoom.controller.ChatListAdapter;
import com.zhuye.zhengmeng.chatRoom.fragment.BottomPanelFragment;
import com.zhuye.zhengmeng.chatRoom.message.GiftMessage;
import com.zhuye.zhengmeng.utils.ToastManager;
import com.zhuye.zhengmeng.view.MyAppTitle;
import com.zhuye.zhengmeng.widget.ChatListView;
import com.zhuye.zhengmeng.widget.InputPanel;
import com.zhuye.zhengmeng.widget.animation.HeartLayout;

import java.util.Random;

import io.rong.imlib.RongIMClient;
import io.rong.imlib.model.MessageContent;
import io.rong.imlib.model.UserInfo;
import io.rong.message.InformationNotificationMessage;
import io.rong.message.TextMessage;

public class KtvRoomActivity extends FragmentActivity implements View.OnClickListener, Handler.Callback {

    public static final String LIVE_URL = "live_url";

    private ViewGroup background;
    private ChatListView chatListView;
    private BottomPanelFragment bottomPanel;
    private TextView btnGift;
    private TextView huanzhuang;
    private TextView zhuanfa;
    private TextView rb_paimai;
    //    private ImageView btnHeart;
    private HeartLayout heartLayout;
    private SurfaceView surfaceView;

    private Random random = new Random();
    private Handler handler = new Handler(this);
    private ChatListAdapter chatListAdapter;
    private String roomId;
    private KSYMediaPlayer ksyMediaPlayer;
    private SurfaceHolder surfaceHolder;
    private String rongcloudToken;
    private String user_nicename;
    private String chatroom_name;
    private String userId;
    private static final String TAG = "KtvRoomActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ktv_room);
        roomId = getIntent().getStringExtra("roomId");
        chatroom_name = getIntent().getStringExtra("chatroom_name");
        rongcloudToken = SPUtils.getInstance("userInfo").getString("rongcloudToken");
        userId = SPUtils.getInstance("userInfo").getString("userId");
        user_nicename = SPUtils.getInstance("userInfo").getString("user_nicename");
        connect(rongcloudToken);
        setTitle();
    }

    private void setTitle() {
        MyAppTitle titleBar = findViewById(R.id.titleBar);
        titleBar.initViewsVisible(true, true, false, false);
        titleBar.setTitleSize(20);
        titleBar.setAppTitle(chatroom_name);
        titleBar.setOnLeftButtonClickListener(new MyAppTitle.OnLeftButtonClickListener() {
            @Override
            public void onLeftButtonClick(View v) {
                finish();
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
        zhuanfa = bottomPanel.getView().findViewById(R.id.rb_zhuanfa);
        rb_paimai = bottomPanel.getView().findViewById(R.id.rb_paimai);
//        btnHeart = bottomPanel.getView().findViewById(R.id.btn_heart);
        heartLayout = findViewById(R.id.heart_layout);
        surfaceView = findViewById(R.id.player_surface);

        chatListAdapter = new ChatListAdapter();
        chatListView.setAdapter(chatListAdapter);
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
                //
                ToastManager.show("换装");

            }
        });
        zhuanfa.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //
                ToastManager.show("转发");
            }
        });
        rb_paimai.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //
//                Intent intent = new Intent(KtvRoomActivity.this, PaiMaiActivity.class);
                Intent intent = new Intent(KtvRoomActivity.this, DianGeListActivity.class);
                intent.putExtra("roomId",roomId);
                startActivity(intent);
            }
        });
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
        super.onDestroy();
    }
}
