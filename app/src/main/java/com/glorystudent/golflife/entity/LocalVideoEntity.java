package com.glorystudent.golflife.entity;

import android.graphics.Bitmap;

import java.io.Serializable;

/**
 * TODO 本地视频实体类
 * Created by Gavin.J on 2017/11/7.
 */

public class LocalVideoEntity implements Serializable {
    private int id;
    private String title;
    private Bitmap bitmap;
    private String content;
    private String date;
    private String path;
    private String type;
    private String zipPath;
    private String time;

    public LocalVideoEntity() {
    }

    public LocalVideoEntity(int id, String title, Bitmap bitmap, String content, String date, String path, String type, String zipPath) {
        this.id = id;
        this.title = title;
        this.bitmap = bitmap;
        this.content = content;
        this.date = date;
        this.path = path;
        this.type = type;
        this.zipPath = zipPath;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getTime() {
        return time;
    }

    public String getZipPath() {
        return zipPath;
    }

    public void setZipPath(String zipPath) {
        this.zipPath = zipPath;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public Bitmap getBitmap() {
        return bitmap;
    }

    public void setBitmap(Bitmap bitmap) {
        this.bitmap = bitmap;
    }

    public String getContent() {
        return content;
    }

    public void setContext(String content) {
        this.content = content;
    }

    @Override
    public String toString() {
        return "LocalVideoEntity{" +
                "id=" + id +
                ", title='" + title + '\'' +
                ", bitmap=" + bitmap +
                ", content='" + content + '\'' +
                ", date='" + date + '\'' +
                ", path='" + path + '\'' +
                ", type='" + type + '\'' +
                ", zipPath='" + zipPath + '\'' +
                ", time='" + time + '\'' +
                '}';
    }
}

