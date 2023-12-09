package com.example.duanmau1.dao;

import android.app.ProgressDialog;
import android.content.Context;
import android.util.Log;
import android.widget.ProgressBar;

import androidx.annotation.NonNull;

import com.example.duanmau1.model.HoaDonChiTiet;
import com.example.duanmau1.model.LoaiMoHinh;
import com.example.duanmau1.model.MoHinh;
import com.example.duanmau1.model.NguoiDung;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.ArrayList;
import java.util.List;

public class HoaDonChiTietDao {
    private ProgressDialog progressDialog;
    private DatabaseReference dbReference;
    private MoHinhDao moHinhDao;
    private NguoiDungDao nguoiDungDao;
    private Context context;

    public HoaDonChiTietDao(Context context) {
        this.context = context;
        this.dbReference = FirebaseDatabase.getInstance().getReference("list_hoa_don_chi_tiet");
        setDialog();
    }

    private void setDialog() {
        progressDialog = new ProgressDialog(context);
        progressDialog.setMessage("Loading...");
        progressDialog.setCancelable(false);
    }

    public interface OnGetAllHoaDonChiTiet {
        void onGetSuccess(List<HoaDonChiTiet> listHdct);
    }

    public interface OnGetTotalPrice {
        void onGetSuccess(int total);
    }

    public void getAllHoaDonChiTiet(int mand,OnGetAllHoaDonChiTiet onGetAllHoaDonChiTiet) {
        dbReference.child("list_hdct_user_" + mand).get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DataSnapshot> task) {
                List<HoaDonChiTiet> hoaDonChiTiets = new ArrayList<>();
                for (DataSnapshot hdctSnapshot: task.getResult().getChildren()) {
                    HoaDonChiTiet hoaDonChiTiet = hdctSnapshot.getValue(HoaDonChiTiet.class);
                    hoaDonChiTiets.add(hoaDonChiTiet);
                }

                onGetAllHoaDonChiTiet.onGetSuccess(hoaDonChiTiets);
            }
        });
    }

    public void addHoaDonChiTiet(HoaDonChiTiet hoaDonChiTiet) {
        progressDialog.show();
        getAllHoaDonChiTiet(hoaDonChiTiet.getMaNd(), new OnGetAllHoaDonChiTiet() {
            @Override
            public void onGetSuccess(List<HoaDonChiTiet> listHdct) {
                int count = 0;

                if (listHdct.size() > 0) {
                    for (HoaDonChiTiet hoaDonCt: listHdct) {
                        if (hoaDonCt.getMaMh() == hoaDonChiTiet.getMaMh()) {
                            count = 1;
                            dbReference.child("list_hdct_user_" + hoaDonCt.getMaNd()).
                                    child("hdct_"+ hoaDonCt.getMaHdct())
                                    .child("soLuong")
                                    .setValue(hoaDonChiTiet.getSoLuong() + hoaDonCt.getSoLuong());
                            progressDialog.dismiss();
                            return;
                        }
                    }
                }


                if (count == 0) {
                    int maHdct =0;
                    if (listHdct.size() > 0) {
                        int maHdctMax  =0;
                        for (HoaDonChiTiet hoaDonChiTiet1: listHdct) {
                            if (hoaDonChiTiet1.getMaHdct() > maHdctMax) {
                                maHdctMax = hoaDonChiTiet1.getMaHdct();
                            }
                        }
                        maHdct = maHdctMax + 1;
                    }
                    hoaDonChiTiet.setMaHdct(maHdct);
                    dbReference.child("list_hdct_user_" + hoaDonChiTiet.getMaNd()).
                            child("hdct_" + maHdct).setValue(hoaDonChiTiet);
                }
                progressDialog.dismiss();
            }
        });
    }

    public void deleteHoaDonChiTiet(int maNd, int maHdct) {
        dbReference.child("list_hdct_user_" + maNd).child("hdct_" + maHdct)
                .removeValue().addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void unused) {
                Log.e("ducduy", "onSuccess: deleted hdct: " + maHdct +" of user: " + maNd );
            }
        });
    }

    public void addSoLuong(HoaDonChiTiet hoaDonCt) {
        dbReference.child("list_hdct_user_" + hoaDonCt.getMaNd()).
                child("hdct_"+ hoaDonCt.getMaHdct())
                .child("soLuong")
                .setValue(1 + hoaDonCt.getSoLuong());
    }

    public void minhSoLuong(HoaDonChiTiet hoaDonCt) {
        dbReference.child("list_hdct_user_" + hoaDonCt.getMaNd()).
                child("hdct_"+ hoaDonCt.getMaHdct())
                .child("soLuong")
                .setValue(hoaDonCt.getSoLuong() - 1);
    }

    public void getTongTien(int mand, OnGetTotalPrice onGetTotalPrice)  {

        moHinhDao = new MoHinhDao(context);
        getAllHoaDonChiTiet(mand, new OnGetAllHoaDonChiTiet() {
            @Override
            public void onGetSuccess(List<HoaDonChiTiet> listHdct) {
                final int[] total = {0};
                for (HoaDonChiTiet hoaDonChiTiet: listHdct) {
                    moHinhDao.getMoHinhById(hoaDonChiTiet.getMaMh(), new MoHinhDao.OnGetMoHinhSuccess() {
                        @Override
                        public void onGetSuccess(MoHinh moHinh, LoaiMoHinh loaiMoHinh) {
                            total[0] += moHinh.getGiaBan() * hoaDonChiTiet.getSoLuong();
                            onGetTotalPrice.onGetSuccess(total[0]);
                        }
                    });
                }
                if (listHdct.size() == 0) {
                    onGetTotalPrice.onGetSuccess(total[0]);
                }

            }
        });
    }

    public void deleteAllHdctByMand(int mand) {
        dbReference.child("list_hdct_user_" + mand).removeValue();
    }

    public void deleteAllHdctByMaMh(int mamh) {

        nguoiDungDao = new NguoiDungDao(context);
        nguoiDungDao.getListNguoiDung(new NguoiDungDao.OnGetListSuccess() {
            @Override
            public void onGetListSuccess(List<NguoiDung> nguoiDungs) {
                for (NguoiDung nguoiDung: nguoiDungs) {
                    getAllHoaDonChiTiet(nguoiDung.getIdNd(), new OnGetAllHoaDonChiTiet() {
                        @Override
                        public void onGetSuccess(List<HoaDonChiTiet> listHdct) {
                            for (HoaDonChiTiet hoaDonChiTiet: listHdct) {
                                if (hoaDonChiTiet.getMaMh() == mamh) {
                                    deleteHoaDonChiTiet(nguoiDung.getIdNd(), hoaDonChiTiet.getMaHdct());
                                }
                            }
                        }
                    });
                }
            }
        });
    }
}


