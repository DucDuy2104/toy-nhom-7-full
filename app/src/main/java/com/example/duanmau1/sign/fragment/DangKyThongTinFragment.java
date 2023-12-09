package com.example.duanmau1.sign.fragment;

import android.Manifest;
import android.app.Activity;
import android.app.DatePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.SpinnerAdapter;
import android.widget.Toast;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import com.example.duanmau1.R;
import com.example.duanmau1.customview.CustomToast;
import com.example.duanmau1.dao.NguoiDungDao;
import com.example.duanmau1.dao.SharePreNguoiDung;
import com.example.duanmau1.model.NguoiDung;
import com.example.duanmau1.sign.SignInActivity;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class DangKyThongTinFragment extends Fragment {
    View view;
    EditText edtName, edtEmail, edtDiaChi, edtSdt, edtNgaySinh;
    Spinner spnGt;
    Context context;
    SharePreNguoiDung sharePreNguoiDung;

    Uri selectedUri;
    ImageView img;

    NguoiDungDao nguoiDungDao;
    public DangKyThongTinFragment(Context context) {
        this.context = context;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_dang_ky_thong_tin, container, false);
        initView();
        setView();
        return view;
    }

    private void setView() {
        nguoiDungDao = new NguoiDungDao(context);
        sharePreNguoiDung = new SharePreNguoiDung(context);
        List<String> gioiTinh = new ArrayList<>();
        gioiTinh.add("Nam");
        gioiTinh.add("Nữ");
        SpinnerAdapter spinnerAdapter = new com.example.duanmau1.admin.adapter.SpinnerAdapter(context, R.layout.custom_item_spn, gioiTinh);
        spnGt.setAdapter(spinnerAdapter);
        edtNgaySinh.setFocusable(false);
        edtNgaySinh.setFocusableInTouchMode(false);
        edtNgaySinh.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DatePickerDialog datePickerDialog = new DatePickerDialog(
                        requireContext(),
                        new DatePickerDialog.OnDateSetListener() {
                            @Override
                            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                                // Xử lý ngày được chọn
                                String selectedDate = dayOfMonth + "/" + (month + 1) + "/" + year;
                                edtNgaySinh.setText(selectedDate);
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

        img.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                checkPermissions();
            }
        });


    }
    public void onCreateFr() {
        String name = edtName.getText().toString();
        String gioiTinh = spnGt.getSelectedItem().toString();
        String diaChi = edtDiaChi.getText().toString();
        String email = edtEmail.getText().toString();
        String soDienThoai = edtSdt.getText().toString();
        String ngaySinh = edtNgaySinh.getText().toString();
        if (name.trim().equals("") || diaChi.trim().equals("") || email.trim().equals("") || soDienThoai.trim().equals("") || ngaySinh.trim().equals("") || selectedUri == null) {
            CustomToast customToast = new CustomToast(context, CustomToast.WARNING, "Vui lòng nhập đủ thông tin!");
            customToast.showToast();
        } else {
            String username = sharePreNguoiDung.getUserName();
            String pass = sharePreNguoiDung.getPass();
            NguoiDung nguoiDung = new NguoiDung(name, gioiTinh, ngaySinh, soDienThoai, email, diaChi, username, pass, selectedUri.toString(), 1);
            nguoiDungDao.addNguoiDung(nguoiDung);
            CustomToast customToast = new CustomToast(context, CustomToast.SUCCESS, "Sign up successfully!");
            customToast.showToast();
            Intent intent = new Intent(context, SignInActivity.class);
            startActivity(intent);
            sharePreNguoiDung.clearData();
        }
    }

    private void initView() {
        edtName = view.findViewById(R.id.edt_nameDangKy);
        edtEmail= view.findViewById(R.id.edt_emailDangKy);
        edtDiaChi = view.findViewById(R.id.edt_diachiDangKy);
        edtSdt = view.findViewById(R.id.edt_sdtDangKy);
        edtNgaySinh = view.findViewById(R.id.edt_ngaySinhDangKy);
        spnGt = view.findViewById(R.id.spn_gioiTinhDangKy);
        img = view.findViewById(R.id.img_infoDangKy);
    }

    private void checkPermissions() {
        if (android.os.Build.VERSION.SDK_INT < 33) {
            if (ContextCompat.checkSelfPermission(requireContext(), Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_DENIED) {
                requestPermissionLauncher.launch(Manifest.permission.READ_EXTERNAL_STORAGE);
            } else {
                pickImageFromGallery();
            }
        } else {
            if (ContextCompat.checkSelfPermission(requireContext(), Manifest.permission.READ_MEDIA_IMAGES) == PackageManager.PERMISSION_DENIED) {
                requestPermissionLauncher.launch(Manifest.permission.READ_MEDIA_IMAGES);
            } else {
                pickImageFromGallery();
            }
        }

    }

    private void pickImageFromGallery() {
        Intent intent = new Intent(Intent.ACTION_PICK);
        intent.setType("image/*");
        pickImageLauncher.launch(intent);
    }


    private final ActivityResultLauncher<String> requestPermissionLauncher = registerForActivityResult(
            new ActivityResultContracts.RequestPermission(), result -> {
                if (result) {
                    // Quyền đã được cấp phép sau khi người dùng chấp nhận yêu cầu
                    // Bạn có thể thực hiện các hành động cần thiết ở đây
                    pickImageFromGallery();
                } else {
                    // Quyền không được cấp phép
                    // Hiển thị thông báo hoặc thực hiện hành động phù hợp
                    Toast.makeText(requireContext(), "Permission denied", Toast.LENGTH_SHORT).show();
                }
            });

    private final ActivityResultLauncher<Intent> pickImageLauncher = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(), result -> {
                if (result.getResultCode() == Activity.RESULT_OK && result.getData() != null) {
                    Uri selectedImageUri = result.getData().getData();
                    selectedUri = selectedImageUri;
                    img.setImageURI(selectedUri);
                }
            });

}
