package com.example.duanmau1.admin.fragment;


import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.drawable.ColorDrawable;
import android.media.Image;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.duanmau1.R;
import com.example.duanmau1.admin.activity.AdminActivity;
import com.example.duanmau1.admin.adapter.LoaiAdminAdapter;
import com.example.duanmau1.dao.LoaiMoHinhDao;
import com.example.duanmau1.model.LoaiMoHinh;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class LoaiAdminFragment extends Fragment {
    ImageView imgSelected;
    View view;
    RecyclerView recLoai;
    LoaiAdminAdapter loaiAdapter;
    LoaiMoHinhDao loaiMoHinhDao;
    FloatingActionButton fBtnAdd;
    Uri selectedUri;
    List<LoaiMoHinh> mLoaiMoHinhList;

    private DatabaseReference mDatabase= FirebaseDatabase.getInstance().getReference("list_loai");

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_loai_admin, container, false);
        initView();
        setView();
        setOnClick();
        return view;
    }

    private void setRealtime() {
        mDatabase.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                LoaiMoHinh loaiMoHinh =snapshot.getValue(LoaiMoHinh.class);
                mLoaiMoHinhList.add(loaiMoHinh);
                loaiAdapter.notifyDataSetChanged();
            }

            @Override
            public void onChildChanged(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {

            }

            @Override
            public void onChildRemoved(@NonNull DataSnapshot snapshot) {
                LoaiMoHinh loaiMoHinh =snapshot.getValue(LoaiMoHinh.class);
                for (int i = 0; i < mLoaiMoHinhList.size(); i++) {
                    if (mLoaiMoHinhList.get(i).getMaLmh() == loaiMoHinh.getMaLmh()) {
                        mLoaiMoHinhList.remove(i);
                        loaiAdapter.notifyDataSetChanged();
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

    private void setOnClick() {
        fBtnAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                View viewAddDialog = LayoutInflater.from(getContext()).inflate(R.layout.dialog_add_loai, null);
                AlertDialog dialog = new AlertDialog.Builder(getContext()).create();
                dialog.setView(viewAddDialog);
                imgSelected = viewAddDialog.findViewById(R.id.img_addLoaiDialog);
                EditText edtName = viewAddDialog.findViewById(R.id.edt_tenAddLoaiDialog);
                Button btnCanCel = viewAddDialog.findViewById(R.id.btn_cancelLoaiDialog);
                Button btnAdd = viewAddDialog.findViewById(R.id.btn_addLoaiDialog);

                imgSelected.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        checkPermissions();
                    }
                });

                btnAdd.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        String name = edtName.getText().toString();
                        if (name.equals("")) {
                            Toast.makeText(getContext(), "Vui Lòng Nhập Tên Loại!", Toast.LENGTH_SHORT).show();
                            return;
                        } else if (selectedUri == null) {
                            Toast.makeText(getContext(), "Vui Lòng Chọn Ảnh!", Toast.LENGTH_SHORT).show();
                            return;
                        }

                        LoaiMoHinh loaiMoHinh = new LoaiMoHinh(name, selectedUri.toString());
                        loaiMoHinhDao.addLoaiMoHinh(loaiMoHinh);
                        dialog.dismiss();
                    }
                });

                btnCanCel.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        dialog.dismiss();
                    }
                });

                dialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
                dialog.show();
            }
        });
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

    private void setView() {
        loaiMoHinhDao = new LoaiMoHinhDao(getContext());
        mLoaiMoHinhList= new ArrayList<>();
        loaiAdapter = new LoaiAdminAdapter(mLoaiMoHinhList, getContext());
        recLoai.setAdapter(loaiAdapter);
        recLoai.setLayoutManager(new LinearLayoutManager(getContext()));
        setRealtime();
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
            imgSelected.setImageURI(selectedUri);
        }
    });

    private void initView() {
        recLoai = view.findViewById(R.id.rec_loaiAdminFr);
        fBtnAdd = view.findViewById(R.id.fBtn_addLoai);
    }

    @Override
    public void onResume() {
        super.onResume();
        loaiAdapter.notifyDataSetChanged();
    }
}
