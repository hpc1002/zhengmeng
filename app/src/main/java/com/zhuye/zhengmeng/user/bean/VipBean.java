package com.zhuye.zhengmeng.user.bean;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * Created by hpc on 2017/12/4.
 */

public class VipBean {
    public String msg;
    public int code;
    public ArrayList<Data> data;

    public class Data implements Serializable {
        public String vip_time;
        public String vip_price;
        public String vip_img;
        public String vip_id;

    }
}
