package com.zhuye.zhengmeng.user;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.zhuye.zhengmeng.R;
import com.zhuye.zhengmeng.base.BaseActivity;
import com.zhuye.zhengmeng.view.MyAppTitle;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * 提现
 */
public class WithdrawalsActivity extends BaseActivity implements View.OnClickListener {


    @BindView(R.id.titleBar)
    MyAppTitle titleBar;
    @BindView(R.id.tv_bankCard)
    TextView tvBankCard;
    @BindView(R.id.tag01)
    ImageView tag01;
    @BindView(R.id.zichan)
    TextView zichan;
    @BindView(R.id.tixian_num)
    TextView tixianNum;
    @BindView(R.id.whole_tixian)
    TextView wholeTixian;
    @BindView(R.id.withdrawals)
    TextView withdrawals;

    @Override
    protected void processLogic() {

    }

    @Override
    protected void setListener() {
        setTitle();
        withdrawals.setOnClickListener(this);
    }

    private void setTitle() {
        titleBar.initViewsVisible(true, true, false, false);
        titleBar.setTitleSize(20);
        titleBar.setAppTitle("提现");
        titleBar.setOnLeftButtonClickListener(new MyAppTitle.OnLeftButtonClickListener() {
            @Override
            public void onLeftButtonClick(View v) {
                finish();
            }
        });
    }

    @Override
    protected void loadViewLayout() {
        setContentView(R.layout.activity_withdrawals);
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

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.withdrawals:
                startActivity(new Intent(this, BindCardActivity.class));
                break;
        }
    }
}
