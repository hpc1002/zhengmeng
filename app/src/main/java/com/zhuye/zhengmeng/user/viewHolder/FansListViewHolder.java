package com.zhuye.zhengmeng.user.viewHolder;

import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.bitmap.CenterCrop;
import com.jude.easyrecyclerview.adapter.BaseViewHolder;
import com.zhuye.zhengmeng.Constant;
import com.zhuye.zhengmeng.R;
import com.zhuye.zhengmeng.user.bean.FansListBean;

/**
 * Created by hpc on 2017/11/1.
 */

public class FansListViewHolder extends BaseViewHolder<FansListBean.Data> {

    private TextView tv_nickname;
    private ImageView iv_user;

    public FansListViewHolder(ViewGroup itemView) {
        super(itemView, R.layout.item_fan);
        tv_nickname = $(R.id.tv_nickname);
        iv_user = $(R.id.iv_user);


    }

    @Override
    public void setData(FansListBean.Data data) {
        super.setData(data);
        tv_nickname.setText(data.user_nicename);
        Glide.with(getContext())
                .load(Constant.BASE_URL2 + data.avatar)
                .placeholder(R.mipmap.default_img)
                .bitmapTransform(new CenterCrop(getContext()))
                .into(iv_user);
    }
}
