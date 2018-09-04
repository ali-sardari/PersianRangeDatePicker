package com.sardari.ali.persianrangedatepicker;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.sardari.daterangepicker.customviews.DateRangeCalendarView;
import com.sardari.daterangepicker.dialog.DatePickerDialog;
import com.sardari.daterangepicker.utils.PersianCalendar;

public class MainActivity extends AppCompatActivity {

    Button btn_ShowDatePicker;
    TextView txtStartDate, txtEndDate;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initSingleDate();
    }

    private void initSingleDate() {
        btn_ShowDatePicker = findViewById(R.id.btn_ShowDatePicker);
        txtStartDate = findViewById(R.id.txtStartDate);
        txtEndDate = findViewById(R.id.txtEndDate);

        btn_ShowDatePicker.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ShowDatePicker();
            }
        });
    }

    private void ShowDatePicker() {
        DatePickerDialog datePickerDialog = new DatePickerDialog(MainActivity.this);
        datePickerDialog.setSelectionMode(DateRangeCalendarView.SelectionMode.Range);
        datePickerDialog.setEnableTimePicker(false);
        datePickerDialog.setCanceledOnTouchOutside(true);
        datePickerDialog.setOnRangeDateSelectedListener(new DatePickerDialog.OnRangeDateSelectedListener() {
            @Override
            public void onRangeDateSelected(PersianCalendar startDate, PersianCalendar endDate) {
                txtStartDate.setText(startDate.getPersianShortDateTime());
                txtEndDate.setText(endDate.getPersianShortDateTime());
            }
        });

        datePickerDialog.showDialog();
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

}
