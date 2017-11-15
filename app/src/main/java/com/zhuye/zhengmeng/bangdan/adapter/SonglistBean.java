package com.zhuye.zhengmeng.bangdan.adapter;

import java.io.Serializable;
import java.util.List;

/**
 * Created by zzzy on 2017/11/8.
 */

public class SonglistBean implements Serializable{


    /**
     * data : [{"song_id":"179","song_name":"周杰伦 - 鞋子特大号","song_path":"/changba/Upload/song/2017-11-02/zhoujielun - xiezitedahao.mp3","lyric_path":"/changba/Upload/song/2017-11-02/zhoujielun - xiezitedahao.lrc","song_click":"0","song_img":"0","is_have":0},{"song_id":"178","song_name":"周杰伦 - 算什么男人","song_path":"/changba/Upload/song/2017-11-02/zhoujielun - suanshimenanren.mp3","lyric_path":"/changba/Upload/song/2017-11-02/zhoujielun - suanshimenanren.lrc","song_click":"0","song_img":"0","is_have":0},{"song_id":"59","song_name":"周杰伦 - 窃爱","song_path":"/changba/Upload/song/2017-11-02/zhoujielun - qieai.mp3","lyric_path":"/changba/Upload/song/2017-11-02/zhoujielun - qieai.lrc","song_click":"0","song_img":"0","is_have":0},{"song_id":"8","song_name":"周杰伦 - 告白气球","song_path":"/changba/Upload/song/2017-11-02/zhoujielun - gaobaiqiqiu.mp3","lyric_path":"/changba/Upload/song/2017-11-02/zhoujielun - gaobaiqiqiu.lrc","song_click":"0","song_img":"0","is_have":0},{"song_id":"54","song_name":"张学友 - 情书","song_path":"/changba/Upload/song/2017-11-02/zhangxueyou - qingshu.mp3","lyric_path":"/changba/Upload/song/2017-11-02/zhangxueyou - qingshu.lrc","song_click":"0","song_img":"0","is_have":0},{"song_id":"53","song_name":"张信哲 - 白月光","song_path":"/changba/Upload/song/2017-11-02/zhangxinzhe - baiyueguang.mp3","lyric_path":"/changba/Upload/song/2017-11-02/zhangxinzhe - baiyueguang.lrc","song_click":"0","song_img":"0","is_have":0},{"song_id":"172","song_name":"张赫宣 - 你是我心爱的姑娘 (Live) ","song_path":"/changba/Upload/song/2017-11-02/zhanghexuan - nishiwoxinaideguniang (Live) .mp3","lyric_path":"/changba/Upload/song/2017-11-02/zhanghexuan - nishiwoxinaideguniang (Live).lrc","song_click":"0","song_img":"0","is_have":0},{"song_id":"171","song_name":"张碧晨 - 年轮 (Live)","song_path":"/changba/Upload/song/2017-11-02/zhangbichen - nianlun (Live).mp3","lyric_path":"/changba/Upload/song/2017-11-02/zhangbichen - nianlun (Live).lrc","song_click":"0","song_img":"0","is_have":0},{"song_id":"170","song_name":"艺涛 - 夜色","song_path":"/changba/Upload/song/2017-11-02/yitao - yese.mp3","lyric_path":"/changba/Upload/song/2017-11-02/yitao - yese.lrc","song_click":"0","song_img":"0","is_have":0},{"song_id":"169","song_name":"杨宗纬 - 一次就好","song_path":"/changba/Upload/song/2017-11-02/yangzongwei - yicijiuhao.mp3","lyric_path":"/changba/Upload/song/2017-11-02/yangzongwei - yicijiuhao.lrc","song_click":"0","song_img":"0","is_have":0},{"song_id":"168","song_name":"杨坤 - 一万个女人","song_path":"/changba/Upload/song/2017-11-02/yangkun - yiwangenvren.mp3","lyric_path":"/changba/Upload/song/2017-11-02/yangkun - yiwangenvren.lrc","song_click":"0","song_img":"0","is_have":0},{"song_id":"166","song_name":"杨坤 - 答案","song_path":"/changba/Upload/song/2017-11-02/yangkun - daan.mp3","lyric_path":"/changba/Upload/song/2017-11-02/yangkun - daan.lrc","song_click":"0","song_img":"0","is_have":0},{"song_id":"164","song_name":"薛之谦 - 火星人来过","song_path":"/changba/Upload/song/2017-11-02/xuezhiqian - huoxingrenlaiguo.mp3","lyric_path":"/changba/Upload/song/2017-11-02/xuezhiqian - huoxingrenlaiguo.lrc","song_click":"0","song_img":"0","is_have":0},{"song_id":"163","song_name":"许巍 - 时光","song_path":"/changba/Upload/song/2017-11-02/xuwei - shiguang.mp3","lyric_path":"/changba/Upload/song/2017-11-02/xuwei - shiguang.lrc","song_click":"0","song_img":"0","is_have":0},{"song_id":"162","song_name":"萧敬腾 - 我在人民广场吃炸鸡 (Live)","song_path":"/changba/Upload/song/2017-11-02/xiaojingteng - wozairenminguangchangchizhaji (Live).mp3","lyric_path":"/changba/Upload/song/2017-11-02/xiaojingteng - wozairenminguangchangchizhaji (Live).lrc","song_click":"0","song_img":"0","is_have":0},{"song_id":"161","song_name":"萧煌奇 - 末班车","song_path":"/changba/Upload/song/2017-11-02/xiaohuangqi - mobanche.mp3","lyric_path":"/changba/Upload/song/2017-11-02/xiaohuangqi - mobanche.lrc","song_click":"0","song_img":"0","is_have":0},{"song_id":"160","song_name":"王童语 - 丫头","song_path":"/changba/Upload/song/2017-11-02/wangtongyu - yatou.mp3.qmfpd2","lyric_path":"/changba/Upload/song/2017-11-02/wangtongyu - yatou.lrc","song_click":"0","song_img":"0","is_have":0}]
     * msg :
     * code : 200
     */

    private String msg;
    private int code;
    private List<DataBean> data;

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

    public List<DataBean> getData() {
        return data;
    }

    public void setData(List<DataBean> data) {
        this.data = data;
    }

    public static class DataBean implements Serializable {
        /**
         * song_id : 179
         * song_name : 周杰伦 - 鞋子特大号
         * song_path : /changba/Upload/song/2017-11-02/zhoujielun - xiezitedahao.mp3
         * lyric_path : /changba/Upload/song/2017-11-02/zhoujielun - xiezitedahao.lrc
         * song_click : 0
         * song_img : 0
         * is_have : 0
         */

        private String song_id;
        private String song_name;
        private String song_path;
        private String lyric_path;
        private String song_click;
        private String song_img;
        private int is_have;

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

        public int getIs_have() {
            return is_have;
        }

        public void setIs_have(int is_have) {
            this.is_have = is_have;
        }
    }
}
