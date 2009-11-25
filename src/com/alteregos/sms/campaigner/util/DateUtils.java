package com.alteregos.sms.campaigner.util;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

/**
 * 
 * @author John Emmanuel
 */
public class DateUtils {

    public static final int MILLIS_IN_DAY = 1000 * 60 * 60 * 24;
    private static final DateFormat SHORT_DATE_FORMAT = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss");

    public static DateFormat getDefaultDateFormat() {
        return SHORT_DATE_FORMAT;
    }

    public static Date getDate(String date, String format) {
        DateFormat dateFormat = new SimpleDateFormat(format);
        Date d = null;
        try {
            d = (Date) dateFormat.parse(date);
        } catch (ParseException parseException) {
            parseException.printStackTrace();
        }
        return d;
    }

    public static boolean isAfter(Date dateToCheck, Date referenceDate) {
        return dateToCheck.after(referenceDate);
    }

    public static boolean isBefore(Date dateToCheck, Date referenceDate) {
        return dateToCheck.before(referenceDate);
    }

    public static boolean isSameOrBefore(Date dateToCheck, Date referenceDate) {
        boolean before = dateToCheck.before(referenceDate);
        boolean equals = dateToCheck.equals(referenceDate);
        return (equals || before);
    }

    public static boolean isSameOrAfter(Date dateToCheck, Date referenceDate) {
        boolean after = dateToCheck.after(referenceDate);
        boolean equals = dateToCheck.equals(referenceDate);
        return (equals || after);
    }

    public static Date getPreviousDate(Date currentDate) {
        return new Date(currentDate.getTime() - MILLIS_IN_DAY);
    }

    public static Date getNextDate(Date currentDate) {
        return new Date(currentDate.getTime() + MILLIS_IN_DAY);
    }

    public static Date getTodaysDate() {
        return truncateDate(new Date());
    }

    public static Date getEndOfDay(Date date) {
        Date truncated = truncateDate(date);
        return addTimeStampToDate(truncated, 23, 59, 59);
    }

    public static Date truncateDate(Date d) {
        GregorianCalendar gc1 = new GregorianCalendar();
        gc1.clear();
        gc1.setTime(d);
        int year = gc1.get(
                Calendar.YEAR),
                month = gc1.get(Calendar.MONTH),
                day = gc1.get(Calendar.DAY_OF_MONTH);
        GregorianCalendar gc2 = new GregorianCalendar(year, month, day);
        return gc2.getTime();
    }

    public static Date addTimeStampToDate(Date date, int hour, int minutes, int seconds) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        calendar.set(Calendar.HOUR_OF_DAY, hour);
        calendar.set(Calendar.MINUTE, minutes);
        calendar.set(Calendar.SECOND, seconds);
        calendar.set(Calendar.MILLISECOND, 0);
        return calendar.getTime();
    }

    public static boolean isBetween(Date dateToCheck, Date firstDate, Date lastDate) {
        boolean isBetween = false;
        boolean isAfterFirst = isAfter(dateToCheck, firstDate);
        boolean isBeforeSecond = isAfter(lastDate, dateToCheck);

        if (isAfterFirst && isBeforeSecond) {
            isBetween = true;
        }

        return isBetween;
    }

    public static Date minus12Hours(Date date) {
        return new Date(date.getTime() - MILLIS_IN_DAY / 2);
    }

    public static int getCurrentYear() {
        Calendar cal = Calendar.getInstance();
        return cal.get(Calendar.YEAR);
    }

    public static int getCurrentMonth() {
        Calendar cal = Calendar.getInstance();
        return cal.get(Calendar.MONTH);
    }

    public static int getDaysInCurrentMonth() {
        int year = getCurrentYear();
        int month = getCurrentMonth();
        Calendar cal = new GregorianCalendar(year, month, 1);
        return cal.getActualMaximum(Calendar.DAY_OF_MONTH);
    }

    public static int getDaysInMonth(int month, int year) {
        Calendar cal = new GregorianCalendar(year, month, 1);
        return cal.getActualMaximum(Calendar.DAY_OF_MONTH);
    }

    public static String getFormattedDate(int dayOfMonth, int month, int year, String format) {
        Calendar cal = Calendar.getInstance();
        cal.set(Calendar.DAY_OF_MONTH, dayOfMonth);
        cal.set(Calendar.MONTH, month);
        cal.set(Calendar.YEAR, year);
        Date date = truncateDate(cal.getTime());
        DateFormat dateFormat = new SimpleDateFormat(format);
        return dateFormat.format(date);
    }

    public static Date formatTime(long totalTime) {



        long hours,
                mins,
                secs;
        hours = totalTime / (60 * 60);
        mins = totalTime % (60 * 60);
        secs = mins % (60);
        mins /= 60;
        if (secs > 60) {
            secs -= 60;
            mins++;
        }
        if (mins > 60) {
            mins -= 60;
            hours++;
        }
        Calendar c = Calendar.getInstance();
        c.set(Calendar.HOUR_OF_DAY, (int) hours);
        c.set(Calendar.MINUTE, (int) mins);
        c.set(Calendar.SECOND, (int) secs);
        return c.getTime();
    }

    public static boolean isInFormat(String date, String format) {
        try {
            DateFormat dateFormat = new SimpleDateFormat(format);
            dateFormat.parse(date);
        } catch (ParseException parseException) {
            return false;
        }
        return true;
    }
}
