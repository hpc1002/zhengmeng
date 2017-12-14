package com.zhuye.zhengmeng;

/**
 * Created by hpc on 2017/10/26.
 */

public class Constant {
//    public static final String BASE_URL_PINJIE = "http://192.168.1.4";
//    //基本URL
//    public static final String BASE_URL = "http://192.168.1.4/";
//    public static final String BASE_URL2 = "http://192.168.1.4";
    public static final String BASE_URL_PINJIE = "http://changba.zyeo.net";
    //基本URL
    public static final String BASE_URL = "http://changba.zyeo.net/api/";
    public static final String BASE_URL2 = "http://changba.zyeo.net";
    //注册
    public static final String REGISTER_URL = BASE_URL + "users/register";
    //登录
    public static final String LOGIN_URL = BASE_URL + "users/login";
    //获取手机验证码
    public static final String GET_SMSCODE_URL = BASE_URL + "users/MobileVerify";
    //验证验证码
    public static final String VERIFIC_CODE_URL = BASE_URL + "users/phone_verify";
    //找回密码
    public static final String CHANGE_PASSWORD_URL = BASE_URL + "users/Change_password";
    //动态获取列表
    public static final String CONDITION_LIST_URL = BASE_URL + "users/condition_list";
    //收藏
    public static final String COLLECT_LIST_URL = BASE_URL + "users/collect_list";
    //获取用户信息
    public static final String USERINFO_URL = BASE_URL + "users/userinfo";
    //修改用户信息
    public static final String CHANGE_USERINFO_URL = BASE_URL + "users/modify_datum";
    //签到
    public static final String SIGN_URL = BASE_URL + "users/user_sign_in";
    //粉丝列表
    public static final String FANS_LIST_URL = BASE_URL + "users/fans_list";
    //商城web
    public static final String SHOP_WEB_URL = BASE_URL + "gift/shop";
    //分享列表
    public static final String SHARE_LIST_URL = BASE_URL + "users/share_list";
    //金币列表
    public static final String GOLD_LIST_URL = BASE_URL + "users/gold_list";
    //活动公告
    public static final String MSG_LIST_URL = BASE_URL + "users/msg_list";
    //收到的礼物列表
    public static final String RECEIVE_LIST_URL = BASE_URL + "users/receive_gift";
    //送出的礼物列表
    public static final String SEND_LIST_URL = BASE_URL + "users/send_gift";
    //歌曲列表
    public static final String SONG_LIST_URL = BASE_URL + "ktv/song_list";
    //动态详情评论列表
    public static final String CONTENT_LIST_URL = BASE_URL + "users/comment_list";

    //获取头像列表
    public static final String AVATOR_LIST_URL = BASE_URL + "users/user_photo_list";
    //上传头像
    public static final String UPLOAD_AVATOR_URL = BASE_URL + "users/user_photo";
    //更换用户头像
    public static final String CHANGE_AVATOR_URL = BASE_URL + "users/setting_avatar";
    //KTV房间列表
    public static final String KTV_ROOM_LIST_URL = BASE_URL + "rongcloud/chatroom_list";
    //上传录音文件
    public static final String RECORD_URL = BASE_URL + "users/Upload_video";
    //平台比赛列表
    public static final String GAME_COMPETITION_LIST_URL = BASE_URL + "monthlyfocus/game_list";
    //二级评论列表
    public static final String ERJI_COMMENT_LIST_URL = BASE_URL + "users/comment_page_list";
    //动态详情
    public static final String DYNAMIC_DETAIL_URL = BASE_URL + "users/production_details";
    //创建聊天室
    public static final String CREATE_CHATROOM_URL = BASE_URL + "rongcloud/create";
    //帮助
    public static final String HELP_URL = BASE_URL + "about/help_url";
    //关于征梦
    public static final String ABOUTUS_URL = BASE_URL + "about/about_url";
    //活动公告
    public static final String ACTIVITY_BULLETIN_URL = BASE_URL + "users/msg_list";
    //动态点赞
    public static final String CLICK_ZAN_URL = BASE_URL + "users/like";
    //全国地区榜单
    public static final String BANGDAN_LIST_URL = BASE_URL + "monthlyfocus/city_list";
    //我的关注列表
    public static final String ATTENTION_LIST_URL = BASE_URL + "users/attention_list";
    //平台比赛列表
    public static final String COMPETITION_LIST_URL = BASE_URL + "monthlyfocus/monthly_focus_list";
    //比赛榜单
    public static final String BANGDAN_RANK_LIST_URL = BASE_URL + "monthlyfocus/game_list";
    //银行卡列表
    public static final String BANKCARD_LIST_URL = BASE_URL + "users/bank_card_list";
    //上榜记录
    public static final String RANKING_LIST_URL = BASE_URL + "users/ranking_list";
    //添加取消关注
    public static final String FOLLOW_OR_NOT_URL = BASE_URL + "users/attention";
    //动态详情粉丝数量
    public static final String FANS_NUMBER_URL = BASE_URL + "users/fans_count";
    //粉丝贡献榜
    public static final String FANS_CONTRIBUTION_RANK_URL = BASE_URL + "users/production_details_fans_list";
    //添加评论
    public static final String ADD_COMMENT_URL = BASE_URL + "users/comment_add";
    //添加收藏
    public static final String ADD_COLLECT_URL = BASE_URL + "users/add_collect";
    //取消收藏
    public static final String CANCEL_COLLECT_URL = BASE_URL + "users/abolish_collect";
    //加入聊天室
    public static final String ENTER_CHATROOM_URL = BASE_URL + "rongcloud/join";
    //送礼物web
    public static final String SENDGIFT_URL = BASE_URL + "gift/send_gift";
    //ktv排麦列表
    public static final String PAIMAI_LIST_URL = BASE_URL + "ktv/mini_marker_list";
    //ktv点唱
    public static final String CHOOSE_SONG_URL = BASE_URL + "ktv/mini_marker_add";
    //添加银行卡
    public static final String ADD_BANK_CARD_URL = BASE_URL + "users/bank_card_add";
    //删除银行卡
    public static final String DELETE_BANK_CARD_URL = BASE_URL + "users/bank_card_del";
    //下首歌曲播放歌曲文件
    public static final String NEXT_SONG_PLAY_URL = BASE_URL + "ktv/next_song_play";
    //房间是否有人在唱
    public static final String IF_SING_URL = BASE_URL + "ktv/is_play";
    //歌曲是否演唱
    public static final String IF_SING_SONG_URL = BASE_URL + "ktv/is_sing";
    //获取拉流地址
    public static final String GET_PULL_URL = BASE_URL + "ktv/get_la";
    //获取推流地址
    public static final String GET_PUSH_URL = BASE_URL + "ktv/get_tui";

