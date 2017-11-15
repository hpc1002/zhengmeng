package com.zhuye.zhengmeng.dynamic.bean;

import java.util.ArrayList;

/**
 * Created by hpc on 2017/10/31.
 * 礼物
 */

public class ErjiCommentListBean {
    public String msg;
    public int code;
    public ArrayList<Data> data;

    public class Data {
        public String comment_id;
        public String sen_user_id;
        public String content;
        public String comment_time;
        public String comment_like;
        public int is_like;
        public String reply_count;
        public String avatar;
        public String user_nicename;

    }
}
