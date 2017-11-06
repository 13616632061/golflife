package com.glorystudent.golflife.entity;

/**
 * TODO 提现申请
 * Created by Gavin.J on 2017/11/6.
 */

public class RequestBlankApplyForEntity {
    private int ApplyBankID;//提现账户ID
    private float ApplyMoney;//提现金额

    public RequestBlankApplyForEntity(int applyBankID, float applyMoney) {
        ApplyBankID = applyBankID;
        ApplyMoney = applyMoney;
    }

    public int getApplyBankID() {
        return ApplyBankID;
    }

    public void setApplyBankID(int applyBankID) {
        ApplyBankID = applyBankID;
    }

    public float getApplyMoney() {
        return ApplyMoney;
    }

    public void setApplyMoney(float applyMoney) {
        ApplyMoney = applyMoney;
    }
}
