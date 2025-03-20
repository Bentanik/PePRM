package com.example.baiexam.model;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "tac_gias")
public class TacGia {
    @PrimaryKey(autoGenerate = true)
    private int idTacGia;

    private String tenTacGia;
    private String email;
    private String diaChi;
    private String dienThoai;

    public TacGia() {
    }

    public TacGia(String tenTacGia, String email, String diaChi, String dienThoai) {
        this.tenTacGia = tenTacGia;
        this.email = email;
        this.diaChi = diaChi;
        this.dienThoai = dienThoai;
    }

    public String getTenTacGia() {
        return tenTacGia;
    }

    public void setTenTacGia(String tenTacGia) {
        this.tenTacGia = tenTacGia;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getDiaChi() {
        return diaChi;
    }

    public void setDiaChi(String diaChi) {
        this.diaChi = diaChi;
    }

    public String getDienThoai() {
        return dienThoai;
    }

    public void setDienThoai(String dienThoai) {
        this.dienThoai = dienThoai;
    }

    public int getIdTacGia() {
        return idTacGia;
    }

    public void setIdTacGia(int idTacGia) {
        this.idTacGia = idTacGia;
    }
}
