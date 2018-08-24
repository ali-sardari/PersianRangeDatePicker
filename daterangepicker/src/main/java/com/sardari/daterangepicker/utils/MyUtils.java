package com.sardari.daterangepicker.utils;

import android.content.Context;
import android.view.Gravity;
import android.widget.Toast;

public class MyUtils {
    private static final MyUtils ourInstance = new MyUtils();

    public static MyUtils getInstance() {
        return ourInstance;
    }

    private MyUtils() {
    }

    public void Toast(Context context, String message) {
        Toast toast = Toast.makeText(context, message, Toast.LENGTH_SHORT);
        toast.setGravity(Gravity.CENTER, 0, 0);
        toast.show();
    }
}
