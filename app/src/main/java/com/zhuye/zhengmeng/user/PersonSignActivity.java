package com.zhuye.zhengmeng.user;

import android.content.Context;
import android.view.View;
import android.widget.EditText;

import com.blankj.utilcode.util.SPUtils;
import com.blankj.utilcode.util.ToastUtils;
import com.lzy.okgo.model.Response;
import com.zhuye.zhengmeng.R;
import com.zhuye.zhengmeng.base.BaseActivity;
import com.zhuye.zhengmeng.http.DreamApi;
import com.zhuye.zhengmeng.http.MyCallBack;
import com.zhuye.zhengmeng.view.MyAppTitle;

import org.json.JSONException;
import org.json.JSONObject;

import butterknife.BindView;

public class PersonSignActivity extends BaseActivity {

    @BindView(R.id.titleBar)
    MyAppTitle titleBar;
    @BindView(R.id.change_user_autograph_Ev)
    EditText change_user_autographEv;

    @Override
    protected void processLogic() {

    }

    @Override
    protected void setListener() {
        setTitle();
    }

    @Override
    protected void loadViewLayout() {
        setContentView(R.layout.activity_person_sign);
    }

    @Override
    protected Context getActivityContext() {
        return this;
    }

    private void setTitle() {
        titleBar.initViewsVisible(true, true, false, true);
        titleBar.setAppTitle("个人签名");
        titleBar.setRightTitle("保存");
        titleBar.setTitleSize(20);
        titleBar.setOnLeftButtonClickListener(new MyAppTitle.OnLeftButtonClickListener() {
            @Override
            public void onLeftButtonClick(View v) {
                finish();
            }
        });
        titleBar.setOnRightButtonClickListener(new MyAppTitle.OnRightButtonClickListener() {
            @Override
            public void OnRightButtonClick(View v) {
                //请求接口
                String token = SPUtils.getInstance("userInfo").getString("token");
                final String user_autograph = change_user_autographEv.getText().toString();
                DreamApi.changeUserInfo(PersonSignActivity.this, 0x001, token, "1", user_autograph, new MyCallBack() {
                    @Override
                    public void onSuccess(int what, Response<String> result) {
                        try {
                            JSONObject jsonObject = new JSONObject(result.body());
                            int code = jsonObject.optInt("code");
                            if (code == 200) {
                                ToastUtils.showShort("签名修改成功");
                                SPUtils.getInstance("userInfo").put("user_autograph", user_autograph);
                                finish();
                            } else {
                                ToastUtils.showShort("签名修改失败");
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }

                    @Override
                    public void onFail(int what, Response<String> result) {
                        ToastUtils.showShort("签名修改失败");
                    }

                    @Override
                    public void onFinish(int what) {

                    }
                });
            }
        });
    }
}
