package com.example.duanmau1.admin.fragment;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.viewpager.widget.ViewPager;
import androidx.viewpager2.widget.ViewPager2;

import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import com.example.duanmau1.R;
import com.example.duanmau1.admin.adapter.PagerAdminAdapter;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationBarView;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class HomeAdminFragment extends Fragment {

    View view;
    BottomNavigationView btNav;
    ViewPager2 vPager;
    PagerAdminAdapter pagerAdminAdapter;
    List<Fragment> fragmentList;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_home_admin, container, false);
        initView();
        setView();
        return view;
    }

    private void setView() {
        fragmentList =new ArrayList<>();
        fragmentList.add(new LoaiAdminFragment());
        fragmentList.add(new MoHinhAdminFragment());
        fragmentList.add(new HoaDonAdminFragment());
        fragmentList.add(new KhachHangAdminFragment());
        pagerAdminAdapter = new PagerAdminAdapter(requireActivity(),fragmentList);
        vPager.setAdapter(pagerAdminAdapter);
        vPager.setCurrentItem(0);
        btNav.setOnItemSelectedListener(new NavigationBarView.OnItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                if (item.getItemId() == R.id.type_btNav) {
                    vPager.setCurrentItem(0);
                    return true;
                } else if (item.getItemId() == R.id.toy_btNav) {
                    vPager.setCurrentItem(1);
                    return true;
                } else if (item.getItemId() ==R.id.bill_btNav) {
                    vPager.setCurrentItem(2);
                    return true;
                } else if (item.getItemId() == R.id.user_btNav) {
                    vPager.setCurrentItem(4);
                    return true;
                }
                return false;
            }
        });

        vPager.registerOnPageChangeCallback(new ViewPager2.OnPageChangeCallback() {
            @Override
            public void onPageSelected(int position) {
                if (position == 0) {
                    btNav.getMenu().findItem(R.id.type_btNav).setChecked(true);
                }else if (position == 1) {
                    btNav.getMenu().findItem(R.id.toy_btNav).setChecked(true);
                } else if (position ==2) {
                    btNav.getMenu().findItem(R.id.bill_btNav).setChecked(true);
                } else if (position == 3) {
                    btNav.getMenu().findItem(R.id.user_btNav).setChecked(true);
                }
            }
        });
    }

    private void initView() {
        btNav = view.findViewById(R.id.btNav_homeAdminFr);
        vPager = view.findViewById(R.id.vPager_homeAminFr);
    }
}