package com.glorystudent.golflife.activity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.PopupWindow;
import android.widget.Toast;

import com.glorystudent.golflibrary.base.BaseActivity;
import com.glorystudent.golflibrary.util.GlideUtil;
import com.glorystudent.golflibrary.util.SharedUtil;
import com.glorystudent.golflife.Manifest;
import com.glorystudent.golflife.R;
import com.glorystudent.golflife.api.ConstantsURL;
import com.glorystudent.golflife.api.OkGoRequest;
import com.glorystudent.golflife.api.RequestAPI;
import com.glorystudent.golflife.util.Constants;
import com.glorystudent.golflife.util.TimeUtil;
import com.lzy.okgo.OkGo;
import com.lzy.okgo.callback.StringCallback;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.File;
import java.io.InputStreamReader;
import java.util.Calendar;
import java.util.Date;

import butterknife.Bind;
import butterknife.OnClick;
import okhttp3.Call;
import okhttp3.Response;

/**
 * TODO 设置个人头像页面
 */
public class HeadPortraitActivity extends BaseActivity implements View.OnClickListener, OkGoRequest.OnOkGoUtilListener {

    private final static String TAG = "HeadPortraitActivity";
    private static final int RESULT_CAMERA_ONLY = 100;
    private static final int RESULT_CAMERA_CROP_PATH_RESULT = 301;
    private static final int CROP_PHOTO = 0x1112;
    private Uri imageUri;
    private Uri imageCropUri;
    private PopupWindow window;
    @Bind(R.id.head_portrait_iv)
    public ImageView mImage;
    public Bitmap mbitmap;

    private static final int REQUEST_CAMERA_ACCESS_PERMISSION = 1;
    private static final int REQUEST_WRITE_STORAGE_PERMISSION = 2;
    private String rootUrl = null;//根目录
    private String curFormatDateStr = null;
    private String filePath;//文件路径


    @Override
    protected int getContentId() {
        return R.layout.activity_head_portrait;
    }
    @Override
    protected void init() {

        String head_pic_url = SharedUtil.getString(Constants.HEAD_PORTRAIT);
        if(head_pic_url != null){
            GlideUtil.loadImageView(this, head_pic_url, mImage);
        }else{
            mImage.setImageResource(R.drawable.pic_default_avatar);
        }
        String path = getSDCardPath();
        File file = new File(path + "/temp_"+ System.currentTimeMillis()+ ".png");
        imageUri = Uri.fromFile(file);
        File cropFile = new File(getSDCardPath() + "/temp_crop_" + System.currentTimeMillis() + ".png");
        imageCropUri = Uri.fromFile(cropFile);
    }

    @Subscribe(sticky = true, threadMode = ThreadMode.MAIN)
    public void getBitmap(Bitmap bitmap) {
        Log.d(TAG, "getBitmap: 接收到");
        mImage.setImageBitmap(bitmap);
    }

    /**
     * TODO 点击事件监听
     * @param view
     */
    @OnClick({R.id.back, R.id.tv_more})
    public void onclick(View view) {
        switch (view.getId()) {
            case R.id.back:
                //返回
                finish();
                break;
            case R.id.tv_more:
                //更多
                showPopwindow();
                break;
        }
    }

