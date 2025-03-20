package com.example.baiexam.dao;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import com.example.baiexam.model.Sach;

import java.util.List;

@Dao
public interface SachDao {

    @Query("SELECT * FROM sachs")
    List<Sach> getAllSach();

    @Query("SELECT * FROM sachs WHERE idTacGia IN (:tacGiaId)")
    List<Sach> getAllByTacGiaId(int tacGiaId);

    @Query("SELECT * FROM sachs WHERE idSach IN (:sachId)")
    Sach loadSachById(int sachId);

    @Insert
    void insertSach(Sach sach);

    @Update
    void updateSach(Sach sach);
    @Delete
    void deleteSach(Sach sach);
}

