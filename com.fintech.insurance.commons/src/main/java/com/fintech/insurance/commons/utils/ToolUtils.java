package com.fintech.insurance.commons.utils;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.serializer.SerializerFeature;
import com.fintech.insurance.commons.enums.GenderType;
import com.fintech.insurance.commons.exceptions.FInsuranceIOException;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.IOUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.*;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * 方便的计算工具
 */
public class ToolUtils {

    private static final Logger LOG = LoggerFactory.getLogger(ToolUtils.class);

    public static final String TEMP_DIR = "insurance";


    /**
     * 根据身份证的号码算出当前身份证持有者的性别和年龄 18位身份证
     *
     * @return
     * @throws Exception
     */
    public static Map<String, Object> getCarInfo(String CardCode) {
        Map<String, Object> map = new HashMap<String, Object>();
        String year = CardCode.substring(6).substring(0, 4);// 得到年份
        String yue = CardCode.substring(10).substring(0, 2);// 得到月份
        String sex;
        if (Integer.parseInt(CardCode.substring(16).substring(0, 1)) % 2 == 0) {// 判断性别
            sex = GenderType.FEMALE.getCode();
        } else {
            sex = GenderType.MALE.getCode();
        }
        Date date = new Date();// 得到当前的系统时间
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
        String fyear = format.format(date).substring(0, 4);// 当前年份
        String fyue = format.format(date).substring(5, 7);// 月份
        int age = 0;
        if (Integer.parseInt(yue) <= Integer.parseInt(fyue)) { // 当前月份大于用户出身的月份表示已过生
            age = Integer.parseInt(fyear) - Integer.parseInt(year) + 1;
        } else {// 当前用户还没过生
            age = Integer.parseInt(fyear) - Integer.parseInt(year);
        }
        map.put("sex", sex);
        map.put("age", age);
        return map;
    }

    /**
     * 对fastjson的序列化方法增加features输出
     * @param obj 待序列化的对象
     * @return
     */
    public static String toJsonString(Object obj) {
        String jsonStr = JSON.toJSONString(obj, SerializerFeature.WriteEnumUsingToString, SerializerFeature.WriteNullStringAsEmpty,
                SerializerFeature.WriteNonStringValueAsString);
        return jsonStr;
    }

    public static byte[] downloadFile(String fileUrl) {
        URL url = null;
        InputStream inStream = null;
        ByteArrayOutputStream output = null;
        try {
            url = new URL(fileUrl);

            URLConnection conn = url.openConnection();
            inStream = conn.getInputStream();

            output = new ByteArrayOutputStream();
            byte[] buf = new byte[1024];
            int numBytesRead = 0;
            while ((numBytesRead = inStream.read(buf)) != -1) {
                output.write(buf, 0, numBytesRead);
            }
            return output.toByteArray();
        } catch (MalformedURLException e) {
            LOG.error("Failed to download file:" + fileUrl + " for: " + e.getMessage(), e);
            throw new FInsuranceIOException(fileUrl);
        } catch (FileNotFoundException e) {
            LOG.error("Failed to download file:" + fileUrl + " for: " + e.getMessage(), e);
            throw new FInsuranceIOException(e.getMessage() + ": " + fileUrl);
        } catch (IOException e) {
            LOG.error("Failed to download file:" + fileUrl + " for: " + e.getMessage(), e);
            throw new FInsuranceIOException(e.getMessage() + ": " + fileUrl);
        } finally {
            if (null != inStream) {
                try {
                    inStream.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }

            if (null != output) {
                try {
                    output.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }


    public static File downloadFileToTempUrl(String fileUrl) {
        URL url = null;
        InputStream inStream = null;
        BufferedOutputStream output = null;
        try {
            url = new URL(fileUrl);
            URLConnection conn = url.openConnection();
            inStream = conn.getInputStream();

            String rootPath = System.getProperty("java.io.tmpdir");
            String fileName = String.valueOf(Calendar.getInstance().getTime().getTime());
            // 创建临时文件
            File tempFile = FileUtils.getFile(rootPath, TEMP_DIR, fileName);
            FileOutputStream fos = FileUtils.openOutputStream(tempFile);
            output = new BufferedOutputStream(fos, 1024);

            byte[] buf = new byte[1024];
            int numBytesRead = 0;
            while ((numBytesRead = inStream.read(buf)) != -1) {
                output.write(buf, 0, numBytesRead);
            }
            output.flush();
            return tempFile;
        } catch (MalformedURLException e) {
            throw new FInsuranceIOException(fileUrl);
        } catch (FileNotFoundException e) {
            throw new FInsuranceIOException(e.getMessage() + ": " + fileUrl);
        } catch (IOException e) {
            throw new FInsuranceIOException(e.getMessage() + ": " + fileUrl);
        } finally {
            IOUtils.closeQuietly(output);
            IOUtils.closeQuietly(inStream);
        }
    }


    public static void main(String[] args) throws MalformedURLException {
        // URL url = new URL("c:\\ab");
        //byte[] data = ToolUtils.downloadFile("http://ozhoilwen.bkt.clouddn.com/pdf_QN20180105161818001C?e=1515142098&token=49-Ruxyz4EdBenYuZ91u6cPjPpHGfvVwN6t2HnPP:RciRUk4Z4d-qVugZqw6dK68PSGM=");
        //System.out.println(data.length);
        System.out.println(System.getProperty("java.io.tmpdir"));
        File file = ToolUtils.downloadFileToTempUrl("http://ozhoilwen.bkt.clouddn.com/pdf_QN20180105161818001C?e=1515142098&token=49-Ruxyz4EdBenYuZ91u6cPjPpHGfvVwN6t2HnPP:RciRUk4Z4d-qVugZqw6dK68PSGM=");
        System.out.println(file.getAbsolutePath());
        File file2 = new File("E:\\Insurance\\temp\\abc.txt");
        System.out.println(file2);
        System.out.println(file2.exists());

    }


}
