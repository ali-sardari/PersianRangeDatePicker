package com.sardari.daterangepicker.customviews;

import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Color;
import android.graphics.Typeface;
import android.graphics.drawable.GradientDrawable;
import android.os.Build;
import android.support.annotation.ColorInt;
import android.support.v4.content.ContextCompat;
import android.util.AttributeSet;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.sardari.daterangepicker.R;
import com.sardari.daterangepicker.dialog_fragment.TimePickerDialog;
import com.sardari.daterangepicker.models.DayContainer;
import com.sardari.daterangepicker.utils.MyUtils;
import com.sardari.daterangepicker.utils.PersianCalendar;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Locale;

import static com.sardari.daterangepicker.customviews.DateRangeCalendarView.SelectionMode.Multiple;
import static com.sardari.daterangepicker.customviews.DateRangeCalendarView.SelectionMode.Range;
import static com.sardari.daterangepicker.customviews.DateRangeCalendarView.SelectionMode.Single;

public class DateRangeCalendarView extends LinearLayout {
    //region Fields
    private Context mContext;
    private LinearLayout llDaysContainer;
    private LinearLayout llTitleWeekContainer;
    private CustomTextView tvYearTitle;
    private TextView tvYearGeorgianTitle;
    private ImageView imgVNavLeft, imgVNavRight;
    private Locale locale;

    private PersianCalendar currentCalendarMonth, minSelectedDate, maxSelectedDate;
    private ArrayList<Integer> selectedDatesRange = new ArrayList<>();
    private Typeface typeface;

    private static final int STRIP_TYPE_NONE = 0;
    private static final int STRIP_TYPE_LEFT = 1;
    private static final int STRIP_TYPE_RIGHT = 2;

    private int weekColor;
    private int titleColor;
    private int rangeStripColor;
    private int selectedDateCircleColor;
    private int selectedDateColor, defaultDateColor, disableDateColor, rangeDateColor, holidayColor, headerColor, todayColor;
    private boolean shouldEnabledTime = false;
    private float textSizeTitle, textSizeWeek, textSizeDate;
    private PersianCalendar selectedCal, date;
    //endregion

    //region Enum
    public enum CalendarType {
        Persian(1),
        Gregorian(2);

        private final int value;

        CalendarType(int value) {
            this.value = value;
        }

        public int getValue() {
            return value;
        }
    }

    public enum SelectionMode {
        Single(1),
        Multiple(2),
        Range(3),
        None(4);

        private final int value;

        SelectionMode(int value) {
            this.value = value;
        }

        public int getValue() {
            return value;
        }
    }
    //endregion

    //region Constructor
    public DateRangeCalendarView(Context context) {
        super(context);
        initView(context, null);
    }

    public DateRangeCalendarView(Context context, AttributeSet attrs) {
        super(context, attrs);
        initView(context, attrs);
    }

    public DateRangeCalendarView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initView(context, attrs);
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    public DateRangeCalendarView(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        initView(context, attrs);
    }

    /**
     * To initialize child views
     *
     * @param context      - App context
     * @param attributeSet - Attr set
     */
    private void initView(Context context, AttributeSet attributeSet) {
        mContext = context;
        locale = context.getResources().getConfiguration().locale;
        setAttributes(attributeSet);

        LayoutInflater layoutInflater = LayoutInflater.from(mContext);

        LinearLayout mainView = (LinearLayout) layoutInflater.inflate(R.layout.layout_calendar_month, this, true);
        llDaysContainer = mainView.findViewById(R.id.llDaysContainer);
        llTitleWeekContainer = mainView.findViewById(R.id.llTitleWeekContainer);
        tvYearTitle = mainView.findViewById(R.id.tvYearTitle);
        tvYearGeorgianTitle = mainView.findViewById(R.id.tvYearGeorgianTitle);
        imgVNavLeft = mainView.findViewById(R.id.imgVNavLeft);
        imgVNavRight = mainView.findViewById(R.id.imgVNavRight);

        RelativeLayout rlHeaderCalendar = mainView.findViewById(R.id.rlHeaderCalendar);
        rlHeaderCalendar.setBackgroundColor(headerColor);

        setListeners();

        if (isInEditMode()) {
            return;
        }

        drawCalendarForMonth(getCurrentMonth(new PersianCalendar()));

        setWeekTitleColor(weekColor);
    }

