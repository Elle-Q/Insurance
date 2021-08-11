package com.fintech.insurance.commons.utils;

import org.joda.time.DateTime;
import org.joda.time.LocalDate;
import org.junit.Assert;
import org.junit.Test;

import java.util.Calendar;
import java.util.Date;

public class DateCommonUtilsTest {


    @Test
    public void getDays() throws Exception {
        Date begin = DateCommonUtils.getYesterday();
        Date end = DateCommonUtils.getTomorrow();

        Assert.assertEquals(DateCommonUtils.getDays(begin, end), 2);

    }

    @Test
    public void getTomorrow() throws Exception {
        Date begin = new DateTime(new Date()).toLocalDate().toDate();
        Date end = DateCommonUtils.getTomorrow();

        Assert.assertEquals(DateCommonUtils.getDays(begin, end), 1);
    }

    @Test
    public void getTomorrow1() throws Exception {
        Date begin = new DateTime(new Date()).toLocalDate().toDate();
        Date end = DateCommonUtils.getTomorrow(begin);

        Assert.assertEquals(DateCommonUtils.getDays(begin, end), 1);
    }

    @Test
    public void getToday() throws Exception {
        Date begin = new DateTime(new Date()).toLocalDate().toDate();
        Date end = DateCommonUtils.getToday();

        Assert.assertEquals(DateCommonUtils.getDays(begin, end), 0);
    }

    @Test
    public void getYesterday() throws Exception {
        Date begin = new DateTime(new Date()).toLocalDate().toDate();
        Date end = DateCommonUtils.getYesterday();

        Assert.assertEquals(DateCommonUtils.getDays(begin, end), -1);
    }

    @Test
    public void getYesterday1() throws Exception {
        Date begin = new DateTime(new Date()).toLocalDate().toDate();
        Date end = DateCommonUtils.getYesterday(begin);

        Assert.assertEquals(DateCommonUtils.getDays(begin, end), -1);
    }

    @Test
    public void getEndTimeOfDate() throws Exception {
        Date begin = new DateTime(new Date()).toLocalDate().toDate();
        Date end = DateCommonUtils.getEndTimeOfDate(DateCommonUtils.formatDate(begin));
        Calendar cal = Calendar.getInstance();
        cal.setTime(begin);
        Calendar cal2 = Calendar.getInstance();
        cal2.setTime(end);
        Assert.assertEquals(cal.get(Calendar.DAY_OF_MONTH)-cal2.get(Calendar.DAY_OF_MONTH), 0);
    }

    @Test
    public void getEndTimeOfDate2() throws Exception {
        Date begin = new DateTime(new Date()).toLocalDate().toDate();
        Date end = DateCommonUtils.getEndTimeOfDate(DateCommonUtils.getDateTimeStamp(begin));
        Calendar cal = Calendar.getInstance();
        cal.setTime(begin);
        Calendar cal2 = Calendar.getInstance();
        cal2.setTime(end);
        Assert.assertEquals(cal.get(Calendar.DAY_OF_MONTH)-cal2.get(Calendar.DAY_OF_MONTH), 0);
    }

    @Test
    public void getEndTimeOfDate3() throws Exception {
        Date begin = DateCommonUtils.formDateString("2018-01-10");
        Date end = DateCommonUtils.getEndTimeOfDate(DateCommonUtils.getDateTimeStamp(begin));
        Calendar cal = Calendar.getInstance();
        cal.setTime(begin);
        Calendar cal2 = Calendar.getInstance();
        cal2.setTime(end);
        Assert.assertEquals(cal.get(Calendar.DAY_OF_MONTH)-cal2.get(Calendar.DAY_OF_MONTH), 0);
    }

    @Test
    public void getCurrentTime() throws Exception {
        Date begin = new DateTime(new Date()).toDate();
        String end = DateCommonUtils.getCurrentTime();

        //Assert.assertEquals(DateCommonUtils.formTime(begin),end);
    }

