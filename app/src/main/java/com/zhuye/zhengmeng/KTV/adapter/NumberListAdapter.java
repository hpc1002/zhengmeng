package com.zhuye.zhengmeng.KTV.adapter;

import android.support.annotation.Nullable;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.zhuye.zhengmeng.Constant;
import com.zhuye.zhengmeng.DataProvider;
import com.zhuye.zhengmeng.KTV.bean.OnRoomBean;
import com.zhuye.zhengmeng.R;
import com.zhuye.zhengmeng.widget.RoundedCornerImageView;

import java.util.List;
import java.util.Random;

/**
 * Created by hpc on 2017/12/13.
 */

public class NumberListAdapter extends BaseQuickAdapter<OnRoomBean.Data, BaseViewHolder> {
    public NumberListAdapter(int layoutResId, @Nullable List<OnRoomBean.Data> data) {
        super(layoutResId, data);
    }

    @Override
    protected void convert(BaseViewHolder helper, OnRoomBean.Data item) {
        Random rand = new Random();
        int i = rand.nextInt(2);
        Glide.with(mContext)
                .load(DataProvider.getShafaImage().get(i))
                .placeholder(R.mipmap.default_img)
                .into((ImageView) helper.getView(R.id.item_img));
        if (item.avatar.contains("http://")){
            Glide.with(mContext)
                    .load(item.avatar)
                    .placeholder(R.mipmap.default_img)
                    .into((RoundedCornerImageView) helper.getView(R.id.avatar));
        }else{
            Glide.with(mContext)
                    .load(Constant.BASE_URL2+item.avatar)
                    .placeholder(R.mipmap.default_img)
                    .into((RoundedCornerImageView) helper.getView(R.id.avatar));
        }

    }
}
