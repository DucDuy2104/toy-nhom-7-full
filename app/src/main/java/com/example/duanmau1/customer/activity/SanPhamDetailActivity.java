package com.example.duanmau1.customer.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.duanmau1.GroupFunction.CurrencyConvert;
import com.example.duanmau1.R;
import com.example.duanmau1.customview.CustomToast;
import com.example.duanmau1.dao.HoaDonChiTietDao;
import com.example.duanmau1.dao.MoHinhDao;
import com.example.duanmau1.dao.SharePreNguoiDung;
import com.example.duanmau1.model.HoaDonChiTiet;
import com.example.duanmau1.model.LoaiMoHinh;
import com.example.duanmau1.model.MoHinh;

public class SanPhamDetailActivity extends AppCompatActivity {
    private int maMh;
    private MoHinhDao moHinhDao;

    private ImageView imgSp;
    private TextView tvNameSp, tvGiaSp, tvRatingSp, tvSoldSp, tvChatLieuSp, tvTiLeSp, tvNgaySx, tvLoaiSp, tvGioiTinhSp, tvXuatSuSp,
    tvKhoSp, tvChieuCaoSp, tvGioiHanTuoiSp, tvCountSp;
    private Button btnMinusCount, btnAddCount, btnGoToCart, btnAddToCart;
    private SharePreNguoiDung sharePreNguoiDung;
    private HoaDonChiTietDao hoaDonChiTietDao;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_san_pham_detail);
        initView();
        getData();
        setView();
    }

    private void setView() {
        final int[] kho = new int[1];
        moHinhDao.getMoHinhById(maMh, new MoHinhDao.OnGetMoHinhSuccess() {
            @Override
            public void onGetSuccess(MoHinh moHinh, LoaiMoHinh loaiMoHinh) {
                Glide.with(SanPhamDetailActivity.this).load(Uri.parse(moHinh.getImgUri())).into(imgSp);
                tvNameSp.setText(moHinh.getTenMh());
                tvGiaSp.setText(CurrencyConvert.convertFromFloatToVNCurrency(moHinh.getGiaBan()));
                tvRatingSp.setText("Đánh giá: " + moHinh.getDanhGia());
                tvSoldSp.setText("Đã bán: " + moHinh.getSoLuongDaBan());
                tvChatLieuSp.setText(moHinh.getChatLieu());
                tvTiLeSp.setText(moHinh.getTiLe());
                tvNgaySx.setText(moHinh.getNgaySx());
                tvLoaiSp.setText(loaiMoHinh.getTenLoai());
                tvGioiTinhSp.setText(moHinh.getGioiTinh());
                tvXuatSuSp.setText(moHinh.getXuatXu());
                tvKhoSp.setText((moHinh.getSoLuong() - moHinh.getSoLuongDaBan()) + "");
                tvChieuCaoSp.setText(moHinh.getChieuCao() + "cm");
                tvGioiHanTuoiSp.setText(moHinh.getGioiHanDoTuoi() + "");
                tvCountSp.setText("1");
                kho[0] = moHinh.getSoLuong() - moHinh.getSoLuongDaBan();
            }
        });

        btnMinusCount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int count = Integer.parseInt(tvCountSp.getText().toString());
                if (count == 1) {
                    return;
                } else {
                    count--;
                }

                tvCountSp.setText(count + "");
            }
        });

        btnAddCount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int count = Integer.parseInt(tvCountSp.getText().toString());
                if (count == kho[0]) {
                    Toast.makeText(SanPhamDetailActivity.this, "KHO CHỈ CÒN " + count + " SẢN PHẨM", Toast.LENGTH_SHORT).show();
                    return;
                } else  {
                    count++;
                }

                tvCountSp.setText(count + "");
            }
        });

        btnGoToCart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(SanPhamDetailActivity.this, UserActivity.class);
                Bundle bundle = new Bundle();
                bundle.putInt("viewNum", 1);
                intent.putExtras(bundle);
                startActivity(intent);
                finish();
            }
        });

        btnAddToCart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int maNd = sharePreNguoiDung.getMaNd();
                if (maNd == -1) {
                    Toast.makeText(SanPhamDetailActivity.this, "Không lấy được người dùng!", Toast.LENGTH_SHORT).show();
                    return;
                }
                int sLuong = Integer.parseInt(tvCountSp.getText().toString());
                HoaDonChiTiet hoaDonChiTiet = new HoaDonChiTiet(maMh, maNd, sLuong);
                hoaDonChiTietDao.addHoaDonChiTiet(hoaDonChiTiet);
                CustomToast customToast  = new CustomToast(SanPhamDetailActivity.this, CustomToast.SUCCESS, "Thêm thành công!");
                customToast.showToast();
            }
        });
    }

    private void getData() {
        maMh = getIntent().getExtras().getInt("mamh");
        moHinhDao = new MoHinhDao(this);
        hoaDonChiTietDao = new HoaDonChiTietDao(this);
        sharePreNguoiDung = new SharePreNguoiDung(this);
    }

    private void initView() {
        imgSp = findViewById(R.id.img_detaileSpUser);
        tvNameSp = findViewById(R.id.tv_nameDetailSpUser);
        tvGiaSp = findViewById(R.id.tv_priceDetailSpUser);
        tvRatingSp = findViewById(R.id.tv_ratingDetailSpUser);
        tvSoldSp = findViewById(R.id.tv_soldDetailSpUser);
        tvChatLieuSp = findViewById(R.id.tv_chatLieuDetailSpUser);
        tvTiLeSp = findViewById(R.id.tv_tiLeDetailSpUser);
        tvNgaySx = findViewById(R.id.tv_ngaySxDetailSpUser);
        tvLoaiSp = findViewById(R.id.tv_loaiDetailSpUser);
        tvGioiTinhSp = findViewById(R.id.tv_sexDetailSpUser);
        tvXuatSuSp = findViewById(R.id.tv_xuatSuDetailSpUser);
        tvKhoSp = findViewById(R.id.tv_khoDetailSpUser);
        tvChieuCaoSp = findViewById(R.id.tv_chieuCaoDetailSpUser);
        tvGioiHanTuoiSp = findViewById(R.id.tv_gioiHanTuoiDetailSpUser);
        tvCountSp = findViewById(R.id.tv_countDetailSpUser);
        btnMinusCount = findViewById(R.id.btn_minusCount);
        btnAddCount = findViewById(R.id.btn_addCount);
        btnGoToCart = findViewById(R.id.btn_goToCartDetailSpUser);
        btnAddToCart = findViewById(R.id.btn_addToCartDetailSpUser);
    }
}