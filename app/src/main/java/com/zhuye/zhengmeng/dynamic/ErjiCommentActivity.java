package com.zhuye.zhengmeng.dynamic;

import android.content.Context;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.view.ViewGroup;

import com.blankj.utilcode.util.SPUtils;
import com.google.gson.Gson;
import com.jude.easyrecyclerview.EasyRecyclerView;
import com.jude.easyrecyclerview.adapter.BaseViewHolder;
import com.jude.easyrecyclerview.adapter.RecyclerArrayAdapter;
import com.lzy.okgo.model.Response;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnLoadmoreListener;
import com.scwang.smartrefresh.layout.listener.OnRefreshListener;
import com.zhuye.zhengmeng.R;
import com.zhuye.zhengmeng.base.BaseActivity;
import com.zhuye.zhengmeng.dynamic.bean.CommentListBean;
import com.zhuye.zhengmeng.dynamic.viewHolder.ErjiCommentListViewHolder;
import com.zhuye.zhengmeng.http.DreamApi;
import com.zhuye.zhengmeng.http.MyCallBack;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

public class ErjiCommentActivity extends BaseActivity implements OnRefreshListener, OnLoadmoreListener {


    @BindView(R.id.recyclerView)
    EasyRecyclerView recyclerView;
    @BindView(R.id.refreshLayout)
    SmartRefreshLayout refreshLayout;
    private String token;
    private RecyclerArrayAdapter<CommentListBean.Data> erjiCommentListAdapter;

    @Override
    protected void processLogic() {

    }

    @Override
    protected void setListener() {
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        token = SPUtils.getInstance("userInfo").getString("token");
        String sen_user_id = getIntent().getStringExtra("sen_user_id");
        String production_id = getIntent().getStringExtra("production_id");
        DreamApi.getSecondCommentList(0x001, token, 1, production_id, sen_user_id, myCallBack);
        refreshLayout.autoRefresh();
        refreshLayout.setOnRefreshListener(this);
        refreshLayout.setOnLoadmoreListener(this);
    }

    @Override
    protected void loadViewLayout() {
        setContentView(R.layout.activity_erji_comment);
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
                            CommentListBean commentListBean = new Gson().fromJson(result.body(), CommentListBean.class);
                            ArrayList<CommentListBean.Data> data = new ArrayList<>();
                            data.addAll(commentListBean.data);

                            initUi(data);
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

    private void initUi(ArrayList<CommentListBean.Data> data) {
        recyclerView.setAdapterWithProgress(erjiCommentListAdapter = new RecyclerArrayAdapter<CommentListBean.Data>(this) {
            @Override
            public BaseViewHolder OnCreateViewHolder(ViewGroup parent, int viewType) {
                return new ErjiCommentListViewHolder(parent);
            }
        });
        erjiCommentListAdapter.setNoMore(R.layout.empty);
        erjiCommentListAdapter.addAll(data);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // TODO: add setContentView(...) invocation
        ButterKnife.bind(this);
    }

    @Override
    public void onRefresh(RefreshLayout refreshlayout) {

    }

    @Override
    public void onLoadmore(RefreshLayout refreshlayout) {

    }
}
