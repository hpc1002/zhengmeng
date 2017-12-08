package com.zhuye.zhengmeng.user.adapter;

import android.support.annotation.Nullable;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.zhuye.zhengmeng.Constant;
import com.zhuye.zhengmeng.R;
import com.zhuye.zhengmeng.dynamic.bean.FansContributionBean;

import java.util.List;

/**
 * Created by hpc on 2017/11/10.
 */

public class FansContributionAdapter extends BaseQuickAdapter<FansContributionBean.DataBean.GiftReceptionListBean, BaseViewHolder> {
    public FansContributionAdapter(int layoutResId, @Nullable List<FansContributionBean.DataBean.GiftReceptionListBean> data) {
        super(layoutResId, data);
    }

    @Override
    protected void convert(BaseViewHolder helper, FansContributionBean.DataBean.GiftReceptionListBean item) {
        helper.setText(R.id.tv_fan_name, item.getUser_nicename());
        helper.setText(R.id.tv_jinbi, item.getSum_score());
        if (item.getAvatar().contains("http")){
            Glide.with(mContext)
                    .load(item.getAvatar())
                    .centerCrop()
                    .placeholder(R.mipmap.touxiang_re)
                    .into((ImageView) helper.getView(R.id.iv_fans_avatar));
        }else{
            Glide.with(mContext)
                    .load(Constant.BASE_URL2 + item.getAvatar())
                    .centerCrop()
                    .placeholder(R.mipmap.touxiang_re)
                    .into((ImageView) helper.getView(R.id.iv_fans_avatar));
        }

    }
}
