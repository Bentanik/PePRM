package com.example.baiexam.model;

import androidx.room.Entity;
import androidx.room.ForeignKey;
import androidx.room.PrimaryKey;

@Entity(
        tableName = "sachs",
        foreignKeys = @ForeignKey(
                entity = TacGia.class,
                parentColumns = "idTacGia",
                childColumns = "idTacGia",
                onDelete = ForeignKey.CASCADE
        )
)
public class Sach {
    @PrimaryKey(autoGenerate = true)
    private int idSach;

    private String tenSach;
    private String ngayXB;
    private String theLoai;
    private int idTacGia;

    public Sach(String tenSach, String ngayXB, String theLoai, int idTacGia) {
        this.tenSach = tenSach;
        this.ngayXB = ngayXB;
        this.theLoai = theLoai;
        this.idTacGia = idTacGia;
    }

    public String getTenSach() {
        return tenSach;
    }

    public void setTenSach(String tenSach) {
        this.tenSach = tenSach;
    }

    public String getNgayXB() {
        return ngayXB;
    }

    public void setNgayXB(String ngayXB) {
        this.ngayXB = ngayXB;
    }

    public String getTheLoai() {
        return theLoai;
    }

    public void setTheLoai(String theLoai) {
        this.theLoai = theLoai;
    }

    public int getIdTacGia() {
        return idTacGia;
    }

    public void setIdTacGia(int idTacGia) {
        this.idTacGia = idTacGia;
    }

    public int getIdSach() {
        return idSach;
    }

    public void setIdSach(int idSach) {
        this.idSach = idSach;
    }
}

