package com.example.duanmau1.sign;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.duanmau1.R;
import com.example.duanmau1.admin.activity.AdminActivity;
import com.example.duanmau1.customer.activity.UserActivity;
import com.example.duanmau1.customview.CustomToast;
import com.example.duanmau1.dao.NguoiDungDao;
import com.example.duanmau1.dao.SharePreNguoiDung;

public class SignInActivity extends AppCompatActivity {
    private NguoiDungDao nguoiDungDao;
    private EditText edtTenDn, edtMatKhau;
    private Button btnSignIn, btnSignUp;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_in);
        intiView();
        setView();
    }

    private void setView() {
        nguoiDungDao = new NguoiDungDao(this);
        btnSignIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String username =edtTenDn.getText().toString();
                String pass = edtMatKhau.getText().toString();

                if (username.trim().equals("") || pass.trim().equals("")) {
                    CustomToast customToast = new CustomToast(SignInActivity.this, CustomToast.WARNING,
                            "Vui lòng nhập đủ thông tin!");
                    customToast.showToast();
                    return;
                }

                nguoiDungDao.checkNguoiDung(username, pass, new NguoiDungDao.OnCheckUserComplete() {
                    @Override
                    public void onCheckComplete(boolean isSuccess, int mand, int role) {
                        if (!isSuccess) {
                            CustomToast customToast = new CustomToast(SignInActivity.this, CustomToast.ERROR,
                                    "Sai thông tin!");
                            customToast.showToast();
                            return;
                        }
                        SharePreNguoiDung sharePre= new SharePreNguoiDung(SignInActivity.this);
                        sharePre.setMaNd(mand);

                        if (role == 0) {
                            Intent intent = new Intent(SignInActivity.this, AdminActivity.class);
                            startActivity(intent);
                            finish();
                        } else if (role == 1) {
                            Intent intent = new Intent(SignInActivity.this, UserActivity.class);
                            Bundle bundle = new Bundle();
                            bundle.putInt("viewNum", -1);
                            intent.putExtras(bundle);
                            startActivity(intent);
                            finish();
                        }
                    }
                });
            }
        });

        btnSignUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(SignInActivity.this, SignUpActivity.class);
                startActivity(intent);
            }
        });
    }

    private void intiView() {
        edtTenDn = findViewById(R.id.edt_tenDnSignIn);
        edtMatKhau = findViewById(R.id.edt_matKhauSignIn);
        btnSignIn = findViewById(R.id.btn_signInDn);
        btnSignUp = findViewById(R.id.btn_signUpDn);
    }
}