    /**
     * To parse attributes from xml layout to configure calendar views.
     *
     * @param attributeSet - Attribute set
     */
    private void setAttributes(AttributeSet attributeSet) {
        textSizeTitle = getResources().getDimension(R.dimen.text_size_title);
        textSizeWeek = getResources().getDimension(R.dimen.text_size_week);
        textSizeDate = getResources().getDimension(R.dimen.text_size_date);

        weekColor = ContextCompat.getColor(mContext, R.color.week_color);
        titleColor = ContextCompat.getColor(mContext, R.color.title_color);
        rangeStripColor = ContextCompat.getColor(mContext, R.color.range_bg_color);
        selectedDateCircleColor = ContextCompat.getColor(mContext, R.color.selected_date_circle_color);
        selectedDateColor = ContextCompat.getColor(mContext, R.color.selected_date_color);
        defaultDateColor = ContextCompat.getColor(mContext, R.color.default_date_color);
        holidayColor = ContextCompat.getColor(mContext, R.color.holiday_date_color);
        todayColor = ContextCompat.getColor(mContext, R.color.today_date_color);
        rangeDateColor = ContextCompat.getColor(mContext, R.color.range_date_color);
        disableDateColor = ContextCompat.getColor(mContext, R.color.disable_date_color);
        headerColor = ContextCompat.getColor(mContext, R.color.header_color);


//        if (attributeSet != null) {
        TypedArray ta = mContext.obtainStyledAttributes(attributeSet, R.styleable.DateRangeCalendarView, 0, 0);
        try {
            weekColor = ta.getColor(R.styleable.DateRangeCalendarView_week_color, weekColor);
            titleColor = ta.getColor(R.styleable.DateRangeCalendarView_title_color, titleColor);
//            rangeStripColor = ta.getColor(R.styleable.DateRangeCalendarView_range_color, rangeStripColor);
            selectedDateCircleColor = ta.getColor(R.styleable.DateRangeCalendarView_selected_date_circle_color, selectedDateCircleColor);
            shouldEnabledTime = ta.getBoolean(R.styleable.DateRangeCalendarView_enable_time_selection, false);

            textSizeTitle = ta.getDimension(R.styleable.DateRangeCalendarView_text_size_title, textSizeTitle);
            textSizeWeek = ta.getDimension(R.styleable.DateRangeCalendarView_text_size_week, textSizeWeek);
            textSizeDate = ta.getDimension(R.styleable.DateRangeCalendarView_text_size_date, textSizeDate);

            selectedDateColor = ta.getColor(R.styleable.DateRangeCalendarView_selected_date_color, selectedDateColor);
            defaultDateColor = ta.getColor(R.styleable.DateRangeCalendarView_default_date_color, defaultDateColor);
            rangeDateColor = ta.getColor(R.styleable.DateRangeCalendarView_range_date_color, rangeDateColor);
            disableDateColor = ta.getColor(R.styleable.DateRangeCalendarView_disable_date_color, disableDateColor);
            headerColor = ta.getColor(R.styleable.DateRangeCalendarView_header_color, headerColor);

            selectionMode = ta.getInt(R.styleable.DateRangeCalendarView_selectionMode, Single.getValue());
        } finally {
            ta.recycle();
        }
//        }
    }
    //endregion

