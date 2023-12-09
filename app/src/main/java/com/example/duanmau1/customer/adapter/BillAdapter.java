package com.example.duanmau1.customer.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.duanmau1.GroupFunction.CurrencyConvert;
import com.example.duanmau1.R;
import com.example.duanmau1.dao.HoaDonDao;
import com.example.duanmau1.model.HoaDon;

import java.util.List;

public class BillAdapter extends RecyclerView.Adapter<BillAdapter.BillViewHolder> {
    List<HoaDon> hoaDonList;
    Context context;
    HoaDonDao hoaDonDao;

    public BillAdapter(List<HoaDon> hoaDonList, Context context) {
        this.hoaDonList = hoaDonList;
        this.context = context;
        this.hoaDonDao = new HoaDonDao(context);
    }

    @NonNull
    @Override
    public BillViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_bill_user, parent, false);
        return new BillViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull BillViewHolder holder, int position) {
        HoaDon hoaDon = hoaDonList.get(position);
        SubBillAdapter subBillAdapter = new SubBillAdapter(context, hoaDon.getHoaDonChiTietList());
        LinearLayoutManager manager = new LinearLayoutManager(context);
        holder.recBill.setAdapter(subBillAdapter);
        holder.recBill.setLayoutManager(manager);
        holder.recBill.addItemDecoration(new DividerItemDecoration(context, DividerItemDecoration.VERTICAL));
        holder.tvSluong.setText(hoaDon.getCount() +"");
        holder.tvDate.setText(hoaDon.getNgayLap());
        holder.tvThanhTien.setText(CurrencyConvert.convertFromFloatToVNCurrency(hoaDon.getTongTien()));
        if (hoaDon.getTinhTrang() == 0) {
            holder.tvTinhTrang.setText("Đang chờ xác nhận từ người bán...");
        } else if (hoaDon.getTinhTrang() == 1) {
            holder.tvTinhTrang.setText("Hoàn thành!");
        }else  {
            holder.tvTinhTrang.setText("Đã hủy hoặc người bán từ chối giao!");
        }

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!hoaDon.isExpand()) {
                    holder.recBill.setVisibility(View.VISIBLE);
                    if (hoaDon.getTinhTrang() == 0 ){
                        holder.btnHuyDon.setVisibility(View.VISIBLE);
                    } else if (hoaDon.getTinhTrang() == -1) {
                        holder.btnDatLai.setVisibility(View.VISIBLE);
                    }
                } else {
                    holder.recBill.setVisibility(View.GONE);
                    holder.btnHuyDon.setVisibility(View.GONE);
                    holder.btnDatLai.setVisibility(View.GONE);
                }
                hoaDon.setExpand(!hoaDon.isExpand());
            }
        });

        holder.btnHuyDon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showDialogCancelBill(hoaDon);
            }
        });

        holder.btnDatLai.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showDialogReOrder(hoaDon);
            }
        });
    }

    private void showDialogReOrder(HoaDon hoaDon) {
        View viewDialog = LayoutInflater.from(context).inflate(R.layout.dialog_warning, null);
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setView(viewDialog);
        AlertDialog dialog = builder.create();

        ImageButton btnCancel = viewDialog.findViewById(R.id.btn_closeWarningDialog);
        Button btnConFirm = viewDialog.findViewById(R.id.btn_conFirmWarningDialog);
        TextView tvTitle = viewDialog.findViewById(R.id.tv_titleWarningDialog);
        TextView tvMess = viewDialog.findViewById(R.id.tv_messageWarningDialog);

        tvTitle.setText("XÁC NHẬN");
        tvMess.setText("Bạn có chắc chắn muốn đặt lại đơn hàng!");

        btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });

        btnConFirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                hoaDonDao.reOrder(hoaDon.getMaNd(), hoaDon.getMaHd());
                dialog.dismiss();
            }
        });

        dialog.show();
    }

    private void showDialogCancelBill(HoaDon hoaDon) {
        View viewDialog = LayoutInflater.from(context).inflate(R.layout.dialog_warning, null);
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setView(viewDialog);
        AlertDialog dialog = builder.create();

        ImageButton btnCancel = viewDialog.findViewById(R.id.btn_closeWarningDialog);
        Button btnConFirm = viewDialog.findViewById(R.id.btn_conFirmWarningDialog);
        TextView tvTitle = viewDialog.findViewById(R.id.tv_titleWarningDialog);
        TextView tvMess = viewDialog.findViewById(R.id.tv_messageWarningDialog);

        tvTitle.setText("XÁC NHẬN");
        tvMess.setText("Bạn có chắc chắn muốn hủy hóa đơn?");

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

    public static class BillViewHolder extends RecyclerView.ViewHolder {
        RecyclerView recBill;
        TextView tvSluong, tvThanhTien,tvTinhTrang, tvDate;
        Button btnHuyDon, btnDatLai;
        public BillViewHolder(@NonNull View itemView) {
            super(itemView);
            recBill =itemView.findViewById(R.id.rec_itemBillUser);
            tvSluong = itemView.findViewById(R.id.tv_soLuongItemBill);
            tvThanhTien = itemView.findViewById(R.id.tv_thanhTienItemBill);
            tvTinhTrang = itemView.findViewById(R.id.tv_tinhTrangItemBill);
            tvDate = itemView.findViewById(R.id.tv_dateItemBill);
            btnHuyDon = itemView.findViewById(R.id.btn_huyDonItemBill);
            btnDatLai = itemView.findViewById(R.id.btn_datLaiItemBillUser);
        }
    }
}
