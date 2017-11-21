package com.zhuye.zhengmeng.user;

import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.RelativeLayout;
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

public class BindCardActivity extends BaseActivity {


    @BindView(R.id.titleBar)
    MyAppTitle titleBar;
    @BindView(R.id.tv_bank)
    TextView tvBank;
    @BindView(R.id.tv_bankName)
    EditText tvBankName;
    @BindView(R.id.rl_bank)
    RelativeLayout rlBank;
    @BindView(R.id.tv_card)
    TextView tvCard;
    @BindView(R.id.tv_card_number)
    EditText tvCardNumber;
    @BindView(R.id.rl_card_number)
    RelativeLayout rlCardNumber;
    @BindView(R.id.tv_name)
    TextView tvName;
    @BindView(R.id.tv_name_input)
    EditText tvNameInput;
    @BindView(R.id.rl_name)
    RelativeLayout rlName;
    @BindView(R.id.quick_bind)
    RelativeLayout quickBind;
    private String token;

    @Override
    protected void processLogic() {

    }

    @Override
    protected void setListener() {
        token = SPUtils.getInstance("userInfo").getString("token");
        setTitle();

        quickBind.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String cardNumber = tvCardNumber.getText().toString();
                String bankName = tvBankName.getText().toString();
                String userName = tvNameInput.getText().toString();
                if (cardNumber.equals("")) {
                    ToastManager.show("银行卡号不能为空");
                    return;
                }
                if (bankName.equals("")) {
                    ToastManager.show("银行不能为空");
                    return;
                }
                if (userName.equals("")) {
                    ToastManager.show("持卡人姓名不能为空");
                    return;
                }
                DreamApi.addBankCard(0x001, token, cardNumber, bankName, userName, myCallBack);
            }
        });
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
                            ToastManager.show(jsonObject.getString("msg"));
                            finish();
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

    private void setTitle() {
        titleBar.initViewsVisible(true, true, false, false);
        titleBar.setTitleSize(20);
        titleBar.setAppTitle("添加银行卡");
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
