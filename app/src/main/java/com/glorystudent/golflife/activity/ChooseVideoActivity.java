package com.glorystudent.golflife.activity;

import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import com.glorystudent.golflibrary.base.BaseActivity;
import com.glorystudent.golflife.R;
import com.glorystudent.golflife.adapter.ChooseVideoListAdapter;
import com.glorystudent.golflife.entity.LocalVideoEntity;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.OnClick;

/**
 * TODO 选择视频
 */
public class ChooseVideoActivity extends BaseActivity implements AdapterView.OnItemClickListener {

    @Bind(R.id.lv_choose_video)
    public ListView lv_choose_video;

    private ChooseVideoListAdapter chooseVideoListAdapter;
    private SQLiteDatabase sqLiteDatabase;
    private List<LocalVideoEntity> datas;

    @Override
    protected int getContentId() {
        return R.layout.activity_choose_video;
    }

    @Override
    protected void init() {
        sqLiteDatabase = SQLiteDatabase.openOrCreateDatabase(getDatabasePath("video.db"), null);
        chooseVideoListAdapter = new ChooseVideoListAdapter(this);
        lv_choose_video.setAdapter(chooseVideoListAdapter);
        lv_choose_video.setOnItemClickListener(this);
        createDatas();
    }

    private void createDatas(){
        datas = new ArrayList<>();
        Cursor cursor = sqLiteDatabase.rawQuery("select * from videoModel", null);
        int count = cursor.getCount();
        if (cursor != null && count > 0) {
            while (cursor.moveToPosition(count - 1)) {
                int id = cursor.getInt(cursor.getColumnIndex("id"));
                String title = cursor.getString(cursor.getColumnIndex("title"));
                String context = cursor.getString(cursor.getColumnIndex("content"));
                String date = cursor.getString(cursor.getColumnIndex("duration"));
                String path = cursor.getString(cursor.getColumnIndex("path"));
                String type = cursor.getString(cursor.getColumnIndex("type"));
                String zippath = cursor.getString(cursor.getColumnIndex("zippath"));
                byte[] in = cursor.getBlob(cursor.getColumnIndex("picBytes"));
                Bitmap bitmap2 = null;
                if (in != null) {
                    bitmap2 = BitmapFactory.decodeByteArray(in, 0, in.length);
                    LocalVideoEntity localVideoEntity = new LocalVideoEntity(id, title, bitmap2, context, date, path, type, zippath);
                    datas.add(localVideoEntity);
                }
                count--;
                if (count < 0) {
                    cursor.close();
                    break;
                }
            }
            chooseVideoListAdapter.setDatas(datas);
        }
    }

    /**
     * TODO 点击事件监听
     * @param view
     */
    @OnClick({R.id.back})
    public void onclick(View view) {
        switch (view.getId()) {
            case R.id.back:
                //返回
                finish();
                break;
        }
    }

    /**
     * TODO 视频列表点击事件
     * @param parent
     * @param view
     * @param position
     * @param id
     */
    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        LocalVideoEntity datas = chooseVideoListAdapter.getDatas(position);
        Intent intent = new Intent();
        intent.putExtra("id", datas.getId());
        setResult(Activity.RESULT_OK, intent);
        finish();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        sqLiteDatabase.close();
    }
}
