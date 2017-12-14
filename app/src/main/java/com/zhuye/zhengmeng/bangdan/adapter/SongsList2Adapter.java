package com.zhuye.zhengmeng.bangdan.adapter;

import android.content.Context;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.ImageView;

import com.blankj.utilcode.util.SPUtils;
import com.bumptech.glide.Glide;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.lzy.okgo.model.Response;
import com.zhuye.zhengmeng.Constant;
import com.zhuye.zhengmeng.R;
import com.zhuye.zhengmeng.http.DreamApi;
import com.zhuye.zhengmeng.http.MyCallBack;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by hpc on 2017/11/7.
 */

public class SongsList2Adapter extends BaseQuickAdapter<SonglistBean.DataBean, BaseViewHolder> {
    private List<SonglistBean.DataBean> data_list = new ArrayList();

    private String roomId;
    private Context context;
    private String token = SPUtils.getInstance("userInfo").getString("token");

    public SongsList2Adapter(int layoutResId, @Nullable List<SonglistBean.DataBean> data, String roomId, Context context) {
        super(layoutResId, data);
        this.data_list = data;
        this.roomId = roomId;
        this.context = context;
    }

    @Override
    public int getItemCount() {
        return data_list.size();
    }

    @Override
    protected void convert(final BaseViewHolder helper, final SonglistBean.DataBean item) {
        helper.setText(R.id.name_song, item.getSong_name());
        helper.setText(R.id.song_size, "共演唱" + item.getSong_click() + "次");
        helper.addOnClickListener(R.id.btn_yanchang);

        helper.getView(R.id.btn_yanchang).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DreamApi.chooseSong(0x002, token, roomId, item.getSong_id(), new MyCallBack() {
                    @Override
                    public void onSuccess(int what, Response<String> result) {
                        JSONObject jsonObject = null;
                        try {
                            jsonObject = new JSONObject(result.body());
                            int code = jsonObject.getInt("code");
                            if (code == 200) {

                                helper.setText(R.id.btn_yanchang, "排麦");
                                helper.getView(R.id.btn_yanchang).setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View view) {
                                        mCallBack.OnItemClickListener(item.getSong_id());
                                    }
                                });
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
        Glide.with(mContext)
                .load(Constant.BASE_URL2 + item.getSong_img())
                .centerCrop()
                .placeholder(R.mipmap.default_img)
                .into((ImageView) helper.getView(R.id.img_song));
    }

    private allCheck mCallBack;

    public void setCallBack(allCheck callBack) {
        mCallBack = callBack;
    }

    public interface allCheck {
        void OnItemClickListener(String id);
    }
}
