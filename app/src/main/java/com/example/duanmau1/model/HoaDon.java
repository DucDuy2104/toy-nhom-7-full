package com.example.duanmau1.model;

import java.util.List;

public class HoaDon {
    private int maHd; // mã hóa đơn
    private int maNd; //mã người dùng
    private String ngayLap; //ngày lập
    private int tinhTrang; //tình trạng
    private int tongTien;
    private List<HoaDonChiTiet> hoaDonChiTietList; //list hóa đơn chi tiết

    boolean isExpand = false;

    public boolean isExpand() {
        return isExpand;
    }

    public void setExpand(boolean expand) {
        isExpand = expand;
    }

    public HoaDon() {
    }

    public HoaDon(int maNd, String ngayLap, int tinhTrang, int tongTien, List<HoaDonChiTiet> hoaDonChiTietList) {
        this.maNd = maNd;
        this.ngayLap = ngayLap;
        this.tinhTrang = tinhTrang;
        this.tongTien = tongTien;
        this.hoaDonChiTietList = hoaDonChiTietList;
    }

    public int getTongTien() {
        return tongTien;
    }

    public void setTongTien(int tongTien) {
        this.tongTien = tongTien;
    }

    public int getMaHd() {
        return maHd;
    }

    public void setMaHd(int maHd) {
        this.maHd = maHd;
    }

    public int getMaNd() {
        return maNd;
    }

    public void setMaNd(int maNd) {
        this.maNd = maNd;
    }

    public String getNgayLap() {
        return ngayLap;
    }

    public void setNgayLap(String ngayLap) {
        this.ngayLap = ngayLap;
    }

    public int getTinhTrang() {
        return tinhTrang;
    }

    public void setTinhTrang(int tinhTrang) {
        this.tinhTrang = tinhTrang;
    }

    public List<HoaDonChiTiet> getHoaDonChiTietList() {
        return hoaDonChiTietList;
    }

    public void setHoaDonChiTietList(List<HoaDonChiTiet> hoaDonChiTietList) {
        this.hoaDonChiTietList = hoaDonChiTietList;
    }

    public int getCount() {
        int count = 0;
        for (HoaDonChiTiet hoaDonChiTiet: hoaDonChiTietList) {
            count+= hoaDonChiTiet.getSoLuong();
        }
        return count;
    }
}
