package com.zhuye.zhengmeng.shop.bean;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by hpc on 2017/12/4.
 */

public class GiftDetailBean {
    public String msg;
    public int code;
    public Data data;

    public class Data {
        public ArrayList<ShopList> shop_list;
        public ArrayList<KnapsackLlist> knapsack_list;
        public String score;

        public class ShopList {
            public String gift_id;
            public String gift_name;
            public String gift_price;
            public String gift_descript;
            public String gift_img;
            public String gift_time;
            public String gift_type;
            public String gift_rmb;
        }

        public class KnapsackLlist {
            public String gift_name;
            public String gift_id;
            public String gift_price;
            public String gift_time;
            public String gift_img;
        }
    }
}
