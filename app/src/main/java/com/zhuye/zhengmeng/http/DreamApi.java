package com.zhuye.zhengmeng.http;

import android.app.Activity;
import android.util.Log;

import com.lzy.okgo.OkGo;
import com.lzy.okgo.callback.StringCallback;
import com.lzy.okgo.model.Progress;
import com.lzy.okgo.model.Response;
import com.zhuye.zhengmeng.App;
import com.zhuye.zhengmeng.Constant;
import com.zhuye.zhengmeng.http.callback.StringDialogCallback;

import java.io.File;

import okhttp3.Headers;


/**
 * 网络请求
 * Created by hpc on 2017/7/6.
 */

public class DreamApi {

    private static final String TAG = "DreamApi";

    //获取验证码
    public static void getSmsCode(Activity activity, final int what, String phone, final MyCallBack myCallBack) {
        OkGo.<String>get(Constant.GET_SMSCODE_URL)
                .tag(App.getInstance())
                .params("phone", phone)
                .execute(new StringDialogCallback(activity, "请求验证码...") {
                    @Override
                    public void onSuccess(Response<String> response) {
                        myCallBack.onSuccess(what, response);
                    }

                    @Override
                    public void onError(Response<String> response) {
                        super.onError(response);
                        myCallBack.onFail(what, response);
                    }
                });
    }

    //注册
    public static void register(Activity activity, final int what, String mobile, String password, String verify, final MyCallBack myCallBack) {
        OkGo.<String>post(Constant.REGISTER_URL)
                .tag(App.getInstance())
                .params("mobile", mobile)
                .params("password", password)
                .params("verify", verify)
                .execute(new StringDialogCallback(activity, "请求注册...") {
                    @Override
                    public void onSuccess(Response<String> response) {
                        myCallBack.onSuccess(what, response);
                    }

                    @Override
                    public void onError(Response<String> response) {
                        super.onError(response);
                        myCallBack.onFail(what, response);
                    }
                });
    }

    //登录接口
    public static void login(Activity context, final int what, String type, String mobile,
                             String password, String openid, String avatar, String sex,
                             String user_nicename, final MyCallBack myCallBack) {
        OkGo.<String>post(Constant.LOGIN_URL)
                .tag(App.getInstance())
                .params("type", type)
                .params("mobile", mobile)
                .params("password", password)
                .params("openid", openid)
                .params("avatar", avatar)
                .params("sex", sex)
                .params("user_nicename", user_nicename)
                .execute(new StringDialogCallback(context, "登录中...") {
                    @Override
                    public void onSuccess(Response<String> response) {
                        Headers headers = response.headers();
                        myCallBack.onSuccess(what, response);
                    }

                    @Override
                    public void onError(Response<String> response) {
                        super.onError(response);
                        myCallBack.onFail(what, response);
                    }
                });
    }

    //验证验证码接口
    public static void verificCode(final int what, String mobile,
                                   String verify, final MyCallBack myCallBack) {
        OkGo.<String>post(Constant.VERIFIC_CODE_URL)
                .tag(App.getInstance())
                .params("mobile", mobile)
                .params("verify", verify)
                .execute(new StringCallback() {
                    @Override
                    public void onSuccess(Response<String> response) {
                        myCallBack.onSuccess(what, response);
                    }

                    @Override
                    public void onError(Response<String> response) {
                        super.onError(response);
                        myCallBack.onFail(what, response);
                    }
                });
    }

    //找回密码接口
    public static void changePassword(Activity context, final int what, String mobile,
                                      String verify, String password, final MyCallBack myCallBack) {
        OkGo.<String>post(Constant.CHANGE_PASSWORD_URL)
                .tag(App.getInstance())
                .params("mobile", mobile)
                .params("verify", verify)
                .params("password", password)
                .execute(new StringDialogCallback(context, "正在修改密码...") {
                    @Override
                    public void onSuccess(Response<String> response) {
                        myCallBack.onSuccess(what, response);
                    }

                    @Override
                    public void onError(Response<String> response) {
                        super.onError(response);
                        myCallBack.onFail(what, response);
                    }
                });
    }

    //获取收藏列表
    public static void getCollectList(Activity context, final int what, String token,
                                      int page, final MyCallBack myCallBack) {
        OkGo.<String>post(Constant.COLLECT_LIST_URL)
                .tag(App.getInstance())
                .params("token", token)
                .params("page", page)
                .execute(new StringDialogCallback(context, "数据加载中...") {
                    @Override
                    public void onSuccess(Response<String> response) {
                        myCallBack.onSuccess(what, response);
                    }

                    @Override
                    public void onError(Response<String> response) {
                        myCallBack.onFail(what, response);
                    }

                    @Override
                    public void onFinish() {
                        super.onFinish();
                        myCallBack.onFinish(what);
                    }
                });
    }

