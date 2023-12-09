package com.example.duanmau1.customview;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.duanmau1.R;

public class CustomToast {
    private Context context;
    private View view;
    private Toast toast;
    private TextView tvMess;
    private  View subView;
    private ImageView img;

    public static int WARNING = 0;
    public static int ERROR = -1;
    public static int SUCCESS =1;



    public CustomToast(Context context, int type, String message) {
        this.context = context;
        this.view = LayoutInflater.from(context).inflate(R.layout.cusomt_toast, null);
        this.toast = new Toast(context);
        this.toast.setDuration(Toast.LENGTH_SHORT);
        this.tvMess = this.view.findViewById(R.id.tv_messageToast);
        this.subView = this.view.findViewById(R.id.view);
        this.img = this.view.findViewById(R.id.img);
        this.tvMess.setText(message);
        if (type ==  WARNING) {
            setWarning();
        } else if (type == ERROR) {
            setError();
        } else {
            setSuccess();
        }
        toast.setView(view);
    }

    private void setSuccess() {

    }

    private void setWarning() {
        this.subView.setBackgroundColor(Color.parseColor("#FCBF49"));
        this.img.setImageResource(R.drawable.warning_toast);
    }

    private void setError() {
        this.subView.setBackgroundColor(Color.parseColor("#E04F5F"));
        this.img.setImageResource(R.drawable.close);
    }

     public void showToast(){
        toast.show();
    }

}
