package com.example.duanmau1.customer.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.viewpager2.widget.ViewPager2;

import com.example.duanmau1.R;
import com.example.duanmau1.customer.adapter.ViewPagerUserAdapter;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationBarView;

import java.util.ArrayList;
import java.util.List;

public class HomeUserFragment extends Fragment {
    View view;
    ViewPager2 vPagerUserBtNav;
    BottomNavigationView btNavUser;
    ViewPagerUserAdapter adapterFragemt;
    int viewNum = -1;

    public void setViewNum(int viewNum) {
        this.viewNum = viewNum;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = LayoutInflater.from(getContext()).inflate(R.layout.fragment_home_user, container, false);
        initView();
        setView();
        setOnClickNav();
        return view;
    }

    private void setOnClickNav() {
        btNavUser.setOnItemSelectedListener(new NavigationBarView.OnItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                if (item.getItemId() == R.id.toy_btNavUser) {
                    vPagerUserBtNav.setCurrentItem(0);
                } else if (item.getItemId() == R.id.cart_btNavUser) {
                    vPagerUserBtNav.setCurrentItem(1);
                } else if (item.getItemId() == R.id.bill_btNavUser) {
                    vPagerUserBtNav.setCurrentItem(2);
                } else if (item.getItemId() == R.id.info_btNavUser) {
                    vPagerUserBtNav.setCurrentItem(3);
                }
                return true;
            }
        });

        vPagerUserBtNav.registerOnPageChangeCallback(new ViewPager2.OnPageChangeCallback() {
            @Override
            public void onPageSelected(int position) {
                if (position == 0) {
                    btNavUser.getMenu().findItem(R.id.toy_btNavUser).setChecked(true);
                } else if (position == 1) {
                    btNavUser.getMenu().findItem(R.id.cart_btNavUser).setChecked(true);
                } else if (position == 2) {
                    btNavUser.getMenu().findItem(R.id.bill_btNavUser).setChecked(true);
                } else if (position == 3) {
                    btNavUser.getMenu().findItem(R.id.info_btNavUser).setChecked(true);
                }
                super.onPageSelected(position);
            }
        });
    }

    private void setView() {
        List<Fragment> fragmentList = new ArrayList<>();
        fragmentList.add(new SanPhamUserFragment());
        fragmentList.add(new CartUserFragment());
        fragmentList.add(new BillUserFragment());
        fragmentList.add(new InfoUserFragment());
        adapterFragemt = new ViewPagerUserAdapter(requireActivity(), fragmentList);
        vPagerUserBtNav.setAdapter(adapterFragemt);
    }

    private void initView() {
        vPagerUserBtNav = view.findViewById(R.id.vPager_homeUserFr);
        btNavUser = view.findViewById(R.id.btNav_homeUserFr);
    }


    @Override
    public void onResume() {
        super.onResume();
        if (viewNum == -1) {
            return;
        } else if (viewNum == 1) {
            vPagerUserBtNav.setCurrentItem(1);
        }
    }
}
