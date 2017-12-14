package com.zhuye.zhengmeng.home.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.blankj.utilcode.util.SPUtils;
import com.bumptech.glide.Glide;
import com.flyco.dialog.listener.OnBtnClickL;
import com.flyco.dialog.widget.MaterialDialog;
import com.flyco.dialog.widget.NormalDialog;
import com.lzy.okgo.model.Response;
import com.zhuye.zhengmeng.Constant;
import com.zhuye.zhengmeng.R;
import com.zhuye.zhengmeng.base.BaseNoFragment;
import com.zhuye.zhengmeng.http.DreamApi;
import com.zhuye.zhengmeng.http.MyCallBack;
import com.zhuye.zhengmeng.user.LocalRecordActivity;
import com.zhuye.zhengmeng.user.MyBankCardActivity;
import com.zhuye.zhengmeng.user.MyCollectActivity;
import com.zhuye.zhengmeng.user.MyFansActivity;
import com.zhuye.zhengmeng.user.MyFeedbackActivity;
import com.zhuye.zhengmeng.user.MyFollowActivity;
import com.zhuye.zhengmeng.user.MyGiftActivity;
import com.zhuye.zhengmeng.user.MyGoldActivity;
import com.zhuye.zhengmeng.user.MyMessageActivity;
import com.zhuye.zhengmeng.user.MyShareActivity;
import com.zhuye.zhengmeng.user.RechargeActivity;
import com.zhuye.zhengmeng.user.SettingActivity;
import com.zhuye.zhengmeng.user.UserInfoActivity;
import com.zhuye.zhengmeng.user.ZuoPinJiActivity;
import com.zhuye.zhengmeng.utils.ToastManager;
import com.zhuye.zhengmeng.widget.RoundedCornerImageView;

import org.json.JSONException;
import org.json.JSONObject;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

/**
 * Created by hpc on 2017/10/26.
 */

public class MeFragment extends BaseNoFragment implements View.OnClickListener {
    @BindView(R.id.avatar)
    RoundedCornerImageView avatar;
    @BindView(R.id.nickname)
    TextView nickname;
    @BindView(R.id.btn_sign)
    TextView btnSign;
    @BindView(R.id.tag_vip)
    ImageView tagVip;
    @BindView(R.id.tv_follow)
    TextView tvFollow;
    @BindView(R.id.tv_fans)
    TextView tvFans;
    @BindView(R.id.tv_message)
    TextView tvMessage;
    @BindView(R.id.iv_local_record)
    ImageView ivLocalRecord;
    @BindView(R.id.tv_local_record)
    TextView tvLocalRecord;
    @BindView(R.id.rl_local_record)
    RelativeLayout rlLocalRecord;
    @BindView(R.id.iv_my_collect)
    ImageView ivMyCollect;
    @BindView(R.id.tv_my_collect)
    TextView tvMyCollect;
    @BindView(R.id.rl_my_collect)
    RelativeLayout rlMyCollect;
    @BindView(R.id.iv_comment_reply)
    ImageView ivCommentReply;
    @BindView(R.id.tv_comment_reply)
    TextView tvCommentReply;
    @BindView(R.id.rl_comment_reply)
    RelativeLayout rlCommentReply;
    @BindView(R.id.iv_my_gift)
    ImageView ivMyGift;
    @BindView(R.id.tv_my_gift)
    TextView tvMyGift;
    @BindView(R.id.rl_my_gift)
    RelativeLayout rlMyGift;
    @BindView(R.id.iv_my_gold)
    ImageView ivMyGold;
    @BindView(R.id.tv_my_gold)
    TextView tvMyGold;
    @BindView(R.id.rl_my_gold)
    RelativeLayout rlMyGold;
    @BindView(R.id.iv_my_share)
    ImageView ivMyShare;
    @BindView(R.id.tv_my_share)
    TextView tvMyShare;
    @BindView(R.id.rl_my_share)
    RelativeLayout rlMyShare;
    @BindView(R.id.iv_my_feedback)
    ImageView ivMyFeedback;
    @BindView(R.id.tv_my_feedback)
    TextView tvMyFeedback;
    @BindView(R.id.rl_my_feedback)
    RelativeLayout rlMyFeedback;
    Unbinder unbinder;
    @BindView(R.id.iv_set)
    ImageView ivSet;
    @BindView(R.id.zuopinji)
    RelativeLayout zuopinji;

