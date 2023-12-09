package com.example.duanmau1.customer.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.duanmau1.R;
import com.example.duanmau1.customer.adapter.BillAdapter;
import com.example.duanmau1.dao.SharePreNguoiDung;
import com.example.duanmau1.model.HoaDon;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.List;

public class SubBillFrament extends Fragment {
    SharePreNguoiDung sharePreNguoiDung;
    View view;
    RecyclerView recSubBill;
    int status;
    int mand;
    BillAdapter billAdapter;
    List<HoaDon> hoaDonList;

    DatabaseReference refe;

    public SubBillFrament(int status) {
        this.status = status;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_sub_bill, container, false);
        initView();
        setView();
        return view;
    }

    private void setView() {

        sharePreNguoiDung = new SharePreNguoiDung(getContext());
        mand = sharePreNguoiDung.getMaNd();
        refe = FirebaseDatabase.getInstance().getReference("list_hoa_don")
                .child("list_hoa_don_user_" + mand);
        hoaDonList = new ArrayList<>();
        billAdapter = new BillAdapter(hoaDonList, getContext());
        recSubBill.setLayoutManager(new LinearLayoutManager(getContext()));
        recSubBill.setAdapter(billAdapter);
        if (status== 0){
            setRecOnGoing();
        } else if (status ==1){
            setRecGone();
        } else  {
            setRecCancel();
        }
    }

    private void setRecGone() {
        refe.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                HoaDon hoaDon = snapshot.getValue(HoaDon.class);
                if (hoaDon.getTinhTrang() == 1) {
                    hoaDonList.add(hoaDon);
                    billAdapter.notifyDataSetChanged();
                }
            }

            @Override
            public void onChildChanged(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                HoaDon hoaDon = snapshot.getValue(HoaDon.class);
                if (hoaDon.getTinhTrang() == 1) {
                    hoaDonList.add(hoaDon);
                    billAdapter.notifyDataSetChanged();
                }
            }

            @Override
            public void onChildRemoved(@NonNull DataSnapshot snapshot) {
                HoaDon hoaDon = snapshot.getValue(HoaDon.class);
                for (int i =0; i< hoaDonList.size(); i++) {
                    if (hoaDon.getMaHd() == hoaDonList.get(i).getMaHd()) {
                        hoaDonList.remove(i);
                        billAdapter.notifyDataSetChanged();
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

    private void setRecCancel() {
        refe.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                HoaDon hoaDon = snapshot.getValue(HoaDon.class);
                if (hoaDon.getTinhTrang() == -1) {
                    hoaDonList.add(hoaDon);
                    billAdapter.notifyDataSetChanged();
                }
            }

            @Override
            public void onChildChanged(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                HoaDon hoaDon = snapshot.getValue(HoaDon.class);
                if (hoaDon.getTinhTrang() != -1) {
                    for (int i = 0; i < hoaDonList.size(); i++) {
                        if (hoaDon.getMaHd() ==  hoaDonList.get(i).getMaHd()) {
                            hoaDonList.remove(i);
                            billAdapter.notifyDataSetChanged();
                        }
                    }
                }

                if (hoaDon.getTinhTrang() == -1) {
                    hoaDonList.add(hoaDon);
                    billAdapter.notifyDataSetChanged();
                }
            }

            @Override
            public void onChildRemoved(@NonNull DataSnapshot snapshot) {
                HoaDon hoaDon = snapshot.getValue(HoaDon.class);
                for (int i =0; i< hoaDonList.size(); i++) {
                    if (hoaDon.getMaHd() == hoaDonList.get(i).getMaHd()) {
                        hoaDonList.remove(i);
                        billAdapter.notifyDataSetChanged();
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

    private void setRecOnGoing() {
        refe.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                HoaDon hoaDon = snapshot.getValue(HoaDon.class);
                if (hoaDon.getTinhTrang() == 0) {
                    hoaDonList.add(hoaDon);
                    billAdapter.notifyDataSetChanged();
                }
            }

            @Override
            public void onChildChanged(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                HoaDon hoaDon = snapshot.getValue(HoaDon.class);
                if (hoaDon.getTinhTrang() != 0) {
                    for (int i = 0; i < hoaDonList.size(); i++) {
                        if (hoaDon.getMaHd() ==  hoaDonList.get(i).getMaHd()) {
                            hoaDonList.remove(i);
                            billAdapter.notifyDataSetChanged();
                            return;
                        }
                    }
                }
                if (hoaDon.getTinhTrang() == 0) {
                    hoaDonList.add(hoaDon);
                    billAdapter.notifyDataSetChanged();
                }
            }

            @Override
            public void onChildRemoved(@NonNull DataSnapshot snapshot) {
                HoaDon hoaDon = snapshot.getValue(HoaDon.class);
                for (int i =0; i< hoaDonList.size(); i++) {
                    if (hoaDon.getMaHd() == hoaDonList.get(i).getMaHd()) {
                        hoaDonList.remove(i);
                        billAdapter.notifyDataSetChanged();
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

    private void initView() {
        recSubBill = view.findViewById(R.id.rec_subBillUserFr);
    }
}
