package com.example.duanmau1.admin.adapter;

import android.content.Context;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextClock;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.duanmau1.GroupFunction.CurrencyConvert;
import com.example.duanmau1.R;
import com.example.duanmau1.dao.HoaDonChiTietDao;
import com.example.duanmau1.dao.HoaDonDao;
import com.example.duanmau1.dao.NguoiDungDao;
import com.example.duanmau1.model.HoaDon;
import com.example.duanmau1.model.NguoiDung;

import java.util.List;

public class BillAdminAdapter extends RecyclerView.Adapter<BillAdminAdapter.BillAdminViewHolder> {
    Context context;
    List<HoaDon> hoaDonList;
    NguoiDungDao nguoiDungDao;
    HoaDonChiTietDao hoaDonChiTietDao;
    HoaDonDao hoaDonDao;

    public BillAdminAdapter(Context context, List<HoaDon> hoaDonList) {
        this.context = context;
        this.hoaDonList = hoaDonList;
        this.nguoiDungDao = new NguoiDungDao(context);
        this.hoaDonChiTietDao = new HoaDonChiTietDao(context);
        this.hoaDonDao = new HoaDonDao(context);
    }

    @NonNull
    @Override
    public BillAdminViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_bill_admin, parent,false);
        return new BillAdminViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull BillAdminViewHolder holder, int position) {
        HoaDon hoaDon = hoaDonList.get(position);
        nguoiDungDao.getNguoiDungById(hoaDon.getMaNd(), new NguoiDungDao.OnGetDataSuccess() {
            @Override
            public void onGetNdSuccess(NguoiDung nguoiDung) {
                Glide.with(context).load(Uri.parse(nguoiDung.getImageUri())).into(holder.img);
                holder.tvName.setText(nguoiDung.getTenNd());
                holder.tvAmount.setText(hoaDon.getCount() +"");
                holder.tvTotal.setText(CurrencyConvert.convertFromFloatToVNCurrency(hoaDon.getTongTien()));
                holder.tvDate.setText(hoaDon.getNgayLap());

                SubBillAdminhAdapter subBillAdminhAdapter = new SubBillAdminhAdapter(context, hoaDon.getHoaDonChiTietList());
                LinearLayoutManager linearLayoutManager = new LinearLayoutManager(context);
                holder.recBill.setAdapter(subBillAdminhAdapter);
                holder.recBill.setLayoutManager(linearLayoutManager);

                if (hoaDon.getTinhTrang() == 0) {
                    holder.tvStatus.setText("Đang chờ xác nhận từ người bán...");
                } else if (hoaDon.getTinhTrang() == 1) {
                    holder.tvStatus.setText("Hoàn thành!");
                }else  {
                    holder.tvStatus.setText("Đã hủy hoặc người bán từ chối giao!");
                }

                holder.itemView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (!hoaDon.isExpand()) {
                            holder.recBill.setVisibility(View.VISIBLE);
                            if (hoaDon.getTinhTrang() == 0) {
                                holder.btnConfirm.setVisibility(View.VISIBLE);
                                holder.btnCancel.setVisibility(View.VISIBLE);
                            }
                        } else {
                            holder.recBill.setVisibility(View.GONE);
                            holder.btnCancel.setVisibility(View.GONE);
                            holder.btnConfirm.setVisibility(View.GONE);
                        }
                        hoaDon.setExpand(!hoaDon.isExpand());
                    }
                });

                holder.btnCancel.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        showDialogCancel(hoaDon);
                    }
                });

                holder.btnConfirm.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        showDialogConfirm(hoaDon);
                    }
                });

            }
        });

    }

    private void showDialogConfirm(HoaDon hoaDon) {
        View viewDialog = LayoutInflater.from(context).inflate(R.layout.dialog_warning, null);
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setView(viewDialog);
        AlertDialog dialog = builder.create();

        ImageButton btnCancel = viewDialog.findViewById(R.id.btn_closeWarningDialog);
        Button btnConFirm = viewDialog.findViewById(R.id.btn_conFirmWarningDialog);
        TextView tvTitle = viewDialog.findViewById(R.id.tv_titleWarningDialog);
        TextView tvMess = viewDialog.findViewById(R.id.tv_messageWarningDialog);

        tvTitle.setText("XÁC NHẬN");
        tvMess.setText("Xác nhận đơn hàng!");
        btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });

        btnConFirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                hoaDonDao.confirmBill(hoaDon.getMaNd(), hoaDon.getMaHd());
                dialog.dismiss();
            }
        });

        dialog.show();
    }

    private void showDialogCancel(HoaDon hoaDon) {
        View viewDialog = LayoutInflater.from(context).inflate(R.layout.dialog_warning, null);
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setView(viewDialog);
        AlertDialog dialog = builder.create();

        ImageButton btnCancel = viewDialog.findViewById(R.id.btn_closeWarningDialog);
        Button btnConFirm = viewDialog.findViewById(R.id.btn_conFirmWarningDialog);
        TextView tvTitle = viewDialog.findViewById(R.id.tv_titleWarningDialog);
        TextView tvMess = viewDialog.findViewById(R.id.tv_messageWarningDialog);

        tvTitle.setText("XÁC NHẬN");
        tvMess.setText("Bạn có chắc chắn muốn đặt từ chối đơn hàng này?");

        btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });

        btnConFirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                hoaDonDao.cancelBill(hoaDon.getMaNd(), hoaDon.getMaHd());
                dialog.dismiss();
            }
        });

        dialog.show();
    }

    @Override
    public int getItemCount() {
        return hoaDonList.size();
    }

    public static class BillAdminViewHolder extends RecyclerView.ViewHolder {
        ImageView img;
        TextView tvName, tvAmount, tvTotal, tvDate, tvStatus;
        Button btnConfirm, btnCancel;
        RecyclerView recBill;
        public BillAdminViewHolder(@NonNull View itemView) {
            super(itemView);
            img = itemView.findViewById(R.id.img_userItemBillAdmin);
            tvName = itemView.findViewById(R.id.tv_nameItemBillAdmin);
            tvDate = itemView.findViewById(R.id.tv_dateItemBillAdmin);
            tvAmount = itemView.findViewById(R.id.tv_amoutItemBillAdmin);
            tvTotal = itemView.findViewById(R.id.tv_totalItemBillAdmin);
            btnConfirm = itemView.findViewById(R.id.btn_confirmItemBillAdmin);
            btnCancel = itemView.findViewById(R.id.btn_cancelItemBillAdmin);
            recBill = itemView.findViewById(R.id.rec_itemBillAdmin);
            tvStatus = itemView.findViewById(R.id.tv_statusItemBillAdmin);
        }
    }
}
