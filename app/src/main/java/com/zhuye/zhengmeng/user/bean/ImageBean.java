package com.zhuye.zhengmeng.user.bean;

import java.io.Serializable;
import java.util.List;

/**
 * Created by zzzy on 2017/11/2.
 */

public class ImageBean implements Serializable{

    /**
     * data : [{"photo_path":"图片路径","photo_id":"图片id","is_pic":"是否头像"},{"photo_path":"图片路径","photo_id":"图片id","is_pic":"是否头像"}]
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
         * photo_path : 图片路径
         * photo_id : 图片id
         * is_pic : 是否头像
         */

        private String photo_path;
        private String photo_id;
        private String is_pic;

        public String getPhoto_path() {
            return photo_path;
        }

        public void setPhoto_path(String photo_path) {
            this.photo_path = photo_path;
        }

        public String getPhoto_id() {
            return photo_id;
        }

        public void setPhoto_id(String photo_id) {
            this.photo_id = photo_id;
        }

        public String getIs_pic() {
            return is_pic;
        }

        public void setIs_pic(String is_pic) {
            this.is_pic = is_pic;
        }
    }
}