    //用户动态列表
    public static void conditionList(Activity context, final int what, String token,
                                     String sum_number, String user_id, final MyCallBack myCallBack) {
        OkGo.<String>post(Constant.CONDITION_LIST_URL)
                .tag(App.getInstance())
                .params("token", token)
                .params("sum_number", sum_number)
                .params("user_id", user_id)
                .execute(new StringDialogCallback(context, "获取消息...") {
                    @Override
                    public void onSuccess(Response<String> response) {
                        myCallBack.onSuccess(what, response);
                    }

                    @Override
                    public void onError(Response<String> response) {
                        super.onError(response);
                        myCallBack.onFail(what, response);
                    }
                });
    }

    //用户动态列表
    public static void conditionListNorefresh(final int what, String token,
                                              String sum_number, String user_id, final MyCallBack myCallBack) {
        OkGo.<String>post(Constant.CONDITION_LIST_URL)
                .tag(App.getInstance())
                .params("token", token)
                .params("sum_number", sum_number)
                .params("user_id", user_id)
                .execute(new StringCallback() {
                    @Override
                    public void onSuccess(Response<String> response) {
                        myCallBack.onSuccess(what, response);
                    }

                    @Override
                    public void onError(Response<String> response) {
                        super.onError(response);
                        myCallBack.onFail(what, response);
                    }

                    @Override
                    public void onFinish() {
                        super.onFinish();
                        myCallBack.onFinish(what);
                    }
                });
    }

    //上榜记录
    public static void getRankList(final int what, String token,
                                   String user_id, final MyCallBack myCallBack) {
        OkGo.<String>post(Constant.RANKING_LIST_URL)
                .tag(App.getInstance())
                .params("token", token)
                .params("user_id", user_id)
                .execute(new StringCallback() {
                    @Override
                    public void onSuccess(Response<String> response) {
                        myCallBack.onSuccess(what, response);
                    }

                    @Override
                    public void onError(Response<String> response) {
                        super.onError(response);
                        myCallBack.onFail(what, response);
                    }

                    @Override
                    public void onFinish() {
                        super.onFinish();
                        myCallBack.onFinish(what);
                    }
                });
    }

    //获取用户信息
    public static void getUserInfo(final int what, String token, final MyCallBack myCallBack) {
        OkGo.<String>post(Constant.USERINFO_URL)
                .tag(App.getInstance())
                .params("token", token)
                .execute(new StringCallback() {
                    @Override
                    public void onSuccess(Response<String> response) {
                        myCallBack.onSuccess(what, response);
                    }

                    @Override
                    public void onError(Response<String> response) {
                        super.onError(response);
                        myCallBack.onFail(what, response);
                    }
                });
    }

    //修改用户信息
    public static void changeUserInfo(Activity activity, final int what, String token, String type, String str, final MyCallBack myCallBack) {
        OkGo.<String>post(Constant.CHANGE_USERINFO_URL)
                .tag(App.getInstance())
                .params("token", token)
                .params("type", type)
                .params("str", str)
                .execute(new StringDialogCallback(activity, "上传中...") {
                    @Override
                    public void onSuccess(Response<String> response) {
                        myCallBack.onSuccess(what, response);
                    }

                    @Override
                    public void onError(Response<String> response) {
                        super.onError(response);
                        myCallBack.onFail(what, response);
                    }
                });
    }

    //签到
    public static void sign(final int what, String token, final MyCallBack myCallBack) {
        OkGo.<String>post(Constant.SIGN_URL)
                .tag(App.getInstance())
                .params("token", token)
                .execute(new StringCallback() {
                    @Override
                    public void onSuccess(Response<String> response) {
                        myCallBack.onSuccess(what, response);
                    }

                    @Override
                    public void onError(Response<String> response) {
                        super.onError(response);
                        myCallBack.onFail(what, response);
                    }
                });
    }

    //粉丝列表
    public static void getFansList(Activity activity, final int what, String token, String user_id, int page, final MyCallBack myCallBack) {
        OkGo.<String>post(Constant.FANS_LIST_URL)
                .tag(App.getInstance())
                .params("token", token)
                .params("user_id", user_id)
                .params("page", page)
                .execute(new StringDialogCallback(activity, "页面加载中...") {
                    @Override
                    public void onSuccess(Response<String> response) {
//                        Headers headers = response.headers();
//                        String rawCookies = headers.get("Set-Cookie");
//                        Log.i(TAG, "onSuccess:rawCookies " + rawCookies);
                        myCallBack.onSuccess(what, response);
                    }

                    @Override
                    public void onError(Response<String> response) {
                        super.onError(response);
                        myCallBack.onFail(what, response);
                    }

                    @Override
                    public void onFinish() {
                        super.onFinish();
                        myCallBack.onFinish(what);
                    }
                });
    }

