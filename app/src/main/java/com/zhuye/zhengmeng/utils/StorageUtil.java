package com.zhuye.zhengmeng.utils;

import android.content.Context;
import android.content.SharedPreferences;

import org.json.JSONArray;
import org.json.JSONException;

import java.util.Map;
import java.util.Set;

public class StorageUtil {
    private static final String PREF_NAME = "cloudwater";

    private static SharedPreferences sp;

    private static SharedPreferences getPreferences(Context context) {
        return context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
    }

    private static SharedPreferences.Editor getEditor(Context context) {
        return getPreferences(context).edit();
    }

    public static void setKeyValue(Context context, String key, String value) {
        getEditor(context).putString(key, value).commit();
    }

    public static String getValue(Context context, String key) {
        return getPreferences(context).getString(key, "");
    }

    public static void putBoolean(Context ctx, String key, boolean value) {
        if (sp == null) {
            sp = ctx.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
        }

        sp.edit().putBoolean(key, value).commit();
    }

    public static boolean getBoolean(Context ctx, String key, boolean defValue) {
        if (sp == null) {
            sp = ctx.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
        }
        return sp.getBoolean(key, defValue);
    }

    public static void remove(Context context, String key) {
        getEditor(context).remove(key).commit();
    }

    public static void clear(Context context, String prefixes) throws JSONException {
        Map<String, ?> all = getPreferences(context).getAll();
        Set<String> keys = all.keySet();
        JSONArray jsonArray = new JSONArray(prefixes);
        int length = jsonArray.length();
        for (String key : keys) {
            boolean exist = false;
            for (int i = 0; i < length; i++) {
                String prefix = jsonArray.getString(i);
                if (key.startsWith(prefix)) {
                    exist = true;
                }
            }
            if (!exist) {
                remove(context, key);
            }
        }
    }


    public static String getTokenId(Context context) {
        return StorageUtil.getValue(context, "token");
    }

    public static String getUserId(Context context) {
        return StorageUtil.getValue(context, "userId");
    }
    public static String getUserType(Context context) {
        return StorageUtil.getValue(context, "userType");
    }
    public static String getCourseType(Context context) {
        return StorageUtil.getValue(context, "course_type");
    }

    public static boolean deteteUserPhone(Context context) {
        return getEditor(context).remove("userPhone").commit();
    }

    public static boolean deteteUserId(Context context) {
        return getEditor(context).remove("userId").commit();
    }
}
