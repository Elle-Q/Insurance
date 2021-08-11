package com.fintech.insurance.micro.thirdparty.service.bestsign;

import com.fintech.insurance.commons.utils.WordUtil;
import com.fintech.insurance.micro.dto.system.UserVO;
import freemarker.template.Configuration;
import freemarker.template.Template;
import freemarker.template.TemplateException;

import java.io.*;
import java.util.HashMap;
import java.util.Map;

/**
 * @Description: (some words)
 * @Author: Yong Li
 * @Date: 2017/11/25 19:19
 */
public class TemplateWriteTest {

    public static void main(String[] args) throws IOException, TemplateException {
        /*//创建配置实例
        Configuration configuration = new Configuration();

        //设置编码
        configuration.setDefaultEncoding("UTF-8");

        //ftl模板文件统一放至 template 包下面
        configuration.setDirectoryForTemplateLoading(new File("F:\\temp"));

        File outFile = new File("out.docx");
        System.out.println("OUT===" + outFile.getAbsolutePath());
        Template template = configuration.getTemplate("template2.ftl","UTF-8");

        Map<String, Object> map = new HashMap<String, Object>();
        map.put("user", "hello");

        Writer out = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(outFile),"UTF-8"));
        //生成文件
        template.process(map, out);
        //关闭流
        out.flush();
        out.close();*/

        Map<String, Object> map = new HashMap<String, Object>();
        UserVO userVO = new UserVO();
        userVO.setId(12345);
        map.put("user", "liyong");

        //WordUtil.createWord(map, "222.ftl", new File("liyong.doc"), WordUtil.class);

        WordUtil.createWordFile(map, "aaa.ftl", new File("aaa.doc"), WordUtil.class);
    }
}
