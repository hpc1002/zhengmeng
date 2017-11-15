package com.zhuye.zhengmeng.bangdan;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.LinearLayoutManager;
import android.view.View;
import android.widget.AdapterView;

import com.blankj.utilcode.util.SPUtils;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.flyco.dialog.listener.OnOperItemClickL;
import com.flyco.dialog.widget.ActionSheetDialog;
import com.google.gson.Gson;
import com.jude.easyrecyclerview.EasyRecyclerView;
import com.lzy.okgo.model.Response;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnLoadmoreListener;
import com.scwang.smartrefresh.layout.listener.OnRefreshListener;
import com.zhuye.zhengmeng.R;
import com.zhuye.zhengmeng.bangdan.adapter.SonglistBean;
import com.zhuye.zhengmeng.bangdan.adapter.SongsListAdapter;
import com.zhuye.zhengmeng.bangdan.recording.AudioActivity;
import com.zhuye.zhengmeng.bangdan.recording.VideoActivity;
import com.zhuye.zhengmeng.base.BaseActivity;
import com.zhuye.zhengmeng.http.DreamApi;
import com.zhuye.zhengmeng.http.MyCallBack;
import com.zhuye.zhengmeng.view.MyAppTitle;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;

import butterknife.BindView;

public class SongsListActivity extends BaseActivity {


    @BindView(R.id.titleBar)
    MyAppTitle titleBar;
    @BindView(R.id.recyclerView)
    EasyRecyclerView recyclerView;
    @BindView(R.id.refreshLayout)
    SmartRefreshLayout refreshLayout;
    private String token;
    int page = 1;

    private SongsListAdapter songsListAdapter;

    @Override
    protected void processLogic() {
    }

    @Override
    protected void setListener() {
        token = SPUtils.getInstance("userInfo").getString("token");
        setTitle();
        refreshLayout.autoRefresh();
        initRes();

    }

    private void initRes() {
        refreshLayout.setOnRefreshListener(new OnRefreshListener() {
            @Override
            public void onRefresh(final RefreshLayout refreshlayout) {
                page = 1;
                DreamApi.getSongsList(0x001, token, page, "", 0, new MyCallBack() {
                    @Override
                    public void onSuccess(int what, Response<String> result) {
                        switch (what) {
                            case 0x001:
                                try {
                                    JSONObject jsonObject = new JSONObject(result.body());
                                    int code = jsonObject.getInt("code");
                                    if (code == 200) {
                                        SonglistBean songsListBean = new Gson().fromJson(result.body(), SonglistBean.class);
                                        List<SonglistBean.DataBean> datas;
                                        datas = songsListBean.getData();
                                        recyclerView.setLayoutManager(new LinearLayoutManager(SongsListActivity.this));
                                        //创建适配器
                                        songsListAdapter = new SongsListAdapter(R.layout.item_song, datas,"",SongsListActivity.this);
                                        //给RecyclerView设置适配器
                                        recyclerView.setAdapter(songsListAdapter);
                                        songsListAdapter.openLoadAnimation(BaseQuickAdapter.ALPHAIN);
                                        songsListAdapter.isFirstOnly(false);
                                        songsListAdapter.setOnItemChildClickListener(new BaseQuickAdapter.OnItemChildClickListener() {
                                            @Override
                                            public void onItemChildClick(BaseQuickAdapter adapter, View view, int position) {
                                                switch (view.getId()) {
                                                    case R.id.btn_yanchang:
                                                        String song_name = songsListAdapter.getItem(position).getSong_name();
                                                        String song_id = songsListAdapter.getItem(position).getSong_id();
                                                        String song_path = songsListAdapter.getItem(position).getSong_path();
                                                        String lyric_path = songsListAdapter.getItem(position).getLyric_path();
                                                        chooseRecordType(song_id, song_name, song_path, lyric_path);
                                                        break;
                                                }
                                            }
                                        });
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
                        refreshLayout.finishLoadmore();//解决加载更多一直显示
                        refreshLayout.finishRefresh();
                    }
                });
            }
        });
        refreshLayout.setOnLoadmoreListener(new OnLoadmoreListener() {
            @Override
            public void onLoadmore(final RefreshLayout refreshlayout) {
                ++page;
                DreamApi.getSongsList(0x001, token, page, "", 0, new MyCallBack() {
                    @Override
                    public void onSuccess(int what, Response<String> result) {
                        try {
                            JSONObject jsonObject = new JSONObject(result.body());
                            int code = jsonObject.getInt("code");
                            if (code == 200) {
                                SonglistBean songsListBean = new Gson().fromJson(result.body(), SonglistBean.class);
                                List<SonglistBean.DataBean> datas;
                                datas = songsListBean.getData();
                                if (datas == null) {
                                    refreshlayout.setLoadmoreFinished(true);
                                } else {
                                    songsListAdapter.addData(datas);
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
                        refreshLayout.finishLoadmore();//解决加载更多一直显示
                        refreshLayout.finishRefresh();
                    }
                });
            }
        });
    }

    private void setTitle() {
        titleBar.initViewsVisible(true, true, false, false);
        titleBar.setAppTitle("歌曲列表");
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
        setContentView(R.layout.activity_songs_list);
    }

    @Override
    protected Context getActivityContext() {
        return this;
    }


    private void chooseRecordType(final String song_id, final String song_name, final String song_path,
                                  final String lyric_path) {
        final String[] stringItems = {"音频录制", "视频录制"};
        final ActionSheetDialog dialog = new ActionSheetDialog(this, stringItems, null);
        dialog.title("录制").titleTextSize_SP(14.5f).show();
        dialog.setOnOperItemClickL(new OnOperItemClickL() {
            @Override
            public void onOperItemClick(AdapterView<?> parent, View view, int position, long id) {
                switch (position) {
                    case 0:
                        Intent intent = new Intent(SongsListActivity.this, AudioActivity.class);
                        intent.putExtra("song_id", song_id);
                        intent.putExtra("song_name", song_name);
                        intent.putExtra("song_path", song_path);
                        intent.putExtra("type", "2");
                        intent.putExtra("song_type", "0");
                        intent.putExtra("lyric_path", lyric_path);
                        intent.putExtra("activity_id", "");
                        startActivity(intent);
                        dialog.dismiss();
                        break;
                    case 1:
                        //视频录制
                        Intent intent1 = new Intent(SongsListActivity.this, VideoActivity.class);
                        intent1.putExtra("song_id", song_id);
                        intent1.putExtra("song_name", song_name);
                        intent1.putExtra("song_path", song_path);
                        intent1.putExtra("type", "1");
                        intent1.putExtra("song_type", "0");
                        intent1.putExtra("lyric_path", lyric_path);
                        intent1.putExtra("activity_id", "");
                        startActivity(intent1);
                        dialog.dismiss();
                        break;
                    default:
                        break;
                }

            }
        });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        page = 1;
    }
}
