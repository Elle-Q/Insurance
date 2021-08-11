package com.fintech.insurance.micro.api.biz;

import com.fintech.insurance.commons.utils.Pagination;
import com.fintech.insurance.commons.web.FintechResponse;
import com.fintech.insurance.micro.dto.customer.CustomerConsultationVO;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

@RequestMapping(value = "/customer/intention")
public interface IntentionServiceAPI {

    /**
     * 意向管理分页查询
     */
    @RequestMapping(value = "/page", method = RequestMethod.GET)
    FintechResponse<Pagination<CustomerConsultationVO>> pageIntention(@RequestParam(name = "submmitStartTime", required = false) Long submmitStartTime,
                                                                      @RequestParam(name = "submmitEndTime", required = false) Long submmitEndTime,
                                                                      @RequestParam(name = "mobile", defaultValue = "") String mobile,
                                                                      @RequestParam(name = "name", defaultValue = "") String name,
                                                                      @RequestParam(name = "pageIndex", defaultValue = "1") Integer pageIndex,
                                                                      @RequestParam(name = "pageSize", defaultValue = "20") Integer pageSize);

    /**
     * WX客户咨询信息保存
     * @param customerConsultationVO
     */
    @RequestMapping(value = "/save-consultation", method = RequestMethod.POST)
    FintechResponse<Integer> saveConsultation(@RequestBody CustomerConsultationVO customerConsultationVO);
}