    //商城web url获取
    public static void getShopWebUrl(final int what, String token, final MyCallBack myCallBack) {
        OkGo.<String>post(Constant.SHOP_WEB_URL)
                .tag(App.getInstance())
                .params("token", token)
                .execute(new StringCallback() {
                    @Override
                    public void onSuccess(Response<String> response) {
                        myCallBack.onSuccess(what, response);
                    }

                    @Override
                    public void onError(Response<String> response) {
                        super.onError(response);
                        myCallBack.onFail(what, response);
                    }
                });
    }

    //分享列表
    public static void getShareListUrl(Activity activity, final int what, String token, int page, final MyCallBack myCallBack) {
        OkGo.<String>post(Constant.SHARE_LIST_URL)
                .tag(App.getInstance())
                .params("token", token)
                .params("page", page)
                .execute(new StringDialogCallback(activity, "列表加载中...") {
                    @Override
                    public void onSuccess(Response<String> response) {
                        myCallBack.onSuccess(what, response);
                    }

                    @Override
                    public void onError(Response<String> response) {
                        super.onError(response);
                        myCallBack.onFail(what, response);
                    }

                    @Override
                    public void onFinish() {
                        super.onFinish();
                        myCallBack.onFinish(what);
                    }
                });
    }

    //金币列表
    public static void getGoldList(final int what, Activity activity, String token, final MyCallBack myCallBack) {
        OkGo.<String>post(Constant.GOLD_LIST_URL)
                .tag(App.getInstance())
                .params("token", token)
                .execute(new StringDialogCallback(activity, "数据加载中...") {
                    @Override
                    public void onSuccess(Response<String> response) {
                        myCallBack.onSuccess(what, response);
                    }

                    @Override
                    public void onError(Response<String> response) {
                        super.onError(response);
                        myCallBack.onFail(what, response);
                    }
                });
    }

    //活动公告
    public static void getMsgList(final int what, Activity activity, String token, final MyCallBack myCallBack) {
        OkGo.<String>post(Constant.MSG_LIST_URL)
                .tag(App.getInstance())
                .params("token", token)
                .execute(new StringDialogCallback(activity, "公告加载中...") {
                    @Override
                    public void onSuccess(Response<String> response) {
                        myCallBack.onSuccess(what, response);
                    }

                    @Override
                    public void onError(Response<String> response) {
                        super.onError(response);
                        myCallBack.onFail(what, response);
                    }
                });
    }

    //送出的礼物列表
    public static void getSendGiftList(final int what, String token, int page, final MyCallBack myCallBack) {
        OkGo.<String>post(Constant.SEND_LIST_URL)
                .tag(App.getInstance())
                .params("token", token)
                .params("page", page)
                .execute(new StringCallback() {
                    @Override
                    public void onSuccess(Response<String> response) {
                        myCallBack.onSuccess(what, response);
                    }

                    @Override
                    public void onError(Response<String> response) {
                        super.onError(response);
                        myCallBack.onFail(what, response);
                    }

                    @Override
                    public void onFinish() {
                        super.onFinish();
                        myCallBack.onFinish(what);
                    }
                });
    }

    //收到的礼物列表
    public static void getReceiveGiftList(final int what, Activity activity, String token, int page, final MyCallBack myCallBack) {
        OkGo.<String>post(Constant.RECEIVE_LIST_URL)
                .tag(App.getInstance())
                .params("token", token)
                .params("page", page)
                .execute(new StringDialogCallback(activity, "数据加载中...") {
                    @Override
                    public void onSuccess(Response<String> response) {
                        myCallBack.onSuccess(what, response);
                    }

                    @Override
                    public void onError(Response<String> response) {
                        super.onError(response);
                        myCallBack.onFail(what, response);
                    }

                    @Override
                    public void onFinish() {
                        super.onFinish();
                        myCallBack.onFinish(what);
                    }
                });
    }

    //歌曲列表
    public static void getSongsList(final int what, String token, int page,
                                    String name, int apartment_id, final MyCallBack myCallBack) {
        OkGo.<String>post(Constant.SONG_LIST_URL)
                .tag(App.getInstance())
                .params("token", token)
                .params("page", page)
                .params("name", name)
                .params("apartment_id", apartment_id)
                .execute(new StringCallback() {
                    @Override
                    public void onSuccess(Response<String> response) {
                        myCallBack.onSuccess(what, response);
                    }

                    @Override
                    public void onError(Response<String> response) {
                        super.onError(response);
                        myCallBack.onFail(what, response);
                    }

                    @Override
                    public void onFinish() {
                        super.onFinish();
                        myCallBack.onFinish(what);
                    }
                });
    }

