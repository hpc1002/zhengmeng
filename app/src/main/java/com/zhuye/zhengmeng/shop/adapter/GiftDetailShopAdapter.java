package com.zhuye.zhengmeng.shop.adapter;

import android.support.annotation.Nullable;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.zhuye.zhengmeng.Constant;
import com.zhuye.zhengmeng.R;
import com.zhuye.zhengmeng.shop.bean.GiftDetailBean;
import com.zhuye.zhengmeng.shop.bean.GiftShopBean;

import java.util.List;

/**
 * Created by hpc on 2017/12/3.
 */

public class GiftDetailShopAdapter extends BaseQuickAdapter<GiftDetailBean.Data.ShopList, BaseViewHolder> {
    public GiftDetailShopAdapter(int layoutResId, @Nullable List<GiftDetailBean.Data.ShopList> data) {
        super(layoutResId, data);
    }

    @Override
    protected void convert(BaseViewHolder helper, GiftDetailBean.Data.ShopList item) {
        helper.setText(R.id.tv_goods_name, item.gift_name);
        helper.setText(R.id.tv_gold_count, item.gift_price);
        Glide.with(mContext)
                .load(Constant.BASE_URL2 + item.gift_img)
                .placeholder(R.mipmap.default_img)
                .into((ImageView) helper.getView(R.id.iv_gift));
    }
}
