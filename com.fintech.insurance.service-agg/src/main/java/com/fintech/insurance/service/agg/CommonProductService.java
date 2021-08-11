package com.fintech.insurance.service.agg;

import com.fintech.insurance.commons.utils.Pagination;
import com.fintech.insurance.commons.web.FintechResponse;
import com.fintech.insurance.micro.dto.biz.ProductVO;
import com.fintech.insurance.micro.dto.biz.SimpleProductVO;
import com.fintech.insurance.micro.vo.wechat.WeChatProductVO;
import com.fintech.insurance.micro.vo.wechat.WeChatSimpleProductVO;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import javax.validation.constraints.NotNull;

/**
 * @Description: (some words)
 * @Author: Administrator
 * @Date: 2017/12/11 0011 16:14
 */
public interface CommonProductService {
    public WeChatProductVO findProductInfoById(String userType, Integer userId, String channelCode, Integer productId);
}
