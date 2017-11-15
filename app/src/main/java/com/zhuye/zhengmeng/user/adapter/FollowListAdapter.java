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
import com.zhuye.zhengmeng.user.bean.FansListBean;

import java.util.List;

/**
 * Created by hpc on 2017/11/8.
 */

public class FollowListAdapter extends BaseQuickAdapter<FansListBean.Data, BaseViewHolder> {
    private Context context;

    public FollowListAdapter(int layoutResId, @Nullable List<FansListBean.Data> data, Context context) {
        super(layoutResId, data);
        this.context = context;
    }

    @Override
    protected void convert(BaseViewHolder helper, FansListBean.Data item) {
        helper.setText(R.id.tv_nickname, item.user_nicename);
        helper.addOnClickListener(R.id.tv_delete);
        Glide.with(context)
                .load(Constant.BASE_URL2 + item.avatar)
                .centerCrop()
                .placeholder(R.mipmap.default_img)
                .into((ImageView) helper.getView(R.id.iv_user));
    }
}
