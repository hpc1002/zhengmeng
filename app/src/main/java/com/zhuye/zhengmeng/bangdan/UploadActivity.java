package com.zhuye.zhengmeng.bangdan;

import android.content.Context;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.blankj.utilcode.util.FileUtils;
import com.blankj.utilcode.util.SPUtils;
import com.blankj.utilcode.util.ToastUtils;
import com.lzy.okgo.model.Progress;
import com.lzy.okgo.model.Response;
import com.zhuye.zhengmeng.R;
import com.zhuye.zhengmeng.base.BaseActivity;
import com.zhuye.zhengmeng.http.DreamApi;
import com.zhuye.zhengmeng.http.UploadCallBack;
import com.zhuye.zhengmeng.view.MyAppTitle;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;

import butterknife.BindView;

public class UploadActivity extends BaseActivity implements View.OnClickListener {

    @BindView(R.id.titleBar)
    MyAppTitle titleBar;
    @BindView(R.id.editText)
    EditText editText;
    @BindView(R.id.upload)
    Button upload;

    private String filePath;//上传文件路径
    private String song_id;//歌曲id
    private String type;// 1视频 2音频
    private String song_type;// 0动态 1 比赛
    private String activity_id;//活动Id

    @Override
    protected void processLogic() {

    }

    @Override
    protected void setListener() {
        setTitle();
        upload.setOnClickListener(this);
        filePath = getIntent().getStringExtra("filePath");
        song_id = getIntent().getStringExtra("song_id");
        type = getIntent().getStringExtra("type");
        song_type = getIntent().getStringExtra("song_type");
        activity_id = getIntent().getStringExtra("activity_id");
    }

    @Override
    protected void loadViewLayout() {
        setContentView(R.layout.activity_upload);
    }

    @Override
    protected Context getActivityContext() {
        return this;
    }

    private void setTitle() {
        titleBar.initViewsVisible(true, true, false, false);
        titleBar.setAppTitle("发布动态");
        titleBar.setTitleSize(20);
        titleBar.setOnLeftButtonClickListener(new MyAppTitle.OnLeftButtonClickListener() {
            @Override
            public void onLeftButtonClick(View v) {
                finish();
            }
        });
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.upload:
                //上传
                String token = SPUtils.getInstance("userInfo").getString("token");
                if (FileUtils.isFileExists(filePath)) {
                    File file = FileUtils.getFileByPath(filePath);//上传文件
                    String content = editText.getText().toString();//上传文本内容
                    DreamApi.uploadAudio(UploadActivity.this, 0x001, token, file, song_id, content, type, song_type
                            , activity_id, "", new UploadCallBack() {
                                @Override
                                public void onSuccess(int what, Response<String> result) {
                                    try {
                                        JSONObject jsonObject = new JSONObject(result.body());
                                        int code = jsonObject.optInt("code");
                                        if (code == 200) {
                                            ToastUtils.showShort("发布成功");
                                            finish();
                                        }
                                    } catch (JSONException e) {
                                        e.printStackTrace();
                                    }
                                }

                                @Override
                                public void onFail(int what, Response<String> result) {
                                    ToastUtils.showShort("上传失败");
                                }

                                @Override
                                public void uploadProgress(Progress progress) {

                                }
                            });
                } else {
                    ToastUtils.showShort("文件不存在");
                }
                break;
        }
    }

}
