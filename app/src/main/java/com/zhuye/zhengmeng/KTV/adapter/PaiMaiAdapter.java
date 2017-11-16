package com.zhuye.zhengmeng.KTV.adapter;


import android.support.annotation.Nullable;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.zhuye.zhengmeng.Constant;
import com.zhuye.zhengmeng.KTV.bean.PaiMaiBean;
import com.zhuye.zhengmeng.R;

import java.util.List;

/**
 * Created by hpc on 2017/11/15.
 */

public class PaiMaiAdapter extends BaseQuickAdapter<PaiMaiBean.Data, BaseViewHolder> {

    public PaiMaiAdapter(int layoutResId, @Nullable List<PaiMaiBean.Data> data) {
        super(layoutResId, data);
    }

    @Override
    protected void convert(BaseViewHolder helper, PaiMaiBean.Data item) {
        helper.setText(R.id.name_song, item.song_name);

    }
}
