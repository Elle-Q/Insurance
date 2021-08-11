package com.fintech.insurance.micro.customer.persist.dao;

import com.fintech.insurance.components.persist.BaseEntityDaoImpl;
import com.fintech.insurance.micro.customer.persist.entity.CustomerBankCard;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Repository;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @Author: Clayburn
 * @Description:
 * @Date: 2017/11/14 13:46
 */
@Repository
public class CustomerBankCardDaoImpl extends BaseEntityDaoImpl<CustomerBankCard, Integer> implements CustomerBankCardComplexDao {

    @Override
    public CustomerBankCard findCustomerBankCardByCustomerIdAndCardNumber(Integer customerId, String accountNumber) {
        // 这里可以找到已经删除过的银行卡， 不用加disableFlag = true 过滤条件
        StringBuilder hql = new StringBuilder(" select c from CustomerBankCard c where 1=1 ");
        Map<String, Object> map = new HashMap<String, Object>();
        if(customerId != null){
            hql.append(" and c.customerAccount.id = :customerId ");
            map.put("customerId", customerId);
        }
        if(StringUtils.isNoneBlank(accountNumber)){
            hql.append(" and c.accountNumber = :accountNumber ");
            map.put("accountNumber", accountNumber);
        }
        hql.append(" order by c.createAt desc ");
        List<CustomerBankCard> customerBankCards = this.findList(hql, 0, map);

        if (customerBankCards.isEmpty()) {
            return null;
        } else {
            for (CustomerBankCard bankCard : customerBankCards) {
                if (!bankCard.getDisableFlag()) {
                    return bankCard;
                }
            }

            return customerBankCards.get(0);
        }
    }
}
