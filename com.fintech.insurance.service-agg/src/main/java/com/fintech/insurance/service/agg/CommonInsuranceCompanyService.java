package com.fintech.insurance.service.agg;

import com.fintech.insurance.commons.utils.Pagination;
import com.fintech.insurance.micro.dto.biz.SimpleProductVO;
import com.fintech.insurance.micro.vo.wechat.WeChatInsuranceCompanyVO;
import com.fintech.insurance.micro.vo.wechat.WeChatProductVO;
import org.springframework.web.bind.annotation.RequestParam;

import javax.validation.constraints.NotNull;
import java.util.List;

/**
 * @Description: (some words)
 * @Author: Administrator
 * @Date: 2017/12/11 0011 16:14
 */
public interface CommonInsuranceCompanyService {

    public List<WeChatInsuranceCompanyVO> queryAllInsuranceCompany();
}
