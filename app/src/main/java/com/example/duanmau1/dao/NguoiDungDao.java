package com.example.duanmau1.dao;

import android.app.ProgressDialog;
import android.content.Context;
import android.net.Uri;
import android.os.Handler;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.example.duanmau1.customview.CustomToast;
import com.example.duanmau1.model.HoaDon;
import com.example.duanmau1.model.NguoiDung;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CountDownLatch;

public class NguoiDungDao {

    private ProgressDialog progressDialog;
    private DatabaseReference dbUserReference;
    private StorageReference storageReference;
    private HoaDonDao hoaDonDao;
    private HoaDonChiTietDao hoaDonChiTietDao;
    private Context context;
    public NguoiDungDao(Context context) {
        this.dbUserReference  = FirebaseDatabase.getInstance().getReference();
        this.storageReference =  FirebaseStorage.getInstance().getReference();
        this.context = context;
        setDialog();
    }

    private void setDialog() {
        progressDialog = new ProgressDialog(context);
        progressDialog.setMessage("Loading...");
        progressDialog.setCancelable(false);
    }

    public interface OnGetDataSuccess {
        void onGetNdSuccess(NguoiDung nguoiDung);
    }

    public interface OnGetListSuccess {
        void onGetListSuccess(List<NguoiDung> nguoiDungs);
    }

    public interface OnCheckUserComplete {
        void onCheckComplete(boolean isSuccess, int mand, int role);
    }

    //lấy toàn bộ người dùng
    public void getListNguoiDung(OnGetListSuccess onGetListSuccess){
        List<NguoiDung> users = new ArrayList<>();
        dbUserReference.child("list_user").get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DataSnapshot> task) {
                for (DataSnapshot userSnapshot : task.getResult().getChildren()) {
                    NguoiDung user = userSnapshot.getValue(NguoiDung.class);
                    users.add(user);
                }
                onGetListSuccess.onGetListSuccess(users);
            }
        });
    };

    // thêm một người dùng
    public void addNguoiDung(NguoiDung nguoiDung) {
        // Lấy danh sách người dùng để xác định ID mới
        getListNguoiDung(new OnGetListSuccess() {
            @Override
            public void onGetListSuccess(List<NguoiDung> nguoiDungs) {
                int newId = 0; // Giá trị mặc định nếu danh sách trống
                if (nguoiDungs.size() > 0) {
                    int mandMax = 0;
                    for (NguoiDung nguoiDung1: nguoiDungs) {
                        if (nguoiDung1.getIdNd() > mandMax) {
                            mandMax = nguoiDung1.getIdNd();
                        }
                    }
                    newId = mandMax + 1;
                }

                nguoiDung.setIdNd(newId);

                // Tải hình ảnh lên Firebase Storage
                StorageReference imageRef = storageReference.child("list_img_user").child("img_user_" + nguoiDung.getIdNd());
                imageRef.putFile(Uri.parse(nguoiDung.getImageUri()))
                        .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                            @Override
                            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                                // Lấy URI hình ảnh sau khi tải lên thành công
                                imageRef.getDownloadUrl()
                                        .addOnSuccessListener(new OnSuccessListener<Uri>() {
                                            @Override
                                            public void onSuccess(Uri uri) {
                                                nguoiDung.setImageUri(uri.toString());
                                                // Thêm người dùng vào Firebase Realtime Database
                                                dbUserReference.child("list_user").child("user_" + nguoiDung.getIdNd()).setValue(nguoiDung);
                                            }
                                        });
                            }
                        });
            }
        });
    }


    // cập nhật người dùng
    public void updateNguoiDung(NguoiDung nguoiDung) {
        progressDialog.show();
        dbUserReference.child("list_user").child("user_" + nguoiDung.getIdNd()).setValue(nguoiDung)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void unused) {
                        progressDialog.dismiss();
                        CustomToast customToast = new CustomToast(context, CustomToast.SUCCESS, "Update sucessfully!");
                        customToast.showToast();
                    }
                });


    }
    // xóa người dùng
    public void deleteNguoiDung(int mand) {
        hoaDonDao = new HoaDonDao(context);
        hoaDonChiTietDao = new HoaDonChiTietDao(context);
        storageReference.child("list_img_user").child("img_user_" + mand).delete();

        dbUserReference.child("list_user").child("user_" + mand).removeValue()
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void unused) {
                        hoaDonChiTietDao.deleteAllHdctByMand(mand);
                        hoaDonDao.deleteAllHoaDonByMand(mand);
                        CustomToast customToast =new CustomToast(context, CustomToast.SUCCESS, "Deleted successfully!");
                        customToast.showToast();
                    }
                });
    }

    // lấy người dùng bằng mã người dùng
    public void getNguoiDungById(int mand, final OnGetDataSuccess onGetDataSuccess) {
        dbUserReference.child("list_user").child("user_" + mand).get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DataSnapshot> task) {
                NguoiDung nguoiDung= task.getResult().getValue(NguoiDung.class);
                onGetDataSuccess.onGetNdSuccess(nguoiDung);
            }
        });
    }

    public void checkNguoiDung(String username, String pass, OnCheckUserComplete onCheckUserComplete ) {
        progressDialog.show();
        getListNguoiDung(new OnGetListSuccess() {
            @Override
            public void onGetListSuccess(List<NguoiDung> nguoiDungs) {
                int count = 0;

                for (NguoiDung nguoiDung : nguoiDungs) {
                    if (nguoiDung.getTenDn().trim().equals(username) && nguoiDung.getMatKhau().trim().equals(pass)) {
                        count = 1;
                        onCheckUserComplete.onCheckComplete(true, nguoiDung.getIdNd(), nguoiDung.getRole());
                        return;
                    }
                }

                if (count == 0) {
                    onCheckUserComplete.onCheckComplete(false, -1, -1);
                }
                progressDialog.dismiss();
            }
        });
    }


}
