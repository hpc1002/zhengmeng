package com.zhuye.zhengmeng.user;

import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.blankj.utilcode.util.SPUtils;
import com.lzy.okgo.model.Response;
import com.zhuye.zhengmeng.R;
import com.zhuye.zhengmeng.base.BaseActivity;
import com.zhuye.zhengmeng.http.DreamApi;
import com.zhuye.zhengmeng.http.MyCallBack;
import com.zhuye.zhengmeng.utils.ToastManager;
import com.zhuye.zhengmeng.view.MyAppTitle;

import org.json.JSONException;
import org.json.JSONObject;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * 提现
 */
public class WithdrawalsActivity extends BaseActivity implements View.OnClickListener {


    @BindView(R.id.titleBar)
    MyAppTitle titleBar;
    @BindView(R.id.tv_bankCard)
    EditText tvBankCard;
    @BindView(R.id.tag01)
    ImageView tag01;
    @BindView(R.id.withdrawals)
    TextView withdrawals;
    @BindView(R.id.tv_number)
    EditText tiXianNumber;
    @BindView(R.id.tv_phone_number)
    EditText phoneNumber;
    private String token;
    private String type;

    @Override
    protected void processLogic() {

    }

    @Override
    protected void setListener() {
        type = getIntent().getStringExtra("type");
        token = SPUtils.getInstance("userInfo").getString("token");
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
                String tixianNum = tiXianNumber.getText().toString();
                String phoneMumber = phoneNumber.getText().toString();
                String bankNumber = tvBankCard.getText().toString();
                DreamApi.tiXian(0x001, token, type, tixianNum, bankNumber, phoneMumber, new MyCallBack() {
                    @Override
                    public void onSuccess(int what, Response<String> result) {
                        try {
                            JSONObject jsonObject = new JSONObject(result.body());
                            int code = jsonObject.getInt("code");
                            if (code == 200) {
                                String msg = jsonObject.getString("msg");
                                ToastManager.show(msg);
                                finish();
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

                    }
                });
                break;
        }
    }
}
