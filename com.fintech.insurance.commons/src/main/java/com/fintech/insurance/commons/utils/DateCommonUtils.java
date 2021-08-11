package com.fintech.insurance.commons.utils;

import com.fintech.insurance.commons.constants.BasicConstants;
import org.apache.commons.lang3.StringUtils;
import org.joda.time.DateTime;
import org.joda.time.Days;
import org.joda.time.LocalDate;
import org.joda.time.Seconds;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

public class DateCommonUtils {

    public static final String DEFAULT_DATE_FORMAT_STR2 = "yyyy-MM-dd HH:mm";
    public static final String DEFAULT_DATE_PREFIX = "19";
    private static final Logger log = LoggerFactory.getLogger(DateCommonUtils.class);
    private static final String DEFAULT_DATE_FROMATE = "yyyy-MM-dd";
    public static final Date FIRST_DATE = DateCommonUtils.formDateString("1970-01-01");
    private static final String DEFAULT_DATE_FORMAT_STR = "yyyy-MM-dd HH:mm:ss";
    private static final String DEFAULT_TIME_FORMAT_STR = "yyyyMMddHHmm";
    public static final String TIME_FORMAT_YEAR_MONTH = "yyMM";

    /**
     * 计算2个日期时间相隔多少天
     *
     * @param date1
     * @param date2
     * @return
     */
    public static long getDays(Date date1, Date date2) {
        if (date1 == null || date2 == null) {
            return 0L;
        }

        DateTime dateTime1 = new DateTime(date1);
        DateTime dateTime2 = new DateTime(date2);
        return Days.daysBetween(dateTime1, dateTime2).getDays();
    }

    //获取指定日期的明天
    public static Date getTomorrow(Date date) {
        if (date == null) {
            date = Calendar.getInstance().getTime();
        }

        DateTime dateTime = new DateTime(date);
        dateTime = dateTime.dayOfMonth().roundFloorCopy().plusDays(1);
        return dateTime.toDate();
    }

    //获取当前日期的明天
    public static Date getTomorrow() {
        DateTime dateTime = DateTime.now();
        dateTime = dateTime.dayOfMonth().roundFloorCopy().plusDays(1);
        return dateTime.toDate();
    }

    //获取今天的凌晨时间
    public static Date getToday() {
        DateTime dateTime = DateTime.now();
        dateTime = dateTime.dayOfMonth().roundFloorCopy();
        return dateTime.toDate();
    }

    //获取指定日期的昨天
    public static Date getYesterday(Date date) {
        if (date == null) {
            date = Calendar.getInstance().getTime();
        }

        DateTime dateTime = new DateTime(date);
        dateTime = dateTime.dayOfMonth().roundFloorCopy().minusDays(1);
        return dateTime.toDate();
    }

    //获取指定日期的0时0分0秒
    public static Date getBeginDateByDate(Date date) {
        if (date == null) {
            date = Calendar.getInstance().getTime();
        }

        DateTime dateTime = new DateTime(date);
        dateTime = dateTime.dayOfMonth().roundFloorCopy();
        return dateTime.toDate();
    }

    //获取当前日期的昨天
    public static Date getYesterday() {
        DateTime dateTime = DateTime.now();
        dateTime = dateTime.dayOfMonth().roundFloorCopy().minusDays(1);
        return dateTime.toDate();
    }

    //获取获取当天的23：59：59时间
    public static Date getEndTimeOfDate(Date date) {
        date = DateCommonUtils.getAfterDay(date, 1);
        DateTime dateTime = new DateTime(date).withMillisOfDay(0);
        return dateTime.secondOfMinute().addToCopy(-1).toDate();
    }

    //获取获取当天的23：59：59时间
    public static Date getEndTimeOfDate(String dateString) {
        return DateCommonUtils.getEndTimeOfDate(DateCommonUtils.formDateString(dateString));
    }

    //获取获取当天的23：59：59时间
    public static Date getEndTimeOfDate(long dateStamp) {
        return DateCommonUtils.getEndTimeOfDate(DateCommonUtils.stampToDate(dateStamp));
    }

