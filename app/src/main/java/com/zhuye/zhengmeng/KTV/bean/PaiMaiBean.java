package com.zhuye.zhengmeng.KTV.bean;

import java.util.ArrayList;

/**
 * Created by hpc on 2017/11/15.
 */

public class PaiMaiBean {
    public String msg;
    public int code;
    public ArrayList<Data> data;

    public class Data {
        public String song_name;
        public String user_nicename;
        public String avatar;

    }
}
