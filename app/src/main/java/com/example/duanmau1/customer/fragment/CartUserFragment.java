package com.example.duanmau1.customer.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.duanmau1.GroupFunction.CurrencyConvert;
import com.example.duanmau1.R;
import com.example.duanmau1.customer.adapter.CartItemAdapter;
import com.example.duanmau1.dao.HoaDonChiTietDao;
import com.example.duanmau1.dao.HoaDonDao;
import com.example.duanmau1.dao.SharePreNguoiDung;
import com.example.duanmau1.model.HoaDon;
import com.example.duanmau1.model.HoaDonChiTiet;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class CartUserFragment extends Fragment {
    View view;
    RecyclerView recHoaDon;
    Button btnBuy;
    TextView tongTien;
    DatabaseReference ref;
    List<HoaDonChiTiet> hoaDonChiTietList;
    CartItemAdapter cartItemAdapter;
    SharePreNguoiDung sharePreNguoiDung;
    int mand;
    HoaDonChiTietDao hoaDonChiTietDao;
    HoaDonDao hoaDonDao;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = LayoutInflater.from(getContext()).inflate(R.layout.fragment_gio_hang_user, container, false);
        initView();
        setView();
        return view;
    }

    private void setView() {
        sharePreNguoiDung = new SharePreNguoiDung(getContext());
        mand = sharePreNguoiDung.getMaNd();
        hoaDonChiTietList=new ArrayList<>();
        cartItemAdapter = new CartItemAdapter(getContext(), hoaDonChiTietList);
        recHoaDon.setLayoutManager(new LinearLayoutManager(getContext()));
        recHoaDon.setAdapter(cartItemAdapter);
        setRealTime();
        hoaDonChiTietDao = new HoaDonChiTietDao(getContext());
        setTongTien();
        setBtnBuyClick();
    }

    private void setBtnBuyClick() {
        hoaDonDao = new HoaDonDao(getContext());
        btnBuy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                hoaDonChiTietDao.getAllHoaDonChiTiet(mand, new HoaDonChiTietDao.OnGetAllHoaDonChiTiet() {
                    @Override
                    public void onGetSuccess(List<HoaDonChiTiet> listHdct) {
                        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("hh:mm:ss dd/MM/yyyy");
                        Date date = new Date();
                        String dateString = simpleDateFormat.format(date);
                        String tTien = tongTien.getText().toString();
                        int total = CurrencyConvert.converFromVNCurrencyToFloat(tTien);
                        HoaDon hoaDon = new HoaDon(mand, dateString, 0, total, listHdct);
                        hoaDonDao.addHoaDon(hoaDon);
                    }
                });
            }
        });
    }

    private void setTongTien() {
        hoaDonChiTietDao.getTongTien(mand, new HoaDonChiTietDao.OnGetTotalPrice() {
            @Override
            public void onGetSuccess(int total) {
                tongTien.setText(CurrencyConvert.convertFromFloatToVNCurrency(total));
            }
        });
    }

    private void setRealTime() {
        ref = FirebaseDatabase.getInstance().getReference("list_hoa_don_chi_tiet")
                .child("list_hdct_user_"+ mand);

        ref.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                HoaDonChiTiet hoaDonChiTiet = snapshot.getValue(HoaDonChiTiet.class);
                hoaDonChiTietList.add(hoaDonChiTiet);
                cartItemAdapter.notifyDataSetChanged();
                setTongTien();
            }

            @Override
            public void onChildChanged(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                setTongTien();
            }

            @Override
            public void onChildRemoved(@NonNull DataSnapshot snapshot) {
                setTongTien();

            }

            @Override
            public void onChildMoved(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    private void initView() {
        recHoaDon = view.findViewById(R.id.rec_cartFragment);
        btnBuy = view.findViewById(R.id.btn_buyNowCart);
        tongTien = view.findViewById(R.id.tv_tongTienCart);
    }


}
