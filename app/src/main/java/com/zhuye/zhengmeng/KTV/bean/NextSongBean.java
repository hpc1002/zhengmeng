package com.zhuye.zhengmeng.KTV.bean;

import java.util.ArrayList;

/**
 * Created by hpc on 2017/11/22.
 */

public class NextSongBean {
    public String msg;
    public int code;
    public Data data;

    public class Data {
        public String song_path;
        public String lyric_path;
        public String user_id;
        public String is_play;
        public String avatar;

    }
}
