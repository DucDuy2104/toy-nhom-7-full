package com.example.duanmau1.admin.adapter;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.text.Layout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.duanmau1.GroupFunction.CurrencyConvert;
import com.example.duanmau1.R;
import com.example.duanmau1.admin.activity.DetailMoHinhActivity;
import com.example.duanmau1.customview.CustomToast;
import com.example.duanmau1.dao.MoHinhDao;
import com.example.duanmau1.model.LoaiMoHinh;
import com.example.duanmau1.model.MoHinh;

import java.util.List;

public class MoHinhAdminAdapter extends RecyclerView.Adapter<MoHinhAdminAdapter.MoHinhViewHolder> {
    private List<MoHinh> moHinhList;
    private Context context;
    private MoHinhDao moHinhDao;

    public MoHinhAdminAdapter(List<MoHinh> moHinhList, Context context) {
        this.moHinhList = moHinhList;
        this.context = context;
        this.moHinhDao = new MoHinhDao(context);
    }

    public void setMoHinhList(List<MoHinh> moHinhList) {
        this.moHinhList = moHinhList;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public MoHinhViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_mo_hinh_admin, parent, false);
        return new MoHinhViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MoHinhViewHolder holder, int position) {
        MoHinh moHinh = moHinhList.get(position);
        Glide.with(context).load(Uri.parse(moHinh.getImgUri())).into(holder.imgMoHinh);
        holder.tvName.setText(moHinh.getTenMh());
        holder.tvRate.setText("Rate: " + moHinh.getDanhGia());
        holder.tvPrice.setText(CurrencyConvert.convertFromFloatToVNCurrency(moHinh.getGiaBan()));
        holder.tvAmount.setText("Amount: " + moHinh.getSoLuong());
        holder.tvSold.setText("Sold: " + moHinh.getSoLuongDaBan());
        moHinhDao.getMoHinhById(moHinh.getMaMh(), new MoHinhDao.OnGetMoHinhSuccess() {
            @Override
            public void onGetSuccess(MoHinh moHinh, LoaiMoHinh loaiMoHinh) {
                holder.tvType.setText("Type: " + loaiMoHinh.getTenLoai());
            }
        });

        holder.btnDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showDialog(moHinh.getMaMh(), moHinh.getTenMh());
            }
        });

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, DetailMoHinhActivity.class);
                Bundle bundle = new Bundle();
                bundle.putInt("mamh", moHinh.getMaMh());
                intent.putExtras(bundle);
                context.startActivity(intent);
            }
        });
    }

    private void showDialog(int maMh, String tenMh) {
        View viewDialog = LayoutInflater.from(context).inflate(R.layout.dialog_warning, null);
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setView(viewDialog);
        AlertDialog dialog = builder.create();

        ImageButton btnCancel = viewDialog.findViewById(R.id.btn_closeWarningDialog);
        Button btnConFirm = viewDialog.findViewById(R.id.btn_conFirmWarningDialog);
        TextView tvTitle = viewDialog.findViewById(R.id.tv_titleWarningDialog);
        TextView tvMess = viewDialog.findViewById(R.id.tv_messageWarningDialog);

        tvTitle.setText(tenMh);
        tvMess.setText("Bạn có chắc chắn muốn xóa " + tenMh);

        btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });

        btnConFirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showSubDialog(maMh);
                dialog.dismiss();
            }
        });
        dialog.show();
    }

    private void showSubDialog(int maMh) {
        View viewDialog = LayoutInflater.from(context).inflate(R.layout.dialog_error, null);
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setView(viewDialog);
        AlertDialog dialog = builder.create();

        ImageButton btnCancel = viewDialog.findViewById(R.id.btn_closeErrorDialog);
        Button btnConFirm = viewDialog.findViewById(R.id.btn_conFirmErrorDialog);
        TextView tvTitle = viewDialog.findViewById(R.id.tv_titleErrorDialog);
        TextView tvMess = viewDialog.findViewById(R.id.tv_messageErrorDialog);

        tvTitle.setText("CẢNH BÁO");
        tvMess.setText("!!! Xóa mô hình, các hóa đơn liên quan đến mô hình sẽ bị xóa !!!");

        btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });

        btnConFirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                moHinhDao.deleteMoHinh(maMh);
                CustomToast customToast = new CustomToast(context, CustomToast.SUCCESS, "Xóa thành công!");
                customToast.showToast();
                dialog.dismiss();
            }
        });
        dialog.show();
    }

    @Override
    public int getItemCount() {
        return moHinhList.size();
    }

    public class MoHinhViewHolder extends RecyclerView.ViewHolder {
        private ImageView imgMoHinh;
        private TextView tvName, tvRate, tvPrice, tvAmount, tvSold, tvType;
        private ImageButton btnDelete;
        public MoHinhViewHolder(@NonNull View itemView) {
            super(itemView);
            imgMoHinh = itemView.findViewById(R.id.img_itemMoHinh);
            tvName = itemView.findViewById(R.id.tv_nameItemMoHinh);
            tvRate = itemView.findViewById(R.id.tv_rateItemMoHinh);
            tvPrice =itemView.findViewById(R.id.tv_priceItemMoHinh);
            tvAmount= itemView.findViewById(R.id.tv_amountItemMoHinh);
            tvSold = itemView.findViewById(R.id.tv_soldItemMoHinh);
            tvType =itemView.findViewById(R.id.tv_typeItemMoHinh);
            btnDelete = itemView.findViewById(R.id.btn_deleteItemMoHinh);
        }
    }
}
