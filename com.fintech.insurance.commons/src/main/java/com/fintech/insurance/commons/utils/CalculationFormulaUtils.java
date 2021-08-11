package com.fintech.insurance.commons.utils;

import com.fintech.insurance.commons.enums.ProductType;
import com.fintech.insurance.commons.enums.RepayDayType;
import org.apache.commons.lang.time.DateUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.util.Date;

/**
 * 计算公式
 *
 * @author man liu
 * @since 2017-12-11
 * @version 1.0.0
 */
public class CalculationFormulaUtils {

    private static final Logger logger = LoggerFactory.getLogger(CalculationFormulaUtils.class);

    public static final  Double NUMBER = 1.06;

    public static final  Double NUMBER_ONE = 1.0;

    public static final  Double ZERO_NUMBER = 0.0;

    private static final  BigDecimal YEAR_DAYS = new BigDecimal(365);

    private static final  Double SCACLE_NUMBER = 30.41;

    //利率放大倍数
    private static final  BigDecimal RATIO_ENLARGEMENT  = new BigDecimal(10000);

    //获取商业残值
   public static Double getCommercialRisk(Integer money, Date beginDate, Date endDate, ProductType productType){
       Double d = ZERO_NUMBER;
       if(money == null || beginDate == null || endDate == null || !endDate.after(beginDate) || endDate.before(DateCommonUtils.getToday())){
           return d;
       }
       long days = DateCommonUtils.getDays(beginDate, endDate) < 0 ? 0 : DateCommonUtils.getDays(beginDate, endDate);
       //大于一年的保单残值就是保单金额
       if(days >= YEAR_DAYS.longValue() || ProductType.CAR_INSTALMENTS == productType){
           return new BigDecimal(money).doubleValue();
       }
       d = new BigDecimal(money * days).divide(YEAR_DAYS,0, BigDecimal.ROUND_HALF_UP).doubleValue();
       return d;
   }


   //单个车辆借款金额
    public static BigDecimal getVehicleLoanAmount(Integer compulsoryInsuranceAmount, Integer taxAmount, Integer commercialRisk, Double loanRatio, ProductType productType){
        BigDecimal d = BigDecimal.ZERO;
        if(compulsoryInsuranceAmount == null || taxAmount == null || commercialRisk == null || loanRatio == null){
            return d;
        }
        if(ProductType.CAR_INSTALMENTS == productType){
            return new BigDecimal(commercialRisk);
        }
        d = (new BigDecimal(compulsoryInsuranceAmount + taxAmount).add(new BigDecimal(commercialRisk).divide(new BigDecimal(NUMBER), 6, BigDecimal.ROUND_HALF_UP))).multiply(new BigDecimal(loanRatio)).divide(RATIO_ENLARGEMENT).setScale(0, BigDecimal.ROUND_HALF_UP);
        return d;
    }

    //每期还款金额
    public static BigDecimal getEachRepayAmount(BigDecimal contractAmount, Integer businessDuration, Double interestRate) {
        if(contractAmount == null || businessDuration == null || interestRate == null){
            return BigDecimal.ZERO;
        }
        return contractAmount.divide(new BigDecimal(businessDuration),0, BigDecimal.ROUND_DOWN).add(contractAmount.multiply(new BigDecimal(interestRate)).divide(RATIO_ENLARGEMENT)).setScale(0, BigDecimal.ROUND_HALF_UP);
    }

    //计算第一期还款金额
    public static BigDecimal getFirstRepayMoney(BigDecimal contractAmount, Integer businessDuration, Double interestRate, RepayDayType type) {
        if(contractAmount == null || businessDuration == null || interestRate == null || type == null){
            return BigDecimal.ZERO;
        }
        BigDecimal d = null;
        if(type == RepayDayType.INITIAL_PAYMENT) {
            //期初还款本金+尾差本金
            d = contractAmount.divide(new BigDecimal(businessDuration),0,BigDecimal.ROUND_DOWN).add(getRemainMoney(contractAmount,businessDuration)).setScale(0, BigDecimal.ROUND_DOWN);
        }else{
            //期初还款本金+尾差本金+第一期利息
            d = contractAmount.divide(new BigDecimal(businessDuration),0,BigDecimal.ROUND_DOWN).add(getRemainMoney(contractAmount,businessDuration)).setScale(0, BigDecimal.ROUND_DOWN);
            d = d.add(contractAmount.multiply(new BigDecimal(interestRate)).divide(RATIO_ENLARGEMENT,0,BigDecimal.ROUND_DOWN)).setScale(0, BigDecimal.ROUND_DOWN);
        }
        return d;
    }

