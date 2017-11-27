package com.zhuye.zhengmeng.user;

import android.content.Context;
import android.content.Intent;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.shuyu.waveview.FileUtils;
import com.zhuye.zhengmeng.R;
import com.zhuye.zhengmeng.base.BaseActivity;
import com.zhuye.zhengmeng.user.adapter.Mp3ListAdapter;
import com.zhuye.zhengmeng.user.bean.Mp3ListBean;
import com.zhuye.zhengmeng.utils.GetLocalMp3Utils;
import com.zhuye.zhengmeng.utils.ToastManager;
import com.zhuye.zhengmeng.view.MyAppTitle;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * 本地录音
 */
public class LocalRecordActivity extends BaseActivity {


    @BindView(R.id.titleBar)
    MyAppTitle titleBar;
    @BindView(R.id.local_record)
    RecyclerView localRecord;
    private static final String TAG = "LocalRecordActivity";

    @Override
    protected void processLogic() {

    }

    @Override
    protected void setListener() {
        setTitle();
        ArrayList<String> Mp3List = GetLocalMp3Utils.GetMp3FileName(FileUtils.getAppPath());
        Log.i(TAG, "setListener: " + Mp3List);
        localRecord.setLayoutManager(new LinearLayoutManager(this));
        final Mp3ListAdapter mp3ListAdapter = new Mp3ListAdapter(R.layout.item_mp3,Mp3List);
        localRecord.setAdapter(mp3ListAdapter);
        mp3ListAdapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                Log.i(TAG, "onItemClick: "+FileUtils.getAppPath()+mp3ListAdapter.getItem(position));
                String filepath =FileUtils.getAppPath()+mp3ListAdapter.getItem(position);
                File file = new File(filepath);
//                Intent it = new Intent(Intent.ACTION_VIEW);
//                it.setDataAndType(Uri.parse(filepath), "audio/mp3");
//                startActivity(it);
//                if(file.exists()){
//                    try {
//                      MediaPlayer  mediaPlayer = new MediaPlayer();
//                        mediaPlayer.setDataSource(filepath);//设置播放的数据源。
//                        mediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
//                        mediaPlayer.prepare();//准备开始播放 播放的逻辑是c代码在新的线程里面执行。
//                        mediaPlayer.start();
//
//                        mediaPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
//                            @Override
//                            public void onCompletion(MediaPlayer mp) {
//
//                            }
//                        });
//                    } catch (Exception e) {
//                        e.printStackTrace();
//                        ToastManager.show("播放失败");
//                    }
//                }else{
//                    ToastManager.show("文件不存在");
//                }
            }
        });
    }

    @Override
    protected void loadViewLayout() {
        setContentView(R.layout.activity_local_record);
    }

    private void setTitle() {
        titleBar.initViewsVisible(true, true, false, false);
        titleBar.setAppTitle("本地录音");
        titleBar.setTitleSize(20);
        titleBar.setOnLeftButtonClickListener(new MyAppTitle.OnLeftButtonClickListener() {
            @Override
            public void onLeftButtonClick(View v) {
                finish();
            }
        });
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