    @Test
    public void formDateString() throws Exception {
        Calendar c = Calendar.getInstance();
        LocalDate loclDate = new DateTime(c.getTime()).toLocalDate();
        Date begin = new DateTime(c.getTime()).toLocalDate().toDate();
        Date end = DateCommonUtils.formDateString(loclDate.getYear() + "-" + loclDate.getMonthOfYear() + "-" + loclDate.getDayOfMonth());
        Assert.assertNotEquals(DateCommonUtils.formatTime(begin), end);
    }

    @Test
    public void formDateString2() throws Exception {
        Calendar c = Calendar.getInstance();
        LocalDate loclDate = new DateTime(c.getTime()).toLocalDate();
        Date begin = new DateTime(c.getTime()).toLocalDate().toDate();
        Date end = DateCommonUtils.formDateString2(loclDate.getYear() + "-" + loclDate.getMonthOfYear() + "-" + loclDate.getDayOfMonth() + " 12:12");
        Assert.assertNotEquals(DateCommonUtils.formDateString(loclDate.getYear() + "-" + loclDate.getMonthOfYear() + "-" + loclDate.getDayOfMonth()), end);
    }

    @Test
    public void dateToStringByFormat() throws Exception {
        Calendar c = Calendar.getInstance();
        LocalDate loclDate = new DateTime(c.getTime()).toLocalDate();
        String end = DateCommonUtils.dateToStringByFormat(c.getTime(), "yyyy-MM-dd");
        Assert.assertEquals(loclDate.getYear() + "-" + loclDate.getMonthOfYear() + "-" + loclDate.getDayOfMonth(), end);
    }

    @Test
    public void formateTime() throws Exception {
        Calendar c = Calendar.getInstance();
        LocalDate loclDate = new DateTime(c.getTime()).toLocalDate();
        String end = DateCommonUtils.formatTime(c.getTime());
        Assert.assertNotEquals(loclDate.getYear() + "-" + loclDate.getMonthOfYear() + "-" + loclDate.getDayOfMonth(), end);
    }

    @Test
    public void is15HoursBefore() throws Exception {
        boolean end = DateCommonUtils.is15HoursBefore(new Date());
        //Assert.assertEquals(true,end);
    }

    @Test
    public void getAfterDay() throws Exception {
        Date end = DateCommonUtils.getAfterDay(new Date(), 3);
        Assert.assertNotEquals(DateTime.now().plus(3), end);
    }

    @Test
    public void dateFormat() throws Exception {
        Date end = DateCommonUtils.dateFormat(new Date());
        Assert.assertNotEquals(DateTime.now().toLocalDate(), end);
    }

    @Test
    public void dateToString() throws Exception {
        String end = DateCommonUtils.formatDate(new Date());
        Assert.assertEquals(DateCommonUtils.dateToStringByFormat(new Date(), "yyyy-MM-dd"), end);
    }

    @Test
    public void getBeforeDay() throws Exception {
        Calendar c = Calendar.getInstance();
        Date end = DateCommonUtils.getBeforeDay(c.getTime());
        Assert.assertEquals(new DateTime(c).plusDays(-1).toDate(), end);
    }

    @Test
    public void getCurrentDate() throws Exception {
        Calendar c = Calendar.getInstance();
        Date end = DateCommonUtils.getCurrentDate();
        Assert.assertEquals(new DateTime(c.getTime()).toLocalDate().toDate(), end);
    }

    @Test
    public void getDateFormat() throws Exception {
        Calendar c = Calendar.getInstance();
        Date end = DateCommonUtils.getCurrentDate();
        Assert.assertEquals(new DateTime(c.getTime()).toLocalDate().toDate(), end);
    }

    @Test
    public void getMaxDate() throws Exception {
        Calendar c = Calendar.getInstance();
        Date end = DateCommonUtils.getMaxDate(new Date(), c.getTime());
        //Assert.assertEquals(new DateTime(c.getTime()).toLocalDate().toDate(),end);
    }

    @Test
    public void getMinDate() throws Exception {
        Calendar c = Calendar.getInstance();
        Date end = DateCommonUtils.getMaxDate(new Date(), c.getTime());
    }

    @Test
    public void getEndDateByAddMonths() throws Exception {
        Calendar c = Calendar.getInstance();
        Date end = DateCommonUtils.getEndDateByAddMonths(DateCommonUtils.getToday(), 1, 1);
        Assert.assertEquals(DateCommonUtils.getBeforeDay(new DateTime(c.getTime()).toLocalDate().plusMonths(1).plusDays(-1).toDate()), DateCommonUtils.getBeforeDay(end));
    }

