package com.glorystudent.golflife.activity;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.os.StrictMode;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.annotation.RequiresApi;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.glorystudent.golflibrary.base.BaseActivity;
import com.glorystudent.golflife.R;
import com.glorystudent.golflife.util.ImageUtil;
import com.glorystudent.golflife.util.TakePhotoUtil;
import com.glorystudent.golflife.util.TimeUtil;

import java.io.File;
import java.io.IOException;
import java.util.Calendar;

import butterknife.Bind;
import butterknife.OnClick;

/**
 *TODO 添加图片页面
 */

public class ReleasedAddImageActivity extends BaseActivity {

    private static final String TAG = "AddImageActivity";
    @Bind(R.id.back)
    ImageView back;
    @Bind(R.id.actionbar)
    LinearLayout actionbar;
    @Bind(R.id.ll_photograph)
    LinearLayout photograph;
    @Bind(R.id.ll_add_from_album)
    LinearLayout addFromAlbum;
    @Bind(R.id.competition_banner1)
    ImageView banner1;
    @Bind(R.id.competition_banner2)
    ImageView banner2;
    @Bind(R.id.competition_banner3)
    ImageView banner3;
    @Bind(R.id.competition_banner4)
    ImageView banner4;
    @Bind(R.id.competition_banner5)
    ImageView banner5;
    @Bind(R.id.competition_banner6)
    ImageView banner6;
    @Bind(R.id.competition_banner7)
    ImageView competitionBanner7;
    private static final int LOCAL_IMAGE_CODE = 1;
    private static final int CAMERA_IMAGE_CODE = 2;
    private static final String IMAGE_TYPE = "image/*";
    private static final int REQUEST_CAMERA_ACCESS_PERMISSION = 1;
    private static final int REQUEST_WRITE_STORAGE_PERMISSION = 2;
    private String rootUrl = null;//根目录
    private String curFormatDateStr = null;
    private String filePath;//文件路径

    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN_MR2)
    @Override
    protected int getContentId() {
        return R.layout.activity_released_add_image;
    }

    @Override
    protected void init() {
        rootUrl = Environment.getExternalStorageDirectory().getPath();
    }

    /**
     * TODO 点击事件监听
     * @param view
     */
    @OnClick({R.id.back, R.id.ll_photograph, R.id.ll_add_from_album, R.id.competition_banner1, R.id.competition_banner2, R.id.competition_banner3, R.id.competition_banner4, R.id.competition_banner5, R.id.competition_banner6, R.id.competition_banner7})
    public void onViewClicked(View view) {
        showLoading();
        curFormatDateStr = TimeUtil.getImageNameTime(Calendar.getInstance().getTime());
        String path = rootUrl + "/golf/upload/" + "banner_" + curFormatDateStr + ".png";
        Bitmap bitmap = null;
        Intent data = null;
        switch (view.getId()) {
            case R.id.back:
                finish();
                break;
            case R.id.ll_photograph:
                //拍照获取
                checkCameraPermission();//检查相机权限
                break;
            case R.id.ll_add_from_album:
                //从相册中取
                Intent intent = new Intent();
                 /* 开启Pictures画面Type设定为image */
                intent.setType(IMAGE_TYPE);
                /* 使用Intent.ACTION_GET_CONTENT这个Action */
                intent.setAction(Intent.ACTION_GET_CONTENT);
                /* 取得相片后返回本画面 */
                startActivityForResult(intent, LOCAL_IMAGE_CODE);
                break;
            case R.id.competition_banner1:
                bitmap = BitmapFactory.decodeResource(getResources(), R.drawable.competition_banner_1);
               ImageUtil.saveBitmapFile(bitmap, path);
                data = new Intent();
                data.putExtra("url", path);
                this.setResult(RESULT_OK, data);
                finish();
                break;
            case R.id.competition_banner2:
                bitmap = BitmapFactory.decodeResource(getResources(), R.drawable.competition_banner_2);
               ImageUtil.saveBitmapFile(bitmap, path);
                data = new Intent();
                data.putExtra("url", path);
                this.setResult(RESULT_OK, data);
                finish();
                break;
            case R.id.competition_banner3:
                bitmap = BitmapFactory.decodeResource(getResources(), R.drawable.competition_banner_3);
                ImageUtil.saveBitmapFile(bitmap, path);
                data = new Intent();
                data.putExtra("url", path);
                this.setResult(RESULT_OK, data);
                finish();
                break;
            case R.id.competition_banner4:
                bitmap = BitmapFactory.decodeResource(getResources(), R.drawable.competition_banner_4);
                ImageUtil.saveBitmapFile(bitmap, path);
                data = new Intent();
                data.putExtra("url", path);
                this.setResult(RESULT_OK, data);
                finish();
                break;
            case R.id.competition_banner5:
                bitmap = BitmapFactory.decodeResource(getResources(), R.drawable.competition_banner_5);
                ImageUtil.saveBitmapFile(bitmap, path);
                data = new Intent();
                data.putExtra("url", path);
                this.setResult(RESULT_OK, data);
                finish();
                break;
            case R.id.competition_banner6:
                bitmap = BitmapFactory.decodeResource(getResources(), R.drawable.competition_banner_6);
                ImageUtil.saveBitmapFile(bitmap, path);
                data = new Intent();
                data.putExtra("url", path);
                this.setResult(RESULT_OK, data);
                finish();
                break;
            case R.id.competition_banner7:
                bitmap = BitmapFactory.decodeResource(getResources(), R.drawable.competition_banner_7);
                ImageUtil.saveBitmapFile(bitmap, path);
                data = new Intent();
                data.putExtra("url", path);
                this.setResult(RESULT_OK, data);
                finish();
                break;
        }
        dismissLoading();
    }

    /**
     * TODO 检查相机权限
     */
    private void checkCameraPermission() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M && ActivityCompat.checkSelfPermission(this, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.CAMERA}, REQUEST_CAMERA_ACCESS_PERMISSION);
        } else {
            checkWriteStoragePermission();
        }
    }

    /**
     * TODO 检查写入SDK权限
     */
    private void checkWriteStoragePermission() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M && ActivityCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, REQUEST_WRITE_STORAGE_PERMISSION);
        } else {
            startCamera();
        }
    }
    /**
     * TODO 开启相机
     */
    private void startCamera() {
        filePath = rootUrl + "/golf/camera/" + "IMG_" + curFormatDateStr + ".png";
        File file = new File(filePath);
        if (!file.exists()) {
            File dirs = new File(file.getParent());
            if (!dirs.exists()) {
                dirs.mkdirs();
            }
        }
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        intent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(new File(filePath)));
        startActivityForResult(intent, CAMERA_IMAGE_CODE);

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
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            String url = "";
            if (requestCode == LOCAL_IMAGE_CODE) {
                Uri uri = data.getData();
                Log.i(TAG, "相册uri是：" + uri.toString());
                url =ImageUtil.getRealFilePath(this, uri);
                Log.i(TAG, "本地相册url是：" + url);
                data.putExtra("url", url);
                this.setResult(RESULT_OK, data);
                finish();
            } else if (requestCode == CAMERA_IMAGE_CODE) {
                //拍摄的照片存储位置
//                url = rootUrl + "/golf/camera/" + fileName;
                Log.i(TAG, "相机拍照的url是：" + filePath);
                Intent intent = new Intent();
                intent.putExtra("url", filePath);
                this.setResult(RESULT_OK, intent);
                finish();
            }
        } else {
//            Toast.makeText(this, "没有添加图片", Toast.LENGTH_SHORT).show();
        }
    }
}
