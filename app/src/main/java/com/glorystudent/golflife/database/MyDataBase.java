package com.glorystudent.golflife.database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * 创建数据库
 * Created by hyj on 2017/1/13.
 */
public class MyDataBase extends SQLiteOpenHelper {
    private static MyDataBase sInstance;
    public static final String DATABASE_NAME = "video.db";
    private static final int DATABASE_VERSION = 1;

    public static int version() {
        return DATABASE_VERSION;
    }

    public static MyDataBase getInstance(Context context) {
        if (sInstance == null) {
            sInstance = new MyDataBase(context.getApplicationContext());
        }
        return sInstance;
    }
    public MyDataBase(Context context) {
        //CursorFactory设置为null,使用默认值
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }
    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("create table img(id integer PRIMARY KEY AUTOINCREMENT,fileMd5 String,type String,duration String,date String,path String,zippath String,title String,content String,picBytes BLOB)");
        db.execSQL("create table videoModel(id integer PRIMARY KEY AUTOINCREMENT,fileMd5 String,type String,duration String,date String,path String,zippath String,title String,content String,picBytes BLOB)");
        db.execSQL("create table friends(id integer PRIMARY KEY AUTOINCREMENT,phonenumber String)");
        db.execSQL("create table golffriends(id integer PRIMARY KEY AUTOINCREMENT, phonenumber String, username String)");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }
}
