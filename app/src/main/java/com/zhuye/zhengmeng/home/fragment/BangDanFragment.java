package com.zhuye.zhengmeng.home.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.blankj.utilcode.util.SPUtils;
import com.google.gson.Gson;
import com.jude.easyrecyclerview.EasyRecyclerView;
import com.jude.easyrecyclerview.adapter.BaseViewHolder;
import com.jude.easyrecyclerview.adapter.RecyclerArrayAdapter;
import com.lzy.okgo.model.Response;
import com.zhuye.zhengmeng.DataProvider;
import com.zhuye.zhengmeng.R;
import com.zhuye.zhengmeng.bangdan.ChooseAddressActivity;
import com.zhuye.zhengmeng.bangdan.CompetitionActivity;
import com.zhuye.zhengmeng.bangdan.SongsListActivity;
import com.zhuye.zhengmeng.base.BaseNoFragment;
import com.zhuye.zhengmeng.dynamic.DynamicDetail2Activity;
import com.zhuye.zhengmeng.dynamic.DynamicDetailActivity;
import com.zhuye.zhengmeng.home.adapter.BGABannerAdapter;
import com.zhuye.zhengmeng.home.bean.BangDanListBean;
import com.zhuye.zhengmeng.home.bean.BannerDto;
import com.zhuye.zhengmeng.home.viewHolder.BangdanAViewHolder;
import com.zhuye.zhengmeng.home.viewHolder.BangdanBViewHolder;
import com.zhuye.zhengmeng.home.viewHolder.BangdanCViewHolder;
import com.zhuye.zhengmeng.http.DreamApi;
import com.zhuye.zhengmeng.http.MyCallBack;
import com.zhuye.zhengmeng.utils.ToastManager;
import com.zhuye.zhengmeng.widget.SmartTab.SmartTabLayout;
import com.zhuye.zhengmeng.widget.SmartTab.UtilsV4.v4.FragmentPagerItem;
import com.zhuye.zhengmeng.widget.SmartTab.UtilsV4.v4.FragmentPagerItemAdapter;
import com.zhuye.zhengmeng.widget.SmartTab.UtilsV4.v4.FragmentPagerItems;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;
import cn.bingoogolapple.bgabanner.BGABanner;

/**
 * Created by hpc on 2017/10/26.
 */

public class BangDanFragment extends BaseNoFragment implements View.OnClickListener {
    @BindView(R.id.home_recommend_banner)
    BGABanner homeRecommendBanner;
    @BindView(R.id.platform_songs)
    TextView platformSongs;
    @BindView(R.id.platform_competition)
    TextView platformCompetition;
    @BindView(R.id.tv_quanguobang)
    TextView tvQuanguobang;
    @BindView(R.id.tv_quyubang)
    LinearLayout tvQuyubang;
    Unbinder unbinder;
    @BindView(R.id.tv_position)
    TextView tvPosition;
    @BindView(R.id.iv_arrow_down)
    ImageView ivArrowDown;
    @BindView(R.id.tab)
    FrameLayout tab;
    @BindView(R.id.viewpager)
    ViewPager viewpager;
    @BindView(R.id.recyclerViewA)
    EasyRecyclerView recyclerViewA;
    @BindView(R.id.recyclerViewB)
    EasyRecyclerView recyclerViewB;
    @BindView(R.id.recyclerViewC)
    EasyRecyclerView recyclerViewC;
    private int status_area = 1;
    private int status_world = 0;
    private String token;
    private View view;
    private RecyclerArrayAdapter<BangDanListBean.Data> adapterA;
    private RecyclerArrayAdapter<BangDanListBean.Data> adapterB;
    private RecyclerArrayAdapter<BangDanListBean.Data> adapterC;

    @Override
    protected View initView(LayoutInflater inflater, ViewGroup container) {

        view = inflater.inflate(R.layout.fragment_bangdan, container, false);
        return view;
    }

    @Override
    protected void initListener() {
        token = SPUtils.getInstance("userInfo").getString("token");
        tvQuanguobang.setTextColor(getResources().getColor(R.color.red));
        tvPosition.setTextColor(getResources().getColor(R.color.black));
        platformSongs.setOnClickListener(this);
        platformCompetition.setOnClickListener(this);
        tvPosition.setOnClickListener(this);
        tvQuanguobang.setOnClickListener(this);
        recyclerViewA.setLayoutManager(new LinearLayoutManager(getActivity()));
        recyclerViewB.setLayoutManager(new GridLayoutManager(getActivity(), 2));
        recyclerViewC.setLayoutManager(new GridLayoutManager(getActivity(), 3));

        tab.addView(LayoutInflater.from(getActivity()).inflate(R.layout.tab_top_layout, tab, false));
        SmartTabLayout viewPagerTab = view.findViewById(R.id.viewpagertab);
        FragmentPagerItems pages = new FragmentPagerItems(getActivity());
        pages.add(FragmentPagerItem.of("全国榜", RankFragment.class));
        pages.add(FragmentPagerItem.of("郑州榜 ", RankFragment.class));
        FragmentPagerItemAdapter adapter = new FragmentPagerItemAdapter(getChildFragmentManager(), pages);
        viewpager.setAdapter(adapter);
        viewPagerTab.setViewPager(viewpager);
    }

