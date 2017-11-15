package com.zhuye.zhengmeng.user.adapter;

import android.support.annotation.Nullable;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.zhuye.zhengmeng.user.bean.RankListBean;

import java.util.List;

/**
 * Created by hpc on 2017/11/9.
 */

public class RankRecordAdapter extends BaseQuickAdapter<RankListBean.Data, BaseViewHolder> {
    public RankRecordAdapter(int layoutResId, @Nullable List<RankListBean.Data> data) {
        super(layoutResId, data);
    }

    @Override
    protected void convert(BaseViewHolder helper, RankListBean.Data item) {

    }
}
