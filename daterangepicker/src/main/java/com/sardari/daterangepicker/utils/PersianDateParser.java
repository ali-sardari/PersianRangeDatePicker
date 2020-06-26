package com.sardari.daterangepicker.utils;

public class PersianDateParser {
    private String dateString;
    private String delimiter = "/";

    private PersianDateParser(String dateString) {
        this.dateString = dateString;
    }

    PersianDateParser(String dateString, String delimiter) {
        this(dateString);
        this.delimiter = delimiter;
    }

    public PersianCalendar getPersianDate() {
        checkDateStringInitialValidation();

        String[] tokens = splitDateString(normalizeDateString(dateString));
        int year = Integer.parseInt(tokens[0]);
        int month = Integer.parseInt(tokens[1]);
        int day = Integer.parseInt(tokens[2]);

        checkPersianDateValidation(year, month, day);

        PersianCalendar pCal = new PersianCalendar();
        pCal.setPersianDate(year, month, day);

        return pCal;
    }

    private void checkPersianDateValidation(int year, int month, int day) {
        if (year < 1)
            throw new RuntimeException("year is not valid");
        if (month < 1 || month > 12)
            throw new RuntimeException("month is not valid");
        if (day < 1 || day > 31)
            throw new RuntimeException("day is not valid");
        if (month > 6 && day == 31)
            throw new RuntimeException("day is not valid");
        if (month == 12 && day == 30 && !PersianCalendarUtils.isPersianLeapYear(year))
            throw new RuntimeException("day is not valid " + year + " is not a leap year");
    }

    private String normalizeDateString(String dateString) {
        return dateString;
    }

    private String[] splitDateString(String dateString) {
        String[] tokens = dateString.split(delimiter);
        if (tokens.length != 3)
            throw new RuntimeException("wrong date:" + dateString + " is not a Persian Date or can not be parsed");

        return tokens;
    }

    private void checkDateStringInitialValidation() {
        if (dateString == null)
            throw new RuntimeException("input didn't assign please use setDateString()");
    }
}
