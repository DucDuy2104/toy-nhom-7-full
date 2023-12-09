package com.example.duanmau1.admin.adapter;

import android.content.Context;
import android.media.Image;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.duanmau1.R;
import com.example.duanmau1.model.NguoiDung;

import java.util.List;

public class KhachHangAdminAdapter extends RecyclerView.Adapter<KhachHangAdminAdapter.KhachHangViewHolder> {
    private List<NguoiDung> nguoiDungList;
    private Context context;

    public KhachHangAdminAdapter(List<NguoiDung> nguoiDungList, Context context) {
        this.nguoiDungList = nguoiDungList;
        this.context = context;
    }

    public void setNguoiDungList(List<NguoiDung> nguoiDungList) {
        this.nguoiDungList = nguoiDungList;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public KhachHangViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_khach_hang_admin, parent, false);
        return new KhachHangViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull KhachHangViewHolder holder, int position) {
        NguoiDung nguoiDung = nguoiDungList.get(position);

        Glide.with(context).load(Uri.parse(nguoiDung.getImageUri())).into(holder.imgUser);
        holder.tvName.setText(nguoiDung.getTenNd());
        holder.tvPhone.setText("SDT: " + nguoiDung.getSdtNd());
        holder.tvAddress.setText("Dia chi: " +  nguoiDung.getDiaChiNd());
    }

    @Override
    public int getItemCount() {
        return nguoiDungList.size();
    }

    public class KhachHangViewHolder extends RecyclerView.ViewHolder {
        private ImageView imgUser;
        private TextView tvName,tvPhone, tvAddress;
        private ImageButton btnDelete;
        public KhachHangViewHolder(@NonNull View itemView) {
            super(itemView);
            imgUser = itemView.findViewById(R.id.img_itemKhachHang);
            tvName = itemView.findViewById(R.id.tv_nameItemKhachHang);
            tvPhone = itemView.findViewById(R.id.tv_sdtItemKhachHang);
            tvAddress = itemView.findViewById(R.id.tv_diaChiItemKhachHang);
        }
    }
}
