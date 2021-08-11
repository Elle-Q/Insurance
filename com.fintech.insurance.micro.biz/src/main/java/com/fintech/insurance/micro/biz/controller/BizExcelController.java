package com.fintech.insurance.micro.biz.controller;

import com.fintech.insurance.components.web.BaseFintechController;
import com.fintech.insurance.micro.api.biz.BizExcelServiceAPI;
import com.fintech.insurance.micro.biz.service.ExportExcelService;
import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


/**
 * @Description: 操作表格
 * @Author: Yong Li
 * @Date: 2017/11/10 12:16
 */
@RestController
public class BizExcelController extends BaseFintechController implements BizExcelServiceAPI {

    @Resource
    private ExportExcelService service;

    @RequestMapping(value = "/export", method ={RequestMethod.POST,RequestMethod.GET})
    public String exportExcel(HttpServletResponse response)
    {
        //response.setContentType("application/binary;charset=UTF-8");
        try
        {
            //ServletOutputStream outputStream = response.getOutputStream();
            String fileName = new String(("excel"+"-"+System.currentTimeMillis()).getBytes(), "UTF-8");
            String s =new String(fileName +".zip");
            //response.setHeader("Content-disposition", "attachment; filename=" + fileName + ".xlsx");// 组装附件名称和格式

            //response.setHeader( "Content-Disposition ", "attachment;filename= "+s);
            String hql = fileName;
            String[] titles = { "商品名", "商品单价", "商品单位" };
            List<Map<String,Object>> mapList = new ArrayList<Map<String,Object>>();
            for(int i =1;i<10 ;i++) {
                Map<String, Object> map = new HashMap<String, Object>();
                map.put("productName", "奔驰"+i);
                map.put("productCode", "宝马"+i);
                map.put("productType", "劳斯莱斯"+i);
                mapList.add(map);
            }
            service.bizReportExportExcel(mapList,"1286961802@qq.com","demo.xlsx");
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }
        return null;
    }

    @RequestMapping(value = "/upload", method ={RequestMethod.POST,RequestMethod.GET})
    public String upload(HttpServletRequest request, HttpServletResponse response)
    {
        MultipartHttpServletRequest mulRequest = (MultipartHttpServletRequest) request;
        MultipartFile file = mulRequest.getFile("excel");
        String filename = file.getOriginalFilename();
        if (filename == null || "".equals(filename))
        {
            return null;
        }
        try
        {
            InputStream input = file.getInputStream();
            XSSFWorkbook workBook = new XSSFWorkbook(input);
            XSSFSheet sheet = workBook.getSheetAt(0);
            if (sheet != null)
            {
                for (int i = 1; i < sheet.getPhysicalNumberOfRows(); i++)
                {
                    XSSFRow row = sheet.getRow(i);
                    for (int j = 0; j < row.getPhysicalNumberOfCells(); j++)
                    {
                        XSSFCell cell = row.getCell(j);
                        String cellStr = cell.toString();
                        System.out.print("【"+cellStr+"】 ");
                    }
                    System.out.println();
                }

            }
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
        return null;
    }

}
