package com.zhuye.zhengmeng.bangdan.viewHolder;

import android.media.MediaPlayer;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.bitmap.CenterCrop;
import com.jude.easyrecyclerview.adapter.BaseViewHolder;
import com.zhuye.zhengmeng.Constant;
import com.zhuye.zhengmeng.R;
import com.zhuye.zhengmeng.bangdan.bean.SongsListBean;

import java.io.IOException;

/**
 * Created by hpc on 2017/11/1.
 */

public class SongsListViewHolder extends BaseViewHolder<SongsListBean.Data> {

    private TextView name_song, song_size, btn_yanchang;
    private ImageView img_song;

    public SongsListViewHolder(ViewGroup itemView) {
        super(itemView, R.layout.item_song);
        name_song = $(R.id.name_song);
        song_size = $(R.id.song_size);
        btn_yanchang = $(R.id.btn_yanchang);
        img_song = $(R.id.img_song);
    }

    /**
     * {
     * "data": [
     * {
     * "song_id": "5",
     * "song_name": "华晨宇 - 智商二五零",
     * "song_path": "/changba/Uploads/Song/2017-10-09/59daea5ac0343.mp3",
     * "lyric_path": "/changba/Uploads/Lyric/2017-10-09/59daea5ac1f37.lrc",
     * "song_click": "6",
     * "song_img": "/changba/Uploads/Images/2017-10-09/59db24aa83f4d.png",
     * "is_have": 0
     * },
     *
     * @param data
     */
    @Override
    public void setData(final SongsListBean.Data data) {
        super.setData(data);
        name_song.setText(data.song_name);
        song_size.setText(data.song_name);
//        btn_yanchang.setText(data.);
        int time = getLongTime(Constant.BASE_URL2 + data.song_img);
        btn_yanchang.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mCallBack.OnItemClickListener(data.song_id, data.song_name, data.song_path, data.lyric_path);
            }
        });
        Glide.with(getContext())
                .load(Constant.BASE_URL2 + data.song_img)
                .placeholder(R.mipmap.default_img)
                .bitmapTransform(new CenterCrop(getContext()))
                .into(img_song);
    }

    private int getLongTime(String url){
        int longTime = 0;
        try {
            MediaPlayer mediaPlayer = new MediaPlayer();
            mediaPlayer.setDataSource(url);
            mediaPlayer.prepare();
            longTime = mediaPlayer.getDuration();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return longTime;
    }

    private allCheck mCallBack;

    public void setCallBack(allCheck callBack) {
        mCallBack = callBack;
    }

    public interface allCheck {
        void OnItemClickListener(String id, String song_name, String song_path, String lyric_path);
    }
}
