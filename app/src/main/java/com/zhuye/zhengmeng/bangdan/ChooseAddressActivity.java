package com.zhuye.zhengmeng.bangdan;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.lzy.okgo.OkGo;
import com.zhuye.zhengmeng.R;
import com.zhuye.zhengmeng.bangdan.adapter.ListSortAdapter;
import com.zhuye.zhengmeng.bangdan.adapter.SortModel;
import com.zhuye.zhengmeng.bangdan.view.SideBar;
import com.zhuye.zhengmeng.base.BaseActivity;
import com.zhuye.zhengmeng.utils.CharacterParser;
import com.zhuye.zhengmeng.utils.PinyinComparator;
import com.zhuye.zhengmeng.view.MyAppTitle;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class ChooseAddressActivity extends BaseActivity {


    @BindView(R.id.country_lvcountry)
    ListView sortListView;
    @BindView(R.id.dialog)
    TextView dialog;
    @BindView(R.id.sidrbar)
    SideBar sideBar;
    @BindView(R.id.titleBar)
    MyAppTitle titleBar;
    private CharacterParser characterParser;
    private List<SortModel> SourceDateList;

    /**
     * 汉字转成拼音的类
     */
    private PinyinComparator pinyinComparator;
    private ListSortAdapter adapter;
    private String[] strings;
    private String[] city;


//    @Override
//    protected void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//
//
////        StatusBarUtil.setTranslucent(this);
//
//    }

    @Override
    protected void processLogic() {

    }

    @Override
    protected void setListener() {
        setTitle();
        initViews();
    }

    private void setTitle() {
        titleBar.initViewsVisible(true, true, false, false);
        titleBar.setAppTitle("选择城市");
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
        setContentView(R.layout.activity_choose_address);
    }

    @Override
    protected Context getActivityContext() {
        return this;
    }


    private void initViews() {
//        实例化汉字转拼音
//        获取characterParser的实例

        characterParser = CharacterParser.getInstance();

        pinyinComparator = new PinyinComparator();

        sideBar.setTextView(dialog);

        //设置右触摸监听
        sideBar.setOnTouchingLetterChangedListener(new SideBar.OnTouchingLetterChangedListener() {

            @Override
            public void onTouchingLetterChanged(String s) {
                //该字母首次出现的位置
                int position = adapter.getPositionForSection(s.charAt(0));
                if (position != -1) {
                    sortListView.setSelection(position);
                }

            }
        });
        View headerView = View.inflate(this, R.layout.address_header, null);
        TextView tv_now = (TextView) headerView.findViewById(R.id.tv_now);

        tv_now.setText(getIntent().getStringExtra("now"));
        tv_now.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(ChooseAddressActivity.this, "当前位置", Toast.LENGTH_SHORT).show();

            }
        });
        TextView tv_hot = (TextView) headerView.findViewById(R.id.tv_hot);
        tv_hot.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                这里要利用adapter.getItem(position)来获取当前position所对应的对象
                Toast.makeText(ChooseAddressActivity.this, "热门位置", Toast.LENGTH_SHORT).show();
            }
        });
        sortListView.addHeaderView(headerView);
        sortListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long id) {
                if (position > 0) {
                    Toast.makeText(getApplication(), ((SortModel) adapter.getItem(position - 1)).getName(), Toast.LENGTH_SHORT).show();
                    SharedPreferences sp = getSharedPreferences("data", MODE_PRIVATE);
                    sp.edit().putString("Location", ((SortModel) adapter.getItem(position - 1)).getName()).commit();
                    finish();
                }
            }
        });
//       OkGo.get(MyApplication.ip+"mobile/indexapi/c").execute(new StringCallback() {
//        @Override
//        public void onSuccess(String s, Call call, Response response) {
//            try {
//                JSONObject jsonObject =  new  JSONObject(s);
//                if (jsonObject.getString("code").equals("402")) {
//
//                    List<CityId> city =  JSON.parseArray(jsonObject.getString("data"), CityId.class);
////                   写不了接口 转成字符串数组
//                   String[]  strings =  new String[]{};
//                    for (int i = 0;i<city.size();i++){
//                        String cityName = city.get(i).getName();
//
//                    }
//                    SourceDateList= filledData(strings);
//                }
//            } catch (JSONException e) {
//                e.printStackTrace();
//            }
//        }
//    });

        SourceDateList = filledData(getResources().getStringArray(R.array.date));
//       根据a-z进行排序源数据
        Collections.sort(SourceDateList, pinyinComparator);
//        创建adapter
        adapter = new ListSortAdapter(this, SourceDateList);
//        给listView设置数据
        sortListView.setAdapter(adapter);


//        mClearEditText.addTextChangedListener(new TextWatcher() {
//
//            @Override
//            public void onTextChanged(CharSequence s, int start, int before, int count) {
//                //µ±ÊäÈë¿òÀïÃæµÄÖµÎª¿Õ£¬¸üÐÂÎªÔ­À´µÄÁÐ±í£¬·ñÔòÎª¹ýÂËÊý¾ÝÁÐ±í
//                filterData(s.toString());
//            }
//
//            @Override
//            public void beforeTextChanged(CharSequence s, int start, int count,
//                                          int after) {
//
//            }
//
//            @Override
//            public void afterTextChanged(Editable s) {
//            }
//        });


    }

    /**
     * ÎªListViewÌî³äÊý¾Ý
     * 填充数据
     *
     * @param date
     * @return
     */
    private List<SortModel> filledData(String[] date) {
        List<SortModel> mSortList = new ArrayList<SortModel>();

        for (int i = 0; i < date.length; i++) {
            SortModel sortModel = new SortModel();
            sortModel.setName(date[i]);
//            汉字转拼音
            String pinyin = characterParser.getSelling(date[i]);
//            转换为大写
            String sortString = pinyin.substring(0, 1).toUpperCase();

//            正则表达式，判断首字母是否是英文字母
            if (sortString.matches("[A-Z]")) {
                sortModel.setSortLetters(sortString.toUpperCase());
            } else {
                sortModel.setSortLetters("#");
            }

            mSortList.add(sortModel);
        }
        return mSortList;

    }

    /**
     * 根据输入框中的值来过滤数据并更新ListView
     *
     * @param filterStr
     */
    private void filterData(String filterStr) {
        List<SortModel> filterDateList = new ArrayList<SortModel>();

        if (TextUtils.isEmpty(filterStr)) {
            filterDateList = SourceDateList;
        } else {
            filterDateList.clear();
            for (SortModel sortModel : SourceDateList) {
                String name = sortModel.getName();
                if (name.indexOf(filterStr.toString()) != -1 || characterParser.getSelling(name).startsWith(filterStr.toString())) {
                    filterDateList.add(sortModel);
                }
            }
        }

        //  根据a-z进行排序
        Collections.sort(filterDateList, pinyinComparator);
        adapter.updateListView(filterDateList);
    }

    @Override
    protected void onDestroy() {

        OkGo.getInstance().cancelTag(this);
        super.onDestroy();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // TODO: add setContentView(...) invocation
        ButterKnife.bind(this);
    }
}
