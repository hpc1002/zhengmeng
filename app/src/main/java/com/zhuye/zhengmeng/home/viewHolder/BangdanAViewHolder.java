package com.zhuye.zhengmeng.home.viewHolder;

import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.bitmap.CenterCrop;
import com.jude.easyrecyclerview.adapter.BaseViewHolder;
import com.zhuye.zhengmeng.R;
import com.zhuye.zhengmeng.home.bean.BangDanListBean;

/**
 * Created by hpc on 2017/11/1.
 */

public class BangdanAViewHolder extends BaseViewHolder<BangDanListBean.Data> {

    private TextView tv_bangdan_name_a, tv_rank_a;
    private ImageView iv_bangdan_a;


    public BangdanAViewHolder(ViewGroup itemView) {
        super(itemView, R.layout.item_bangdan_a);
        tv_bangdan_name_a = $(R.id.tv_bangdan_name_a);
        tv_rank_a = $(R.id.tv_rank_a);
        iv_bangdan_a = $(R.id.iv_bangdan_a);


    }

    @Override
    public void setData(BangDanListBean.Data data) {
        super.setData(data);
        tv_bangdan_name_a.setText(data.production_name + "名字666");
        tv_rank_a.setText("1");
        Glide.with(getContext())
                .load(data.img_url)
                .placeholder(R.mipmap.default_img)
                .bitmapTransform(new CenterCrop(getContext()))
                .into(iv_bangdan_a);
    }
}
