package com.zhuye.zhengmeng.home.adapter;

import android.content.Context;
import android.view.View;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.zhuye.zhengmeng.R;

import cn.bingoogolapple.bgabanner.BGABanner;

/**
 * Created by XY on 2016/9/14.
 */
public class BGABannerAdapter implements BGABanner.Adapter {
    private Context context;

    public BGABannerAdapter(Context context) {
        this.context = context;
    }

    @Override
    public void fillBannerItem(BGABanner banner, View view, Object model, int position) {
        Glide.with(context)
                .load(model)
                .crossFade()
                .placeholder(R.mipmap.home_banner)
                .into((ImageView) view);
    }
}
