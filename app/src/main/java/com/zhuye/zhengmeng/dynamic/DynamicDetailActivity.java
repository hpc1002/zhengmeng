package com.zhuye.zhengmeng.dynamic;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.widget.LinearLayoutManager;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.blankj.utilcode.util.SPUtils;
import com.bumptech.glide.Glide;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.flyco.dialog.listener.OnOperItemClickL;
import com.flyco.dialog.widget.ActionSheetDialog;
import com.google.gson.Gson;
import com.jude.easyrecyclerview.EasyRecyclerView;
import com.longsh.optionframelibrary.OptionCenterDialog;
import com.lzy.okgo.model.Response;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnLoadmoreListener;
import com.scwang.smartrefresh.layout.listener.OnRefreshListener;
import com.zhuye.zhengmeng.Constant;
import com.zhuye.zhengmeng.R;
import com.zhuye.zhengmeng.bangdan.SongsListActivity;
import com.zhuye.zhengmeng.bangdan.recording.QAudioActivity;
import com.zhuye.zhengmeng.bangdan.recording.QVideoActivity;
import com.zhuye.zhengmeng.base.BaseActivity;
import com.zhuye.zhengmeng.dynamic.adapter.CommentListAdapter;
import com.zhuye.zhengmeng.dynamic.bean.CommentListBean;
import com.zhuye.zhengmeng.dynamic.bean.FansContributionBean;
import com.zhuye.zhengmeng.http.DreamApi;
import com.zhuye.zhengmeng.http.MyCallBack;
import com.zhuye.zhengmeng.user.adapter.FansContributionAdapter;
import com.zhuye.zhengmeng.utils.ToastManager;
import com.zhuye.zhengmeng.widget.RoundedCornerImageView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import cn.jzvd.JZVideoPlayerStandard;

import static com.zhuye.zhengmeng.R.id.song_name;

public class DynamicDetailActivity extends BaseActivity implements OnRefreshListener, OnLoadmoreListener, View.OnClickListener {


    @BindView(R.id.videoplayer)
    JZVideoPlayerStandard videoplayer;
    @BindView(R.id.avatar)
    RoundedCornerImageView avatar;
    @BindView(R.id.nickname)
    TextView nickname;
    @BindView(R.id.tv_fans_number)
    TextView tvFansNumber;
    @BindView(R.id.follow_state)
    TextView followState;
    @BindView(R.id.sign_name)
    TextView signName;
    @BindView(R.id.recyclerView)
    EasyRecyclerView recyclerView;
    @BindView(R.id.refreshLayout)
    SmartRefreshLayout refreshLayout;
    @BindView(R.id.ll_bottom)
    LinearLayout llBottom;
    @BindView(R.id.rb_gift)
    TextView rbGift;
    @BindView(R.id.rb_comment)
    TextView rbComment;
    @BindView(R.id.rb_zhuanfa)
    TextView rbZhuanfa;
    @BindView(R.id.rb_perform)
    TextView rbPerform;
    @BindView(R.id.gridRecyclerView)
    EasyRecyclerView gridRecyclerView;
    @BindView(R.id.tv_gift_count)
    TextView tvGiftCount;
    private String production_id;
    private CommentListAdapter commentListAdapter;
    private String token;
    private PopupWindow popWindow;
    FansContributionAdapter fansContributionAdapter;
    int page = 1;

    @Override
    protected void processLogic() {

    }

    @Override
    protected void setListener() {

        production_id = getIntent().getStringExtra("production_id");
        String production_path = getIntent().getStringExtra("production_path");
        String production_name = getIntent().getStringExtra("production_name");

        token = SPUtils.getInstance("userInfo").getString("token");

        refreshLayout.autoRefresh();
        refreshLayout.setOnRefreshListener(this);
        refreshLayout.setOnLoadmoreListener(this);
        DreamApi.getDynamicDetail(0x002, token, production_id, myCallBack);
        DreamApi.getFansNumber(0x005, token, production_id, myCallBack);
        DreamApi.getFansContribution(0x006, token, "638", myCallBack);
        videoplayer.setUp(Constant.BASE_URL2 + production_path
                , JZVideoPlayerStandard.SCREEN_LAYOUT_NORMAL, production_name);
        videoplayer.startVideo();
        rbGift.setOnClickListener(this);
        rbComment.setOnClickListener(this);
        rbZhuanfa.setOnClickListener(this);
        rbPerform.setOnClickListener(this);
    }

