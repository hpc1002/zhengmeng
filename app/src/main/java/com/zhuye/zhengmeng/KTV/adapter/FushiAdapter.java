package com.zhuye.zhengmeng.KTV.adapter;

import android.support.annotation.Nullable;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.zhuye.zhengmeng.KTV.bean.Fushi;
import com.zhuye.zhengmeng.R;

import java.util.List;

/**
 * Created by hpc on 2017/11/16.
 */

public class FushiAdapter extends BaseQuickAdapter<Fushi, BaseViewHolder> {
    public FushiAdapter(int layoutResId, @Nullable List<Fushi> data) {
        super(layoutResId, data);
    }

    @Override
    protected void convert(BaseViewHolder helper, Fushi item) {
        Glide.with(mContext)
                .load(item.image)
                .centerCrop()
                .into((ImageView) helper.getView(R.id.fushi_image));
    }
}
