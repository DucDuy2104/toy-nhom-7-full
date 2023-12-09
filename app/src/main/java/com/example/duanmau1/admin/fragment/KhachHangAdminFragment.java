package com.example.duanmau1.admin.fragment;

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
import com.example.duanmau1.admin.adapter.KhachHangAdminAdapter;
import com.example.duanmau1.dao.NguoiDungDao;
import com.example.duanmau1.model.NguoiDung;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.List;

public class KhachHangAdminFragment extends Fragment {
    View view;
    RecyclerView recKhachHang;
    KhachHangAdminAdapter khachHangAdminAdapter;
    NguoiDungDao nguoiDungDao;
    DatabaseReference reference;
    List<NguoiDung> nguoiDungs;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_khach_hang_admin, container, false);
        initView();
        setView();
        return view;
    }

    private void setView() {
        nguoiDungDao = new NguoiDungDao(getContext());
        nguoiDungs = new ArrayList<>();
        khachHangAdminAdapter = new KhachHangAdminAdapter(nguoiDungs, getContext());
        recKhachHang.setAdapter(khachHangAdminAdapter);
        recKhachHang.setLayoutManager(new LinearLayoutManager(getContext()));
        setRealTime();
    }

    private void setRealTime() {
        reference = FirebaseDatabase.getInstance().getReference("list_user");
        reference.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                NguoiDung nguoiDung = snapshot.getValue(NguoiDung.class);
                if (nguoiDung.getRole() != 0) {
                    nguoiDungs.add(nguoiDung);
                    khachHangAdminAdapter.notifyDataSetChanged();
                }
            }

            @Override
            public void onChildChanged(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                NguoiDung nguoiDung = snapshot.getValue(NguoiDung.class);
                for (int i = 0; i < nguoiDungs.size(); i++) {
                    if (nguoiDung.getIdNd() == nguoiDungs.get(i).getIdNd()) {
                        nguoiDungs.set(i, nguoiDung);
                        khachHangAdminAdapter.notifyDataSetChanged();
                        return;
                    }
                }

            }

            @Override
            public void onChildRemoved(@NonNull DataSnapshot snapshot) {
                NguoiDung nguoiDung = snapshot.getValue(NguoiDung.class);
                for (int i = 0; i < nguoiDungs.size(); i++) {
                    if (nguoiDung.getIdNd() == nguoiDungs.get(i).getIdNd()) {
                        nguoiDungs.remove(i);
                        khachHangAdminAdapter.notifyDataSetChanged();
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

    private void initView() {
        recKhachHang = view.findViewById(R.id.rec_khachHangAdminFr);
    }
}
