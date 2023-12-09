package com.example.duanmau1.admin.activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.duanmau1.R;
import com.example.duanmau1.admin.fragment.HomeAdminFragment;
import com.example.duanmau1.customer.activity.UserActivity;
import com.example.duanmau1.dao.NguoiDungDao;
import com.example.duanmau1.dao.SharePreNguoiDung;
import com.example.duanmau1.model.NguoiDung;
import com.example.duanmau1.sign.SignInActivity;
import com.google.android.material.navigation.NavigationView;

import org.checkerframework.checker.units.qual.A;

public class AdminActivity extends AppCompatActivity {

    private Toolbar toolbar;
    private FrameLayout frameLayout;
    private NavigationView navigationView;
    private DrawerLayout drawerLayout;

    private int mand;
    private SharePreNguoiDung sharePreNguoiDung;
    private NguoiDungDao nguoiDungDao ;
    private  TextView tvName, tvEmail;
    private ImageView imgUser;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin);
        initView();
        setView();
        setNavOnItemSelect();
    }

    private void initView() {
        toolbar = findViewById(R.id.tb_adminAct);
        frameLayout = findViewById(R.id.frLayout_adminAct);
        navigationView = findViewById(R.id.drawerNav_adminAct);
        drawerLayout =findViewById(R.id.drawerLayout_adminAct);
        tvName = navigationView.getHeaderView(0).findViewById(R.id.tv_nameHeaderNav);
        tvEmail = navigationView.getHeaderView(0).findViewById(R.id.tv_emailHeaderNav);
        imgUser = navigationView.getHeaderView(0).findViewById(R.id.img_headerNav);
    }

    private void setView() {
        nguoiDungDao = new NguoiDungDao(this);
        sharePreNguoiDung = new SharePreNguoiDung(this);
        mand = sharePreNguoiDung.getMaNd();

        nguoiDungDao.getNguoiDungById(mand, new NguoiDungDao.OnGetDataSuccess() {
            @Override
            public void onGetNdSuccess(NguoiDung nguoiDung) {
                tvEmail.setText(nguoiDung.getEmailNd());
                tvName.setText(nguoiDung.getTenNd());
                Glide.with(AdminActivity.this).load(Uri.parse(nguoiDung.getImageUri())).into(imgUser);
            }
        });
        //set ToolBar
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeAsUpIndicator(R.drawable.baseline_menu_24);

        //set first Fragment
        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.frLayout_adminAct, new HomeAdminFragment())
                .commit();

    }

    private void setNavOnItemSelect() {
        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                if (item.getItemId() == R.id.home_drawerNav) {
                    getSupportFragmentManager()
                            .beginTransaction()
                            .replace(R.id.frLayout_adminAct, new HomeAdminFragment())
                            .commit();
                    drawerLayout.close();
                } else if (item.getItemId() == R.id.exit_drawerNav) {
                    Intent intent = new Intent(AdminActivity.this, SignInActivity.class);
                    startActivity(intent);
                } else if (item.getItemId() == R.id.changePass_drawerNav) {
                    setDialog();
                    drawerLayout.close();
                }
                return true;
            }
        });
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == android.R.id.home){
            drawerLayout.openDrawer(GravityCompat.START);
        }
        return super.onOptionsItemSelected(item);
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
                            Toast.makeText(AdminActivity.this, "Sai mật khẩu!", Toast.LENGTH_SHORT).show();
                            return;
                        } else if (!newPass.trim().equals(rePass)) {
                            Toast.makeText(AdminActivity.this, "Mật khẩu mới không khớp!", Toast.LENGTH_SHORT).show();
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


}