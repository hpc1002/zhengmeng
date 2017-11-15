package com.zhuye.zhengmeng.user;

import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.zhuye.zhengmeng.R;
import com.zhuye.zhengmeng.base.BaseActivity;
import com.zhuye.zhengmeng.view.MyAppTitle;

import butterknife.BindView;
import butterknife.ButterKnife;

public class BindCardActivity extends BaseActivity {

    @BindView(R.id.titleBar)
    MyAppTitle titleBar;
    @BindView(R.id.tv_bank)
    TextView tvBank;
    @BindView(R.id.tv_bankName)
    TextView tvBankName;
    @BindView(R.id.rl_bank)
    RelativeLayout rlBank;
    @BindView(R.id.tv_card)
    TextView tvCard;
    @BindView(R.id.tv_card_number)
    TextView tvCardNumber;
    @BindView(R.id.rl_card_number)
    RelativeLayout rlCardNumber;
    @BindView(R.id.tv_name)
    TextView tvName;
    @BindView(R.id.tv_name_input)
    TextView tvNameInput;
    @BindView(R.id.rl_name)
    RelativeLayout rlName;
    @BindView(R.id.tv_idcard)
    TextView tvIdcard;
    @BindView(R.id.tv_idcard_number)
    TextView tvIdcardNumber;
    @BindView(R.id.rl_idCard)
    RelativeLayout rlIdCard;
    @BindView(R.id.tv_phone)
    TextView tvPhone;
    @BindView(R.id.tv_phone_number)
    TextView tvPhoneNumber;
    @BindView(R.id.rl_phone)
    RelativeLayout rlPhone;
    @BindView(R.id.tv_sms)
    TextView tvSms;
    @BindView(R.id.tv_smsCode)
    TextView tvSmsCode;
    @BindView(R.id.tv_send_smsCode)
    TextView tvSendSmsCode;
    @BindView(R.id.rl_smsCode)
    RelativeLayout rlSmsCode;

    @Override
    protected void processLogic() {

    }

    @Override
    protected void setListener() {
        setTitle();
    }

    private void setTitle() {
        titleBar.initViewsVisible(true, true, false, false);
        titleBar.setTitleSize(20);
        titleBar.setAppTitle("绑定银行卡");
        titleBar.setOnLeftButtonClickListener(new MyAppTitle.OnLeftButtonClickListener() {
            @Override
            public void onLeftButtonClick(View v) {
                finish();
            }
        });
    }

    @Override
    protected void loadViewLayout() {
        setContentView(R.layout.activity_bind_card);
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
