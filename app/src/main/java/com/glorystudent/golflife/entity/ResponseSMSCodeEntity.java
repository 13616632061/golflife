package com.glorystudent.golflife.entity;

/**
 * Created by Gavin.J on 2017/10/24.
 */

public class ResponseSMSCodeEntity extends BaseResponseEntity{
    private String phonecodekey;

    public String getPhonecodekey() {
        return phonecodekey;
    }

    public void setPhonecodekey(String phonecodekey) {
        this.phonecodekey = phonecodekey;
    }
}
