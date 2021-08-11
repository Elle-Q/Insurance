package com.fintech.insurance.commons.utils;

import org.apache.commons.lang3.StringUtils;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.UUID;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * 用于处理文本和字符串的帮助类
 */
public class TextUtils {

    /**
     * 输入的字符是否是汉字
     *
     * @param a
     * @return
     */
    public static boolean isChinese(char a) {
        int v = (int) a;
        return (v >= 19968 && v <= 171941);
    }

    /**
     * 截取指定长度字符串
     *
     * @param str
     * @param length
     * @param chineseCharLength
     * @return
     */
    public static String subString(String str, int length, int chineseCharLength, String suffix) {
        if (StringUtils.isEmpty(str)) {
            return str;
        }

        if (str.length() < length) {
            return str;
        }

        int curLength = 0, index = 0;
        for (int i = 0; i < str.length(); i++) {
            int value = (char) str.charAt(i);
            // 汉字范围 \u4e00-\u9fa5 (中文)
            if (value >= 19968 && value <= 171941) {
                curLength += chineseCharLength;
            } else {
                curLength += 1;
            }

            if (curLength > length) {
                index = i;
                break;
            }
        }

        String subStr = str.substring(0, index);
        if (suffix != null) {
            subStr += suffix;
        }

        return subStr;
    }

    public static String subString(String str, int length, String suffix) {
        return subString(str, length, 2, suffix);
    }

    public static String subString(String str, int length) {
        return subString(str, length, 2, "...");
    }

    public static String randomUUID() {
        return StringUtils.remove(UUID.randomUUID().toString(), "-");
    }

    public static boolean emailFormat(String email) {
        final String pattern1 = "^([a-z.A-Z0-9_-])+@([a-zA-Z0-9_-])+(\\.([a-zA-Z0-9_-])+)+$";
        final Pattern pattern = Pattern.compile(pattern1);
        final Matcher mat = pattern.matcher(email);
        boolean b = mat.matches();
        return b;
    }

    public static boolean mobileFormat(String mobile) {
        Pattern pattern = Pattern.compile("^1\\d{10}$");
        Matcher matcher = pattern.matcher(mobile);
        if (matcher.matches()) {
            return true;
        }
        return false;
    }

    /**
     * @param fileName :文件名
     * @param array    :后缀名数据
     * @return
     * @Description (判断文件是否后缀是否符合)
     */
    public static boolean fileSuffixInArray(String fileName, String[] array) {
        boolean isExists = false;
        if (StringUtils.isEmpty(fileName) || array == null || array.length == 0) {
            return false;
        }
        for (int i = 0; i < array.length; i++) {
            if (StringUtils.endsWithIgnoreCase(fileName, array[i])) {
                isExists = true;
                break;
            }
        }
        return isExists;
    }

    /**
     * 转换字符集到utf8
     */
    public static String convertToUtf8(String src) {
        if (src == null || src.length() == 0) {
            return src;
        }
        byte[] srcData = src.getBytes();

        try {
            return new String(srcData, "UTF-8");
        }
        catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        return "";
    }

    /**
     * 从utf8转换字符集
     */
    public static String convertFromUtf8(String src) {
        if (src == null || src.length() == 0) {
            return src;
        }
        byte[] srcData = new byte[0];
        try {
            srcData = src.getBytes("UTF-8");
        }
        catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        return new String(srcData);
    }

    public static String urlEncode(String data) throws UnsupportedEncodingException {
        String newData = TextUtils.convertToUtf8(data);
        return URLEncoder.encode(newData, "UTF-8");
    }

    public static String join(String[] items, String split) {
        if (items.length == 0) {
            return "";
        }
        StringBuffer s = new StringBuffer();
        int i;
        for (i = 0; i < items.length - 1; i++) {
            s.append(items[i]);
            s.append(split);
        }
        s.append(items[i]);
        return s.toString();
    }


}
