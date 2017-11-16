package com.zhuye.zhengmeng.chatRoom.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.blankj.utilcode.util.SPUtils;
import com.google.gson.Gson;
import com.jude.easyrecyclerview.EasyRecyclerView;
import com.lzy.okgo.model.Response;
import com.zhuye.zhengmeng.KTV.MessageEvent;
import com.zhuye.zhengmeng.KTV.adapter.PaiMaiAdapter;
import com.zhuye.zhengmeng.KTV.bean.PaiMaiBean;
import com.zhuye.zhengmeng.R;
import com.zhuye.zhengmeng.http.DreamApi;
import com.zhuye.zhengmeng.http.MyCallBack;
import com.zhuye.zhengmeng.utils.ToastManager;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

/**
 * Created by hpc on 2017/11/15.
 */

public class PaiMaiFragment extends Fragment {
    @BindView(R.id.recyclerView)
    EasyRecyclerView recyclerView;
    Unbinder unbinder;
    private String roomId;
    private String token;
    private PaiMaiAdapter paiMaiAdapter;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.activity_pai_mai, container, false);
        unbinder = ButterKnife.bind(this, view);
        token = SPUtils.getInstance("userInfo").getString("token");
//        roomId = getIntent().getStringExtra("roomId");

        //注册事件
        EventBus.getDefault().register(this);
        return view;
    }
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onMoonEvent(MessageEvent messageEvent){
        roomId=messageEvent.getMessage();
        initView();
    }
    private void initView() {
        DreamApi.getPaiMaiList(0x001, token, roomId, new MyCallBack() {
            @Override
            public void onSuccess(int what, Response<String> result) {
                try {
                    JSONObject jsonObject = new JSONObject(result.body());
                    int code = jsonObject.getInt("code");
                    if (code == 200) {

                        PaiMaiBean paiMaiListBean = new Gson().fromJson(result.body(), PaiMaiBean.class);
                        List<PaiMaiBean.Data> data;
                        data = paiMaiListBean.data;
                        initUi(data);
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

            }
        });
    }

    private void initUi(List<PaiMaiBean.Data> data) {
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        paiMaiAdapter = new PaiMaiAdapter(R.layout.item_paimai, data);
        recyclerView.setAdapter(paiMaiAdapter);
        if (data.size() == 0) {
            paiMaiAdapter.setEmptyView(R.layout.empty, recyclerView);
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
        //取消注册事件
        EventBus.getDefault().unregister(this);
    }
}
