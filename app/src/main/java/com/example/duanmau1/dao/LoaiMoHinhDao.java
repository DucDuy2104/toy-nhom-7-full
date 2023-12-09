package com.example.duanmau1.dao;

import android.app.ProgressDialog;
import android.content.Context;
import android.net.Uri;

import androidx.annotation.NonNull;

import com.example.duanmau1.model.LoaiMoHinh;
import com.example.duanmau1.model.NguoiDung;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.util.ArrayList;
import java.util.List;

public class LoaiMoHinhDao {
    private ProgressDialog progressDialog;
    private DatabaseReference dbReference;
    private StorageReference storageReference;
    private Context context;
    private MoHinhDao moHinhDao;
    public LoaiMoHinhDao(Context context) {
        this.dbReference  = FirebaseDatabase.getInstance().getReference();
        this.storageReference =  FirebaseStorage.getInstance().getReference();
        this.context = context;
    }

    private void setDialog(String message) {
        progressDialog = new ProgressDialog(context);
        progressDialog.setMessage(message);
        progressDialog.setCancelable(false);
    }
    public interface OnGetListSuccess {
        void onGetListSuccess(List<LoaiMoHinh> loaiMoHinhList);
    }

    public interface OnGetLoaiSuccess {
        void onGetLoaiSuccess(LoaiMoHinh loaiMoHinh);
    }

    public void getAllLoaiMoHinh(OnGetListSuccess onGetListSuccess) {
        List<LoaiMoHinh> loaiMoHinhList = new ArrayList<>();
        dbReference.child("list_loai").get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DataSnapshot> task) {
                for (DataSnapshot userSnapshot : task.getResult().getChildren()) {
                    LoaiMoHinh loaiMoHinh = userSnapshot.getValue(LoaiMoHinh.class);
                    loaiMoHinhList.add(loaiMoHinh);
                }
                onGetListSuccess.onGetListSuccess(loaiMoHinhList);
            }
        });
    }

    public void addLoaiMoHinh(LoaiMoHinh loaiMoHinh) {
        setDialog("Đang thêm...");
        progressDialog.show();
        getAllLoaiMoHinh(new OnGetListSuccess() {
            @Override
            public void onGetListSuccess(List<LoaiMoHinh> loaiMoHinhList) {
                int maLmh = 0;
                if (loaiMoHinhList.size() > 0) {
                    int maLmhMax = 0;
                    for (LoaiMoHinh loaiMoHinh1: loaiMoHinhList) {
                        if (loaiMoHinh1.getMaLmh()> maLmhMax) {
                            maLmhMax = loaiMoHinh1.getMaLmh();
                        }
                    }
                    maLmh = maLmhMax + 1;
                }
                loaiMoHinh.setMaLmh(maLmh);
                storageReference.child("list_img_loai").child("img_loai_" + loaiMoHinh.getMaLmh()).putFile(Uri.parse(loaiMoHinh.getImgUri()))
                        .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                            @Override
                            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                                storageReference.child("list_img_loai").child("img_loai_" + loaiMoHinh.getMaLmh())
                                        .getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                                            @Override
                                            public void onSuccess(Uri uri) {
                                                loaiMoHinh.setImgUri(uri.toString());
                                                dbReference.child("list_loai").child("loai_" + loaiMoHinh.getMaLmh()).setValue(loaiMoHinh).addOnSuccessListener(new OnSuccessListener<Void>() {
                                                    @Override
                                                    public void onSuccess(Void unused) {
                                                        progressDialog.dismiss();
                                                    }
                                                });
                                            }
                                        });
                            }
                        });
            }
        });

    }

    public void updateLoaiMoHinh(LoaiMoHinh loaiMoHinh) {
        storageReference.child("list_img_loai").child("img_loai_" + loaiMoHinh.getMaLmh()).putFile(Uri.parse(loaiMoHinh.getImgUri()))
                .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                        storageReference.child("list_img_loai").child("img_loai_" + loaiMoHinh.getMaLmh())
                                .getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                                    @Override
                                    public void onSuccess(Uri uri) {
                                        loaiMoHinh.setImgUri(uri.toString());
                                        dbReference.child("list_loai").child("loai_" + loaiMoHinh.getMaLmh()).setValue(loaiMoHinh);
                                    }
                                });
                    }
                });
    }

    public void deleteLoaiMoHinh(int maLoai){
        setDialog("Đang xóa...");
        progressDialog.show();
        moHinhDao =new MoHinhDao(context);
        dbReference.child("list_loai").child("loai_" + maLoai).removeValue();
        storageReference.child("list_img_loai").child("img_loai_" + maLoai).delete().addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void unused) {
                moHinhDao.deleteAllMoHinhByLoai(maLoai);
                progressDialog.dismiss();
            }
        });
    }

    public  void getLoaiMoHinhById(int maLoai, OnGetLoaiSuccess onGetLoaiSuccess) {
        dbReference.child("list_loai").child("loai_" + maLoai).get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DataSnapshot> task) {
                LoaiMoHinh loaiMoHinh  = task.getResult().getValue(LoaiMoHinh.class);
                onGetLoaiSuccess.onGetLoaiSuccess(loaiMoHinh);
            }
        });
    }




}
