package com.sardari.daterangepicker.models;

import android.support.v7.widget.AppCompatImageView;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.sardari.daterangepicker.customviews.CustomTextView;
import com.sardari.daterangepicker.utils.PersianCalendar;

public class DayContainer {
    public RelativeLayout rootView;
    public CustomTextView tvDate;
    public TextView tvDateGeorgian;
    public AppCompatImageView imgEvent;
    public View strip;

    public DayContainer(RelativeLayout rootView) {
        this.rootView = rootView;
        strip = rootView.getChildAt(0);
        tvDate = (CustomTextView) rootView.getChildAt(1);
        tvDateGeorgian = (TextView) rootView.getChildAt(3);
        imgEvent = (AppCompatImageView) rootView.getChildAt(2);
    }

    public static int GetContainerKey(PersianCalendar cal) {
        String str = cal.getPersianShortDate().replace("/", "");
        return Integer.parseInt(str);
    }

    public static String GetContainerKey2(PersianCalendar date) {
        String year, month, day;
        year = date.getPersianYear() + "";
        if (date.getPersianMonth() <= 9)
            month = "0" + date.getPersianMonth();
        else
            month = date.getPersianMonth() + "";
        if (date.getPersianDay() <= 9)
            day = "0" + date.getPersianDay();
        else
            day = date.getPersianDay() + "";

        return year + month + day;
    }

    public static PersianCalendar GetDateFromKey(String key) {
        PersianCalendar persianCalendar = new PersianCalendar();

        int _y = Integer.parseInt(key.substring(0, 4));
        int _m = Integer.parseInt(key.substring(4, 6));
        int _d = Integer.parseInt(key.substring(6));

        persianCalendar.setPersianDate(_y, _m - 1, _d);
        return persianCalendar;
    }
}
