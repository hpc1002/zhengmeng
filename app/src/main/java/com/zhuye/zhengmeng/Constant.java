package com.zhuye.zhengmeng;

/**
 * Created by hpc on 2017/10/26.
 */

public class Constant {
    public static final String BASE_URL_PINJIE = "http://192.168.0.134";
    //基本URL
    public static final String BASE_URL = "http://192.168.0.134/";
    public static final String BASE_URL2 = "http://192.168.0.134";
    //注册
    public static final String REGISTER_URL = BASE_URL + "changba/api/users/register";
    //登录
    public static final String LOGIN_URL = BASE_URL + "changba/api/users/login";
    //获取手机验证码
    public static final String GET_SMSCODE_URL = BASE_URL + "changba/Api/users/MobileVerify";
    //验证验证码
    public static final String VERIFIC_CODE_URL = BASE_URL + "changba/api/users/phone_verify";
    //找回密码
    public static final String CHANGE_PASSWORD_URL = BASE_URL + "changba/api/users/Change_password";
    //动态获取列表
    public static final String CONDITION_LIST_URL = BASE_URL + "changba/api/users/condition_list";
    //收藏
    public static final String COLLECT_LIST_URL = BASE_URL + "changba/api/users/collect_list";
    //获取用户信息
    public static final String USERINFO_URL = BASE_URL + "changba/api/users/userinfo";
    //修改用户信息
    public static final String CHANGE_USERINFO_URL = BASE_URL + "changba/api/users/modify_datum";
    //签到
    public static final String SIGN_URL = BASE_URL + "changba/api/users/user_sign_in";
    //粉丝列表
    public static final String FANS_LIST_URL = BASE_URL + "changba/api/users/fans_list";
    //商城web
    public static final String SHOP_WEB_URL = BASE_URL + "changba/api/gift/shop";
    //分享列表
    public static final String SHARE_LIST_URL = BASE_URL + "changba/api/users/share_list";
    //金币列表
    public static final String GOLD_LIST_URL = BASE_URL + "changba/api/users/gold_list";
    //活动公告
    public static final String MSG_LIST_URL = BASE_URL + "changba/api/users/msg_list";
    //收到的礼物列表
    public static final String RECEIVE_LIST_URL = BASE_URL + "/changba/api/users/receive_gift";
    //送出的礼物列表
    public static final String SEND_LIST_URL = BASE_URL + "/changba/api/users/send_gift";
    //歌曲列表
    public static final String SONG_LIST_URL = BASE_URL + "/changba/api/ktv/song_list";
    //动态详情评论列表
    public static final String CONTENT_LIST_URL = BASE_URL + "/changba/api/users/comment_list";

    //获取头像列表
    public static final String AVATOR_LIST_URL = BASE_URL + "changba/api/users/user_photo_list";
    //上传头像
    public static final String UPLOAD_AVATOR_URL = BASE_URL + "changba/api/users/user_photo";
    //更换用户头像
    public static final String CHANGE_AVATOR_URL = BASE_URL + "changba/api/users/setting_avatar";
    //KTV房间列表
    public static final String KTV_ROOM_LIST_URL = BASE_URL + "changba/api/rongcloud/chatroom_list";
    //上传录音文件
    public static final String RECORD_URL = BASE_URL + "changba/api/users/Upload_video";
    //平台比赛列表
    public static final String GAME_COMPETITION_LIST_URL = BASE_URL + "changba/api/monthlyfocus/game_list";
    //二级评论列表
    public static final String ERJI_COMMENT_LIST_URL = BASE_URL + "changba/api/users/comment_page_list";
    //动态详情
    public static final String DYNAMIC_DETAIL_URL = BASE_URL + "changba/api/users/production_details";
    //创建聊天室
    public static final String CREATE_CHATROOM_URL = BASE_URL + "changba/api/rongcloud/create";
    //帮助
    public static final String HELP_URL = BASE_URL + "changba/api/about/help_url";
    //关于征梦
    public static final String ABOUTUS_URL = BASE_URL + "changba/api/about/about_url";
    //活动公告
    public static final String ACTIVITY_BULLETIN_URL = BASE_URL + "changba/api/users/msg_list";
    //动态点赞
    public static final String CLICK_ZAN_URL = BASE_URL + "changba/api/users/like";
    //全国地区榜单
    public static final String BANGDAN_LIST_URL = BASE_URL + "changba/api/monthlyfocus/city_list";
    //我的关注列表
    public static final String ATTENTION_LIST_URL = BASE_URL + "changba/api/users/attention_list";
    //平台比赛列表
    public static final String COMPETITION_LIST_URL = BASE_URL + "changba/api/monthlyfocus/monthly_focus_list";
    //比赛榜单
    public static final String BANGDAN_RANK_LIST_URL = BASE_URL + "changba/api/monthlyfocus/game_list";
    //银行卡列表
    public static final String BANKCARD_LIST_URL = BASE_URL + "changba/api/users/bank_card_list";
    //上榜记录
    public static final String RANKING_LIST_URL = BASE_URL + "changba/api/users/ranking_list";
    //添加取消关注
    public static final String FOLLOW_OR_NOT_URL = BASE_URL + "changba/api/users/attention";
    //动态详情粉丝数量
    public static final String FANS_NUMBER_URL = BASE_URL + "changba/api/users/fans_count";
    //粉丝贡献榜
    public static final String FANS_CONTRIBUTION_RANK_URL = BASE_URL + "changba/api/users/production_details_fans_list";
    //添加评论
    public static final String ADD_COMMENT_URL = BASE_URL + "changba/api/users/comment_add";
    //加入聊天室
    public static final String ENTER_CHATROOM_URL = BASE_URL + "changba/api/rongcloud/join";
    //送礼物web
    public static final String SENDGIFT_URL = BASE_URL + "changba/api/gift/send_gift";
    //ktv排麦列表
    public static final String PAIMAI_LIST_URL = BASE_URL + "changba/api/ktv/mini_marker_list";
    //ktv点唱
    public static final String CHOOSE_SONG_URL = BASE_URL + "changba/api/ktv/mini_marker_add";
    //添加银行卡
    public static final String ADD_BANK_CARD_URL = BASE_URL + "changba/api/users/bank_card_add";
    //删除银行卡
    public static final String DELETE_BANK_CARD_URL = BASE_URL + "changba/api/users/bank_card_del";

    //融云app_key
    public static final String APP_KEY = "k51hidwqknr6b";
}
