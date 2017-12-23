package com.zhuye.zhengmeng.shop.adapter;

import android.support.annotation.Nullable;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.zhuye.zhengmeng.Constant;
import com.zhuye.zhengmeng.R;
import com.zhuye.zhengmeng.shop.bean.GiftShopBean;

import java.util.List;

/**
 * Created by hpc on 2017/12/3.
 */

public class GiftShopAdapter extends BaseQuickAdapter<GiftShopBean.Data, BaseViewHolder> {
    public GiftShopAdapter(int layoutResId, @Nullable List<GiftShopBean.Data> data) {
        super(layoutResId, data);
    }

    @Override
    protected void convert(BaseViewHolder helper, GiftShopBean.Data item) {
        helper.setText(R.id.tv_goods_name, item.gift_name);
        helper.setText(R.id.tv_gold_count, item.gift_price);

        Glide.with(mContext)
                .load(Constant.BASE_URL2 + item.gift_img)
                .placeholder(R.mipmap.zhanwei1)
                .into((ImageView) helper.getView(R.id.iv_gift));
    }
}