    //获取商品列表
    public static final String GET_GOOD_LIST_URL = BASE_URL + "gift/android_gift_list";
    //购买商品
    public static final String BUY_GOOD_URL = BASE_URL + "gift/android_buy_gift";
    //详情礼物列表
    public static final String GOOD_DETAIL_LIST_URL = BASE_URL + "gift/android_p_gift_list";
    //背包礼物赠送
    public static final String PACKAGE_GIFT_SEND__URL = BASE_URL + "gift/android_p_gift_send";
    //商城礼物赠送
    public static final String SHOP_GIFT_SEND__URL = BASE_URL + "gift/android_shop_send";
    //会员价格 列表
    public static final String VIP_PRICE_LIST__URL = BASE_URL + "users/vip_list";
    //微信支付
    public static final String WEXIN_PAY_URL = BASE_URL + "payment/weixin_payment";
    //阿里支付
    public static final String AL_PAY_URL = BASE_URL + "payment/alipay_payment";
    //提现
    public static final String TIXIAN_URL = BASE_URL + "users/withdraw_deposit";
    //用户更新是否在线
    public static final String IFONLINE_URL = BASE_URL + "ktv/modify_chatroom_time";
    //加入房间
    public static final String JOIN_ROOM_URL = BASE_URL + "ktv/add_chatroom";
    //反馈
    public static final String FEEDBACK_URL = BASE_URL+"gift/fankui";
    //分享房间号链接
    public static final String SHARE_ROOM_URL = BASE_URL + "gift/fengxiang_url";
//    //注册
//    public static final String REGISTER_URL = BASE_URL + "changba/api/users/register";
//    //登录
//    public static final String LOGIN_URL = BASE_URL + "changba/api/users/login";
//    //获取手机验证码
//    public static final String GET_SMSCODE_URL = BASE_URL + "changba/Api/users/MobileVerify";
//    //验证验证码
//    public static final String VERIFIC_CODE_URL = BASE_URL + "changba/api/users/phone_verify";
//    //找回密码
//    public static final String CHANGE_PASSWORD_URL = BASE_URL + "changba/api/users/Change_password";
//    //动态获取列表
//    public static final String CONDITION_LIST_URL = BASE_URL + "changba/api/users/condition_list";
//    //收藏
//    public static final String COLLECT_LIST_URL = BASE_URL + "changba/api/users/collect_list";
//    //获取用户信息
//    public static final String USERINFO_URL = BASE_URL + "changba/api/users/userinfo";
//    //修改用户信息
//    public static final String CHANGE_USERINFO_URL = BASE_URL + "changba/api/users/modify_datum";
//    //签到
//    public static final String SIGN_URL = BASE_URL + "changba/api/users/user_sign_in";
//    //粉丝列表
//    public static final String FANS_LIST_URL = BASE_URL + "changba/api/users/fans_list";
//    //商城web
//    public static final String SHOP_WEB_URL = BASE_URL + "changba/api/gift/shop";
//    //分享列表
//    public static final String SHARE_LIST_URL = BASE_URL + "changba/api/users/share_list";
//    //金币列表
//    public static final String GOLD_LIST_URL = BASE_URL + "changba/api/users/gold_list";
//    //活动公告
//    public static final String MSG_LIST_URL = BASE_URL + "changba/api/users/msg_list";
//    //收到的礼物列表
//    public static final String RECEIVE_LIST_URL = BASE_URL + "/changba/api/users/receive_gift";
//    //送出的礼物列表
//    public static final String SEND_LIST_URL = BASE_URL + "/changba/api/users/send_gift";
//    //歌曲列表
//    public static final String SONG_LIST_URL = BASE_URL + "/changba/api/ktv/song_list";
//    //动态详情评论列表
//    public static final String CONTENT_LIST_URL = BASE_URL + "/changba/api/users/comment_list";
//
//    //获取头像列表
//    public static final String AVATOR_LIST_URL = BASE_URL + "changba/api/users/user_photo_list";
//    //上传头像
//    public static final String UPLOAD_AVATOR_URL = BASE_URL + "changba/api/users/user_photo";
//    //更换用户头像
//    public static final String CHANGE_AVATOR_URL = BASE_URL + "changba/api/users/setting_avatar";
//    //KTV房间列表
//    public static final String KTV_ROOM_LIST_URL = BASE_URL + "changba/api/rongcloud/chatroom_list";
//    //上传录音文件
//    public static final String RECORD_URL = BASE_URL + "changba/api/users/Upload_video";
//    //平台比赛列表
//    public static final String GAME_COMPETITION_LIST_URL = BASE_URL + "changba/api/monthlyfocus/game_list";
//    //二级评论列表
//    public static final String ERJI_COMMENT_LIST_URL = BASE_URL + "changba/api/users/comment_page_list";
//    //动态详情
//    public static final String DYNAMIC_DETAIL_URL = BASE_URL + "changba/api/users/production_details";
//    //创建聊天室
//    public static final String CREATE_CHATROOM_URL = BASE_URL + "changba/api/rongcloud/create";
//    //帮助
//    public static final String HELP_URL = BASE_URL + "changba/api/about/help_url";
//    //关于征梦
//    public static final String ABOUTUS_URL = BASE_URL + "changba/api/about/about_url";
//    //活动公告
//    public static final String ACTIVITY_BULLETIN_URL = BASE_URL + "changba/api/users/msg_list";
//    //动态点赞
//    public static final String CLICK_ZAN_URL = BASE_URL + "changba/api/users/like";
//    //全国地区榜单
//    public static final String BANGDAN_LIST_URL = BASE_URL + "changba/api/monthlyfocus/city_list";
//    //我的关注列表
//    public static final String ATTENTION_LIST_URL = BASE_URL + "changba/api/users/attention_list";
//    //平台比赛列表
//    public static final String COMPETITION_LIST_URL = BASE_URL + "changba/api/monthlyfocus/monthly_focus_list";
//    //比赛榜单
//    public static final String BANGDAN_RANK_LIST_URL = BASE_URL + "changba/api/monthlyfocus/game_list";
//    //银行卡列表
//    public static final String BANKCARD_LIST_URL = BASE_URL + "changba/api/users/bank_card_list";
//    //上榜记录
//    public static final String RANKING_LIST_URL = BASE_URL + "changba/api/users/ranking_list";
//    //添加取消关注
//    public static final String FOLLOW_OR_NOT_URL = BASE_URL + "changba/api/users/attention";
//    //动态详情粉丝数量
//    public static final String FANS_NUMBER_URL = BASE_URL + "changba/api/users/fans_count";
//    //粉丝贡献榜
//    public static final String FANS_CONTRIBUTION_RANK_URL = BASE_URL + "changba/api/users/production_details_fans_list";
//    //添加评论
//    public static final String ADD_COMMENT_URL = BASE_URL + "changba/api/users/comment_add";
//    //加入聊天室
//    public static final String ENTER_CHATROOM_URL = BASE_URL + "changba/api/rongcloud/join";
//    //送礼物web
//    public static final String SENDGIFT_URL = BASE_URL + "changba/api/gift/send_gift";
//    //ktv排麦列表
//    public static final String PAIMAI_LIST_URL = BASE_URL + "changba/api/ktv/mini_marker_list";
//    //ktv点唱
//    public static final String CHOOSE_SONG_URL = BASE_URL + "changba/api/ktv/mini_marker_add";
//    //添加银行卡
//    public static final String ADD_BANK_CARD_URL = BASE_URL + "changba/api/users/bank_card_add";
//    //删除银行卡
//    public static final String DELETE_BANK_CARD_URL = BASE_URL + "changba/api/users/bank_card_del";
//    //下首歌曲播放歌曲文件
//    public static final String NEXT_SONG_PLAY_URL = BASE_URL + "changba/api/ktv/next_song_play";
//    //房间是否有人在唱
//    public static final String IF_SING_URL = BASE_URL + "changba/api/ktv/is_play";
//    //歌曲是否演唱
//    public static final String IF_SING_SONG_URL = BASE_URL + "changba/api/ktv/is_sing";
//    //获取拉流地址
//    public static final String GET_PULL_URL = BASE_URL + "changba/api/ktv/get_la";
//    //获取推流地址
//    public static final String GET_PUSH_URL = BASE_URL + "changba/api/ktv/get_tui";
    //融云app_key
    public static final String APP_KEY = "k51hidwqknr6b";
    //微信app_id
    public static final String WX_APP_ID = "wx719fa095e03da584";
}
