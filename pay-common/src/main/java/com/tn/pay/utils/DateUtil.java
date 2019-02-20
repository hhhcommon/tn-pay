package com.tn.pay.utils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class DateUtil {
    private static final String OLD_PATTERN = "yyyyMMddHHmmss";
    private static final String NEW_PATTERN = "yyyy-MM-dd HH:mm:ss";
    private static final SimpleDateFormat OLD = new SimpleDateFormat(OLD_PATTERN);
    private static final SimpleDateFormat NEW = new SimpleDateFormat(NEW_PATTERN);

    public static String getSimpleDate(String date) {
        if (date.length() > NEW_PATTERN.length()) {
            return date.substring(0, NEW_PATTERN.length());
        }
        return date;
    }

    public static String transSimpleDate(String date) throws ParseException {
        Date parse = OLD.parse(date);
        return NEW.format(parse);
    }

    public static String transSmallDate(String date) throws ParseException {
        Date parse = NEW.parse(date);
        return OLD.format(parse);
    }

    public static String getNowTime() {
        return new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date());
    }
}
