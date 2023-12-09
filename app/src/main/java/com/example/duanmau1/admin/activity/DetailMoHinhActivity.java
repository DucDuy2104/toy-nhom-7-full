package com.example.duanmau1.admin.activity;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.duanmau1.GroupFunction.CurrencyConvert;
import com.example.duanmau1.R;
import com.example.duanmau1.customview.CustomToast;
import com.example.duanmau1.dao.MoHinhDao;
import com.example.duanmau1.model.LoaiMoHinh;
import com.example.duanmau1.model.MoHinh;
import com.google.android.material.bottomsheet.BottomSheetBehavior;
import com.google.android.material.bottomsheet.BottomSheetDialog;

import java.util.Calendar;

public class DetailMoHinhActivity extends AppCompatActivity {
    TextView tvName, tvRating, tvPrice, tvAmount, tvSold, tvType, tvMaterial, tvRatio,
    tvHeight, tvRemaining, tvDate, tvSex, tvAgeLimit, tvOrigin;
    Button btnUpdate, btnExit, btnDelete;
    ImageView img;
    int mamh;
    MoHinhDao moHinhDao;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail_mo_hinh);
        getData();
        initView();
        setView();
        setOnViewClicked();
    }

    private void setOnViewClicked() {
        btnExit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        btnDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                moHinhDao.getMoHinhById(mamh, new MoHinhDao.OnGetMoHinhSuccess() {
                    @Override
                    public void onGetSuccess(MoHinh moHinh, LoaiMoHinh loaiMoHinh) {
                        showDialogDelete(moHinh.getMaMh(), moHinh.getTenMh());
                    }
                });
            }
        });

        btnUpdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showBottomSheetDialog();
            }
        });
    }

    private void showBottomSheetDialog() {
        View viewBottomDialog = LayoutInflater.from(this).inflate(R.layout.dialog_update_sp, null);
        BottomSheetDialog bottomSheetDialog = new BottomSheetDialog(this);
        bottomSheetDialog.setContentView(viewBottomDialog);
        bottomSheetDialog.setCancelable(true);
        bottomSheetDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        //mở rộng toàn bộ
        BottomSheetBehavior<View> behavior = BottomSheetBehavior.from((View)viewBottomDialog.getParent());
        behavior.setState(BottomSheetBehavior.STATE_EXPANDED);


        EditText edtName = viewBottomDialog.findViewById(R.id.edt_nameSpUpdateAdmin);
        EditText edtPrice = viewBottomDialog.findViewById(R.id.edt_priceSpUpdateAdmin);
        EditText edtMaterial = viewBottomDialog.findViewById(R.id.edt_materialSpUpadteAdmin);
        EditText edtRatio = viewBottomDialog.findViewById(R.id.edt_ratioUpdateSpAdmin);
        EditText edtHeight = viewBottomDialog.findViewById(R.id.edt_heightUpdateSpAdmin);
        EditText edtDate = viewBottomDialog.findViewById(R.id.edt_dateUpdateSpAdmin);
        EditText edtAgeLimit = viewBottomDialog.findViewById(R.id.edt_ageUpdateSpAdmin);
        EditText edtOrigin = viewBottomDialog.findViewById(R.id.edt_originUpdateSpAdmin);
        Button btnUpdate = viewBottomDialog.findViewById(R.id.btn_updateSpAdmin);
        Button btnCancel = viewBottomDialog.findViewById(R.id.btn_cancelUpdateSpAdmin);

        moHinhDao.getMoHinhById(mamh, new MoHinhDao.OnGetMoHinhSuccess() {
            @Override
            public void onGetSuccess(MoHinh moHinh, LoaiMoHinh loaiMoHinh) {
                edtName.setText(moHinh.getTenMh());
                edtPrice.setText(moHinh.getGiaBan()+"");
                edtMaterial.setText(moHinh.getChatLieu());
                edtRatio.setText(moHinh.getTiLe());
                edtHeight.setText(moHinh.getChieuCao()+"");
                edtDate.setText(moHinh.getNgaySx());
                edtAgeLimit.setText(moHinh.getGioiHanDoTuoi()+"");
                edtOrigin.setText(moHinh.getXuatXu());
                edtDate.setFocusable(false);
                edtDate.setFocusableInTouchMode(false);

                edtDate.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        DatePickerDialog datePickerDialog = new DatePickerDialog(
                                DetailMoHinhActivity.this,
                                new DatePickerDialog.OnDateSetListener() {
                                    @Override
                                    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                                        // Xử lý ngày được chọn
                                        String selectedDate = dayOfMonth + "/" + (month + 1) + "/" + year;
                                        edtDate.setText(selectedDate);
                                    }
                                },
                                // Thiết lập ngày mặc định khi mở dialog (năm, tháng, ngày)
                                Calendar.getInstance().get(Calendar.YEAR),
                                Calendar.getInstance().get(Calendar.MONTH),
                                Calendar.getInstance().get(Calendar.DAY_OF_MONTH)
                        );

                        datePickerDialog.show();
                    }
                });

                btnCancel.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        bottomSheetDialog.dismiss();
                    }
                });


                btnUpdate.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        String name = edtName.getText().toString();
                        int price = Integer.parseInt(edtPrice.getText().toString());
                        String material = edtMaterial.getText().toString();
                        String ratio = edtRatio.getText().toString();
                        int height = Integer.parseInt(edtHeight.getText().toString());
                        String date = edtDate.getText().toString();
                        int age = Integer.parseInt(edtAgeLimit.getText().toString());
                        String origin = edtOrigin.getText().toString();

                        moHinh.setTenMh(name);
                        moHinh.setGiaBan(price);
                        moHinh.setChatLieu(material);
                        moHinh.setTiLe(ratio);
                        moHinh.setChieuCao(height);
                        moHinh.setNgaySx(date);
                        moHinh.setGioiHanDoTuoi(age);
                        moHinh.setXuatXu(origin);

                        moHinhDao.updateMoHinh(moHinh);
                        CustomToast customToast = new CustomToast(DetailMoHinhActivity.this, CustomToast.SUCCESS, "Updated successfully!");
                        customToast.showToast();
                        setView();
                        bottomSheetDialog.dismiss();
                    }
                });
            }
        });

        bottomSheetDialog.show();
    }

    private void setView() {
        moHinhDao.getMoHinhById(mamh, new MoHinhDao.OnGetMoHinhSuccess() {
            @Override
            public void onGetSuccess(MoHinh moHinh, LoaiMoHinh loaiMoHinh) {
                Glide.with(DetailMoHinhActivity.this).load(Uri.parse(moHinh.getImgUri())).into(img);
                tvName.setText(moHinh.getTenMh());
                tvRating.setText(moHinh.getDanhGia() + "");
                tvPrice.setText(CurrencyConvert.convertFromFloatToVNCurrency(moHinh.getGiaBan()));
                tvAmount.setText(moHinh.getSoLuong()+"");
                tvSold.setText(moHinh.getSoLuongDaBan()+ "");
                tvType.setText(loaiMoHinh.getTenLoai());
                tvMaterial.setText(moHinh.getChatLieu());
                tvRatio.setText(moHinh.getTiLe());
                tvHeight.setText(moHinh.getChieuCao() +"cm");
                tvRemaining.setText((moHinh.getSoLuong() - moHinh.getSoLuongDaBan()) + "");
                tvDate.setText(moHinh.getNgaySx());
                tvSex.setText(moHinh.getGioiTinh());
                tvAgeLimit.setText(moHinh.getGioiHanDoTuoi()+"");
                tvOrigin.setText(moHinh.getXuatXu());
            }
        });
    }

    private void initView() {
        tvName = findViewById(R.id.tv_nameDetailSpAdmin);
        tvRating = findViewById(R.id.tv_ratingDetailSpAdmin);
        tvPrice = findViewById(R.id.tv_priceDetailSpAdmin);
        tvAmount = findViewById(R.id.tv_amountDetailSpAdmin);
        tvSold  = findViewById(R.id.tv_soldDetailSpAdmin);
        tvType  =findViewById(R.id.tv_typeDetailSpAdmin);
        tvMaterial = findViewById(R.id.tv_materialDetailSpAdmin);
        tvRatio = findViewById(R.id.tv_ratioDetailSpAdmin);
        tvHeight = findViewById(R.id.tv_heightDetailSpAdmin);
        tvRemaining = findViewById(R.id.tv_remainingDetailSpAdmin);
        tvDate= findViewById(R.id.tv_dateDetailSpAdmin);
        tvSex = findViewById(R.id.tv_sexDetailSpAdmin);
        tvAgeLimit = findViewById(R.id.tv_ageDetailSpAdmin);
        tvOrigin = findViewById(R.id.tv_originDetailSpAdmin);
        btnUpdate =findViewById(R.id.btn_updateDetailAdmin);
        btnDelete = findViewById(R.id.btn_deleteDetailSpAdmin);
        btnExit = findViewById(R.id.btn_exitDetailSpAdmin);
        img = findViewById(R.id.img_moHinhDetailMhAdmin);
    }

    private void showDialogDelete(int maMh, String tenMh) {
        View viewDialog = LayoutInflater.from(this).inflate(R.layout.dialog_warning, null);
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setView(viewDialog);
        AlertDialog dialog = builder.create();

        ImageButton btnCancel = viewDialog.findViewById(R.id.btn_closeWarningDialog);
        Button btnConFirm = viewDialog.findViewById(R.id.btn_conFirmWarningDialog);
        TextView tvTitle = viewDialog.findViewById(R.id.tv_titleWarningDialog);
        TextView tvMess = viewDialog.findViewById(R.id.tv_messageWarningDialog);

        tvTitle.setText(tenMh);
        tvMess.setText("Bạn có chắc chắn muốn xóa " + tenMh);

        btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });

        btnConFirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showSubDialog(maMh);
                dialog.dismiss();
            }
        });
        dialog.show();
    }

    private void showSubDialog(int maMh) {
        View viewDialog = LayoutInflater.from(this).inflate(R.layout.dialog_error, null);
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setView(viewDialog);
        AlertDialog dialog = builder.create();

        ImageButton btnCancel = viewDialog.findViewById(R.id.btn_closeErrorDialog);
        Button btnConFirm = viewDialog.findViewById(R.id.btn_conFirmErrorDialog);
        TextView tvTitle = viewDialog.findViewById(R.id.tv_titleErrorDialog);
        TextView tvMess = viewDialog.findViewById(R.id.tv_messageErrorDialog);

        tvTitle.setText("CẢNH BÁO");
        tvMess.setText("!!! Xóa mô hình, các hóa đơn liên quan đến mô hình sẽ bị xóa !!!");

        btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });

        btnConFirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                moHinhDao.deleteMoHinh(maMh);
                CustomToast customToast = new CustomToast(DetailMoHinhActivity.this, CustomToast.SUCCESS, "Xóa thành công!");
                customToast.showToast();
                dialog.dismiss();
                finish();
            }
        });
        dialog.show();
    }

    private void getData() {
        moHinhDao = new MoHinhDao(this);
        Intent intent  = getIntent();
        mamh = intent.getExtras().getInt("mamh", -1);
    }

}