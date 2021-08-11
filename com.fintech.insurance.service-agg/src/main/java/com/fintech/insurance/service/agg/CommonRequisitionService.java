package com.fintech.insurance.service.agg;

import com.fintech.insurance.commons.constants.VoidPlaceHolder;
import com.fintech.insurance.commons.web.FintechResponse;
import com.fintech.insurance.micro.dto.IdVO;
import com.fintech.insurance.micro.dto.biz.RequisitionDetailInfoVO;
import com.fintech.insurance.micro.dto.biz.StatisticRequisitionVO;
import com.fintech.insurance.micro.vo.wechat.WeChatCustomerRequisitionVO;
import com.fintech.insurance.micro.vo.wechat.WeChatSimpleRequisitionDetailVO;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * @Description: (some words)
 * @Author: Administrator
 * @Date: 2017/12/11 0011 16:14
 */
public interface CommonRequisitionService {

    public FintechResponse<WeChatCustomerRequisitionVO> getCustomerRequisitionVOById(Integer id);

    /**
     * 查询车辆信息
     * @param requisitionId 业务单id
     * @param pageIndex 页数
     * @param pageSize 每页数
     * @return
     */
    List<WeChatSimpleRequisitionDetailVO> pageRequisitionDetailByRequisitionId(Integer requisitionId, Integer pageIndex, Integer pageSize);
}
