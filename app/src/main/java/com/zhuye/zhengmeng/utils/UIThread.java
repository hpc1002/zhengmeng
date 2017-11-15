package com.zhuye.zhengmeng.utils;

import android.os.Handler;
import android.os.Looper;

public class UIThread {

    private static class LazyHolder {
        private static final UIThread INSTANCE = new UIThread();
    }
    public static UIThread getInstance() {
        return LazyHolder.INSTANCE;
    }
    private final Handler handler;
    private UIThread() {
        this.handler = new Handler(Looper.getMainLooper());
    }

    public void post(Runnable runnable) {
        handler.post(runnable);
    }

    public void postDelay(Runnable runnable, long delay) {
        handler.postDelayed(runnable, delay);
    }
}
