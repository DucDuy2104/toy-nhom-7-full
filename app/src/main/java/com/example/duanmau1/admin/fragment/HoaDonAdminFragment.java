package com.example.duanmau1.admin.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.viewpager2.widget.ViewPager2;

import com.example.duanmau1.R;
import com.example.duanmau1.admin.adapter.PagerAdminAdapter;
import com.google.android.material.tabs.TabLayout;
import com.google.android.material.tabs.TabLayoutMediator;

import java.util.ArrayList;
import java.util.List;

public class HoaDonAdminFragment extends Fragment {
    View view;
    List<Fragment> fragmentList;
    TabLayout tab;
    ViewPager2 vPager;
    PagerAdminAdapter pagerAdminAdapter;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_hoa_don_admin, container, false);
        initView();
        setView();
        return view;
    }

    private void setView() {
        fragmentList= new ArrayList<>();
        fragmentList.add(new SubBillAdminFragment(0));
        fragmentList.add(new SubBillAdminFragment(1));
        fragmentList.add(new SubBillAdminFragment(-1));
        pagerAdminAdapter = new PagerAdminAdapter(requireActivity(), fragmentList);
        vPager.setAdapter(pagerAdminAdapter);
        new TabLayoutMediator(tab, vPager, new TabLayoutMediator.TabConfigurationStrategy() {
            @Override
            public void onConfigureTab(@NonNull TabLayout.Tab tab, int position) {
                switch (position) {
                    case 0:
                        tab.setText("Mới");
                        break;
                    case 1:
                        tab.setText("Hoàn thành");
                        break;
                    case 2:
                        tab.setText("Đã hủy");
                        break;
                }
            }
        }).attach();
    }

    private void initView() {
        tab = view.findViewById(R.id.tab_billAdmin);
        vPager = view.findViewById(R.id.vPager_billAdmin);
    }
}
