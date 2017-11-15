package com.zhuye.zhengmeng.bangdan.bean;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * Created by hpc on 2017/11/02.
 * 歌曲
 */

public class SongsListBean implements Serializable {
    public String msg;
    public int code;
    public ArrayList<Data> data;

    public class Data {
        public String song_id;
        public String song_name;
        public String song_path;
        public String lyric_path;
        public String song_click;
        public String song_img;
        public String is_have;

        public String getSong_id() {
            return song_id;
        }

        public void setSong_id(String song_id) {
            this.song_id = song_id;
        }

        public String getSong_name() {
            return song_name;
        }

        public void setSong_name(String song_name) {
            this.song_name = song_name;
        }

        public String getSong_path() {
            return song_path;
        }

        public void setSong_path(String song_path) {
            this.song_path = song_path;
        }

        public String getLyric_path() {
            return lyric_path;
        }

        public void setLyric_path(String lyric_path) {
            this.lyric_path = lyric_path;
        }

        public String getSong_click() {
            return song_click;
        }

        public void setSong_click(String song_click) {
            this.song_click = song_click;
        }

        public String getSong_img() {
            return song_img;
        }

        public void setSong_img(String song_img) {
            this.song_img = song_img;
        }

        public String getIs_have() {
            return is_have;
        }

        public void setIs_have(String is_have) {
            this.is_have = is_have;
        }
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public ArrayList<Data> getData() {
        return data;
    }

    public void setData(ArrayList<Data> data) {
        this.data = data;
    }
}
