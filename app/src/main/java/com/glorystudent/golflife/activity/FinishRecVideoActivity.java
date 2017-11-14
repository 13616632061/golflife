package com.glorystudent.golflife.activity;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Paint;
import android.media.MediaMetadataRetriever;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import com.glorystudent.golflibrary.base.BaseActivity;
import com.glorystudent.golflibrary.util.GlideUtil;
import com.glorystudent.golflife.R;
import com.glorystudent.golflife.entity.LocalVideoEntity;
import com.glorystudent.golflife.util.ImageUtil;

import java.io.File;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * TODO 完成录制页面
 */
public class FinishRecVideoActivity extends BaseActivity {


    @Bind(R.id.back)
    ImageView back;
    @Bind(R.id.iv_rec_vodeo)
    ImageView ivRecVodeo;
    @Bind(R.id.btn_sendfriend)
    Button btnSendfriend;

    private String path;
    private String type;
    private String zippath;
    private int id;
    private SQLiteDatabase sqLiteDatabase;

    @Override
    protected int getContentId() {
        return R.layout.activity_finish_rec_video;
    }

    @Override
    protected void init() {
        Intent intent=getIntent();
        if(intent!=null){
            path=intent.getStringExtra("path");
            //获取第一帧
            try {
                MediaMetadataRetriever media = new MediaMetadataRetriever();
                media.setDataSource(path);
                Bitmap bitmap = media.getFrameAtTime(1);
                long timeMillis = System.currentTimeMillis();
                String sdCardPath = ImageUtil.getSDCardPath();
                File file = ImageUtil.saveFile(bitmap, sdCardPath, timeMillis + ".jpg");
                String imgPath = file.getPath();
                GlideUtil.loadImageView(this, imgPath,ivRecVodeo);
            }catch (Exception e){
            }
            sqLiteDatabase = SQLiteDatabase.openOrCreateDatabase(getDatabasePath("video.db"), null);
            Cursor cursor = sqLiteDatabase.rawQuery("select * from videoModel where path = ?", new String[]{path + ""});
            int count = cursor.getCount();
            if (cursor != null && count > 0) {
                while (cursor.moveToPosition(count - 1)) {
                     id = cursor.getInt(cursor.getColumnIndex("id"));
                    count--;
                    if (count < 0) {
                        cursor.close();
                        break;
                    }
                }
            }
        }
    }

    /**
     * TODO 点击事件监听
     * @param view
     */
    @OnClick({R.id.back,R.id.btn_sendfriend})
    public void onViewClick(View view){
        switch (view.getId()){
            case R.id.back:
                finish();
                break;
            case R.id.btn_sendfriend:
                Intent intent=new Intent(FinishRecVideoActivity.this,MyFriendActivity.class);
                intent.putExtra("id",id);
                startActivity(intent);
                break;
        }
    }
}
