package com.example.duanmau1.customer.adapter;

import android.content.Context;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.Toolbar;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.duanmau1.GroupFunction.CurrencyConvert;
import com.example.duanmau1.R;
import com.example.duanmau1.dao.HoaDonChiTietDao;
import com.example.duanmau1.dao.MoHinhDao;
import com.example.duanmau1.model.HoaDonChiTiet;
import com.example.duanmau1.model.LoaiMoHinh;
import com.example.duanmau1.model.MoHinh;

import java.util.List;

public class CartItemAdapter extends RecyclerView.Adapter<CartItemAdapter.HoaDonViewHolder> {
    Context context;
    List<HoaDonChiTiet> hoaDonChiTietList;
    MoHinhDao moHinhDao;

    HoaDonChiTietDao hoaDonChiTietDao;
    public CartItemAdapter(Context context, List<HoaDonChiTiet> hoaDonChiTietList) {
        this.context = context;
        this.hoaDonChiTietList = hoaDonChiTietList;
        this.moHinhDao = new MoHinhDao(context);
        this.hoaDonChiTietDao =new HoaDonChiTietDao(context);
    }

    @NonNull
    @Override
    public HoaDonViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_san_pham_cart, parent, false);
        return new HoaDonViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull HoaDonViewHolder holder, int position) {
        HoaDonChiTiet hoaDonChiTiet = hoaDonChiTietList.get(position);
        moHinhDao.getMoHinhById(hoaDonChiTiet.getMaMh(), new MoHinhDao.OnGetMoHinhSuccess() {
            @Override
            public void onGetSuccess(MoHinh moHinh, LoaiMoHinh loaiMoHinh) {

                holder.tvNameSpCart.setText(moHinh.getTenMh());
                holder.tvTongTien.setText(CurrencyConvert.convertFromFloatToVNCurrency(moHinh.getGiaBan()*hoaDonChiTiet.getSoLuong()));
                Glide.with(context).load(Uri.parse(moHinh.getImgUri())).into(holder.imgItemCart);
                holder.tvSLuong.setText(hoaDonChiTiet.getSoLuong()+"");
                holder.btnAdd1.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        int sLuong = Integer.parseInt(holder.tvSLuong.getText().toString());
                        if (sLuong == moHinh.getSoLuong() - moHinh.getSoLuongDaBan()) {
                            return;
                        } else {
                            sLuong++;
                            holder.tvSLuong.setText(sLuong+"");
                            holder.tvTongTien.setText(CurrencyConvert.convertFromFloatToVNCurrency(sLuong * moHinh.getGiaBan()));
                            hoaDonChiTietDao.addSoLuong(hoaDonChiTiet);
                            hoaDonChiTiet.setSoLuong(hoaDonChiTiet.getSoLuong() + 1);

                        }
                    }
                });

                holder.btnMin1.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        int sLuong = Integer.parseInt(holder.tvSLuong.getText().toString());
                        if (sLuong== 1) {
                            return;
                        } else {
                            sLuong--;
                            holder.tvSLuong.setText(sLuong+"");
                            holder.tvTongTien.setText(sLuong * moHinh.getGiaBan() +"đ");
                            hoaDonChiTietDao.minhSoLuong(hoaDonChiTiet);
                            hoaDonChiTiet.setSoLuong(hoaDonChiTiet.getSoLuong() - 1);
                        }
                    }
                });

                holder.btnDelete.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        hoaDonChiTietList.remove(position);
                        notifyDataSetChanged();
                        hoaDonChiTietDao.deleteHoaDonChiTiet(hoaDonChiTiet.getMaNd(),hoaDonChiTiet.getMaHdct());
                    }
                });
            }
        });


    }

    @Override
    public int getItemCount() {
        return hoaDonChiTietList.size();
    }

    public class HoaDonViewHolder extends RecyclerView.ViewHolder {
        ImageView imgItemCart;
        TextView tvNameSpCart, tvTongTien, tvSLuong;
        Button btnAdd1, btnMin1;
        ImageButton btnDelete;

        public HoaDonViewHolder(@NonNull View itemView) {
            super(itemView);
            imgItemCart = itemView.findViewById(R.id.img_itemCart);
            tvNameSpCart = itemView.findViewById(R.id.tv_nameSpCart);
            tvSLuong = itemView.findViewById(R.id.tv_countCart);
            tvTongTien = itemView.findViewById(R.id.tv_priceSpCart);
            btnAdd1 = itemView.findViewById(R.id.btn_addCountCart);
            btnMin1 = itemView.findViewById(R.id.btn_minCountCart);
            btnDelete = itemView.findViewById(R.id.btn_deleteItemCart);
        }
    }
}
