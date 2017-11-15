package com.zhuye.zhengmeng.user.adapter;

import android.content.Context;
import android.content.Intent;
import android.view.View;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.zhuye.zhengmeng.Constant;
import com.zhuye.zhengmeng.R;
import com.zhuye.zhengmeng.user.ShowAvatorImageActivity;
import com.zhuye.zhengmeng.user.bean.GridImgBean;
import com.zhuye.zhengmeng.user.bean.ImageBean;

import java.util.List;

/**
 * Created by lilei on 2017/11/2.
 */

public class GetGridImageAdapter extends BaseQuickAdapter<ImageBean.DataBean, BaseViewHolder> {
    private List<GridImgBean> gridImgBeen;
    private Context mContext;

    public GetGridImageAdapter(Context context, int layoutResId, List data) {
        super(layoutResId, data);
        this.gridImgBeen = data;
        this.mContext = context;
    }

    @Override
    public int getItemCount() {
        return super.getItemCount();
    }

    @Override
    public int getEmptyViewCount() {
        return super.getEmptyViewCount();
    }

    @Override
    protected void convert(final BaseViewHolder helper, final ImageBean.DataBean item) {

        //获取当前条目position
        final int position = helper.getLayoutPosition();

//        if (position == 0) {
//            // 加载网络图片
//            Glide.with(mContext).load(R.mipmap.change_head_portrait).into((ImageView) helper.getView(R.id.singleImage));
//        } else {
            // 加载网络图片
            Glide.with(mContext).load(Constant.BASE_URL_PINJIE + item.getPhoto_path()).into((ImageView) helper.getView(R.id.singleImage));
//        }
        helper.getView(R.id.singleImage).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                if (position == 0) {
//                    //调起照相和相册
//                    EventBus.getDefault().post(new CameraAlbunmBean(1));
//                } else {
                    //调到展示图片的Activity里面
                    Intent intent = new Intent(mContext, ShowAvatorImageActivity.class);
                    intent.putExtra("position", position);
                    mContext.startActivity(intent);
//                }
            }
        });


    }
}
