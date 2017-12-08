package com.zhuye.zhengmeng.user.adapter;

import android.support.annotation.Nullable;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.zhuye.zhengmeng.R;
import com.zhuye.zhengmeng.user.bean.MessageBean;

import java.util.List;

/**
 * Created by hpc on 2017/12/8.
 */

public class MessageAdapter extends BaseQuickAdapter<MessageBean, BaseViewHolder> {
    public MessageAdapter(int layoutResId, @Nullable List<MessageBean> data) {
        super(layoutResId, data);
    }

    @Override
    protected void convert(BaseViewHolder helper, MessageBean item) {
        helper.setText(R.id.message_title, item.title);
        helper.setText(R.id.message_date, item.date);
        helper.setText(R.id.message_content, item.content);
    }
}
