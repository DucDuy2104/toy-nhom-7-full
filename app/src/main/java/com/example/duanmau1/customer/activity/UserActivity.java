package com.example.duanmau1.customer.activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.viewpager2.widget.ViewPager2;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.media.Image;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.duanmau1.R;
import com.example.duanmau1.customer.fragment.HomeUserFragment;
import com.example.duanmau1.customview.CustomToast;
import com.example.duanmau1.dao.NguoiDungDao;
import com.example.duanmau1.dao.SharePreNguoiDung;
import com.example.duanmau1.model.NguoiDung;
import com.example.duanmau1.sign.SignInActivity;
import com.google.android.material.navigation.NavigationView;

public class UserActivity extends AppCompatActivity {

    private FrameLayout frameLayout;
    private NavigationView drNavUserAct;
    private Toolbar toolbar;
    private DrawerLayout drawerLayout;
    private int viewNum = -1;
    NguoiDungDao nguoiDungDao;

    TextView tvName, tvEmail;
    int mand;
    SharePreNguoiDung sharePreNguoiDung;
    ImageView imgUser;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user);
        initView();
        getData();
        setFirst();
        setView();
    }

    private void getData() {
        Intent intent = getIntent();
        if (intent != null) {
            viewNum = intent.getExtras().getInt("viewNum", -1);
        }

    }

    private void setView() {

        drNavUserAct.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                if (item.getItemId() ==  R.id.home_drNavUser) {
                    HomeUserFragment homeUserFragment = new HomeUserFragment();
                    homeUserFragment.setViewNum(viewNum);
                    getSupportFragmentManager()
                            .beginTransaction()
                            .replace(R.id.fr_userActivity, homeUserFragment)
                            .commit();

                    drawerLayout.close();
                } else if (item.getItemId() == R.id.deleteAcc_drNavUser) {
                    showDeleteDialog();
                    drawerLayout.close();
                } else if (item.getItemId() == R.id.signOut_drNavUser) {
                    Intent intent = new Intent(UserActivity.this, SignInActivity.class);
                    startActivity(intent);
                    drawerLayout.close();
                } else if (item.getItemId() == R.id.changePass_drNavUser) {
                    setDialog();
                    drawerLayout.close();
                }
                return true;
            }
        });

    }

    private void showDeleteDialog() {
        View viewDialog = LayoutInflater.from(this).inflate(R.layout.dialog_error, null);
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setView(viewDialog);
        AlertDialog dialog = builder.create();

        ImageButton btnCancel = viewDialog.findViewById(R.id.btn_closeErrorDialog);
        Button btnConFirm = viewDialog.findViewById(R.id.btn_conFirmErrorDialog);
        TextView tvTitle = viewDialog.findViewById(R.id.tv_titleErrorDialog);
        TextView tvMess = viewDialog.findViewById(R.id.tv_messageErrorDialog);

        tvTitle.setText("XÁC NHẬN");
        tvMess.setText("Bạn có chắc chắn muốn xóa tài khoản!");

        btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });

        btnConFirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                nguoiDungDao.deleteNguoiDung(mand);
                Intent intent = new Intent(UserActivity.this, SignInActivity.class);
                startActivity(intent);
                dialog.dismiss();
            }
        });

        dialog.show();


    }

    private void setDialog() {
        View viewDialog = LayoutInflater.from(this).inflate(R.layout.dialog_change_pass, null);
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setView(viewDialog);
        AlertDialog dialog = builder.create();
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));

        EditText edtPass = viewDialog.findViewById(R.id.edt_matKhauChangePass);
        EditText edtNewPass = viewDialog.findViewById(R.id.edt_newMkChangePass);
        EditText edtRePass = viewDialog.findViewById(R.id.edt_reNewMkChangePass);
        Button btnCancel = viewDialog.findViewById(R.id.btn_huyChangePass);
        Button btnOke = viewDialog.findViewById(R.id.btn_changePass);

        btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });

        btnOke.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String pass = edtPass.getText().toString();
                String newPass = edtNewPass.getText().toString();
                String rePass = edtRePass.getText().toString();

                nguoiDungDao.getNguoiDungById(mand, new NguoiDungDao.OnGetDataSuccess() {
                    @Override
                    public void onGetNdSuccess(NguoiDung nguoiDung) {
                        if (!pass.trim().equals(nguoiDung.getMatKhau())) {
                            CustomToast customToast = new CustomToast(UserActivity.this, CustomToast.WARNING, "Sai mật khẩu!");
                            customToast.showToast();
                            return;
                        } else if (!newPass.trim().equals(rePass)) {
                            CustomToast customToast = new CustomToast(UserActivity.this, CustomToast.WARNING, "Mật khẩu mới không khớp!");
                            customToast.showToast();
                            return;
                        }else  {
                            nguoiDung.setMatKhau(newPass);
                            nguoiDungDao.updateNguoiDung(nguoiDung);
                            dialog.dismiss();
                        }
                    }
                });
            }
        });

        dialog.show();


    }

    private void setFirst() {
        nguoiDungDao = new NguoiDungDao(this);
        sharePreNguoiDung = new SharePreNguoiDung(this);
        mand = sharePreNguoiDung.getMaNd();
        nguoiDungDao.getNguoiDungById(mand, new NguoiDungDao.OnGetDataSuccess() {
            @Override
            public void onGetNdSuccess(NguoiDung nguoiDung) {
                tvName.setText(nguoiDung.getTenNd());
                tvEmail.setText(nguoiDung.getEmailNd());
                Glide.with(UserActivity.this).load(Uri.parse(nguoiDung.getImageUri())).into(imgUser);
            }
        });
        HomeUserFragment homeUserFragment = new HomeUserFragment();
        homeUserFragment.setViewNum(viewNum);
        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.fr_userActivity, homeUserFragment)
                .commit();

        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        getSupportActionBar().setHomeAsUpIndicator(R.drawable.baseline_menu_24);

        toolbar.setTitleTextColor(Color.BLACK);
    }

    private void initView() {
        toolbar = findViewById(R.id.tbar_userAct);
        drNavUserAct = findViewById(R.id.navView_userAct);
        frameLayout = findViewById(R.id.fr_userActivity);
        drawerLayout = findViewById(R.id.drawerLayout_userAct);
        tvEmail = drNavUserAct.getHeaderView(0).findViewById(R.id.tv_emailHeaderNav);
        tvName = drNavUserAct.getHeaderView(0).findViewById(R.id.tv_nameHeaderNav);
        imgUser = drNavUserAct.getHeaderView(0).findViewById(R.id.img_headerNav);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            drawerLayout.openDrawer(GravityCompat.START);
        }
        return super.onOptionsItemSelected(item);
    }
}