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
import com.example.duanmau1.admin.adapter.BillAdminAdapter;
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

public class SubBillAdminFragment extends Fragment {
    SharePreNguoiDung sharePreNguoiDung;
    View view;
    RecyclerView recSubBill;
    int status;
    BillAdminAdapter billAdapter;
    List<HoaDon> hoaDonList;

    DatabaseReference refe;

    public SubBillAdminFragment(int status) {
        this.status = status;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_sub_bill_admin, container,false);
        initView();
        setView();
        return view;
    }

    private void setView() {
        refe = FirebaseDatabase.getInstance().getReference("list_hoa_don");
        hoaDonList = new ArrayList<>();
        billAdapter = new BillAdminAdapter(getContext(), hoaDonList);
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

    private void setRecCancel() {
        refe.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                for (DataSnapshot snapshot1: snapshot.getChildren()) {
                    HoaDon hoaDon = snapshot1.getValue(HoaDon.class);
                    if (hoaDon.getTinhTrang() == -1) {
                        hoaDonList.add(hoaDon);
                        billAdapter.notifyDataSetChanged();
                    }
                }
            }

            @Override
            public void onChildChanged(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                for (DataSnapshot snapshot1: snapshot.getChildren()) {
                    HoaDon hoaDon = snapshot1.getValue(HoaDon.class);
                    if (hoaDon.getTinhTrang() == -1) {
                        int i =0;
                        for (HoaDon hoaDon1: hoaDonList) {
                            if (hoaDon.getMaNd() == hoaDon1.getMaNd() && hoaDon1.getMaHd() == hoaDon.getMaHd()) {
                                i++;
                            }
                        }
                        if (i==0) {
                            hoaDonList.add(hoaDon);
                            billAdapter.notifyDataSetChanged();
                        }
                    }
                }

                for (DataSnapshot snapshot1: snapshot.getChildren()) {
                    HoaDon hoaDon = snapshot1.getValue(HoaDon.class);
                    if (hoaDon.getTinhTrang() != -1) {
                        for (int i = 0; i< hoaDonList.size(); i++) {
                            if (hoaDon.getMaNd() == hoaDonList.get(i).getMaNd() && hoaDonList.get(i).getMaHd() == hoaDon.getMaHd()) {
                                hoaDonList.remove(i);
                                billAdapter.notifyDataSetChanged();
                            }
                        }

                    }
                }

                for (int i = 0; i < hoaDonList.size(); i++) {
                    int y =0;
                    int x = 0;
                    for (DataSnapshot dataSnapshot: snapshot.getChildren()){
                        HoaDon hoaDon = dataSnapshot.getValue(HoaDon.class);
                        if (hoaDon.getMaNd() != hoaDonList.get(i).getMaNd()) {
                            x++;
                        }
                        if (hoaDon.getTinhTrang() == -1) {
                            if (hoaDonList.get(i).getMaHd() == hoaDon.getMaHd() && hoaDonList.get(i).getMaNd() == hoaDon.getMaNd()) {
                                y++;
                            }
                        }
                    }
                    if (y == 0 && x == 0) {
                        hoaDonList.remove(i);
                        billAdapter.notifyDataSetChanged();
                    }
                }

            }

            @Override
            public void onChildRemoved(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot dataSnapshot: snapshot.getChildren()) {
                    HoaDon hoaDon =dataSnapshot.getValue(HoaDon.class);
                    for (int i = 0; i <hoaDonList.size(); i++) {
                        if (hoaDon.getMaHd() == hoaDonList.get(i).getMaHd() && hoaDon.getMaNd() == hoaDonList.get(i).getMaNd()) {
                            hoaDonList.remove(i);
                            billAdapter.notifyDataSetChanged();
                        }
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

    private void setRecGone() {
        refe.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                for (DataSnapshot snapshot1: snapshot.getChildren()) {
                    HoaDon hoaDon = snapshot1.getValue(HoaDon.class);
                    if (hoaDon.getTinhTrang() == 1) {
                        hoaDonList.add(hoaDon);
                        billAdapter.notifyDataSetChanged();
                    }
                }
            }

            @Override
            public void onChildChanged(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                for (DataSnapshot snapshot1: snapshot.getChildren()) {
                    HoaDon hoaDon = snapshot1.getValue(HoaDon.class);
                    if (hoaDon.getTinhTrang() == 1) {
                        int i =0;
                        for (HoaDon hoaDon1: hoaDonList) {
                            if (hoaDon.getMaNd() == hoaDon1.getMaNd() && hoaDon1.getMaHd() == hoaDon.getMaHd()) {
                                i++;
                            }
                        }
                        if (i==0) {
                            hoaDonList.add(hoaDon);
                            billAdapter.notifyDataSetChanged();
                        }
                    }
                }

                for (int i = 0; i < hoaDonList.size(); i++) {
                    int y =0;
                    int x = 0;
                    for (DataSnapshot dataSnapshot: snapshot.getChildren()){
                        HoaDon hoaDon = dataSnapshot.getValue(HoaDon.class);
                        if (hoaDon.getMaNd() != hoaDonList.get(i).getMaNd()) {
                            x++;
                        }
                        if (hoaDon.getTinhTrang() == 1) {
                            if (hoaDonList.get(i).getMaHd() == hoaDon.getMaHd() && hoaDonList.get(i).getMaNd() == hoaDon.getMaNd()) {
                                y++;
                            }
                        }
                    }
                    if (y == 0 && x == 0) {
                        hoaDonList.remove(i);
                        billAdapter.notifyDataSetChanged();
                    }
                }
            }

            @Override
            public void onChildRemoved(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot dataSnapshot: snapshot.getChildren()) {
                    HoaDon hoaDon =dataSnapshot.getValue(HoaDon.class);
                    for (int i = 0; i <hoaDonList.size(); i++) {
                        if (hoaDon.getMaHd() == hoaDonList.get(i).getMaHd() && hoaDon.getMaNd() == hoaDonList.get(i).getMaNd()) {
                            hoaDonList.remove(i);
                            billAdapter.notifyDataSetChanged();
                        }
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
                for (DataSnapshot snapshot1: snapshot.getChildren()) {
                    HoaDon hoaDon = snapshot1.getValue(HoaDon.class);
                    if (hoaDon.getTinhTrang() == 0) {
                        hoaDonList.add(hoaDon);
                        billAdapter.notifyDataSetChanged();
                    }
                }
            }

            @Override
            public void onChildChanged(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                for (DataSnapshot snapshot1: snapshot.getChildren()) {
                    HoaDon hoaDon = snapshot1.getValue(HoaDon.class);
                    if (hoaDon.getTinhTrang() != 0) {
                        for (int i = 0; i< hoaDonList.size(); i++) {
                            if (hoaDon.getMaNd() == hoaDonList.get(i).getMaNd() && hoaDon.getMaHd() == hoaDonList.get(i).getMaHd()){
                                hoaDonList.remove(i);
                                billAdapter.notifyDataSetChanged();
                            }
                        }
                    }
                }


                for (DataSnapshot snapshot1: snapshot.getChildren()) {
                    HoaDon hoaDon = snapshot1.getValue(HoaDon.class);
                    if (hoaDon.getTinhTrang() == 0) {
                        int y =0;
                        for (HoaDon hoaDon1:hoaDonList) {
                            if (hoaDon.getMaHd() == hoaDon1.getMaHd()&& hoaDon1.getMaNd() == hoaDon.getMaNd()) {
                                y++;
                            }
                        }
                        if ( y == 0) {
                            hoaDonList.add(hoaDon);
                            billAdapter.notifyDataSetChanged();
                        }
                    }
                }

                for (int i = 0; i < hoaDonList.size(); i++) {
                    int y =0;
                    int x = 0;
                    for (DataSnapshot dataSnapshot: snapshot.getChildren()){
                        HoaDon hoaDon = dataSnapshot.getValue(HoaDon.class);
                        if (hoaDon.getMaNd() != hoaDonList.get(i).getMaNd()) {
                            x++;
                        }
                        if (hoaDon.getTinhTrang() == 0) {
                            if (hoaDonList.get(i).getMaHd() == hoaDon.getMaHd() && hoaDonList.get(i).getMaNd() == hoaDon.getMaNd()) {
                                y++;
                            }
                        }
                    }
                    if (y == 0 && x == 0) {
                        hoaDonList.remove(i);
                        billAdapter.notifyDataSetChanged();
                    }
                }

            }

            @Override
            public void onChildRemoved(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot dataSnapshot: snapshot.getChildren()) {
                    HoaDon hoaDon =dataSnapshot.getValue(HoaDon.class);
                    if (hoaDon.getTinhTrang() == 0) {
                        for (int i = 0; i < hoaDonList.size(); i++) {
                            if (hoaDon.getMaHd() == hoaDonList.get(i).getMaHd() && hoaDon.getMaNd() == hoaDonList.get(i).getMaNd()) {
                                hoaDonList.remove(i);
                                billAdapter.notifyDataSetChanged();
                            }
                        }
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
        recSubBill = view.findViewById(R.id.rec_subBillAdminFr);
    }
}
