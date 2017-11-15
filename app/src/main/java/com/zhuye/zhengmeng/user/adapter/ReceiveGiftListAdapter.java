package com.zhuye.zhengmeng.user.adapter;

import android.content.Context;
import android.support.annotation.Nullable;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.zhuye.zhengmeng.Constant;
import com.zhuye.zhengmeng.R;
import com.zhuye.zhengmeng.user.bean.GiftListBean;

import java.util.List;

/**
 * Created by hpc on 2017/11/8.
 */

public class ReceiveGiftListAdapter extends BaseQuickAdapter<GiftListBean.Data, BaseViewHolder> {
    private Context context;

    public ReceiveGiftListAdapter(int layoutResId, @Nullable List<GiftListBean.Data> data, Context context) {
        super(layoutResId, data);
        this.context = context;
    }

    @Override
    protected void convert(BaseViewHolder helper, GiftListBean.Data item) {
        helper.setText(R.id.tv_nickname,item.user_nicename);
        helper.setText(R.id.tv_gift_name,item.gift_name);
        helper.setText(R.id.tv_gift_count,item.reception_sum);
        Glide.with(context)
                .load(Constant.BASE_URL2 + item.gift_img)
                .centerCrop()
                .placeholder(R.mipmap.default_img)
                .into((ImageView) helper.getView(R.id.iv_gift));
    }
}