    @Override
    protected void initData() {
        getBannerData();
        DreamApi.getBangDanList(0x001, token, myCallBack);
    }

    private void getBannerData() {
        homeRecommendBanner.setAdapter(new BGABannerAdapter(getActivity()));
        ArrayList<BannerDto> pictures = DataProvider.getPictures();
        List<String> bannerTitle = new ArrayList<String>();
        List<String> bannerImage = new ArrayList<String>();
        for (int i = 0; i < pictures.size(); i++) {
            bannerTitle.add(pictures.get(i).getBannerTitle());
            bannerImage.add(pictures.get(i).getImageUrl());
        }
        homeRecommendBanner.setData(bannerImage, bannerTitle);
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
            case R.id.platform_songs:
                Intent intent = new Intent(getActivity(), SongsListActivity.class);
                startActivity(intent);
                break;
            case R.id.platform_competition:
                startActivity(new Intent(getActivity(), CompetitionActivity.class));
                break;
            case R.id.tv_position:
                ToastManager.show("郑州");
                status_area = 1;
                status_world = 0;
//                tvPosition.setTextColor(getResources().getColor(R.color.red));
//                tvQuanguobang.setTextColor(getResources().getColor(R.color.black));
                if (status_area == 1 && status_world == 0) {

                startActivity(new Intent(getActivity(), ChooseAddressActivity.class));


                }


                ToastManager.show("加载区域数据");
                // TODO: 2017/11/2 加载区域数据


                break;
            case R.id.tv_quanguobang:
                // TODO: 2017/11/2 加载全国数据
                status_area = 0;
                status_world = 1;
                tvQuanguobang.setTextColor(getResources().getColor(R.color.red));
                tvPosition.setTextColor(getResources().getColor(R.color.black));
                DreamApi.getBangDanList(0x001, token, myCallBack);
                break;
        }
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
                            BangDanListBean bangDanListBean = new Gson().fromJson(result.body(), BangDanListBean.class);
                            ArrayList<BangDanListBean.Data> data = new ArrayList<>();
                            data.addAll(bangDanListBean.data);
                            initUi(data);
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

    private void initUi(ArrayList<BangDanListBean.Data> data) {
        List<BangDanListBean.Data> dataA = data.subList(0, 1);
        final List<BangDanListBean.Data> dataB = data.subList(1, 9);
        List<BangDanListBean.Data> dataC = data.subList(9, data.size());
        recyclerViewA.setAdapter(adapterA = new RecyclerArrayAdapter<BangDanListBean.Data>(getActivity()) {
            @Override
            public BaseViewHolder OnCreateViewHolder(ViewGroup parent, int viewType) {
                return new BangdanAViewHolder(parent);
            }
        });
        adapterA.addAll(dataA);
        adapterA.setOnItemClickListener(new RecyclerArrayAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(int position) {
                Intent intent = new Intent(getActivity(), DynamicDetail2Activity.class);
                intent.putExtra("production_id", adapterA.getItem(position).production_id);
                intent.putExtra("production_path", adapterA.getItem(position).production_path);
                intent.putExtra("production_name", adapterA.getItem(position).production_name);
                startActivity(intent);
            }
        });
        recyclerViewB.setAdapter(adapterB = new RecyclerArrayAdapter<BangDanListBean.Data>(getActivity()) {
            @Override
            public BaseViewHolder OnCreateViewHolder(ViewGroup parent, int viewType) {
                return new BangdanBViewHolder(parent);
            }
        });
        adapterB.addAll(dataB);
        adapterB.setOnItemClickListener(new RecyclerArrayAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(int position) {
                Intent intent = new Intent(getActivity(), DynamicDetail2Activity.class);
                intent.putExtra("production_id", adapterB.getItem(position).production_id);
                intent.putExtra("production_path", adapterB.getItem(position).production_path);
                intent.putExtra("production_name", adapterB.getItem(position).production_name);
                startActivity(intent);
            }
        });
        recyclerViewC.setAdapter(adapterC = new RecyclerArrayAdapter<BangDanListBean.Data>(getActivity()) {
            @Override
            public BaseViewHolder OnCreateViewHolder(ViewGroup parent, int viewType) {
                return new BangdanCViewHolder(parent);
            }
        });
        adapterC.addAll(dataC);
        adapterC.setOnItemClickListener(new RecyclerArrayAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(int position) {
                Intent intent = new Intent(getActivity(), DynamicDetail2Activity.class);
                intent.putExtra("production_id", adapterC.getItem(position).production_id);
                intent.putExtra("production_path", adapterC.getItem(position).production_path);
                intent.putExtra("production_name", adapterC.getItem(position).production_name);
                startActivity(intent);
            }
        });
    }
}
