package com.zhuye.zhengmeng.user.bean;

import java.io.Serializable;
import java.util.List;

/**
 * Created by zzzy on 2017/11/1.
 */

public class ShareListBean implements Serializable{

    /**
     * data : [{"production_type":"0","user_id":"7","production_id":"587","production_path":"/changba/Uploads/Video/2017-11-01/59f93be054584.mp4","production_name":" ","production_content":"考虑咯0","click":"0","img_url":"http://192.168.0.134/changba/Thumb/img/15089246169968.jpg","user_nicename":" ","avatar":"  ","gift_count":"0","share_count":"0","comment_count":"0"},{"production_type":"1","user_id":"7","production_id":"561","production_path":"/changba/Uploads/Video/2017-10-27/59f294aeef63c.mp4","production_name":" ","production_content":"考虑咯咯哦哦哦哦哦哦1","click":"11","img_url":"http://192.168.0.134/changba/Thumb/img/15089246169968.jpg","user_nicename":" ","avatar":"  ","gift_count":"0","share_count":"0","comment_count":"0"},{"production_type":"1","user_id":"7","production_id":"559","production_path":"/changba/Uploads/Video/2017-10-27/59f2929f357e7.mp4","production_name":" ","production_content":"考虑考虑考虑兔兔2","click":"0","img_url":"http://192.168.0.134/changba/Thumb/img/15089246169968.jpg","user_nicename":" ","avatar":"  ","gift_count":"0","share_count":"0","comment_count":"0"},{"production_type":"1","user_id":"7","production_id":"557","production_path":"/changba/Uploads/Video/2017-10-26/59f1c217b1c7c.mp4","production_name":" ","production_content":"啊可口可乐可口可乐可口可乐可口可乐3","click":"1","img_url":"http://192.168.0.134/changba/Thumb/img/15089246169968.jpg","user_nicename":" ","avatar":"  ","gift_count":"0","share_count":"0","comment_count":"0"},{"production_type":"1","user_id":"7","production_id":"556","production_path":"/changba/Uploads/Video/2017-10-26/59f1c10ec9b62.caf","production_name":" ","production_content":"不会是这样吗\u2026\u2026可口可乐可口可乐的消息4","click":"0","img_url":"http://192.168.0.134/changba/Thumb/img/15089246169968.jpg","user_nicename":" ","avatar":"  ","gift_count":"0","share_count":"0","comment_count":"0"},{"production_type":"1","user_id":"7","production_id":"555","production_path":"/changba/Uploads/Video/2017-10-26/59f1c0b543a93.caf","production_name":" ","production_content":"啊啊可口可乐可口可乐5","click":"0","img_url":"http://192.168.0.134/changba/Thumb/img/15089246169968.jpg","user_nicename":" ","avatar":"  ","gift_count":"0","share_count":"0","comment_count":"0"},{"production_type":"1","user_id":"7","production_id":"554","production_path":"/changba/Uploads/Video/2017-10-26/59f1bdc6b7d57.mp4","production_name":" ","production_content":"可口可乐了可口可乐了可口可乐6","click":"0","img_url":"http://192.168.0.134/changba/Thumb/img/15089246169968.jpg","user_nicename":" ","avatar":"  ","gift_count":"0","share_count":"0","comment_count":"0"},{"production_type":"1","user_id":"7","production_id":"553","production_path":"/changba/Uploads/Video/2017-10-26/59f1bcec33f0c.mp4","production_name":" ","production_content":"这么多年都没看到过这么的好电影7","click":"0","img_url":"http://192.168.0.134/changba/Thumb/img/15089246169968.jpg","user_nicename":" ","avatar":"  ","gift_count":"0","share_count":"0","comment_count":"0"},{"production_type":"1","user_id":"7","production_id":"550","production_path":"/changba/Uploads/Video/2017-10-26/59f14fdfb243b.caf","production_name":" ","production_content":"8","click":"0","img_url":"http://192.168.0.134/changba/Thumb/img/15089246169968.jpg","user_nicename":" ","avatar":"  ","gift_count":"0","share_count":"0","comment_count":"0"},{"production_type":"1","user_id":" ","production_id":"548","production_path":"/changba/Uploads/Video/2017-10-25/59f05cc83eae5.mp4","production_name":" ","production_content":"9","click":" ","img_url":"http://192.168.0.134/changba/Thumb/img/15089246169968.jpg","user_nicename":" ","avatar":" ","gift_count":"0","share_count":"0","comment_count":"0"}]
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

    public static class DataBean {
        /**
         * production_type : 0
         * user_id : 7
         * production_id : 587
         * production_path : /changba/Uploads/Video/2017-11-01/59f93be054584.mp4
         * production_name :
         * production_content : 考虑咯0
         * click : 0
         * img_url : http://192.168.0.134/changba/Thumb/img/15089246169968.jpg
         * user_nicename :
         * avatar :
         * gift_count : 0
         * share_count : 0
         * comment_count : 0
         */

        private String production_type;
        private String user_id;
        private String production_id;
        private String production_path;
        private String production_name;
        private String production_content;
        private String click;
        private String img_url;
        private String user_nicename;
        private String avatar;
        private String gift_count;
        private String share_count;
        private String comment_count;

        public String getProduction_type() {
            return production_type;
        }

        public void setProduction_type(String production_type) {
            this.production_type = production_type;
        }

        public String getUser_id() {
            return user_id;
        }

        public void setUser_id(String user_id) {
            this.user_id = user_id;
        }

        public String getProduction_id() {
            return production_id;
        }

        public void setProduction_id(String production_id) {
            this.production_id = production_id;
        }

        public String getProduction_path() {
            return production_path;
        }

        public void setProduction_path(String production_path) {
            this.production_path = production_path;
        }

        public String getProduction_name() {
            return production_name;
        }

        public void setProduction_name(String production_name) {
            this.production_name = production_name;
        }

        public String getProduction_content() {
            return production_content;
        }

        public void setProduction_content(String production_content) {
            this.production_content = production_content;
        }

        public String getClick() {
            return click;
        }

        public void setClick(String click) {
            this.click = click;
        }

        public String getImg_url() {
            return img_url;
        }

        public void setImg_url(String img_url) {
            this.img_url = img_url;
        }

        public String getUser_nicename() {
            return user_nicename;
        }

        public void setUser_nicename(String user_nicename) {
            this.user_nicename = user_nicename;
        }

        public String getAvatar() {
            return avatar;
        }

        public void setAvatar(String avatar) {
            this.avatar = avatar;
        }

        public String getGift_count() {
            return gift_count;
        }

        public void setGift_count(String gift_count) {
            this.gift_count = gift_count;
        }

        public String getShare_count() {
            return share_count;
        }

        public void setShare_count(String share_count) {
            this.share_count = share_count;
        }

        public String getComment_count() {
            return comment_count;
        }

        public void setComment_count(String comment_count) {
            this.comment_count = comment_count;
        }
    }
}
