package com.example.duanmau1.model;

public class HoaDonChiTiet {
    private int maHdct, maMh,maNd, soLuong;

    public HoaDonChiTiet() {
    }

    public HoaDonChiTiet(int maMh, int maNd, int soLuong) {
        this.maMh = maMh;
        this.maNd = maNd;
        this.soLuong = soLuong;
    }

    public int getMaHdct() {
        return maHdct;
    }

    public void setMaHdct(int maHdct) {
        this.maHdct = maHdct;
    }

    public int getMaMh() {
        return maMh;
    }

    public void setMaMh(int maMh) {
        this.maMh = maMh;
    }

    public int getMaNd() {
        return maNd;
    }

    public void setMaNd(int maNd) {
        this.maNd = maNd;
    }

    public int getSoLuong() {
        return soLuong;
    }

    public void setSoLuong(int soLuong) {
        this.soLuong = soLuong;
    }
}
