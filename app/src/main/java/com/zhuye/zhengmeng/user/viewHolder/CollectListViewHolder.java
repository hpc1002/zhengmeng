package com.zhuye.zhengmeng.user.viewHolder;

import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.bitmap.CenterCrop;
import com.jude.easyrecyclerview.adapter.BaseViewHolder;
import com.zhuye.zhengmeng.Constant;
import com.zhuye.zhengmeng.R;
import com.zhuye.zhengmeng.user.bean.CollectListBean;
import com.zhuye.zhengmeng.user.bean.GiftListBean;

/**
 * Created by hpc on 2017/11/1.
 */

public class CollectListViewHolder extends BaseViewHolder<CollectListBean.Data> {

    private TextView tv_nickname, tv_delete;
    private ImageView iv_user;

    public CollectListViewHolder(ViewGroup itemView) {
        super(itemView, R.layout.item_collect);
        iv_user = $(R.id.iv_user);
        tv_nickname = $(R.id.tv_nickname);
        tv_delete = $(R.id.tv_delete);



    }

    @Override
    public void setData(CollectListBean.Data data) {
        super.setData(data);
        tv_nickname.setText(data.production_name);

        Glide.with(getContext())
                .load(Constant.BASE_URL2 + data.img_url)
                .placeholder(R.mipmap.default_img)
                .bitmapTransform(new CenterCrop(getContext()))
                .into(iv_user);
    }
}