    /**
     * 获取当前时间
     *
     * @return yyyy-MM-dd hh:mm:ss格式字符串
     */
    public static String getCurrentTime() {
        DateTime dateTime = DateTime.now();
        return dateTime.toString(DEFAULT_DATE_FORMAT_STR);
    }

    /**
     * 获取当前时间
     *
     * @return yyyy-MM-dd hh:mm:ss格式字符串
     */
    public static String getNowTimeStr() {
        DateTime dateTime = DateTime.now();
        return dateTime.toString(DEFAULT_TIME_FORMAT_STR);
    }

    //根据yyyy-MM-dd获取时间
    public static Date formDateString(String date) {
        try {
            DateTimeFormatter format = DateTimeFormat.forPattern(DEFAULT_DATE_FROMATE);
            return format.parseDateTime(date).toDate();
        } catch (Exception e) {
            log.info("DateCommonUtils", "formDateString", e);
        }
        return null;
    }

    //根据yyyy-MM-dd HH:mm获取时间
    public static Date formDateString2(String date) {
        try {
            DateTimeFormatter format = DateTimeFormat.forPattern(DEFAULT_DATE_FORMAT_STR2);
            return format.parseDateTime(date).toDate();
        } catch (Exception e) {
            log.info("DateCommonUtils", "formDateString2", e);
        }
        return null;
    }

    // 根据指定日期格式格式化时间
    public static String dateToStringByFormat(Date date, String formate) {
        try {
            DateTime dateTime = new DateTime(date);
            return dateTime.toString(formate);
        } catch (Exception e) {
            log.info("DateCommonUtils", "form Date To String failed", e);
        }
        return null;
    }

    //格式化时间 yyyy-MM-dd HH:mm:ss
    public static String formatTime(Date time) {
        if (null == time) {
            return BasicConstants.STRING_BLANK;
        }
        try {
            DateTime dateTime = new DateTime(time);
            return dateTime.toString(DEFAULT_DATE_FORMAT_STR);
        } catch (Exception e) {
            log.info("DateUtil", "formTime", e);
        }
        return null;
    }


    /**
     * 小于当天时间的15点就返回true，大于当天时间的15点就返回false
     *
     * @param date 当天时间
     * @return true/false
     */
    public static boolean is15HoursBefore(Date date) {
        if (date == null) {
            return false;
        }

        DateTime dateTime1 = new DateTime(date);
        DateTime dateTime2 = new DateTime(date).dayOfMonth().roundFloorCopy().plusHours(15);

        return dateTime1.isBefore(dateTime2);
    }

    /**
     * 获得指定日期的后n天
     *
     * @return
     */
    public static Date getAfterDay(Date date, int n) {
        DateTime dateTime = new DateTime(date);
        DateTime nowTime = dateTime.dayOfMonth().addToCopy(n);
        return nowTime.toDate();
    }

    /**
     * 日期格式化，返回年月日
     *
     * @param datetime
     * @return
     */
    public static Date dateFormat(Date datetime) {
        DateTime dateTime = new DateTime(datetime);
        LocalDate nowTime = dateTime.toLocalDate();
        return nowTime.toDate();
    }

    /**
     * 日期格式化，把（yyyy-MM-dd HH:mm:ss）格式转化成（yyyy-MM-dd）
     *
     * @param date 当前日期时间
     * @return 返回年月日（yyyy-MM-dd）
     */
    public static String formatDate(Date date) {
        DateTime dateTime = new DateTime(date);
        return dateTime.toString(DEFAULT_DATE_FROMATE);
    }

    /**
     * 获得指定时间的前一天日期
     *
     * @return
     */
    public static Date getBeforeDay(Date date) {
        if (date == null) {
            return null;
        }
        DateTime dateTime = new DateTime(date);
        DateTime nowTime = dateTime.dayOfMonth().addToCopy(-1);
        return nowTime.toDate();
    }