    //保证金
    public static BigDecimal getAssureMoney(BigDecimal contractAmount, Integer businessDuration) {
        if(contractAmount == null || businessDuration == null || businessDuration <= 0){
            return BigDecimal.ZERO;
        }
        try {
            return contractAmount.divide(new BigDecimal(businessDuration),0, BigDecimal.ROUND_DOWN);
        } catch (Exception e) {
            logger.error("Fail to calculate the assure money due to exception found", e);
            return BigDecimal.ZERO;
        }
    }
    //尾数
    public static BigDecimal getRemainMoney(BigDecimal contractAmount, Integer businessDuration) {
        if (contractAmount == null || businessDuration == null) {
            return BigDecimal.ZERO;
        }
        return contractAmount.subtract(contractAmount.divide(new BigDecimal(businessDuration),0, BigDecimal.ROUND_DOWN).multiply(new BigDecimal(businessDuration)));
    }
    //服务费
    public static BigDecimal getServiceFee(BigDecimal contractAmount, Double serviceRate) {
        if(contractAmount == null || serviceRate == null){
            return BigDecimal.ZERO;
        }
        return contractAmount.multiply(new BigDecimal(serviceRate)).divide(RATIO_ENLARGEMENT).setScale(0, BigDecimal.ROUND_HALF_UP);
    }

    //获取期数
    public static Integer getBusinessDuration(Date beginDate, Date endDate, ProductType productTpye) {
        if(beginDate == null || endDate == null || endDate.before(DateCommonUtils.getToday())){
            return 0;
        }
        //车贷分期返还天数
        if(ProductType.CAR_INSTALMENTS == productTpye) {
            return new BigDecimal(DateCommonUtils.getDays(beginDate, endDate)).intValue();
        }
        //保单贷款返回剩余可待月数
        return new BigDecimal((DateCommonUtils.getDays(beginDate, endDate)) / SCACLE_NUMBER - 1).intValue();
    }

    //获取还款日
    public static Date getRepayDate(Date beginDate, Integer businessDuration, Integer advanceRepayDays, RepayDayType type){
        if(beginDate == null || businessDuration == null || businessDuration <= 0 || type == null){
            return null;
        }
        Date date = null;
        if(type == RepayDayType.INITIAL_PAYMENT){
            date = DateUtils.addMonths(DateCommonUtils.truncateDay(beginDate),businessDuration-1);
        }else{
            date = DateUtils.addDays(DateUtils.addMonths(DateCommonUtils.truncateDay(beginDate),businessDuration), -advanceRepayDays);
        }
        return date;
    }

    //正常还款本金
    /**
     *
     * @param contractAmount 合同金额
     * @param businessDuration  合同总期数
     * @param repayBusinessDuration 还款期数
     * @param interestRate 利率
     * @param type 还款类型
     * @return
     */
    public static BigDecimal getRepayPrincipal(BigDecimal contractAmount, Integer businessDuration, Integer repayBusinessDuration, Double interestRate, RepayDayType type, ProductType productType) {
        BigDecimal d = BigDecimal.ZERO;
        if(contractAmount == null || interestRate == null || businessDuration == null || businessDuration <= 0 || type == null || repayBusinessDuration == null || repayBusinessDuration <= 0){
            return d;
        }
        if(productType == ProductType.CAR_INSTALMENTS) {
            if (repayBusinessDuration.equals(1)) {
                d = contractAmount.divide(new BigDecimal(businessDuration), 0, BigDecimal.ROUND_DOWN).add(getRemainMoney(contractAmount, businessDuration));
            } else if (businessDuration.equals(repayBusinessDuration)) {//最后一期为零
                d = d;
            } else {
                d = contractAmount.divide(new BigDecimal(businessDuration), 0, BigDecimal.ROUND_DOWN);
            }
        }else{
            if (repayBusinessDuration.equals(1)) {
                d = contractAmount.divide(new BigDecimal(businessDuration), 0, BigDecimal.ROUND_DOWN).add(getRemainMoney(contractAmount, businessDuration));
            } else {
                d = contractAmount.divide(new BigDecimal(businessDuration), 0, BigDecimal.ROUND_DOWN);
            }
        }
        return d;
    }

