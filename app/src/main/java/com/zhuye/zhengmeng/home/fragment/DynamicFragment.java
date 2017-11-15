package com.zhuye.zhengmeng.home.fragment;

import android.content.Intent;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;

import com.blankj.utilcode.util.SPUtils;
import com.blankj.utilcode.util.ToastUtils;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.flyco.dialog.listener.OnOperItemClickL;
import com.flyco.dialog.widget.ActionSheetDialog;
import com.google.gson.Gson;
import com.longsh.optionframelibrary.OptionCenterDialog;
import com.lzy.okgo.model.Response;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnLoadmoreListener;
import com.scwang.smartrefresh.layout.listener.OnRefreshListener;
import com.zhuye.zhengmeng.R;
import com.zhuye.zhengmeng.bangdan.SongsListActivity;
import com.zhuye.zhengmeng.bangdan.recording.QAudioActivity;
import com.zhuye.zhengmeng.bangdan.recording.QVideoActivity;
import com.zhuye.zhengmeng.base.BaseFragment;
import com.zhuye.zhengmeng.dynamic.DynamicDetailActivity;
import com.zhuye.zhengmeng.home.fragment.adapter.DynamicAdapter;
import com.zhuye.zhengmeng.home.fragment.model.DynamicModel;
import com.zhuye.zhengmeng.http.DreamApi;
import com.zhuye.zhengmeng.http.MyCallBack;
import com.zhuye.zhengmeng.view.MyAppTitle;

import java.util.ArrayList;
import java.util.List;

import static com.zhuye.zhengmeng.R.id.song_name;

/**
 * Created by hpc on 2017/10/26.
 * 动态Fragment
 */

public class DynamicFragment extends BaseFragment {

    private static final int CONDITION_LIST_WHAT = 0x006;
    private RecyclerView mRecyclerView;
    private DynamicAdapter dynamicAdapter;

    private SmartRefreshLayout refreshLayout;
    int page = 1;

    @Override
    protected View initView(LayoutInflater inflater, ViewGroup container) {
        View view = inflater.inflate(R.layout.fragment_dynamic, container, false);
        mRecyclerView = view.findViewById(R.id.rv_list);
        refreshLayout = view.findViewById(R.id.refreshLayout);
        return view;
    }

    @Override
    protected void initListener() {
        setDynamicTitle();
        //获取数据
        refreshLayout.autoRefresh();//第一次进入触发自动刷新，演示效果
        initRes();
    }

    @Override
    protected void initData() {

    }

    private void setDynamicTitle() {
        MyAppTitle mNewAppTitle = getActivity().findViewById(R.id.titleBar);
        mNewAppTitle.initViewsVisible(false, true, true, false);
        mNewAppTitle.setAppTitle("动态");
        mNewAppTitle.setTitleSize(20);
        mNewAppTitle.setRightIcon(R.mipmap.add_icon);
        mNewAppTitle.setOnRightButtonClickListener(new MyAppTitle.OnRightButtonClickListener() {
            @Override
            public void OnRightButtonClick(View v) {
                chooseRecordType();
            }
        });
    }

