package com.example.duanmau1.customer.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.viewpager2.widget.ViewPager2;

import com.example.duanmau1.R;
import com.example.duanmau1.customer.adapter.ViewPagerUserAdapter;
import com.google.android.material.tabs.TabLayout;
import com.google.android.material.tabs.TabLayoutMediator;

import java.util.ArrayList;
import java.util.List;

public class BillUserFragment extends Fragment {
    View view;
    ViewPagerUserAdapter viewPagerUserAdapter;
    TabLayout tabLayout;
    ViewPager2 vPager;
    List<Fragment> fragmentList;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = LayoutInflater.from(getContext()).inflate(R.layout.fragment_hoa_don_user, container, false);
        initView();
        setView();
        return view;
    }

    private void setView() {
        fragmentList = new ArrayList<>();
        fragmentList.add(new SubBillFrament(0));
        fragmentList.add(new SubBillFrament(1));
        fragmentList.add(new SubBillFrament(-1));
        viewPagerUserAdapter = new ViewPagerUserAdapter(requireActivity(), fragmentList);
        vPager.setAdapter(viewPagerUserAdapter);
        new TabLayoutMediator(tabLayout, vPager, new TabLayoutMediator.TabConfigurationStrategy() {
            @Override
            public void onConfigureTab(@NonNull TabLayout.Tab tab, int position) {
                switch (position) {
                    case 0:
                        tab.setText("Đã đặt");
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
        vPager =view.findViewById(R.id.vPager_billFr);
        tabLayout = view.findViewById(R.id.tab_billUserFr);
    }
}
