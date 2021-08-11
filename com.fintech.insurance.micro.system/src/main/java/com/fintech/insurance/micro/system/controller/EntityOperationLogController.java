package com.fintech.insurance.micro.system.controller;

import com.fintech.insurance.commons.enums.AccountType;
import com.fintech.insurance.commons.enums.OperationType;
import com.fintech.insurance.commons.constants.VoidPlaceHolder;
import com.fintech.insurance.commons.web.FintechResponse;
import com.fintech.insurance.components.exception.FInsuranceBaseException;
import com.fintech.insurance.components.web.BaseFintechController;
import com.fintech.insurance.micro.api.system.EntityOperationLogServiceAPI;
import com.fintech.insurance.micro.dto.biz.OperationRecordVO;
import com.fintech.insurance.micro.feign.finance.PaymentOrderServiceFeign;
import com.fintech.insurance.micro.system.service.EntityOperationLogService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * 操作记录
 *
 * @author qxy
 * @since 2017-11-15 11:56
 */
@RestController
public class EntityOperationLogController extends BaseFintechController implements EntityOperationLogServiceAPI {

    @Autowired
    EntityOperationLogService entityOperationLogService;

    @Autowired
    PaymentOrderServiceFeign paymentOrderServiceFeign;

    @Override
    public FintechResponse<List<OperationRecordVO>> listOperationrRecord(@RequestParam(name = "id")Integer id) {
        if (null == id) {
            throw new FInsuranceBaseException(101505);
        }
        List<OperationRecordVO> list =  entityOperationLogService.listOperationrRecord(id);
        return FintechResponse.responseData(list);
    }

    @Override
    public FintechResponse<VoidPlaceHolder> createLog(@RequestBody OperationRecordVO operationrRecordVO) {
        entityOperationLogService.createLog(operationrRecordVO);

        return FintechResponse.responseData(VoidPlaceHolder.instance());
    }

        @Override
        public FintechResponse<String[]> getLogAttachment(@RequestParam(value = "userId") Integer userId, @RequestParam(value = "id") Integer id,
                @RequestParam(value = "operationType") String operationType) {
            if (null == userId || null == id || StringUtils.isEmpty(operationType)) {
                throw new FInsuranceBaseException(101509);
            }
            String accountType = "";
            if (OperationType.PAID.getCode().equals(operationType)) {
                accountType = AccountType.SERVICEFEE.getCode();
            } else if (OperationType.LOANED.getCode().equals(operationType)) {
                accountType = AccountType.LOAN.getCode();
            } else if (OperationType.REFUND.getCode().equals(operationType)) {
                accountType = AccountType.REFUND.getCode();
            }

        return paymentOrderServiceFeign.getByUserAndEntityId(userId, id, accountType);
    }
}
