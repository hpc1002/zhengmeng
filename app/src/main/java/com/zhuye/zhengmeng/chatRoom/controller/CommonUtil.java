package com.zhuye.zhengmeng.chatRoom.controller;


import com.zhuye.zhengmeng.App;

public class CommonUtil {

    public static int dip2px(float dpValue) {
        float scale = App.getInstance().getResources().getDisplayMetrics().density;
        return (int) (dpValue * scale + 0.5f);
    }
}
