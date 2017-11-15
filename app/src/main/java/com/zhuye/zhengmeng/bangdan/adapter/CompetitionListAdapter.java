package com.zhuye.zhengmeng.bangdan.adapter;

import android.content.Context;
import android.support.annotation.Nullable;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.zhuye.zhengmeng.R;
import com.zhuye.zhengmeng.home.fragment.model.DynamicModel;

import java.util.List;

/**
 * Created by hpc on 2017/11/8.
 */

public class CompetitionListAdapter extends BaseQuickAdapter<DynamicModel.DataBean, BaseViewHolder> {
    private Context context;

    public CompetitionListAdapter(int layoutResId, @Nullable List<DynamicModel.DataBean> data, Context context) {
        super(layoutResId, data);
        this.context = context;
    }

    @Override
    protected void convert(BaseViewHolder helper, DynamicModel.DataBean item) {
        helper.setText(R.id.userName, item.getUser_nicename().equals(" ") ? "无名氏" : item.getUser_nicename());

        helper.setText(R.id.avatar, item.getClick());//歌曲点击的次数
        helper.setText(R.id.gift_count, item.getGift_count());//收到礼物的数量
        helper.setText(R.id.share_count, item.getShare_count());//分享的次数
        helper.setText(R.id.comment_count, item.getComment_count());//评论的次数


        helper.setText(R.id.production_name, item.getProduction_name().equals(" ") ? "未知曲艺名" : item.getProduction_name());//作品名称
        helper.setText(R.id.production_content, item.getProduction_content());//作品内容
        String img_AvatarUrl = item.getAvatar();
        ImageView img_avatar = helper.getView(R.id.img_url);
        Glide.with(context).load(img_AvatarUrl).into(img_avatar);
        //作品封面图
        String img_ProductionUrl = item.getImg_url();
        ImageView img_production = helper.getView(R.id.img_url);
        Glide.with(context)
                .load(img_ProductionUrl)
                .placeholder(R.mipmap.home_banner)
                .into(img_production);
    }
}
