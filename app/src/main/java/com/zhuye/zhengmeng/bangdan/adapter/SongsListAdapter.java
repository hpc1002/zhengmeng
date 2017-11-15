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
import com.zhuye.zhengmeng.utils.ToastManager;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by hpc on 2017/11/7.
 */

public class SongsListAdapter extends BaseQuickAdapter<SonglistBean.DataBean, BaseViewHolder> {
    private List<SonglistBean.DataBean> data_list = new ArrayList();

    private String roomId;
    private Context context;
    private String token = SPUtils.getInstance("userInfo").getString("token");

    public SongsListAdapter(int layoutResId, @Nullable List<SonglistBean.DataBean> data, String roomId, Context context) {
        super(layoutResId, data);
        this.data_list = data;
        this.roomId = roomId;
        this.context=context;
    }

    @Override
    public int getItemCount() {
        return data_list.size();
    }

    @Override
    protected void convert(final BaseViewHolder helper, final SonglistBean.DataBean item) {
        helper.setText(R.id.name_song, item.getSong_name());
        helper.setText(R.id.song_size, item.getSong_name());
        helper.addOnClickListener(R.id.btn_yanchang);
        Glide.with(mContext)
                .load(Constant.BASE_URL2 + item.getSong_img())
                .centerCrop()
                .placeholder(R.mipmap.default_img)
                .into((ImageView) helper.getView(R.id.img_song));
    }
}
