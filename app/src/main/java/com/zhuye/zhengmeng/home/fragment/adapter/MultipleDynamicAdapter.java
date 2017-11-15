package com.zhuye.zhengmeng.home.fragment.adapter;

import android.content.Context;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.chad.library.adapter.base.BaseMultiItemQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.zhuye.zhengmeng.Constant;
import com.zhuye.zhengmeng.R;
import com.zhuye.zhengmeng.home.fragment.model.DynamicModel;

import java.util.List;

/**
 * Created by hpc on 2017/11/9.
 */

public class MultipleDynamicAdapter extends BaseMultiItemQuickAdapter<DynamicModel.DataBean, BaseViewHolder> {
    private Context context;

    /**
     * Same as QuickAdapter#QuickAdapter(Context,int) but with
     * some initialization data.
     *
     * @param data A new list is created out of this one to avoid mutable list
     */
    public MultipleDynamicAdapter(List<DynamicModel.DataBean> data, Context context) {
        super(data);
        this.context = context;
        addItemType(DynamicModel.DataBean.MP3, R.layout.fragment_dynamic_item2);
        addItemType(DynamicModel.DataBean.MP4, R.layout.fragment_dynamic_item1);
    }

    @Override
    protected void convert(BaseViewHolder helper, DynamicModel.DataBean item) {
        switch (helper.getItemViewType()) {
            case DynamicModel.DataBean.MP3:
                helper.setText(R.id.userName, item.getUser_nicename().equals(" ") ? "无名氏" : item.getUser_nicename());

                helper.setText(R.id.avatar, item.getClick());//歌曲点击的次数
                helper.setText(R.id.gift_count, item.getGift_count());//收到礼物的数量
                helper.setText(R.id.share_count, item.getShare_count());//分享的次数
                helper.setText(R.id.comment_count, item.getComment_count());//评论的次数


                helper.setText(R.id.production_name, item.getProduction_name().equals(" ") ? "未知曲艺名" : item.getProduction_name());//作品名称
                helper.setText(R.id.production_content, item.getProduction_content());//作品内容

                Glide.with(context)
                        .load(Constant.BASE_URL2 + item.getAvatar())
                        .centerCrop()
                        .placeholder(R.mipmap.default_img)
                        .into((ImageView) helper.getView(R.id.userImg));
                Glide.with(context)
                        .load(item.getImg_url())
                        .placeholder(R.mipmap.home_banner)
                        .into((ImageView) helper.getView(R.id.img_url));
                break;
            case DynamicModel.DataBean.MP4:
                //头像
                String img_AvatarUrl = item.getAvatar();
                ImageView img_avatar = helper.getView(R.id.userImg);
                Glide.with(context).load(Constant.BASE_URL2 + img_AvatarUrl).into(img_avatar);
                //作品封面图
                String img_ProductionUrl = item.getImg_url();
                ImageView img_production = helper.getView(R.id.img_url);
                Glide.with(context)
                        .load(img_ProductionUrl)
                        .placeholder(R.mipmap.home_banner)
                        .into(img_production);
                //用户名
                helper.setText(R.id.userName, item.getUser_nicename().equals(" ") ? "无名氏" : item.getUser_nicename());

                helper.setText(R.id.avatar, item.getClick());//歌曲点击的次数
                helper.setText(R.id.gift_count, item.getGift_count());//收到礼物的数量
                helper.setText(R.id.share_count, item.getShare_count());//分享的次数
                helper.setText(R.id.comment_count, item.getComment_count());//评论的次数


                helper.setText(R.id.production_name, item.getProduction_name().equals(" ") ? "未知曲艺名" : item.getProduction_name());//作品名称
                helper.setText(R.id.production_content, item.getProduction_content());//作品内容
                break;
        }
    }
}
