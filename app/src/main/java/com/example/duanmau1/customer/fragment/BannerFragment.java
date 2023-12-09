package com.example.duanmau1.customer.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.duanmau1.R;

import java.util.ArrayList;
import java.util.List;

public class BannerFragment extends Fragment {
    View view;
    ImageView imgSlide;
    int index;

    public BannerFragment(int index) {
        this.index = index;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_slide_show, container, false);
        initView();
        setView();
        return view;
    }

    private void setView() {
        List<Integer> integerList = new ArrayList<>();
        integerList.add(R.drawable.banner_onpiece);
        integerList.add(R.drawable.banner);
        integerList.add(R.drawable.banner_gundam);
        integerList.add(R.drawable.banner_naruto);

        imgSlide.setImageResource(integerList.get(index));
    }

    private void initView() {
        imgSlide = view.findViewById(R.id.img_silde);
    }
}
