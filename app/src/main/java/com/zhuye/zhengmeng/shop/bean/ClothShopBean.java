package com.zhuye.zhengmeng.shop.bean;

import java.util.List;

/**
 * Created by hpc on 2017/12/3.
 */

public class ClothShopBean {
    public String msg;
    public int code;
    public List<Data> data;

    public class Data {
        public String gift_id;
        public String gift_name;
        public String gift_descript;
        public String gift_img;
        public String gift_price;
        public String gift_time;
        public String gift_type;
        public String gift_rmb;

    }
}