    /**
     * 获取当前日期（格式yyyy-MM-dd）
     *
     * @return 返回Date类型的当前日期
     */
    public static Date getCurrentDate() {
        LocalDate dateTime = DateTime.now().toLocalDate();
        return dateTime.toDate();
    }

    /**
     * 获取当前日期（格式yyyy-MM-dd）
     *
     * @return 返回Date类型的当前日期
     */
    public static String getDateFormat(Date date) {
        DateTime dateTime = new DateTime(date);
        return dateTime.toString(DEFAULT_DATE_FROMATE);
    }

    /**
     * 返回两个时间中较大者
     */
    public static Date getMaxDate(Date date1, Date date2) {
        if (date1 == null && date2 == null) {
            return null;
        }
        if (date1 == null) {
            return date2;
        }
        if (date2 == null) {
            return date1;
        }
        if (date1.getTime() > date2.getTime()) {
            return date1;
        } else {
            return date2;
        }
    }

    /**
     * 返回两个时间中较小者
     */
    public static Date getMinDate(Date date1, Date date2) {
        if (date1 == null && date2 == null) {
            return null;
        }
        if (date1 == null) {
            return date2;
        }
        if (date2 == null) {
            return date1;
        }
        if (date1.getTime() < date2.getTime()) {
            return date1;
        } else {
            return date2;
        }
    }

    /**
     * @param beginDate :最初的开始时间
     * @param time
     * @param span
     * @return
     * @Description (以span为跨度，time为期数，共span*time个月得到的日期)
     */
    public static Date getEndDateByAddMonths(Date beginDate, Integer time, Integer span) {
        if (beginDate == null || time == null || span == null) {
            return null;
        }
        if (time.equals(0)) {
            return beginDate;
        }
        Calendar cal = Calendar.getInstance();
        Calendar cal2 = Calendar.getInstance();
        Date endDate = null;
        cal.setTime(beginDate);
        cal2.setTime(beginDate);
        int day = cal.get(Calendar.DAY_OF_MONTH);
        int month = cal.get(Calendar.MONTH);
        cal.set(Calendar.MONTH, month + time * span);
        cal.set(Calendar.DAY_OF_MONTH, day - 1);
        if (cal.get(Calendar.MONTH) - cal2.get(Calendar.MONTH) > time * span
                || (cal.get(Calendar.MONTH) - cal2.get(Calendar.MONTH) < 0
                && cal.get(Calendar.MONTH) + 12 - cal2.get(Calendar.MONTH) > time * span)) {
            cal.set(Calendar.DAY_OF_MONTH, 0);
        }
        endDate = cal.getTime();
        return endDate;
    }

    /**
     * @param beginDate :最初的开始时间
     * @param time
     * @param span
     * @return
     * @Description (以span为跨度，time为期数，共span*time个月得到的日期)
     */
    public static Date getBeginDateByAddMonths(Date beginDate, Integer time, Integer span) {
        if (beginDate == null || time == null || span == null) {
            return null;
        }
        if (time.equals(1)) {
            return beginDate;
        }
        time = time - 1;
        Calendar cal = Calendar.getInstance();
        Calendar cal2 = Calendar.getInstance();
        Date endDate = null;
        cal.setTime(beginDate);
        cal2.setTime(beginDate);
        int day = cal.get(Calendar.DAY_OF_MONTH);
        int month = cal.get(Calendar.MONTH);
        cal.set(Calendar.MONTH, month + time * span);
        cal.set(Calendar.DAY_OF_MONTH, day - 1);
        if (cal.get(Calendar.MONTH) - cal2.get(Calendar.MONTH) > time * span
                || (cal.get(Calendar.MONTH) - cal2.get(Calendar.MONTH) < 0
                && cal.get(Calendar.MONTH) + 12 - cal2.get(Calendar.MONTH) > time * span)) {
            cal.set(Calendar.DAY_OF_MONTH, 0);
        }
        endDate = cal.getTime();

        return DateCommonUtils.getAfterDay(endDate, 1);
    }

