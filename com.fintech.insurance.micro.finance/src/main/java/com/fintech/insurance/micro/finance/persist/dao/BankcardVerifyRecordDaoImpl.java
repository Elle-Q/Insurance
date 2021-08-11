package com.fintech.insurance.micro.finance.persist.dao;

import com.fintech.insurance.components.persist.BaseDaoImpl;
import com.fintech.insurance.micro.finance.persist.entity.BankcardVerifyRecord;
import org.springframework.stereotype.Repository;

@Repository
public class BankcardVerifyRecordDaoImpl extends BaseDaoImpl<BankcardVerifyRecord, Integer> implements BankcardVerifyRecordComplexDao {
}
