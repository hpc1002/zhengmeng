package com.zhuye.zhengmeng.user.viewHolder;

import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.bitmap.CenterCrop;
import com.jude.easyrecyclerview.adapter.BaseViewHolder;
import com.zhuye.zhengmeng.Constant;
import com.zhuye.zhengmeng.R;
import com.zhuye.zhengmeng.user.bean.GoldListBean;

/**
 * Created by hpc on 2017/11/1.
 */

public class GoldListViewHolder extends BaseViewHolder<GoldListBean.Data.GoldList> {

    private TextView gold_number;
    private ImageView iv_gold_item;

    public GoldListViewHolder(ViewGroup itemView) {
        super(itemView, R.layout.item_gold);
        gold_number = $(R.id.gold_number);
        iv_gold_item = $(R.id.iv_gold_item);


    }

    @Override
    public void setData(GoldListBean.Data.GoldList data) {
        super.setData(data);
        gold_number.setText(data.gold_price + "元");

        Glide.with(getContext())
                .load(Constant.BASE_URL2 + data.gold_img)
                .placeholder(R.mipmap.default_img)
                .bitmapTransform(new CenterCrop(getContext()))
                .into(iv_gold_item);
    }
}
