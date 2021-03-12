package com.kcbs.webforum.model.request;


import io.swagger.annotations.ApiModel;

import javax.validation.constraints.Max;

@ApiModel
public class UserInfoReq {
    private String school;
    @Max(value = 2,message = "性别只能为0-女 1-男 2-保密")
    private int sex;
    private String personalWebsite;
    private String wechat;
    private String qq;

    public String getSchool() {
        return school;
    }

    public void setSchool(String school) {
        this.school = school;
    }

    public int getSex() {
        return sex;
    }

    public void setSex(int sex) {
        this.sex = sex;
    }

    public String getPersonalWebsite() {
        return personalWebsite;
    }

    public void setPersonalWebsite(String personalWebsite) {
        this.personalWebsite = personalWebsite;
    }

    public String getWechat() {
        return wechat;
    }

    public void setWechat(String wechat) {
        this.wechat = wechat;
    }

    public String getQq() {
        return qq;
    }

    public void setQq(String qq) {
        this.qq = qq;
    }

    @Override
    public String toString() {
        return "UserInfoReq{" +
                "school='" + school + '\'' +
                ", sex=" + sex +
                ", personalWebsite='" + personalWebsite + '\'' +
                ", wechat='" + wechat + '\'' +
                ", qq='" + qq + '\'' +
                '}';
    }
}
