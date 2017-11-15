package com.zhuye.zhengmeng.home.fragment;

import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.blankj.utilcode.util.SPUtils;
import com.google.gson.Gson;
import com.jude.easyrecyclerview.EasyRecyclerView;
import com.jude.easyrecyclerview.adapter.BaseViewHolder;
import com.jude.easyrecyclerview.adapter.RecyclerArrayAdapter;
import com.lzy.okgo.model.Response;
import com.zhuye.zhengmeng.R;
import com.zhuye.zhengmeng.base.BaseFragment;
import com.zhuye.zhengmeng.home.bean.BangDanListBean;
import com.zhuye.zhengmeng.home.viewHolder.BangdanAViewHolder;
import com.zhuye.zhengmeng.home.viewHolder.BangdanBViewHolder;
import com.zhuye.zhengmeng.home.viewHolder.BangdanCViewHolder;
import com.zhuye.zhengmeng.http.DreamApi;
import com.zhuye.zhengmeng.http.MyCallBack;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

/**
 * Created by hpc on 2017/11/7.
 */

public class RankFragment extends BaseFragment {
    @BindView(R.id.recyclerView_a)
    EasyRecyclerView recyclerViewA;
    @BindView(R.id.recyclerView_b)
    EasyRecyclerView recyclerViewB;
    @BindView(R.id.recyclerView_c)
    EasyRecyclerView recyclerViewC;
    Unbinder unbinder;
    private String token;
    private RecyclerArrayAdapter<BangDanListBean.Data> adapterA;
    private RecyclerArrayAdapter<BangDanListBean.Data> adapterB;
    private RecyclerArrayAdapter<BangDanListBean.Data> adapterC;

    @Override
    protected View initView(LayoutInflater inflater, ViewGroup container) {
        return inflater.inflate(R.layout.fragment_rank, container, false);
    }

    @Override
    protected void initListener() {
        token = SPUtils.getInstance("userInfo").getString("token");
        recyclerViewA.setLayoutManager(new LinearLayoutManager(getActivity()));
        recyclerViewB.setLayoutManager(new GridLayoutManager(getActivity(), 2));
        recyclerViewC.setLayoutManager(new GridLayoutManager(getActivity(), 3));
    }

    @Override
    protected void initData() {
        DreamApi.getBangDanList(0x001, token, myCallBack);
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
                            BangDanListBean bangDanListBean = new Gson().fromJson(result.body(), BangDanListBean.class);
                            ArrayList<BangDanListBean.Data> data = new ArrayList<>();
                            data.addAll(bangDanListBean.data);
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

        }
    };

    private void initUi(ArrayList<BangDanListBean.Data> data) {
        List<BangDanListBean.Data> dataA = data.subList(0, 1);
        List<BangDanListBean.Data> dataB = data.subList(1, 9);
        List<BangDanListBean.Data> dataC = data.subList(9, data.size());
        recyclerViewA.setAdapter(adapterA = new RecyclerArrayAdapter<BangDanListBean.Data>(getActivity()) {
            @Override
            public BaseViewHolder OnCreateViewHolder(ViewGroup parent, int viewType) {
                return new BangdanAViewHolder(parent);
            }
        });
        adapterA.addAll(dataA);

//        recyclerViewB.setAdapter(adapterB = new RecyclerArrayAdapter<BangDanListBean.Data>(getActivity()) {
//            @Override
//            public BaseViewHolder OnCreateViewHolder(ViewGroup parent, int viewType) {
//                return new BangdanBViewHolder(parent);
//            }
//        });
        adapterA.addAll(dataB);
        recyclerViewC.setAdapter(adapterC = new RecyclerArrayAdapter<BangDanListBean.Data>(getActivity()) {
            @Override
            public BaseViewHolder OnCreateViewHolder(ViewGroup parent, int viewType) {
                return new BangdanCViewHolder(parent);
            }
        });
        adapterA.addAll(dataC);
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
}
