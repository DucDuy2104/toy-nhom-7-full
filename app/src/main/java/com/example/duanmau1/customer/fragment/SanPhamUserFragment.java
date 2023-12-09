package com.example.duanmau1.customer.fragment;

import android.os.Bundle;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager2.widget.ViewPager2;

import com.example.duanmau1.R;
import com.example.duanmau1.customer.adapter.SanPhamOrderByLoaiAdapter;
import com.example.duanmau1.customer.adapter.ViewPagerUserAdapter;
import com.example.duanmau1.dao.LoaiMoHinhDao;
import com.example.duanmau1.dao.MoHinhDao;
import com.example.duanmau1.model.LoaiMoHinh;
import com.example.duanmau1.model.MoHinh;
import com.example.duanmau1.model.NguoiDung;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.List;

import me.relex.circleindicator.CircleIndicator3;

public class SanPhamUserFragment extends Fragment {
    private View view;
    private RecyclerView recSpUserFrag;
    private List<LoaiMoHinh> loaiMoHinhList;
    private List<List<MoHinh>> lists;
    private DatabaseReference mDatabaseLoai;
    private DatabaseReference mDatabaseMohinh;
    private  SanPhamOrderByLoaiAdapter sanPhamOrderByLoaiAdapter;
    private MoHinhDao moHinhDao;
    private LoaiMoHinhDao loaiMoHinhDao;
    private ViewPagerUserAdapter vPagerAdapter;
    private ViewPager2 vPagerSlide;
    private List<Fragment> fragmentList;
    private CircleIndicator3 indicator3;
    int count = 0;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = LayoutInflater.from(getContext()).inflate(R.layout.fragment_san_pham_user, container, false);
        initView();
        setView();
        setAutoSlide();
        return view;
    }

    private void setAutoSlide() {
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                if (count == 3) {
                    count = 0;
                } else {
                    count++;
                }
                vPagerSlide.setCurrentItem(count);
                setAutoSlide();
            }
        }, 2000);
    }

    private void setView() {
        fragmentList = new ArrayList<>();
        fragmentList.add(new BannerFragment(0));
        fragmentList.add(new BannerFragment(1));
        fragmentList.add(new BannerFragment(2));
        fragmentList.add(new BannerFragment(3));
        vPagerAdapter = new ViewPagerUserAdapter(requireActivity(), fragmentList);;
        vPagerSlide.setAdapter(vPagerAdapter);
        indicator3.setViewPager(vPagerSlide);
        vPagerAdapter.registerAdapterDataObserver(indicator3.getAdapterDataObserver());
        moHinhDao = new MoHinhDao(getContext());
        loaiMoHinhDao = new LoaiMoHinhDao(getContext());
        loaiMoHinhList = new ArrayList<>();
        lists = new ArrayList<>();
        sanPhamOrderByLoaiAdapter = new SanPhamOrderByLoaiAdapter(getContext(), loaiMoHinhList, lists);
        LinearLayoutManager manager = new LinearLayoutManager(getContext());
        recSpUserFrag.setAdapter(sanPhamOrderByLoaiAdapter);
        recSpUserFrag.setLayoutManager(manager);
        setRealTime();
    }

    private void setRealTime() {
        mDatabaseMohinh = FirebaseDatabase.getInstance().getReference("list_mo_hinh");
        mDatabaseMohinh.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                MoHinh moHinh = snapshot.getValue(MoHinh.class);
                    loaiMoHinhDao.getLoaiMoHinhById(moHinh.getMaLmh(), new LoaiMoHinhDao.OnGetLoaiSuccess() {
                        @Override
                        public void onGetLoaiSuccess(LoaiMoHinh loaiMoHinh) {
                            int count = 0;
                            for (int i = 0; i <loaiMoHinhList.size(); i++) {
                                if (moHinh.getMaLmh() == loaiMoHinhList.get(i).getMaLmh()) {
                                    goToAddMoHinh(moHinh);
                                    count = 1;
                                    return;
                                }
                            }
                            if (count == 0) {
                                loaiMoHinhList.add(loaiMoHinh);
                                List<MoHinh> moHinhList = new ArrayList<>();
                                lists.add(moHinhList);
                                goToAddMoHinh(moHinh);
                            }

                        }
                    });


            }

            @Override
            public void onChildChanged(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                MoHinh moHinh = snapshot.getValue(MoHinh.class);
                for (int i = 0; i< loaiMoHinhList.size();i++) {
                    if (loaiMoHinhList.get(i).getMaLmh() == moHinh.getMaLmh()) {
                        for (int y = 0; y < lists.size(); y++) {
                            if (lists.get(i).get(y).getMaMh() == moHinh.getMaMh()) {
                                lists.get(i).set(y, moHinh);
                                sanPhamOrderByLoaiAdapter.notifyDataSetChanged();
                                return;
                            }
                        }
                    }
                }
            }

            @Override
            public void onChildRemoved(@NonNull DataSnapshot snapshot) {
                MoHinh moHinh = snapshot.getValue(MoHinh.class);
                for (int i = 0; i < loaiMoHinhList.size();i++) {
                    if (loaiMoHinhList.get(i).getMaLmh() == moHinh.getMaLmh()) {
                        for (int y = 0; y < lists.size(); y++) {
                            if (lists.get(i).get(y).getMaMh() == moHinh.getMaMh()) {
                                lists.get(i).remove(y);
                                sanPhamOrderByLoaiAdapter.notifyDataSetChanged();
                                return;
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

    private void goToAddMoHinh(MoHinh moHinh) {
        for (int i = 0; i < loaiMoHinhList.size(); i++) {
            if (moHinh.getMaLmh() == loaiMoHinhList.get(i).getMaLmh()) {
                lists.get(i).add(moHinh);
            }
        }

        sanPhamOrderByLoaiAdapter.notifyDataSetChanged();
    }

    private void initView() {
        vPagerSlide = view.findViewById(R.id.vPager_silde);
        recSpUserFrag = view.findViewById(R.id.rec_sanPhamUserFr);
        indicator3 = view.findViewById(R.id.indicator_banner);
    }
}
