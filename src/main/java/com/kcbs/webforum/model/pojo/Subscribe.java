package com.kcbs.webforum.model.pojo;

import com.fasterxml.jackson.annotation.JsonFormat;

import java.util.Date;

public class Subscribe {
    private Long id;

    private Long beSubscribe;

    private Long subscribe;

    private Byte isDel;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date updateTime;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getBeSubscribe() {
        return beSubscribe;
    }

    public void setBeSubscribe(Long beSubscribe) {
        this.beSubscribe = beSubscribe;
    }

    public Long getSubscribe() {
        return subscribe;
    }

    public void setSubscribe(Long subscribe) {
        this.subscribe = subscribe;
    }

    public Byte getIsDel() {
        return isDel;
    }

    public void setIsDel(Byte isDel) {
        this.isDel = isDel;
    }

    public Date getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(Date updateTime) {
        this.updateTime = updateTime;
    }
}