    /**
     * TODO 显示popupWindow
     */
    private void showPopwindow() {
        // 利用layoutInflater获得View
        LayoutInflater inflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View view = inflater.inflate(R.layout.popu_photo_layout, null);
        Button photograph = (Button) view.findViewById(R.id.photograph);
        Button photo_album = (Button) view.findViewById(R.id.photo_album);
        Button cancel = (Button) view.findViewById(R.id.cancel);
        photograph.setOnClickListener(this);
        photo_album.setOnClickListener(this);
        cancel.setOnClickListener(this);
        // 下面是两种方法得到宽度和高度 getWindow().getDecorView().getWidth()

        window = new PopupWindow(view,
                WindowManager.LayoutParams.MATCH_PARENT,
                WindowManager.LayoutParams.WRAP_CONTENT);
        // 设置popWindow弹出窗体可点击，这句话必须添加，并且是true
        window.setFocusable(true);
        // 实例化一个ColorDrawable颜色为半透明
        ColorDrawable dw = new ColorDrawable(0x80000000);
        window.setBackgroundDrawable(dw);
        // 设置popWindow的显示和消失动画
        window.setAnimationStyle(R.style.mypopwindow_anim_style);
        // 在底部显示
        window.showAtLocation(HeadPortraitActivity.this.findViewById(R.id.tv_more),
                Gravity.BOTTOM, 0, 0);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.photograph:
                window.dismiss();
                //拍照
                checkCameraPermission();
//                takeCameraOnly();
                break;
            case R.id.photo_album:
                window.dismiss();
                //从相册里获取
                intoPhotoAlbum();
                break;
            case R.id.cancel:
                //取消
                window.dismiss();
                break;
        }
    }
    //todo 从相册里选择图片
    private void intoPhotoAlbum(){
        Intent intent = new Intent();
        intent.setAction(Intent.ACTION_GET_CONTENT);
        intent.setType("image/*");
        intent.addCategory(Intent.CATEGORY_OPENABLE);
        startActivityForResult(intent, CROP_PHOTO);
    }
    //TODO 拍照
    private void takeCameraOnly() {
        try {
            Intent intent = null;
            intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);//action is capture
            intent.putExtra("return-data", false);
            intent.putExtra(MediaStore.EXTRA_OUTPUT, imageUri);
            intent.putExtra("outputFormat", Bitmap.CompressFormat.JPEG.toString());
            intent.putExtra("noFaceDetection", true);
            startActivityForResult(intent, RESULT_CAMERA_ONLY);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    /**
     * TODO 开启相机
     */
    private void startCamera() {
        rootUrl = Environment.getExternalStorageDirectory().getPath();
        curFormatDateStr = TimeUtil.getImageNameTime(Calendar.getInstance().getTime());
        filePath = rootUrl + "/golf/camera/" + "IMG_" + curFormatDateStr + ".png";
        File file = new File(filePath);
        if (!file.exists()) {
            File dirs = new File(file.getParent());
            if (!dirs.exists()) {
                dirs.mkdirs();
            }
        }
        imageUri=Uri.fromFile(new File(filePath));
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        intent.putExtra(MediaStore.EXTRA_OUTPUT, imageUri);
        startActivityForResult(intent, RESULT_CAMERA_ONLY);}
    /**
     * TODO 检查相机权限
     */
    private void checkCameraPermission() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M && ActivityCompat.checkSelfPermission(this, android.Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{android.Manifest.permission.CAMERA}, REQUEST_CAMERA_ACCESS_PERMISSION);
        } else {
            checkWriteStoragePermission();
        }
    }

    /**
     * TODO 检查写入SDK权限
     */
    private void checkWriteStoragePermission() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M && ActivityCompat.checkSelfPermission(this, android.Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{android.Manifest.permission.WRITE_EXTERNAL_STORAGE}, REQUEST_WRITE_STORAGE_PERMISSION);
        } else {
            startCamera();
        }
    }
    //TODO 共同调用裁剪
    public void cropImg(Uri uri) {
        Intent intent = new Intent("com.android.camera.action.CROP");
        intent.setDataAndType(uri, "image/*");
        intent.putExtra("crop", "true");
        intent.putExtra("aspectX", 1);
        intent.putExtra("aspectY", 1);
        intent.putExtra("outputX", 700);
        intent.putExtra("outputY", 700);
        intent.putExtra("return-data", false);
        intent.putExtra(MediaStore.EXTRA_OUTPUT, imageCropUri);
        intent.putExtra("outputFormat", Bitmap.CompressFormat.JPEG.toString());
        intent.putExtra("noFaceDetection", true);
        startActivityForResult(intent, RESULT_CAMERA_CROP_PATH_RESULT);
    }

    /**
     * TODO 页面数据返回处理
     * @param requestCode
     * @param resultCode
     * @param data
     */
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode != Activity.RESULT_OK)
            return;
        switch (requestCode) {
            case CROP_PHOTO:
                if(data!=null){
                    Uri uri = data.getData();
                    cropImg(uri);//得到Uri进行裁剪
                }
                break;

            case RESULT_CAMERA_ONLY: {
                cropImg(imageUri);//得到Uri进行裁剪
            }
            break;
            case RESULT_CAMERA_CROP_PATH_RESULT: {
                Bundle extras = data.getExtras();
                try {
                    mbitmap = BitmapFactory.decodeStream(getContentResolver().openInputStream(imageCropUri));
                    showWaiting();
                    new OkGoRequest().setOnOkGoUtilListener(this).upImgDatas(this, mbitmap);//开始上传
                } catch (Exception e) {
                    e.printStackTrace();
                }

            }
            break;
        }
    }
    /**
     * TODO 开启权限结果
     * @param requestCode
     * @param permissions
     * @param grantResults
     */
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (requestCode == REQUEST_CAMERA_ACCESS_PERMISSION) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                checkWriteStoragePermission();
            } else {
                Toast.makeText(this, "相机权限开启失败", Toast.LENGTH_SHORT).show();
            }
        } else if (requestCode == REQUEST_WRITE_STORAGE_PERMISSION) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                startCamera();
            } else {
                Toast.makeText(this, "写入权限开启失败", Toast.LENGTH_SHORT).show();
            }
        } else {
            super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        }
    }
    /**
     * TODO 获得sdk路径
     * @return
     */
    public static String getSDCardPath() {
        String cmd = "cat /proc/mounts";
        Runtime run = Runtime.getRuntime();// 返回与当前 Java 应用程序相关的运行时对象
        try {
            Process p = run.exec(cmd);// 启动另一个进程来执行命令
            BufferedInputStream in = new BufferedInputStream(p.getInputStream());
            BufferedReader inBr = new BufferedReader(new InputStreamReader(in));

            String lineStr;
            while ((lineStr = inBr.readLine()) != null) {
                // 获得命令执行后在控制台的输出信息
                if (lineStr.contains("sdcard")
                        && lineStr.contains(".android_secure")) {
                    String[] strArray = lineStr.split(" ");
                    if (strArray != null && strArray.length >= 5) {
                        String result = strArray[1].replace("/.android_secure","");
                        return result;
                    }
                }
                // 检查命令是否执行失败。
                if (p.waitFor() != 0 && p.exitValue() == 1) {
                    // p.exitValue()==0表示正常结束，1：非正常结束
                }
            }
            inBr.close();
            in.close();
        } catch (Exception e) {

            return Environment.getExternalStorageDirectory().getPath();
        }
        return Environment.getExternalStorageDirectory().getPath();
    }

    /**
     * TODO OkGoRequest().setOnOkGoUtilListene监听
     * @param json
     */
    @Override
    public void parseDatas(String json) {
        try {
            JSONObject jo = new JSONObject(json);
            String statuscode = jo.getString("statuscode");
            String statusmessage = jo.getString("statusmessage");
            if(statuscode.equals("1")){
                mImage.setImageBitmap(mbitmap);
                String failePath = jo.getString("failePath");
                System.out.println("failePath: " +failePath
                        );
                SharedUtil.putString(Constants.HEAD_PORTRAIT, failePath);
                editUser(failePath);
                //将选择好的图片更新到其他页面
                EventBus.getDefault().post(mbitmap);
            }else{
                dismissWaiting();
                Toast.makeText(HeadPortraitActivity.this, "保存失败，错误码:" + statusmessage, Toast.LENGTH_SHORT).show();
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
    @Override
    public void requestFailed() {
        dismissWaiting();
        Toast.makeText(HeadPortraitActivity.this, "网络请求失败", Toast.LENGTH_SHORT).show();
    }

    /**
     * TODO 编辑用户信息 保存头像
     * @param failePath
     */
    private void editUser(String failePath) {
        String request = RequestAPI.editUserHeadPhoto(this, failePath);
        OkGo.post(ConstantsURL.EditUser)
                .tag(this)
                .params("request", request)
                .execute(new StringCallback() {
                    @Override
                    public void onSuccess(String s, Call call, Response response) {
                        System.out.println("保存个人头像; "+s);
                        try {
                            JSONObject jo = new JSONObject(s);
                            String statuscode = jo.getString("statuscode");
                            String statusmessage = jo.getString("statusmessage");
                            if(statuscode.equals("1")){
                                Toast.makeText(HeadPortraitActivity.this, "保存成功", Toast.LENGTH_SHORT).show();
                            }else{
                                Toast.makeText(HeadPortraitActivity.this, "上传头像失败，错误码:" + statusmessage, Toast.LENGTH_SHORT).show();
                                return;
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                        dismissWaiting();
                    }
                    @Override
                    public void onError(Call call, Response response, Exception e) {
                        dismissWaiting();
                        Toast.makeText(HeadPortraitActivity.this, "网络请求失败", Toast.LENGTH_SHORT).show();
                    }
                });
    }
}
