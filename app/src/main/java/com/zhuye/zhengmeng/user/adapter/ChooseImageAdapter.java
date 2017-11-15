package com.zhuye.zhengmeng.user.adapter;

import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.zhuye.zhengmeng.Constant;
import com.zhuye.zhengmeng.R;
import com.zhuye.zhengmeng.user.bean.ImageBean;

import java.util.List;

/**
 * Created by lilei on 2017/11/2.
 */
public class ChooseImageAdapter extends BaseQuickAdapter<ImageBean.DataBean, BaseViewHolder> {

    public ChooseImageAdapter(int layoutResId, List<ImageBean.DataBean> data) {
        super(layoutResId, data);
    }

    @Override
    protected void convert(BaseViewHolder helper, ImageBean.DataBean item) {
        // 加载网络图片
        Glide.with(mContext).load(Constant.BASE_URL_PINJIE + item.getPhoto_path()).into((ImageView) helper.getView(R.id.singleImage));
    }
}