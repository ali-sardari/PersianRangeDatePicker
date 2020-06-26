package com.sardari.ali.persianrangedatepicker;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.sardari.daterangepicker.customviews.DateRangeCalendarView;
import com.sardari.daterangepicker.dialog.DatePickerDialog;
import com.sardari.daterangepicker.utils.PersianCalendar;

public class MainActivity extends AppCompatActivity {

    Button btn_ShowDatePicker;
    TextView txtStartDate, txtEndDate;
    DateRangeCalendarView calendar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initSingleDate();
    }

    private void initSingleDate() {
        calendar = findViewById(R.id.calendar);
        btn_ShowDatePicker = findViewById(R.id.btn_ShowDatePicker);
        txtStartDate = findViewById(R.id.txtStartDate);
        txtEndDate = findViewById(R.id.txtEndDate);
        //********************************************************

        final DatePickerDialog datePickerDialog = new DatePickerDialog(this);
        datePickerDialog.setSelectionMode(DateRangeCalendarView.SelectionMode.Range);
//        datePickerDialog.setEnableTimePicker(true);
//        datePickerDialog.setShowGregorianDate(true);
        datePickerDialog.setTextSizeTitle(10.0f);
        datePickerDialog.setTextSizeWeek(12.0f);
        datePickerDialog.setTextSizeDate(14.0f);
        datePickerDialog.setCanceledOnTouchOutside(true);
        datePickerDialog.setOnRangeDateSelectedListener(new DatePickerDialog.OnRangeDateSelectedListener() {
            @Override
            public void onRangeDateSelected(PersianCalendar startDate, PersianCalendar endDate) {
                txtStartDate.setText(startDate.getPersianShortDateTime());
                txtEndDate.setText(endDate.getPersianShortDateTime());
            }
        });
//        datePickerDialog.setAcceptButtonColor(ContextCompat.getColor(this, R.color.colorAccent));
//        datePickerDialog.showDialog();
        btn_ShowDatePicker.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                datePickerDialog.showDialog();
            }
        });

        //********************************************************


        DateRangeCalendarView calendar = findViewById(R.id.calendar);
        calendar.setCalendarListener(new DateRangeCalendarView.CalendarListener() {
            @Override
            public void onDateSelected(PersianCalendar date) {
                Log.w("calendar",date.getPersianShortDate());
            }

            @Override
            public void onDateRangeSelected(PersianCalendar startDate, PersianCalendar endDate) {
                Log.w("calendar",startDate.getPersianShortDate());

            }

            @Override
            public void onCancel() {

            }
        });
    }

    private void ShowDatePicker() {
        DatePickerDialog datePickerDialog = new DatePickerDialog(MainActivity.this);
        datePickerDialog.setSelectionMode(DateRangeCalendarView.SelectionMode.Range);
//        datePickerDialog.setEnableTimePicker(true);
//        datePickerDialog.setShowGregorianDate(true);
        datePickerDialog.setTextSizeTitle(10.0f);
        datePickerDialog.setTextSizeWeek(12.0f);
        datePickerDialog.setTextSizeDate(14.0f);
        datePickerDialog.setCanceledOnTouchOutside(true);
        datePickerDialog.setOnRangeDateSelectedListener(new DatePickerDialog.OnRangeDateSelectedListener() {
            @Override
            public void onRangeDateSelected(PersianCalendar startDate, PersianCalendar endDate) {
                txtStartDate.setText(startDate.getPersianShortDateTime());
                txtEndDate.setText(endDate.getPersianShortDateTime());
            }
        });
//        datePickerDialog.setAcceptButtonColor(ContextCompat.getColor(this, R.color.colorAccent));
        datePickerDialog.showDialog();
    }
}


//    private void ShowTimePicker() {
////        TextView textView = (TextView) view.findViewById(R.id.txt_Amount);
////        textView.setOnLongClickListener(v -> true);
//
//        TimePickerDialog timePickerDialog = new TimePickerDialog(getActivity(), "select a time:", new TimePickerDialog.TimePickerCallback() {
//            @Override
//            public void onTimeSelected(int hours, int mins) {
////                selectedCal.set(Calendar.HOUR, hours);
////                selectedCal.set(Calendar.MINUTE, mins);
////
////                if (calendarListener != null) {
////                    calendarListener.onDateSelected(selectedCal);
////                }
//            }
//
//            @Override
//            public void onCancel() {
////                DateRangeCalendarView.this.resetAllSelectedViews();
//            }
//        });
//
//        timePickerDialog.showDialog();
//    }
