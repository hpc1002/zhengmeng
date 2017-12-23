package com.zhuye.zhengmeng.bangdan.adapter;

import android.support.annotation.Nullable;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.zhuye.zhengmeng.Constant;
import com.zhuye.zhengmeng.R;
import com.zhuye.zhengmeng.bangdan.bean.CompetitionDetailBean;

import java.util.List;

/**
 * Created by hpc on 2017/12/18.
 */

public class CompelistAdapter extends BaseQuickAdapter<CompetitionDetailBean.Data.SongList, BaseViewHolder> {
    public CompelistAdapter(int layoutResId, @Nullable List<CompetitionDetailBean.Data.SongList> data) {
        super(layoutResId, data);
    }

    @Override
    protected void convert(BaseViewHolder helper, CompetitionDetailBean.Data.SongList item) {
        helper.setText(R.id.name_song, item.song_name);
        helper.setText(R.id.song_size, item.song_name);
        helper.setText(R.id.name_song, item.song_name);
        helper.addOnClickListener(R.id.btn_yanchang);
        Glide.with(mContext)
                .load(Constant.BASE_URL2 + item.song_img)
                .centerCrop()
                .placeholder(R.mipmap.default_img)
                .into((ImageView) helper.getView(R.id.img_song));
    }
}
