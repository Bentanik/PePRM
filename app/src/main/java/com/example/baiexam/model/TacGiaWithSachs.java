package com.example.baiexam.model;

import androidx.room.Embedded;
import androidx.room.Relation;

import java.util.List;

public class TacGiaWithSachs {
    @Embedded
    public TacGia tacGia;

    @Relation(
            parentColumn = "idTacGia",
            entityColumn = "idTacGia"
    )
    public List<Sach> sachs;
}