    /**
     * @param beginDate :开始时间
     * @param time      ：期数
     * @param span      ：跨度
     * @param repayDay  ：回款日
     * @return
     * @Description (获取分期产品的每一期的回款日期)
     */
    public static Date getRepayDateByPeriod(Date beginDate, Integer time, Integer span, Long repayDay) {
        if (beginDate == null || time == null || span == null || repayDay == null) {
            return null;
        }
        Date thisPeriodBeginDate = getBeginDateByAddMonths(beginDate, time, span);
        Date thisPeriodEndDate = getEndDateByAddMonths(beginDate, time, span);
        Calendar cal = Calendar.getInstance();
        cal.setTime(thisPeriodBeginDate);
        cal.set(Calendar.DAY_OF_MONTH, (int) (long) repayDay);
        while (cal.getTime().before(thisPeriodEndDate)) {
            DateTime beginTime = new DateTime(cal.getTime()).plusMonths(1);
            cal.setTime(beginTime.toDate());
        }
        return cal.getTime();
    }

    /**
     * 返回标准格式Date类型
     *
     * @param date
     * @return 1970-01-01
     */
    public static Date sqlDateFormat(Date date) {
        long time = dateFormat(date).getTime();
        return new java.sql.Date(time);

    }

    /**
     * @param indentityCard
     * @return
     * @Description (根据身份证抽取生日)
     */
    public static Date birhtDay(String indentityCard) {
        Date birthday = null;
        String year = null;
        String month = null;
        String day = null;
        if (StringUtils.isNoneBlank(indentityCard)) {

            if (indentityCard.length() == 18) {
                year = indentityCard.substring(6, 10);
                month = indentityCard.substring(10, 12);
                day = indentityCard.substring(12, 14);
            } else if (indentityCard.length() == 15) {
                year = DEFAULT_DATE_PREFIX + indentityCard.substring(6, 8);
                month = indentityCard.substring(8, 10);
                day = indentityCard.substring(10, 12);
            }
            String dateStr = year + "-" + month + "-" + day;
            birthday = DateCommonUtils.formDateString(dateStr);

        }
        return birthday;
    }

    /**
     * 凌晨
     *
     * @param date
     * @return
     * @flag 0 返回yyyy-MM-dd 00:00:00日期<br>
     * 1 返回yyyy-MM-dd 23:59:59日期
     */
    public static Date weeHours(Date date, int flag) {
        if (flag == 0) {
            return new DateTime(date).toLocalDate().toDate();
        } else if (flag == 1) {
            //凌晨23:59:59
            return new DateTime(DateCommonUtils.getTomorrow()).secondOfMinute().addToCopy(-1).toDate();
        }
        return date;
    }

    /**
     * 获取两个日期之间相隔多少秒
     *
     * @return long
     */
    public static int getSeconds(Date validStartTime, Date validEndTime) {
        return Seconds.secondsBetween(new DateTime(validStartTime), new DateTime(validEndTime)).getSeconds();
    }

    //获取当天00:00:00
    public static Date truncateDay(Date date) {
        if (date == null) {
            throw new IllegalArgumentException("Null date!");
        }
        DateTime dateTime = new DateTime(date);
        dateTime = dateTime.dayOfMonth().roundFloorCopy();
        return dateTime.toDate();
    }

    //比较两个日期的大小
    public static int truncatedDayCompare(Date date1, Date date2) {
        if (date1 == null || date2 == null) {
            throw new IllegalArgumentException("Null date1 or date2!");
        }
        DateTime dateTime1 = new DateTime(date1);
        DateTime dateTime2 = new DateTime(date2);
        dateTime1 = dateTime1.dayOfMonth().roundFloorCopy();
        dateTime2 = dateTime2.dayOfMonth().roundFloorCopy();
        return dateTime1.compareTo(dateTime2);

    }
    // 两个日期之间相隔天数
    public static int intervalDays(Date date1, Date date2) {
        if (date1 == null || date2 == null) {
            return 0;
        }
        long time1 = date1.getTime();



        long time2 = date2.getTime();

        return (int)((time2 - time1) / (24 * 3600 * 1000));
    }

