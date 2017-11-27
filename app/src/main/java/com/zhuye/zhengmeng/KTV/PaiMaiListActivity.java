package com.zhuye.zhengmeng.KTV;

import android.content.Context;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.view.View;

import com.blankj.utilcode.util.SPUtils;
import com.google.gson.Gson;
import com.jude.easyrecyclerview.EasyRecyclerView;
import com.lzy.okgo.model.Response;
import com.zhuye.zhengmeng.KTV.adapter.PaiMaiAdapter;
import com.zhuye.zhengmeng.KTV.bean.PaiMaiBean;
import com.zhuye.zhengmeng.R;
import com.zhuye.zhengmeng.base.BaseActivity;
import com.zhuye.zhengmeng.http.DreamApi;
import com.zhuye.zhengmeng.http.MyCallBack;
import com.zhuye.zhengmeng.view.MyAppTitle;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class PaiMaiListActivity extends BaseActivity {
    @BindView(R.id.recyclerView)
    EasyRecyclerView recyclerView;
    @BindView(R.id.titleBar)
    MyAppTitle titleBar;
    private String roomId;
    private String token;
    private PaiMaiAdapter paiMaiAdapter;


    @Override
    protected void processLogic() {

    }

    @Override
    protected void setListener() {
        setTitle();
        roomId = getIntent().getStringExtra("roomId");
        token = SPUtils.getInstance("userInfo").getString("token");
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

    private void setTitle() {

        titleBar.initViewsVisible(true, true, false, false);
        titleBar.setTitleSize(20);
        titleBar.setAppTitle("排麦列表");
        titleBar.setOnLeftButtonClickListener(new MyAppTitle.OnLeftButtonClickListener() {
            @Override
            public void onLeftButtonClick(View v) {
                finish();
            }
        });
    }

    private void initUi(List<PaiMaiBean.Data> data) {
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        paiMaiAdapter = new PaiMaiAdapter(R.layout.item_paimai, data);
        recyclerView.setAdapter(paiMaiAdapter);
        if (data.size() == 0) {
            paiMaiAdapter.setEmptyView(R.layout.empty, recyclerView);
        }
    }

    @Override
    protected void loadViewLayout() {
        setContentView(R.layout.activity_pai_mai_list);
    }

    @Override
    protected Context getActivityContext() {
        return this;
    }


}
