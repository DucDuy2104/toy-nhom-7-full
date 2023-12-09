package com.example.duanmau1.dao;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;

public class SharePreNguoiDung {
    private Context context;
    private SharedPreferences sharedPreferences;
    private SharedPreferences.Editor editor;

    public SharePreNguoiDung(Context context) {
        this.context = context;
        this.sharedPreferences = ((Activity)context).getSharedPreferences("stoy",Context.MODE_PRIVATE);
        this.editor = sharedPreferences.edit();
    }

    public void setMaNd(int mand) {
        editor.putInt("mand", mand);
        editor.apply();
    }

    public int getMaNd() {
        return sharedPreferences.getInt("mand", -1);
    }

    public void setPass(String pass) {
        editor.putString("pass", pass);
        editor.apply();
    }

    public void setUserName(String userName) {
        editor.putString("userName", userName);
        editor.apply();
    }

    public void setRePass(String rePass) {
        editor.putString("rePass", rePass);
        editor.apply();
    }

    public String getRePass() {
        return sharedPreferences.getString("rePass", "-1");
    }

    public String getPass() {
        return sharedPreferences.getString("pass", "-1");
    }

    public String getUserName() {
        return sharedPreferences.getString("userName", "-1");
    }

    public void clearData() {
        editor.clear();
        editor.apply();
    }
}