    //评论列表
    public static void getCommentList(final int what, String token, int page,
                                      String production_id, final MyCallBack myCallBack) {
        OkGo.<String>post(Constant.CONTENT_LIST_URL)
                .tag(App.getInstance())
                .params("token", token)
                .params("page", page)
                .params("production_id", production_id)
                .execute(new StringCallback() {
                    @Override
                    public void onSuccess(Response<String> response) {
                        myCallBack.onSuccess(what, response);
                    }

                    @Override
                    public void onError(Response<String> response) {
                        super.onError(response);
                        myCallBack.onFail(what, response);
                    }

                    @Override
                    public void onFinish() {
                        super.onFinish();
                        myCallBack.onFinish(what);
                    }
                });
    }

    //二级评论列表
    public static void getSecondCommentList(final int what, String token, int page,
                                            String production_id, String receive_user_id,
                                            final MyCallBack myCallBack) {
        OkGo.<String>post(Constant.ERJI_COMMENT_LIST_URL)
                .tag(App.getInstance())
                .params("token", token)
                .params("page", page)
                .params("production_id", production_id)
                .params("receive_user_id", receive_user_id)
                .execute(new StringCallback() {
                    @Override
                    public void onSuccess(Response<String> response) {
                        myCallBack.onSuccess(what, response);
                    }

                    @Override
                    public void onError(Response<String> response) {
                        super.onError(response);
                        myCallBack.onFail(what, response);
                    }

                    @Override
                    public void onFinish() {
                        super.onFinish();
                        myCallBack.onFinish(what);
                    }
                });
    }

    //获取头像列表
    public static void getAvatorList(Activity activity, final int what, String token, final MyCallBack myCallBack) {
        OkGo.<String>post(Constant.AVATOR_LIST_URL)
                .tag(App.getInstance())
                .params("token", token)
                .execute(new StringDialogCallback(activity, "头像加载中...") {
                    @Override
                    public void onSuccess(Response<String> response) {
                        myCallBack.onSuccess(what, response);
                    }

                    @Override
                    public void onError(Response<String> response) {
                        super.onError(response);
                        myCallBack.onFail(what, response);
                    }
                });
    }

    //获取头像列表没有Dialog
    public static void getAvatorListNoDialog(final int what, String token, final MyCallBack myCallBack) {
        OkGo.<String>post(Constant.AVATOR_LIST_URL)
                .tag(App.getInstance())
                .params("token", token)
                .execute(new StringCallback() {
                    @Override
                    public void onSuccess(Response<String> response) {
                        myCallBack.onSuccess(what, response);
                    }

                    @Override
                    public void onError(Response<String> response) {
                        super.onError(response);
                        myCallBack.onFail(what, response);
                    }
                });
    }

    //上传头像
    public static void uploadAvator(Activity activity, final int what, String token, File file, final UploadCallBack myCallBack) {
        OkGo.<String>post(Constant.UPLOAD_AVATOR_URL)
                .tag(App.getInstance())
                .params("token", token)
                .params("file", file)
                .execute(new StringDialogCallback(activity, "头像上传中...") {
                    @Override
                    public void onSuccess(Response<String> response) {
                        myCallBack.onSuccess(what, response);
                    }

                    @Override
                    public void uploadProgress(Progress progress) {
                        myCallBack.uploadProgress(progress);
                    }
                });
    }

    //更换用户头像
    public static void changeAvatar(final int what, String token, String photo_id, final MyCallBack myCallBack) {
        OkGo.<String>post(Constant.CHANGE_AVATOR_URL)
                .tag(App.getInstance())
                .params("token", token)
                .params("photo_id", photo_id)
                .execute(new StringCallback() {
                    @Override
                    public void onSuccess(Response<String> response) {
                        myCallBack.onSuccess(what, response);
                    }

                    @Override
                    public void onError(Response<String> response) {
                        super.onError(response);
                        myCallBack.onFail(what, response);
                    }
                });
    }

    //获取ktv房间列表
    public static void getKtvRoomList(final int what, String token, int page, String chatroom_name, final MyCallBack myCallBack) {
        OkGo.<String>post(Constant.KTV_ROOM_LIST_URL)
                .tag(App.getInstance())
                .params("token", token)
                .params("page", page)
                .params("chatroom_name", chatroom_name)
                .execute(new StringCallback() {
                    @Override
                    public void onSuccess(Response<String> response) {
                        myCallBack.onSuccess(what, response);
                    }

                    @Override
                    public void onError(Response<String> response) {
                        super.onError(response);
                        myCallBack.onFail(what, response);
                    }

                    @Override
                    public void onFinish() {
                        super.onFinish();
                        myCallBack.onFinish(what);
                    }
                });
    }

