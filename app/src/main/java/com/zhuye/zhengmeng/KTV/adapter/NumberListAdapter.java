package com.zhuye.zhengmeng.KTV.adapter;

import android.support.annotation.Nullable;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.zhuye.zhengmeng.Constant;
import com.zhuye.zhengmeng.KTV.bean.OnRoomBean;
import com.zhuye.zhengmeng.R;
import com.zhuye.zhengmeng.widget.RoundedCornerImageView;

import java.util.List;

/**
 * Created by hpc on 2017/12/13.
 */

public class NumberListAdapter extends BaseQuickAdapter<OnRoomBean.Data, BaseViewHolder> {
    public NumberListAdapter(int layoutResId, @Nullable List<OnRoomBean.Data> data) {
        super(layoutResId, data);
    }

    @Override
    protected void convert(BaseViewHolder helper, OnRoomBean.Data item) {
        Glide.with(mContext)
                .load(Constant.BASE_URL2 + item.avatar)
                .placeholder(R.mipmap.default_img)
                .into((RoundedCornerImageView) helper.getView(R.id.item_img));
    }
}
