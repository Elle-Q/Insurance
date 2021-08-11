package com.fintech.insurance.micro.biz.service;


import java.util.List;
import java.util.Map;

/**
 * @Description: (导入导出管理)
 * @Author: yongNeng Liu
 * @Date: 2017/11/13 10:28
 */
public interface ExportExcelService {
    public void bizReportExportExcel(List<Map<String,Object>> list, String toUser, String demoFile);
}
