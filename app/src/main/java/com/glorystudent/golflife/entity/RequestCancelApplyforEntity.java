package com.glorystudent.golflife.entity;

/**
 * TODO 取消提现申请实体类
 * Created by Gavin.J on 2017/11/6.
 */

public class RequestCancelApplyforEntity {
    private int Id;//:int						申请ID
    private int ApplyState;//:int					取消状态值(4)

    public RequestCancelApplyforEntity(int id, int applyState) {
        Id = id;
        ApplyState = applyState;
    }

    public int getId() {
        return Id;
    }

    public void setId(int id) {
        Id = id;
    }

    public int getApplyState() {
        return ApplyState;
    }

    public void setApplyState(int applyState) {
        ApplyState = applyState;
    }
}
