package com.glorystudent.golflife.util;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v4.content.FileProvider;

import com.glorystudent.golflife.activity.FriendChatActivity;

import java.io.File;
import java.io.IOException;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import static android.support.v4.app.ActivityCompat.startActivityForResult;


/**
 * Created by Gavin.J on 2017/11/14.
 */

public class TakePhotoUtil {
    /**
     * TODO 拍照
     */
    public static void startTake(Context context,int takephoto) {
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        //判断是否有相机应用
        if (takePictureIntent.resolveActivity(context.getPackageManager()) != null) {
            //创建临时图片文件
            File photoFile = null;
            try {
                photoFile = createPublicImageFile();
                //设置Action为拍照
                if (photoFile != null) {
                    takePictureIntent.setAction(MediaStore.ACTION_IMAGE_CAPTURE);
                    //这里加入flag
                    takePictureIntent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
                    Uri photoURI = FileProvider.getUriForFile(context, context.getPackageName() + ".fileprovider", photoFile);
                    List<ResolveInfo> resInfoList= context.getPackageManager().queryIntentActivities(takePictureIntent, PackageManager.MATCH_DEFAULT_ONLY);
                    for (ResolveInfo resolveInfo : resInfoList) {
                        String packageName = context.getPackageName();
                        context.grantUriPermission(packageName, photoURI,
                                Intent.FLAG_GRANT_WRITE_URI_PERMISSION);
                    }
                    takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoURI);
                    startActivityForResult((Activity) context,takePictureIntent, takephoto,null);
                }
            } catch (IOException e) {
                e.printStackTrace();
                System.out.println("拍照error："+e.toString());
            }
        }
    }
    /**
     * TODO 创建临时图片文件
     *
     * @return
     * @throws IOException
     */
    public static File createPublicImageFile() throws IOException {
        File path = null;
        if (ImageUtil.hasSdcard()) {
            path = Environment.getExternalStoragePublicDirectory(
                    Environment.DIRECTORY_DCIM);
        }
        Date date = new Date();
        String timeStamp = TimeUtil.getTimelocale(date,"yyyyMMdd_HHmmss", Locale.CHINA);
        String imageFileName = "Camera/" + "IMG_" + timeStamp + ".jpg";
        File image = new File(path, imageFileName);
        return image;
    }

}
