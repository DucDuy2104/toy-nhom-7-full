package com.example.duanmau1.model;

public class LoaiMoHinh {
    private int maLmh; //mã loại mô hình
    private String tenLoai; // tên loại
    private String imgUri; // đường dẫn của ảnh

    private boolean isXemTatCa = false;


    public boolean getIsXemTatCa() {
        return isXemTatCa;
    }

    public void setIsXemTatCa(boolean isXemTatCa) {
        this.isXemTatCa = isXemTatCa;
    }

    public LoaiMoHinh() {
    }

    public LoaiMoHinh(String tenLoai, String imgUri) {
        this.tenLoai = tenLoai;
        this.imgUri = imgUri;
    }

    public int getMaLmh() {
        return maLmh;
    }

    public void setMaLmh(int maLmh) {
        this.maLmh = maLmh;
    }

    public String getTenLoai() {
        return tenLoai;
    }

    public void setTenLoai(String tenLoai) {
        this.tenLoai = tenLoai;
    }

    public String getImgUri() {
        return imgUri;
    }

    public void setImgUri(String imgUri) {
        this.imgUri = imgUri;
    }
}
