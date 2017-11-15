package com.zhuye.zhengmeng.activity.lanuch;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;

import com.blankj.utilcode.util.SPUtils;
import com.zhuye.zhengmeng.R;
import com.zhuye.zhengmeng.activity.lanuch.viewpagertransforms.DepthPageTransformer;
import com.zhuye.zhengmeng.activity.lanuch.viewpagertransforms.TransformUtil;
import com.zhuye.zhengmeng.activity.register.RegisterActivity;
import com.zhuye.zhengmeng.home.HomeActivity;

import java.util.ArrayList;
import java.util.List;

/**
 * 项目名:   ViewPager
 * 包名:     com.lilei.viewpager
 * 创建者:   lilei
 * 创建时间:  2017/10/11 11:56
 * 描述:     引导界面
 */

public class GuideActivity extends AppCompatActivity {

    private Button startView;
    private ImageView skip_Iv;
    private ViewPager pager;

    private List<ImageView> mImageList;
    private static final int[] imagesArray = new int[]{
            R.mipmap.launcher_01,
            R.mipmap.launcher_02,
            R.mipmap.launcher_03,
    };
    private NavigatorPagerAdapter mPagerAdapter = new NavigatorPagerAdapter();

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_guide);
        pager = (ViewPager) findViewById(R.id.guide_Vp);
        //viewPager切换动画效果
        TransformUtil.forward(pager, new DepthPageTransformer());

        startView = (Button) findViewById(R.id.btn);
        skip_Iv = (ImageView) findViewById(R.id.skip);
        pager.setAdapter(mPagerAdapter);

        mImageList = new ArrayList<>();   //初始化mImageList，防止空指针
        for (int i = 0; i < imagesArray.length; i++) {
            //做出页面
            ImageView imageView = new ImageView(GuideActivity.this);
            imageView.setImageResource(imagesArray[i]);
            imageView.setScaleType(ImageView.ScaleType.FIT_XY);
            mImageList.add(imageView);
        }
//        initMagicIndicator3();  //指示器样式
        viewPagerScroll();
    }

    public void viewPagerScroll() {
        //在页面发生改变的时候回调
        pager.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            //控制按钮的隐藏和显示
            public void onPageSelected(int position) {
                // TODO Auto-generated method stub
                if (position == imagesArray.length - 1) {
                    //最后一页
                    startView.setVisibility(View.VISIBLE);
//                    skip_Iv.setVisibility(View.GONE);
                } else {
                    startView.setVisibility(View.GONE);
//                    skip_Iv.setVisibility(View.VISIBLE);
                }
            }

            @Override
            public void onPageScrolled(int position, float arg1, int arg2) {
                // TODO Auto-generated method stub
            }

            @Override
            public void onPageScrollStateChanged(int arg0) {
                // TODO Auto-generated method stub
            }
        });
    }

    //跳过按钮
    public void skip(View view) {
        SPUtils.getInstance().put("is_guide_show", true);
//        Intent intent = new Intent(this, BottomBarActivity.class);
        Intent intent = new Intent(this, RegisterActivity.class);
        startActivity(intent);
        finish();
    }

    //开始体验按钮
    public void start(View view) {
        SPUtils.getInstance().put("is_guide_show", true);
        String token = SPUtils.getInstance("userInfo").getString("token");
        if (!token.equals("")){
            startActivity(new Intent(this, HomeActivity.class));
        } else {
            Intent intent = new Intent(this, RegisterActivity.class);
            startActivity(intent);
        }
        finish();
    }


    public class NavigatorPagerAdapter extends PagerAdapter {

        //返回ViewPager里面有几页
        public int getCount() {
            // TODO Auto-generated method stub
            return imagesArray.length;
        }

        @Override
        //视图是否由对象
        public boolean isViewFromObject(View view, Object obj) {
            // TODO Auto-generated method stub
            return view == obj;
        }

        //用来生成每一页的试图
        //container每一页的父容器
        //position当前显示的第几页，从0开始
        public Object instantiateItem(ViewGroup container, int position) {

            ImageView imageView = mImageList.get(position);
            container.addView(imageView);
            return imageView;
        }

        //在切换下一页的时候销毁上一页的试图
        public void destroyItem(ViewGroup container, int position, Object object) {

            container.removeView((View) object);

        }
    }
}
