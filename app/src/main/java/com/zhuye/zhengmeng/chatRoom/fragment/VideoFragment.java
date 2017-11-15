package com.zhuye.zhengmeng.chatRoom.fragment;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

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

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_video, container, false);
        initVideo();
        unbinder = ButterKnife.bind(this, view);
        return view;
    }

    private void initVideo() {

        mLivePusher = new TXLivePusher(getActivity());
        mLivePushConfig = new TXLivePushConfig();
        mLivePusher.setConfig(mLivePushConfig);
        String rtmpUrl = "rtmp://vlive3.rtmp.cdn.ucloud.com.cn/ucloud/8971";
        mLivePusher.startPusher(rtmpUrl);

        mLivePusher.startCameraPreview(videoView);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }

    @Override
    public void onStop() {
        super.onStop();
        videoView.onPause();  // mCaptureView 是摄像头的图像渲染view
        mLivePusher.pausePusher(); // 通知 SDK 进入“后台推流模式”了
    }

}
