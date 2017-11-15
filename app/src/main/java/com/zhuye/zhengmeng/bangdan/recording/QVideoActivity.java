package com.zhuye.zhengmeng.bangdan.recording;

/**
 * Created by zzzy on 2017/11/8.
 */

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.graphics.PixelFormat;
import android.hardware.Camera;
import android.media.MediaPlayer;
import android.media.MediaRecorder;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.util.SparseIntArray;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.Surface;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.flyco.dialog.listener.OnBtnClickL;
import com.flyco.dialog.widget.NormalDialog;
import com.shuyu.waveview.FileUtils;
import com.zhuye.zhengmeng.Constant;
import com.zhuye.zhengmeng.R;
import com.zhuye.zhengmeng.bangdan.UploadActivity;
import com.zhuye.zhengmeng.utils.DateUtil;

import java.io.IOException;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;


public class QVideoActivity extends Activity implements SurfaceHolder.Callback {
    @BindView(R.id.back)
    ImageView back;
    @BindView(R.id.rl)
    RelativeLayout rl;
    @BindView(R.id.surfaceview)
    SurfaceView surfaceview;
    @BindView(R.id.start_recordVideo)
    TextView startRecordVideo;
    @BindView(R.id.bottom_rl)
    RelativeLayout bottomRl;
    private SurfaceHolder mSurfaceHolder;
    private boolean isRecording = false;//标记是否已经在录制
    private MediaRecorder mRecorder;//音视频录制类
    private Camera mCamera = null;//相机
    private Camera.Size mSize = null;//相机的尺寸
    private int mCameraFacing = Camera.CameraInfo.CAMERA_FACING_FRONT;//默认后置摄像头
    private static final SparseIntArray orientations = new SparseIntArray();//手机旋转对应的调整角度
    static final int REQUEST_CODE_ASK_CALL_PHONE = 122;

    String filePath;//视频路径
    private String song_path;//歌曲路径
    private String song_name;//歌曲名称
    private String lyric_path;//歌曲歌词
    private String song_id;//歌曲id
    private String type;// 1视频 2音频
    private String song_type;// 0动态 1 比赛
    private String activity_id;//活动Id
    private MediaPlayer mediaPlayer = new MediaPlayer();
    private Handler handler = new Handler();

    boolean mIsRecord = false;//是否正在录音
    private boolean repared = false;//判断mediaPlayer是否初始化完成

    static {
        orientations.append(Surface.ROTATION_0, 90);
        orientations.append(Surface.ROTATION_90, 0);
        orientations.append(Surface.ROTATION_180, 270);
        orientations.append(Surface.ROTATION_270, 180);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setWindow();
        setContentView(R.layout.activity_qvideo);
        ButterKnife.bind(this);
        Accessibility();
    }

