package com.zhuye.zhengmeng.bangdan;

import android.content.Context;
import android.view.View;

import com.zhuye.zhengmeng.R;
import com.zhuye.zhengmeng.base.BaseActivity;
import com.zhuye.zhengmeng.view.MyAppTitle;

import butterknife.BindView;

public class CompetitionDetailActivity extends BaseActivity {


    @BindView(R.id.titleBar)
    MyAppTitle titleBar;

    @Override
    protected void processLogic() {

    }

    @Override
    protected void setListener() {
        setTitle();
    }

    private void setTitle() {
        titleBar.initViewsVisible(true, true, false, false);
        titleBar.setAppTitle("比赛名称");
        titleBar.setTitleSize(20);
        titleBar.setOnLeftButtonClickListener(new MyAppTitle.OnLeftButtonClickListener() {
            @Override
            public void onLeftButtonClick(View v) {
                finish();
            }
        });
    }

    @Override
    protected void loadViewLayout() {
        setContentView(R.layout.activity_competition_detail);
    }

    @Override
    protected Context getActivityContext() {
        return this;
    }
}
