package com.zhuye.zhengmeng.dynamic.bean;

import java.util.List;

/**
 * Created by hpc on 2017/11/10.
 */

public class FansContributionBean {

    /**
     * data : {"gift_reception_count":"17","gift_reception_list":[{"user_id":"1","gift_id":"13","reception_sum":"1","sum_score":"772","user_nicename":"Yunqiang ","avatar":"/changba/Uploads/Images/2017-10-09/59db24aa83f4d.png"},{"user_id":"7","gift_id":"13","reception_sum":"1","sum_score":"755","user_nicename":"考虑考","avatar":"/changba/Uploads/Images/2017-11-06/5a003e560ae13.png"},{"user_id":"10","gift_id":"14","reception_sum":"1","sum_score":"680","user_nicename":"丘处机","avatar":"/changba/Uploads/Images/2017-11-09/5a03d15538e5f.jpg"},{"user_id":"10","gift_id":"13","reception_sum":"1","sum_score":"604","user_nicename":"丘处机","avatar":"/changba/Uploads/Images/2017-11-09/5a03d15538e5f.jpg"}]}
     * msg :
     * code : 200
     */

    private DataBean data;
    private String msg;
    private int code;

    public DataBean getData() {
        return data;
    }

    public void setData(DataBean data) {
        this.data = data;
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

    public static class DataBean {
        /**
         * gift_reception_count : 17
         * gift_reception_list : [{"user_id":"1","gift_id":"13","reception_sum":"1","sum_score":"772","user_nicename":"Yunqiang ","avatar":"/changba/Uploads/Images/2017-10-09/59db24aa83f4d.png"},{"user_id":"7","gift_id":"13","reception_sum":"1","sum_score":"755","user_nicename":"考虑考","avatar":"/changba/Uploads/Images/2017-11-06/5a003e560ae13.png"},{"user_id":"10","gift_id":"14","reception_sum":"1","sum_score":"680","user_nicename":"丘处机","avatar":"/changba/Uploads/Images/2017-11-09/5a03d15538e5f.jpg"},{"user_id":"10","gift_id":"13","reception_sum":"1","sum_score":"604","user_nicename":"丘处机","avatar":"/changba/Uploads/Images/2017-11-09/5a03d15538e5f.jpg"}]
         */

        private String gift_reception_count;
        private List<GiftReceptionListBean> gift_reception_list;

        public String getGift_reception_count() {
            return gift_reception_count;
        }

        public void setGift_reception_count(String gift_reception_count) {
            this.gift_reception_count = gift_reception_count;
        }

        public List<GiftReceptionListBean> getGift_reception_list() {
            return gift_reception_list;
        }

        public void setGift_reception_list(List<GiftReceptionListBean> gift_reception_list) {
            this.gift_reception_list = gift_reception_list;
        }

        public static class GiftReceptionListBean {
            /**
             * user_id : 1
             * gift_id : 13
             * reception_sum : 1
             * sum_score : 772
             * user_nicename : Yunqiang
             * avatar : /changba/Uploads/Images/2017-10-09/59db24aa83f4d.png
             */

            private String user_id;
            private String gift_id;
            private String reception_sum;
            private String sum_score;
            private String user_nicename;
            private String avatar;

            public String getUser_id() {
                return user_id;
            }

            public void setUser_id(String user_id) {
                this.user_id = user_id;
            }

            public String getGift_id() {
                return gift_id;
            }

            public void setGift_id(String gift_id) {
                this.gift_id = gift_id;
            }

            public String getReception_sum() {
                return reception_sum;
            }

            public void setReception_sum(String reception_sum) {
                this.reception_sum = reception_sum;
            }

            public String getSum_score() {
                return sum_score;
            }

            public void setSum_score(String sum_score) {
                this.sum_score = sum_score;
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
        }
    }
}
