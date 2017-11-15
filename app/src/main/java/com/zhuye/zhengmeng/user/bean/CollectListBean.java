package com.zhuye.zhengmeng.user.bean;

import java.util.ArrayList;

/**
 * Created by hpc on 2017/10/31.
 * 礼物
 */

public class CollectListBean {
    public String msg;
    public int code;
    public ArrayList<Data> data;

    public class Data {
        public String production_id;
        public String production_name;
        public String img_url;
        public String collect_id;

    }
}
