package com.glorystudent.golflife.entity;

import com.chad.library.adapter.base.entity.SectionEntity;

/**
 * Created by Gavin.J on 2017/11/6.
 */

public class ItemMenuEntity<T> extends SectionEntity<T> {
    private T data;
    public ItemMenuEntity(boolean isHeader, String header) {
        super(isHeader, header);
    }

    public ItemMenuEntity(T t) {
        super(t);
        data=t;
    }

    public T getData() {
        return data;
    }
}
