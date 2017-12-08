package com.zhuye.zhengmeng.pay;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextPaint;
import android.view.View;
import android.widget.TextView;

import com.zhuye.zhengmeng.MainActivity;
import com.zhuye.zhengmeng.R;
import com.zhuye.zhengmeng.base.BaseActivity;
import com.zhuye.zhengmeng.view.MyAppTitle;

import butterknife.BindView;
import butterknife.ButterKnife;

public class PaySuccessActivity extends BaseActivity {
    @BindView(R.id.titleBar)
    MyAppTitle titleBar;
    @BindView(R.id.order_price)
    TextView orderPrice;
    @BindView(R.id.order_number)
    TextView orderNumber;
    @BindView(R.id.order_time)
    TextView orderTime;
    @BindView(R.id.return_home)
    TextView returnHome;

    @Override
    protected void processLogic() {
        TextPaint hotPaint = orderPrice.getPaint();
        hotPaint.setFakeBoldText(true);
//        String course_name = getIntent().getStringExtra("course_name");
        String total_amount = getIntent().getStringExtra("total_amount");
        String out_trade_no = getIntent().getStringExtra("out_trade_no");
        String timestamp = getIntent().getStringExtra("timestamp");
        String tag = getIntent().getStringExtra("tag");
        if (tag.equals("ali")) {
            orderPrice.setText(total_amount);
        } else if (tag.equals("wechat")&&total_amount!=null) {
            orderPrice.setText(total_amount+"");
        }
        orderTime.setText(timestamp);
        orderNumber.setText(out_trade_no);
    }

    @Override
    protected void setListener() {
        setTitle();
        returnHome.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(PaySuccessActivity.this, MainActivity.class);
                startActivity(intent);
            }
        });
    }

    private void setTitle() {
        titleBar.initViewsVisible(true, true, false, false);
        titleBar.setAppTitle("支付成功");
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
        setContentView(R.layout.activity_pay_success);
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
