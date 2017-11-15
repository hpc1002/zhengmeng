package com.zhuye.zhengmeng.user.bean;

import java.util.ArrayList;

/**
 * Created by hpc on 2017/10/31.
 */

public class GoldListBean {
    public String msg;
    public int code;
    public Data data;

    public class Data {
        public String score_Focus;
        public String score;
        public ArrayList<GoldList> list;

        public class GoldList {
            public String gold_id;
            public String gold_price;
            public String gold_sum;
            public String gold_img;
        }
    }
}
