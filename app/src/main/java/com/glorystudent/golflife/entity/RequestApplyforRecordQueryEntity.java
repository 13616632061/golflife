package com.glorystudent.golflife.entity;

/**
 * TODO 查询提现记录实体类
 * Created by Gavin.J on 2017/11/6.
 */

public class RequestApplyforRecordQueryEntity {
    private int ApplyState;

    public RequestApplyforRecordQueryEntity(int applyState) {
        ApplyState = applyState;
    }

    public int getApplyState() {

        return ApplyState;
    }

    public void setApplyState(int applyState) {
        ApplyState = applyState;
    }
}
