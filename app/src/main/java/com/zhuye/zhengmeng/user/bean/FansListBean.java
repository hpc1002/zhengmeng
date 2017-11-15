package com.zhuye.zhengmeng.user.bean;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * Created by hpc on 2017/10/31.
 * 礼物
 */

public class FansListBean implements Serializable {
    public String msg;
    public int code;
    public ArrayList<Data> data;

    public class Data implements Serializable{
        public int user_id;
        public String user_nicename;
        public String avatar;

    }
}
