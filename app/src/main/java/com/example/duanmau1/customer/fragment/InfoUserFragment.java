package com.example.duanmau1.customer.fragment;

import android.annotation.SuppressLint;
import android.graphics.Color;
import android.media.Image;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import com.bumptech.glide.Glide;
import com.example.duanmau1.R;
import com.example.duanmau1.dao.NguoiDungDao;
import com.example.duanmau1.dao.SharePreNguoiDung;
import com.example.duanmau1.model.NguoiDung;

public class InfoUserFragment extends Fragment {
    View view;
    ImageView imgInfo, btnChinhSua;
    Button btnUpdate;
    TextView tvName, tvEmail;
    EditText edtGioiTinh, edtDiaChi, edtSdt, edtNgaySinh;
    SharePreNguoiDung sharePreNguoiDung;

    int mand;
    boolean isEnable = true;
    NguoiDungDao nguoiDungDao;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = LayoutInflater.from(getContext()).inflate(R.layout.fragment_thong_tin_user, container, false);
        initView();
        setView();
        return view;
    }

    private void setView() {
        sharePreNguoiDung = new SharePreNguoiDung(getContext());
        nguoiDungDao = new NguoiDungDao(getContext());
        mand = sharePreNguoiDung.getMaNd();
        nguoiDungDao.getNguoiDungById(mand, new NguoiDungDao.OnGetDataSuccess() {
            @Override
            public void onGetNdSuccess(NguoiDung nguoiDung) {
                Glide.with(getContext()).load(Uri.parse(nguoiDung.getImageUri())).into(imgInfo);
                tvName.setText(nguoiDung.getTenNd());
                tvEmail.setText(nguoiDung.getEmailNd());
                edtDiaChi.setText(nguoiDung.getDiaChiNd());
                edtGioiTinh.setText(nguoiDung.getGioiTinhNd());
                edtSdt.setText(nguoiDung.getSdtNd());
                edtNgaySinh.setText(nguoiDung.getNgaySinhNd());
            }
        });

        setOnViewClick();
    }

    private void setOnViewClick() {
        btnChinhSua.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setFocuss(edtDiaChi,edtGioiTinh, edtSdt, edtNgaySinh);
            }
        });

        btnUpdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                nguoiDungDao.getNguoiDungById(mand, new NguoiDungDao.OnGetDataSuccess() {
                    @Override
                    public void onGetNdSuccess(NguoiDung nguoiDung) {
                        String diaChi = edtDiaChi.getText().toString();
                        String ngaySinh = edtNgaySinh.getText().toString();
                        String sdt = edtSdt.getText().toString();
                        String gioiTinh = edtGioiTinh.getText().toString();

                        nguoiDung.setDiaChiNd(diaChi);
                        nguoiDung.setGioiTinhNd(gioiTinh);
                        nguoiDung.setSdtNd(sdt);
                        nguoiDung.setNgaySinhNd(ngaySinh);

                        nguoiDungDao.updateNguoiDung(nguoiDung);

                        isEnable = false;
                        setFocuss(edtDiaChi,edtGioiTinh, edtSdt, edtNgaySinh);
                    }
                });
            }
        });
    }

    private void initView() {
        imgInfo = view.findViewById(R.id.img_infoUser);
        btnChinhSua = view.findViewById(R.id.btn_suaInforUser);
        btnUpdate = view.findViewById(R.id.btn_updateInfoUser);
        tvName =view.findViewById(R.id.tv_nameUserInfo);
        tvEmail = view.findViewById(R.id.tv_emailUserInfo);
        edtGioiTinh = view.findViewById(R.id.edt_gioiTinhInfoUser);
        edtDiaChi = view.findViewById(R.id.edt_diaChiInfoUser);
        edtSdt = view.findViewById(R.id.edt_phoneInfoUser);
        edtNgaySinh = view.findViewById(R.id.edt_birthInfoUser);
    }

    private void setFocuss(EditText editText, EditText editText1, EditText editText2,EditText editText3) {
        if (isEnable) {
            editText.setFocusable(true);
            editText.setFocusableInTouchMode(true);
            editText.setTextColor(Color.BLACK);

            editText1.setFocusable(true);
            editText1.setFocusableInTouchMode(true);
            editText1.setTextColor(Color.BLACK);

            editText2.setFocusable(true);
            editText2.setFocusableInTouchMode(true);
            editText2.setTextColor(Color.BLACK);

            editText3.setFocusable(true);
            editText3.setFocusableInTouchMode(true);
            editText3.setTextColor(Color.BLACK);
        } else {
            editText.setFocusable(false);
            editText.setFocusableInTouchMode(false);
            editText.setTextColor(ContextCompat.getColor(getContext(), R.color.txt_color));

            editText1.setFocusable(false);
            editText1.setFocusableInTouchMode(false);
            editText1.setTextColor(ContextCompat.getColor(getContext(), R.color.txt_color));

            editText2.setFocusable(false);
            editText2.setFocusableInTouchMode(false);
            editText2.setTextColor(ContextCompat.getColor(getContext(), R.color.txt_color));

            editText3.setFocusable(false);
            editText3.setFocusableInTouchMode(false);
            editText3.setTextColor(ContextCompat.getColor(getContext(), R.color.txt_color));
        }

        isEnable = !isEnable;

    }
}
