package com.zhuye.zhengmeng.activity.lanuch.viewpagertransforms;

import android.view.View;

/**
 *
 */
public class ZoomInTransform extends TransformAdapter {

    @Override
    public void onRightScorlling(View view, float position) {
        view.setScaleX(1 - position);
        view.setScaleY(1 - position);
    }

}
