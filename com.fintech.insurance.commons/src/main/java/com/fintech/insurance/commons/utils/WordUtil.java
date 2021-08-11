package com.fintech.insurance.commons.utils;

import com.fintech.insurance.commons.constants.BasicConstants;
import freemarker.template.Configuration;
import freemarker.template.Template;
import freemarker.template.TemplateException;

import java.io.*;
import java.util.Map;

/**
 * @Description: word 文档生成工具类
 * @Author: Yong Li
 * @Date: 2017/12/21 12:00
 */
public class WordUtil {

    //创建配置实例
    private static final Configuration freemarkerConfiguration = new Configuration();

    static  {
        //设置编码
        freemarkerConfiguration.setDefaultEncoding(BasicConstants.CHARSET_UTF8);
    }

    /**
     * 根据freemarker的模板文件生成word文件， 模板文件的搜索可以通过指定 <code>classForTemplateLoading</code> 来
     * 从相应的资源目录（resources）里面查找，如果某文件位于resources/foo/bar.txt， 则指定文件名的时候请设定
     * <code>freemarkerTemplateFileName</code> 为 "foo/bar.txt"
     *
     * 注意：生成的文件最好后缀名为.doc, 如果为.docx打开，可能打开不了，会提示文件已损坏
     * @param dataMap freemarker中变量替换列表
     * @param freemarkerTemplateFileName 模板文件名， 包括自 resources开始的目录以及文件后缀名
     * @param outputFile 生成的word文件路径
     * @param classForTemplateLoading 从指定class里面的jar包开始搜索resources目录.
     */
    public static void createWordFile(Map<String, Object> dataMap, String freemarkerTemplateFileName, File outputFile,
                                    Class<?> classForTemplateLoading) throws IOException, TemplateException {
        byte[] out = WordUtil.createWordData(dataMap, freemarkerTemplateFileName, classForTemplateLoading);

        if (out.length == 0) {
            throw new IllegalStateException("The word file is empty");
        }
        FileOutputStream fout = null;
        try {
            fout = new FileOutputStream(outputFile);
            fout.write(out);
        } finally {
            if (null != fout) {
                fout.close();
            }
        }
    }

    public static byte[] createWordData(Map dataMap, String freemarkerTemplateFileName, Class<?> classForTemplateLoading)  {
        //ftl模板文件统一放至 resources 目录下面
        freemarkerConfiguration.setClassForTemplateLoading(classForTemplateLoading,"/");

        //获取模板
        Template template = null;
        BufferedWriter out = null;
        try {
            template = freemarkerConfiguration.getTemplate(freemarkerTemplateFileName);

            ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream(1024);

            //将模板和数据模型合并生成文件
            out = new BufferedWriter(new OutputStreamWriter(byteArrayOutputStream, BasicConstants.CHARSET_UTF8));

            //生成文件
            template.process(dataMap, out);
            //关闭流
            out.flush();

            return byteArrayOutputStream.toByteArray();
        } catch (IOException e) {
            throw new IllegalStateException("File IO exception:" + e.getMessage(), e);
        } catch (TemplateException e) {
            throw new IllegalStateException("can not find template with name: " + freemarkerTemplateFileName, e);
        } finally {
            if (null != out) {
                try {
                    out.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }


}
