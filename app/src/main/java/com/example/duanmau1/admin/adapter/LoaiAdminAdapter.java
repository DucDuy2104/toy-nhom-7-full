package com.example.duanmau1.admin.adapter;

import android.content.Context;
import android.content.DialogInterface;
import android.media.Image;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.duanmau1.R;
import com.example.duanmau1.customview.CustomToast;
import com.example.duanmau1.dao.LoaiMoHinhDao;
import com.example.duanmau1.dao.MoHinhDao;
import com.example.duanmau1.model.LoaiMoHinh;

import java.util.List;

public class LoaiAdminAdapter extends RecyclerView.Adapter<LoaiAdminAdapter.LoaiViewHolder> {
    private List<LoaiMoHinh> loaiMoHinhList;
    private MoHinhDao moHinhDao;
    private Context context;
    private LoaiMoHinhDao loaiMoHinhDao;

    public LoaiAdminAdapter(List<LoaiMoHinh> loaiMoHinhList, Context context) {
        this.loaiMoHinhList = loaiMoHinhList;
        this.moHinhDao = new MoHinhDao(context);
        this.context = context;
        this.loaiMoHinhDao = new LoaiMoHinhDao(context);
    }

    public void setLoaiMoHinhList(List<LoaiMoHinh> loaiMoHinhList) {
        this.loaiMoHinhList = loaiMoHinhList;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public LoaiViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_loai_admin, parent, false);
        return new LoaiViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull LoaiViewHolder holder, int position) {
        LoaiMoHinh loaiMoHinh = loaiMoHinhList.get(position);
        Glide.with(context).load(Uri.parse(loaiMoHinh.getImgUri())).into(holder.imgLoai);
        holder.tvName.setText(loaiMoHinh.getTenLoai());
        moHinhDao.countMoHinhByLoai(loaiMoHinh.getMaLmh(), new MoHinhDao.OnCountMoHinh() {
            @Override
            public void onCountComplete(int count) {
                holder.tvSoLuong.setText("SL: "+ count);
            }
        });

        holder.btnDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showDialog(loaiMoHinh.getTenLoai(), loaiMoHinh.getMaLmh());
            }
        });
    }

    private void showDialog(String tenLoai, int maLoai) {
        View viewDialog = LayoutInflater.from(context).inflate(R.layout.dialog_warning, null);
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setView(viewDialog);
        AlertDialog dialog = builder.create();

        ImageButton btnCancel = viewDialog.findViewById(R.id.btn_closeWarningDialog);
        Button btnConFirm = viewDialog.findViewById(R.id.btn_conFirmWarningDialog);
        TextView tvTitle = viewDialog.findViewById(R.id.tv_titleWarningDialog);
        TextView tvMess = viewDialog.findViewById(R.id.tv_messageWarningDialog);

        tvTitle.setText(tenLoai);
        tvMess.setText("Bạn có chắc chăn muốn xóa loại " + tenLoai);

        btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
        
        btnConFirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showSubDilog(tenLoai, maLoai);
                dialog.dismiss();
            }
        });

        dialog.show();


    }

    private void showSubDilog(String tenLoai, int maLoai) {
        View viewDialog = LayoutInflater.from(context).inflate(R.layout.dialog_error, null);
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setView(viewDialog);
        AlertDialog dialog = builder.create();

        ImageButton btnCancel = viewDialog.findViewById(R.id.btn_closeErrorDialog);
        Button btnConFirm = viewDialog.findViewById(R.id.btn_conFirmErrorDialog);
        TextView tvTitle = viewDialog.findViewById(R.id.tv_titleErrorDialog);
        TextView tvMess = viewDialog.findViewById(R.id.tv_messageErrorDialog);

        tvTitle.setText("CẢNH BÁO");
        tvMess.setText("!!! Các mô hình có loại " + tenLoai+" cũng sẽ bị xóa !!!" + "\n"
                + "!!! Các hóa đơn liên quan đến các mô hình " + tenLoai + " cũng sẽ bị xóa !!!");

        btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });

        btnConFirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                loaiMoHinhDao.deleteLoaiMoHinh(maLoai);
                CustomToast customToast = new CustomToast(context, CustomToast.SUCCESS, "Deleted successfully!");
                customToast.showToast();
                dialog.dismiss();
            }
        });

        dialog.show();
    }

    @Override
    public int getItemCount() {
        return loaiMoHinhList.size();
    }

    public class LoaiViewHolder extends RecyclerView.ViewHolder {
        private ImageView imgLoai;
        private TextView tvName, tvSoLuong;
        private ImageButton btnDelete;
        public LoaiViewHolder(@NonNull View itemView) {
            super(itemView);
            imgLoai = itemView.findViewById(R.id.img_itemLoai);
            tvName = itemView.findViewById(R.id.tv_nameItemLoai);
            tvSoLuong = itemView.findViewById(R.id.tv_slItemLoai);
            btnDelete = itemView.findViewById(R.id.btn_deleteItemLoai);
        }
    }
}
