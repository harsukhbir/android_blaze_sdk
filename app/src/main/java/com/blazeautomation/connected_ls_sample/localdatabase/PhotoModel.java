package com.blazeautomation.connected_ls_sample.localdatabase;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

import java.io.Serializable;
@Entity
public class PhotoModel implements Serializable {

    @PrimaryKey(autoGenerate = true)
    private int id;
    @ColumnInfo(name = "id_")
    private String ID;
    @ColumnInfo(name = "model")
    private String model;
    @ColumnInfo(name = "type")
    private String type;
    @ColumnInfo(name = "location")
    private String location;
    @ColumnInfo(name = "installationPhotoUrl")
    private String installationPhotoUrl;
    @ColumnInfo(name = "shipped")
    private String shipped;
    @ColumnInfo(name = "received")
    private String received;
    @ColumnInfo(name = "installed")
    private String installed;
    @ColumnInfo(name = "hubId")
    private String hubId;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getID() {
        return ID;
    }

    public void setID(String ID) {
        this.ID = ID;
    }

    public String getModel() {
        return model;
    }

    public void setModel(String model) {
        this.model = model;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getInstallationPhotoUrl() {
        return installationPhotoUrl;
    }

    public void setInstallationPhotoUrl(String installationPhotoUrl) {
        this.installationPhotoUrl = installationPhotoUrl;
    }

    public String getShipped() {
        return shipped;
    }

    public void setShipped(String shipped) {
        this.shipped = shipped;
    }

    public String getReceived() {
        return received;
    }

    public void setReceived(String received) {
        this.received = received;
    }

    public String getInstalled() {
        return installed;
    }

    public void setInstalled(String installed) {
        this.installed = installed;
    }

    public String getHubId() {
        return hubId;
    }

    public void setHubId(String hubId) {
        this.hubId = hubId;
    }
}
