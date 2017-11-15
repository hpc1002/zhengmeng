package com.zhuye.zhengmeng.user;

import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v4.content.FileProvider;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Toast;

import com.blankj.utilcode.util.SPUtils;
import com.blankj.utilcode.util.ToastUtils;
import com.flyco.dialog.listener.OnOperItemClickL;
import com.flyco.dialog.widget.ActionSheetDialog;
import com.google.gson.Gson;
import com.lzy.okgo.model.Progress;
import com.lzy.okgo.model.Response;
import com.zhuye.zhengmeng.App;
import com.zhuye.zhengmeng.R;
import com.zhuye.zhengmeng.base.BaseActivity;
import com.zhuye.zhengmeng.http.DreamApi;
import com.zhuye.zhengmeng.http.MyCallBack;
import com.zhuye.zhengmeng.http.UploadCallBack;
import com.zhuye.zhengmeng.user.adapter.GetGridImageAdapter;
import com.zhuye.zhengmeng.user.adapter.GridImageAdapter;
import com.zhuye.zhengmeng.user.bean.CameraAlbunmBean;
import com.zhuye.zhengmeng.user.bean.ImageBean;
import com.zhuye.zhengmeng.view.MyAppTitle;
import com.zhuye.zhengmeng.view.camera.CropUtils;
import com.zhuye.zhengmeng.view.camera.DialogPermission;
import com.zhuye.zhengmeng.view.camera.FileUtil;
import com.zhuye.zhengmeng.view.camera.PermissionUtil;
import com.zhuye.zhengmeng.view.camera.SharedPreferenceMark;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class SelectAvatorActivity extends BaseActivity {

    @BindView(R.id.titleBar)
    MyAppTitle titleBar;
    private static final int REQUEST_CODE_TAKE_PHOTO = 1;
    private static final int REQUEST_CODE_ALBUM = 2;
    private static final int REQUEST_CODE_CROUP_PHOTO = 3;
    @BindView(R.id.grid_avator)
    RecyclerView gridAvator;
    private File file;
    private Uri uri;
    private GridImageAdapter gridImageAdapter;
    private GetGridImageAdapter getgridImageAdapter;
    private List<ImageBean.DataBean> datas = new ArrayList<>();
    private static final int CHANGE_AVATAR_WHAT = 0x003;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ButterKnife.bind(this);
        EventBus.getDefault().register(this);
    }

    @Override
    protected void processLogic() {
        //获取头像列表
        String token = SPUtils.getInstance("userInfo").getString("token");
        DreamApi.getAvatorList(this, 0x001, token, callBack);
    }

    @Override
    protected void setListener() {
        setTitle();
        init();//建立相机存储的缓存的路径
    }

    @Override
    protected void loadViewLayout() {
        setContentView(R.layout.activity_select_avator);
    }

    @Override
    protected Context getActivityContext() {
        return null;
    }

    /**
     * 建立相机存储的缓存的路径
     */
    private void init() {
        file = new File(FileUtil.getCachePath(this), "user-avatar.jpg");
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.N) {
            uri = Uri.fromFile(file);
        } else {
            //通过FileProvider创建一个content类型的Uri(android 7.0需要这样的方法跨应用访问)
            uri = FileProvider.getUriForFile(App.getInstance(), "com.zhuye.zhengmeng", file);
        }
    }

    private void setTitle() {
        titleBar.initViewsVisible(true, true, true, false);
        titleBar.setAppTitle("头像相册");
        titleBar.setRightIcon(R.mipmap.qq);
        titleBar.setTitleSize(20);
        titleBar.setOnLeftButtonClickListener(new MyAppTitle.OnLeftButtonClickListener() {
            @Override
            public void onLeftButtonClick(View v) {
                finish();
            }
        });
        titleBar.setOnRightButtonClickListener(new MyAppTitle.OnRightButtonClickListener() {

            @Override
            public void OnRightButtonClick(View v) {
                chooseType();
            }
        });
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEventMainThread(CameraAlbunmBean event) {
        chooseType();
    }

    private void chooseType() {
        final String[] stringItems = {"拍照", "从相册选取"};
        final ActionSheetDialog dialog = new ActionSheetDialog(this, stringItems, null);
        dialog.title("选择头像").titleTextSize_SP(14.5f).show();

        dialog.setOnOperItemClickL(new OnOperItemClickL() {
            @Override
            public void onOperItemClick(AdapterView<?> parent, View view, int position, long id) {
                switch (position) {
                    //拍照
                    case 0:
                        if (PermissionUtil.hasCameraPermission(SelectAvatorActivity.this)) {
                            uploadAvatarFromPhotoRequest();
                            dialog.dismiss();
                        }
                        break;
                    //相册
                    case 1:
                        if (PermissionUtil.hasReadExternalStoragePermission(SelectAvatorActivity.this)) {
                            uploadAvatarFromAlbumRequest();
                            dialog.dismiss();
                        }
                        break;
                    default:
                        break;
                }

            }
        });
    }

    /**
     * photo
     */
    private void uploadAvatarFromPhotoRequest() {
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
        intent.putExtra(MediaStore.Images.Media.ORIENTATION, 0);
        intent.putExtra(MediaStore.EXTRA_OUTPUT, uri);
        startActivityForResult(intent, REQUEST_CODE_TAKE_PHOTO);
    }

    /**
     * album
     */
    private void uploadAvatarFromAlbumRequest() {
        Intent photoPickerIntent = new Intent(Intent.ACTION_PICK);
        photoPickerIntent.setType("image/*");
        startActivityForResult(photoPickerIntent, REQUEST_CODE_ALBUM);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode != -1) {
            return;
        }
        if (requestCode == REQUEST_CODE_ALBUM && data != null) {
            Uri newUri;
            if (Build.VERSION.SDK_INT < Build.VERSION_CODES.N) {
                newUri = Uri.parse("file:///" + CropUtils.getPath(this, data.getData()));
            } else {
                newUri = data.getData();
            }
            if (newUri != null) {
                startPhotoZoom(newUri);
            } else {
                Toast.makeText(this, "没有得到相册图片", Toast.LENGTH_LONG).show();
            }
        } else if (requestCode == REQUEST_CODE_TAKE_PHOTO) {
            startPhotoZoom(uri);
        } else if (requestCode == REQUEST_CODE_CROUP_PHOTO) {
            uploadAvatarFromPhoto();
        }
    }

    private void uploadAvatarFromPhoto() {
        compressAndUploadAvatar(file.getPath());
    }

    /**
     * 压缩加载头像
     *
     * @param fileSrc
     */
    private void compressAndUploadAvatar(String fileSrc) {
        final File cover = FileUtil.getSmallBitmap(this, fileSrc);
        //加载本地图片
        Uri uri = Uri.fromFile(cover);
//        ToastUtils.showShort(uri.toString());
        File file = FileUtil.getFileByUri(uri, this);
        //上传文件
        String token = SPUtils.getInstance("userInfo").getString("token");
        DreamApi.uploadAvator(this, 0x002, token, file, uploadCallBack);
    }


    /**
     * 裁剪拍照裁剪
     *
     * @param uri
     */
    public void startPhotoZoom(Uri uri) {
        Intent intent = new Intent("com.android.camera.action.CROP");
        intent.setDataAndType(uri, "image/*");
        intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
        intent.putExtra("crop", "true");// crop=true 有这句才能出来最后的裁剪页面.
        intent.putExtra("aspectX", 1);// 这两项为裁剪框的比例.
        intent.putExtra("aspectY", 1);// x:y=1:1
//        intent.putExtra("outputX", 400);//图片输出大小
//        intent.putExtra("outputY", 400);
        intent.putExtra("output", Uri.fromFile(file));
        intent.putExtra("outputFormat", "JPEG");// 返回格式
        startActivityForResult(intent, REQUEST_CODE_CROUP_PHOTO);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode) {

            case PermissionUtil.REQUEST_SHOWCAMERA:
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    // Permission Granted，请求拍照
                    uploadAvatarFromPhotoRequest();
                } else {
                    if (!SharedPreferenceMark.getHasShowCamera()) {
                        SharedPreferenceMark.setHasShowCamera(true);
                        new DialogPermission(this, "关闭摄像头权限影响扫描功能");
                    } else {
                        Toast.makeText(this, "未获取摄像头权限", Toast.LENGTH_SHORT).show();
                    }
                }
                break;
            case PermissionUtil.REQUEST_READ_EXTERNAL_STORAGE:
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    // Permission Granted，请求相册
                    uploadAvatarFromAlbumRequest();
                } else {
                    Toast.makeText(this, "没有外部相册请求权限", Toast.LENGTH_SHORT).show();
                }
                break;
            default:
                super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        }
    }

    private UploadCallBack uploadCallBack = new UploadCallBack() {
        @Override
        public void onSuccess(int what, Response<String> result) {
            switch (what) {
                case 0x002:
                    try {
                        JSONObject jsonObject = new JSONObject(result.body());
                        int code = jsonObject.optInt("code");
                        if (code == 200) {
                            //获取头像列表
                            String token = SPUtils.getInstance("userInfo").getString("token");
                            DreamApi.getAvatorList(SelectAvatorActivity.this, 0x001, token, callBack);
                        } else if (code == 109) {
                            ToastUtils.showShort("更换失败");
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
        public void uploadProgress(Progress progress) {

        }
    };

    @Override
    protected void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }


    String photo_path = "";
    private MyCallBack callBack = new MyCallBack() {
        @Override
        public void onSuccess(int what, Response<String> result) {
            switch (what) {
                case 0x001:
                    try {
                        Gson gson = new Gson();
                        ImageBean imageBean = gson.fromJson(result.body(), ImageBean.class);
                        if (imageBean.getCode() == 200) {
                            datas = imageBean.getData();
                            //获取第一张图片为用户头像
                            String Photo_id = datas.get(0).getPhoto_id();
                            //获取第一张图片photo_path
                            photo_path = datas.get(0).getPhoto_path();
                            //创建布局管理
                            LinearLayoutManager layoutManager = new GridLayoutManager(SelectAvatorActivity.this, 3);
                            gridAvator.setLayoutManager(layoutManager);
                            //创建适配器
                            getgridImageAdapter = new GetGridImageAdapter(SelectAvatorActivity.this, R.layout.fragment_user_ziliao, datas);
//                            gridImageAdapter.setEmptyView(R.layout.fragment_user_ziliao, (ViewGroup) gridAvator.getParent());
                            //给RecyclerView设置适配器
                            gridAvator.setAdapter(getgridImageAdapter);
                            getgridImageAdapter.notifyDataSetChanged();
                            //更换用户头像，就是用户新上传成功的这一张
                            String token = SPUtils.getInstance("userInfo").getString("token");
                            DreamApi.changeAvatar(CHANGE_AVATAR_WHAT, token, Photo_id, callBack);
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    break;
                case CHANGE_AVATAR_WHAT:
                    try {
                        JSONObject jsonObject = new JSONObject(result.body());
                        int code = jsonObject.optInt("code");
                        if (code == 200) {
                            SPUtils.getInstance("userInfo").put("avatar", photo_path);//头像
//                            ToastUtils.showShort(jsonObject.optString("msg"));
                        } else if (code == 109) {
//                            ToastUtils.showShort(jsonObject.optString("msg"));
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
}
