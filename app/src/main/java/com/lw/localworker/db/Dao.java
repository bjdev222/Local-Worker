package com.lw.localworker.db;

import androidx.room.Insert;
import androidx.room.Query;

import com.lw.localworker.model.WModel;

import java.util.List;

@androidx.room.Dao
public interface Dao {

    @Query("SELECT * FROM  save")
    List<Save> getAll();

    @Query("SELECT * FROM save WHERE WPNumber IN (:num)")
    List<Save> loadAllByIds(String num);

    @Insert
    void insertAll(Save... users);

    @Query("DELETE  FROM save WHERE WPNumber IN (:num)")
    Void deleteData(String num);
}
