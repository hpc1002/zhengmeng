package com.zhuye.zhengmeng.bangdan;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.TextView;

import com.blankj.utilcode.util.SPUtils;
import com.bumptech.glide.Glide;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.flyco.dialog.listener.OnOperItemClickL;
import com.flyco.dialog.widget.ActionSheetDialog;
import com.google.gson.Gson;
import com.lzy.okgo.model.Response;
import com.zhuye.zhengmeng.Constant;
import com.zhuye.zhengmeng.R;
import com.zhuye.zhengmeng.bangdan.adapter.CompelistAdapter;
import com.zhuye.zhengmeng.bangdan.bean.CompetitionDetailBean;
import com.zhuye.zhengmeng.bangdan.recording.AudioActivity;
import com.zhuye.zhengmeng.bangdan.recording.QAudioActivity;
import com.zhuye.zhengmeng.bangdan.recording.VideoActivity;
import com.zhuye.zhengmeng.base.BaseActivity;
import com.zhuye.zhengmeng.http.DreamApi;
import com.zhuye.zhengmeng.http.MyCallBack;
import com.zhuye.zhengmeng.view.MyAppTitle;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import butterknife.BindView;


public class CompetitionDetailActivity extends BaseActivity {


    @BindView(R.id.titleBar)
    MyAppTitle titleBar;
    @BindView(R.id.competition_img)
    ImageView competition_img;
    @BindView(R.id.competition_name)
    TextView competition_name;
    @BindView(R.id.see_rules)
    TextView see_rules;
    @BindView(R.id.recyclerView)
    RecyclerView recyclerView;
    @BindView(R.id.own_play)
    TextView own_play;
    private String token;
    private String activity_id;
    @Override
    protected void processLogic() {

    }

    @Override
    protected void setListener() {
        token = SPUtils.getInstance("userInfo").getString("token");
        setTitle();
        recyclerView.setLayoutManager(new LinearLayoutManager(CompetitionDetailActivity.this));
        DreamApi.competitionDetail(0x001, token, new MyCallBack() {
            @Override
            public void onSuccess(int what, Response<String> result) {
                try {
                    JSONObject jsonObject = new JSONObject(result.body());
                    int code = jsonObject.getInt("code");
                    if (code == 200) {
                        CompetitionDetailBean competitionDetailBean = new Gson().fromJson(result.body(), CompetitionDetailBean.class);
                        ArrayList<CompetitionDetailBean.Data.ActiveDetail> active_detail = competitionDetailBean.data.active_detail;
                        ArrayList<CompetitionDetailBean.Data.SongList> song_list = competitionDetailBean.data.song_list;
                        Glide.with(CompetitionDetailActivity.this)
                                .load(Constant.BASE_URL2 + active_detail.get(0).activity_img)
                                .centerCrop()
                                .placeholder(R.mipmap.logo)
                                .into(competition_img);
                        competition_name.setText(active_detail.get(0).activity_title);

                        activity_id = active_detail.get(0).activity_id;
                        final CompelistAdapter compelistAdapter = new CompelistAdapter(R.layout.item_song, song_list);
                        recyclerView.setAdapter(compelistAdapter);
                        compelistAdapter.setOnItemChildClickListener(new BaseQuickAdapter.OnItemChildClickListener() {
                            @Override
                            public void onItemChildClick(BaseQuickAdapter adapter, View view, int position) {
                                switch (view.getId()) {
                                    case R.id.btn_yanchang:
                                        String song_name = compelistAdapter.getItem(position).song_name;
                                        String song_id = compelistAdapter.getItem(position).song_id;
                                        String song_path = compelistAdapter.getItem(position).song_path;
                                        String lyric_path = compelistAdapter.getItem(position).lyric_path;
                                        chooseRecordType(song_id, song_name, song_path, lyric_path,activity_id);
                                        break;
                                }
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
        own_play.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent1 = new Intent(CompetitionDetailActivity.this, QAudioActivity.class);
                intent1.putExtra("song_id", "");
                intent1.putExtra("song_name", "");
                intent1.putExtra("song_path", "");
                intent1.putExtra("type", "2");
                intent1.putExtra("song_type", "1");
                intent1.putExtra("lyric_path", "");
                intent1.putExtra("activity_id", activity_id);
                startActivity(intent1);
            }
        });
    }
    private void chooseRecordType(final String song_id, final String song_name, final String song_path,
                                  final String lyric_path, final String activity_id) {
        final String[] stringItems = {"音频录制", "视频录制"};
        final ActionSheetDialog dialog = new ActionSheetDialog(this, stringItems, null);
        dialog.title("录制").titleTextSize_SP(14.5f).show();
        dialog.setOnOperItemClickL(new OnOperItemClickL() {
            @Override
            public void onOperItemClick(AdapterView<?> parent, View view, int position, long id) {
                switch (position) {
                    case 0:
                        Intent intent = new Intent(CompetitionDetailActivity.this, AudioActivity.class);
                        intent.putExtra("song_id", song_id);
                        intent.putExtra("song_name", song_name);
                        intent.putExtra("song_path", song_path);
                        intent.putExtra("type", "2");
                        intent.putExtra("song_type", "1");
                        intent.putExtra("lyric_path", lyric_path);
                        intent.putExtra("activity_id", activity_id);
                        startActivity(intent);
                        dialog.dismiss();
                        break;
                    case 1:
                        //视频录制
                        Intent intent1 = new Intent(CompetitionDetailActivity.this, VideoActivity.class);
                        intent1.putExtra("song_id", song_id);
                        intent1.putExtra("song_name", song_name);
                        intent1.putExtra("song_path", song_path);
                        intent1.putExtra("type", "1");
                        intent1.putExtra("song_type", "1");
                        intent1.putExtra("lyric_path", lyric_path);
                        intent1.putExtra("activity_id", activity_id);
                        startActivity(intent1);
                        dialog.dismiss();
                        break;
                    default:
                        break;
                }

            }
        });
    }
    private void setTitle() {
        titleBar.initViewsVisible(true, true, false, false);
        titleBar.setAppTitle("比赛详情");
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
