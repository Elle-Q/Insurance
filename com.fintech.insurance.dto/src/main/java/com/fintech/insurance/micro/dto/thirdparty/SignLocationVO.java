package com.fintech.insurance.micro.dto.thirdparty;

import com.alibaba.fastjson.annotation.JSONField;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @Description: 签署位置
 * @Author: Yong Li
 * @Date: 2017/11/22 18:33
 */
public class SignLocationVO {

    /**
     * 页码
     */
    @JSONField(name = "pageNum")
    private Integer pageNum;

    /**
     * 横坐标，按页面尺寸的百分比计算，取值0.0 - 1.0。以左上角为原点
     */
    @JSONField(name = "x")
    private Double xaxis;

    /**
     * 纵坐标，按页面尺寸的百分比计算，取值0.0 - 1.0。以左上角为原点
     */
    @JSONField(name = "y")
    private Double yaxis;

    public SignLocationVO() {
    }

    public SignLocationVO(Integer pageNum, Double xaxis, Double yaxis) {
        this.pageNum = pageNum;
        this.xaxis = xaxis;
        this.yaxis = yaxis;
    }

    public Integer getPageNum() {
        return pageNum;
    }

    public void setPageNum(Integer pageNum) {
        this.pageNum = pageNum;
    }

    public Double getXaxis() {
        return xaxis;
    }

    public void setXaxis(Double xaxis) {
        this.xaxis = xaxis;
    }

    public Double getYaxis() {
        return yaxis;
    }

    public void setYaxis(Double yaxis) {
        this.yaxis = yaxis;
    }

    public static void main(String[] args){
        List<SignLocationVO> locations = new ArrayList<SignLocationVO>();
        locations.add(new SignLocationVO(1, 0.21, 0.88));

        Map<String, Object> map = new HashMap<String, Object>();
        map.put("locations", locations);

        //System.out.println(BestsignUtil.toJsonString(locations));
    }
}
