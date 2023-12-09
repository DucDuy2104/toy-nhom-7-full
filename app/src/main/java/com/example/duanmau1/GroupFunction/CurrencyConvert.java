package com.example.duanmau1.GroupFunction;

import java.text.NumberFormat;
import java.util.Locale;

public class CurrencyConvert {
    public static String convertFromFloatToVNCurrency(int price) {
        Locale localeVN = new Locale("vi", "VN");
        NumberFormat currencyFormatter = NumberFormat.getCurrencyInstance(localeVN);

        // Định dạng số tiền theo định dạng tiền tệ Việt Nam
        String formattedAmount = currencyFormatter.format(price);
        return formattedAmount;
    }

    public static int converFromVNCurrencyToFloat(String formattedAmount) {
        String numericString = formattedAmount.replaceAll("[^\\d]", "");

        int amount = Integer.parseInt(numericString);
        return amount;
    }
}