    @Test
    public void getBeginDateByAddMonths() throws Exception {
        Calendar c = Calendar.getInstance();
        Date end = DateCommonUtils.getBeginDateByAddMonths(DateCommonUtils.getToday(), 1, 1);
        Assert.assertEquals(DateCommonUtils.getBeforeDay(new DateTime(c.getTime()).toLocalDate().toDate()), DateCommonUtils.getBeforeDay(end));
    }

    @Test
    public void getRepayDateByPeriod() throws Exception {
        Calendar c = Calendar.getInstance();
        Date end = DateCommonUtils.getRepayDateByPeriod(DateCommonUtils.getToday(), 1, 1, 1L);
        int year = c.get(Calendar.YEAR);//年份
        int month = c.get(Calendar.MONTH) + 1;//月份
        String ms = month < 10 ? "0" + month : month + "";
        int day = c.getActualMaximum(Calendar.DATE);
        String days = day < 10 ? "0" + day : day + "";
        //Assert.assertEquals(DateCommonUtils.formDateString(year+"-"+ms+"-"+days),end);
    }

    @Test
    public void sqlDateFormat() throws Exception {
        Calendar c = Calendar.getInstance();
        Date end = DateCommonUtils.sqlDateFormat(c.getTime());
        Assert.assertEquals(new DateTime(c.getTime()).toLocalDate().toDate(), end);

    }

    @Test
    public void birhtDay() throws Exception {
        Calendar c = Calendar.getInstance();
        Date end = DateCommonUtils.birhtDay("431102199001105555");
        Assert.assertEquals(DateCommonUtils.formDateString("1990-01-10"), end);
    }

    @Test
    public void weeHours() throws Exception {
        Calendar c = Calendar.getInstance();
        Date end = DateCommonUtils.weeHours(c.getTime(), 0);
        Assert.assertEquals(DateCommonUtils.getToday(), end);
    }

    @Test
    public void getSeconds() throws Exception {
        Calendar c = Calendar.getInstance();
        long end = DateCommonUtils.getSeconds(c.getTime(), new Date());
        Assert.assertEquals(end, 0);

        Date d1 = DateCommonUtils.formDateString("2017-01-01");
        Date d2 = DateCommonUtils.formDateString("2017-01-02");
        Assert.assertEquals(86400, DateCommonUtils.getSeconds(d1, d2));
        Assert.assertEquals(-86400, DateCommonUtils.getSeconds(d2, d1));
    }

    @Test
    public void testGetDateTimeStamp() throws Exception {
        Date now = new Date();
        long stamp = DateCommonUtils.getDateTimeStamp(now);

        Date newDate = DateCommonUtils.stampToDate(stamp);
        Assert.assertTrue(now.equals(newDate));
    }

    @Test
    public void testConvertDateStringToStamp() {
        String testDate = DateCommonUtils.getDateFormat(new Date());
        long startDateStamp = DateCommonUtils.convertDateStringToStamp(testDate, false);
        long endDateStamp = DateCommonUtils.convertDateStringToStamp(testDate, true);

        Assert.assertEquals(1000 * (60 * 60 * 24 - 1), endDateStamp - startDateStamp );
    }

    @Test
    public void testGetYearOfDate() {
        Date d = DateCommonUtils.formDateString("2017-11-03");
        Assert.assertEquals(DateCommonUtils.getYearOfDate(d), 2017);
    }

    @Test
    public void testGetYearOfCentury() {
        Date d = DateCommonUtils.formDateString("2017-11-03");
        Assert.assertEquals(DateCommonUtils.getYearOfCentury(d), 17);
    }

    @Test
    public void testGetMonthOfDate() {
        Date d = DateCommonUtils.formDateString("2017-11-03");
        Assert.assertEquals(DateCommonUtils.getMonthOfDate(d), 11);
    }

    @Test
    public void nowPlusSeconds(){
        Date d = DateTime.now().plusSeconds(420).toDate();
        //Assert.assertEquals();
    }
}