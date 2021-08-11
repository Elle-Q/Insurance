package com.fintech.insurance.micro.biz.service;


import com.fintech.insurance.commons.utils.DateCommonUtils;
import com.fintech.insurance.commons.utils.ExportUtils;
import com.fintech.insurance.components.exception.FInsuranceBaseException;
import org.apache.commons.lang3.StringUtils;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.poifs.filesystem.POIFSFileSystem;
import org.apache.poi.xssf.usermodel.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.util.ClassUtils;
import org.springframework.util.ResourceUtils;

import java.io.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

/**
 * @Description: (导入导出管理)
 * @Author: yongneng liu
 * @Date: 2017/11/14 0014 16:08
 */
@Async
@Service
public class ExportExcelServiceImpl implements ExportExcelService {

    private static final Logger LOG = LoggerFactory.getLogger(ExportExcelServiceImpl.class);

    @Autowired
    private SendMailService sendMailService;

    private ExportExcelPropertiesBean exportExcelPropertiesBean;

    private EmailPropertiesBean emailPropertiesBean;

    @Autowired
    public void setExportExcelPropertiesBean(ExportExcelPropertiesBean exportExcelPropertiesBean) {
        this.exportExcelPropertiesBean = exportExcelPropertiesBean;
    }

    @Autowired
    public void setEmailPropertiesBean(EmailPropertiesBean emailPropertiesBean) {
        this.emailPropertiesBean = emailPropertiesBean;
    }

    public void bizReportExportExcel(List<Map<String,Object>> list, String toUser, String demoFile) {
        if (list == null || list.size() < 1 || StringUtils.isBlank(demoFile)) {
            throw new FInsuranceBaseException(104105);
        }
        if (exportExcelPropertiesBean != null && exportExcelPropertiesBean.getMaxsize() != null && list.size() > exportExcelPropertiesBean.getMaxsize()) {
            this.expoertZip(list, toUser, demoFile);
        } else {
            this.expoertExcel(list, toUser, demoFile);
        }
    }

