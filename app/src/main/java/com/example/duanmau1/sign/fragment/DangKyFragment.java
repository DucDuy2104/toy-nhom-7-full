package com.example.duanmau1.sign.fragment;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.duanmau1.R;
import com.example.duanmau1.dao.SharePreNguoiDung;
import com.example.duanmau1.sign.SignInActivity;

public class DangKyFragment extends Fragment {
    View view;
    EditText edtUserName, edtPass, edtRePass;
    TextView tvDangNhap;
    Context context;

    public DangKyFragment(Context context) {
        this.context = context;
    }
    SharePreNguoiDung sharePreNguoiDung;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_dang_ky, container, false);
        initView();
        setView();
        return view;
    }

    private void setView() {
        sharePreNguoiDung = new SharePreNguoiDung(context);
    }

    private void initView() {
        edtUserName = view.findViewById(R.id.edt_tenDnSignUp);
        edtPass = view.findViewById(R.id.edt_matKhauSignUp);
        edtRePass = view.findViewById(R.id.edt_reMatKhauSignUp);
        tvDangNhap = view.findViewById(R.id.tv_dangNhapNgaySignUp);

        tvDangNhap.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, SignInActivity.class);
                startActivity(intent);
            }
        });
    }

    public void onNext() {
        String userName = edtUserName.getText().toString().trim();
        String pass = edtPass.getText().toString().trim();
        String rePass = edtRePass.getText().toString();

        sharePreNguoiDung.setUserName(userName);
        sharePreNguoiDung.setPass(pass);
        sharePreNguoiDung.setRePass(rePass);
    }

    @Override
    public void onPause() {
        super.onPause();

    }
}
