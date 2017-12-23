package com.zhuye.zhengmeng.bangdan.bean;

import java.util.ArrayList;

/**
 * Created by hpc on 2017/12/18.
 */

public class CompetitionDetailBean {
    public String msg;
    public int code;
    public Data data;

    public class Data {
        public ArrayList<ActiveDetail> active_detail;
        public ArrayList<SongList> song_list;
        public class ActiveDetail{
            public String activity_title;
            public String activity_content;
            public String activity_img;
            public String activity_id;
            public String song_id;
        }
        public class SongList{
            public String song_id;
            public String song_name;
            public String song_path;
            public String lyric_path;
            public String song_img;
        }

    }
}
