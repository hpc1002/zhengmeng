package com.zhuye.zhengmeng.user.adapter;

import android.content.Context;
import android.support.annotation.Nullable;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.zhuye.zhengmeng.Constant;
import com.zhuye.zhengmeng.R;
import com.zhuye.zhengmeng.user.bean.CollectListBean;

import java.util.List;

/**
 * Created by hpc on 2017/11/8.
 */

public class CollectListAdapter extends BaseQuickAdapter<CollectListBean.Data, BaseViewHolder> {
    private Context context;

    public CollectListAdapter(int layoutResId, @Nullable List<CollectListBean.Data> data, Context context) {
        super(layoutResId, data);
        this.context = context;
    }

    @Override
    protected void convert(BaseViewHolder helper, CollectListBean.Data item) {
        helper.setText(R.id.tv_nickname, item.production_name);
        helper.addOnClickListener(R.id.tv_delete);
        Glide.with(context)
                .load(Constant.BASE_URL2 + item.img_url)
                .centerCrop()
                .placeholder(R.mipmap.default_img)
                .into((ImageView) helper.getView(R.id.iv_user));
    }
}
