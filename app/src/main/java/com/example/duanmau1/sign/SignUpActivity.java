package com.example.duanmau1.sign;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.viewpager2.widget.ViewPager2;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;

import com.example.duanmau1.R;
import com.example.duanmau1.customer.adapter.ViewPagerUserAdapter;
import com.example.duanmau1.customer.fragment.CartUserFragment;
import com.example.duanmau1.customview.CustomToast;
import com.example.duanmau1.dao.SharePreNguoiDung;
import com.example.duanmau1.sign.fragment.DangKyFragment;
import com.example.duanmau1.sign.fragment.DangKyThongTinFragment;

import java.util.ArrayList;
import java.util.List;

import me.relex.circleindicator.CircleIndicator3;

public class SignUpActivity extends AppCompatActivity {

    ViewPager2 vPager;
    CircleIndicator3 cIndicator;
    Button btnNext;
    ViewPagerUserAdapter viewPagerUserAdapter;
    int count =0;
    List<Fragment> fragmentList;

    SharePreNguoiDung sharePreNguoiDung;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);
        initView();
        setView();
        setOnClick();
    }

    private void setOnClick() {
        btnNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (count == 0) {
                    ((DangKyFragment)fragmentList.get(0)).onNext();
                    String pass = sharePreNguoiDung.getPass();
                    String rePass = sharePreNguoiDung.getRePass();
                    String userName = sharePreNguoiDung.getUserName();
                    if (pass.trim().equals("") || rePass.trim().equals("") || userName.trim().equals("")) {
                        CustomToast customToast = new CustomToast(SignUpActivity.this, CustomToast.WARNING, "Vui Lòng nhập đủ thông tin!");
                        customToast.showToast();
                    } else if (!pass.trim().equals(rePass)) {
                        CustomToast customToast = new CustomToast(SignUpActivity.this, CustomToast.WARNING, "Mật Khẩu không khớp!");
                        customToast.showToast();
                    } else {
                        vPager.setCurrentItem(1);
                        btnNext.setText("CREATE");
                        count++;
                        return;
                    }
                }

                if (count > 0) {
                    ((DangKyThongTinFragment)fragmentList.get(1)).onCreateFr();
                }
            }
        });


    }

    private void setView() {
        sharePreNguoiDung = new SharePreNguoiDung(this);
        fragmentList = new ArrayList<>();
        fragmentList.add(new DangKyFragment(this));
        fragmentList.add(new DangKyThongTinFragment(this));
        viewPagerUserAdapter = new ViewPagerUserAdapter(this, fragmentList);
        vPager.setAdapter(viewPagerUserAdapter);
        cIndicator.setViewPager(vPager);
        viewPagerUserAdapter.registerAdapterDataObserver(cIndicator.getAdapterDataObserver());
        vPager.setFocusable(false);
    }

    private void initView() {
        vPager = findViewById(R.id.vPager_dangKy);
        cIndicator = findViewById(R.id.indicator);
        btnNext = findViewById(R.id.btn_nextSignUp);
    }
}