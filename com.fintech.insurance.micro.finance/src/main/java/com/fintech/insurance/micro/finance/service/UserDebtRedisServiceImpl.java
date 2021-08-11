package com.fintech.insurance.micro.finance.service;

import com.fintech.insurance.commons.enums.DebtStatus;
import com.fintech.insurance.commons.enums.SystemProfile;
import com.fintech.insurance.commons.utils.DateCommonUtils;
import com.fintech.insurance.components.cache.AbstractRedisService;
import com.fintech.insurance.components.web.context.FInsuranceApplicationContext;
import com.fintech.insurance.micro.finance.model.UserDebtInfoRedisVO;
import com.fintech.insurance.micro.finance.service.yjf.YjfPaymentServiceImpl;
import com.fintech.insurance.micro.finance.service.yjf.YjfPropertiesBean;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.*;
import java.util.concurrent.TimeUnit;

/**
 * @Description: (some words)
 * @Author: Yong Li
 * @Date: 2017/12/22 14:16
 */
@Service
public class UserDebtRedisServiceImpl extends AbstractRedisService<Set<UserDebtInfoRedisVO>> implements UserDebtRedisService {

    private static final Logger LOG = LoggerFactory.getLogger(UserDebtRedisServiceImpl.class);

    private static final String DEBT_INFO_REDIS_KEY = "BANKCARD_DEBT_INFO";

    @Resource
    protected ValueOperations<String, List<UserDebtInfoRedisVO>> valueOperations;

    @Autowired
    private YjfPropertiesBean propertiesBean;

    protected String getRedisKey(String bankcardNum) {
        return String.format("%s#%s", DEBT_INFO_REDIS_KEY, bankcardNum);
    }

    @Deprecated
    @Override
    protected String getRedisKey() {
        throw new IllegalStateException("this interface is duplicated.");
    }

    @Override
    public void saveOrUpdate(UserDebtInfoRedisVO debtInfo) {
        if (null == debtInfo) {
            return ;
        }

        String bankcardKey = getRedisKey(debtInfo.getBankcardNum());

        List<UserDebtInfoRedisVO> existingData = null;
        if (!redisTemplate.hasKey(bankcardKey)) {
            existingData = new ArrayList<UserDebtInfoRedisVO>();
            existingData.add(debtInfo);
        } else {
            existingData = valueOperations.get(bankcardKey);
            if (!existingData.contains(debtInfo)) {
                existingData.add(debtInfo);  // 更新
            } else {
                for (UserDebtInfoRedisVO vo : existingData) {
                    if (debtInfo.getDebtOrderNum().equals(vo.getDebtOrderNum())) {
                        vo.setDebtStatus(debtInfo.getDebtStatus());
                        break;
                    }
                }
            }
        }

        // 新（重新）更新到缓存
        valueOperations.set(bankcardKey, existingData);
        redisTemplate.expire(bankcardKey, getExpiredSeconds(), TimeUnit.SECONDS);
    }

    private int getExpiredSeconds() {
        return DateCommonUtils.getSeconds(new Date(), DateCommonUtils.getTomorrow());
    }


    @Override
    public Double countTotalDebtedAmount(String bankcardNum) {
        String bankcardKey = getRedisKey(bankcardNum);
        if (!redisTemplate.hasKey(bankcardKey)) {
            return 0.0;
        } else {
            Double totalAmount = 0.0;
            List<UserDebtInfoRedisVO> existingData = valueOperations.get(bankcardKey);
            for (UserDebtInfoRedisVO vo : existingData) {
                if (vo.getDebtStatus().isNotFailed()) {
                    totalAmount += vo.getAmount();
                }
            }
            return totalAmount;
        }
    }

    @Override
    public boolean isDebtAvailable(String bankcardNum) {
        String bankcardKey = getRedisKey(bankcardNum);
        if (redisTemplate.hasKey(bankcardKey)) {
            List<UserDebtInfoRedisVO> existingData = valueOperations.get(bankcardKey);

            int successCount = 0;
            int failedCount = 0;
            int unknownCount = 0;
            for (UserDebtInfoRedisVO vo : existingData) {
                if (DebtStatus.PROCESSING == vo.getDebtStatus()) {
                    unknownCount ++;
                } else if (DebtStatus.CONFIRMED == vo.getDebtStatus() || DebtStatus.SETTLED == vo.getDebtStatus()) {
                    successCount ++;
                } else if (DebtStatus.FAILED == vo.getDebtStatus()) {
                    failedCount ++;
                }
            }


            if ((successCount + unknownCount) >= propertiesBean.getDebtTimes() || (failedCount + unknownCount) >= propertiesBean.getDebtTimes() ) {
                return false;
            }
        }
        return true;
    }

    /**
     * 指定扣款订单号在某天是否为第一次扣款
     * @param debtOrderNum 扣款订单号
     * @return
     */
    @Override
    public boolean isFirstDebtOnToday(String bankcardNum, String debtOrderNum) {
        LOG.info("try to check the bankcard: {} is the first debt on today, debt order number: {}", bankcardNum, debtOrderNum);
        String bankcardKey = getRedisKey(bankcardNum);
        if (redisTemplate.hasKey(bankcardKey)) {
            List<UserDebtInfoRedisVO> existingData = valueOperations.get(bankcardKey);
            return !existingData.isEmpty() && existingData.get(0).getDebtOrderNum().equals(debtOrderNum);
        }
        LOG.info("The bankcard: {} is not the first debt on today, debt order number: {}", bankcardNum, debtOrderNum);
        return false;
    }

    @Override
    public void clearAll(String bankcardNum) {
        String bankcardKey = getRedisKey(bankcardNum);
        if (redisTemplate.hasKey(bankcardKey)) {
            redisTemplate.delete(bankcardKey);
        }
    }

    @Override
    public int getUsedDebtTimes(String bankcardNum) {
        String bankcardKey = getRedisKey(bankcardNum);
        if (redisTemplate.hasKey(bankcardKey)) {
            return valueOperations.get(bankcardKey).size();
        }
        return 0;
    }
}
