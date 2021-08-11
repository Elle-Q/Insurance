package com.fintech.insurance.micro.finance.persist.dao;

import com.fintech.insurance.components.persist.BaseDao;
import com.fintech.insurance.micro.finance.persist.entity.BankcardVerifyRecord;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface BankcardVerifyRecordDao extends JpaRepository<BankcardVerifyRecord, Integer>, BaseDao<BankcardVerifyRecord>, BankcardVerifyRecordComplexDao {

    @Query("select r from BankcardVerifyRecord r where r.userName = :userName and r.idNumber = :idNumber and r.bankCardNumber= :bankCardNumber " +
            "and r.reservedMobile= :reservedMobile and r.verificationStatus = 'success' order by r.verificationTime desc")
    List<BankcardVerifyRecord> querySuccessResultByFourElements(@Param("userName") String userName, @Param("idNumber") String idNumber,
                                                                @Param("bankCardNumber") String bankCardNumber, @Param("reservedMobile") String reservedMobile);

    List<BankcardVerifyRecord> queryByVerificationStatus(String verificationStatus);

}
