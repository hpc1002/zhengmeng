package com.zhuye.zhengmeng.home.bean;

import java.util.ArrayList;

/**
 * Created by hpc on 2017/11/7.
 */

public class BangDanListBean {
    public String msg;
    public int code;
    public ArrayList<Data> data;
    public class Data {
        public String production_name;
        public String production_id;
        public String production_path;
        public String production_type;
        public String production_content;
        public String img_url;
        public String sum;
    }
}
