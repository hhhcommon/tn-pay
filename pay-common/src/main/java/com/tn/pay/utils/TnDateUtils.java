package com.tn.pay.utils;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.Period;
import java.time.format.DateTimeFormatter;

/**
 * 日期解析工具类
 * Created by taokai on 2017/12/5.
 */
public class TnDateUtils {
    public static final DateTimeFormatter FORMATTER_ALL = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss.S");
    public static final DateTimeFormatter FORMATTER_FULL = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
    public static final DateTimeFormatter FORMAT_DAY = DateTimeFormatter.ofPattern("yyyy-MM-dd");
    public static final DateTimeFormatter FORMAT_SECONDS = DateTimeFormatter.ofPattern("HH:mm:ss");


    /**
     * 获得时间格式
     *
     * @param timeStr
     * @return
     */
    public static DateTimeFormatter getFormatter(String timeStr) {
        int length = timeStr.length();
        if (length == 19) {
            return FORMATTER_FULL;
        } else if (length == 10) {
            return FORMAT_DAY;
        } else if (length == 8) {
            return FORMAT_SECONDS;
        } else if (length == 21) {
            return FORMATTER_ALL;
        }
        return null;
    }


    /**
     * 获取传入时间的零点零分零秒的字符串形式
     *
     * @param localDate
     * @return
     */
    public static String parseDatetoStringZeroPoint(LocalDate localDate) {
        return localDate.format(FORMAT_DAY) + " 00:00:00";
    }

    /**
     * 解析时间为LocalDate
     *
     * @param timeStr
     * @return
     */
    public static LocalDate parseDate(String timeStr) {
        return LocalDate.parse(timeStr, getFormatter(timeStr));
    }

    /**
     * 解析日期类型为字符串 yyyy-MM-dd HH:mm:ss
     *
     * @return
     */
    public static String parseLocalDateTimetoString(LocalDateTime localDateTime) {
        return localDateTime.format(FORMATTER_FULL);
    }

    /**
     * 传入日期和时间返回具体时间 yyyy-MM-dd HH:mm:ss
     */
    public static String parseLocalDateTimetoString(LocalDate localDate,int hour,int min,int seconds) {
      return   parseLocalDateTimetoString(LocalDateTime.of(localDate.getYear(),localDate.getMonth(),localDate.getDayOfMonth(),hour,min,seconds));
    }


    /**
     * 获取两个时间的相差天数
     *
     * @param startStr
     * @param endStr
     * @return
     */
    public static long dayDiff(String startStr, String endStr) {
        LocalDate stDate = LocalDate.parse(startStr, getFormatter(startStr));
        LocalDate etDate = LocalDate.parse(endStr, getFormatter(endStr));
        return etDate.toEpochDay() - stDate.toEpochDay();
    }

    /**
     * 或两个日期相差月数
     *
     * @param start
     * @param end
     * @return
     */
    public static int monthDiff(LocalDate start, LocalDate end) {
        Period period = Period.between(start, end);
        return period.getYears() * 12 + period.getMonths();
    }

}
