package com.glorystudent.golflife.util;

import android.graphics.Bitmap;
import android.os.Environment;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;

/**
 * TODO 图片工具类
 * Created by Gavin.J on 2017/10/26.
 */

public class ImageUtil {
    /**
     * TODO 保存图片
     *
     * @param mBitmap
     * @param bitName
     */
    public static String saveBitmap(Bitmap mBitmap, String bitName) {
        File f = new File(getSDCardPath() + "/" + bitName + ".png");
        if (!f.exists()) {
            try {
                f.createNewFile();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        FileOutputStream fOut = null;
        try {
            fOut = new FileOutputStream(f);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        mBitmap.compress(Bitmap.CompressFormat.PNG, 100, fOut);
        try {
            fOut.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }
        try {
            fOut.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return f.getPath();
    }

    /**
     * TODO 获取SD卡路径
     * @return
     */
    public static String getSDCardPath() {
        String path = Environment.getExternalStorageDirectory().getPath() + "/golf";
        File file = new File(path);
        if (!file.exists()) {
            file.mkdirs();
        }
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
                        String result = strArray[1].replace("/.android_secure", "");
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
            return Environment.getExternalStorageDirectory().getPath() + "/golf";
        }
        return Environment.getExternalStorageDirectory().getPath() + "/golf";
    }
}
