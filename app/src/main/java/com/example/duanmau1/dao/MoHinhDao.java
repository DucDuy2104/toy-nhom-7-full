package com.example.duanmau1.dao;

import android.app.ProgressDialog;
import android.content.Context;
import android.net.Uri;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.example.duanmau1.customview.CustomToast;
import com.example.duanmau1.model.HoaDon;
import com.example.duanmau1.model.HoaDonChiTiet;
import com.example.duanmau1.model.LoaiMoHinh;
import com.example.duanmau1.model.MoHinh;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
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

public class MoHinhDao {
    private ProgressDialog progressDialog;
    private DatabaseReference dbReference;
    private StorageReference storageReference;
    private LoaiMoHinhDao loaiMoHinhDao;
    private HoaDonChiTietDao hoaDonChiTietDao;
    private HoaDonDao hoaDonDao;
    private Context context;
    public MoHinhDao(Context context) {
        this.dbReference  = FirebaseDatabase.getInstance().getReference("list_mo_hinh");
        this.storageReference =  FirebaseStorage.getInstance().getReference("list_img_mo_hinh");
        this.context = context;
        setDialog();
    }

    private void setDialog() {
        progressDialog = new ProgressDialog(context);
        progressDialog.setMessage("Loading...");
        progressDialog.setCancelable(false);
    }

    public interface OnGetListMoHinhSuccess {
        void onGetSuccess(List<MoHinh> moHinhList);
    }

    public interface OnGetMoHinhSuccess {
        void onGetSuccess(MoHinh moHinh, LoaiMoHinh loaiMoHinh);
    }

    public interface OnCountMoHinh{
        void onCountComplete(int count);
    }

    public void getAllMoHinh(OnGetListMoHinhSuccess onGetListMoHinhSuccess) {
        List<MoHinh> moHinhList  = new ArrayList<>();
        dbReference.get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DataSnapshot> task) {
                for (DataSnapshot mhSnapshot: task.getResult().getChildren()) {
                    MoHinh moHinh = mhSnapshot.getValue(MoHinh.class);
                    moHinhList.add(moHinh);
                }
                onGetListMoHinhSuccess.onGetSuccess(moHinhList);
            }
        });
    }

    public void addMoHinh(MoHinh moHinh) {
        progressDialog.show();
        getAllMoHinh(new OnGetListMoHinhSuccess() {
            @Override
            public void onGetSuccess(List<MoHinh> moHinhList) {
                int mamh = 0;

                if (moHinhList.size() > 0) {
                    int maxMamh = 0;
                    for (MoHinh moHinh1:moHinhList) {
                        if (moHinh1.getMaMh() > maxMamh) {
                            maxMamh = moHinh1.getMaMh();
                        }
                    }
                    mamh = maxMamh + 1;
                }
                moHinh.setMaMh(mamh);
                storageReference.child("img_mo_hinh_" + moHinh.getMaMh()).putFile(Uri.parse(moHinh.getImgUri()))
                        .addOnCompleteListener(new OnCompleteListener<UploadTask.TaskSnapshot>() {
                            @Override
                            public void onComplete(@NonNull Task<UploadTask.TaskSnapshot> task) {
                                storageReference.child("img_mo_hinh_" + moHinh.getMaMh()).getDownloadUrl()
                                        .addOnSuccessListener(new OnSuccessListener<Uri>() {
                                            @Override
                                            public void onSuccess(Uri uri) {
                                                moHinh.setImgUri(uri.toString());
                                                dbReference.child("mo_hinh_" + moHinh.getMaMh()).setValue(moHinh)
                                                        .addOnSuccessListener(new OnSuccessListener<Void>() {
                                                            @Override
                                                            public void onSuccess(Void unused) {
                                                                CustomToast customToast = new CustomToast(context, CustomToast.SUCCESS, "Add successfully!");
                                                                customToast.showToast();
                                                                progressDialog.dismiss();
                                                            }
                                                        })
                                                        .addOnFailureListener(new OnFailureListener() {
                                                            @Override
                                                            public void onFailure(@NonNull Exception e) {
                                                                CustomToast customToast = new CustomToast(context, CustomToast.ERROR, "Failure!");
                                                                customToast.showToast();
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

    public void updateMoHinh(MoHinh moHinh) {
        dbReference.child("mo_hinh_" + moHinh.getMaMh()).setValue(moHinh);
    }

    public void deleteMoHinh(int mamh) {
        Log.e("ducduy", "deleteMoHinh: start" );

        hoaDonDao = new HoaDonDao(context);
        hoaDonChiTietDao = new HoaDonChiTietDao(context);
        dbReference.child("mo_hinh_" + mamh).removeValue().addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void unused) {
                Log.e("ducduy", "onSuccess: deletedMohinhSuccess");
                hoaDonDao.deleteAllHoaDonByMaMh(mamh);
                hoaDonChiTietDao.deleteAllHdctByMaMh(mamh);
            }
        });
        storageReference.child("img_mo_hinh_" + mamh).delete();
    }

    public void getMoHinhById(int mamh, OnGetMoHinhSuccess onGetMoHinhSuccess) {

        loaiMoHinhDao = new LoaiMoHinhDao(context);
        dbReference.child("mo_hinh_" + mamh).get().addOnSuccessListener(new OnSuccessListener<DataSnapshot>() {
            @Override
            public void onSuccess(DataSnapshot dataSnapshot) {
                MoHinh moHinh  = dataSnapshot.getValue(MoHinh.class);
                loaiMoHinhDao.getLoaiMoHinhById(moHinh.getMaLmh(), new LoaiMoHinhDao.OnGetLoaiSuccess() {
                    @Override
                    public void onGetLoaiSuccess(LoaiMoHinh loaiMoHinh) {
                        onGetMoHinhSuccess.onGetSuccess(moHinh, loaiMoHinh);
                    }
                });
            }
        });
    }

    public void countMoHinhByLoai(int maLmh, OnCountMoHinh onCountMoHinh) {
        getAllMoHinh(new OnGetListMoHinhSuccess() {
            @Override
            public void onGetSuccess(List<MoHinh> moHinhList) {
                int count  = 0;
                for (MoHinh moHinh: moHinhList){
                    if (moHinh.getMaLmh() == maLmh) {
                        count++;
                    }
                }
                onCountMoHinh.onCountComplete(count);
            }
        });
    }

    public void updateSold(int mamh,int soldAdd) {
        getMoHinhById(mamh, new OnGetMoHinhSuccess() {
            @Override
            public void onGetSuccess(MoHinh moHinh, LoaiMoHinh loaiMoHinh) {
                dbReference.child("mo_hinh_" + moHinh.getMaMh()).child("soLuongDaBan")
                        .setValue(moHinh.getSoLuongDaBan() + soldAdd);
            }
        });
    }

    public void deleteAllMoHinhByLoai(int maLmh){
        Log.e("ducduy", "deleteAllMoHinhByLoai: start");
        getAllMoHinh(new OnGetListMoHinhSuccess() {
            @Override
            public void onGetSuccess(List<MoHinh> moHinhList) {
                for (MoHinh moHinh: moHinhList) {
                    if (moHinh.getMaLmh() == maLmh) {
                        deleteMoHinh(moHinh.getMaMh());
                    }
                }
            }
        });

    }


}