    private void expoertZip(List<Map<String,Object>> list, String toUser, String demoFile) {
        // 创建一个workbook 对应一个excel应用文件
        XSSFWorkbook workBook = new XSSFWorkbook();
        //文件前缀
        String prefixes ="业务报表" + DateCommonUtils.getNowTimeStr();

        // 在workbook中添加一个sheet,对应Excel文件中的sheet
        XSSFSheet sheet = workBook.createSheet(prefixes);
        ExportUtils exportUtil = new ExportUtils(workBook, sheet);
        XSSFCellStyle headStyle = exportUtil.getHeadStyle();
        XSSFCellStyle bodyStyle = exportUtil.getBodyStyle();
        Map<String,List<String>> map = null;
        InputStream demoFilePath = ClassUtils.getDefaultClassLoader().getResourceAsStream(demoFile);
        try {
            File file = ResourceUtils.getFile("classpath:" + demoFile);
            LOG.error("expoertZip file:" + file == null ? null : file.getAbsolutePath());
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        LOG.error("expoertZip toUser：" + toUser );
        String fileType = "xlsx";
        String suffix = ".xlsx";
        if(exportExcelPropertiesBean != null) {
            fileType = exportExcelPropertiesBean.getFileType() == null ?  "xlsx" : exportExcelPropertiesBean.getFileType();
            if ("xlsx".equalsIgnoreCase(fileType)) {
                map = parsefileNew(demoFilePath);
            } else {
                map = parsefileOld(demoFilePath);
                suffix = ".xls";
            }
        }
        // 构建表头
        createSheet(list, sheet, map,  headStyle,  bodyStyle);
        File file = null;
        File fileZip = null;
        try {
            ByteArrayOutputStream bos = new ByteArrayOutputStream();
            workBook.write(bos);
            file = new File(prefixes+suffix);
            if (!file.exists()) {
                file.createNewFile();
            }
            fileZip = new File( prefixes + ".zip");
            if (!fileZip.exists()) {
                fileZip.createNewFile();
            }
            String filePath = file.getAbsolutePath().substring(0, file.getAbsolutePath().lastIndexOf("/")+1);

            FileOutputStream ostream = new FileOutputStream(file);
            bos.writeTo(ostream);
            ostream.flush();
            ostream.close();

            ZipOutputStream zos = new ZipOutputStream(new BufferedOutputStream(new FileOutputStream(fileZip)));
            FileInputStream fileInputStream = new FileInputStream(file);

            //放入压缩zip包中;
            zos.putNextEntry(new ZipEntry(prefixes + suffix));
            //读取文件;
            byte[] buf = new byte[1024];
            int len = 0;
            while ((len = fileInputStream.read(buf)) > 0) {
                zos.write(buf, 0, len);
            }
            zos.closeEntry();
            zos.flush();
            //关闭;
            if (fileInputStream != null) {
                fileInputStream.close();
            }
            zos.close();
            sendMailService.sendFileMail(emailPropertiesBean.getUsername(), toUser, "诺米金融业务统计数据表", "诺米金融业务统计数据表", filePath + prefixes + ".zip", prefixes + ".zip");
        } catch (IOException e) {
            LOG.error(" expoertZip sendMail failed " , e);
            sendMailService.sendTextMail(emailPropertiesBean.getUsername(), emailPropertiesBean.getUsername(),"邮件发送失败", "您的邮件发送失败了，请尽快核实" );
        } finally {
            try {
                if (file.exists()) {
                    file.delete();
                }
                if (fileZip.exists()) {
                    fileZip.delete();
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

    }

    private void expoertExcel(List<Map<String,Object>> list, String toUser,String demoFile) {
        // 创建一个workbook 对应一个excel应用文件
        XSSFWorkbook workBook = new XSSFWorkbook();
        //文件前缀
        String prefixes ="业务报表" + DateCommonUtils.getNowTimeStr();

        // 在workbook中添加一个sheet,对应Excel文件中的sheet
        XSSFSheet sheet = workBook.createSheet(prefixes);
        ExportUtils exportUtil = new ExportUtils(workBook, sheet);
        XSSFCellStyle headStyle = exportUtil.getHeadStyle();
        XSSFCellStyle bodyStyle = exportUtil.getBodyStyle();
        Map<String,List<String>> map = null;
        try {
            File file = ResourceUtils.getFile("classpath:" + demoFile);
            LOG.error("expoertExcel file:" + file == null ? null : file.getAbsolutePath());
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        InputStream demoFilePath = ClassUtils.getDefaultClassLoader().getResourceAsStream(demoFile);
        LOG.error("expoertExcel toUser：" + toUser);
        String fileType = "xlsx";
        String suffix = ".xlsx";
        if(exportExcelPropertiesBean != null) {
            fileType = exportExcelPropertiesBean.getFileType() == null ?  "xlsx" : exportExcelPropertiesBean.getFileType();
            if ("xlsx".equalsIgnoreCase(fileType)) {
                map = parsefileNew(demoFilePath);
            } else {
                map = parsefileOld(demoFilePath);
                suffix = ".xls";
            }
        }
        // 构建表头
        createSheet(list, sheet, map,  headStyle,  bodyStyle);
        File file = null;
        try {
            ByteArrayOutputStream bos = new ByteArrayOutputStream();
            workBook.write(bos);
            file = new File(  prefixes + suffix);
            if (!file.exists()) {
                file.createNewFile();
            }
            String filePath = file.getAbsolutePath().substring(0, file.getAbsolutePath().lastIndexOf("/")+1);
            FileOutputStream ostream = new FileOutputStream(file);
            bos.writeTo(ostream);
            ostream.flush();
            ostream.close();
            sendMailService.sendFileMail(emailPropertiesBean.getUsername(), toUser, "诺米金融业务统计数据表", "诺米金融业务统计数据表", filePath + prefixes + suffix, prefixes + suffix);
        } catch (IOException e) {
            LOG.error(" expoertExcel sendMail failed " , e);
            sendMailService.sendTextMail(emailPropertiesBean.getUsername(), emailPropertiesBean.getUsername(),"邮件发送失败", "您的邮件发送失败了，请尽快核实" );
        } finally {
            try {
                if (file.exists()) {
                    file.delete();
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

    }

    //创建表格
    private void createSheet(List<Map<String,Object>> list,XSSFSheet sheet, Map<String,List<String>> map, XSSFCellStyle headStyle, XSSFCellStyle bodyStyle ){

        // 构建表头
        XSSFRow headRow = sheet.createRow(0);
        XSSFCell cell = null;
        List<String> dataStrList = null;//获取参数
        if(map != null) {
            dataStrList = map.get("key1");//获取参数
            for (int i = 0; i < map.get("key0").size(); i++) {
                cell = headRow.createCell(i);
                cell.setCellStyle(headStyle);
                cell.setCellValue(map.get("key0").get(i));
            }
        }

        // 构建表体数据
        if (list != null && list.size() > 0) {
            for (int j = 0; j < list.size(); j++) {
                XSSFRow bodyRow = sheet.createRow(j + 1);
                Map<String,Object> mapVO = list.get(j);
                if(dataStrList != null && dataStrList.size() >0) {
                    for (int i=0;i<dataStrList.size();i++){
                        cell = bodyRow.createCell(i);
                        cell.setCellStyle(bodyStyle);
                        cell.setCellValue(String.valueOf(mapVO.get(dataStrList.get(i))));
                    }
                }
            }
        }
    }

    //解析文件
    private Map<String, List<String>> parsefileOld(String filePath) {
        Map<String, List<String>> map = new HashMap<String, List<String>>();
        FileInputStream in = null;
        HSSFWorkbook workbook = null;
        try {
            in = new FileInputStream(filePath);
            POIFSFileSystem fs = new POIFSFileSystem(in);
            workbook = new HSSFWorkbook(fs);
        } catch (IOException e) {
            System.out.println(e.toString());
        } finally {
            try {
                if(in != null) {
                    in.close();
                }
            } catch (IOException e) {
                System.out.println(e.toString());
            }
        }


        HSSFSheet sheet = workbook.getSheetAt(0);
        HSSFRow row1 = sheet.getRow(1);
        HSSFRow row2 = sheet.getRow(2);
        List<String> list1 = new ArrayList<String>();
        List<String> list2 = new ArrayList<String>();
        for(int i=0; i<row1.getLastCellNum();i++){
            list1.add(row1.getCell(i).getStringCellValue());
        }
        for(int i=0; i<row2.getLastCellNum();i++){
            list2.add(row2.getCell(i).getStringCellValue());
        }
        map.put("key0",list1);
        map.put("key1",list2);
        return map;
    }

    //解析文件
    private Map<String, List<String>> parsefileOld(InputStream inputStream) {
        Map<String, List<String>> map = new HashMap<String, List<String>>();
        InputStream in = null;
        HSSFWorkbook workbook = null;
        try {
            in = inputStream;
            POIFSFileSystem fs = new POIFSFileSystem(in);
            workbook = new HSSFWorkbook(fs);
        } catch (IOException e) {
            System.out.println(e.toString());
        } finally {
            try {
                if(in != null) {
                    in.close();
                }
            } catch (IOException e) {
                System.out.println(e.toString());
            }
        }


        HSSFSheet sheet = workbook.getSheetAt(0);
        HSSFRow row1 = sheet.getRow(1);
        HSSFRow row2 = sheet.getRow(2);
        List<String> list1 = new ArrayList<String>();
        List<String> list2 = new ArrayList<String>();
        for(int i=0; i<row1.getLastCellNum();i++){
            list1.add(row1.getCell(i).getStringCellValue());
        }
        for(int i=0; i<row2.getLastCellNum();i++){
            list2.add(row2.getCell(i).getStringCellValue());
        }
        map.put("key0",list1);
        map.put("key1",list2);
        return map;
    }

    //解析文件xlsx
    private Map<String, List<String>> parsefileNew(String filePath) {
        Map<String, List<String>> map = new HashMap<String, List<String>>();
        // IO流读取文件
        InputStream input = null;
        XSSFWorkbook wb = null;
        FileInputStream in = null;
        try {
            input = new FileInputStream(filePath);
            // 创建文档
            wb = new XSSFWorkbook(input);
            //读取sheet(页)
            for (int numSheet = 0; numSheet < wb.getNumberOfSheets(); numSheet++) {
                XSSFSheet xssfSheet = wb.getSheetAt(numSheet);
                if (xssfSheet == null) {
                    continue;
                }
                //读取Row,从第二行开始
                for (int rowNum = 0; rowNum <= 1; rowNum++) {
                    XSSFRow xssfRow = xssfSheet.getRow(rowNum);
                    if (xssfRow != null) {
                        List<String> rowList = new ArrayList<String>();
                        //读取列，从第一列开始
                        for (int c = 0; c <= xssfRow.getLastCellNum() + 1; c++) {
                            XSSFCell cell = xssfRow.getCell(c);
                            if (cell == null) {
                                continue;
                            }
                            rowList.add(cell.getStringCellValue());
                        }
                        map.put("key" + rowNum, rowList);
                    }
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                if(input != null) {
                    input.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return map;
    }

    //解析文件xlsx
    private Map<String, List<String>> parsefileNew(InputStream inputStream) {
        Map<String, List<String>> map = new HashMap<String, List<String>>();
        // IO流读取文件
        InputStream input = null;
        XSSFWorkbook wb = null;
        FileInputStream in = null;
        try {
            input = inputStream;
            // 创建文档
            wb = new XSSFWorkbook(input);
            //读取sheet(页)
            for (int numSheet = 0; numSheet < wb.getNumberOfSheets(); numSheet++) {
                XSSFSheet xssfSheet = wb.getSheetAt(numSheet);
                if (xssfSheet == null) {
                    continue;
                }
                //读取Row,从第二行开始
                for (int rowNum = 0; rowNum <= 1; rowNum++) {
                    XSSFRow xssfRow = xssfSheet.getRow(rowNum);
                    if (xssfRow != null) {
                        List<String> rowList = new ArrayList<String>();
                        //读取列，从第一列开始
                        for (int c = 0; c <= xssfRow.getLastCellNum() + 1; c++) {
                            XSSFCell cell = xssfRow.getCell(c);
                            if (cell == null) {
                                continue;
                            }
                            rowList.add(cell.getStringCellValue());
                        }
                        map.put("key" + rowNum, rowList);
                    }
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                if(input != null) {
                    input.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return map;
    }
}
