package com.blazeautomation.connected_ls_sample;

import java.io.Serializable;

public class BlazeDevice implements Serializable {
    public String name;
    public String bOneId;
    public String categoryId;
    public String deviceCreateDate;

    public BlazeDevice() {
    }

    public String getName() {
        return this.name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getbOneId() {
        return this.bOneId;
    }

    public void setbOneId(String bOneId) {
        this.bOneId = bOneId;
    }

    public String getCategoryId() {
        return this.categoryId;
    }

    public void setCategoryId(String categoryId) {
        this.categoryId = categoryId;
    }

    public String getDeviceCreateDate() {
        return this.deviceCreateDate;
    }

    public void setDeviceCreateDate(String deviceCreateDate) {
        this.deviceCreateDate = deviceCreateDate;
    }
}