    private void chooseRecordType() {
        final String[] stringItems = {"清唱录制", "伴唱录制"};
        final ActionSheetDialog dialog = new ActionSheetDialog(getActivity(), stringItems, null);
        dialog.title("录制").titleTextSize_SP(14.5f).show();
        dialog.setOnOperItemClickL(new OnOperItemClickL() {
            @Override
            public void onOperItemClick(AdapterView<?> parent, View view, int position, long id) {
                switch (position) {
                    case 0:
                        final ArrayList<String> list = new ArrayList<>();
                        list.add("清唱录音");
                        list.add("清唱视频");
                        final OptionCenterDialog optionCenterDialog = new OptionCenterDialog();
                        optionCenterDialog.show(getActivity(), list);
                        optionCenterDialog.setItemClickListener(new AdapterView.OnItemClickListener() {
                            @Override
                            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                                switch (position){
                                    case 0:
                                        Intent intent1 = new Intent(getActivity(), QAudioActivity.class);
                                        intent1.putExtra("song_id", "");
                                        intent1.putExtra("song_name", song_name);
                                        intent1.putExtra("song_path", "");
                                        intent1.putExtra("type", "1");
                                        intent1.putExtra("song_type", "0");
                                        intent1.putExtra("lyric_path", "");
                                        intent1.putExtra("activity_id", "");
                                        startActivity(intent1);
                                        break;
                                    case 1:
                                        Intent intent = new Intent(getActivity(), QVideoActivity.class);
                                        intent.putExtra("song_id", "");
                                        intent.putExtra("song_name", song_name);
                                        intent.putExtra("song_path", "");
                                        intent.putExtra("type", "2");
                                        intent.putExtra("song_type", "0");
                                        intent.putExtra("lyric_path", "");
                                        intent.putExtra("activity_id", "");
                                        startActivity(intent);
                                        break;
                                }
                                optionCenterDialog.dismiss();
                            }
                        });
                        dialog.dismiss();

                        break;
                    case 1:
                        Intent intent = new Intent(getActivity(), SongsListActivity.class);
                        startActivity(intent);
                        dialog.dismiss();
                        break;
                    default:
                        break;
                }

            }
        });
    }

    private void initRes() {
        refreshLayout.setOnRefreshListener(new OnRefreshListener() {
            @Override
            public void onRefresh(final RefreshLayout refreshlayout) {
                //获取数据
                page = 1;
                String token = SPUtils.getInstance("userInfo").getString("token");
                DreamApi.conditionListNorefresh(CONDITION_LIST_WHAT, token, String.valueOf(page), "0", callBack);
            }
        });
        refreshLayout.setOnLoadmoreListener(new OnLoadmoreListener() {
            @Override
            public void onLoadmore(final RefreshLayout refreshlayout) {
                ++page;
                String token = SPUtils.getInstance("userInfo").getString("token");
                DreamApi.conditionListNorefresh(CONDITION_LIST_WHAT, token, String.valueOf(page), "0", new MyCallBack() {
                    @Override
                    public void onSuccess(int what, Response<String> result) {
                        Gson gson = new Gson();
                        DynamicModel dynamicModel = gson.fromJson(result.body(), DynamicModel.class);
                        List<DynamicModel.DataBean> datas;
                        datas = dynamicModel.getData();
                        if (datas.size() == 0) {
                            refreshLayout.setLoadmoreFinished(true);
                        } else {
                            dynamicAdapter.addData(datas);
                        }
                    }

                    @Override
                    public void onFail(int what, Response<String> result) {
                        dynamicAdapter.setEmptyView(R.layout.empty, mRecyclerView);
                    }

                    @Override
                    public void onFinish(int what) {
                        refreshlayout.finishLoadmore();//解决加载更多一直显示
                        refreshlayout.finishRefresh();
                    }
                });

            }
        });

    }

    /**
     * 接口回调
     */
    MyCallBack callBack = new MyCallBack() {
        @Override
        public void onSuccess(int what, Response<String> result) {
            switch (what) {
                case CONDITION_LIST_WHAT:
                    try {
                        Gson gson = new Gson();
                        DynamicModel dynamicModel = gson.fromJson(result.body(), DynamicModel.class);
                        List<DynamicModel.DataBean> datas;
                        datas = dynamicModel.getData();
                        //创建布局管理
                        final LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity());
                        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
                        mRecyclerView.setLayoutManager(layoutManager);
                        //创建适配器
                        dynamicAdapter = new DynamicAdapter(R.layout.fragment_dynamic_item1, datas, getActivity());
                        //给RecyclerView设置适配器
                        mRecyclerView.setAdapter(dynamicAdapter);
                        dynamicAdapter.openLoadAnimation(BaseQuickAdapter.ALPHAIN);
                        dynamicAdapter.isFirstOnly(false);

                        dynamicAdapter.setCallBack(new DynamicAdapter.allCheck() {
                            @Override
                            public void OnItemClickListener(String id, String urlPath, String name) {
                                Intent intent = new Intent(getActivity(), DynamicDetailActivity.class);
                                intent.putExtra("production_id", id);
                                intent.putExtra("production_path", urlPath);
                                intent.putExtra("production_name", name);
                                startActivity(intent);
                            }
                        });
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    break;
                default:
                    break;
            }
        }

        @Override
        public void onFail(int what, Response<String> result) {
            switch (what) {
                case CONDITION_LIST_WHAT:
                    ToastUtils.showShort(result.body());
                    break;
                default:
                    break;
            }
        }

        @Override
        public void onFinish(int what) {
            switch (what) {
                case CONDITION_LIST_WHAT:
                    refreshLayout.finishRefresh();
                    break;
                default:
                    break;
            }
        }
    };

    @Override
    public void onDestroy() {
        super.onDestroy();
        page = 1;
    }
}
