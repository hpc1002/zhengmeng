package com.zhuye.zhengmeng.view;


import android.annotation.TargetApi;
import android.content.Context;
import android.os.Build;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.zhuye.zhengmeng.R;

/**
 * App通用titleBar
 **/
public class MyAppTitle extends LinearLayout {
    private OnLeftButtonClickListener mLeftButtonClickListener;
    private OnRightButtonClickListener mRightButtonClickListener;
    private MyViewHolder mViewHolder;
    private View viewAppTitle;

    public MyAppTitle(Context context) {
        super(context);

        init();
    }

    public MyAppTitle(Context context, AttributeSet attrs) {
        super(context, attrs);

        init();
    }

    @TargetApi(Build.VERSION_CODES.HONEYCOMB)
    public MyAppTitle(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);

        init();
    }

    private void init() {
        LayoutInflater inflater = (LayoutInflater) this.getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        LayoutParams layoutParams = new LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
        viewAppTitle = inflater.inflate(R.layout.common_title_bar, null);
        this.addView(viewAppTitle, layoutParams);

        mViewHolder = new MyViewHolder(this);
        mViewHolder.llLeftGoBack.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isFastDoubleClick()) {
                    return;
                }

                if (mLeftButtonClickListener != null) {
                    mLeftButtonClickListener.onLeftButtonClick(v);
                }
            }
        });
        mViewHolder.llRight.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isFastDoubleClick()) {
                    return;
                }

                if (mRightButtonClickListener != null) {
                    mRightButtonClickListener.OnRightButtonClick(v);
                }
            }
        });
    }

    public void initViewsVisible(boolean isLeftButtonVisile, boolean isCenterTitleVisile, boolean isRightIconVisile, boolean isRightTitleVisile) {
        // 左侧返回
        mViewHolder.llLeftGoBack.setVisibility(isLeftButtonVisile ? View.VISIBLE : View.INVISIBLE);

        // 中间标题
        mViewHolder.tvCenterTitle.setVisibility(isCenterTitleVisile ? View.VISIBLE : View.INVISIBLE);

        // 右侧返回图标,文字
        if (!isRightIconVisile && !isRightTitleVisile) {
            mViewHolder.llRight.setVisibility(View.INVISIBLE);
        } else {
            mViewHolder.llRight.setVisibility(View.VISIBLE);
        }
        mViewHolder.ivRightComplete.setVisibility(isRightIconVisile ? View.VISIBLE : View.GONE);
        mViewHolder.tvRightComplete.setVisibility(isRightTitleVisile ? View.VISIBLE : View.INVISIBLE);
    }

    /**
     * 设置标题
     *
     * @param title
     */
    public void setAppTitle(String title) {
        mViewHolder.tvCenterTitle.setText(title);
    }

    public void setLeftIcon(int sourceId) {
        mViewHolder.llLeftGoBack.setImageResource(sourceId);
    }

    public void setTitleSize(float size) {
        mViewHolder.tvCenterTitle.setTextSize(size);
    }

    public void setRightTitle(String text) {
        if (!TextUtils.isEmpty(text)) {
            mViewHolder.tvRightComplete.setText(text);
        }
    }

    public void setRightIcon(int sourceID) {
        mViewHolder.ivRightComplete.setImageResource(sourceID);
    }

    public void setLeftOnclick(OnLeftButtonClickListener mOnLeftButtonClickListener) {
        if (mOnLeftButtonClickListener != null) {
        }
    }

    public void setAppBackground(int color) {
        viewAppTitle.setBackgroundColor(color);
    }

    public void setOnLeftButtonClickListener(OnLeftButtonClickListener listen) {
        mLeftButtonClickListener = listen;
    }

    public void setOnRightButtonClickListener(OnRightButtonClickListener listen) {
        mRightButtonClickListener = listen;
    }

    public interface OnLeftButtonClickListener {
        void onLeftButtonClick(View v);
    }

    public interface OnRightButtonClickListener {
        void OnRightButtonClick(View v);
    }

    static class MyViewHolder {
        ImageView llLeftGoBack;
        TextView tvCenterTitle;
        RelativeLayout llRight;
        ImageView ivRightComplete;
        TextView tvRightComplete;

        public MyViewHolder(View v) {
            llLeftGoBack = v.findViewById(R.id.llLeftGoBack);
            tvCenterTitle = v.findViewById(R.id.tvCenterTitle);
            llRight = v.findViewById(R.id.llRight);
            ivRightComplete = v.findViewById(R.id.ivRightComplete);
            tvRightComplete = v.findViewById(R.id.tvRightComplete);
        }
    }

    // 防止快速点击默认等待时长为900ms
    private long DELAY_TIME = 900;
    private static long lastClickTime;

    private boolean isFastDoubleClick() {
        long time = System.currentTimeMillis();
        long timeD = time - lastClickTime;
        if (0 < timeD && timeD < DELAY_TIME) {
            return true;
        }
        lastClickTime = time;
        return false;
    }

}