    //还款利息
    public static BigDecimal getRepayInterest(BigDecimal contractAmount, Integer businessDuration, Integer repayBusinessDuration, Double interestRate, RepayDayType type, ProductType productType) {
        BigDecimal d = BigDecimal.ZERO;
        if(contractAmount == null || interestRate == null || businessDuration == null || businessDuration <= 0 || type == null || repayBusinessDuration == null || repayBusinessDuration <= 0){
            return d;
        }
        if(type == RepayDayType.INITIAL_PAYMENT) {
            if (repayBusinessDuration.equals(1)) {
                d = d;
            } else {
                d = contractAmount.multiply(new BigDecimal(interestRate)).divide(RATIO_ENLARGEMENT).setScale(0, BigDecimal.ROUND_HALF_UP);
            }
        }else{
            d = contractAmount.multiply(new BigDecimal(interestRate)).divide(RATIO_ENLARGEMENT).setScale(0, BigDecimal.ROUND_HALF_UP);
        }
        return d;
    }

    //获取费用
    public static Double getFee(Double money, Double feeRate){
        Double d = ZERO_NUMBER;
        if(money == null || feeRate == null ){
            return d;
        }
        d = new BigDecimal(money).multiply(new BigDecimal(feeRate)).divide(RATIO_ENLARGEMENT).setScale(0, BigDecimal.ROUND_HALF_UP).doubleValue();
        return d;
    }


    //获取申请金额
    public static Integer getApplyMoney(BigDecimal money, Double loanRatio){
        Integer applyMoney = 0;
        if(money == null || loanRatio == null){
            return applyMoney;
        }
        applyMoney = money.multiply(new BigDecimal(loanRatio)).divide(RATIO_ENLARGEMENT).setScale(0,   BigDecimal.ROUND_HALF_UP).intValue();
        return applyMoney;
    }

    /**
     *
     * @param restCapitalAmount: 剩余本金
     * @param repayCapitalAmount: 当期还款本金
     * @param overdueRate: 逾期费率
     * @param overdueDays: 逾期天数
     * @return
     */
    public static Double getOverdueFines(Double restCapitalAmount, Double repayCapitalAmount, Double overdueRate, Integer overdueDays) {
        if(restCapitalAmount == null || repayCapitalAmount == null || overdueRate == null || overdueDays == null) {
            return ZERO_NUMBER;
        }

        BigDecimal surplusCapitalAmount = new BigDecimal(restCapitalAmount).add(new BigDecimal(repayCapitalAmount));
        BigDecimal overdueFines = surplusCapitalAmount.multiply(new BigDecimal(overdueRate))
                .multiply(new BigDecimal(overdueDays)).divide(RATIO_ENLARGEMENT).setScale(0, BigDecimal.ROUND_HALF_UP);
        return overdueFines.doubleValue();

    }

    public static void main(String[] args) {
        BigDecimal bigDecimal = new BigDecimal(1000000);
        BigDecimal coumployDecimal = new BigDecimal(100000);
        BigDecimal taxtbigDecimal = new BigDecimal(200000);
        String s="2017-12-05";
        System.out.print(s.substring(s.length()-2));
        Date beginDate = DateCommonUtils.getToday();
        Date endDate = DateUtils.addMonths(beginDate,12);
        ProductType productTpye = ProductType.POLICY_LOANS;
        DecimalFormat df = new DecimalFormat("#.00");
        Integer i =1;
        Integer m =null;

        System.out.println(i.equals(m));
        Integer compulsoryInsuranceAmount= 991781;
        Integer taxAmount=0;
        Double commercialRisk= taxtbigDecimal.doubleValue();
        Double loanRatio = 9000.0;
        Double rate = 100D;
        BigDecimal bigecimal = new BigDecimal(100000 + 500000).add(new BigDecimal(700000).divide(new BigDecimal(CalculationFormulaUtils.NUMBER), 6, BigDecimal.ROUND_HALF_UP));

        Integer integer = CalculationFormulaUtils.getApplyMoney(new BigDecimal(2100000000).divide(new BigDecimal(CalculationFormulaUtils.NUMBER),6,BigDecimal.ROUND_HALF_UP), loanRatio);
        System.out.println(integer);

    }

}
