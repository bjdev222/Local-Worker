package com.lw.localworker.model;

import androidx.room.ColumnInfo;
import androidx.room.PrimaryKey;

import java.io.Serializable;

public class WModel implements Serializable {

    @PrimaryKey(autoGenerate = true)
    public int uid;

    @ColumnInfo(name = "WName")
    String WName;

    @ColumnInfo(name = "WPNumber")
    String WPNumber;

    @ColumnInfo(name = "WWNumber")
    String WWNumber;

    @ColumnInfo(name = "WEmailId")
    String WEmailId;

    @ColumnInfo(name = "WAddress")
    String WAddress;

    @ColumnInfo(name = "WCity")
    String WCity;

    @ColumnInfo(name = "WPincode")
    String WPincode;

    @ColumnInfo(name = "WState")
    String WState;

    @ColumnInfo(name = "WWorkName")
    String WWorkName;

    @ColumnInfo(name = "WDesc")
    String WDesc;

    @ColumnInfo(name = "WSearch")
    String WSearch;

    @ColumnInfo(name = "WProfileUrl")
    String WProfileUrl;

    @ColumnInfo(name = "WGalleryUrl1")
    String WGalleryUrl1;

    @ColumnInfo(name = "WGalleryUrl2")
    String WGalleryUrl2;

    @ColumnInfo(name = "WGalleryUrl3")
    String WGalleryUrl3;

    @ColumnInfo(name = "WGalleryUr4")
    String WGalleryUr4;

    public WModel() {
    }

    public String getWName() {
        return WName;
    }

    public void setWName(String WName) {
        this.WName = WName;
    }

    public String getWPNumber() {
        return WPNumber;
    }

    public void setWPNumber(String WPNumber) {
        this.WPNumber = WPNumber;
    }

    public String getWWNumber() {
        return WWNumber;
    }

    public void setWWNumber(String WWNumber) {
        this.WWNumber = WWNumber;
    }

    public String getWEmailId() {
        return WEmailId;
    }

    public void setWEmailId(String WEmailId) {
        this.WEmailId = WEmailId;
    }

    public String getWAddress() {
        return WAddress;
    }

    public void setWAddress(String WAddress) {
        this.WAddress = WAddress;
    }

    public String getWCity() {
        return WCity;
    }

    public void setWCity(String WCity) {
        this.WCity = WCity;
    }

    public String getWPincode() {
        return WPincode;
    }

    public void setWPincode(String WPincode) {
        this.WPincode = WPincode;
    }

    public String getWState() {
        return WState;
    }

    public void setWState(String WState) {
        this.WState = WState;
    }

    public String getWWorkName() {
        return WWorkName;
    }

    public void setWWorkName(String WWorkName) {
        this.WWorkName = WWorkName;
    }

    public String getWDesc() {
        return WDesc;
    }

    public void setWDesc(String WDesc) {
        this.WDesc = WDesc;
    }

    public String getWSearch() {
        return WSearch;
    }

    public void setWSearch(String WSearch) {
        this.WSearch = WSearch;
    }

    public String getWProfileUrl() {
        return WProfileUrl;
    }

    public void setWProfileUrl(String WProfileUrl) {
        this.WProfileUrl = WProfileUrl;
    }

    public String getWGalleryUrl1() {
        return WGalleryUrl1;
    }

    public void setWGalleryUrl1(String WGalleryUrl1) {
        this.WGalleryUrl1 = WGalleryUrl1;
    }

    public String getWGalleryUrl2() {
        return WGalleryUrl2;
    }

    public void setWGalleryUrl2(String WGalleryUrl2) {
        this.WGalleryUrl2 = WGalleryUrl2;
    }

    public String getWGalleryUrl3() {
        return WGalleryUrl3;
    }

    public void setWGalleryUrl3(String WGalleryUrl3) {
        this.WGalleryUrl3 = WGalleryUrl3;
    }

    public String getWGalleryUr4() {
        return WGalleryUr4;
    }

    public void setWGalleryUr4(String WGalleryUr4) {
        this.WGalleryUr4 = WGalleryUr4;
    }
}