    //获取平台歌曲列表
    public static void getCompetitionList(final int what, String token,
                                          String city, int page, final MyCallBack myCallBack) {
        OkGo.<String>post(Constant.COMPETITION_LIST_URL)
                .tag(App.getInstance())
                .params("token", token)
                .params("city", city)
                .params("page", page)
                .execute(new StringCallback() {
                    @Override
                    public void onSuccess(Response<String> response) {
                        myCallBack.onSuccess(what, response);
                    }

                    @Override
                    public void onError(Response<String> response) {
                        super.onError(response);
                        myCallBack.onFail(what, response);
                    }

                    @Override
                    public void onFinish() {
                        super.onFinish();
                        myCallBack.onFinish(what);
                    }
                });
    }

    //获取动态详情
    public static void getDynamicDetail(final int what, String token, String production_id, final MyCallBack myCallBack) {
        OkGo.<String>post(Constant.DYNAMIC_DETAIL_URL)
                .tag(App.getInstance())
                .params("token", token)
                .params("production_id", production_id)
                .execute(new StringCallback() {
                    @Override
                    public void onSuccess(Response<String> response) {
                        myCallBack.onSuccess(what, response);
                    }

                    @Override
                    public void onError(Response<String> response) {
                        super.onError(response);
                        myCallBack.onFail(what, response);
                    }
                });
    }

    //创建聊天室
    public static void createChatRoom(final int what, Activity activity, String token, String chatroom_name, final MyCallBack myCallBack) {
        OkGo.<String>post(Constant.CREATE_CHATROOM_URL)
                .tag(App.getInstance())
                .params("token", token)
                .params("chatroom_name", chatroom_name)
                .execute(new StringDialogCallback(activity, "创建中...") {
                    @Override
                    public void onSuccess(Response<String> response) {
                        myCallBack.onSuccess(what, response);
                    }

                    @Override
                    public void onError(Response<String> response) {
                        super.onError(response);
                        myCallBack.onFail(what, response);
                    }
                });
    }

    //帮助与反馈
    public static void helpAndFeedBack(final int what, Activity activity, String token, final MyCallBack myCallBack) {
        OkGo.<String>post(Constant.HELP_URL)
                .tag(App.getInstance())
                .params("token", token)
                .execute(new StringDialogCallback(activity, "加载中...") {
                    @Override
                    public void onSuccess(Response<String> response) {
                        myCallBack.onSuccess(what, response);
                    }

                    @Override
                    public void onError(Response<String> response) {
                        super.onError(response);
                        myCallBack.onFail(what, response);
                    }
                });
    }

    //关于我们
    public static void aboutUs(final int what, Activity activity, String token, final MyCallBack myCallBack) {
        OkGo.<String>post(Constant.ABOUTUS_URL)
                .tag(App.getInstance())
                .params("token", token)
                .execute(new StringDialogCallback(activity, "加载中...") {
                    @Override
                    public void onSuccess(Response<String> response) {
                        myCallBack.onSuccess(what, response);
                    }

                    @Override
                    public void onError(Response<String> response) {
                        super.onError(response);
                        myCallBack.onFail(what, response);
                    }
                });
    }

    //官方公告
    public static void activityBucketin(final int what, Activity activity, String token, final MyCallBack myCallBack) {
        OkGo.<String>post(Constant.ACTIVITY_BULLETIN_URL)
                .tag(App.getInstance())
                .params("token", token)
                .execute(new StringDialogCallback(activity, "加载中...") {
                    @Override
                    public void onSuccess(Response<String> response) {
                        myCallBack.onSuccess(what, response);
                    }

                    @Override
                    public void onError(Response<String> response) {
                        super.onError(response);
                        myCallBack.onFail(what, response);
                    }
                });
    }

    //动态点赞
    public static void dianZan(final int what, String token, String comment_id,
                               final MyCallBack myCallBack) {
        OkGo.<String>post(Constant.CLICK_ZAN_URL)
                .tag(App.getInstance())
                .params("token", token)
                .params("comment_id", comment_id)
                .execute(new StringCallback() {
                    @Override
                    public void onSuccess(Response<String> response) {
                        myCallBack.onSuccess(what, response);
                    }

                    @Override
                    public void onError(Response<String> response) {
                        super.onError(response);
                        myCallBack.onFail(what, response);
                    }
                });
    }

