package com.example.duanmau1.customer.adapter;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.Layout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.duanmau1.GroupFunction.CurrencyConvert;
import com.example.duanmau1.R;
import com.example.duanmau1.customer.activity.SanPhamDetailActivity;
import com.example.duanmau1.model.MoHinh;

import java.util.List;

public class SanPhamUserAdapter extends RecyclerView.Adapter<SanPhamUserAdapter.SanPhamUserViewHolder> {
    Context context;
    List<MoHinh> moHinhList;

    public SanPhamUserAdapter(Context context, List<MoHinh> moHinhList) {
        this.context = context;
        this.moHinhList = moHinhList;
    }

    @NonNull
    @Override
    public SanPhamUserViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_sam_pham_user, parent,false);
        return new SanPhamUserViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull SanPhamUserViewHolder holder, int position) {
        MoHinh moHinh = moHinhList.get(position);
        Glide.with(context).load(moHinh.getImgUri()).into(holder.imgSp);
        holder.tvNameSp.setText(moHinh.getTenMh());
        holder.tvPriceSp.setText(CurrencyConvert.convertFromFloatToVNCurrency(moHinh.getGiaBan()));
        holder.tvRating.setText(moHinh.getDanhGia() + "");
        holder.btnDetail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent  = new Intent(context, SanPhamDetailActivity.class);
                Bundle bundle = new Bundle();
                bundle.putInt("mamh",moHinh.getMaMh());
                intent.putExtras(bundle);
                ((Activity)context).startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return moHinhList.size();
    }

    public static class SanPhamUserViewHolder extends RecyclerView.ViewHolder {
        ImageView imgSp;
        TextView tvNameSp, tvPriceSp, tvRating;
        Button btnDetail;
        public SanPhamUserViewHolder(@NonNull View itemView) {
            super(itemView);
            imgSp = itemView.findViewById(R.id.img_itemSanPhamUser);
            tvNameSp = itemView.findViewById(R.id.tv_nameSpUser);
            tvRating =itemView.findViewById(R.id.tv_ratingSpUser);
            tvPriceSp = itemView.findViewById(R.id.tv_priceSpUser);
            btnDetail = itemView.findViewById(R.id.btn_detailSpUser);
        }
    }
}
