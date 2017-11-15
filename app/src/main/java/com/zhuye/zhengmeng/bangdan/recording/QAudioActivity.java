package com.zhuye.zhengmeng.bangdan.recording;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.blankj.utilcode.util.ToastUtils;
import com.czt.mp3recorder.MP3Recorder;
import com.flyco.dialog.listener.OnBtnClickL;
import com.flyco.dialog.widget.NormalDialog;
import com.shuyu.waveview.AudioPlayer;
import com.shuyu.waveview.FileUtils;
import com.zhuye.zhengmeng.R;
import com.zhuye.zhengmeng.bangdan.UploadActivity;
import com.zhuye.zhengmeng.base.BaseNoActivity;
import com.zhuye.zhengmeng.utils.DateUtil;

import java.io.File;
import java.io.IOException;

import butterknife.BindView;
import butterknife.OnClick;

public class QAudioActivity extends BaseNoActivity {


    @BindView(R.id.back)
    ImageView back;
    @BindView(R.id.start_record)
    TextView startRecord;
    @BindView(R.id.bottom_rl)
    RelativeLayout bottomRl;

    String filePath;//录音路径
    MP3Recorder mRecorder;
    AudioPlayer audioPlayer;
    boolean mIsRecord = false;//是否正在录音
    boolean mIsPlay = false;

    private String type;// 1视频 2音频
    private String song_type;// 0动态 1 比赛
    private String activity_id;//活动Id


    @Override
    protected void loadViewLayout() {
        setContentView(R.layout.activity_qaudio);
    }

    @Override
    protected Context getActivityContext() {
        return this;
    }

    @Override
    protected void processLogic() {

    }

    @Override
    protected void setListener() {

        audioPlayer = new AudioPlayer(this, new Handler() {
            @Override
            public void handleMessage(Message msg) {
                super.handleMessage(msg);
                switch (msg.what) {
                    case AudioPlayer.HANDLER_COMPLETE://播放结束
                        mIsPlay = false;
                        break;
                    case AudioPlayer.HANDLER_PREPARED://播放开始
                        break;
                    case AudioPlayer.HANDLER_ERROR://播放错误
                        resolveResetPlay();
                        break;
                }

            }
        });
        type = getIntent().getStringExtra("type");//1视频 2音频
        song_type = getIntent().getStringExtra("song_type");// 0动态 1 比赛
        activity_id = getIntent().getStringExtra("activity_id");//活动Id
    }


