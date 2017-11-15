package com.zhuye.zhengmeng.home.fragment.adapter;

import android.content.Context;
import android.support.annotation.Nullable;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.zhuye.zhengmeng.Constant;
import com.zhuye.zhengmeng.DataProvider;
import com.zhuye.zhengmeng.R;
import com.zhuye.zhengmeng.home.bean.KTVListBean;

import java.util.List;
import java.util.Random;

/**
 * Created by hpc on 2017/11/8.
 */

public class KtvRoomListAdapter extends BaseQuickAdapter<KTVListBean.Data, BaseViewHolder> {
    private Context mContext;

    public KtvRoomListAdapter(int layoutResId, @Nullable List<KTVListBean.Data> data, Context context) {
        super(layoutResId, data);
        this.mContext = context;
    }

    @Override
    protected void convert(BaseViewHolder helper, KTVListBean.Data item) {
        helper.setText(R.id.user_name, item.user_nicename);
        helper.setText(R.id.tv_time, item.chatroom_time);
        helper.setText(R.id.room_name, item.chatroom_name);
        helper.setText(R.id.tv_time, item.chatroom_time);
        Glide.with(mContext)
                .load(Constant.BASE_URL2 + item.avatar)
                .centerCrop()
                .placeholder(R.mipmap.default_img)
                .into((ImageView) helper.getView(R.id.user_avatar));

        Random rand = new Random();
        int i = rand.nextInt(3);
        helper.getView(R.id.ll_background).setBackgroundResource(DataProvider.getNarrowImage().get(i));
    }
}
