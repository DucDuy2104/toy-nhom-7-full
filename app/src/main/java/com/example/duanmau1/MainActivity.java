package com.example.duanmau1;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.app.AlertDialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.duanmau1.dao.LoaiMoHinhDao;
import com.example.duanmau1.dao.MoHinhDao;
import com.example.duanmau1.dao.NguoiDungDao;
import com.example.duanmau1.model.LoaiMoHinh;
import com.example.duanmau1.model.MoHinh;
import com.example.duanmau1.model.NguoiDung;

public class MainActivity extends AppCompatActivity {
    private static final int PERMISSION_CODE = 20313;

    private AlertDialog alertDialog;
    private ProgressBar progressBar;

    EditText edtId, edtname,edtgioiTinh, edtNgaySinh, edtSdt,edtEmail,edtDiaChi, edtTenDn, edtMatKhau;
    ImageView imgUser;
    Button btnSend,btnGet;
    Uri selectedImageUri;
    NguoiDungDao nguoiDungDao;

    ActivityResultLauncher<Intent> pickImageLauncher = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(), result -> {
        if (result.getResultCode() == RESULT_OK) {
            Intent data = result.getData();
            if (data != null) {
                selectedImageUri = data.getData();
                // Xử lý đường dẫn ảnh ở đây
                imgUser.setImageURI(selectedImageUri); // Hiển thị ảnh trong ImageView, ví dụ.
            }
        }
    });
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        
        initView();

        progressBar = new ProgressBar(this);
        progressBar.setIndeterminate(true);

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setView(progressBar);
        builder.setMessage("Đợi xíu, đang load...."); // Thay bằng thông điệp tương ứng
        builder.setCancelable(false);

        alertDialog = builder.create();
        
        imgUser.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                checkPermissions();
            }
        });

        btnSend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
             String name = edtname.getText().toString();
             String gt =  edtgioiTinh.getText().toString();
             String ngSinh = edtNgaySinh.getText().toString();
             String sdt = edtSdt.getText().toString();
             String email=edtEmail.getText().toString();
             String diaChi= edtDiaChi.getText().toString();
             String tenDn = edtTenDn.getText().toString();
             String matkhau = edtMatKhau.getText().toString();
//                this.tenNd = tenNd;
//                this.gioiTinhNd = gioiTinhNd;
//                this.ngaySinhNd = ngaySinhNd;
//                this.sdtNd = sdtNd;
//                this.emailNd = emailNd;
//                this.diaChiNd = diaChiNd;
//                this.tenDn = tenDn;
//                this.matKhau = matKhau;
//                this.imageUri = imageUri;
//                this.role = role;
             NguoiDung nguoiDung = new NguoiDung(name, gt, ngSinh, sdt, email, diaChi, tenDn, matkhau, selectedImageUri.toString(), 1);
             nguoiDungDao.addNguoiDung(nguoiDung);
            }
        });

        btnGet.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showProgressDialog();
                nguoiDungDao.getNguoiDungById(0, new NguoiDungDao.OnGetDataSuccess() {
                    @Override
                    public void onGetNdSuccess(NguoiDung nguoiDung) {
                        if (nguoiDung != null){
                            edtId.setText(nguoiDung.getIdNd() + "");
                            edtname.setText(nguoiDung.getTenNd());
                            edtgioiTinh.setText(nguoiDung.getGioiTinhNd());
                            edtNgaySinh.setText(nguoiDung.getNgaySinhNd());
                            edtSdt.setText(nguoiDung.getSdtNd());
                            edtEmail.setText(nguoiDung.getEmailNd());
                            edtDiaChi.setText(nguoiDung.getDiaChiNd());
                            edtTenDn.setText(nguoiDung.getTenDn());
                            edtMatKhau.setText(nguoiDung.getMatKhau());
                            Uri uri = Uri.parse(nguoiDung.getImageUri());
                            Glide.with(MainActivity.this).load(uri).into(imgUser);
                        }


                        dismissProgressDialog();
                    }
                });

            }
        });
    }

    private void checkPermissions() {
            if (checkSelfPermission(android.Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_DENIED) {
                String[] permission = new String[] {Manifest.permission.READ_EXTERNAL_STORAGE};
                ActivityCompat.requestPermissions(this, permission, PERMISSION_CODE);
            } else {
                getImageFromGallary();
            }
    }


    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == PERMISSION_CODE) {
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
               getImageFromGallary();
            } else {
                Toast.makeText(this, "Permission denied", Toast.LENGTH_SHORT).show();
            }
        }
    }

    private void getImageFromGallary() {
        Intent intent = new Intent(Intent.ACTION_PICK);
        intent.setType("image/*"); // Chỉ cho phép chọn ảnh
        pickImageLauncher.launch(intent);
    }

    private void initView() {
        edtId =findViewById(R.id.edt_id);
        edtname = findViewById(R.id.edt_name);
        edtgioiTinh = findViewById(R.id.edt_gt);
        edtNgaySinh = findViewById(R.id.edt_ngaysinh);
        edtSdt = findViewById(R.id.edt_sdt);
        edtEmail =findViewById(R.id.edt_email);
        edtDiaChi  = findViewById(R.id.edt_diachi);
        edtTenDn = findViewById(R.id.edt_tenDn);
        edtMatKhau= findViewById(R.id.edt_matkhau);
        imgUser  = findViewById(R.id.image);
        btnGet  = findViewById(R.id.btn_get);
        btnSend  = findViewById(R.id.btn_send);
        nguoiDungDao = new NguoiDungDao(this);
    }

    private void showProgressDialog() {
        alertDialog.show();
    }

    private void dismissProgressDialog() {
        alertDialog.dismiss();
    }
}