    private String token;
    private View view;
    private String sign_type;
    @Override
    protected View initView(LayoutInflater inflater, ViewGroup container) {

        view = inflater.inflate(R.layout.fragment_me, container, false);
        return view;
    }

    @Override
    protected void initListener() {
        rlMyGift.setOnClickListener(this);
        rlLocalRecord.setOnClickListener(this);
        rlMyCollect.setOnClickListener(this);
        rlMyShare.setOnClickListener(this);
        rlMyGold.setOnClickListener(this);
        tvFans.setOnClickListener(this);
        tvMessage.setOnClickListener(this);
        tvFollow.setOnClickListener(this);
        rlMyFeedback.setOnClickListener(this);
        zuopinji.setOnClickListener(this);
        ivSet.setOnClickListener(this);
        btnSign.setOnClickListener(this);
        rlCommentReply.setOnClickListener(this);
        avatar.setOnClickListener(this);
        tagVip.setOnClickListener(this);
    }

    @Override
    protected void initData() {

        token = SPUtils.getInstance("userInfo").getString("token");

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // TODO: inflate a fragment view
        View rootView = super.onCreateView(inflater, container, savedInstanceState);
        unbinder = ButterKnife.bind(this, rootView);
        return rootView;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.rl_my_gift:
                startActivity(new Intent(getActivity(), MyGiftActivity.class));
                break;
            case R.id.rl_my_gold:
                startActivity(new Intent(getActivity(), MyGoldActivity.class));
                break;
            case R.id.rl_local_record:
                startActivity(new Intent(getActivity(), LocalRecordActivity.class));
                break;
            case R.id.rl_my_collect:
                startActivity(new Intent(getActivity(), MyCollectActivity.class));
                break;
            case R.id.rl_my_share:
                startActivity(new Intent(getActivity(), MyShareActivity.class));
                break;
                case R.id.rl_my_feedback:
                startActivity(new Intent(getActivity(), MyFeedbackActivity.class));
                break;
            case R.id.rl_comment_reply:
                startActivity(new Intent(getActivity(), MyBankCardActivity.class));
                break;
            case R.id.tv_fans:
                startActivity(new Intent(getActivity(), MyFansActivity.class));
                break;
            case R.id.tv_message:
                startActivity(new Intent(getActivity(), MyMessageActivity.class));
                break;
            case R.id.tv_follow:
                startActivity(new Intent(getActivity(), MyFollowActivity.class));
                break;
            case R.id.avatar:
                startActivity(new Intent(getActivity(), UserInfoActivity.class));
                break;
            case R.id.zuopinji:
                startActivity(new Intent(getActivity(), ZuoPinJiActivity.class));
                break;
            case R.id.iv_set:
                startActivity(new Intent(getActivity(), SettingActivity.class));
                break;
            case R.id.btn_sign:
                //签到
                DreamApi.sign(0x002, token, myCallBack);
                break;
            case R.id.tag_vip:
                Intent intent = new Intent(getActivity(), RechargeActivity.class);
                startActivity(intent);
                break;
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        nickname.setText(SPUtils.getInstance("userInfo").getString("user_nicename"));
        String avatar = SPUtils.getInstance("userInfo").getString("avatar");
        if (avatar.contains("http")) {
            Glide.with(getActivity())
                    .load(avatar)
                    .into((ImageView) view.findViewById(R.id.avatar));
        } else {
            Glide.with(getActivity())
                    .load(Constant.BASE_URL_PINJIE + avatar)
                    .into((ImageView) view.findViewById(R.id.avatar));
        }

    }

