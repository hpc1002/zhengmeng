package com.zhuye.zhengmeng.dynamic.viewHolder;

import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.bitmap.CenterCrop;
import com.jude.easyrecyclerview.adapter.BaseViewHolder;
import com.zhuye.zhengmeng.Constant;
import com.zhuye.zhengmeng.R;
import com.zhuye.zhengmeng.dynamic.bean.CommentListBean;
import com.zhuye.zhengmeng.utils.DateUtil;

/**
 * Created by hpc on 2017/11/1.
 */

public class ErjiCommentListViewHolder extends BaseViewHolder<CommentListBean.Data> {

    private TextView tv_user_name, tv_content, tv_time, tv_zan;
    private ImageView iv_user;

    public ErjiCommentListViewHolder(ViewGroup itemView) {
        super(itemView, R.layout.item_erji_comment);
        tv_user_name = $(R.id.tv_user_name);
        tv_content = $(R.id.tv_content);
        tv_time = $(R.id.tv_time);
        tv_zan = $(R.id.tv_zan);
        iv_user = $(R.id.iv_user);


    }


    @Override
    public void setData(CommentListBean.Data data) {
        super.setData(data);
        tv_user_name.setText(data.user_nicename);
        tv_content.setText(data.content);
        tv_zan.setText(data.comment_like);
        tv_time.setText(DateUtil.toDate(Long.parseLong(data.comment_time)));

        Glide.with(getContext())
                .load(Constant.BASE_URL2 + data.avatar)
                .placeholder(R.mipmap.default_img)
                .bitmapTransform(new CenterCrop(getContext()))
                .into(iv_user);
    }
}