    @Override
    protected void loadViewLayout() {
        setContentView(R.layout.activity_dynamic_detail);
    }

    @Override
    protected Context getActivityContext() {
        return this;
    }

    @Override
    public void onBackPressed() {
        if (videoplayer.backPress()) {
            return;
        }
        super.onBackPressed();
    }

    @Override
    protected void onPause() {
        super.onPause();
        videoplayer.releaseAllVideos();
    }

    private MyCallBack myCallBack = new MyCallBack() {
        @Override
        public void onSuccess(int what, Response<String> result) {
            switch (what) {
                case 0x001:
                    try {
                        JSONObject jsonObject = new JSONObject(result.body());
                        int code = jsonObject.getInt("code");
                        if (code == 200) {
                            CommentListBean commentListBean = new Gson().fromJson(result.body(), CommentListBean.class);
                            List<CommentListBean.Data> datas;
                            datas = commentListBean.data;
                            initUi(datas);
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
                            JSONArray data = jsonObject.getJSONArray("data");
                            JSONObject jsonObject1 = new JSONObject(data.get(0).toString());
                            String isatt = jsonObject1.getString("isatt");
                            final String user_id = jsonObject1.getString("user_id");
                            String user_nicename = jsonObject1.getString("user_nicename");
                            String user_avatar = jsonObject1.getString("avatar");
                            String production_content = jsonObject1.getString("production_content");
                            nickname.setText(user_nicename);
                            signName.setText(production_content);
                            if (isatt.equals("0")) {
                                //未关注
                                followState.setText("关注");
                                followState.setBackgroundResource(R.drawable.frame_button);
                                Drawable img_on;
                                Resources res = getResources();
                                img_on = res.getDrawable(R.mipmap.add);
                                // 调用setCompoundDrawables时，必须调用Drawable.setBounds()方法,否则图片不显示
                                img_on.setBounds(0, 0, img_on.getMinimumWidth(), img_on.getMinimumHeight());

                                followState.setCompoundDrawables(img_on, null, null, null);
                            } else if (isatt.equals("1")) {
                                //已关注
                                followState.setText("取消关注");
                                followState.setBackgroundResource(R.drawable.frame_button_gray);
                                followState.setCompoundDrawables(null, null, null, null);
                            }
                            followState.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View view) {
                                    DreamApi.followOrNot(0x004, token, user_id, myCallBack);
                                }
                            });

                            Glide.with(DynamicDetailActivity.this)
                                    .load(Constant.BASE_URL2 + user_avatar)
                                    .centerCrop()
                                    .placeholder(R.mipmap.touxiang_re)
                                    .into(avatar);

                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    break;
                case 0x003:
                    try {
                        JSONObject jsonObject = new JSONObject(result.body());
                        int code = jsonObject.getInt("code");
                        if (code == 200) {
                            refreshLayout.autoRefresh();
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    break;
                case 0x004:
                    try {
                        JSONObject jsonObject = new JSONObject(result.body());
                        int code = jsonObject.getInt("code");
                        if (code == 200) {
                            String msg = jsonObject.getString("msg");
                            ToastManager.show(msg);
                            DreamApi.getDynamicDetail(0x002, token, production_id, myCallBack);
                            DreamApi.getFansNumber(0x005, token, production_id, myCallBack);
                            return;
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    break;
                case 0x005:
                    try {
                        JSONObject jsonObject = new JSONObject(result.body());
                        int code = jsonObject.getInt("code");
                        if (code == 200) {
                            //粉丝数量
                            String data = jsonObject.getString("data");
                            tvFansNumber.setText(data + "粉丝");
                            Drawable img_on;
                            Resources res = getResources();
                            img_on = res.getDrawable(R.mipmap.dt_fans);
                            // 调用setCompoundDrawables时，必须调用Drawable.setBounds()方法,否则图片不显示
                            img_on.setBounds(0, 0, img_on.getMinimumWidth(), img_on.getMinimumHeight());
                            tvFansNumber.setCompoundDrawables(img_on, null, null, null);
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    break;
                case 0x006:
                    try {
                        JSONObject jsonObject = new JSONObject(result.body());
                        int code = jsonObject.getInt("code");
                        if (code == 200) {
                            //粉丝贡献榜
                            FansContributionBean fansContributionBean = new Gson().fromJson(result.body(), FansContributionBean.class);
                            Drawable img_on;
                            Resources res = getResources();
                            img_on = res.getDrawable(R.mipmap.gift);
                            // 调用setCompoundDrawables时，必须调用Drawable.setBounds()方法,否则图片不显示
                            img_on.setBounds(0, 0, img_on.getMinimumWidth(), img_on.getMinimumHeight());
                            tvGiftCount.setCompoundDrawables(img_on, null, null, null);
                            tvGiftCount.setText(fansContributionBean.getData().getGift_reception_count());
                            List<FansContributionBean.DataBean.GiftReceptionListBean> data;
                            data = fansContributionBean.getData().getGift_reception_list();
                            LinearLayoutManager layoutManager = new LinearLayoutManager(DynamicDetailActivity.this);
                            layoutManager.setOrientation(LinearLayoutManager.HORIZONTAL);
                            gridRecyclerView.setLayoutManager(layoutManager);

                            fansContributionAdapter = new FansContributionAdapter(R.layout.item_fans_contribution, data);
                            gridRecyclerView.setAdapter(fansContributionAdapter);
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    break;
                case 0x007:
                    try {
                        JSONObject jsonObject = new JSONObject(result.body());
                        int code = jsonObject.getInt("code");
                        if (code == 200) {
                            refreshLayout.autoRefresh();
                            comment_content.setText("");
                            popupInputMethodWindow();
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
            switch (what) {
                case 0x001:
                    refreshLayout.finishLoadmore();//解决加载更多一直显示
                    refreshLayout.finishRefresh();
                    break;
            }

        }
    };

    private void initUi(final List<CommentListBean.Data> datas) {
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        commentListAdapter = new CommentListAdapter(R.layout.item_comment, datas, this);
        //给RecyclerView设置适配器
        recyclerView.setAdapter(commentListAdapter);
        if (datas.size() == 0) {
            commentListAdapter.setEmptyView(R.layout.empty, recyclerView);
        }
        commentListAdapter.openLoadAnimation(BaseQuickAdapter.ALPHAIN);
        commentListAdapter.isFirstOnly(false);
        commentListAdapter.setOnItemChildClickListener(new BaseQuickAdapter.OnItemChildClickListener() {
            @Override
            public void onItemChildClick(BaseQuickAdapter adapter, View view, int position) {
                switch (view.getId()) {
                    case R.id.iv_zan:
                        DreamApi.dianZan(0x003, token, commentListAdapter.getItem(position).comment_id, myCallBack);
                        break;
                    case R.id.reply_count:
                        String sen_user_id = commentListAdapter.getItem(position).sen_user_id;
                        Intent intent = new Intent(DynamicDetailActivity.this, ErjiCommentActivity.class);
                        intent.putExtra("sen_user_id", sen_user_id);
                        intent.putExtra("production_id", production_id);
                        startActivity(intent);
                        break;
                }
            }
        });
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // TODO: add setContentView(...) invocation
        ButterKnife.bind(this);
    }

    @Override
    public void onRefresh(RefreshLayout refreshlayout) {
        page = 1;
        DreamApi.getCommentList(0x001, token, page, production_id, myCallBack);
    }

    @Override
    public void onLoadmore(final RefreshLayout refreshlayout) {
        page++;
        DreamApi.getCommentList(0x001, token, page, production_id, new MyCallBack() {
            @Override
            public void onSuccess(int what, Response<String> result) {
                try {
                    JSONObject jsonObject = new JSONObject(result.body());
                    int code = jsonObject.getInt("code");
                    if (code == 200) {
                        CommentListBean commentListBean = new Gson().fromJson(result.body(), CommentListBean.class);
                        List<CommentListBean.Data> datas;
                        datas = commentListBean.data;
                        if (datas.size() == 0) {
                            refreshLayout.setLoadmoreFinished(true);
                        } else {
                            commentListAdapter.addData(datas);
                        }
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFail(int what, Response<String> result) {

            }

            @Override
            public void onFinish(int what) {
                refreshlayout.finishLoadmore();//解决加载更多一直显示
                refreshlayout.finishRefresh();
            }
        });
    }

    private void popupInputMethodWindow() {
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                InputMethodManager imm = (InputMethodManager) getSystemService(Service.INPUT_METHOD_SERVICE);
                imm.toggleSoftInput(0, InputMethodManager.HIDE_NOT_ALWAYS);
//                InputMethodManager imm = (InputMethodManager) rbComment.getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
//                imm.hideSoftInputFromWindow(getWindow().getDecorView().getWindowToken(), 0);
            }
        }, 0);
    }

    private EditText comment_content;
    private TextView fabiao;

    private void showPopup() {
        if (popWindow == null) {
            LayoutInflater layoutInflater = (LayoutInflater) this.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            View view = layoutInflater.inflate(R.layout.popwindow_pinglun, null);
            // 创建一个PopuWidow对象
            popWindow = new PopupWindow(view, LinearLayout.LayoutParams.FILL_PARENT, 140, true);

            comment_content = view.findViewById(R.id.pinglun);
            fabiao = view.findViewById(R.id.fabiao);
        }
        //popupwindow弹出时的动画		popWindow.setAnimationStyle(R.style.popupWindowAnimation);
        // 使其聚集 ，要想监听菜单里控件的事件就必须要调用此方法
        popWindow.setFocusable(true);
        // 设置允许在外点击消失
        popWindow.setOutsideTouchable(false);
        // 设置背景，这个是为了点击“返回Back”也能使其消失，并且并不会影响你的背景
        popWindow.setBackgroundDrawable(new BitmapDrawable());
        //软键盘不会挡着popupwindow
        popWindow.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);
        //设置菜单显示的位置
        popWindow.showAtLocation(rbComment, Gravity.BOTTOM, 0, 0);
        fabiao.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (comment_content.getText().toString().equals("")) {
                    ToastManager.show("评论不能为空");
                    return;
                }
                DreamApi.addComment(0x007, token, production_id, "0", comment_content.getText().toString(), myCallBack);
            }
        });
        //监听菜单的关闭事件
        popWindow.setOnDismissListener(new PopupWindow.OnDismissListener() {
            @Override
            public void onDismiss() {

            }
        });

        //监听触屏事件
        popWindow.setTouchInterceptor(new View.OnTouchListener() {
            public boolean onTouch(View view, MotionEvent event) {
                return false;
            }
        });
    }

    private void chooseRecordType() {
        final String[] stringItems = {"清唱录制", "伴唱录制"};
        final ActionSheetDialog dialog = new ActionSheetDialog(this, stringItems, null);
        dialog.title("录制").titleTextSize_SP(14.5f).show();
        dialog.setOnOperItemClickL(new OnOperItemClickL() {
            @Override
            public void onOperItemClick(AdapterView<?> parent, View view, int position, long id) {
                switch (position) {
                    case 0:
                        final ArrayList<String> list = new ArrayList<>();
                        list.add("清唱录音");
                        list.add("清唱视频");
                        final OptionCenterDialog optionCenterDialog = new OptionCenterDialog();
                        optionCenterDialog.show(DynamicDetailActivity.this, list);
                        optionCenterDialog.setItemClickListener(new AdapterView.OnItemClickListener() {
                            @Override
                            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                                switch (position) {
                                    case 0:
                                        Intent intent1 = new Intent(DynamicDetailActivity.this, QAudioActivity.class);
                                        intent1.putExtra("song_id", "");
                                        intent1.putExtra("song_name", song_name);
                                        intent1.putExtra("song_path", "");
                                        intent1.putExtra("type", "1");
                                        intent1.putExtra("song_type", "0");
                                        intent1.putExtra("lyric_path", "");
                                        intent1.putExtra("activity_id", "");
                                        startActivity(intent1);
                                        break;
                                    case 1:
                                        Intent intent = new Intent(DynamicDetailActivity.this, QVideoActivity.class);
                                        intent.putExtra("song_id", "");
                                        intent.putExtra("song_name", song_name);
                                        intent.putExtra("song_path", "");
                                        intent.putExtra("type", "2");
                                        intent.putExtra("song_type", "0");
                                        intent.putExtra("lyric_path", "");
                                        intent.putExtra("activity_id", "");
                                        startActivity(intent);
                                        break;
                                }
                                optionCenterDialog.dismiss();
                            }
                        });
                        dialog.dismiss();

                        break;
                    case 1:
                        Intent intent = new Intent(DynamicDetailActivity.this, SongsListActivity.class);
                        startActivity(intent);
                        dialog.dismiss();
                        break;
                    default:
                        break;
                }

            }
        });
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.rb_gift:
                //送礼物
                ToastManager.show("送礼物");
                break;
            case R.id.rb_comment:
                //评论
                showPopup();
                popupInputMethodWindow();
                break;
            case R.id.rb_perform:
                //演唱
                chooseRecordType();
                break;
            case R.id.rb_zhuanfa:
                //转发
                ToastManager.show("转发");
                break;
            default:
                break;
        }
    }
}
