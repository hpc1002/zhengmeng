package com.zhuye.zhengmeng.dynamic.viewHolder;

import android.view.View;
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
import com.zhuye.zhengmeng.utils.ToastManager;

/**
 * Created by hpc on 2017/11/1.
 */

public class CommentListViewHolder extends BaseViewHolder<CommentListBean.Data> {

    private TextView observer_name, observer_content, tv_zan, pinlun_time, replyCount;
    private ImageView img_observer, iv_zan;

    public CommentListViewHolder(ViewGroup itemView) {
        super(itemView, R.layout.item_comment);
        observer_name = $(R.id.observer_name);
        observer_content = $(R.id.observer_content);
        tv_zan = $(R.id.tv_zan);
        pinlun_time = $(R.id.pinlun_time);
        replyCount = $(R.id.reply_count);
        img_observer = $(R.id.img_observer);
        iv_zan = $(R.id.iv_zan);

    }


    @Override
    public void setData(final CommentListBean.Data data) {
        super.setData(data);
        observer_name.setText(data.user_nicename);
        observer_content.setText(data.content);
        tv_zan.setText(data.comment_like);
        pinlun_time.setText(DateUtil.toDate(Long.parseLong(data.comment_time)));
        String reply_count = data.reply_count;
        int i = Integer.parseInt(reply_count);
        if (i > 0) {
            replyCount.setVisibility(View.VISIBLE);
            replyCount.setText(data.reply_count + "条回复");
            replyCount.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    mCallBack.OnReplyItemClickListener(data.sen_user_id);
                }
            });
        } else {
            replyCount.setVisibility(View.GONE);
        }
        int is_like = data.is_like;
        if (is_like == 1) {
            iv_zan.setImageResource(R.mipmap.praise_on);
            return;
        } else if (is_like == 0) {
            iv_zan.setImageResource(R.mipmap.praise);
        }
        iv_zan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mCallBack.OnItemClickListener(data.comment_id);
            }
        });

        Glide.with(getContext())
                .load(Constant.BASE_URL2 + data.avatar)
                .placeholder(R.mipmap.default_img)
                .bitmapTransform(new CenterCrop(getContext()))
                .into(img_observer);
    }

    private allCheck mCallBack;

    public void setCallBack(allCheck callBack) {
        mCallBack = callBack;
    }

    public interface allCheck {
        void OnItemClickListener(String id);
        void OnReplyItemClickListener(String id);
    }
}
