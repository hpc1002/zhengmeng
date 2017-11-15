package com.zhuye.zhengmeng.user;

import android.content.Context;
import android.os.Bundle;
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
import butterknife.ButterKnife;

public class ChangeUserNameActivity extends BaseActivity {

    @BindView(R.id.titleBar)
    MyAppTitle titleBar;
    @BindView(R.id.change_userName_Ev)
    EditText changeUserNameEv;


    @Override
    protected void processLogic() {

    }

    @Override
    protected void setListener() {
        setTitle();
    }

    @Override
    protected void loadViewLayout() {
        setContentView(R.layout.activity_change_user_name);
    }

    @Override
    protected Context getActivityContext() {
        return null;
    }

    private void setTitle() {
        titleBar.initViewsVisible(true, true, false, true);
        titleBar.setAppTitle("用户名");
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
                final String changeUserName = changeUserNameEv.getText().toString();
                DreamApi.changeUserInfo(ChangeUserNameActivity.this, 0x001, token, "3", changeUserName, new MyCallBack() {
                    @Override
                    public void onSuccess(int what, Response<String> result) {
                        try {
                            JSONObject jsonObject = new JSONObject(result.body());
                            int code = jsonObject.optInt("code");
                            if (code == 200){
                                ToastUtils.showShort("昵称修改成功");
                                SPUtils.getInstance("userInfo").put("user_nicename",changeUserName);
                                finish();
                            } else {
                                ToastUtils.showShort("昵称修改失败");
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }

                    @Override
                    public void onFail(int what, Response<String> result) {
                        ToastUtils.showShort("昵称修改失败");
                    }

                    @Override
                    public void onFinish(int what) {

                    }
                });
            }
        });
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // TODO: add setContentView(...) invocation
        ButterKnife.bind(this);
    }
}
