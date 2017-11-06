package com.glorystudent.golflife.entity;

/**
 * TODO 删除已绑定银行卡请求
 * Created by Gavin.J on 2017/11/6.
 */

public class RequestBlankAccountDeleteEntity {
    private int ID;//提现账户ID

    public RequestBlankAccountDeleteEntity(int ID) {
        this.ID = ID;
    }

    public int getID() {
        return ID;
    }

    public void setID(int ID) {
        this.ID = ID;
    }
}
