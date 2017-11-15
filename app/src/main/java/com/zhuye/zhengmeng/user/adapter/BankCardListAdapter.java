package com.zhuye.zhengmeng.user.adapter;

import android.support.annotation.Nullable;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.zhuye.zhengmeng.DataProvider;
import com.zhuye.zhengmeng.R;
import com.zhuye.zhengmeng.user.bean.BankCardListBean;

import java.util.List;
import java.util.Random;

/**
 * Created by hpc on 2017/11/9.
 */

public class BankCardListAdapter extends BaseQuickAdapter<BankCardListBean.Data, BaseViewHolder> {
    public BankCardListAdapter(int layoutResId, @Nullable List<BankCardListBean.Data> data) {
        super(layoutResId, data);
    }

    @Override
    protected void convert(BaseViewHolder helper, BankCardListBean.Data item) {
        helper.setText(R.id.bank_name, item.bank_card_name);
        helper.setText(R.id.bank_number, item.bank_card_numbers);
        Random rand = new Random();
        int i = rand.nextInt(4);
        helper.getView(R.id.ll_bank).setBackgroundResource(DataProvider.getBankBackImage().get(i));
    }
}
