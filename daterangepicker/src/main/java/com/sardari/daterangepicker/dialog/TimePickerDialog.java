package com.sardari.daterangepicker.dialog;

import android.app.Dialog;
import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.TimePicker;

import com.sardari.daterangepicker.R;
import com.sardari.daterangepicker.customviews.CustomTextView;

import java.util.Calendar;

public class TimePickerDialog extends Dialog {
    private CustomTextView tvHeaderTitle, tvDialogDone, tvDialogCancel;

    private String mTitle;
    private int hours, minutes;

    private TimePicker timePicker;

    public interface TimePickerCallback {
        void onTimeSelected(int hours, int mins);

        void onCancel();
    }

    private TimePickerCallback onTimeChangedListener;

    public TimePickerDialog(Context context, String title, TimePickerCallback timePickerCallback) {
        super(context);
//        mContext = context;
        mTitle = title;
        this.onTimeChangedListener = timePickerCallback;

        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        getWindow().setGravity(Gravity.BOTTOM);
        setCanceledOnTouchOutside(false);

        initView();

        setListeners();

        //Grab the window of the dialog, and change the width
        WindowManager.LayoutParams lp = new WindowManager.LayoutParams();
        Window window = this.getWindow();
        lp.copyFrom(window.getAttributes());
        //This makes the dialog take up the full width
        lp.width = WindowManager.LayoutParams.MATCH_PARENT;
        lp.height = WindowManager.LayoutParams.WRAP_CONTENT;
        window.setAttributes(lp);
    }

    private void initView() {
        setContentView(R.layout.dialog_time_picker);

        tvHeaderTitle = findViewById(R.id.tvHeaderTitle);
        tvDialogDone = findViewById(R.id.tvHeaderDone);
        tvDialogCancel = findViewById(R.id.tvHeaderCancel);

        timePicker = findViewById(R.id.timePicker);

        timePicker.setOnTimeChangedListener(new TimePicker.OnTimeChangedListener() {
            @Override
            public void onTimeChanged(TimePicker view, int hourOfDay, int minute) {
                hours = hourOfDay;
                minutes = minute;
            }
        });

        tvHeaderTitle.setText(mTitle);
    }

    private void setListeners() {
        tvDialogCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (onTimeChangedListener != null) {
                    onTimeChangedListener.onCancel();
                }
                TimePickerDialog.this.dismiss();
            }
        });

        tvDialogDone.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (onTimeChangedListener != null) {
                    onTimeChangedListener.onTimeSelected(hours, minutes);
                }
                TimePickerDialog.this.dismiss();
            }
        });
    }

    public void showDialog() {
        hours = Calendar.getInstance().get(Calendar.HOUR_OF_DAY);
        minutes = Calendar.getInstance().get(Calendar.MINUTE);
        this.show();
    }
}
