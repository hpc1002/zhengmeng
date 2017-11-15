package com.zhuye.zhengmeng.home.bean;

import java.util.ArrayList;

/**
 * Created by hpc on 2017/11/3.
 */

/**
 * {
 * "data": [
 * {
 * "chatroom_id": "49",
 * "chatroom_name": "123456",
 * "chatroom_time": "1970-01-01 08:00:49",
 * "user_nicename": "Yunqiang ",
 * "avatar": "angba/Uploads/Images/2017-10-09/59db24aa83f4d.png"
 * },
 * {
 * "chatroom_id": "50",
 * "chatroom_name": "147258",
 * "chatroom_time": "1970-01-01 08:00:50",
 * "user_nicename": "Yunqiang ",
 * "avatar": "angba/Uploads/Images/2017-10-09/59db24aa83f4d.png"
 * },
 * {
 * "chatroom_id": "51",
 * "chatroom_name": "369789",
 * "chatroom_time": "1970-01-01 08:00:51",
 * "user_nicename": "Yunqiang ",
 * "avatar": "angba/Uploads/Images/2017-10-09/59db24aa83f4d.png"
 * },
 * {
 * "chatroom_id": "52",
 * "chatroom_name": "123147",
 * "chatroom_time": "1970-01-01 08:00:52",
 * "user_nicename": "Yunqiang ",
 * "avatar": "angba/Uploads/Images/2017-10-09/59db24aa83f4d.png"
 * }
 * ],
 * "msg": "",
 * "code": 200
 * }
 * P
 */
public class KTVListBean {
    public String msg;
    public int code;
    public ArrayList<Data> data;
    public class Data {
        public String chatroom_id;
        public String chatroom_name;
        public String chatroom_time;
        public String user_nicename;
        public String avatar;
    }

}