    //榜单列表
    public static void getBangDanList(final int what, String token,String city,
                                      final MyCallBack myCallBack) {
        OkGo.<String>post(Constant.BANGDAN_LIST_URL)
                .tag(App.getInstance())
                .params("token", token)
                .params("city", city)
                .execute(new StringCallback() {
                    @Override
                    public void onSuccess(Response<String> response) {
                        myCallBack.onSuccess(what, response);
                    }

                    @Override
                    public void onError(Response<String> response) {
                        super.onError(response);
                        myCallBack.onFail(what, response);
                    }
                });
    }

    //我的关注
    public static void getAttentionList(final int what, String token, int page, String user_id,
                                        final MyCallBack myCallBack) {
        OkGo.<String>post(Constant.ATTENTION_LIST_URL)
                .tag(App.getInstance())
                .params("token", token)
                .params("user_id", user_id)
                .params("page", page)
                .execute(new StringCallback() {
                    @Override
                    public void onSuccess(Response<String> response) {
                        myCallBack.onSuccess(what, response);
                    }

                    @Override
                    public void onError(Response<String> response) {
                        super.onError(response);
                        myCallBack.onFail(what, response);
                    }

                    @Override
                    public void onFinish() {
                        super.onFinish();
                        myCallBack.onFinish(what);
                    }
                });
    }

    //比赛榜单
    public static void getBangdanList(final int what, String token,
                                      final MyCallBack myCallBack) {
        OkGo.<String>post(Constant.BANGDAN_RANK_LIST_URL)
                .tag(App.getInstance())
                .params("token", token)
                .execute(new StringCallback() {
                    @Override
                    public void onSuccess(Response<String> response) {
                        myCallBack.onSuccess(what, response);
                    }

                    @Override
                    public void onError(Response<String> response) {
                        super.onError(response);
                        myCallBack.onFail(what, response);
                    }

                    @Override
                    public void onFinish() {
                        super.onFinish();
                        myCallBack.onFinish(what);
                    }
                });
    }

    //比赛榜单
    public static void getBankCardList(final int what, String token,
                                       final MyCallBack myCallBack) {
        OkGo.<String>post(Constant.BANKCARD_LIST_URL)
                .tag(App.getInstance())
                .params("token", token)
                .execute(new StringCallback() {
                    @Override
                    public void onSuccess(Response<String> response) {
                        myCallBack.onSuccess(what, response);
                    }

                    @Override
                    public void onError(Response<String> response) {
                        super.onError(response);
                        myCallBack.onFail(what, response);
                    }

                    @Override
                    public void onFinish() {
                        super.onFinish();
                        myCallBack.onFinish(what);
                    }
                });
    }

    //上传视频 平台比赛
    public static void uploadAudio(Activity activity, final int what, String token, File file, String song_id, String production_content,
                                   String type, String song_type, String activity_id, String city, final UploadCallBack myCallBack) {
        OkGo.<String>post(Constant.RECORD_URL)
                .tag(App.getInstance())
                .params("token", token)
                .params("file", file)
                .params("song_id", song_id)
                .params("production_content", production_content)
                .params("type", type)
                .params("song_type", song_type)
                .params("activity_id", activity_id)
                .params("city", city)
                .execute(new StringDialogCallback(activity, "上传中...") {
                    @Override
                    public void onSuccess(Response<String> response) {
                        myCallBack.onSuccess(what, response);
                    }

                    @Override
                    public void uploadProgress(Progress progress) {
                        myCallBack.uploadProgress(progress);
                    }
                });
    }

    //添加或取消关注
    public static void followOrNot(final int what, String token, String by_user_id, final MyCallBack myCallBack) {
        OkGo.<String>post(Constant.FOLLOW_OR_NOT_URL)
                .tag(App.getInstance())
                .params("token", token)
                .params("by_user_id", by_user_id)
                .execute(new StringCallback() {
                    @Override
                    public void onSuccess(Response<String> response) {
                        myCallBack.onSuccess(what, response);
                    }

                    @Override
                    public void onError(Response<String> response) {
                        super.onError(response);
                        myCallBack.onFail(what, response);
                    }
                });
    }

    //获取粉丝数量
    public static void getFansNumber(final int what, String token, String production_id, final MyCallBack myCallBack) {
        OkGo.<String>post(Constant.FANS_NUMBER_URL)
                .tag(App.getInstance())
                .params("token", token)
                .params("production_id", production_id)
                .execute(new StringCallback() {
                    @Override
                    public void onSuccess(Response<String> response) {
                        myCallBack.onSuccess(what, response);
                    }

                    @Override
                    public void onError(Response<String> response) {
                        super.onError(response);
                        myCallBack.onFail(what, response);
                    }
                });
    }

