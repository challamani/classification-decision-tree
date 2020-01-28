package com.practice.spring_boot.decision_tree.c45.util;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.TimeZone;
import java.util.concurrent.TimeUnit;

public class CommonUtil {
    private static final String BETWEEN="between";
    private static final String STRING_EQ="stringCompare";
    private static final String NUM_EQ="numberEq";
    private static final String LESS_THAN="lessThan";
    private static final String GREATER_THAN="greaterThan";

    public static Object convertValueType(String decisionType,String value){

        switch (decisionType) {
            case BETWEEN:
            case GREATER_THAN:
            case LESS_THAN:
                return Double.parseDouble(value);
            case NUM_EQ:
                return Integer.parseInt(value);
            default:
                return value;

        }
    }

    public static String getCurrentDateTime(String format) {
        try {
            Calendar calendar = Calendar.getInstance();
            calendar.add(Calendar.HOUR, 2);
            Date date = calendar.getTime();
            DateFormat dateFormat = new SimpleDateFormat(format);
            dateFormat.setTimeZone(TimeZone.getTimeZone("UTC"));
            return dateFormat.format(date);
        } catch (Exception e) {
            return null;
        }
    }


    public static Date getStringToDate(String format,String dateStr) {
        try {
            DateFormat dateFormat = new SimpleDateFormat(format);
            return dateFormat.parse(dateStr.trim());
        } catch (Exception e) {
            System.out.println(e.getMessage());
            return null;
        }
    }

    public static long getDifferenceDays(Date d1, Date d2) {
        long diff = d2.getTime() - d1.getTime();
        return TimeUnit.DAYS.convert(diff, TimeUnit.MILLISECONDS);
    }

    public static String getDateToString(String format,Date date) {
        try {
            DateFormat dateFormat = new SimpleDateFormat(format);
            return dateFormat.format(date);
        } catch (Exception e) {
            return "";
        }
    }
}
