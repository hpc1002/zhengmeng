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

public class BangdanBViewHolder extends BaseViewHolder<BangDanListBean.Data> {

    private TextView tv_bangdan_name_b, tv_rank_b;
    private ImageView iv_bangdan_b;

    public BangdanBViewHolder(ViewGroup itemView) {
        super(itemView, R.layout.item_bangdan_b);
        tv_bangdan_name_b = $(R.id.tv_bangdan_name_b);
        tv_rank_b = $(R.id.tv_rank_b);
        iv_bangdan_b = $(R.id.iv_bangdan_b);
    }

    @Override
    public void setData(BangDanListBean.Data data) {
        super.setData(data);
        tv_bangdan_name_b.setText(data.production_name + "名字666");

        tv_rank_b.setText(data.sum);

        Glide.with(getContext())
                .load(data.img_url)
                .placeholder(R.mipmap.default_img)
                .bitmapTransform(new CenterCrop(getContext()))
                .into(iv_bangdan_b);
    }
}