    //获取粉丝贡献榜
    public static void getFansContribution(final int what, String token, String production_id, final MyCallBack myCallBack) {
        OkGo.<String>post(Constant.FANS_CONTRIBUTION_RANK_URL)
                .tag(App.getInstance())
                .params("token", token)
                .params("production_id", production_id)
                .execute(new StringCallback() {
                    @Override
                    public void onSuccess(Response<String> response) {
                        myCallBack.onSuccess(what, response);
                    }

                    @Override
                    public void onError(Response<String> response) {
                        super.onError(response);
                        myCallBack.onFail(what, response);
                    }
                });
    }

    //获取粉丝贡献榜
    public static void addComment(final int what, String token, String production_id,
                                  String receive_user_id, String content, final MyCallBack myCallBack) {
        OkGo.<String>post(Constant.ADD_COMMENT_URL)
                .tag(App.getInstance())
                .params("token", token)
                .params("production_id", production_id)
                .params("receive_user_id", receive_user_id)
                .params("content", content)
                .execute(new StringCallback() {
                    @Override
                    public void onSuccess(Response<String> response) {
                        myCallBack.onSuccess(what, response);
                    }

                    @Override
                    public void onError(Response<String> response) {
                        super.onError(response);
                        myCallBack.onFail(what, response);
                    }
                });
    }

    //获取粉丝贡献榜
    public static void enterChatRoom(final int what, String token,
                                     String chatroom_id, final MyCallBack myCallBack) {
        OkGo.<String>post(Constant.ENTER_CHATROOM_URL)
                .tag(App.getInstance())
                .params("token", token)
                .params("chatroom_id", chatroom_id)
                .execute(new StringCallback() {
                    @Override
                    public void onSuccess(Response<String> response) {
                        myCallBack.onSuccess(what, response);
                    }

                    @Override
                    public void onError(Response<String> response) {
                        super.onError(response);
                        myCallBack.onFail(what, response);
                    }
                });
    }

    //送礼物web
    public static void sendGift(final int what, String token,
                                String production_id, final MyCallBack myCallBack) {
        OkGo.<String>post(Constant.SENDGIFT_URL)
                .tag(App.getInstance())
                .params("token", token)
                .params("production_id", production_id)
                .execute(new StringCallback() {
                    @Override
                    public void onSuccess(Response<String> response) {
                        myCallBack.onSuccess(what, response);
                    }

                    @Override
                    public void onError(Response<String> response) {
                        super.onError(response);
                        myCallBack.onFail(what, response);
                    }
                });
    }

    //排麦列表
    public static void getPaiMaiList(final int what, String token,
                                     String apartment_id, final MyCallBack myCallBack) {
        OkGo.<String>post(Constant.PAIMAI_LIST_URL)
                .tag(App.getInstance())
                .params("token", token)
                .params("apartment_id", apartment_id)
                .execute(new StringCallback() {
                    @Override
                    public void onSuccess(Response<String> response) {
                        myCallBack.onSuccess(what, response);
                    }

                    @Override
                    public void onError(Response<String> response) {
                        super.onError(response);
                        myCallBack.onFail(what, response);
                    }

                    @Override
                    public void onFinish() {
                        super.onFinish();
                        myCallBack.onFinish(what);
                    }
                });
    }

    //点歌
    public static void chooseSong(final int what, String token,
                                  String apartment_id, String song_id, final MyCallBack myCallBack) {
        OkGo.<String>post(Constant.CHOOSE_SONG_URL)
                .tag(App.getInstance())
                .params("token", token)
                .params("apartment_id", apartment_id)
                .params("song_id", song_id)
                .execute(new StringCallback() {
                    @Override
                    public void onSuccess(Response<String> response) {
                        myCallBack.onSuccess(what, response);
                    }

                    @Override
                    public void onError(Response<String> response) {
                        super.onError(response);
                        myCallBack.onFail(what, response);
                    }

                    @Override
                    public void onFinish() {
                        super.onFinish();
                        myCallBack.onFinish(what);
                    }
                });
    }

    //添加银行卡
    public static void addBankCard(final int what, String token,
                                   String bank_card_numbers, String bank_card_name,
                                   String bank_card_user_name, final MyCallBack myCallBack) {
        OkGo.<String>post(Constant.ADD_BANK_CARD_URL)
                .tag(App.getInstance())
                .params("token", token)
                .params("bank_card_numbers", bank_card_numbers)
                .params("bank_card_name", bank_card_name)
                .params("bank_card_user_name", bank_card_user_name)
                .execute(new StringCallback() {
                    @Override
                    public void onSuccess(Response<String> response) {
                        myCallBack.onSuccess(what, response);
                    }

                    @Override
                    public void onError(Response<String> response) {
                        super.onError(response);
                        myCallBack.onFail(what, response);
                    }

                    @Override
                    public void onFinish() {
                        super.onFinish();
                        myCallBack.onFinish(what);
                    }
                });
    }

