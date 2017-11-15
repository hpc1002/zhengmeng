package com.zhuye.zhengmeng.user;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.blankj.utilcode.util.SPUtils;
import com.google.gson.Gson;
import com.jude.easyrecyclerview.EasyRecyclerView;
import com.jude.easyrecyclerview.adapter.BaseViewHolder;
import com.jude.easyrecyclerview.adapter.RecyclerArrayAdapter;
import com.lzy.okgo.model.Response;
import com.zhuye.zhengmeng.R;
import com.zhuye.zhengmeng.base.BaseActivity;
import com.zhuye.zhengmeng.http.DreamApi;
import com.zhuye.zhengmeng.http.MyCallBack;
import com.zhuye.zhengmeng.user.bean.GoldListBean;
import com.zhuye.zhengmeng.user.viewHolder.GoldListViewHolder;
import com.zhuye.zhengmeng.utils.ToastManager;
import com.zhuye.zhengmeng.view.MyAppTitle;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * 我的金币
 */
public class MyGoldActivity extends BaseActivity implements View.OnClickListener {


    @BindView(R.id.titleBar)
    MyAppTitle titleBar;
    @BindView(R.id.recyclerView)
    EasyRecyclerView recyclerView;
    @BindView(R.id.tv_my_gold_count)
    TextView tvMyGoldCount;
    @BindView(R.id.tv_bangdan_gold_count)
    TextView tvBangdanGoldCount;
    @BindView(R.id.tv_tixian_my)
    TextView tvTixianMy;
    @BindView(R.id.tv_tixian_bangdan)
    TextView tvTixianBangdan;

    private RecyclerArrayAdapter<GoldListBean.Data.GoldList> goldListAdapter;

    @Override
    protected void processLogic() {
        String token = SPUtils.getInstance("userInfo").getString("token");
        if (!token.equals("")) {
            DreamApi.getGoldList(0x001, this, token, myCallBack);
        }
    }

    @Override
    protected void setListener() {
        setTitle();
        recyclerView.setLayoutManager(new GridLayoutManager(this, 3));
        tvTixianMy.setOnClickListener(this);
        tvTixianBangdan.setOnClickListener(this);
    }

    private void setTitle() {
        titleBar.initViewsVisible(true, true, false, false);
        titleBar.setTitleSize(20);
        titleBar.setAppTitle("我的金币");
        titleBar.setOnLeftButtonClickListener(new MyAppTitle.OnLeftButtonClickListener() {
            @Override
            public void onLeftButtonClick(View v) {
                finish();
            }
        });
    }

    @Override
    protected void loadViewLayout() {
        setContentView(R.layout.activity_my_gold);
    }

    @Override
    protected Context getActivityContext() {
        return this;
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
                            GoldListBean goldListBean = new Gson().fromJson(result.body(), GoldListBean.class);
                            String score = goldListBean.data.score;
                            String score_Focus = goldListBean.data.score_Focus;
                            tvMyGoldCount.setText(score);
                            tvBangdanGoldCount.setText(score_Focus);
                            ArrayList<GoldListBean.Data.GoldList> goldLists = new ArrayList<>();
                            for (int i = 0; i < goldListBean.data.list.size(); i++) {
                                goldLists.add(goldListBean.data.list.get(i));
                            }
                            initUi(goldLists);
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

    private void initUi(ArrayList<GoldListBean.Data.GoldList> goldLists) {
        recyclerView.setAdapterWithProgress(goldListAdapter = new RecyclerArrayAdapter<GoldListBean.Data.GoldList>(this) {
            @Override
            public BaseViewHolder OnCreateViewHolder(ViewGroup parent, int viewType) {
                return new GoldListViewHolder(parent);
            }
        });
        goldListAdapter.addAll(goldLists);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // TODO: add setContentView(...) invocation
        ButterKnife.bind(this);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.tv_tixian_my:
                ToastManager.show("提现我的金币");
                startActivity(new Intent(this, WithdrawalsActivity.class));
                break;
            case R.id.tv_tixian_bangdan:
                ToastManager.show("提现榜单金币");
                break;
        }
    }
}
