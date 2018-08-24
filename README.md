# PersianRangeDatePicker

![Date Picker](https://github.com/ali-sardari/PersianRangeDatePicker/blob/master/images/range_date_picker.png)

Usages
Use this dependency in your build.gradle file to reference this library in your project

Step 1. Add the JitPack repository to your build file. Add it in your root build.gradle at the end of repositories:

```groovy
allprojects {
        repositories {
            ...
            maven { url "https://jitpack.io" }
        }
    }
```

Step 2. Add the dependency
```groovy
dependencies {
    implementation 'com.github.ali-sardari:PersianRangeDatePicker:1.0.2'
}
```

Then in your Java Code, you use it like below.

```java
{
    DatePickerDialog datePickerDialog = new DatePickerDialog(MainActivity.this);
    datePickerDialog.setCalendarType(DateRangeCalendarView.CalendarType.Persian);
    datePickerDialog.setSelectionMode(DateRangeCalendarView.SelectionMode.Range);
    datePickerDialog.setSelectableDaysCount(5);
    datePickerDialog.setCanceledOnTouchOutside(true);
    datePickerDialog.setOnRangeDateSelectedListener(new DatePickerDialog.OnRangeDateSelectedListener() {
        @Override
        public void onRangeDateSelected(PersianCalendar startDate, PersianCalendar endDate) {
            txtStartDate.setText(startDate.getPersianShortDate());
            txtEndDate.setText(endDate.getPersianShortDate());
        }
    });
}

datePickerDialog.showDialog();
```
