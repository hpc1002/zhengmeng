package com.zhuye.zhengmeng.user.adapter;

import android.support.annotation.Nullable;
import android.view.View;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.zhuye.zhengmeng.Constant;
import com.zhuye.zhengmeng.R;
import com.zhuye.zhengmeng.user.bean.VipBean;

import java.util.List;

/**
 * Created by hpc on 2017/12/4.
 */

public class VipListAdapter extends BaseQuickAdapter<VipBean.Data,BaseViewHolder> {
    public VipListAdapter(int layoutResId, @Nullable List<VipBean.Data> data) {
        super(layoutResId, data);
    }

    @Override
    protected void convert(BaseViewHolder helper, VipBean.Data item) {
        helper.getView(R.id.gold_number).setVisibility(View.GONE);
//        helper.setText(R.id.gold_number,item.vip_price+"元");
//        helper.getView(R.id.vip_time).setVisibility(View.VISIBLE);
//        helper.setText(R.id.vip_time,item.vip_time+"个月");
        Glide.with(mContext)
                .load(Constant.BASE_URL2+item.vip_img)
                .placeholder(R.mipmap.zhanwei1)
                .into((ImageView) helper.getView(R.id.iv_gold_item));
    }
}
