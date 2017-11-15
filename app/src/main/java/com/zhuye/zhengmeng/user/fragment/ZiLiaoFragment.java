package com.zhuye.zhengmeng.user.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.blankj.utilcode.util.SPUtils;
import com.google.gson.Gson;
import com.lzy.okgo.model.Response;
import com.zhuye.zhengmeng.R;
import com.zhuye.zhengmeng.base.BaseFragment;
import com.zhuye.zhengmeng.http.DreamApi;
import com.zhuye.zhengmeng.http.MyCallBack;
import com.zhuye.zhengmeng.user.SelectAvatorActivity;
import com.zhuye.zhengmeng.user.UserInfoActivity;
import com.zhuye.zhengmeng.user.adapter.ChooseImageAdapter;
import com.zhuye.zhengmeng.user.bean.ImageBean;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

/**
 * Created by lilei on 2017/11/1.
 */

public class ZiLiaoFragment extends BaseFragment implements View.OnClickListener {
    Unbinder unbinder;
    @BindView(R.id.choose_ziliao_rl)//编辑资料
            RelativeLayout chooseZiliaoRl;
    @BindView(R.id.ageConstellation)//年龄星座
            TextView ageConstellation;
    @BindView(R.id.count_img)//头像个数
            TextView countImg;
    @BindView(R.id.choose_pic_rl)//选择头像
            RelativeLayout choosePicRl;
    @BindView(R.id.recyclerView_img)
    RecyclerView recyclerViewImg;//头像横线列表

    private ChooseImageAdapter adapter;

    @Override
    protected View initView(LayoutInflater inflater, ViewGroup container) {
        View v = inflater.inflate(R.layout.fragment_ziliao, container, false);
        return v;
    }

    @Override
    protected void initListener() {
        chooseZiliaoRl.setOnClickListener(this);
        choosePicRl.setOnClickListener(this);
        recyclerViewImg.setOnClickListener(this);
    }

    @Override
    protected void initData() {
        //获取头像列表
        String token = SPUtils.getInstance("userInfo").getString("token");
        DreamApi.getAvatorListNoDialog(0x001, token, callBack);
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
            case R.id.choose_ziliao_rl:
                //编辑资料
                startActivity(new Intent(getActivity(), UserInfoActivity.class));
                break;
            case R.id.recyclerView_img:
                startActivity(new Intent(getActivity(), SelectAvatorActivity.class));
                break;
            case R.id.choose_pic_rl:
                //选择头像
                startActivity(new Intent(getActivity(), SelectAvatorActivity.class));
                break;
            default:
                break;
        }
    }

    private MyCallBack callBack = new MyCallBack() {
        @Override
        public void onSuccess(int what, Response<String> result) {
            switch (what) {
                case 0x001:
                    try {
                        List<ImageBean.DataBean> datas;
                        Gson gson = new Gson();
                        ImageBean imageBean = gson.fromJson(result.body(), ImageBean.class);
                        if (imageBean.getCode() == 200) {
                            datas = imageBean.getData();
                            countImg.setText(datas.size() + "");
                            //创建布局管理
                            LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity());
                            layoutManager.setOrientation(LinearLayoutManager.HORIZONTAL);
                            recyclerViewImg.setLayoutManager(layoutManager);
                            //创建适配器
                            adapter = new ChooseImageAdapter(R.layout.fragment_user_ziliao, datas);
                            //给RecyclerView设置适配器
                            adapter.setEmptyView(R.layout.fragment_user_ziliao, (ViewGroup) recyclerViewImg.getParent());
                            recyclerViewImg.setAdapter(adapter);

                        }
                    } catch (Exception e) {
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
    public void onDestroy() {
        super.onDestroy();
    }
}