    /**
     * {
     * "data": {
     * "user_id": "10",
     * "user_nicename": " ",
     * "mobile": "18516886132",
     * "avatar": "  ",
     * "sex": "0",
     * "signature": " ",
     * "attention_sum": "0",
     * "fans_sum": "0",
     * "type": 1
     * },
     * "msg": "",
     * "code": 200
     * }
     */
    private MyCallBack myCallBack = new MyCallBack() {
        @Override
        public void onSuccess(int what, Response<String> result) {
            switch (what) {
                case 0x001:
                    try {
                        JSONObject jsonObject = new JSONObject(result.body());
                        int code = jsonObject.getInt("code");
                        if (code == 200) {
                            JSONObject data = jsonObject.getJSONObject("data");
                            String user_nicename = data.optString("user_nicename");
                            String user_id = data.optString("user_id");
                            String avatar = data.optString("avatar");
                            String fans_sum = data.optString("fans_sum");
                            String attention_sum = data.optString("attention_sum");

                            sign_type = data.optString("sign_type");
                            String cookie = data.optString("cookie");
                            SPUtils.getInstance("userInfo").put("cookie", cookie);//关注数
                            SPUtils.getInstance("userInfo").put("attention_sum", attention_sum);//关注数
                            SPUtils.getInstance("userInfo").put("fans_sum", fans_sum);//粉丝数
                            SPUtils.getInstance("userInfo").put("user_id", user_id);//个人的Id
                            if (sign_type.equals("0")) {
                                //未签到
                                btnSign.setBackground(getResources().getDrawable(R.drawable.frame_button));
                                btnSign.setText("签到");


                            } else if (sign_type.equals("1")) {
                                //已签到
                                btnSign.setBackground(getResources().getDrawable(R.drawable.frame_button_gray));
                                btnSign.setText("已签到");
                            }
                            nickname.setText(user_nicename);
                            tvFollow.setText("关注 " + attention_sum);
                            tvFans.setText("粉丝 " + fans_sum);
                            tvMessage.setText("消息 ");
                            String userAvatar = SPUtils.getInstance("userInfo").getString("avatar");
                            if (userAvatar.contains("http")) {
                                Glide.with(getActivity())
                                        .load(userAvatar)
                                        .into((ImageView) view.findViewById(R.id.avatar));
                            } else {
                                Glide.with(getActivity())
                                        .load(Constant.BASE_URL_PINJIE + userAvatar)
                                        .into((ImageView) view.findViewById(R.id.avatar));
                            }

                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    break;
                case 0x002:
                    try {
                        JSONObject jsonObject = new JSONObject(result.body());
                        int code = jsonObject.getInt("code");
                        if (code == 200) {
                            String msg = jsonObject.getString("msg");
                            ToastManager.show(msg);
                            DreamApi.getUserInfo(0x001, token, myCallBack);
                            final NormalDialog dialog = new NormalDialog(getActivity());
                            if (!sign_type.equals("1")){
                                dialog.content("签到成功，获得5个金币")
                                        .btnNum(1)
                                        .btnText("确定")
                                        .style(NormalDialog.STYLE_TWO)
                                        .titleTextSize(23)
                                        .show();
                                dialog.setOnBtnClickL(new OnBtnClickL() {
                                    @Override
                                    public void onBtnClick() {

                                        dialog.dismiss();
                                    }
                                });
                            }



                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    break;
            }
        }

        @Override
        public void onFail(int what, Response<String> result) {

        }

        @Override
        public void onFinish(int what) {

        }
    };

    @Override
    public void onHiddenChanged(boolean hidden) {
        super.onHiddenChanged(hidden);
        DreamApi.getUserInfo(0x001, token, myCallBack);
    }
}
