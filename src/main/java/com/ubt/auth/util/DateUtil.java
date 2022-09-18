package com.ubt.auth.util;

import java.text.SimpleDateFormat;
import java.time.Instant;
import java.util.Calendar;
import java.util.Date;

public class DateUtil {

    public static Date addMinutesToDate(Date date, int minutes) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        calendar.add(Calendar.MINUTE, minutes);
        return calendar.getTime();
    }

    public static Date addHoursToDate(Date date, int hours) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        calendar.add(Calendar.HOUR, hours);
        return calendar.getTime();
    }

    public static Date addDaysToDate(Date date, int days) {
        return addHoursToDate(date, 24 * days);
    }

    public static long differenceInMinutes(Date date) {
        long difference = date.getTime() - new Date().getTime();
        return difference / (60 * 1000) % 60;
    }

    public static long differenceInDays(Date date) {
        long difference = date.getTime() - new Date().getTime();
        return difference / (24 * 60 * 60 * 1000);
    }

    public static boolean currentDateBefore(Date date) {
        return date != null && new Date().before(date);
    }

    public static boolean currentDateAfter(Date date) {
        return date != null && new Date().after(date);
    }

    public static String formatDate(Instant date) {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd-M-yyyy hh:mm:ss");
        return simpleDateFormat.format(Date.from(date));
    }

}