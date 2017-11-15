package com.zhuye.zhengmeng.bangdan.adapter;

import android.content.Context;
import android.support.annotation.Nullable;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.zhuye.zhengmeng.Constant;
import com.zhuye.zhengmeng.R;
import com.zhuye.zhengmeng.bangdan.bean.BangDanListBean;

import java.util.List;

/**
 * Created by hpc on 2017/11/8.
 */

public class BangDanRankAdapter extends BaseQuickAdapter<BangDanListBean.Data, BaseViewHolder> {
    private Context context;

    public BangDanRankAdapter(int layoutResId, @Nullable List<BangDanListBean.Data> data, Context context) {
        super(layoutResId, data);
        this.context = context;
    }

    @Override
    protected void convert(BaseViewHolder helper, BangDanListBean.Data item) {
        helper.setText(R.id.tv_rank, item.num);
        helper.setText(R.id.tv_nickname, item.user_nicename);
        helper.setText(R.id.tv_song_name, item.song_name);
        Glide.with(mContext)
                .load(Constant.BASE_URL2 + item.avatar)
                .centerCrop()
                .placeholder(R.mipmap.default_img)
                .into((ImageView) helper.getView(R.id.iv_user));
    }
}