    private void setWindow() {
        requestWindowFeature(Window.FEATURE_NO_TITLE);// 去掉标题栏
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);// 设置全屏
        // 设置竖屏显示
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        // 选择支持半透明模式,在有surfaceview的activity中使用。
        getWindow().setFormat(PixelFormat.TRANSLUCENT);
    }

    private void initViews() {

        type = getIntent().getStringExtra("type");//1视频 2音频
        song_type = getIntent().getStringExtra("song_type");// 0动态 1 比赛
        lyric_path = getIntent().getStringExtra("lyric_path");//歌曲名称
        activity_id = getIntent().getStringExtra("activity_id");//活动Id
        song_path = getIntent().getStringExtra("song_path");//歌曲路径
        song_name = getIntent().getStringExtra("song_name");//歌曲名称
        song_id = getIntent().getStringExtra("song_id");//歌曲Id

        try {
            mediaPlayer.reset();
            mediaPlayer.setDataSource(Constant.BASE_URL_PINJIE + song_path);
            mediaPlayer.prepareAsync();
            mediaPlayer.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
                @Override
                public void onPrepared(MediaPlayer mp) {
                    repared = true;
                }
            });
            mediaPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
                @Override
                public void onCompletion(MediaPlayer mp) {
                    //音乐播放完毕的时候，结束录音，上传
                    if (repared){
                        stopRecord();//停止录音
                        completeRecord();
                    }
                }
            });
        } catch (IOException e) {
            e.printStackTrace();
        }

        startRecordVideo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isRecording) {
                    //正在录制
                    NoCompleteDialog();
                } else {
                    //没有录制，调开始录制
                    startRecord();
                    mediaPlayer.start();
                    handler.post(runnable);
                }
            }
        });
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                NormalDialogCustomAttr();
            }
        });
        SurfaceHolder holder = surfaceview.getHolder();// 取得holder
        holder.setFormat(PixelFormat.TRANSPARENT);
        holder.setKeepScreenOn(true);
        holder.addCallback(this); // holder加入回调接口
    }

    private Runnable runnable = new Runnable() {
        @Override
        public void run() {
            if (mediaPlayer.isPlaying()) {
                long time = mediaPlayer.getCurrentPosition();
            }

            handler.postDelayed(this, 300);
        }
    };

    private void completeRecord() {
        final NormalDialog dialog = new NormalDialog(this);
        dialog.isTitleShow(false)
                .bgColor(Color.parseColor("#ffffff"))
                .cornerRadius(5)
                .content("视频已录制完成，是否需要上传")
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
                            Intent intent = new Intent(QVideoActivity.this, UploadActivity.class);
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

    private void NoCompleteDialog() {
        final NormalDialog dialog = new NormalDialog(this);
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
                        stopRecord();
                        handler.removeCallbacks(runnable);
                        //调到上传界面
                        if (com.blankj.utilcode.util.FileUtils.isFileExists(filePath)) {
                            Intent intent = new Intent(QVideoActivity.this, UploadActivity.class);
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

    /**
     * 初始化相机
     */
    private void initCamera() {
        if (Camera.getNumberOfCameras() == 2) {
            mCamera = Camera.open(mCameraFacing);
        } else {
            mCamera = Camera.open();
        }

        CameraSizeComparator sizeComparator = new CameraSizeComparator();
        Camera.Parameters parameters = mCamera.getParameters();

        if (mSize == null) {
            List<Camera.Size> vSizeList = parameters.getSupportedPreviewSizes();
            Collections.sort(vSizeList, sizeComparator);

            for (int num = 0; num < vSizeList.size(); num++) {
                Camera.Size size = vSizeList.get(num);

                if (size.width >= 800 && size.height >= 480) {
                    this.mSize = size;
                    break;
                }
            }
            mSize = vSizeList.get(0);

            List<String> focusModesList = parameters.getSupportedFocusModes();

            //增加对聚焦模式的判断
            if (focusModesList.contains(Camera.Parameters.FOCUS_MODE_CONTINUOUS_VIDEO)) {
                parameters.setFocusMode(Camera.Parameters.FOCUS_MODE_CONTINUOUS_VIDEO);
            } else if (focusModesList.contains(Camera.Parameters.FOCUS_MODE_AUTO)) {
                parameters.setFocusMode(Camera.Parameters.FOCUS_MODE_AUTO);
            }
            mCamera.setParameters(parameters);
        }
        int rotation = getWindowManager().getDefaultDisplay().getRotation();
        int orientation = orientations.get(rotation);
        mCamera.setDisplayOrientation(orientation);
    }

    @Override
    protected void onResume() {
        super.onResume();
        initCamera();
    }

    @Override
    public void onPause() {
        releaseCamera();
        super.onPause();
    }

    /**
     * 开始录制
     */
    private void startRecord() {

        if (mRecorder == null) {
            mRecorder = new MediaRecorder(); // 创建MediaRecorder
        }
        if (mCamera != null) {
            mCamera.stopPreview();
            mCamera.unlock();
            mRecorder.setCamera(mCamera);
        }
        try {
            // 设置音频采集方式
            mRecorder.setAudioSource(MediaRecorder.AudioSource.CAMCORDER);
            //设置视频的采集方式
            mRecorder.setVideoSource(MediaRecorder.VideoSource.CAMERA);
            //设置文件的输出格式
            mRecorder.setOutputFormat(MediaRecorder.OutputFormat.MPEG_4);//aac_adif， aac_adts， output_format_rtp_avp， output_format_mpeg2ts ，webm
            //设置audio的编码格式
            mRecorder.setAudioEncoder(MediaRecorder.AudioEncoder.AMR_NB);
            //设置video的编码格式
            mRecorder.setVideoEncoder(MediaRecorder.VideoEncoder.H264);
            //设置录制的视频编码比特率
            mRecorder.setVideoEncodingBitRate(1024 * 1024);
            //设置录制的视频帧率,注意文档的说明:
            mRecorder.setVideoFrameRate(30);
            //设置要捕获的视频的宽度和高度
            mSurfaceHolder.setFixedSize(320, 240);//最高只能设置640x480
            mRecorder.setVideoSize(320, 240);//最高只能设置640x480
            //设置记录会话的最大持续时间（毫秒）
            mRecorder.setMaxDuration(60 * 1000);
            mRecorder.setPreviewDisplay(mSurfaceHolder.getSurface());
            String path = getExternalCacheDir().getPath();
            if (path != null) {
                filePath = FileUtils.getAppPath() + song_name + DateUtil.nowTime() + ".mp4";
//                File dir = new File(filePath);
//                if (!dir.exists()) {
//                    dir.mkdir();
//                }
//                path = dir + "/" + System.currentTimeMillis() + ".mp4";
                //设置输出文件的路径
                mRecorder.setOutputFile(filePath);
                //准备录制
                mRecorder.prepare();
                //开始录制
                mRecorder.start();
                isRecording = true;
                startRecordVideo.setText("完成录制");
                startRecordVideo.setBackgroundColor(getResources().getColor(R.color.course_name));
                bottomRl.setBackgroundColor(getResources().getColor(R.color.course_name));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 停止录制
     */
    private void stopRecord() {
        try {
            //停止录制
            mRecorder.stop();
            //重置
            mRecorder.reset();
//            startRecordVideo.setText("开始");
        } catch (Exception e) {
            e.printStackTrace();
        }
        isRecording = false;
    }

    /**
     * 释放MediaRecorder
     */
    private void releaseMediaRecorder() {
        if (mRecorder != null) {
            mRecorder.release();
            mRecorder = null;
        }
    }

    /**
     * 释放相机资源
     */
    private void releaseCamera() {
        try {
            if (mCamera != null) {
                mCamera.stopPreview();
                mCamera.setPreviewCallback(null);
                mCamera.unlock();
                mCamera.release();
            }
        } catch (RuntimeException e) {
        } finally {
            mCamera = null;
        }
    }

    @Override
    public void surfaceChanged(SurfaceHolder holder, int format, int width,
                               int height) {
        // 将holder，这个holder为开始在onCreate里面取得的holder，将它赋给mSurfaceHolder
        mSurfaceHolder = holder;
        if (mCamera == null) {
            return;
        }
        try {
            //设置显示
            mCamera.setPreviewDisplay(holder);
            mCamera.startPreview();
        } catch (Exception e) {
            e.printStackTrace();
            releaseCamera();
            finish();
        }
    }

    @Override
    public void surfaceCreated(SurfaceHolder holder) {
        // 将holder，这个holder为开始在onCreate里面取得的holder，将它赋给mSurfaceHolder
        mSurfaceHolder = holder;
    }

    @Override
    public void surfaceDestroyed(SurfaceHolder holder) {
        // surfaceDestroyed的时候同时对象设置为null
        if (isRecording && mCamera != null) {
            mCamera.lock();
        }
        surfaceview = null;
        mSurfaceHolder = null;
        releaseMediaRecorder();
        releaseCamera();
    }

    private class CameraSizeComparator implements Comparator<Camera.Size> {
        public int compare(Camera.Size lhs, Camera.Size rhs) {
            if (lhs.width == rhs.width) {
                return 0;
            } else if (lhs.width > rhs.width) {
                return 1;
            } else {
                return -1;
            }
        }
    }
    /**
     * 对于6.0以后的机器动态权限申请
     */
    /**
     * 对于6.0以后的机器动态权限申请
     */
    public void Accessibility() {
        if (Build.VERSION.SDK_INT >= 23) {
            int checkCallPhonePermission = ContextCompat.checkSelfPermission(QVideoActivity.this, Manifest.permission.CAMERA);
            int checkCallPhonePermission2 = ContextCompat.checkSelfPermission(QVideoActivity.this, Manifest.permission.WRITE_EXTERNAL_STORAGE);
            int checkCallPhonePermission3 = ContextCompat.checkSelfPermission(QVideoActivity.this, Manifest.permission.RECORD_AUDIO);
            if (checkCallPhonePermission != PackageManager.PERMISSION_GRANTED && checkCallPhonePermission2 != PackageManager.PERMISSION_GRANTED && checkCallPhonePermission3 != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(QVideoActivity.this, new String[]{Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.RECORD_AUDIO}, REQUEST_CODE_ASK_CALL_PHONE);
                return;
            } else {
                initViews();

            }
        } else {
            initViews();
        }
    }


    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        switch (requestCode) {
            case REQUEST_CODE_ASK_CALL_PHONE:
                if (grantResults[0] == PackageManager.PERMISSION_GRANTED && grantResults[1] == PackageManager.PERMISSION_GRANTED) {
                    // Permission Granted
                    initViews();

                } else {
                    // Permission Denied
                    Toast.makeText(QVideoActivity.this, "CALL_PHONE Denied", Toast.LENGTH_SHORT)
                            .show();
                }
                break;
            default:
                super.onRequestPermissionsResult(requestCode, permissions, grantResults);
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

    private void NormalDialogCustomAttr() {
        final NormalDialog dialog = new NormalDialog(this);
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
                        stopRecord();
                        finish();
                    }
                });
    }

}
