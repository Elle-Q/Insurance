package com.fintech.insurance.micro.customer.webchat.controller;

import com.fintech.insurance.commons.constants.BasicConstants;
import com.fintech.insurance.commons.utils.Pagination;
import com.fintech.insurance.commons.web.FintechResponse;
import com.fintech.insurance.components.annotation.RequireWechatLogin;
import com.fintech.insurance.components.exception.FInsuranceBaseException;
import com.fintech.insurance.components.web.BaseFintechWechatController;
import com.fintech.insurance.micro.dto.finance.FinanceRepaymentPlanVO;
import com.fintech.insurance.micro.feign.finance.RepaymentPlanServiceFeign;
import com.fintech.insurance.micro.vo.wechat.RepaymentPlanSimpleVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;

/**
 * @Author: qxy
 * @Description:客户还款计划分页查询
 * @Date: 2017/12/9 15:15
 */
@RestController
@RequestMapping(value = "/wechat/customer/repayment-plan", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
@RequireWechatLogin
public class RepaymentPlanController extends BaseFintechWechatController{

    @Autowired
    private RepaymentPlanServiceFeign repaymentPlanServiceFeign;

    @RequestMapping(value = "/page", method = RequestMethod.GET)
    public FintechResponse<Pagination<RepaymentPlanSimpleVO>> pageRepaymentPlanByCustomeId(@RequestParam(value = "days") Integer days,
                                                                    @RequestParam(value = "pageIndex", defaultValue = BasicConstants.DEFAULT_PAGE_INDEX) Integer pageIndex,
                                                                    @RequestParam(value = "pageSize", defaultValue = BasicConstants.DEFAULT_PAGE_SIZE) Integer pageSize) {

        Integer currentLoginUserId = getCurrentUserId();//当前登录客户
        FintechResponse<Pagination<FinanceRepaymentPlanVO>> response = repaymentPlanServiceFeign.pageRepaymentPlanByCustomeId(currentLoginUserId, days, pageIndex, pageSize);
        if (!response.isOk()) {
            throw FInsuranceBaseException.buildFromErrorResponse(response);
        }
        Pagination<FinanceRepaymentPlanVO> repaymentPlanVOPagination = response.getData();
        List<RepaymentPlanSimpleVO> repaymentPlanSimpleVOList = new ArrayList<>();
        if (null != repaymentPlanVOPagination.getItems() && repaymentPlanVOPagination.getItems().size() > 0) {
            for (FinanceRepaymentPlanVO r : repaymentPlanVOPagination.getItems()) {
                RepaymentPlanSimpleVO repaymentPlanSimpleVO = this.convertToVO(r);
                repaymentPlanSimpleVOList.add(repaymentPlanSimpleVO);
            }
        }
        Pagination<RepaymentPlanSimpleVO> resultPage = Pagination.createInstance(pageIndex, pageSize, repaymentPlanVOPagination.getTotalRowsCount(), repaymentPlanSimpleVOList);

        return FintechResponse.responseData(resultPage);
    }

    //转化为微信前端vo
    private RepaymentPlanSimpleVO convertToVO(FinanceRepaymentPlanVO r) {
        if (null == r) {
            return null;
        }
        RepaymentPlanSimpleVO repaymentPlanSimpleVO = new RepaymentPlanSimpleVO();
        repaymentPlanSimpleVO.setId(r.getId());
        repaymentPlanSimpleVO.setRepayTotalAmount(r.getRepayTotalAmount());
        repaymentPlanSimpleVO.setRepayDate(r.getRepayDate());
        return repaymentPlanSimpleVO;
    }
}
