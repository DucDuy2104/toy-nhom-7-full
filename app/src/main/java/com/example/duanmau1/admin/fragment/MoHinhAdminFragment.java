package com.example.duanmau1.admin.fragment;

import android.Manifest;
import android.app.Activity;
import android.app.DatePickerDialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.SimpleAdapter;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.duanmau1.R;
import com.example.duanmau1.admin.adapter.MoHinhAdminAdapter;
import com.example.duanmau1.admin.adapter.SpinnerAdapter;
import com.example.duanmau1.dao.LoaiMoHinhDao;
import com.example.duanmau1.dao.MoHinhDao;
import com.example.duanmau1.model.LoaiMoHinh;
import com.example.duanmau1.model.MoHinh;
import com.google.android.material.bottomsheet.BottomSheetBehavior;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Random;

public class MoHinhAdminFragment extends Fragment {
    View view;
    RecyclerView recMoHinh;
    MoHinhAdminAdapter adapter;
    MoHinhDao moHinhDao;
    List<MoHinh> mListMoHinh;
    FloatingActionButton fBtnAdd;
    LoaiMoHinhDao loaiMoHinhDao;
    ImageView imgMoHinh;
    Uri selectedUri;
    private final DatabaseReference mDatabase= FirebaseDatabase.getInstance().getReference("list_mo_hinh");
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_mo_hinh_admin, container, false);
        initView();
        setView();
        setOnClick();
        return view;
    }

    private void setOnClick() {
        fBtnAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                View viewBottomDialog = LayoutInflater.from(getContext()).inflate(R.layout.dialog_add_mo_hinh, null);
                BottomSheetDialog bottomSheetDialog = new BottomSheetDialog(getContext());
                bottomSheetDialog.setContentView(viewBottomDialog);
                bottomSheetDialog.setCancelable(true);
                bottomSheetDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                //mở rộng toàn bộ
                BottomSheetBehavior<View> behavior = BottomSheetBehavior.from((View)viewBottomDialog.getParent());
                behavior.setState(BottomSheetBehavior.STATE_EXPANDED);

                imgMoHinh = viewBottomDialog.findViewById(R.id.img_addMoHinhDialog);
                EditText edtNameAddMh = viewBottomDialog.findViewById(R.id.edt_tenAddMoHinh);
                EditText edtTiLeAddMh = viewBottomDialog.findViewById(R.id.edt_tiLeAddMoHinh);
                EditText edtNgaySxAddMh = viewBottomDialog.findViewById(R.id.edt_ngaySxAddMoHinh);
                EditText edtChatLieu = viewBottomDialog.findViewById(R.id.edt_chatLieuAddMoHinh);
                EditText edtXuatSuAddMh = viewBottomDialog.findViewById(R.id.edt_xuatSuAddMoHinh);
                EditText edtGiaBanAddMh = viewBottomDialog.findViewById(R.id.edt_giaBanAddMoHinh);
                EditText edtSLuongAddMh = viewBottomDialog.findViewById(R.id.edt_soLuongAddMoHinh);
                EditText edtChieuCaoAddMh = viewBottomDialog.findViewById(R.id.edt_chieuCaoAddMoHinh);
                EditText edtGioiHanDoTuoiAddMh = viewBottomDialog.findViewById(R.id.edt_gioiHanTuoiAddMoHinh);
                Spinner spinnerLoaiAddMh = viewBottomDialog.findViewById(R.id.spn_loaiAddMoHinh);
                RadioButton rdoNamAddMh = viewBottomDialog.findViewById(R.id.rdo_namAddMoHinh);
                Button btnCancelAddMh = viewBottomDialog.findViewById(R.id.btn_cancelAddMoHinh);
                Button btnAddMh = viewBottomDialog.findViewById(R.id.btn_addMoHinh);

                edtNgaySxAddMh.setFocusable(false);
                edtNgaySxAddMh.setFocusableInTouchMode(false);

                edtNgaySxAddMh.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        DatePickerDialog datePickerDialog = new DatePickerDialog(
                                requireContext(),
                                new DatePickerDialog.OnDateSetListener() {
                                    @Override
                                    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                                        // Xử lý ngày được chọn
                                        String selectedDate = dayOfMonth + "/" + (month + 1) + "/" + year;
                                        edtNgaySxAddMh.setText(selectedDate);
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

                loaiMoHinhDao.getAllLoaiMoHinh(new LoaiMoHinhDao.OnGetListSuccess() {
                    @Override
                    public void onGetListSuccess(List<LoaiMoHinh> loaiMoHinhList) {
                        List<String> listTenLoai = new ArrayList<>();
                        for (LoaiMoHinh loaiMoHinh: loaiMoHinhList) {
                            listTenLoai.add(loaiMoHinh.getTenLoai());
                        }
                        SpinnerAdapter spnAdapter = new SpinnerAdapter(getContext(), R.layout.custom_item_spn, listTenLoai);
                        spnAdapter.setDropDownViewResource(R.layout.custom_item_spn);
                        spinnerLoaiAddMh.setAdapter(spnAdapter);
                    }
                });


                imgMoHinh.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        checkPermissions();
                    }
                });

                btnCancelAddMh.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        bottomSheetDialog.dismiss();
                    }
                });

                btnAddMh.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        loaiMoHinhDao.getAllLoaiMoHinh(new LoaiMoHinhDao.OnGetListSuccess() {
                            @Override
                            public void onGetListSuccess(List<LoaiMoHinh> loaiMoHinhList) {
                                String chatLieu = edtChatLieu.getText().toString();
                                String nameMh = edtNameAddMh.getText().toString();
                                String tiLeMh = edtTiLeAddMh.getText().toString();
                                String ngaySx = edtNgaySxAddMh.getText().toString();
                                String xuatSu = edtXuatSuAddMh.getText().toString();
                                int giaBan = Integer.parseInt(edtGiaBanAddMh.getText().toString());
                                int soLuong = Integer.parseInt(edtSLuongAddMh.getText().toString());
                                int chieuCao = Integer.parseInt(edtChieuCaoAddMh.getText().toString());
                                int gioiHanDoTuoi = Integer.parseInt(edtGioiHanDoTuoiAddMh.getText().toString());
                                String tenLoai = spinnerLoaiAddMh.getSelectedItem().toString();
                                String gioiTinh = rdoNamAddMh.isChecked()? "Nam" : "Nữ";
                                loaiMoHinhDao.getAllLoaiMoHinh(new LoaiMoHinhDao.OnGetListSuccess() {
                                    @Override
                                    public void onGetListSuccess(List<LoaiMoHinh> loaiMoHinhList) {
                                        for (int i = 0; i< loaiMoHinhList.size(); i++) {
                                            if (loaiMoHinhList.get(i).getTenLoai().equals(tenLoai.trim())) {
                                                int maLoai = loaiMoHinhList.get(i).getMaLmh();
                                                MoHinh moHinh = new MoHinh(nameMh, chatLieu, tiLeMh, ngaySx, gioiTinh, xuatSu, 4.5f,
                                                        giaBan, soLuong, 0, chieuCao, gioiHanDoTuoi, maLoai, selectedUri.toString());
                                                moHinhDao.addMoHinh(moHinh);
                                                bottomSheetDialog.dismiss();
                                            }
                                        }
                                    }
                                });
                            }
                        });

                    }
                });

                bottomSheetDialog.show();
            }
        });
    }

    private void setRealTime() {
        mDatabase.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                MoHinh moHinh = snapshot.getValue(MoHinh.class);
                mListMoHinh.add(moHinh);
                adapter.notifyDataSetChanged();
            }

            @Override
            public void onChildChanged(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                MoHinh moHinh = snapshot.getValue(MoHinh.class);
                for (int i = 0; i < mListMoHinh.size(); i++) {
                    if (moHinh.getMaMh() == mListMoHinh.get(i).getMaMh()) {
                        mListMoHinh.set(i, moHinh);
                        adapter.notifyDataSetChanged();
                        return;
                    }
                }
            }

            @Override
            public void onChildRemoved(@NonNull DataSnapshot snapshot) {
                MoHinh moHinh = snapshot.getValue(MoHinh.class);
                for (int i = 0; i < mListMoHinh.size(); i++) {
                    if (moHinh.getMaMh() == mListMoHinh.get(i).getMaMh()) {
                        mListMoHinh.remove(i);
                        adapter.notifyDataSetChanged();
                        return;
                    }
                }

            }

            @Override
            public void onChildMoved(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });


    }

    private void setView() {
        loaiMoHinhDao = new LoaiMoHinhDao(getContext());
        moHinhDao = new MoHinhDao(getContext());
        mListMoHinh = new ArrayList<>();
        adapter = new MoHinhAdminAdapter(mListMoHinh, getContext());
        recMoHinh.setLayoutManager(new LinearLayoutManager(getContext()));
        recMoHinh.setAdapter(adapter);
        setRealTime();
    }

    private void initView() {
        fBtnAdd = view.findViewById(R.id.fBtn_addMoHinh);
        recMoHinh = view.findViewById(R.id.rec_moHinhAdminFr);
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
                    imgMoHinh.setImageURI(selectedUri);
                }
            });

    private void pickImageFromGallery() {
        Intent intent = new Intent(Intent.ACTION_PICK);
        intent.setType("image/*");
        pickImageLauncher.launch(intent);
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

}
