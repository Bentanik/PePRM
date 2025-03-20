package com.example.baiexam.dao;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import com.example.baiexam.model.TacGia;

import java.util.List;

@Dao
public interface TacGiaDao {
    @Query("SELECT * FROM tac_gias")
    List<TacGia> getAllTacGia();

    @Query("SELECT * FROM tac_gias WHERE idTacGia IN (:tacGiaId)")
    TacGia loadTacGiaById(int tacGiaId);

    @Insert
    void insertTacGia(TacGia tacGia);

    @Update
    void updateTacGia(TacGia tacGia);

    @Delete
    void deleteTacGia(TacGia tacGia);
}
