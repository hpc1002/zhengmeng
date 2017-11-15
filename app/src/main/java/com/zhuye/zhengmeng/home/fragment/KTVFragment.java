package com.zhuye.zhengmeng.home.fragment;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.blankj.utilcode.util.SPUtils;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.google.gson.Gson;
import com.jude.easyrecyclerview.EasyRecyclerView;
import com.lzy.okgo.model.Response;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnLoadmoreListener;
import com.scwang.smartrefresh.layout.listener.OnRefreshListener;
import com.zhuye.zhengmeng.KTV.KtvRoomActivity;
import com.zhuye.zhengmeng.R;
import com.zhuye.zhengmeng.base.BaseFragment;
import com.zhuye.zhengmeng.home.bean.KTVListBean;
import com.zhuye.zhengmeng.home.fragment.adapter.KtvRoomListAdapter;
import com.zhuye.zhengmeng.http.DreamApi;
import com.zhuye.zhengmeng.http.MyCallBack;
import com.zhuye.zhengmeng.utils.ToastManager;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;

import butterknife.BindView;
import butterknife.Unbinder;
import io.rong.imkit.RongIM;
import io.rong.imlib.RongIMClient;

/**
 * Created by hpc on 2017/10/26.
 */

public class KTVFragment extends BaseFragment implements OnRefreshListener, OnLoadmoreListener, View.OnClickListener {
    @BindView(R.id.recyclerView)
    EasyRecyclerView recyclerView;
    @BindView(R.id.search_content)
    EditText searchContent;
    @BindView(R.id.search_tag)
    ImageView searchTag;
    @BindView(R.id.iv_add)
    ImageView ivAdd;
    @BindView(R.id.toolbar)
    Toolbar toolbar;
    Unbinder unbinder;

    @BindView(R.id.refreshLayout)
    SmartRefreshLayout refreshLayout;
    int page = 1;
    private String token;
    private KtvRoomListAdapter ktvListAdapter;
    private static final String TAG = "KTVFragment";
    private String rongcloudToken;
    private String userId;
    String chatroom_id;
    String chatroom_name;
    @Override
    protected View initView(LayoutInflater inflater, ViewGroup container) {
        return inflater.inflate(R.layout.fragment_ktv, container, false);
    }

    @Override
    protected void initListener() {
        rongcloudToken = SPUtils.getInstance("userInfo").getString("rongcloudToken");
        userId = SPUtils.getInstance("userInfo").getString("userId");
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        token = SPUtils.getInstance("userInfo").getString("token");
        refreshLayout.autoRefresh();
        refreshLayout.setOnRefreshListener(this);
        refreshLayout.setOnLoadmoreListener(this);
        ivAdd.setOnClickListener(this);
        final String searchData = searchContent.getText().toString();
        searchContent.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView textView, int actionId, KeyEvent keyEvent) {
                if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                    //执行搜索事件
                    DreamApi.getKtvRoomList(0x001, token, 1, searchData, myCallBack);
                    return true;
                }
                return false;
            }
        });
    }


    @Override
    protected void initData() {

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

                            KTVListBean ktvListBean = new Gson().fromJson(result.body(), KTVListBean.class);
                            List<KTVListBean.Data> chatRooms;
                            chatRooms = ktvListBean.data;

                            initUi(chatRooms);
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
                            ToastManager.show("创建成功");
                            refreshLayout.autoRefresh();
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

    private void initUi(List<KTVListBean.Data> chatRooms) {

        ktvListAdapter = new KtvRoomListAdapter(R.layout.item_ktv, chatRooms, getActivity());
        //给RecyclerView设置适配器
        recyclerView.setAdapter(ktvListAdapter);
        if (chatRooms.size() == 0) {
            ktvListAdapter.setEmptyView(R.layout.empty, recyclerView);
        }
        ktvListAdapter.openLoadAnimation(BaseQuickAdapter.ALPHAIN);
        ktvListAdapter.isFirstOnly(false);
        ktvListAdapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {

                chatroom_id = ktvListAdapter.getItem(position).chatroom_id;
                chatroom_name = ktvListAdapter.getItem(position).chatroom_name;
                Intent intent = new Intent(getActivity(), KtvRoomActivity.class);
                intent.putExtra("roomId",chatroom_id);
                intent.putExtra("chatroom_name",chatroom_name);
                startActivity(intent);
            }
        });
    }




    @Override
    public void onRefresh(RefreshLayout refreshlayout) {
        page = 1;
        DreamApi.getKtvRoomList(0x001, token, page, "", myCallBack);
    }

    @Override
    public void onLoadmore(final RefreshLayout refreshlayout) {
        ++page;
        DreamApi.getKtvRoomList(0x001, token, page, "", new MyCallBack() {
            @Override
            public void onSuccess(int what, Response<String> result) {
                switch (what) {
                    case 0x001:
                        try {
                            JSONObject jsonObject = new JSONObject(result.body());
                            int code = jsonObject.getInt("code");
                            if (code == 200) {

                                KTVListBean ktvListBean = new Gson().fromJson(result.body(), KTVListBean.class);
                                List<KTVListBean.Data> chatRooms;
                                chatRooms = ktvListBean.data;
                                if (chatRooms.size() == 0) {
                                    refreshLayout.setLoadmoreFinished(true);
                                } else {
                                    ktvListAdapter.addData(chatRooms);
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
                refreshlayout.finishLoadmore();//解决加载更多一直显示
                refreshlayout.finishRefresh();
            }
        });
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.iv_add:
                //创建聊天室操作
                // get prompts.xml view
                LayoutInflater li = LayoutInflater.from(getActivity());
                View promptsView = li.inflate(R.layout.prompts, null);

                AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(
                        getActivity());

                // set prompts.xml to alertdialog builder
                alertDialogBuilder.setView(promptsView);

                final EditText userInput = (EditText) promptsView
                        .findViewById(R.id.editTextDialogUserInput);

                // set dialog message
                alertDialogBuilder
                        .setCancelable(false)
                        .setPositiveButton("确定",
                                new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int id) {
                                        DreamApi.createChatRoom(0x002, getActivity(), token, userInput.getText().toString(), myCallBack);

                                    }
                                })
                        .setNegativeButton("取消",
                                new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int id) {
                                        dialog.cancel();
                                    }
                                });

                // create alert dialog
                AlertDialog alertDialog = alertDialogBuilder.create();

                // show it
                alertDialog.show();
                break;
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        page = 1;
    }
}
