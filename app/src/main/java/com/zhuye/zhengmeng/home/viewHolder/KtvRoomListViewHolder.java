package com.zhuye.zhengmeng.home.viewHolder;

import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.bitmap.CenterCrop;
import com.jude.easyrecyclerview.adapter.BaseViewHolder;
import com.zhuye.zhengmeng.Constant;
import com.zhuye.zhengmeng.DataProvider;
import com.zhuye.zhengmeng.R;
import com.zhuye.zhengmeng.home.bean.KTVListBean;

import java.util.Random;

/**
 * Created by hpc on 2017/11/1.
 */

public class KtvRoomListViewHolder extends BaseViewHolder<KTVListBean.Data> {

    private TextView user_name, tv_time, room_name;
    private ImageView user_avatar;
    private LinearLayout ll_background;

    public KtvRoomListViewHolder(ViewGroup itemView) {
        super(itemView, R.layout.item_ktv);
        user_name = $(R.id.user_name);
        tv_time = $(R.id.tv_time);
        room_name = $(R.id.room_name);
        user_avatar = $(R.id.user_avatar);
        ll_background = $(R.id.ll_background);


    }

    @Override
    public void setData(KTVListBean.Data data) {
        super.setData(data);
        user_name.setText(data.user_nicename);
        tv_time.setText(data.chatroom_time);
        room_name.setText(data.chatroom_name);
        Glide.with(getContext())
                .load(Constant.BASE_URL2 + data.avatar)
                .placeholder(R.mipmap.default_img)
                .bitmapTransform(new CenterCrop(getContext()))
                .into(user_avatar);
        Random rand = new Random();
        int i = rand.nextInt(3);
        ll_background.setBackgroundResource(DataProvider.getNarrowImage().get(i));
    }
}
