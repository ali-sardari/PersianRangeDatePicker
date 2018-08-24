package com.sardari.daterangepicker.dialog_fragment;

import android.app.Dialog;
import android.content.Context;
import android.graphics.Typeface;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.widget.Button;

import com.sardari.daterangepicker.R;
import com.sardari.daterangepicker.customviews.DateRangeCalendarView;
import com.sardari.daterangepicker.utils.MyUtils;
import com.sardari.daterangepicker.utils.PersianCalendar;

import java.util.ArrayList;

public class DatePickerDialog extends Dialog {
    //region Fields
    private Context mContext;
    private DateRangeCalendarView calendar;
    private Button btn_Accept;
    private PersianCalendar date, startDate, endDate;
    //endregion

    public DatePickerDialog(Context context) {
        super(context);
        mContext = context;
        requestWindowFeature(Window.FEATURE_NO_TITLE);

        if (getWindow() != null)
            getWindow().setGravity(Gravity.CENTER);

        initView();

        PersianCalendar today = new PersianCalendar();
        setCurrentDate(today);
    }

    private void initView() {
        //region init View & Font
        setContentView(R.layout.dialog_date_picker);

        btn_Accept = findViewById(R.id.btn_Accept);
        calendar = findViewById(R.id.calendar);
        //endregion
    }

    public void showDialog() {
        calendar.setCalendarListener(new DateRangeCalendarView.CalendarListener() {
            @Override
            public void onDateSelected(PersianCalendar _date) {
                date = _date;
            }

            @Override
            public void onDateRangeSelected(PersianCalendar _startDate, PersianCalendar _endDate) {
                startDate = _startDate;
                endDate = _endDate;
            }

            @Override
            public void onCancel() {

            }
        });

        btn_Accept.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (selectionMode == DateRangeCalendarView.SelectionMode.Single) {
                    //region SelectionMode.Single
                    if (date != null) {
                        if (onSingleDateSelectedListener != null) {
                            onSingleDateSelectedListener.onSingleDateSelected(date);
                        }

                        dismiss();
                    } else {
                        MyUtils.getInstance().Toast(mContext, "لطفا یک تاریخ انتخاب کنید");
                    }
                    //endregion
                } else if (selectionMode == DateRangeCalendarView.SelectionMode.Range) {
                    //region SelectionMode.Range
                    if (startDate != null) {
                        if (endDate != null) {
                            if (onRangeDateSelectedListener != null) {
                                onRangeDateSelectedListener.onRangeDateSelected(startDate, endDate);
                            }

                            dismiss();
                        } else {
                            MyUtils.getInstance().Toast(mContext, "لطفا بازه زمانی را مشخص کنید");
                        }
                    } else {
                        MyUtils.getInstance().Toast(mContext, "لطفا بازه زمانی را مشخص کنید");
                    }
                    //endregion
                }
            }
        });

        calendar.setMaxDate(maxDate);
        calendar.setMinDate(minDate);
        calendar.setCurrentDate(currentDate);
        calendar.setCalendarType(calendarType.getValue());
        calendar.setSelectionMode(selectionMode.getValue());
        calendar.setDisableDaysAgo(true);

        this.show();
    }

    //region Properties
    //region Typeface -> Getter/Setter
    public void setTypeface(Typeface typeface) {
        if (typeface != null) {
            calendar.setFonts(typeface);
        }
    }
    //endregion

    //region SelectionMode -> Getter/Setter {Single(1),Multiple(2),Range(3),None(4)}
    private DateRangeCalendarView.SelectionMode selectionMode = DateRangeCalendarView.SelectionMode.Single;

    public DateRangeCalendarView.SelectionMode getSelectionMode() {
        return selectionMode;
    }

    public void setSelectionMode(DateRangeCalendarView.SelectionMode selectionMode) {
        this.selectionMode = selectionMode;

        if (selectionMode != null)
            calendar.setSelectionMode(selectionMode.getValue());
    }
    //endregion

    //region CalendarType -> Getter/Setter { Persian(1),Gregorian(2)}
    private DateRangeCalendarView.CalendarType calendarType = DateRangeCalendarView.CalendarType.Persian;

    public DateRangeCalendarView.CalendarType getCalendarType() {
        return calendarType;
    }

    public void setCalendarType(DateRangeCalendarView.CalendarType calendarType) {
        this.calendarType = calendarType;

        calendar.setCalendarType(calendarType.getValue());
    }
    //endregion

    //region currentDate -> Getter/Setter
    private PersianCalendar currentDate;

    public PersianCalendar getCurrentDate() {
        return currentDate;
    }

    public void setCurrentDate(PersianCalendar currentDate) {
        this.currentDate = currentDate;
        calendar.setCurrentDate(currentDate);
    }
    //endregion

    //region MinDate -> Getter/Setter
    private PersianCalendar minDate;

    public PersianCalendar getMinDate() {
        return minDate;
    }

    public void setMinDate(PersianCalendar minDate) {
        this.minDate = minDate;
        calendar.setMinDate(minDate);
    }
    //endregion

    //region MaxDate -> Getter/Setter
    private PersianCalendar maxDate;

    public PersianCalendar getMaxDate() {
        return maxDate;
    }

    public void setMaxDate(PersianCalendar maxDate) {
        this.maxDate = maxDate;
        calendar.setMaxDate(maxDate);
    }
    //endregion

    //region SelectableDaysCount -> Getter/Setter
    private int selectableDaysCount;

    public int getSelectableDaysCount() {
        return selectableDaysCount;
    }

    public void setSelectableDaysCount(int selectableDaysCount) {
        this.selectableDaysCount = selectableDaysCount;
    }
    //endregion

    //region Listener -> OnSingleDateSelected & OnRangeDateSelected
    //region OnSingleDateSelected -> Getter/Setter
    private OnSingleDateSelectedListener onSingleDateSelectedListener;

    public OnSingleDateSelectedListener getOnSingleDateSelectedListener() {
        return onSingleDateSelectedListener;
    }

    public void setOnSingleDateSelectedListener(OnSingleDateSelectedListener onSingleDateSelectedListener) {
        this.onSingleDateSelectedListener = onSingleDateSelectedListener;
    }
    //endregion

    //region OnRangeDateSelected -> Getter/Setter
    private OnRangeDateSelectedListener onRangeDateSelectedListener;

    public OnRangeDateSelectedListener getOnRangeDateSelectedListener() {
        return onRangeDateSelectedListener;
    }

    public void setOnRangeDateSelectedListener(OnRangeDateSelectedListener onRangeDateSelectedListener) {
        this.onRangeDateSelectedListener = onRangeDateSelectedListener;
    }
    //endregion
    //endregion
    //endregion

    //region Listeners -> Interface
    public interface OnSingleDateSelectedListener {
        void onSingleDateSelected(PersianCalendar date);
    }

    public interface OnRangeDateSelectedListener {
        void onRangeDateSelected(PersianCalendar startDate, PersianCalendar endDate);
    }

    public interface OnMultipleDateSelectedListener {
        void onMultipleDateSelected(ArrayList<PersianCalendar> startDate);
    }
    //endregion
}
