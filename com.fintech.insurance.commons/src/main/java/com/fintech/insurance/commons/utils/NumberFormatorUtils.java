package com.fintech.insurance.commons.utils;

import org.apache.commons.lang3.StringUtils;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.text.DecimalFormat;
import java.text.NumberFormat;

/**
 * 数字格式器
 *
 * @author Sean Zhang
 * @since 2017-06-06
 * @version 1.0.0
 */
public class NumberFormatorUtils {

    public static BigDecimal TEN_THOUSAND = new BigDecimal(10000);
    public static BigDecimal HUNDRED = new BigDecimal(100);
    public static String NUMBER_ZERO = "0";

    public final static char[] rDigits = { '0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'A', 'B', 'C', 'D', 'E', 'F', 'G', 'H', 'J', 'K', 'L', 'M', 'N', 'P', 'Q', 'R', 'S', 'T', 'U', 'V', 'W', 'X', 'Y', 'Z', '+' }; /** * @param args * @throws InterruptedException */

    /**
     * 汉语中数字大写
     */
    private static final String[] CN_UPPER_NUMBER = { "零", "壹", "贰", "叁", "肆",
            "伍", "陆", "柒", "捌", "玖" };
    /**
     * 汉语中货币单位大写，这样的设计类似于占位符
     */
    private static final String[] CN_UPPER_MONETRAY_UNIT = { "分", "角", "元",
            "拾", "佰", "仟", "万", "拾", "佰", "仟", "亿", "拾", "佰", "仟", "兆", "拾",
            "佰", "仟" };
    /**
     * 特殊字符：整
     */
    private static final String CN_FULL = "整";
    /**
     * 特殊字符：负
     */
    private static final String CN_NEGATIVE = "负";
    /**
     * 金额的精度，默认值为2
     */
    private static final int MONEY_PRECISION = 2;
    /**
     * 特殊字符：零元整
     */
    private static final String CN_ZEOR_FULL = "零元" + CN_FULL;

    /**
     * 格式化数值为百分号表示,例如： 990000 =》 99% 在格式化时， 根据精度的不同，使用向下取值， 比如9.99%只保留以为精度时， 为9.9%
     *
     * @param value
     * @param scaleNumber
     *            百分号的精度, 以0.999为例， 为1时刑如99.9%, 为2时形如99.90%
     * @param isTailSymbol  是否显示百分比符号
     * @return
     */
    public static String longToPercentValue(long value, int scaleNumber, boolean isTailSymbol) {
        BigDecimal decimalValue = null;
        if (scaleNumber >= 0) {
            decimalValue = NumberFormatorUtils.divideTenThousand(value, scaleNumber + 2);
        } else {
            decimalValue = NumberFormatorUtils.divideTenThousand(value, 10);
        }
        String percentValue = NumberFormatorUtils.actualValueToPercent(decimalValue.doubleValue(), scaleNumber);
        return isTailSymbol ? percentValue : percentValue.substring(0, percentValue.length() - 1);
    }

    //个位十位变成零
    public static long removeTinyCent(long financialAmount) {
        return financialAmount / 100 * 100;
    }

    //除以1万保留4位小数 isTailSymbol表示是否有%号
    public static String longToPercentValueIgnoreScale(long value, boolean isTailSymbol) {
        return NumberFormatorUtils.longToPercentValue(value, -1, isTailSymbol);
    }

    //转成百分数 scaleNumber保留小数位数
    public static String actualValueToPercent(double value, int scaleNumber) {
        if (scaleNumber >= 0) {
            NumberFormat nf = NumberFormat.getPercentInstance();
            nf.setRoundingMode(RoundingMode.FLOOR);
            nf.setMinimumFractionDigits(scaleNumber);
            nf.setMaximumFractionDigits(scaleNumber);
            return nf.format(value);
        } else {
            BigDecimal valueBigDecimal = BigDecimal.valueOf(value);
            String actualValue = valueBigDecimal.stripTrailingZeros().toPlainString();
            return String.format("%s%%", actualValue);
        }
    }

    /**
     * 将字符串转换为长整型
     *
     * @param value
     * @return
     */
    public static Long stringToLongValue(String value) {
        if (value == null) {
            return 0l;
        } else {
            if (value.trim().equals("")) {
                return 0l;
            } else {
                BigDecimal decimalValue = new BigDecimal(value);
                decimalValue = decimalValue.setScale(2, BigDecimal.ROUND_DOWN);
                decimalValue = decimalValue.multiply(TEN_THOUSAND);
                return decimalValue.longValue();
            }
        }
    }

    /**
     * 将字符串转换为长整型保留所有小数位
     *
     * @param value
     * @return
     */
    public static Long stringToLongValueNotSetScale(String value) {
        if (value == null) {
            return 0l;
        } else {
            if (value.trim().equals("")) {
                return 0l;
            } else {
                BigDecimal decimalValue = new BigDecimal(value);
                decimalValue = decimalValue.multiply(TEN_THOUSAND);
                return decimalValue.longValue();
            }
        }
    }

    /**
     * 万份收益
     *
     * @param value
     * @return
     */
    public static Long stringThousandIncomeToLongValue(String value) {
        if (value == null) {
            return 0l;
        } else {
            if (value.trim().equals("")) {
                return 0l;
            } else {
                BigDecimal decimalValue = new BigDecimal(value);
                decimalValue = decimalValue.setScale(4, BigDecimal.ROUND_DOWN);
                decimalValue = decimalValue.multiply(TEN_THOUSAND);
                return decimalValue.longValue();
            }
        }
    }

    /**
     * 将长整型转换为字符串
     *
     * @param value
     * @return
     */
    public static String longTypeToStringValue(Long value) {
        if (value == null) {
            return null;
        } else {
            return longToStringValue(value.longValue());
        }
    }

    /**
     * @Description 将长整型转换为字符串不截取小数点后位数
     * @param value
     * @return
     */

    public static String longTypeToStringValueNotSetScale(Long value) {
        if (value == null) {
            return null;
        } else {
            return longToStringValueNotSetScale(value.longValue());
        }
    }

    private static String longToStringValueNotSetScale(long value) {
        BigDecimal decimalValue = new BigDecimal(value);
        decimalValue = decimalValue.divide(TEN_THOUSAND);
        return decimalValue.toString();
    }

    /**
     * 将长整型转换为字符串
     *
     * @param value
     * @return
     */
    public static String longToStringValue(long value) {
        return longToStringValue(value, 2);
    }

    //除以1万转成String，scale保留的小数位
    public static String longToStringValue(long value, int scale) {
        BigDecimal decimalValue = new BigDecimal(value);
        decimalValue = decimalValue.divide(TEN_THOUSAND);
        decimalValue = decimalValue.setScale(scale, BigDecimal.ROUND_DOWN);
        return decimalValue.toString();
    }

    //除以1万转成BigDecimal，scale表示表示小数点后位数
    public static BigDecimal divideTenThousand(Long value, int scale) {
        BigDecimal decimalValue = new BigDecimal(value);
        decimalValue = decimalValue.divide(TEN_THOUSAND);
        return decimalValue.setScale(scale, BigDecimal.ROUND_DOWN).stripTrailingZeros();
    }

    /**
     * 将长整型转换为字符串并去掉末尾的0
     *
     * @param value
     * @return
     */
    public static String longToStringByStripZeros(Long value) {
        if (value == null) {
            return NUMBER_ZERO;
        } else if (value.equals(0L)) {
            return NUMBER_ZERO;
        } else {
            return NumberFormatorUtils.divideTenThousand(value, 2).toPlainString();
        }
    }

    /**
     * 内部金额数据转化，用于输出到前端
     *
     * @param value
     * @return
     */
    public static Double convertFinancialAmount(Long value) {
        if (null == value) {
            return 0d;
        } else {
            return new BigDecimal(value).divide(TEN_THOUSAND).doubleValue();
        }
    }

    /**
     * 乘以10000转成数据库金额
     *
     * @param value
     * @return
     */
    public static Long convertDoubleFinancialAmount(Double value) {
        if (null == value) {
            return 0L;
        } else {
            return new BigDecimal(value).multiply(TEN_THOUSAND).longValue();
        }
    }


    /**
     * 将长整型转换为字符串并去掉末尾的0
     *
     * @param value
     * @return
     */
    public static String longToStringThousandsIncomeByStripZeros(Long value) {
        if (value == null) {
            return NUMBER_ZERO;
        } else if (value.equals(0L)) {
            return NUMBER_ZERO;
        } else {
            return NumberFormatorUtils.divideTenThousand(value, 4).toPlainString();
        }
    }

    /**
     * 数据库金额long类型转让int类型去掉最后两个不四舍五入
     *
     * @param value
     * @return
     */
    public static int longToIntegerByRemoveLasterTwo(Long value) {
        if (value == null) {
            return 0;
        } else if (value.equals(0L)) {
            return 0;
        } else {
            return Integer.parseInt(String.valueOf(value / 100));
        }
    }

    /**
     * 数据库金额long类型转让String类型去掉最后两个不四舍五入
     *
     * @param value
     * @return
     */
    public static String longToStringByRemoveLasterTwo(Long value) {
        if (value == null) {
            return "0";
        } else if (value.equals(0L)) {
            return "0";
        } else {
            return new BigDecimal(value).divide(NumberFormatorUtils.TEN_THOUSAND).setScale(2, BigDecimal.ROUND_CEILING)
                    .toPlainString();
        }
    }

    /**
     * 统一数字转化乘以100保存到数据库
     *
     * @param value
     * @return
     */
    public static BigDecimal convertFinanceNumberToSave(Number value) {
        if (null == value) {
            return new BigDecimal(0);
        } else {
            return new BigDecimal(value.doubleValue()).multiply(HUNDRED);
        }
    }

    //number 除以100转bigdecimal
    public static BigDecimal convertFinanceNumberToShow(Number value) {
        if (null == value) {
            return new BigDecimal(0d);
        } else {
            return new BigDecimal(value.doubleValue()).divide(HUNDRED);
        }
    }

    //double乘以100变成int
    public static Integer convertDoubleToInteger(Number value) {
        if (null == value) {
            return 0;
        } else {
            BigDecimal bigDecimal = new BigDecimal(value.doubleValue()).setScale(2, BigDecimal.ROUND_DOWN);
            return bigDecimal.multiply(HUNDRED).intValue();
        }
    }

    //double类型数据保留两位小数
    public static String convertFinanceNumberToShowString(Double value) {
        if(value == null){
            return "0.00";
        }
        BigDecimal b = new BigDecimal(value).divide(HUNDRED);
        BigDecimal bigDecimal = b.setScale(2,   BigDecimal.ROUND_HALF_UP);
        return bigDecimal.toString();
    }

    /**
     * 把输入的金额转换为汉语中人民币的大写
     *
     * @param numberOfMoney
     *            输入的金额
     * @return 对应的汉语大写
     */
    public static String number2CNMontrayUnit(BigDecimal numberOfMoney) {
        StringBuffer sb = new StringBuffer();
        // -1, 0, or 1 as the value of this BigDecimal is negative, zero, or
        // positive.
        int signum = numberOfMoney.signum();
        // 零元整的情况
        if (signum == 0) {
            return CN_ZEOR_FULL;
        }
        //这里会进行金额的四舍五入
        long number = numberOfMoney.movePointRight(MONEY_PRECISION)
                .setScale(0, 4).abs().longValue();
        // 得到小数点后两位值
        long scale = number % 100;
        int numUnit = 0;
        int numIndex = 0;
        boolean getZero = false;
        // 判断最后两位数，一共有四中情况：00 = 0, 01 = 1, 10, 11
        if (!(scale > 0)) {
            numIndex = 2;
            number = number / 100;
            getZero = true;
        }
        if ((scale > 0) && (!(scale % 10 > 0))) {
            numIndex = 1;
            number = number / 10;
            getZero = true;
        }
        int zeroSize = 0;
        while (true) {
            if (number <= 0) {
                break;
            }
            // 每次获取到最后一个数
            numUnit = (int) (number % 10);
            if (numUnit > 0) {
                if ((numIndex == 9) && (zeroSize >= 3)) {
                    sb.insert(0, CN_UPPER_MONETRAY_UNIT[6]);
                }
                if ((numIndex == 13) && (zeroSize >= 3)) {
                    sb.insert(0, CN_UPPER_MONETRAY_UNIT[10]);
                }
                sb.insert(0, CN_UPPER_MONETRAY_UNIT[numIndex]);
                sb.insert(0, CN_UPPER_NUMBER[numUnit]);
                getZero = false;
                zeroSize = 0;
            } else {
                ++zeroSize;
                if (!(getZero)) {
                    sb.insert(0, CN_UPPER_NUMBER[numUnit]);
                }
                if (numIndex == 2) {
                    if (number > 0) {
                        sb.insert(0, CN_UPPER_MONETRAY_UNIT[numIndex]);
                    }
                } else if (((numIndex - 2) % 4 == 0) && (number % 1000 > 0)) {
                    sb.insert(0, CN_UPPER_MONETRAY_UNIT[numIndex]);
                }
                getZero = true;
            }
            // 让number每次都去掉最后一个数
            number = number / 10;
            ++numIndex;
        }
        // 如果signum == -1，则说明输入的数字为负数，就在最前面追加特殊字符：负
        if (signum == -1) {
            sb.insert(0, CN_NEGATIVE);
        }
        // 输入的数字小数点后两位为"00"的情况，则要在最后追加特殊字符：整
        if (!(scale > 0)) {
            sb.append(CN_FULL);
        }
        return sb.toString();
    }


    //转成34进制
    public static String Parse34Encode(long value)
    {
        int digitIndex = 0;
        long longPositive = Math.abs(value);
        int radix = 34;//34进制
        char[] outDigits = new char[35];
        for (digitIndex = 0; digitIndex <= 34; digitIndex++)
        {
            if (longPositive == 0) {
                break;
            }
            Long s=longPositive % radix;
            outDigits[outDigits.length - digitIndex - 1] = rDigits[s.intValue()];
            longPositive /= radix;
        }
        return new String(outDigits, outDigits.length - digitIndex, digitIndex);
    }


    public static void main(String[] args) {
        String sizeStr = StringUtils.leftPad(NumberFormatorUtils.Parse34Encode(35), 2, "0");
//        System.out.println(Double.parseDouble(new BigDecimal(1234567)
//                .divide(new BigDecimal(10000), 2, RoundingMode.FLOOR).toString()));
//        System.out.println(NumberFormatorUtils.convertFinancialAmount(1236564L));
//        System.out.println(NumberFormatorUtils.convertDoubleFinancialAmount(123.75645D));
  //      System.out.println(NumberFormatorUtils.convertNumberToSave(123.75645D));
 //       System.out.println(NumberFormatorUtils.convertNumberToShow(123.75645D));

        // System.out.println(NumberFormatorUtils.bigDecimalToLong(new BigDecimal(10.02)));

//        System.out.println(NumberFormatorUtils.longToPercentValue(20100, 2, true));
//        System.out.println(NumberFormatorUtils.longToPercentValue(20100, 2, false));
//        System.out.println(NumberFormatorUtils.longToPercentValue(20000, 0, true));
//        System.out.println(NumberFormatorUtils.longToPercentValue(20000, 0, false));
//        System.out.println(NumberFormatorUtils.longToPercentValue(9902, 2, true));
//        System.out.println(NumberFormatorUtils.longToPercentValue(9902, -1, true));
//        System.out.println(NumberFormatorUtils.longToPercentValueIgnoreScale(18630, true));
//        System.out.println(NumberFormatorUtils.longToPercentValueIgnoreScale(9920, true));
//        System.out.println(NumberFormatorUtils.longToPercentValueIgnoreScale(9920, true));
//        System.out.println(NumberFormatorUtils.longToPercentValueIgnoreScale(10, true));
//        System.out.println(NumberFormatorUtils.longToPercentValueIgnoreScale(10, false));
//
//        System.out.println(new BigDecimal(5000100L).divide(NumberFormatorUtils.TEN_THOUSAND)
//                .setScale(2, BigDecimal.ROUND_CEILING).toPlainString());
//
//        System.out.println(NumberFormatorUtils.longToPercentValue(9995, 1, true));
//        System.out.println(NumberFormatorUtils.actualValueToPercent(0.0, 0));
//        System.out.println(NumberFormatorUtils.actualValueToPercent(1, 0));
//
//        System.out.println(NumberFormatorUtils.longToStringValue(250000000 / 10000, 0));
//
//        System.out.println(1000000000000000000L);
//        System.out.println(NumberFormatorUtils.longToStringValue(0L));
//
//        System.out.println(new BigDecimal(89770000).divide(NumberFormatorUtils.TEN_THOUSAND).stripTrailingZeros().toPlainString());

        double money = 2020004.01;
        BigDecimal numberOfMoney = new BigDecimal(money);
        String s = NumberFormatorUtils.number2CNMontrayUnit(numberOfMoney);
        System.out.println("你输入的金额为：【"+ money +"】   #--# [" +s.toString()+"]");
    }

}