    /*
        * 将时间戳转换为时间
        */
    public static Date stampToDate(long timeStamp) {
        DateTime epoc = new DateTime(timeStamp);
        Date date = null;
        try {
            date = epoc.toDate();
        } catch (Exception e) {
            throw new IllegalArgumentException("can not convert the timestamp to Date:" + timeStamp);
        }
        return date;
    }

    /**
     * 获取毫秒级时间戳: 自1970-01-01T00:00:00Z以来的毫秒数
     * @param date
     * @return
     */
    public static Long getDateTimeStamp(Date date) {
        DateTime dateTime = new DateTime(date);
        return dateTime.getMillis();
    }

    public static int getYearOfDate(Date date) {
        if (null == date) {
            throw new IllegalStateException("date is null");
        }
        DateTime dateTime = new DateTime(date);
        return dateTime.getYear();
    }

    // 获取当前世纪的年份数字（两位）
    public static int getYearOfCentury(Date date) {
        if (null == date) {
            throw new IllegalStateException("date is null");
        }
        DateTime dateTime = new DateTime(date);
        return dateTime.getYearOfCentury();
    }

    public static int getMonthOfDate(Date date) {
        if (null == date) {
            throw new IllegalStateException("date is null");
        }
        DateTime dateTime = new DateTime(date);
        return dateTime.getMonthOfYear();
    }

    public static int getDayOfDate(Date date) {
        if (null == date) {
            throw new IllegalStateException("date is null");
        }
        DateTime dateTime = new DateTime(date);
        return dateTime.getDayOfMonth();
    }

    /**
     * 将日期类型字符串转化为时间戳, 如果isEndDate为True, 则使用时间为00:00:00计算为指定日期时间的时间戳，
     * 否则使用23:59:59计算指定日期时间的时间戳.
     * @param dateStr 日期时间字符串，格式如: 2017-01-01
     * @return 时间戳
     */
    public static Long convertDateStringToStamp(String dateStr, Boolean isEndDate) {
        if (StringUtils.isBlank(dateStr)) {
            return null;
        }
        Date date = DateCommonUtils.formDateString(dateStr);
        date = !isEndDate ? DateCommonUtils.truncateDay(date) : DateCommonUtils.getEndTimeOfDate(date);
        return DateCommonUtils.getDateTimeStamp(date);
    }

    /**
     * 返回此刻加上一定秒数后的Date
     * @param seconds
     * @return
     */
    public static Date nowPlusSeconds(int seconds){
        return DateTime.now().plusSeconds(seconds).toDate();
    }

    public static void main(String[] args) {
        /*Date a =DateCommonUtils.getToday();
        System.out.println(DateCommonUtils.getBeginDateByDate(new Date()));
       // System.out.println(DateCommonUtils.getEndTimeOfDate(new Date()));
        //System.out.println(DateUtils.truncatedCompareTo(DateCommonUtils.formDateString("2017-06-02"), new Date(), Calendar.DAY_OF_MONTH));
        List<Date> dates = new ArrayList<Date>();
        dates.add(DateCommonUtils.formDateString("1901-06-02"));
        dates.add(new Date());
        //System.out.println(Collections.max(dates));

        //System.out.println(DateCommonUtils.birhtDay("431102901015511"));
        //System.out.println(DateCommonUtils.formDateString("1970-01-01"));
        //System.out.println(DateCommonUtils.weeHours(new Date(), 1));

        Date current = DateCommonUtils.formDateString("2017-10-31");
        DateTime epoc = new DateTime(current);
        System.out.print(epoc.plusMonths(4).toDate());

        System.out.print(new org.joda.time.DateTime(DateCommonUtils.formDateString("2017-2-31")).plusMonths(4).plusDays(-1).toDate());*/

        int i = intervalDays(DateCommonUtils.getAfterDay(new Date(), 5), DateCommonUtils.getCurrentDate());

        System.out.println(i);

        System.out.println(DateCommonUtils.getToday());
    }

}
