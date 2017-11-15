package com.zhuye.zhengmeng.user;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.lzy.okgo.model.Response;
import com.zhuye.zhengmeng.R;
import com.zhuye.zhengmeng.base.BaseActivity;
import com.zhuye.zhengmeng.http.MyCallBack;
import com.zhuye.zhengmeng.view.MyAppTitle;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * 我的消息
 */
public class MyMessageActivity extends BaseActivity implements View.OnClickListener{


    @BindView(R.id.titleBar)
    MyAppTitle titleBar;
    @BindView(R.id.tv_title)
    TextView tvTitle;
    @BindView(R.id.message)
    TextView message;
    @BindView(R.id.notice)
    LinearLayout notice;
    @BindView(R.id.Invitation_title)
    TextView InvitationTitle;
    @BindView(R.id.friend_text)
    TextView friendText;
    @BindView(R.id.Invitation)
    LinearLayout Invitation;
    private String token;

    @Override
    protected void processLogic() {


    }

    @Override
    protected void setListener() {
        setTitle();
        notice.setOnClickListener(this);
        Invitation.setOnClickListener(this);
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
        switch (view.getId()){
            case R.id.notice://官方公告
                startActivity(new Intent(MyMessageActivity.this,NoticeActivity.class));
                break;
            case R.id.Invitation:
                startActivity(new Intent(MyMessageActivity.this,FriendInvitationActivity.class));
                break;
        }
    }
}
