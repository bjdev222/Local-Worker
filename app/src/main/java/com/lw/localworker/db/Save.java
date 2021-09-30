package com.lw.localworker.db;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity
public class Save {

    @PrimaryKey(autoGenerate = true)
    int id;

    @ColumnInfo(name="WPNumber")
    String WPNumber;

    public Save(String WPNumber) {
        this.WPNumber = WPNumber;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getWPNumber() {
        return WPNumber;
    }

    public void setWPNumber(String WPNumber) {
        this.WPNumber = WPNumber;
    }
}