    //region NavigationClickListener & dayClickListener
    private void setListeners() {
        //region imgVNavLeft.setOnClickListener
        imgVNavLeft.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                if (currentCalendarMonth.getPersianMonth() == 0) {
                    currentCalendarMonth.setPersianYear(currentCalendarMonth.getPersianYear() - 1);
                    currentCalendarMonth.setPersianMonth(11);
                } else {
                    currentCalendarMonth.setPersianMonth(currentCalendarMonth.getPersianMonth() - 1);
                }

                currentCalendarMonth.setPersianDay(1);

                drawCalendarForMonth(currentCalendarMonth);
            }
        });
        //endregion

        //region imgVNavRight.setOnClickListener
        imgVNavRight.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                currentCalendarMonth.setPersianMonth(currentCalendarMonth.getPersianMonth() + 1);
                currentCalendarMonth.setPersianDay(1);

                drawCalendarForMonth(currentCalendarMonth);
            }
        });
        //endregion
    }

    private final OnClickListener dayClickListener = new OnClickListener() {
        @Override
        public void onClick(View view) {
            DayContainer container = new DayContainer((RelativeLayout) view);
            int key = (int) view.getTag();

            selectedCal = new PersianCalendar();
            date = DayContainer.GetDateFromKey(String.valueOf(key));
            selectedCal.setPersianDate(date.getPersianYear(), date.getPersianMonth(), date.getPersianDay());

            if (selectionMode == Single.getValue()) {
                //region SelectionMode.Single
                resetAllSelectedViews();
                makeAsSelectedDate(container, 0);

                if (shouldEnabledTime) {
                    //region shouldEnabledTime
                    TimePickerDialog awesomeTimePickerDialog = new TimePickerDialog(mContext, mContext.getString(R.string.select_time), new TimePickerDialog.TimePickerCallback() {
                        @Override
                        public void onTimeSelected(int hours, int mins) {
                            selectedCal.set(Calendar.HOUR, hours);
                            selectedCal.set(Calendar.MINUTE, mins);

                            if (calendarListener != null) {
                                calendarListener.onDateSelected(selectedCal);
                            }
                        }

                        @Override
                        public void onCancel() {
                            resetAllSelectedViews();
                        }
                    });
                    awesomeTimePickerDialog.showDialog();
                    //endregion
                } else {
                    //region SelectDateWithoutTime
                    if (calendarListener != null) {
                        calendarListener.onDateSelected(selectedCal);
                    }
                    //endregion
                }
                //endregion
            } else if (selectionMode == Multiple.getValue()) {
                //region SelectionMode.Multiple
                //endregion
            } else if (selectionMode == Range.getValue()) {
                //region SelectionMode.Range
                if (minSelectedDate != null) {
                    if (maxSelectedDate == null) {
                        maxSelectedDate = selectedCal;
                        drawSelectedDateRange(minSelectedDate, maxSelectedDate);
                    } else {
                        MyUtils.getInstance().Toast(mContext, "تاریخ برگشت را وارد کنید");

                        resetAllSelectedViews();

                        minSelectedDate = selectedCal;
                        maxSelectedDate = null;

                        makeAsSelectedDate(container, 0);
                    }
                } else {
                    MyUtils.getInstance().Toast(mContext, "تاریخ برگشت را وارد کنید");

                    minSelectedDate = selectedCal;
                    maxSelectedDate = null;

                    makeAsSelectedDate(container, 0);
                }

                if (shouldEnabledTime) {
                    //region shouldEnabledTime
                    TimePickerDialog awesomeTimePickerDialog = new TimePickerDialog(mContext, mContext.getString(R.string.select_time), new TimePickerDialog.TimePickerCallback() {
                        @Override
                        public void onTimeSelected(int hours, int mins) {
                            selectedCal.set(Calendar.HOUR, hours);
                            selectedCal.set(Calendar.MINUTE, mins);

                            Log.i("Tag", "Time: " + selectedCal.getTime().toString());
                            if (calendarListener != null && minSelectedDate != null && maxSelectedDate != null) {
                                calendarListener.onDateRangeSelected(minSelectedDate, maxSelectedDate);
                            }
                        }

                        @Override
                        public void onCancel() {
                            resetAllSelectedViews();
                        }
                    });
                    awesomeTimePickerDialog.showDialog();
                    //endregion
                } else {
                    //region SelectDateWithoutTime
                    if (calendarListener != null && minSelectedDate != null && maxSelectedDate != null) {
                        calendarListener.onDateRangeSelected(minSelectedDate, maxSelectedDate);
                    }
                    //endregion
                }
                //endregion
            } else {
                //region SelectionMode.None
                //endregion
            }
        }
    };
    //endregion
    //---------------------------------------------------------------------------------------------

    /**
     * To clone calendar obj and get current month calendar starting from 1st date.
     *
     * @param calendar - Calendar
     * @return - Modified calendar obj of month of 1st date.
     */
    private PersianCalendar getCurrentMonth(PersianCalendar calendar) {
        PersianCalendar current = (PersianCalendar) calendar.clone();
        current.setPersianDay(1);
        return current;
    }

    /**
     * To draw calendar for the given month. Here calendar object should start from date of 1st.
     *
     * @param month
     */
    @SuppressLint("SetTextI18n")
    private void drawCalendarForMonth(PersianCalendar month) {
        tvYearTitle.setText(String.format(locale, "%s %d", month.getPersianMonthName(), month.getPersianYear()));

        Log.w("TAG", "DateRangeCalendarView_drawCalendarForMonth_358-> :" + month.getPersianMonth());

        int _month = month.getPersianMonth() + 1;
        switch (_month) {
            case 1:
                tvYearGeorgianTitle.setText("March - April " + month.get(Calendar.YEAR));
                break;
            case 2:
                tvYearGeorgianTitle.setText("April - May " + month.get(Calendar.YEAR));
                break;
            case 3:
                tvYearGeorgianTitle.setText("May - June " + month.get(Calendar.YEAR));
                break;
            case 4:
                tvYearGeorgianTitle.setText("June - July " + month.get(Calendar.YEAR));
                break;
            case 5:
                tvYearGeorgianTitle.setText("July - August " + month.get(Calendar.YEAR));
                break;
            case 6:
                tvYearGeorgianTitle.setText("August - September " + month.get(Calendar.YEAR));
                break;
            case 7:
                tvYearGeorgianTitle.setText("September - October " + month.get(Calendar.YEAR));
                break;
            case 8:
                tvYearGeorgianTitle.setText("October - November " + month.get(Calendar.YEAR));
                break;
            case 9:
                tvYearGeorgianTitle.setText("November - December " + month.get(Calendar.YEAR));
                break;
            case 10:
                tvYearGeorgianTitle.setText(String.format("December %s - January %s ", month.get(Calendar.YEAR), month.get(Calendar.YEAR) + 1));
                break;
            case 11:
                tvYearGeorgianTitle.setText("January - February " + month.get(Calendar.YEAR));
                break;
            case 12:
                tvYearGeorgianTitle.setText("February - March " + month.get(Calendar.YEAR));
                break;
        }

        currentCalendarMonth = (PersianCalendar) month.clone();

        int startDay = month.get(Calendar.DAY_OF_WEEK);
        int persianDay = month.getPersianDay();

        if (startDay != 7) {
            month.setPersianDay(persianDay - startDay);
        }

        for (int i = 0; i < llDaysContainer.getChildCount(); i++) {
            LinearLayout weekRow = (LinearLayout) llDaysContainer.getChildAt(i);

            for (int j = 0; j < 7; j++) {
                RelativeLayout rlDayContainer = (RelativeLayout) weekRow.getChildAt(j);
                DayContainer container = new DayContainer(rlDayContainer);

                if (typeface != null) {
                    container.tvDate.setTypeface(typeface);
                }

                drawDayContainer(container, month);

                month.setPersianDay(month.getPersianDay() + 1);
            }
        }
    }

    /**
     * To draw specific date container according to past date, today, selected or from range.
     *
     * @param container - Date container
     * @param calendar  - Calendar obj of specific date of the month.
     */
    private void drawDayContainer(DayContainer container, PersianCalendar calendar) {
        int date;
        int dateGR;
        if (calendarType == CalendarType.Persian.getValue()) {
            date = calendar.getPersianDay();
            dateGR = calendar.get(Calendar.DATE);
        } else {
            date = calendar.getPersianDay();
            dateGR = calendar.get(Calendar.DATE);
        }

        //----------------------------------------------------------------
        if (currentCalendarMonth.getPersianMonth() != calendar.getPersianMonth()) {
            hideDayContainer(container);
        } else if (isDisableDaysAgo && getCurrentDate().after(calendar) && (getCurrentDate().get(Calendar.DAY_OF_YEAR) != calendar.get(Calendar.DAY_OF_YEAR))) {
            disableDayContainer(container);
//            container.tvDate.setText(String.valueOf(date));

            isToday(container, calendar);
        } else {
            int key = DayContainer.GetContainerKey(calendar);

            isToday(container, calendar);

            if (selectedDatesRange.indexOf(key) == 0) {
                makeAsSelectedDate(container, STRIP_TYPE_LEFT);
            } else if (selectedDatesRange.size() != 0 && selectedDatesRange.indexOf(key) == selectedDatesRange.size() - 1) {
                makeAsSelectedDate(container, STRIP_TYPE_RIGHT);
            } else if (selectedDatesRange.contains(key)) {
                makeAsRangeDate(container);
            } else {
                enabledDayContainer(container);
                selectHolidayContainer(container, calendar);
            }

            //---check date selected-------------------------------------------------------------
            if ((selectionMode == SelectionMode.Single.getValue()) && (selectedCal != null && calendar.getPersianShortDate().equals(selectedCal.getPersianShortDate()))) {
                makeAsSelectedDate(container, STRIP_TYPE_LEFT);
            } else if ((selectionMode == SelectionMode.Range.getValue()) && (minSelectedDate != null && calendar.getPersianShortDate().equals(minSelectedDate.getPersianShortDate()))) {
                makeAsSelectedDate(container, STRIP_TYPE_LEFT);
            }

            //---disable max date-------------------------------------------------------------
            if ((getMaxDate() != null) && (getMaxDate().before(calendar))) {
                disableDayContainer(container);
//                container.tvDate.setText(String.valueOf(date));
            }

        }

        container.tvDate.setText(String.valueOf(date));
        container.tvDateGeorgian.setText(String.valueOf(dateGR));

        container.rootView.setTag(DayContainer.GetContainerKey(calendar));
    }

    //---------------------------------------------------------------------------------------------
    private boolean isToday(DayContainer container, PersianCalendar persianCalendar) {
        if (getCurrentDate().compareTo(persianCalendar) == 0) {
            container.imgEvent.setVisibility(VISIBLE);
            container.imgEvent.setColorFilter(todayColor, android.graphics.PorterDuff.Mode.SRC_IN);
//            container.tvDate.setTextColor(todayColor);
            container.tvDate.setTypeface(typeface, Typeface.BOLD);

            return true;
        } else {
            container.imgEvent.setVisibility(GONE);
            container.tvDate.setTypeface(typeface, Typeface.NORMAL);

            return false;
        }
    }

    /**
     * To hide date if date is from previous month.
     *
     * @param container - Container
     */
    private void hideDayContainer(DayContainer container) {
        container.tvDate.setText("");
        container.tvDate.setBackgroundColor(Color.TRANSPARENT);
        container.strip.setBackgroundColor(Color.TRANSPARENT);
        container.rootView.setBackgroundColor(Color.TRANSPARENT);
        container.rootView.setVisibility(INVISIBLE);
        container.rootView.setOnClickListener(null);
    }

    /**
     * To disable past date. Click listener will be removed.
     *
     * @param container - Container
     */
    private void disableDayContainer(DayContainer container) {
        container.tvDate.setBackgroundColor(Color.TRANSPARENT);
        container.strip.setBackgroundColor(Color.TRANSPARENT);
        container.rootView.setBackgroundColor(Color.TRANSPARENT);
        container.tvDate.setTextColor(disableDateColor);
        container.rootView.setVisibility(VISIBLE);
        container.rootView.setOnClickListener(null);
    }

    /**
     * To enable date by enabling click listeners.
     *
     * @param container - Container
     */
    private void enabledDayContainer(DayContainer container) {
        container.tvDate.setBackgroundColor(Color.TRANSPARENT);
        container.strip.setBackgroundColor(Color.TRANSPARENT);
        container.rootView.setBackgroundColor(Color.TRANSPARENT);
        container.tvDate.setTextColor(defaultDateColor);
        container.rootView.setVisibility(VISIBLE);
        container.rootView.setOnClickListener(dayClickListener);
    }

    /**
     * To enable date by enabling click listeners.
     *
     * @param container - Container
     */
    private void selectHolidayContainer(DayContainer container, PersianCalendar _calendar) {
        if (_calendar.get(Calendar.DAY_OF_WEEK) == 6) {
            container.tvDate.setTextColor(holidayColor);
        }

        if (_calendar.getPersianMonth() + 1 == 1) {
            if (_calendar.getPersianDay() == 1 ||
                    _calendar.getPersianDay() == 2 ||
                    _calendar.getPersianDay() == 3 ||
                    _calendar.getPersianDay() == 4 ||
                    _calendar.getPersianDay() == 13) {
                container.tvDate.setTextColor(holidayColor);
            }
        }

        if (_calendar.getPersianMonth() + 1 == 12) {
            if (_calendar.getPersianDay() == 29 || _calendar.getPersianDay() == 30) {
                container.tvDate.setTextColor(holidayColor);
            }
        }
    }

    /**
     * To draw date container as selected as end selection or middle selection.
     *
     * @param container - Container
     * @param stripType - Right end date, Left end date or middle
     */
    private void makeAsSelectedDate(DayContainer container, int stripType) {
        RelativeLayout.LayoutParams layoutParams = (RelativeLayout.LayoutParams) container.strip.getLayoutParams();
        if (stripType == STRIP_TYPE_LEFT) {
            GradientDrawable mDrawable = (GradientDrawable) ContextCompat.getDrawable(mContext, R.drawable.range_bg_left);
            mDrawable.setColor(rangeStripColor);
            container.strip.setBackground(mDrawable);
            layoutParams.setMargins(20, 0, 0, 0);
        } else if (stripType == STRIP_TYPE_RIGHT) {
            GradientDrawable mDrawable = (GradientDrawable) ContextCompat.getDrawable(mContext, R.drawable.range_bg_right);
            mDrawable.setColor(rangeStripColor);
            container.strip.setBackground(mDrawable);
            layoutParams.setMargins(0, 0, 20, 0);
        } else {
            container.strip.setBackgroundColor(Color.TRANSPARENT);
            layoutParams.setMargins(0, 0, 0, 0);
        }

        container.strip.setLayoutParams(layoutParams);
        GradientDrawable mDrawable = (GradientDrawable) ContextCompat.getDrawable(mContext, R.drawable.shape_rect);
        mDrawable.setColor(selectedDateCircleColor);
        container.tvDate.setBackground(mDrawable);
        container.rootView.setBackgroundColor(Color.TRANSPARENT);
        container.tvDate.setTextColor(selectedDateColor);
//        container.imgEvent.setColorFilter(selectedDateColor, PorterDuff.Mode.SRC_IN);
        container.rootView.setVisibility(VISIBLE);
    }

    /**
     * To draw date as middle date
     *
     * @param container - Container
     */
    private void makeAsRangeDate(DayContainer container) {
        container.tvDate.setBackgroundColor(Color.TRANSPARENT);
        GradientDrawable mDrawable = (GradientDrawable) ContextCompat.getDrawable(mContext, R.drawable.range_bg);
        mDrawable.setColor(rangeStripColor);
        container.strip.setBackground(mDrawable);
        container.rootView.setBackgroundColor(Color.TRANSPARENT);
        container.tvDate.setTextColor(rangeDateColor);
//        container.imgEvent.setColorFilter(rangeDateColor, android.graphics.PorterDuff.Mode.SRC_IN);
        container.rootView.setVisibility(VISIBLE);

        RelativeLayout.LayoutParams layoutParams = (RelativeLayout.LayoutParams) container.strip.getLayoutParams();
        layoutParams.setMargins(0, 0, 0, 0);
        container.strip.setLayoutParams(layoutParams);
    }

    /**
     * To draw selected date range.
     *
     * @param startDate - Start date
     * @param lastDate  - End date
     */
    private void drawSelectedDateRange(PersianCalendar startDate, PersianCalendar lastDate) {
        selectedDatesRange.clear();
        int startDateKey = DayContainer.GetContainerKey(startDate);
        int lastDateKey = DayContainer.GetContainerKey(lastDate);

        PersianCalendar temp = (PersianCalendar) startDate.clone();

        if (startDateKey == lastDateKey) {
//            minSelectedDate = maxSelectedDate = startDate;
        } else if (startDateKey > lastDateKey) {
            maxSelectedDate = startDate;
            minSelectedDate = lastDate;

            int tempXyz = startDateKey;
            startDateKey = lastDateKey;
            lastDateKey = tempXyz;

            temp = (PersianCalendar) lastDate.clone();
        }

        RelativeLayout rlDayContainer = llDaysContainer.findViewWithTag(startDateKey);

        if (rlDayContainer != null && minSelectedDate.getPersianMonth() == currentCalendarMonth.getPersianMonth()) {
            DayContainer container = new DayContainer(rlDayContainer);
            int stripType = STRIP_TYPE_LEFT;
            if (startDateKey == lastDateKey) {
                stripType = STRIP_TYPE_NONE;
            }

            makeAsSelectedDate(container, stripType);
        }

        rlDayContainer = llDaysContainer.findViewWithTag(lastDateKey);
        if (rlDayContainer != null && maxSelectedDate.getPersianMonth() == currentCalendarMonth.getPersianMonth()) {
            DayContainer container = new DayContainer(rlDayContainer);
            int stripType = STRIP_TYPE_RIGHT;
            if (startDateKey == lastDateKey) {
                stripType = STRIP_TYPE_NONE;
            }
            makeAsSelectedDate(container, stripType);
        }

        selectedDatesRange.add(startDateKey);
        temp.setPersianDate(temp.getPersianYear(), temp.getPersianMonth(), temp.getPersianDay() + 1);

        int nextDateKey = DayContainer.GetContainerKey(temp);
        while (lastDateKey > nextDateKey) {
            if (temp.getPersianMonth() == currentCalendarMonth.getPersianMonth()) {
                rlDayContainer = llDaysContainer.findViewWithTag(nextDateKey);
                if (rlDayContainer != null) {
                    DayContainer container = new DayContainer(rlDayContainer);

                    makeAsRangeDate(container);
                }
            }
            selectedDatesRange.add(nextDateKey);
            temp.setPersianDate(temp.getPersianYear(), temp.getPersianMonth(), temp.getPersianDay() + 1);

            nextDateKey = DayContainer.GetContainerKey(temp);
        }
        selectedDatesRange.add(lastDateKey);
    }

    /**
     * To remove all selection and redraw current calendar
     */
    public void resetAllSelectedViews() {
        selectedDatesRange.clear();

        minSelectedDate = null;
        maxSelectedDate = null;

        drawCalendarForMonth(currentCalendarMonth);

        if (calendarListener != null) {
            calendarListener.onCancel();
        }
    }

    /**
     * To set week title color
     *
     * @param color - resource color value
     */
    public void setWeekTitleColor(@ColorInt int color) {
        weekColor = color;
        for (int i = 0; i < llTitleWeekContainer.getChildCount(); i++) {
            CustomTextView textView = (CustomTextView) llTitleWeekContainer.getChildAt(i);
            textView.setTextColor(color);
        }
    }

    /**
     * To apply custom fonts to all the text views
     *
     * @param fonts - Typeface that you want to apply
     */
    public void setFonts(Typeface fonts) {
        this.typeface = fonts;
        if (fonts != null) {
            drawCalendarForMonth(currentCalendarMonth);
            tvYearTitle.setTypeface(fonts);

            for (int i = 0; i < llTitleWeekContainer.getChildCount(); i++) {
                CustomTextView textView = (CustomTextView) llTitleWeekContainer.getChildAt(i);
                textView.setTypeface(fonts);
            }
        }
    }

    //---------------------------------------------------------------------------------------------
    //region selectionMode -> Getter/Setter
    private int selectionMode = SelectionMode.Single.getValue();

    public int getSelectionMode() {
        return selectionMode;
    }

    public void setSelectionMode(int selectionMode) {
        this.selectionMode = selectionMode;
    }
    //endregion

    //region calendarType -> Getter/Setter
    private int calendarType = CalendarType.Persian.getValue();

    public int getCalendarType() {
        return calendarType;
    }

    public void setCalendarType(int calendarType) {
        this.calendarType = calendarType;
    }
    //endregion

    //region currentDate -> Getter/Setter
    private PersianCalendar currentDate = new PersianCalendar();

    public PersianCalendar getCurrentDate() {
        return currentDate;
    }

    public void setCurrentDate(PersianCalendar currentDate) {
        this.currentDate = currentDate;

        if (currentDate != null) {
            currentCalendarMonth = (PersianCalendar) currentDate.clone();
            currentCalendarMonth.setPersianDay(1);

            resetAllSelectedViews();
        }
    }
    //endregion

    //region MinDate -> Getter/Setter
    private PersianCalendar minDate = new PersianCalendar();

    public PersianCalendar getMinDate() {
        return minDate;
    }

    public void setMinDate(PersianCalendar minDate) {
        this.minDate = minDate;
    }
    //endregion

    //region MaxDate -> Getter/Setter
    private PersianCalendar maxDate;

    public PersianCalendar getMaxDate() {
        return maxDate;
    }

    public void setMaxDate(PersianCalendar maxDate) {
        this.maxDate = maxDate;
    }
    //endregion

    //region isDisableDaysAgo -> Getter/Setter
    private boolean isDisableDaysAgo = true;

    public boolean isDisableDaysAgo() {
        return isDisableDaysAgo;
    }

    public void setDisableDaysAgo(boolean disableDaysAgo) {
        isDisableDaysAgo = disableDaysAgo;
    }
    //endregion

    //region messageEnterDate
    String messageEnterDate;

    public String getMessageEnterDate() {
        return messageEnterDate;
    }

    public void setMessageEnterDate(String messageEnterDate) {
        this.messageEnterDate = messageEnterDate;
    }
    //endregion

    //region messageEnterStartDate
    String messageEnterStartDate;

    public String getMessageEnterStartDate() {
        return messageEnterStartDate;
    }

    public void setMessageEnterStartDate(String messageEnterStartDate) {
        this.messageEnterStartDate = messageEnterStartDate;
    }
    //endregion

    //region messageEnterEndDate
    String messageEnterEndDate;

    public String getMessageEnterEndDate() {
        return messageEnterEndDate;
    }

    public void setMessageEnterEndDate(String messageEnterEndDate) {
        this.messageEnterEndDate = messageEnterEndDate;
    }
    //endregion

    //region Listener -> Getter/Setter
    private CalendarListener calendarListener;

    public CalendarListener getCalendarListener() {
        return calendarListener;
    }

    public void setCalendarListener(CalendarListener calendarListener) {
        this.calendarListener = calendarListener;
    }
    //endregion
    //--------------------------------------------------------------------------------------------

    public interface CalendarListener {
        void onDateSelected(PersianCalendar date);

        void onDateRangeSelected(PersianCalendar startDate, PersianCalendar endDate);

        void onCancel();
    }
}