    @OnClick({R.id.back, R.id.start_record})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.back:
                NormalDialogCustomAttr();
                break;
            case R.id.start_record://开始录制
                if (mIsRecord) {
                    DialogCustomAttr();
                } else {
                    resolveRecord();
                }
                break;
        }
    }


    private void DialogCustomAttr() {
        final NormalDialog dialog = new NormalDialog(mContext);
        dialog.isTitleShow(false)
                .bgColor(Color.parseColor("#ffffff"))
                .cornerRadius(5)
                .content("确定要完成录制吗?")
                .contentGravity(Gravity.CENTER)
                .contentTextColor(Color.parseColor("#99000000"))
                .dividerColor(Color.parseColor("#55000000"))
                .btnTextSize(15.5f, 15.5f)//
                .btnTextColor(Color.parseColor("#99000000"), Color.parseColor("#CCEA4F05"))
                .widthScale(0.85f)
                .show();
        dialog.setOnBtnClickL(
                new OnBtnClickL() {
                    @Override
                    public void onBtnClick() {
                        dialog.dismiss();
                    }
                },
                new OnBtnClickL() {
                    @Override
                    public void onBtnClick() {
                        dialog.dismiss();
                        //结束录音
                        resolveStopRecord();
                        //调到上传界面
                        if (com.blankj.utilcode.util.FileUtils.isFileExists(filePath)) {
                            Intent intent = new Intent(QAudioActivity.this, UploadActivity.class);
                            intent.putExtra("filePath", filePath);
                            intent.putExtra("song_id", "");
                            intent.putExtra("type", "2");
                            intent.putExtra("song_type", "0");
                            intent.putExtra("activity_id", "");
                            startActivity(intent);
                            finish();
                        }
                    }
                });
    }

    private void NormalDialogCustomAttr() {
        final NormalDialog dialog = new NormalDialog(mContext);
        dialog.isTitleShow(false)
                .bgColor(Color.parseColor("#ffffff"))
                .cornerRadius(5)
                .content("您确定要放弃当前录音吗?")
                .contentGravity(Gravity.CENTER)
                .contentTextColor(Color.parseColor("#99000000"))
                .dividerColor(Color.parseColor("#55000000"))
                .btnTextSize(15.5f, 15.5f)//
                .btnTextColor(Color.parseColor("#99000000"), Color.parseColor("#CCEA4F05"))
                .widthScale(0.85f)
                .show();

        dialog.setOnBtnClickL(
                new OnBtnClickL() {
                    @Override
                    public void onBtnClick() {
                        dialog.dismiss();
                    }
                },
                new OnBtnClickL() {
                    @Override
                    public void onBtnClick() {
                        dialog.dismiss();
                        //结束录音
                        boolean is_exist = com.blankj.utilcode.util.FileUtils.isFileExists(filePath);
                        if (is_exist) {
                            FileUtils.deleteFile(filePath);
                        }
                        resolveStopRecord();
                        finish();
                    }
                });
    }

    /**
     * 开始录音
     */
    private void resolveRecord() {
        filePath = FileUtils.getAppPath();
        File file = new File(filePath);
        if (!file.exists()) {
            if (!file.mkdirs()) {
                Toast.makeText(this, "创建文件失败", Toast.LENGTH_SHORT).show();
                return;
            }
        }
        filePath = FileUtils.getAppPath() + "" + DateUtil.nowTime() + ".mp3";
        mRecorder = new MP3Recorder(new File(filePath));
        mRecorder.setErrorHandler(new Handler() {
            @Override
            public void handleMessage(Message msg) {
                super.handleMessage(msg);
                if (msg.what == MP3Recorder.ERROR_TYPE) {
                    Toast.makeText(QAudioActivity.this, "没有麦克风权限", Toast.LENGTH_SHORT).show();
                    resolveError();
                }
            }
        });
        try {
            mRecorder.start();
            startRecord.setText("完成录制");
            startRecord.setBackgroundColor(getResources().getColor(R.color.course_name));
            bottomRl.setBackgroundColor(getResources().getColor(R.color.course_name));
        } catch (IOException e) {
            e.printStackTrace();
            ToastUtils.showShort("录音出现异常");
            resolveError();
            return;
        }
        mIsRecord = true;
    }

    /**
     * 暂停
     */
    private void resolvePause() {
        if (!mIsRecord)
            return;
        if (mRecorder.isPause()) {
            mRecorder.setPause(false);
        } else {
            mRecorder.setPause(true);
        }
    }

    /**
     * 播放
     */
    private void resolvePlayRecord() {
        if (TextUtils.isEmpty(filePath) || !new File(filePath).exists()) {
            Toast.makeText(this, "文件不存在", Toast.LENGTH_SHORT).show();
            return;
        }
        mIsPlay = true;
        audioPlayer.playUrl(filePath);
    }

    /**
     * 重置
     */
    private void resolveResetPlay() {
        filePath = "";
        if (mIsPlay) {
            mIsPlay = false;
            audioPlayer.pause();
        }
    }

    /**
     * 录音异常
     */
    private void resolveError() {
        FileUtils.deleteFile(filePath);
        filePath = "";
        if (mRecorder != null && mRecorder.isRecording()) {
            mRecorder.stop();
        }
    }

    /**
     * 停止录音
     */
    private void resolveStopRecord() {
        if (mRecorder != null && mRecorder.isRecording()) {
            mRecorder.setPause(false);
            mRecorder.stop();
        }
        mIsRecord = false;
    }

    @Override
    public void onPause() {
        super.onPause();
        if (mIsRecord) {
            resolveStopRecord();
        }
        if (mIsPlay) {
            audioPlayer.pause();
            audioPlayer.stop();
        }
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if ((keyCode == KeyEvent.KEYCODE_BACK)) {
            NormalDialogCustomAttr();
            return false;
        } else {
            return super.onKeyDown(keyCode, event);
        }
    }

}