    //删除银行卡
    public static void deleteBankCard(final int what, String token,
                                      int bank_card_id, final MyCallBack myCallBack) {
        OkGo.<String>post(Constant.DELETE_BANK_CARD_URL)
                .tag(App.getInstance())
                .params("token", token)
                .params("bank_card_id", bank_card_id)
                .execute(new StringCallback() {
                    @Override
                    public void onSuccess(Response<String> response) {
                        myCallBack.onSuccess(what, response);
                    }

                    @Override
                    public void onError(Response<String> response) {
                        super.onError(response);
                        myCallBack.onFail(what, response);
                    }

                    @Override
                    public void onFinish() {
                        super.onFinish();
                        myCallBack.onFinish(what);
                    }
                });
    }

    //获取下首歌曲播放文件
    public static void getNextSongPlay(final int what, String token,
                                       String apartment_id, final MyCallBack myCallBack) {
        OkGo.<String>post(Constant.NEXT_SONG_PLAY_URL)
                .tag(App.getInstance())
                .params("token", token)
                .params("apartment_id", apartment_id)
                .execute(new StringCallback() {
                    @Override
                    public void onSuccess(Response<String> response) {
                        myCallBack.onSuccess(what, response);
                    }

                    @Override
                    public void onError(Response<String> response) {
                        super.onError(response);
                        myCallBack.onFail(what, response);
                    }

                    @Override
                    public void onFinish() {
                        super.onFinish();
                        myCallBack.onFinish(what);
                    }
                });
    }
    //房间是否有人在唱
    public static void isSinging(final int what, String token,
                                       String apartment_id, String is_play, final MyCallBack myCallBack) {
        OkGo.<String>post(Constant.IF_SING_URL)
                .tag(App.getInstance())
                .params("token", token)
                .params("is_play", is_play)
                .params("apartment_id", apartment_id)
                .execute(new StringCallback() {
                    @Override
                    public void onSuccess(Response<String> response) {
                        myCallBack.onSuccess(what, response);
                    }

                    @Override
                    public void onError(Response<String> response) {
                        super.onError(response);
                        myCallBack.onFail(what, response);
                    }

                    @Override
                    public void onFinish() {
                        super.onFinish();
                        myCallBack.onFinish(what);
                    }
                });
    }
    //歌曲是否有人在唱
    public static void isSongSinging(final int what,
                                 String apartment_id, String is_sing, final MyCallBack myCallBack) {
        OkGo.<String>post(Constant.IF_SING_SONG_URL)
                .tag(App.getInstance())
                .params("is_sing", is_sing)
                .params("apartment_id", apartment_id)
                .execute(new StringCallback() {
                    @Override
                    public void onSuccess(Response<String> response) {
                        myCallBack.onSuccess(what, response);
                    }

                    @Override
                    public void onError(Response<String> response) {
                        super.onError(response);
                        myCallBack.onFail(what, response);
                    }

                    @Override
                    public void onFinish() {
                        super.onFinish();
                        myCallBack.onFinish(what);
                    }
                });
    }
    //获取推流地址
    public static void getPushUrl(final int what,
                                     String chatroom_id, final MyCallBack myCallBack) {
        OkGo.<String>post(Constant.GET_PUSH_URL)
                .tag(App.getInstance())
                .params("chatroom_id", chatroom_id)
                .execute(new StringCallback() {
                    @Override
                    public void onSuccess(Response<String> response) {
                        myCallBack.onSuccess(what, response);
                    }

                    @Override
                    public void onError(Response<String> response) {
                        super.onError(response);
                        myCallBack.onFail(what, response);
                    }

                    @Override
                    public void onFinish() {
                        super.onFinish();
                        myCallBack.onFinish(what);
                    }
                });
    }
    //获取拉流地址
    public static void getPullUrl(final int what,
                                  String chatroom_id, final MyCallBack myCallBack) {
        OkGo.<String>post(Constant.GET_PULL_URL)
                .tag(App.getInstance())
                .params("chatroom_id", chatroom_id)
                .execute(new StringCallback() {
                    @Override
                    public void onSuccess(Response<String> response) {
                        myCallBack.onSuccess(what, response);
                        Log.i(TAG, "onSuccess: "+response.body());
                    }

                    @Override
                    public void onError(Response<String> response) {
                        super.onError(response);
                        myCallBack.onFail(what, response);
                        Log.i(TAG, "onError: "+response);
                    }

                    @Override
                    public void onFinish() {
                        super.onFinish();
                        myCallBack.onFinish(what);
                    }
                });
    }
}

