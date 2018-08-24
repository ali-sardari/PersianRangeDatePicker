package com.sardari.daterangepicker.utils;

public class PersianCalendarUtils {
    public static long persianToJulian(long year, int month, int day) {
        return 365L *
                ((ceil(year - 474L, 2820D) + 474L) - 1L) +
                ((long) Math.floor((682L * (ceil(year - 474L, 2820D) + 474L) - 110L) / 2816D)) +
                (PersianCalendarConstants.PERSIAN_EPOCH - 1L) + 1029983L *
                ((long) Math.floor((year - 474L) / 2820D)) +
                (month < 7 ? 31 * month : 30 * month + 6) + day;
    }

    public static boolean isPersianLeapYear(int persianYear) {
        return PersianCalendarUtils.ceil((38D + (PersianCalendarUtils.ceil(persianYear - 474L, 2820L) + 474L)) * 682D, 2816D) < 682L;
    }

    public static long julianToPersian(long julianDate) {
        long persianEpochInJulian = julianDate - persianToJulian(475L, 0, 1);
        long cyear = ceil(persianEpochInJulian, 1029983D);
        long ycycle = cyear != 1029982L ? ((long) Math.floor((2816D * (double) cyear + 1031337D) / 1028522D)) : 2820L;
        long year = 474L + 2820L * ((long) Math.floor(persianEpochInJulian / 1029983D)) + ycycle;
        long aux = (1L + julianDate) - persianToJulian(year, 0, 1);
        int month = (int) (aux > 186L ? Math.ceil((double) (aux - 6L) / 30D) - 1 : Math.ceil((double) aux / 31D) - 1);
        int day = (int) (julianDate - (persianToJulian(year, month, 1) - 1L));
        return (year << 16) | (month << 8) | day;
    }

    public static long ceil(double double1, double double2) {
        return (long) (double1 - double2 * Math.floor(double1 / double2));
    }
}
