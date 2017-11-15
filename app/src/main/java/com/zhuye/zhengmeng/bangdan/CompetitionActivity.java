package com.zhuye.zhengmeng.bangdan;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.view.View;

import com.blankj.utilcode.util.SPUtils;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.google.gson.Gson;
import com.jude.easyrecyclerview.EasyRecyclerView;
import com.lzy.okgo.model.Response;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnLoadmoreListener;
import com.scwang.smartrefresh.layout.listener.OnRefreshListener;
import com.zhuye.zhengmeng.R;
import com.zhuye.zhengmeng.bangdan.adapter.CompetitionListAdapter;
import com.zhuye.zhengmeng.base.BaseActivity;
import com.zhuye.zhengmeng.dynamic.DynamicDetailActivity;
import com.zhuye.zhengmeng.home.fragment.model.DynamicModel;
import com.zhuye.zhengmeng.http.DreamApi;
import com.zhuye.zhengmeng.http.MyCallBack;
import com.zhuye.zhengmeng.view.MyAppTitle;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class CompetitionActivity extends BaseActivity implements OnRefreshListener, OnLoadmoreListener {


    @BindView(R.id.titleBar)
    MyAppTitle titleBar;
    @BindView(R.id.recyclerView)
    EasyRecyclerView recyclerView;
    //    @BindView(R.id.view_empty)
//    TextView viewEmpty;
    @BindView(R.id.refreshLayout)
    SmartRefreshLayout refreshLayout;
    int page = 1;
    private String token;
    private CompetitionListAdapter competitionListAdapter;

    @Override
    protected void processLogic() {

    }

    @Override
    protected void setListener() {
        token = SPUtils.getInstance("userInfo").getString("token");
        setTitle();

        refreshLayout.autoRefresh();
        refreshLayout.setOnRefreshListener(this);
        refreshLayout.setOnLoadmoreListener(this);
    }

    private void setTitle() {
        titleBar.initViewsVisible(true, true, true, false);
        titleBar.setAppTitle("平台比赛");
        titleBar.setTitleSize(20);
        titleBar.setRightIcon(R.mipmap.home_cup);
        titleBar.setOnLeftButtonClickListener(new MyAppTitle.OnLeftButtonClickListener() {
            @Override
            public void onLeftButtonClick(View v) {
                finish();
            }
        });
        titleBar.setOnRightButtonClickListener(new MyAppTitle.OnRightButtonClickListener() {
            @Override
            public void OnRightButtonClick(View v) {
                startActivity(new Intent(CompetitionActivity.this, BangdanRankActivity.class));
            }
        });
    }

    @Override
    protected void loadViewLayout() {
        setContentView(R.layout.activity_competition);
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
                            DynamicModel dynamicModel = new Gson().fromJson(result.body(), DynamicModel.class);
                            List<DynamicModel.DataBean> data;
                            data = dynamicModel.getData();
                            recyclerView.setLayoutManager(new LinearLayoutManager(CompetitionActivity.this));

                            competitionListAdapter = new CompetitionListAdapter(R.layout.fragment_dynamic_item1, data, CompetitionActivity.this);
                            if (data.size() == 0) {
                                competitionListAdapter.setEmptyView(R.layout.empty, recyclerView);
                            }
                            recyclerView.setAdapter(competitionListAdapter);
                            competitionListAdapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
                                @Override
                                public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                                    Intent intent = new Intent(CompetitionActivity.this, DynamicDetailActivity.class);
                                    intent.putExtra("production_id", competitionListAdapter.getItem(position).getProduction_id());
                                    intent.putExtra("production_path", competitionListAdapter.getItem(position).getProduction_path());
                                    intent.putExtra("production_name", competitionListAdapter.getItem(position).getProduction_name());
                                    startActivity(intent);
                                }
                            });
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
            refreshLayout.finishLoadmore();//解决加载更多一直显示
            refreshLayout.finishRefresh();
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // TODO: add setContentView(...) invocation
        ButterKnife.bind(this);
    }

    @Override
    public void onRefresh(RefreshLayout refreshlayout) {
        page = 1;
        DreamApi.getCompetitionList(0x001, token, "", page, myCallBack);
    }

    @Override
    public void onLoadmore(final RefreshLayout refreshlayout) {
        page++;
        DreamApi.getCompetitionList(0x001, token, "", page, new MyCallBack() {
            @Override
            public void onSuccess(int what, Response<String> result) {
                try {
                    JSONObject jsonObject = new JSONObject(result.body());
                    int code = jsonObject.getInt("code");
                    if (code == 200) {
                        DynamicModel dynamicModel = new Gson().fromJson(result.body(), DynamicModel.class);
                        List<DynamicModel.DataBean> data;
                        data = dynamicModel.getData();

                        if (data.size() == 0) {
                            refreshLayout.setLoadmoreFinished(true);
                        } else {
                            competitionListAdapter.addData(data);
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
                refreshlayout.finishRefresh();
                refreshlayout.finishLoadmore();
            }
        });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        page = 1;
    }
}
