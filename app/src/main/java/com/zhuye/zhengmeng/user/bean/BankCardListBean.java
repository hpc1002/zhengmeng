package com.zhuye.zhengmeng.user.bean;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * Created by hpc on 2017/10/31.
 * 礼物
 */

public class BankCardListBean implements Serializable {
    public String msg;
    public int code;
    public ArrayList<Data> data;

    public class Data implements Serializable{
        public int bank_card_id;
        public String bank_card_numbers;
        public String bank_card_name;

    }
}
