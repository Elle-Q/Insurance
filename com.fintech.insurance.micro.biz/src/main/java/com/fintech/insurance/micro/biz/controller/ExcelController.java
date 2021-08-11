package com.fintech.insurance.micro.biz.controller;

import com.fintech.insurance.components.web.BaseFintechController;
import com.fintech.insurance.micro.api.biz.BizExcelServiceAPI;
import com.fintech.insurance.micro.biz.service.ProductService;
import com.fintech.insurance.micro.dto.biz.ProductVO;
import org.apache.poi.hssf.usermodel.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.UUID;

//import com.microsoft.azure.storage.blob.CloudBlobContainer;
//import com.microsoft.azure.storage.blob.CloudBlockBlob;


/**
 * @Description: 业务报表查询接口
 * @Author: Yong Li
 * @Date: 2017/11/10 12:16
 */
@RestController
public class ExcelController extends BaseFintechController implements BizExcelServiceAPI {

    @Autowired
    private ProductService productService;
    /***
     * 创建表头
     * @param workbook
     * @param sheet
     */
    private void createTitle(HSSFWorkbook workbook, HSSFSheet sheet)
    {
        HSSFRow row = sheet.createRow(0);
        //设置列宽，setColumnWidth的第二个参数要乘以256，这个参数的单位是1/256个字符宽度
        sheet.setColumnWidth(2, 12*256);
        sheet.setColumnWidth(3, 17*256);

        //设置为居中加粗
        HSSFCellStyle style = workbook.createCellStyle();
        HSSFFont font = workbook.createFont();
        //font.setBold(true);
        style.setAlignment(HSSFCellStyle.ALIGN_CENTER);
        style.setFont(font);

        HSSFCell cell;
        cell = row.createCell(0);
        cell.setCellValue("序号");
        cell.setCellStyle(style);

        cell = row.createCell(1);
        cell.setCellValue("金额");
        cell.setCellStyle(style);

        cell = row.createCell(2);
        cell.setCellValue("描述");
        cell.setCellStyle(style);

        cell = row.createCell(3);
        cell.setCellValue("日期");
        cell.setCellStyle(style);
    }

    /***
     * 获取excel数据
     * @return 返回文件名称及excel文件的URL
     * @throws IOException
     */
    @SuppressWarnings({ "unchecked", "rawtypes" })
    @RequestMapping("getExcel")
    public Object getExcel() throws IOException {
        HSSFWorkbook workbook = new HSSFWorkbook();
        HSSFSheet sheet = workbook.createSheet("统计表");
        createTitle(workbook, sheet);
        List<ProductVO> entities = (List<ProductVO>) productService.queryProduct(null, null, null, null, 1, 10).getItems();

        //设置日期格式
        HSSFCellStyle style = workbook.createCellStyle();
        style.setDataFormat(HSSFDataFormat.getBuiltinFormat("m/d/yy h:mm"));

        //新增数据行，并且设置单元格数据
        int rowNum = 1;
        for (ProductVO productVO : entities) {

            HSSFRow row = sheet.createRow(rowNum);
            row.createCell(0).setCellValue(productVO.getId());
            row.createCell(1).setCellValue(productVO.getProductName());
            row.createCell(2).setCellValue(productVO.getProductDescription());
            HSSFCell cell = row.createCell(3);
            cell.setCellValue(productVO.getCreateAt());
            cell.setCellStyle(style);
            rowNum++;
        }

        //拼装blobName
        String fileName = "测试数据统计表.xlsx";
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyyMMdd");
        String dateTime = dateFormat.format(new Date());
        String blobName = dateTime + "/" + UUID.randomUUID().toString().replaceAll("-", "") + "/" + fileName;

//        //获取或创建container
//        CloudBlobContainer blobContainer = BlobHelper.getBlobContainer("temp", storageConfig);
//        //设置文件类型，并且上传到azure blob
//        try {
//            CloudBlockBlob blob = blobContainer.getBlockBlobReference(blobName);
//            ByteArrayOutputStream out = new ByteArrayOutputStream();
//            workbook.write(out);
//            ByteArrayInputStream in = new ByteArrayInputStream(out.toByteArray());
//
//            blob.upload(in, out.toByteArray().length);
//            Map map = new HashMap();
//            map.put("fileName", fileName);
//            map.put("excelUrl", blob.getUri().toString());
//
//            ResultMsg resultMsg = new ResultMsg(ResultStatusCode.OK.getErrcode(),
//                    ResultStatusCode.OK.getErrmsg(), map);
//            return resultMsg;
//
//        } catch (Exception e)
//        {
//            ResultMsg resultMsg = new ResultMsg(ResultStatusCode.SYSTEM_ERR.getErrcode(),
//                    ResultStatusCode.SYSTEM_ERR.getErrmsg(), null);
//            return resultMsg;
//        }
        return  null;
    }

}
