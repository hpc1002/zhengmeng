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
import android.view.View;
import android.widget.AdapterView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.blankj.utilcode.util.SPUtils;
import com.blankj.utilcode.util.ToastUtils;
import com.bumptech.glide.Glide;
import com.flyco.dialog.listener.OnOperItemClickL;
import com.flyco.dialog.widget.ActionSheetDialog;
import com.google.gson.Gson;
import com.lzy.okgo.model.Progress;
import com.lzy.okgo.model.Response;
import com.zhuye.zhengmeng.App;
import com.zhuye.zhengmeng.Constant;
import com.zhuye.zhengmeng.R;
import com.zhuye.zhengmeng.base.BaseActivity;
import com.zhuye.zhengmeng.http.DreamApi;
import com.zhuye.zhengmeng.http.MyCallBack;
import com.zhuye.zhengmeng.http.UploadCallBack;
import com.zhuye.zhengmeng.user.bean.ImageBean;
import com.zhuye.zhengmeng.view.MyAppTitle;
import com.zhuye.zhengmeng.view.camera.CropUtils;
import com.zhuye.zhengmeng.view.camera.DialogPermission;
import com.zhuye.zhengmeng.view.camera.FileUtil;
import com.zhuye.zhengmeng.view.camera.PermissionUtil;
import com.zhuye.zhengmeng.view.camera.SharedPreferenceMark;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import de.hdodenhof.circleimageview.CircleImageView;

import static com.zhuye.zhengmeng.R.id.iduser_change_name;
import static com.zhuye.zhengmeng.R.id.user_avatar;


/**
 * 个人信息
 */
public class UserInfoActivity extends BaseActivity implements View.OnClickListener {


    @BindView(R.id.titleBar)
    MyAppTitle titleBar;

    @BindView(user_avatar)
    CircleImageView userAvatar;
    @BindView(R.id.user_choose_image)
    RelativeLayout userChooseImage;

    private static final int REQUEST_CODE_TAKE_PHOTO = 1;
    private static final int REQUEST_CODE_ALBUM = 2;
    private static final int REQUEST_CODE_CROUP_PHOTO = 3;
    @BindView(R.id.user_signature)
    RelativeLayout userSignature;
    @BindView(R.id.user_sex)
    RelativeLayout userSex;
    @BindView(R.id.user_name)
    TextView userName;
    @BindView(R.id.iduser_change_name)
    RelativeLayout iduserChangeName;
    @BindView(R.id.user_autograph)
    TextView userAutograph;
    @BindView(R.id.user_sex_Tv)
    TextView userSexTv;
    private File file;
    private Uri uri;

    private List<ImageBean.DataBean> datas = new ArrayList<>();

    @Override
    protected void processLogic() {

    }

    @Override
    protected void setListener() {
        userChooseImage.setOnClickListener(this);
        iduserChangeName.setOnClickListener(this);
        userSignature.setOnClickListener(this);
        userSex.setOnClickListener(this);
        setTitle();
        init();//建立相机存储的缓存的路径
    }

    @Override
    protected void loadViewLayout() {
        setContentView(R.layout.activity_user_info);
    }

