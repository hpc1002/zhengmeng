package com.zhuye.zhengmeng.user.adapter;

import android.content.Context;
import android.support.annotation.Nullable;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.zhuye.zhengmeng.Constant;
import com.zhuye.zhengmeng.R;
import com.zhuye.zhengmeng.user.bean.FansListBean;

import java.util.List;

/**
 * Created by hpc on 2017/11/8.
 */

public class FansListAdapter extends BaseQuickAdapter<FansListBean.Data, BaseViewHolder> {
    private Context mContext;

    public FansListAdapter(int layoutResId, @Nullable List<FansListBean.Data> data, Context context) {
        super(layoutResId, data);
        this.mContext = context;
    }

    @Override
    protected void convert(BaseViewHolder helper, FansListBean.Data item) {
        helper.setText(R.id.tv_nickname, item.user_nicename);
        Glide.with(mContext)
                .load(Constant.BASE_URL2 + item.avatar)
                .placeholder(R.mipmap.default_img)
                .centerCrop()
                .into((ImageView) helper.getView(R.id.iv_user));
    }
}
