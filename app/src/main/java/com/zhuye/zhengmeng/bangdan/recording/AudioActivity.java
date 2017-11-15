package com.zhuye.zhengmeng.bangdan.recording;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.media.MediaPlayer;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import com.blankj.utilcode.util.ToastUtils;
import com.czt.mp3recorder.MP3Recorder;
import com.flyco.dialog.listener.OnBtnClickL;
import com.flyco.dialog.widget.NormalDialog;
import com.lzy.okgo.OkGo;
import com.lzy.okgo.callback.FileCallback;
import com.lzy.okgo.model.Response;
import com.shuyu.waveview.AudioPlayer;
import com.shuyu.waveview.FileUtils;
import com.zhuye.zhengmeng.Constant;
import com.zhuye.zhengmeng.R;
import com.zhuye.zhengmeng.bangdan.UploadActivity;
import com.zhuye.zhengmeng.base.BaseNoActivity;
import com.zhuye.zhengmeng.utils.DateUtil;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;

import butterknife.BindView;
import butterknife.OnClick;
import me.wcy.lrcview.LrcView;

public class AudioActivity extends BaseNoActivity {


    @BindView(R.id.lrc_view)
    LrcView lrcView;
    @BindView(R.id.progress_bar)
    SeekBar seekBar;
    @BindView(R.id.btn_play_pause)
    Button btnPlayPause;
    @BindView(R.id.song_name)
    TextView songName;
    @BindView(R.id.record)
    Button record;
    @BindView(R.id.recordPause)
    Button recordPause;
    @BindView(R.id.stop)
    Button stop;
    @BindView(R.id.play)
    Button play;
    @BindView(R.id.reset)
    Button reset;
    @BindView(R.id.btn_layout)
    LinearLayout btnLayout;
    @BindView(R.id.back)
    ImageView back;
    @BindView(R.id.start_record)
    TextView startRecord;
    @BindView(R.id.reset_Iv)
    ImageView resetIv;
    @BindView(R.id.bottom_rl)
    RelativeLayout bottomRl;

    private MediaPlayer mediaPlayer = new MediaPlayer();
    private Handler handler = new Handler();

    String filePath;//录音路径
    MP3Recorder mRecorder;
    AudioPlayer audioPlayer;
    boolean mIsRecord = false;//是否正在录音
    boolean mIsPlay = false;

    int duration;
    int curPosition;

    private String song_path;//歌曲路径
    private String song_name;//歌曲名称
    private String lyric_path;//歌曲歌词
    private String song_id;//歌曲id
    private String type;// 1视频 2音频
    private String song_type;// 0动态 1 比赛
    private String activity_id;//活动Id

    private boolean repared = false;//判断mediaPlayer是否初始化完成

