
package com.fintech.insurance.commons.utils;

import org.junit.Assert;
import org.junit.Test;

import java.math.BigDecimal;

public class NumberFormatorUtilsTest {
    @Test
    public void longToPercentValue() throws Exception {
        long v=123456L;
        String s=NumberFormatorUtils.longToPercentValue(v,2,false);
        Assert.assertNotEquals(NumberFormatorUtils.longToStringValue(v,0),s);
    }

    @Test
    public void removeTinyCent() throws Exception {
        long v=123456L;
        long s=NumberFormatorUtils.removeTinyCent(v);
        Assert.assertEquals(v/100*100,s);
    }

    @Test
    public void longToPercentValueIgnoreScale() throws Exception {
        long v=123456;
        String s=NumberFormatorUtils.longToPercentValueIgnoreScale(v,false);
        Assert.assertNotEquals(NumberFormatorUtils.longToStringValue(Long.parseLong(v+""),2),s);
    }

    @Test
    public void actualValueToPercent() throws Exception {
        double v=123456;
        String s=NumberFormatorUtils.actualValueToPercent(v,0);
        Assert.assertNotEquals("12.34",s);
    }

    @Test
    public void stringToLongValue() throws Exception {
        String v="12.34";
        long s=NumberFormatorUtils.stringToLongValue(v);
        Assert.assertEquals(123400,s);
    }

    @Test
    public void stringToLongValueNotSetScale() throws Exception {
        String v="12.34";
        long s=NumberFormatorUtils.stringToLongValueNotSetScale(v);
        Assert.assertEquals(123400,s);
    }

    @Test
    public void stringThousandIncomeToLongValue() throws Exception {
        String v="12.34";
        long s=NumberFormatorUtils.stringThousandIncomeToLongValue(v);
        Assert.assertEquals(123400,s);
    }

    @Test
    public void longTypeToStringValue() throws Exception {
        long v=123456;
        String s=NumberFormatorUtils.longTypeToStringValue(v);
        Assert.assertEquals("12.34",s);
    }

    @Test
    public void longTypeToStringValueNotSetScale() throws Exception {
        long v=123456;
        String s=NumberFormatorUtils.longTypeToStringValueNotSetScale(v);
        Assert.assertEquals("12.3456",s);
    }

    @Test
    public void longToStringValue() throws Exception {
        long v=123456;
        String s=NumberFormatorUtils.longToStringValue(v);
        Assert.assertEquals("12.34",s);
    }

    @Test
    public void longToStringValue1() throws Exception {
        long v=123456;
        String s=NumberFormatorUtils.longToStringValue(v,3);
        Assert.assertEquals("12.345",s);
    }

    @Test
    public void divideTenThousand() throws Exception {
        long v=123456;
        BigDecimal s=NumberFormatorUtils.divideTenThousand(v,2);
        Assert.assertEquals(new BigDecimal("12.34"),s);
    }

    @Test
    public void longToStringByStripZeros() throws Exception {
        long v=123456;
        String s=NumberFormatorUtils.longToStringByStripZeros(v);
        Assert.assertEquals("12.34",s);
    }

    @Test
    public void longToStringThousandsIncomeByStripZeros() throws Exception {
        long v=123456;
        String s=NumberFormatorUtils.longToStringThousandsIncomeByStripZeros(v);
        Assert.assertEquals("12.3456",s);
    }

    @Test
    public void longToIntegerByRemoveLasterTwo() throws Exception {
        long v=123456;
        int s=NumberFormatorUtils.longToIntegerByRemoveLasterTwo(v);
        Assert.assertEquals(1234,s);
    }

    @Test
    public void longToStringByRemoveLasterTwo() throws Exception {
        long v=123400;
        String s=NumberFormatorUtils.longToStringByRemoveLasterTwo(v);
        Assert.assertEquals("12.34",s);
    }

    @Test
    public void bigDecimalToLong() throws Exception {
        BigDecimal v=new BigDecimal("12.3400");
        //long s=NumberFormatorUtils.bigDecimalToLong(v);
        //Assert.assertEquals(123400,s);
    }

    @Test
    public void main() throws Exception {
    }
}