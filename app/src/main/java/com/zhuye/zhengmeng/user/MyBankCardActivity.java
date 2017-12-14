package com.zhuye.zhengmeng.user;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.support.v7.widget.LinearLayoutManager;
import android.view.Gravity;
import android.view.View;

import com.blankj.utilcode.util.SPUtils;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.flyco.dialog.listener.OnBtnClickL;
import com.flyco.dialog.widget.NormalDialog;
import com.google.gson.Gson;
import com.jude.easyrecyclerview.EasyRecyclerView;
import com.lzy.okgo.model.Response;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnLoadmoreListener;
import com.scwang.smartrefresh.layout.listener.OnRefreshListener;
import com.zhuye.zhengmeng.R;
import com.zhuye.zhengmeng.base.BaseActivity;
import com.zhuye.zhengmeng.http.DreamApi;
import com.zhuye.zhengmeng.http.MyCallBack;
import com.zhuye.zhengmeng.user.adapter.BankCardListAdapter;
import com.zhuye.zhengmeng.user.bean.BankCardListBean;
import com.zhuye.zhengmeng.utils.ToastManager;
import com.zhuye.zhengmeng.view.MyAppTitle;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;

import butterknife.BindView;

public class MyBankCardActivity extends BaseActivity implements OnRefreshListener, OnLoadmoreListener {


    @BindView(R.id.titleBar)
    MyAppTitle titleBar;
    @BindView(R.id.recyclerView)
    EasyRecyclerView recyclerView;
    @BindView(R.id.refreshLayout)
    SmartRefreshLayout refreshLayout;
    private String token;
    private BankCardListAdapter bankCardListAdapter;
    int page = 1;

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
        titleBar.initViewsVisible(true, true, false, true);
        titleBar.setTitleSize(20);
        titleBar.setAppTitle("我的银行卡");
        titleBar.setOnLeftButtonClickListener(new MyAppTitle.OnLeftButtonClickListener() {
            @Override
            public void onLeftButtonClick(View v) {
                finish();
            }
        });
        titleBar.setRightTitle("绑定银行卡");
        titleBar.setOnRightButtonClickListener(new MyAppTitle.OnRightButtonClickListener() {
            @Override
            public void OnRightButtonClick(View v) {
                Intent intent = new Intent(MyBankCardActivity.this, BindCardActivity.class);
                startActivity(intent);
            }
        });
    }

    @Override
    protected void loadViewLayout() {
        setContentView(R.layout.activity_my_bank_card);
    }

    @Override
    protected Context getActivityContext() {
        return this;
    }


    @Override
    public void onRefresh(final RefreshLayout refreshlayout) {
        page = 1;
        DreamApi.getBankCardList(0x001, token, new MyCallBack() {
            @Override
            public void onSuccess(int what, Response<String> result) {
                try {
                    JSONObject jsonObject = new JSONObject(result.body());
                    int code = jsonObject.getInt("code");
                    if (code == 200) {
                        BankCardListBean bankCardListBean = new Gson().fromJson(result.body(), BankCardListBean.class);
                        List<BankCardListBean.Data> data;
                        data = bankCardListBean.data;
                        recyclerView.setLayoutManager(new LinearLayoutManager(MyBankCardActivity.this));

                        bankCardListAdapter = new BankCardListAdapter(R.layout.item_bank_card, data);
                        bankCardListAdapter.setOnItemLongClickListener(new BaseQuickAdapter.OnItemLongClickListener() {
                            @Override
                            public boolean onItemLongClick(BaseQuickAdapter adapter, View view, final int position) {
                                final NormalDialog dialog = new NormalDialog(mContext);
                                dialog.isTitleShow(false)
                                        .bgColor(Color.parseColor("#ffffff"))
                                        .cornerRadius(5)
                                        .content("确定要解绑该银行卡吗")
                                        .contentGravity(Gravity.CENTER)
                                        .contentTextColor(Color.parseColor("#99000000"))
                                        .dividerColor(Color.parseColor("#55000000"))
                                        .btnTextSize(15.5f, 15.5f)//
                                        .btnTextColor(Color.parseColor("#99000000"), Color.parseColor("#CCEA4F05"))
                                        .widthScale(0.85f)
                                        .show();
                                dialog.setOnBtnClickL(
                                        new OnBtnClickL() {
                                            @Override
                                            public void onBtnClick() {
                                                dialog.dismiss();
                                            }
                                        },
                                        new OnBtnClickL() {
                                            @Override
                                            public void onBtnClick() {
                                                dialog.dismiss();

                                                int bank_card_id = bankCardListAdapter.getItem(position).bank_card_id;
                                                DreamApi.deleteBankCard(0x002, token, bank_card_id, new MyCallBack() {
                                                    @Override
                                                    public void onSuccess(int what, Response<String> result) {
                                                        try {
                                                            JSONObject jsonObject = new JSONObject(result.body());
                                                            int code = jsonObject.getInt("code");
                                                            if (code == 200) {
//                                                                ToastManager.show(jsonObject.getString("msg"));
                                                                ToastManager.show("解绑成功");
                                                                refreshlayout.autoRefresh();
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
                                        });

                                return false;
                            }
                        });
                        recyclerView.setAdapter(bankCardListAdapter);
                        if (data == null) {
                            bankCardListAdapter.setEmptyView(R.layout.empty, recyclerView);
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
                refreshlayout.finishLoadmore();
                refreshlayout.finishRefresh();
            }
        });
    }

    @Override
    public void onLoadmore(final RefreshLayout refreshlayout) {
        page++;
        DreamApi.getBankCardList(0x001, token, new MyCallBack() {
            @Override
            public void onSuccess(int what, Response<String> result) {
                try {
                    JSONObject jsonObject = new JSONObject(result.body());
                    int code = jsonObject.getInt("code");
                    if (code == 200) {
                        BankCardListBean bankCardListBean = new Gson().fromJson(result.body(), BankCardListBean.class);
                        List<BankCardListBean.Data> data;
                        data = bankCardListBean.data;
                        if (data == null) {
                            refreshLayout.setLoadmoreFinished(true);
                        } else {
                            bankCardListAdapter.addData(data);
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
                refreshlayout.finishLoadmore();
                refreshlayout.finishRefresh();
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        refreshLayout.autoRefresh();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        page = 1;
    }
}
