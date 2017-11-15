package com.zhuye.zhengmeng.user.bean;

import java.util.ArrayList;

/**
 * Created by hpc on 2017/10/31.
 * 礼物
 */

public class GiftListBean {
    public String msg;
    public int code;
    public ArrayList<Data> data;

    public class Data {
        public String gift_name;
        public String gift_img;
        public String reception_sum;
        public String user_nicename;

    }
}
