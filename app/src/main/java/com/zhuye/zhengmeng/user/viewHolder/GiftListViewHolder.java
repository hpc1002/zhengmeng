package com.zhuye.zhengmeng.user.viewHolder;

import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.bitmap.CenterCrop;
import com.jude.easyrecyclerview.adapter.BaseViewHolder;
import com.zhuye.zhengmeng.Constant;
import com.zhuye.zhengmeng.R;
import com.zhuye.zhengmeng.user.bean.GiftListBean;

/**
 * Created by hpc on 2017/11/1.
 */

public class GiftListViewHolder extends BaseViewHolder<GiftListBean.Data> {

    private TextView tv_nickname, tv_gift_name, tv_gift_count;
    private ImageView iv_gift;

    public GiftListViewHolder(ViewGroup itemView) {
        super(itemView, R.layout.item_gift);
        tv_nickname = $(R.id.tv_nickname);
        tv_gift_name = $(R.id.tv_gift_name);
        tv_gift_count = $(R.id.tv_gift_count);
        iv_gift = $(R.id.iv_gift);


    }

    @Override
    public void setData(GiftListBean.Data data) {
        super.setData(data);
        tv_nickname.setText(data.user_nicename);
        tv_gift_name.setText(data.gift_name);
        tv_gift_count.setText(data.reception_sum);

        Glide.with(getContext())
                .load(Constant.BASE_URL2 + data.gift_img)
                .placeholder(R.mipmap.default_img)
                .bitmapTransform(new CenterCrop(getContext()))
                .into(iv_gift);
    }
}