    @Override
    protected void loadViewLayout() {
        setContentView(R.layout.activity_audio);
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
                    case AudioPlayer.HANDLER_CUR_TIME://更新的时间
                        curPosition = (int) msg.obj;
                        break;
                    case AudioPlayer.HANDLER_COMPLETE://播放结束
                        mIsPlay = false;
                        break;
                    case AudioPlayer.HANDLER_PREPARED://播放开始
                        duration = (int) msg.obj;
                        break;
                    case AudioPlayer.HANDLER_ERROR://播放错误
                        resolveResetPlay();
                        break;
                }

            }
        });
        type = getIntent().getStringExtra("type");//1视频 2音频
        song_type = getIntent().getStringExtra("song_type");// 0动态 1 比赛
        song_id = getIntent().getStringExtra("song_id");//歌曲Id
        song_path = getIntent().getStringExtra("song_path");//歌曲路径
        song_name = getIntent().getStringExtra("song_name");//歌曲名称
        lyric_path = getIntent().getStringExtra("lyric_path");//歌曲名称
        activity_id = getIntent().getStringExtra("activity_id");//活动Id
        songName.setText(song_name);
        try {
            mediaPlayer.reset();
            mediaPlayer.setDataSource(Constant.BASE_URL_PINJIE + song_path);
            mediaPlayer.prepareAsync();
            mediaPlayer.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
                @Override
                public void onPrepared(MediaPlayer mp) {
                    seekBar.setMax(mediaPlayer.getDuration());
                    seekBar.setProgress(0);
                    repared = true;
                }
            });
            mediaPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
                @Override
                public void onCompletion(MediaPlayer mp) {
                    lrcView.updateTime(0);
                    seekBar.setProgress(0);
                    //音乐播放完毕的时候，结束录音，上传
                    if (repared) {
                        resolveStopRecord();
                        completeRecord();
                    }
                }
            });
        } catch (IOException e) {
            e.printStackTrace();
        }

        //加载歌词
        OkGo.<File>get(Constant.BASE_URL_PINJIE + lyric_path)
                .tag(this).execute(new FileCallback() {
            @Override
            public void onSuccess(Response<File> response) {
                String lrc = "";
                File mFile = response.body();
                FileInputStream is = null;
                try {
                    is = new FileInputStream(mFile);
                    int size = is.available();
                    byte[] buffer = new byte[size];
                    is.read(buffer);
                    lrc = new String(buffer);
                    lrcView.loadLrc(lrc);
                    is.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        });

        lrcView.setOnPlayClickListener(new LrcView.OnPlayClickListener() {
            @Override
            public boolean onPlayClick(long time) {
                mediaPlayer.seekTo((int) time);
                if (!mediaPlayer.isPlaying()) {
                    mediaPlayer.start();
                    handler.post(runnable);
                }
                return true;
            }
        });

        btnPlayPause.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!mediaPlayer.isPlaying()) {
                    mediaPlayer.start();
                    handler.post(runnable);
                } else {
                    mediaPlayer.pause();
                    handler.removeCallbacks(runnable);
                }
            }
        });

        seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                mediaPlayer.seekTo(seekBar.getProgress());
                lrcView.updateTime(seekBar.getProgress());
            }
        });
    }

    private Runnable runnable = new Runnable() {
        @Override
        public void run() {
            if (mediaPlayer.isPlaying()) {
                long time = mediaPlayer.getCurrentPosition();
                lrcView.updateTime(time);
                seekBar.setProgress((int) time);
            }

            handler.postDelayed(this, 300);
        }
    };

    @OnClick({R.id.back, R.id.record, R.id.stop, R.id.play, R.id.reset, R.id.recordPause, R.id.start_record
            , R.id.reset_Iv})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.back:
                NormalDialogCustomAttr();
                break;
            case R.id.record:
                resolveRecord();
                break;
            case R.id.stop:
                resolveStopRecord();
                break;
            case R.id.play:
                resolvePlayRecord();
                break;
            case R.id.reset:
                resolveResetPlay();
            case R.id.recordPause:
                resolvePause();
                break;
            case R.id.start_record://开始录制
                if (mIsRecord) {
                    DialogCustomAttr();
                } else {
                    resolveRecord();
                }
                break;
            case R.id.reset_Iv://重新录制
                ResetCustomAttr();
                break;
        }
    }

    private void completeRecord() {
        final NormalDialog dialog = new NormalDialog(mContext);
        dialog.isTitleShow(false)
                .bgColor(Color.parseColor("#ffffff"))
                .cornerRadius(5)
                .content("歌曲已录制完成，是否需要上传")
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
                        finish();
                    }
                },
                new OnBtnClickL() {
                    @Override
                    public void onBtnClick() {
                        dialog.dismiss();
                        //调到上传界面
                        if (com.blankj.utilcode.util.FileUtils.isFileExists(filePath)) {
                            Intent intent = new Intent(AudioActivity.this, UploadActivity.class);
                            intent.putExtra("filePath", filePath);
                            intent.putExtra("song_id", song_id);
                            intent.putExtra("type", type);
                            intent.putExtra("song_type", song_type);
                            intent.putExtra("activity_id", activity_id);
                            startActivity(intent);
                            finish();
                        }
                    }
                });
    }

    private void ResetCustomAttr() {
        final NormalDialog dialog = new NormalDialog(mContext);
        dialog.isTitleShow(false)
                .bgColor(Color.parseColor("#ffffff"))
                .cornerRadius(5)
                .content("当前歌曲还没有保存，确定要重新录制吗?")
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
                        //结束当前录音
                        if (com.blankj.utilcode.util.FileUtils.isFileExists(filePath)) {
                            FileUtils.deleteFile(filePath);
                        }
                        resolveResetPlay();

                    }
                });
    }

    private void DialogCustomAttr() {
        final NormalDialog dialog = new NormalDialog(mContext);
        dialog.isTitleShow(false)
                .bgColor(Color.parseColor("#ffffff"))
                .cornerRadius(5)
                .content("当前歌曲还没有结束，确定要完成录制吗?")
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
                            Intent intent = new Intent(AudioActivity.this, UploadActivity.class);
                            intent.putExtra("filePath", filePath);
                            intent.putExtra("song_id", song_id);
                            intent.putExtra("type", type);
                            intent.putExtra("song_type", song_type);
                            intent.putExtra("activity_id", activity_id);
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
        filePath = FileUtils.getAppPath() + song_name + DateUtil.nowTime() + ".mp3";
        mRecorder = new MP3Recorder(new File(filePath));
        mRecorder.setErrorHandler(new Handler() {
            @Override
            public void handleMessage(Message msg) {
                super.handleMessage(msg);
                if (msg.what == MP3Recorder.ERROR_TYPE) {
                    Toast.makeText(AudioActivity.this, "没有麦克风权限", Toast.LENGTH_SHORT).show();
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
        //**************播放背景音乐***************//
        if (!mediaPlayer.isPlaying()) {
            mediaPlayer.start();
            handler.post(runnable);
        } else {
            mediaPlayer.pause();
            handler.removeCallbacks(runnable);
        }
    }

    /**
     * 暂停
     */
    private void resolvePause() {
        if (!mIsRecord)
            return;
        if (mRecorder.isPause()) {
            mRecorder.setPause(false);
            recordPause.setText("暂停");
        } else {
            mRecorder.setPause(true);
            recordPause.setText("继续");
        }

        //**************播放背景音乐***************//
        if (!mediaPlayer.isPlaying()) {
            mediaPlayer.start();
            handler.post(runnable);
        } else {
            mediaPlayer.pause();
            handler.removeCallbacks(runnable);
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
        //**************播放背景音乐***************//
        mediaPlayer.pause();
        mediaPlayer.start();
//        if (!mediaPlayer.isPlaying()) {
//            mediaPlayer.start();
//            handler.post(runnable);
//        } else {
//            mediaPlayer.pause();
//            handler.removeCallbacks(runnable);
//        }
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
        recordPause.setText("暂停");
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
    protected void onDestroy() {
        super.onDestroy();
        handler.removeCallbacks(runnable);
        mediaPlayer.reset();
        mediaPlayer.release();
        mediaPlayer = null;
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
