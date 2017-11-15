package com.zhuye.zhengmeng.user.bean;

import android.net.Uri;

/**
 * Created by lilei on 2017/11/2.
 */

public class GridImgBean {

    private Uri uri;

    public GridImgBean(Uri uri) {
        this.uri = uri;
    }

    public Uri getUri() {
        return uri;
    }

    public void setUri(Uri uri) {
        this.uri = uri;
    }
}