    @Override
    protected Context getActivityContext() {
        return this;
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
        titleBar.initViewsVisible(true, true, false, false);
        titleBar.setAppTitle("个人信息");
        titleBar.setTitleSize(20);
        titleBar.setOnLeftButtonClickListener(new MyAppTitle.OnLeftButtonClickListener() {
            @Override
            public void onLeftButtonClick(View v) {
                finish();
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        userName.setText(SPUtils.getInstance("userInfo").getString("user_nicename"));
        userAutograph.setText(SPUtils.getInstance("userInfo").getString("user_autograph"));
        userSexTv.setText(SPUtils.getInstance("userInfo").getString("user_sex"));
        String user_avatar = SPUtils.getInstance("userInfo").getString("avatar");
        Glide.with(this).load(Constant.BASE_URL_PINJIE + user_avatar).into(userAvatar);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            //更换头像
            case R.id.user_choose_image:
                chooseType();
//                startActivity(new Intent(UserInfoActivity.this, SelectAvatorActivity.class));
                break;
            case iduser_change_name:
                //更改用户名
                startActivity(new Intent(UserInfoActivity.this, ChangeUserNameActivity.class));
                break;
            case R.id.user_signature:
                startActivity(new Intent(UserInfoActivity.this, PersonSignActivity.class));
                //个人签名
                break;
            case R.id.user_sex:
                //更改性别
                changeSex();
                break;
        }
    }

    private void changeSex() {
        final String[] stringItems = {"保密","男", "女"};
        final String[] stringItemsNumber = {"0","1", "2"};
        final ActionSheetDialog dialog = new ActionSheetDialog(mContext, stringItems, null);
        dialog.title("你是boy还是girl呢").titleTextSize_SP(14.5f).show();

        dialog.setOnOperItemClickL(new OnOperItemClickL() {
            @Override
            public void onOperItemClick(AdapterView<?> parent, View view, final int position, long id) {
                String token = SPUtils.getInstance("userInfo").getString("token");
                DreamApi.changeUserInfo(UserInfoActivity.this, 0x009, token, "2", stringItemsNumber[position], new MyCallBack() {
                    @Override
                    public void onSuccess(int what, Response<String> result) {
                        try {
                            JSONObject jsonObject = new JSONObject(result.body());
                            int code = jsonObject.optInt("code");
                            if (code == 200) {
                                userSexTv.setText(stringItems[position]);
                                ToastUtils.showShort("性别修改成功");
                                SPUtils.getInstance("userInfo").put("user_sex", stringItems[position]);
                            } else {
                                ToastUtils.showShort("性别修改失败");
                            }
                            dialog.dismiss();
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }

                    @Override
                    public void onFail(int what, Response<String> result) {
                        ToastUtils.showShort("性别修改失败");
                        dialog.dismiss();
                    }

                    @Override
                    public void onFinish(int what) {

                    }
                });
            }
        });
    }

    private void chooseType() {
        final String[] stringItems = {"拍照", "从相册选取"};
        final ActionSheetDialog dialog = new ActionSheetDialog(mContext, stringItems, null);
        dialog.title("选择头像").titleTextSize_SP(14.5f).show();

        dialog.setOnOperItemClickL(new OnOperItemClickL() {
            @Override
            public void onOperItemClick(AdapterView<?> parent, View view, int position, long id) {
                switch (position) {
                    //拍照
                    case 0:
                        if (PermissionUtil.hasCameraPermission(UserInfoActivity.this)) {
                            uploadAvatarFromPhotoRequest();
                            dialog.dismiss();
                        }
                        break;
                    //相册
                    case 1:
                        if (PermissionUtil.hasReadExternalStoragePermission(UserInfoActivity.this)) {
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
        Glide.with(this).load(uri).into(userAvatar);
        File file = FileUtil.getFileByUri(uri, this);
        SPUtils.getInstance("userInfo").put("avatar", fileSrc);
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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // TODO: add setContentView(...) invocation
        ButterKnife.bind(this);
    }

    String photo_path = "";
    private UploadCallBack uploadCallBack = new UploadCallBack() {
        @Override
        public void onSuccess(int what, Response<String> result) {
            switch (what) {
                case 0x002:
                    try {
                        JSONObject jsonObject = new JSONObject(result.body());
                        int code = jsonObject.optInt("code");
                        if (code == 200) {
                            ToastUtils.showShort("头像更换成功");

                            String token = SPUtils.getInstance("userInfo").getString("token");
                            DreamApi.getUserInfo(0x003, token, new MyCallBack() {
                                @Override
                                public void onSuccess(int what, Response<String> result) {
                                    try {
                                        JSONObject jsonObject1 = new JSONObject(result.body());
                                        int code1 = jsonObject1.getInt("code");
                                        if (code1==200){
                                            JSONObject data = jsonObject1.getJSONObject("data");
                                            String avatar = data.optString("avatar");
                                            SPUtils.getInstance("userInfo").put("avatar", avatar);//头像url
                                            if (avatar.contains("http")){
                                                Glide.with(UserInfoActivity.this).load(avatar).into(userAvatar);
                                            }else{
                                                Glide.with(UserInfoActivity.this).load(Constant.BASE_URL_PINJIE + avatar).into(userAvatar);
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

                                }
                            });
//                            DreamApi.getAvatorList(UserInfoActivity.this, 0x001, token, new MyCallBack() {
//                                @Override
//                                public void onSuccess(int what, Response<String> result) {
//                                    Gson gson = new Gson();
//                                    ImageBean imageBean = gson.fromJson(result.body(), ImageBean.class);
//                                    if (imageBean.getCode() == 200) {
//                                        datas = imageBean.getData();
//                                        //获取第一张图片为用户头像
//                                        String Photo_id = datas.get(0).getPhoto_id();
//                                        //获取第一张图片photo_path
//                                        photo_path = datas.get(0).getPhoto_path();
//                                        SPUtils.getInstance("userInfo").put("avatar", photo_path);
//                                        Glide.with(UserInfoActivity.this).load(Constant.BASE_URL_PINJIE + photo_path).into(userAvatar);
//                                    }
//                                }
//
//                                @Override
//                                public void onFail(int what, Response<String> result) {
//
//                                }
//
//                                @Override
//                                public void onFinish(int what) {
//
//                                }
//                            });
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
            ToastUtils.showShort("头像更换失败");
        }

        @Override
        public void uploadProgress(Progress progress) {

        }
    };
}
