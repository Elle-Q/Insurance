package com.fintech.insurance.micro.finance.service;


import com.fintech.insurance.micro.finance.model.UserDebtInfoRedisVO;

/**
 * @Description: 用户银行卡代扣 Redis缓存服务
 * @Author: Yong Li
 * @Date: 2017/12/22 14:01
 */
public interface UserDebtRedisService {

    /**
     * 在缓存中保存扣款信息或更新扣款状态
     * @param debtInfo
     */
    void saveOrUpdate(UserDebtInfoRedisVO debtInfo);

    /**
     * 计算用户在当日已使用扣款次数
     * @param bankcardNum
     * @return
     */
    int getUsedDebtTimes(String bankcardNum);

    /**
     * 计算用户在指定日期已使用扣款额度(扣款状态在成功或处理中的记录)
     * @param bankcardNum
     * @return
     */
    Double countTotalDebtedAmount(String bankcardNum);

    /**
     * 计算用户在当天是否还有可用的扣款次数
     * @param bankcardNum
     * @return
     */
    boolean isDebtAvailable(String bankcardNum);

    /**
     * 指定扣款订单号在某天是否为第一次扣款
     *
     * @param bankcardNum 扣款的银行卡号
     * @param debtOrderNum 扣款订单号
     * @return
     */
    boolean isFirstDebtOnToday(String bankcardNum, String debtOrderNum);

    void clearAll(String bankcardNum);
}
