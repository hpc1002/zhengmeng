package com.zhuye.zhengmeng.dynamic.adapter;

import android.content.Context;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.zhuye.zhengmeng.Constant;
import com.zhuye.zhengmeng.R;
import com.zhuye.zhengmeng.dynamic.bean.CommentListBean;
import com.zhuye.zhengmeng.utils.DateUtil;

import java.util.List;

/**
 * Created by hpc on 2017/11/8.
 */

public class CommentListAdapter extends BaseQuickAdapter<CommentListBean.Data, BaseViewHolder> {
    private Context mContext;

    public CommentListAdapter(int layoutResId, @Nullable List<CommentListBean.Data> data, Context context) {
        super(layoutResId, data);
        this.mContext = context;
    }


    @Override
    protected void convert(BaseViewHolder helper, CommentListBean.Data item) {
        helper.setText(R.id.observer_name, item.user_nicename);
        helper.setText(R.id.observer_content, item.content);
        helper.setText(R.id.tv_zan, item.comment_like);

        helper.setText(R.id.pinlun_time, DateUtil.toDate(Long.parseLong(item.comment_time)));
        helper.addOnClickListener(R.id.iv_zan);

        String reply_count = item.reply_count;
        int i = Integer.parseInt(reply_count);

        if (i > 0) {
            helper.getView(R.id.reply_count).setVisibility(View.VISIBLE);
            helper.setText(R.id.reply_count, item.reply_count + "条回复");
            helper.addOnClickListener(R.id.reply_count);
        } else {
            helper.getView(R.id.reply_count).setVisibility(View.GONE);
        }

        int is_like = item.is_like;
        if (is_like == 1) {
            helper.getView(R.id.iv_zan).setBackgroundResource(R.mipmap.praise_on);
            return;
        } else if (is_like == 0) {
            helper.getView(R.id.iv_zan).setBackgroundResource(R.mipmap.praise);
            helper.addOnClickListener(R.id.iv_zan);
        }
//        helper.addOnClickListener(R.id.production_type);
        Glide.with(mContext)
                .load(Constant.BASE_URL2 + item.avatar)
                .centerCrop()
                .placeholder(R.mipmap.default_img)
                .into((ImageView) helper.getView(R.id.img_observer));
    }
}
