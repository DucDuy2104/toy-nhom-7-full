package com.example.duanmau1.customer.adapter;

import android.content.Context;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.duanmau1.GroupFunction.CurrencyConvert;
import com.example.duanmau1.R;
import com.example.duanmau1.dao.MoHinhDao;
import com.example.duanmau1.model.HoaDonChiTiet;
import com.example.duanmau1.model.LoaiMoHinh;
import com.example.duanmau1.model.MoHinh;

import java.util.List;

public class SubBillAdapter extends RecyclerView.Adapter<SubBillAdapter.SubBillViewHolder> {
    Context context;
    List<HoaDonChiTiet> hoaDonChiTietList;
    MoHinhDao moHinhDao;

    public SubBillAdapter(Context context, List<HoaDonChiTiet> hoaDonChiTietList) {
        this.context = context;
        this.hoaDonChiTietList = hoaDonChiTietList;
        this.moHinhDao = new MoHinhDao(context);
    }

    @NonNull
    @Override
    public SubBillViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_sub_bill_user, parent, false);
        return new SubBillViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull SubBillViewHolder holder, int position) {
        HoaDonChiTiet hoaDonChiTiet = hoaDonChiTietList.get(position);
        moHinhDao.getMoHinhById(hoaDonChiTiet.getMaMh(), new MoHinhDao.OnGetMoHinhSuccess() {
            @Override
            public void onGetSuccess(MoHinh moHinh, LoaiMoHinh loaiMoHinh) {
                holder.tvName.setText(moHinh.getTenMh());
                holder.tvSoLuong.setText(hoaDonChiTiet.getSoLuong()+"");
                holder.tvThanhTien.setText(CurrencyConvert.convertFromFloatToVNCurrency(hoaDonChiTiet.getSoLuong() * moHinh.getGiaBan()));
                Glide.with(context).load(Uri.parse(moHinh.getImgUri())).into(holder.imgBill);
            }
        });
    }

    @Override
    public int getItemCount() {
        return hoaDonChiTietList.size();
    }

    public class SubBillViewHolder extends RecyclerView.ViewHolder {
        ImageView imgBill;
        TextView tvName, tvSoLuong, tvThanhTien;
        public SubBillViewHolder(@NonNull View itemView) {
            super(itemView);
            imgBill = itemView.findViewById(R.id.img_itemSubBillUser);
            tvSoLuong = itemView.findViewById(R.id.tv_soLuongItemSubBill);
            tvName = itemView.findViewById(R.id.tv_nameItemSubBill);
            tvThanhTien = itemView.findViewById(R.id.tv_thanhTienItemSubBill);
        }
    }
}
