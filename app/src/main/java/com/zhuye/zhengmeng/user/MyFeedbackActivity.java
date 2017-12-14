package com.zhuye.zhengmeng.user;

import android.content.Context;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

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

public class MyFeedbackActivity extends BaseActivity {


    @BindView(R.id.titleBar)
    MyAppTitle titleBar;
    @BindView(R.id.editText)
    EditText editText;
    @BindView(R.id.submit)
    Button submit;
    private String token;

    @Override
    protected void processLogic() {

    }

    @Override
    protected void setListener() {
        setTitle();
        token = SPUtils.getInstance("userInfo").getString("token");
        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String content = editText.getText().toString();
                //问题提交发起请求
                DreamApi.feedback(0x001, token, content, myCallBack);
            }
        });
    }

    private MyCallBack myCallBack = new MyCallBack() {
        @Override
        public void onSuccess(int what, Response<String> result) {
            switch (what) {
                case 0x001:
                    JSONObject jsonObject = null;
                    try {
                        jsonObject = new JSONObject(result.body());
                        int code = jsonObject.getInt("code");
                        String msg = jsonObject.getString("msg");
                        if (code == 200) {
                            ToastManager.show(msg);
                            finish();
                        } else if (code == 300) {
                            ToastManager.show(msg);
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
        titleBar.setAppTitle("问题反馈");
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
        setContentView(R.layout.activity_my_feedback);
    }

    @Override
    protected Context getActivityContext() {
        return this;
    }
}
