package com.zhuye.zhengmeng.shop.fragment;

import android.app.AlertDialog;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.blankj.utilcode.util.SPUtils;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.google.gson.Gson;
import com.lzy.okgo.model.Response;
import com.zhuye.zhengmeng.R;
import com.zhuye.zhengmeng.base.BaseFragment;
import com.zhuye.zhengmeng.base.BaseNoFragment;
import com.zhuye.zhengmeng.http.DreamApi;
import com.zhuye.zhengmeng.http.MyCallBack;
import com.zhuye.zhengmeng.shop.adapter.GiftShopAdapter;
import com.zhuye.zhengmeng.shop.bean.GiftShopBean;
import com.zhuye.zhengmeng.utils.ToastManager;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

/**
 * Created by hpc on 2017/12/3.
 */

public class GiftShopFragment extends BaseNoFragment {
    @BindView(R.id.recyclerView)
    RecyclerView recyclerView;
    Unbinder unbinder;
    private String token;
    private static final String TAG = "GiftShopFragment";

    @Override
    protected View initView(LayoutInflater inflater, ViewGroup container) {
        return inflater.inflate(R.layout.fragment_gift_shop, container, false);
    }

    @Override
    protected void initListener() {
        token = SPUtils.getInstance("userInfo").getString("token");
        DreamApi.getGoodListUrl(0x001, token, myCallBack);
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
                            GiftShopBean giftShopBean = new Gson().fromJson(result.body(), GiftShopBean.class);
                            List<GiftShopBean.Data> data;
                            Log.i(TAG, "onSuccess: " + giftShopBean.data);
                            data = giftShopBean.data;
                            final GiftShopAdapter giftShopAdapter = new GiftShopAdapter(R.layout.item_shop_gift, data);
                            recyclerView.setLayoutManager(new GridLayoutManager(getActivity(), 3));
                            recyclerView.setAdapter(giftShopAdapter);
                            giftShopAdapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
                                @Override
                                public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                                    showDialog(giftShopAdapter.getItem(position).gift_id, giftShopAdapter.getItem(position).gift_price);
                                }
                            });
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
                            String msg = jsonObject.getString("msg");
                            ToastManager.show("兑换成功");
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

    private void showDialog(final String giftId, String price) {
        LayoutInflater li = LayoutInflater.from(getActivity());
        View promptsView = li.inflate(R.layout.buy_dialog, null);

        final AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(
                getActivity());

        // set prompts.xml to alertdialog builder
        alertDialogBuilder.setView(promptsView);
        // create alert dialog
        final AlertDialog alertDialog = alertDialogBuilder.create();
        final TextView goldSum = promptsView.findViewById(R.id.gold_sum);
        final EditText userInput = promptsView.findViewById(R.id.et_number);
        final Button quick_duihuan = promptsView.findViewById(R.id.quick_duihuan);
        String num = userInput.getText().toString();
        final int priceInt = Integer.parseInt(price.substring(0, price.length() - 3));
        goldSum.setText(priceInt + ".00");
        quick_duihuan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String num = userInput.getText().toString();
//                                DreamApi.createChatRoom(0x002, getActivity(), token, userInput.getText().toString(), myCallBack);
                DreamApi.buyGoodUrl(0x002, token, giftId, num, myCallBack);
                alertDialog.dismiss();
            }
        });
        final int numInt = Integer.parseInt(num);
        // set dialog message
        alertDialogBuilder
                .setCancelable(true);


        // show it
        alertDialog.show();
//        WindowManager.LayoutParams params =
//                alertDialog.getWindow().getAttributes();
//        params.width = 800;
//        params.height = 800 ;
//        alertDialog.getWindow().setAttributes(params);
    }

    @Override
    protected void initData() {

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
