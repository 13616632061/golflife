package com.glorystudent.golflife.entity;

import java.util.List;

/**
 * TODO 获取余额相应
 * Created by Gavin.J on 2017/11/3.
 */

public class ResponseQueryBalanceEntity extends BaseResponseEntity {

    /**
     * applyCashInfos : null
     * applyCash : null
     * freezeTotalMoney : 0
     * totalAmount : 100
     * amountHistory : []
     */

    private Object applyCashInfos;
    private Object applyCash;
    private float freezeTotalMoney;
    private float totalAmount;
    private List<?> amountHistory;

    public Object getApplyCashInfos() {
        return applyCashInfos;
    }

    public void setApplyCashInfos(Object applyCashInfos) {
        this.applyCashInfos = applyCashInfos;
    }

    public Object getApplyCash() {
        return applyCash;
    }

    public void setApplyCash(Object applyCash) {
        this.applyCash = applyCash;
    }

    public float getFreezeTotalMoney() {
        return freezeTotalMoney;
    }

    public void setFreezeTotalMoney(float freezeTotalMoney) {
        this.freezeTotalMoney = freezeTotalMoney;
    }

    public float getTotalAmount() {
        return totalAmount;
    }

    public void setTotalAmount(float totalAmount) {
        this.totalAmount = totalAmount;
    }

    public List<?> getAmountHistory() {
        return amountHistory;
    }

    public void setAmountHistory(List<?> amountHistory) {
        this.amountHistory = amountHistory;
    }
}
