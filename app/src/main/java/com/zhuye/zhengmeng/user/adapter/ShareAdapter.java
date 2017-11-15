package com.zhuye.zhengmeng.user.adapter;

import android.content.Context;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.ImageView;

import com.blankj.utilcode.util.ToastUtils;
import com.bumptech.glide.Glide;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.zhuye.zhengmeng.R;
import com.zhuye.zhengmeng.user.bean.ShareListBean;

import java.util.List;

public class ShareAdapter extends BaseQuickAdapter<ShareListBean.DataBean, BaseViewHolder> {

    private Context mContext;


    public ShareAdapter(int layoutResId, @Nullable List<ShareListBean.DataBean> data, Context context) {
        super(layoutResId, data);
        this.mContext = context;
    }

    @Override
    protected void convert(BaseViewHolder helper, ShareListBean.DataBean item) {
//        data_list = item;//获取数据
        //用户id
        String userId = item.getUser_id();
        //头像
        String img_AvatarUrl = item.getAvatar();
        ImageView img_avatar = helper.getView(R.id.img_url);
        Glide.with(mContext).load(img_AvatarUrl).into(img_avatar);
        //作品封面图
        String img_ProductionUrl = item.getImg_url();
        ImageView img_production = helper.getView(R.id.img_url);
        Glide.with(mContext).load(img_ProductionUrl).placeholder(R.mipmap.home_banner).into(img_production);
        //用户名
        helper.setText(R.id.userName, item.getUser_nicename().equals(" ") ? "无名氏" : item.getUser_nicename());

        helper.setText(R.id.avatar, item.getClick());//歌曲点击的次数
        helper.setText(R.id.gift_count, item.getGift_count());//收到礼物的数量
        helper.setText(R.id.share_count, item.getShare_count());//分享的次数
        helper.setText(R.id.comment_count, item.getComment_count());//评论的次数


        helper.setText(R.id.production_name, item.getProduction_name().equals(" ") ? "未知曲艺名" : item.getProduction_name());//作品名称
        helper.setText(R.id.production_content, item.getProduction_content());//作品内容


        String production_type = item.getProduction_type();//播放类型MP3|MP4
        String production_path = item.getProduction_path();//歌曲播放路径

        helper.getView(R.id.userImg).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ToastUtils.showShort("调到用户界面");
            }
        });
        helper.getView(R.id.production_type).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ToastUtils.showShort("调到播放界面");
            }
        });
        helper.getView(R.id.play).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ToastUtils.showShort("播放音乐");
            }
        });

    }



}