package com.zhuye.zhengmeng.chatRoom.fragment;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.tencent.rtmp.TXLivePlayConfig;
import com.tencent.rtmp.TXLivePlayer;
import com.tencent.rtmp.TXLivePushConfig;
import com.tencent.rtmp.TXLivePusher;
import com.tencent.rtmp.ui.TXCloudVideoView;
import com.zhuye.zhengmeng.R;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

/**
 * Created by hpc on 2017/11/15.
 */

public class VideoFragment extends Fragment {

    @BindView(R.id.video_view)
    TXCloudVideoView videoView;
    Unbinder unbinder;
    private TXLivePushConfig mLivePushConfig;
    private TXLivePusher mLivePusher;
    private TXLivePlayer mLivePlayer;
    private TXLivePlayConfig mPlayConfig;
    private TXCloudVideoView pullMedia;
    private  View view;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        mPlayConfig = new TXLivePlayConfig();

        view = inflater.inflate(R.layout.fragment_video, container, false);
        initVideo();
        unbinder = ButterKnife.bind(this, view);
        return view;
    }

    private void initVideo() {

        pullMedia = view.findViewById(R.id.pull_media);


        //创建player对象
        mLivePlayer = new TXLivePlayer(getActivity());
        //关键player对象与界面view
        mLivePlayer.setPlayerView(pullMedia);
        mLivePlayer.setConfig(mPlayConfig);
        mLivePlayer.setAutoPlay(true);
        //拉流
        pullMedia();
    }

    private void pullMedia() {
        String flvUrl = "rtmp://15904.liveplay.myqcloud.com/live/15904_51faa45776";
        mLivePlayer.startPlay(flvUrl, TXLivePlayer.PLAY_TYPE_LIVE_RTMP); //推荐FLV
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
        stopPlayRtmp();
    }

    @Override
    public void onStop() {
        super.onStop();

        //停止拉流
        if (mLivePlayer != null) {
            mLivePlayer.stopPlay(true);
            mLivePlayer = null;
        }
        if (pullMedia != null) {
            pullMedia.onDestroy();
            pullMedia = null;
        }
        videoView.onPause();  // mCaptureView 是摄像头的图像渲染view
        mLivePusher.pausePusher(); // 通知 SDK 进入“后台推流模式”了
    }

    private void stopPlayRtmp() {
        if (mLivePlayer != null) {
            mLivePlayer.stopRecord();
            mLivePlayer.setPlayListener(null);
            mLivePlayer.stopPlay(true);
        }
        if (pullMedia != null) {
            pullMedia.onDestroy();
            pullMedia = null;
        }
    }}
