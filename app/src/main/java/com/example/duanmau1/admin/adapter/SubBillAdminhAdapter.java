package com.example.duanmau1.admin.adapter;

import android.content.Context;
import android.net.Uri;
import android.text.Layout;
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

public class SubBillAdminhAdapter extends RecyclerView.Adapter<SubBillAdminhAdapter.SubBillAdminViewHolder> {
    Context context;
    List<HoaDonChiTiet> hoaDonChiTietList;
    MoHinhDao moHinhDao;

    public SubBillAdminhAdapter(Context context, List<HoaDonChiTiet> hoaDonChiTietList) {
        this.context = context;
        this.hoaDonChiTietList = hoaDonChiTietList;
        this.moHinhDao = new MoHinhDao(context);
    }

    @NonNull
    @Override
    public SubBillAdminViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_sub_bill_admin, parent, false);
        return new SubBillAdminViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull SubBillAdminViewHolder holder, int position) {
        HoaDonChiTiet hoaDonChiTiet = hoaDonChiTietList.get(position);
        moHinhDao.getMoHinhById(hoaDonChiTiet.getMaMh(), new MoHinhDao.OnGetMoHinhSuccess() {
            @Override
            public void onGetSuccess(MoHinh moHinh, LoaiMoHinh loaiMoHinh) {
                Glide.with(context).load(Uri.parse(moHinh.getImgUri())).into(holder.img);
                holder.tvPrice.setText(CurrencyConvert.convertFromFloatToVNCurrency(moHinh.getGiaBan()));
                holder.tvName.setText(moHinh.getTenMh());
                holder.tvCount.setText("x" +hoaDonChiTiet.getSoLuong());
                holder.tvTotal.setText(CurrencyConvert.convertFromFloatToVNCurrency(moHinh.getGiaBan() * hoaDonChiTiet.getSoLuong()));
            }
        });
    }

    @Override
    public int getItemCount() {
        return hoaDonChiTietList.size();
    }

    public static class SubBillAdminViewHolder extends RecyclerView.ViewHolder {
        ImageView img;
        TextView tvName, tvPrice, tvTotal, tvCount;
        public SubBillAdminViewHolder(@NonNull View itemView) {
            super(itemView);
            img = itemView.findViewById(R.id.img_itemSubBillAdmin);
            tvName = itemView.findViewById(R.id.tv_nameItemSubBillAdmin);
            tvPrice = itemView.findViewById(R.id.tv_giaItemSubBillAdmin);
            tvTotal = itemView.findViewById(R.id.tv_tongTienItemSubBillAdmin);
            tvCount = itemView.findViewById(R.id.tv_countItemSubBillAdmin);
        }
    }
}
