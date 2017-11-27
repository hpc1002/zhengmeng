package com.zhuye.zhengmeng.user.adapter;

import android.support.annotation.Nullable;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.zhuye.zhengmeng.R;
import com.zhuye.zhengmeng.user.bean.Mp3ListBean;

import java.util.List;

/**
 * Created by hpc on 2017/11/25.
 */

public class Mp3ListAdapter extends BaseQuickAdapter<String,BaseViewHolder>{
    public Mp3ListAdapter(int layoutResId, @Nullable List<String> data) {
        super(layoutResId, data);
    }

    @Override
    protected void convert(BaseViewHolder helper, String item) {
        helper.setText(R.id.mp3_name,item);
    }
}
