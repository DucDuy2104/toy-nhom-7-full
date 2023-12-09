package com.example.duanmau1.model;

public class MoHinh {
    private int maMh; //mã mô hình
    private String tenMh, chatLieu, tiLe, ngaySx, gioiTinh, xuatXu;
    // tên mô hình, chất liệu, tỉ lệ, ngày sản xuất, loại sản phẩm, giới tính, xuất xứ;
    private float danhGia;
    private int giaBan, soLuong, soLuongDaBan, chieuCao, gioiHanDoTuoi;
    // đánh giá, giá bán, số lượng, số lượng bán, chiều cao, giới hạn độ tuổi;
    private int maLmh; // mã loại mô hình
    private String imgUri; // đường dẫn của ảnh

    public MoHinh(String tenMh, String chatLieu, String tiLe, String ngaySx, String gioiTinh, String xuatXu, float danhGia, int giaBan, int soLuong, int soLuongDaBan, int chieuCao, int gioiHanDoTuoi, int maLmh, String imgUri) {
        this.tenMh = tenMh;
        this.chatLieu = chatLieu;
        this.tiLe = tiLe;
        this.ngaySx = ngaySx;
        this.gioiTinh = gioiTinh;
        this.xuatXu = xuatXu;
        this.danhGia = danhGia;
        this.giaBan = giaBan;
        this.soLuong = soLuong;
        this.soLuongDaBan = soLuongDaBan;
        this.chieuCao = chieuCao;
        this.gioiHanDoTuoi = gioiHanDoTuoi;
        this.maLmh = maLmh;
        this.imgUri = imgUri;
    }

    public String getImgUri() {
        return imgUri;
    }

    public void setImgUri(String imgUri) {
        this.imgUri = imgUri;
    }

    public MoHinh() {
    }

    public int getMaMh() {
        return maMh;
    }

    public void setMaMh(int maMh) {
        this.maMh = maMh;
    }

    public String getTenMh() {
        return tenMh;
    }

    public void setTenMh(String tenMh) {
        this.tenMh = tenMh;
    }

    public String getChatLieu() {
        return chatLieu;
    }

    public void setChatLieu(String chatLieu) {
        this.chatLieu = chatLieu;
    }

    public String getTiLe() {
        return tiLe;
    }

    public void setTiLe(String tiLe) {
        this.tiLe = tiLe;
    }

    public String getNgaySx() {
        return ngaySx;
    }

    public void setNgaySx(String ngaySx) {
        this.ngaySx = ngaySx;
    }
    public String getGioiTinh() {
        return gioiTinh;
    }

    public void setGioiTinh(String gioiTinh) {
        this.gioiTinh = gioiTinh;
    }

    public String getXuatXu() {
        return xuatXu;
    }

    public void setXuatXu(String xuatXu) {
        this.xuatXu = xuatXu;
    }

    public float getDanhGia() {
        return danhGia;
    }

    public void setDanhGia(float danhGia) {
        this.danhGia = danhGia;
    }

    public int getGiaBan() {
        return giaBan;
    }

    public void setGiaBan(int giaBan) {
        this.giaBan = giaBan;
    }

    public int getSoLuong() {
        return soLuong;
    }

    public void setSoLuong(int soLuong) {
        this.soLuong = soLuong;
    }

    public int getSoLuongDaBan() {
        return soLuongDaBan;
    }

    public void setSoLuongDaBan(int soLuongDaBan) {
        this.soLuongDaBan = soLuongDaBan;
    }

    public int getChieuCao() {
        return chieuCao;
    }

    public void setChieuCao(int chieuCao) {
        this.chieuCao = chieuCao;
    }

    public int getGioiHanDoTuoi() {
        return gioiHanDoTuoi;
    }

    public void setGioiHanDoTuoi(int gioiHanDoTuoi) {
        this.gioiHanDoTuoi = gioiHanDoTuoi;
    }

    public int getMaLmh() {
        return maLmh;
    }

    public void setMaLmh(int maLmh) {
        this.maLmh = maLmh;
    }
}
