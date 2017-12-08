package com.zhuye.zhengmeng.user;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.blankj.utilcode.util.SPUtils;
import com.lzy.okgo.model.Response;
import com.zhuye.zhengmeng.DataProvider;
import com.zhuye.zhengmeng.R;
import com.zhuye.zhengmeng.base.BaseActivity;
import com.zhuye.zhengmeng.http.MyCallBack;
import com.zhuye.zhengmeng.user.adapter.MessageAdapter;
import com.zhuye.zhengmeng.user.bean.MessageBean;
import com.zhuye.zhengmeng.view.MyAppTitle;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * 我的消息
 */
public class MyMessageActivity extends BaseActivity implements View.OnClickListener {


    @BindView(R.id.titleBar)
    MyAppTitle titleBar;
    @BindView(R.id.recyclerView)
    RecyclerView recyclerView;
    //    @BindView(R.id.tv_title)
//    TextView tvTitle;
//    @BindView(R.id.message)
//    TextView message;
//    @BindView(R.id.notice)
//    LinearLayout notice;
//    @BindView(R.id.Invitation_title)
//    TextView InvitationTitle;
//    @BindView(R.id.friend_text)
//    TextView friendText;
//    @BindView(R.id.Invitation)
//    LinearLayout Invitation;
    private String token;

    @Override
    protected void processLogic() {
        List<MessageBean> messageList = DataProvider.getMessageList();
        MessageAdapter messageAdapter = new MessageAdapter(R.layout.item_message, messageList);
        recyclerView.setAdapter(messageAdapter);
    }

    @Override
    protected void setListener() {
        token = SPUtils.getInstance("userInfo").getString("token");
        setTitle();
//        notice.setOnClickListener(this);
//        Invitation.setOnClickListener(this);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
    }

    private void setTitle() {

        titleBar.initViewsVisible(true, true, false, false);
        titleBar.setAppTitle("我的消息");
        titleBar.setTitleSize(20);
        titleBar.setOnLeftButtonClickListener(new MyAppTitle.OnLeftButtonClickListener() {
            @Override
            public void onLeftButtonClick(View v) {
                finish();
            }
        });

    }

    @Override
    protected void loadViewLayout() {
        setContentView(R.layout.activity_my_message);
    }

    @Override
    protected Context getActivityContext() {
        return this;
    }

    private MyCallBack myCallBack = new MyCallBack() {
        @Override
        public void onSuccess(int what, Response<String> result) {
            switch (what) {
                case 0x001:

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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // TODO: add setContentView(...) invocation
        ButterKnife.bind(this);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.notice://官方公告
                startActivity(new Intent(MyMessageActivity.this, NoticeActivity.class));
                break;
            case R.id.Invitation:
                startActivity(new Intent(MyMessageActivity.this, FriendInvitationActivity.class));
                break;
        }
    }
}
