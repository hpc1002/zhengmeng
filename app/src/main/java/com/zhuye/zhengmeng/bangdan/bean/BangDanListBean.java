package com.zhuye.zhengmeng.bangdan.bean;

import java.util.ArrayList;

/**
 * Created by hpc on 2017/11/8.
 */

public class BangDanListBean {
    public String msg;
    public int code;
    public ArrayList<Data> data;

    public class Data {
        public String num;
        public String production_id;
        public String user_nicename;
        public String avatar;
        public String song_name;
    }
}
