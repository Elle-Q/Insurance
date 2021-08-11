package com.fintech.insurance.micro.biz.controller;



import org.junit.After;
import org.junit.Before;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Random;

public abstract class BaseControllerTests {

    private static final Logger logger = LoggerFactory.getLogger(BaseControllerTests.class);

    final static char[] digits = { '0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'A', 'B', 'C', 'D', 'E', 'F', 'G', 'H', 'J', 'K', 'L', 'M', 'N', 'P', 'Q', 'R', 'S', 'T', 'U', 'V', 'W', 'X', 'Y', 'Z'}; /** * @param args * @throws InterruptedException */
    final static char[] rDigits = { '0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'A', 'B', 'C', 'D', 'E', 'F', 'G', 'H', 'J', 'K', 'L', 'M', 'N', 'P', 'Q', 'R', 'S', 'T', 'U', 'V', 'W', 'X', 'Y', 'Z'}; /** * @param args * @throws InterruptedException */

    public static void main(String[] args) throws InterruptedException {
        long timeMillins =26;// System.currentTimeMillis(); System.out.println("初始值:" + timeMillins);
        Object b=Long.toString(timeMillins, 36);
        String tmp = Parse64Encode(timeMillins);
        System.out.println("加密后:" + tmp);
        //System.out.println("解密后:" + UnCompressNumber(tmp));

    } /** * 把10进制的数字转换成64进制 * * @param number * @param shift * @return */
        private static String CompressNumber(long number) {
            char[] buf = new char[34]; int charPos = 34; int radix = 1 << 6; long mask = radix - 1;
            do { buf[--charPos] = digits[(int) (number & mask)]; number >>>= 6;
            } while (number != 0);
            return new String(buf, charPos, (34 - charPos)); } /** * 把64进制的字符串转换成10进制 * * @param decompStr * @return */
            private static long UnCompressNumber(String decompStr) {
                long result = 0;
                for (int i = decompStr.length() - 1; i >= 0; i--)
                {
                    for (int j = 0; j < digits.length; j++) {
                    if (decompStr.charAt(i) == digits[j])
                    { result += ((long) j) << 6 * (decompStr.length() - 1 - i); } } }
                    return result;
            }
   // private static char[] rDigitss = { '0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'a', 'b', 'c', 'd', 'e', 'f', 'g', 'h', 'i', 'j', 'k', 'l', 'm', 'n', 'o', 'p', 'q', 'r', 's', 't', 'u', 'v', 'w', 'x', 'y', 'z', 'A', 'B', 'C', 'D', 'E', 'F', 'G', 'H', 'I', 'J', 'K', 'L', 'M', 'N', 'O', 'P', 'Q', 'R', 'S', 'T', 'U', 'V', 'W', 'X', 'Y', 'Z', '+', '/' };

    final Random random = new Random();
    public static String Parse64Encode(long value)
    {
        int digitIndex = 0;
        long longPositive = Math.abs(value);
        int radix = 34;//64进制
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
    /// <summary>
    /// 64进制转long
    /// </summary>
    /// <param name="value"></param>
    /// <returns></returns>
    private static long Parse64Decode(char[] value)
    {
        int fromBase = 64;
        String sDigits = new String(rDigits, 0, fromBase);
        long result = 0;
        for (int i = 0; i < value.length; i++)
        {
            if (!sDigits.contains(value[i]+"")){
                //throw new ArgumentException(String.Format("The argument \"{0}\" is not in {1} system", value[i], fromBase));
            }
            else
            {

                    int index = 0;
                    for (int xx = 0; xx < rDigits.length; xx++)
                    {
                        if (rDigits[xx] == value[value.length - i - 1])
                        {
                            index = xx;
                        }
                    }
                    result += (long)Math.pow(fromBase, i) * index;//   2

            }
        }
        return result;
    }
    /**
     * 准备测试用例所需要的资源，包括定义mock bean的行为等。
     */
    @Before
    public void setup() throws Exception {
        logger.info("BaseController setup method invoked");
    }

    /**
     * 释放测试用例中所使用到的资源
     */
    @After
    public void teardown() throws Exception {
        logger.info("BaseController teardown method invoked");
    }

        //String json = "[{\"areaCode\":\"3051\",\"areaDesc\":null,\"businessLicence\":null,\"channelCode\":\"000\",\"channelName\":\"2222\",\"companyId\":2,\"companyName\":\"三只松鼠\",\"createAt\":1511755405000,\"id\":39,\"isLocked\":0,\"licencePicture\":null,\"mobile\":null},{\"areaCode\":\"3051\",\"areaDesc\":null,\"businessLicence\":\"HK4444\",\"channelCode\":\"038\",\"channelName\":\"渠道1\",\"companyId\":2,\"companyName\":\"三只松鼠\",\"createAt\":1511754811000,\"id\":38,\"isLocked\":0,\"licencePicture\":null,\"mobile\":null},{\"areaCode\":\"3602\",\"areaDesc\":null,\"businessLicence\":\"987765654534523\",\"channelCode\":\"037\",\"channelName\":\"的高郭德纲的风格\",\"companyId\":3,\"companyName\":\"两只王八\",\"createAt\":1511505727000,\"id\":37,\"isLocked\":0,\"licencePicture\":null,\"mobile\":null},{\"areaCode\":\"3051\",\"areaDesc\":null,\"businessLicence\":\"HK3333\",\"channelCode\":\"036\",\"channelName\":\"这是一个渠道名字\",\"companyId\":2,\"companyName\":\"三只松鼠\",\"createAt\":1511483786000,\"id\":36,\"isLocked\":1,\"licencePicture\":null,\"mobile\":null},{\"areaCode\":\"3303\",\"areaDesc\":null,\"businessLicence\":\"5465877697898\",\"channelCode\":\"564564645\",\"channelName\":\"梵蒂冈和符合法规\",\"companyId\":1,\"companyName\":\"四条腿\",\"createAt\":1511434322000,\"id\":35,\"isLocked\":0,\"licencePicture\":null,\"mobile\":null},{\"areaCode\":\"3602\",\"areaDesc\":null,\"businessLicence\":\"76577879798797\",\"channelCode\":\"18666552211\",\"channelName\":\"大大大大大大大\",\"companyId\":3,\"companyName\":\"两只王八\",\"createAt\":1511433519000,\"id\":34,\"isLocked\":0,\"licencePicture\":null,\"mobile\":null},{\"areaCode\":\"3601\",\"areaDesc\":null,\"businessLicence\":\"89898989898\",\"channelCode\":\"66666666666\",\"channelName\":\"哈哈哈哈或或或\",\"companyId\":1,\"companyName\":\"四条腿\",\"createAt\":1511433308000,\"id\":33,\"isLocked\":0,\"licencePicture\":null,\"mobile\":null},{\"areaCode\":\"3302\",\"areaDesc\":null,\"businessLicence\":\"4545454545\",\"channelCode\":\"88888888888\",\"channelName\":\"ffffff\",\"companyId\":1,\"companyName\":\"四条腿\",\"createAt\":1511433250000,\"id\":32,\"isLocked\":1,\"licencePicture\":null,\"mobile\":null},{\"areaCode\":\"3502\",\"areaDesc\":null,\"businessLicence\":\"55555555555\",\"channelCode\":\"666666\",\"channelName\":\"吞吞吐吐拖拖拖拖拖拖\",\"companyId\":1,\"companyName\":\"四条腿\",\"createAt\":1511433157000,\"id\":31,\"isLocked\":0,\"licencePicture\":null,\"mobile\":null},{\"areaCode\":\"51\",\"areaDesc\":null,\"businessLicence\":\"HK1111\",\"channelCode\":\"QUDAOBIANHAO2\",\"channelName\":\"古巴美食餐厅\",\"companyId\":2,\"companyName\":\"三只松鼠\",\"createAt\":1511339894000,\"id\":29,\"isLocked\":1,\"licencePicture\":null,\"mobile\":null},{\"areaCode\":\"3602\",\"areaDesc\":null,\"businessLicence\":\"bandanmuyinyezhizhao\",\"channelCode\":\"BADANMU\",\"channelName\":\"巴旦木公司\",\"companyId\":1,\"companyName\":\"四条腿\",\"createAt\":1510738335000,\"id\":28,\"isLocked\":1,\"licencePicture\":null,\"mobile\":null},{\"areaCode\":\"51\",\"areaDesc\":null,\"businessLicence\":\"YINGYEZHIZHAOHAO\",\"channelCode\":\"QUDAOBIANHAO\",\"channelName\":\"良品铺子总公司\",\"companyId\":2,\"companyName\":\"三只松鼠\",\"createAt\":1510718463000,\"id\":27,\"isLocked\":0,\"licencePicture\":null,\"mobile\":null},{\"areaCode\":\"4311\",\"areaDesc\":null,\"businessLicence\":\"123456789101112\",\"channelCode\":\"812345\",\"channelName\":\"812345\",\"companyId\":1,\"companyName\":\"四条腿\",\"createAt\":1510634347000,\"id\":26,\"isLocked\":0,\"licencePicture\":null,\"mobile\":null},{\"areaCode\":\"4311\",\"areaDesc\":null,\"businessLicence\":\"1234567891011\",\"channelCode\":\"c12345\",\"channelName\":\"c12345\",\"companyId\":1,\"companyName\":\"四条腿\",\"createAt\":1510629056000,\"id\":23,\"isLocked\":0,\"licencePicture\":null,\"mobile\":null},{\"areaCode\":\"4311\",\"areaDesc\":null,\"businessLicence\":\"12345678910\",\"channelCode\":\"b12345\",\"channelName\":\"b12345\",\"companyId\":1,\"companyName\":\"四条腿\",\"createAt\":1510629007000,\"id\":22,\"isLocked\":0,\"licencePicture\":null,\"mobile\":null},{\"areaCode\":\"4311\",\"areaDesc\":null,\"businessLicence\":\"123456789\",\"channelCode\":\"a12345\",\"channelName\":\"a12345\",\"companyId\":1,\"companyName\":\"四条腿\",\"createAt\":1510628765000,\"id\":21,\"isLocked\":0,\"licencePicture\":null,\"mobile\":null},{\"areaCode\":\"4ss4ds4\",\"areaDesc\":null,\"businessLicence\":\"12s3d44ss4ss56\",\"channelCode\":\"1234ss4d4s45sss\",\"channelName\":\"123ss44ds4sss5\",\"companyId\":1,\"companyName\":\"四条腿\",\"createAt\":1510628412000,\"id\":18,\"isLocked\":0,\"licencePicture\":null,\"mobile\":null},{\"areaCode\":\"44ds4\",\"areaDesc\":null,\"businessLicence\":\"12s3d444ss56\",\"channelCode\":\"12344d4s45sss\",\"channelName\":\"12344ds4sss5\",\"companyId\":1,\"companyName\":\"四条腿\",\"createAt\":1510628271000,\"id\":17,\"isLocked\":0,\"licencePicture\":null,\"mobile\":null},{\"areaCode\":\"44s4\",\"areaDesc\":null,\"businessLicence\":\"12s3444ss56\",\"channelCode\":\"123444s45sss\",\"channelName\":\"12344s4sss5\",\"companyId\":1,\"companyName\":\"四条腿\",\"createAt\":1510628201000,\"id\":16,\"isLocked\":0,\"licencePicture\":null,\"mobile\":null},{\"areaCode\":\"444\",\"areaDesc\":null,\"businessLicence\":\"123444ss56\",\"channelCode\":\"12344445sss\",\"channelName\":\"123444sss5\",\"companyId\":1,\"companyName\":\"四条腿\",\"createAt\":1510628142000,\"id\":15,\"isLocked\":0,\"licencePicture\":null,\"mobile\":null},{\"areaCode\":\"43s11\",\"areaDesc\":null,\"businessLicence\":\"1234ss56\",\"channelCode\":\"12345sss\",\"channelName\":\"12345\",\"companyId\":1,\"companyName\":\"四条腿\",\"createAt\":1510628046000,\"id\":14,\"isLocked\":0,\"licencePicture\":null,\"mobile\":null},{\"areaCode\":\"4311\",\"areaDesc\":null,\"businessLicence\":\"12345678\",\"channelCode\":\"12345\",\"channelName\":\"12345\",\"companyId\":1,\"companyName\":\"四条腿\",\"createAt\":1510627743000,\"id\":12,\"isLocked\":0,\"licencePicture\":null,\"mobile\":null}]";
        //JSON.parse(json);

}
