package com.zhuye.zhengmeng.KTV.adapter;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.annotation.Nullable;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.zhuye.zhengmeng.Constant;
import com.zhuye.zhengmeng.KTV.bean.Fushi;
import com.zhuye.zhengmeng.R;
import com.zhuye.zhengmeng.shop.bean.ClothShopBean;
import com.zhuye.zhengmeng.shop.bean.GiftShopBean;
import com.zhuye.zhengmeng.utils.BitmapUtil;

import java.util.List;

import pl.droidsonroids.gif.GifImageView;

/**
 * Created by hpc on 2017/11/16.
 */

public class FushiAdapter extends BaseQuickAdapter<ClothShopBean.Data, BaseViewHolder> {
    public FushiAdapter(int layoutResId, @Nullable List<ClothShopBean.Data> data) {
        super(layoutResId, data);
    }

    @Override
    protected void convert(BaseViewHolder helper, ClothShopBean.Data item) {
        Glide.with(mContext)
                .load(Constant.BASE_URL2+item.gift_img)
                .centerCrop()
                .diskCacheStrategy(DiskCacheStrategy.SOURCE)
                .into((GifImageView) helper.getView(R.id.fushi_image));
//        Bitmap bitmap = BitmapFactory.decodeFile(Constant.BASE_URL2 + item.gift_img);
//        ((GifImageView) helper.getView(R.id.fushi_image)).setImageBitmap(bitmap);
    }
}
