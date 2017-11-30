package com.zhuye.zhengmeng.bangdan;

import android.content.Context;
import android.support.v7.widget.LinearLayoutManager;
import android.view.View;

import com.blankj.utilcode.util.SPUtils;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.google.gson.Gson;
import com.jude.easyrecyclerview.EasyRecyclerView;
import com.lzy.okgo.OkGo;
import com.lzy.okgo.callback.StringCallback;
import com.lzy.okgo.model.Response;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnLoadmoreListener;
import com.scwang.smartrefresh.layout.listener.OnRefreshListener;
import com.zhuye.zhengmeng.App;
import com.zhuye.zhengmeng.Constant;
import com.zhuye.zhengmeng.KTV.MessageEvent;
import com.zhuye.zhengmeng.R;
import com.zhuye.zhengmeng.bangdan.adapter.SonglistBean;
import com.zhuye.zhengmeng.bangdan.adapter.SongsList2Adapter;
import com.zhuye.zhengmeng.base.BaseActivity;
import com.zhuye.zhengmeng.http.DreamApi;
import com.zhuye.zhengmeng.http.MyCallBack;
import com.zhuye.zhengmeng.view.MyAppTitle;

import org.greenrobot.eventbus.EventBus;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;

import butterknife.BindView;

public class DianGeListActivity extends BaseActivity {


    @BindView(R.id.titleBar)
    MyAppTitle titleBar;
    @BindView(R.id.recyclerView)
    EasyRecyclerView recyclerView;
    @BindView(R.id.refreshLayout)
    SmartRefreshLayout refreshLayout;
    private String token;
    int page = 1;
    private String roomId;

    private SongsList2Adapter songsListAdapter;

    @Override
    protected void processLogic() {
    }

    @Override
    protected void setListener() {
        token = SPUtils.getInstance("userInfo").getString("token");

        roomId = getIntent().getStringExtra("roomId");
        setTitle();
        initRes();
        refreshLayout.autoRefresh();

    }

    private void initRes() {
        refreshLayout.setOnRefreshListener(new OnRefreshListener() {
            @Override
            public void onRefresh(final RefreshLayout refreshlayout) {
                page = 1;
                DreamApi.getSongsList(0x001, token, page, "", 0, new MyCallBack() {
                    @Override
                    public void onSuccess(int what, Response<String> result) {
                        switch (what) {
                            case 0x001:
                                try {
                                    JSONObject jsonObject = new JSONObject(result.body());
                                    int code = jsonObject.getInt("code");
                                    if (code == 200) {
                                        SonglistBean songsListBean = new Gson().fromJson(result.body(), SonglistBean.class);
                                        List<SonglistBean.DataBean> datas;
                                        datas = songsListBean.getData();
                                        recyclerView.setLayoutManager(new LinearLayoutManager(DianGeListActivity.this));
                                        //创建适配器
                                        songsListAdapter = new SongsList2Adapter(R.layout.item_diange, datas, roomId, DianGeListActivity.this);
                                        //给RecyclerView设置适配器
                                        recyclerView.setAdapter(songsListAdapter);
                                        songsListAdapter.openLoadAnimation(BaseQuickAdapter.ALPHAIN);
                                        songsListAdapter.isFirstOnly(false);
                                        songsListAdapter.setCallBack(new SongsList2Adapter.allCheck() {
                                            @Override
                                            public void OnItemClickListener(String id) {
                                                EventBus.getDefault().post(new MessageEvent(roomId));
                                                finish();

                                            }
                                        });
//
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
                });
            }
        });
        refreshLayout.setOnLoadmoreListener(new OnLoadmoreListener() {
            @Override
            public void onLoadmore(final RefreshLayout refreshlayout) {
                ++page;
                OkGo.<String>post(Constant.SONG_LIST_URL)
                        .tag(this)
                        .params("token", token)
                        .params("page", page)
                        .params("name", "")
                        .params("apartment_id", 0)
                        .execute(new StringCallback() {
                            @Override
                            public void onSuccess(Response<String> response) {
                                try {
                                    JSONObject jsonObject = new JSONObject(response.body());
                                    int code = jsonObject.getInt("code");
                                    if (code == 200) {
                                        SonglistBean songsListBean = new Gson().fromJson(response.body(), SonglistBean.class);
                                        List<SonglistBean.DataBean> datas;
                                        datas = songsListBean.getData();
                                        if (datas == null) {
                                            refreshlayout.setLoadmoreFinished(true);
                                        } else {
                                            songsListAdapter.addData(datas);
                                        }
                                    }
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }
                            }

                            @Override
                            public void onError(Response<String> response) {
                                super.onError(response);

                            }

                            @Override
                            public void onFinish() {
                                super.onFinish();

                            }
                        });
//                DreamApi.getSongsList(0x001, token, page, "", 0, new MyCallBack() {
//                    @Override
//                    public void onSuccess(int what, Response<String> result) {
//                        try {
//                            JSONObject jsonObject = new JSONObject(result.body());
//                            int code = jsonObject.getInt("code");
//                            if (code == 200) {
//                                SonglistBean songsListBean = new Gson().fromJson(result.body(), SonglistBean.class);
//                                List<SonglistBean.DataBean> datas;
//                                datas = songsListBean.getData();
//                                if (datas == null) {
//                                    refreshlayout.setLoadmoreFinished(true);
//                                } else {
//                                    songsListAdapter.addData(datas);
//                                }
//                            }
//                        } catch (JSONException e) {
//                            e.printStackTrace();
//                        }
//                    }
//
//                    @Override
//                    public void onFail(int what, Response<String> result) {
//
//                    }
//
//                    @Override
//                    public void onFinish(int what) {
//                        refreshLayout.finishLoadmore();//解决加载更多一直显示
//                        refreshLayout.finishRefresh();
//                    }
//                });
            }
        });
    }

    private void setTitle() {
        titleBar.initViewsVisible(true, true, false, false);
        titleBar.setAppTitle("点歌");
        titleBar.setTitleSize(20);
        titleBar.setOnLeftButtonClickListener(new MyAppTitle.OnLeftButtonClickListener() {
            @Override
            public void onLeftButtonClick(View v) {
                finish();
            }
        });
    }

    @Override
    protected void loadViewLayout() {
        setContentView(R.layout.activity_songs_list);
    }

    @Override
    protected Context getActivityContext() {
        return this;
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        page = 1;
        OkGo.getInstance().cancelTag(this);
    }
}
