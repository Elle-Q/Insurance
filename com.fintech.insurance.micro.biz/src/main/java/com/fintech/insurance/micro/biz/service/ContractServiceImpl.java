package com.fintech.insurance.micro.biz.service;

import com.fintech.insurance.commons.constants.BasicConstants;
import com.fintech.insurance.commons.constants.VoidPlaceHolder;
import com.fintech.insurance.commons.enums.*;
import com.fintech.insurance.commons.exceptions.NullParameterException;
import com.fintech.insurance.commons.utils.CalculationFormulaUtils;
import com.fintech.insurance.commons.utils.DateCommonUtils;
import com.fintech.insurance.commons.utils.NumberFormatorUtils;
import com.fintech.insurance.commons.utils.Pagination;
import com.fintech.insurance.commons.web.FintechResponse;
import com.fintech.insurance.components.cache.RedisSequenceFactory;
import com.fintech.insurance.components.exception.FInsuranceBaseException;
import com.fintech.insurance.components.web.context.FInsuranceApplicationContext;
import com.fintech.insurance.micro.biz.persist.dao.ContractDao;
import com.fintech.insurance.micro.biz.persist.dao.RequisitionDao;
import com.fintech.insurance.micro.biz.persist.dao.RequisitionDetailDao;
import com.fintech.insurance.micro.biz.persist.entity.*;
import com.fintech.insurance.micro.biz.service.contract.ContractGeneratorService;
import com.fintech.insurance.micro.dto.biz.*;
import com.fintech.insurance.micro.dto.customer.CustomerVO;
import com.fintech.insurance.micro.dto.finance.FinanceRepaymentPlanVO;
import com.fintech.insurance.micro.dto.system.UserVO;
import com.fintech.insurance.micro.dto.thirdparty.ContractInfoResponseVO;
import com.fintech.insurance.micro.feign.customer.CustomerServiceFeign;
import com.fintech.insurance.micro.feign.finance.RefundServiceFeign;
import com.fintech.insurance.micro.feign.finance.RepaymentPlanServiceFeign;
import com.fintech.insurance.micro.feign.retrieval.BizQueryFeign;
import com.fintech.insurance.micro.feign.system.SysUserServiceFeign;
import com.fintech.insurance.micro.feign.thirdparty.QiniuBusinessServiceFeign;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import sun.rmi.runtime.Log;

import java.math.BigDecimal;
import java.util.*;
import java.util.concurrent.locks.ReentrantLock;

/**
 * @Description: (some words)
 * @Author: yongneng liu
 * @Date: 2017/11/14 0014 16:07
 */
@Service
@Transactional
public class ContractServiceImpl implements ContractService {

    private static final Logger LOG = LoggerFactory.getLogger(ContractServiceImpl.class);
    private static final String SYMBOL = "%";
    private static final String REDIS_KEY_CONTRACT_SERIAL_NUM = "redis_contract_serial_num";
    private static final int IS_PUBLIC = 0;//??????
    private final static ReentrantLock LOCK = new ReentrantLock();

    @Autowired
    private ContractDao contractDao;
    @Autowired
    private SysUserServiceFeign sysUserServiceFeign;
    @Autowired
    private RefundServiceFeign refundServiceFeign;
    @Autowired
    private RequisitionDetailDao requisitionDetailDao;
    @Autowired
    private BizQueryFeign bizQueryFeign;
    @Autowired
    private RequisitionDao requisitionDao;
    @Autowired
    private CustomerServiceFeign customerServiceFeign;
    @Autowired
    private ProductRateService productRateService;
    @Autowired
    private RedisSequenceFactory redisSequenceFactory;
    @Autowired
    private QiniuBusinessServiceFeign qiniuBusinessServiceFeign;
    @Autowired
    private RepaymentPlanServiceFeign repaymentPlanServiceFeign;
    @Autowired
    protected RedisTemplate<String, String> redisTemplate;
    @Autowired
    @Qualifier("ckContractGenerator")
    private ContractGeneratorService ckContractGenerator;
    @Autowired
    @Qualifier("dkContractGenerator")
    private ContractGeneratorService dkContractGenerator;

    @Override
    @Transactional(readOnly = true)
    public ContractVO getContractById(Integer id) {
        if (id == null) {
            throw new IllegalArgumentException("Null id!");
        }

        Contract contract = contractDao.getContractById(id);
        if (contract == null) {
            LOG.error("Unmatched contractId [" + contract + "] of Contract!");
            throw new IllegalStateException("Unmatched contractId [" + contract + "] of Contract!");
        }

        ContractVO contractVO = this.convertContractVO(contract);

        //?????????????????????
        if (contract.getRequisition() != null) {
            //????????????????????????
            if (contract.getRequisition().getChannelUserId() != null) {
                UserVO userVO = sysUserServiceFeign.getUserById(contract.getRequisition().getChannelUserId()).getData();
                if (userVO != null) {
                    contractVO.setChannelUserName(userVO.getName());
                    contractVO.setChannelUserMobile(userVO.getMobile());
                    // TODO Add other data if necessary
                }
            }

            //??????????????????
            if (contract.getRequisition().getCustomerId() != null && contract.getRequisition().getChannel() != null) {
                FintechResponse<CustomerVO> customerVOResponse = customerServiceFeign.getByCustomerIdAndChannelCode(contract.getRequisition().getCustomerId(), contract.getChannel().getChannelCode());
                if (customerVOResponse != null && customerVOResponse.getData() != null) {
                    contractVO.setCustomerName(customerVOResponse.getData().getName());
                    contractVO.setCustomerMobile(customerVOResponse.getData().getPhone());
                    // TODO Add other data if necessary
                }
            }
        }

        return contractVO;
    }

    //?????????vo
    private ContractVO convertContractVO(Contract entity) {
        ContractVO vo = new ContractVO();
        if (entity == null) {
            return vo;
        }
        vo.setContractId(entity.getId());
        vo.setContractCode(entity.getContractNumber());
        vo.setBorrowAmount(entity.getContractAmount().doubleValue());
        if (entity.getRequisition() != null) {
            Requisition requisition = entity.getRequisition();
            vo.setRequisitionId(requisition.getId());
            vo.setRequisitionNumber(requisition.getRequisitionNumber());
            vo.setProductType(requisition.getProductType());
            vo.setMaxOverdueDays(requisition.getMaxOverdueDays());
            Product product = requisition.getProduct();
            if(product != null) {
                Set<ProductRate> productRateList = product.getProductRateSet();
                for(ProductRate rate : productRateList){
                    if(rate.getBusinessDuration().equals(entity.getBusinessDuration())) {
                        // ???????????????
                        vo.setInterestRate(rate.getInterestRate());
                        break;
                    }
                }
                vo.setPrepaymentDays(requisition.getPrepaymentDays());
                vo.setRepayDayType(requisition.getRepayDayType());
            }
        }
        // ???????????????
        List<RequisitionDetail> requisitionDetails = requisitionDetailDao.getByContract_Id(entity.getId());
        if (requisitionDetails != null && requisitionDetails.size() > 0) {
            vo.setCarNumber(requisitionDetails.get(0).getCarNumber());
        }
        vo.setCustomerId(entity.getCustomerId());
        vo.setServiceRate(entity.getRequisition().getServiceFeeRate() + entity.getRequisition().getOtherFeeRate());
        if (entity.getChannel() != null) {
            vo.setChannelCode(entity.getChannel().getChannelCode());
            vo.setChannelName(entity.getChannel().getChannelName());
        }
        // ????????????
        vo.setBorrowAmount(entity.getContractAmount().doubleValue());
        //????????????
        vo.setOverdueFineRate(entity.getRequisition().getOverdueFineRate());
        return vo;
    }

    @Override
    @Transactional(readOnly = true)
    public Pagination<ContractVO> pageContract(String contractCode, ContractStatus contractStatus, String channelName, String customerName,
                                               String carNumber, Date loadBeginDate, Date loadEndDate, int pageIndex, int pageSize) {
        Long begin = null;
        Long end = null;
        if (loadBeginDate != null) {
            begin = loadBeginDate.getTime();
        }
        if (loadEndDate != null) {
            end = loadEndDate.getTime();
        }

        FintechResponse<Pagination<ContractVO>> response =  bizQueryFeign.pageContractVOByNativeSQL(contractCode, contractStatus, channelName, customerName, carNumber, begin, end, pageIndex, pageSize);

        if (!response.isOk()) {
            throw  FInsuranceBaseException.buildFromErrorResponse(response);
        }

        return response.getData();
    }
    @Override
    @Transactional(readOnly = true)
    public ContractDetailVO getDetailByContractNumber(String contractNumber) {
        ContractDetailVO detailVO = new ContractDetailVO();

        Contract contract = contractDao.getContractInfoByContractNo(contractNumber);
        detailVO.setContractId(contract.getId());
        FintechResponse<CustomerVO> customerVOResponse = customerServiceFeign.getByCustomerIdAndChannelCode(contract.getCustomerId(), contract.getChannel().getChannelCode());
        if (!customerVOResponse.isOk() || customerVOResponse == null || customerVOResponse.getData() == null) {
            LOG.error("??????????????????");
            return detailVO;
        }
        detailVO.setCustomerName(customerVOResponse.getData().getName());
        // ??????????????????
        detailVO.setCustomerMobile(customerVOResponse.getData().getPhone());

        // ????????????????????????
        ContractBaseDetailVO baseDetailVO = this.getContractBaseDetailVOByContract(contract);
        detailVO.setContractBaseDetailVO(baseDetailVO);

        // ????????????
        ProductVO productVO = this.getProductVOByContract(contract);
        detailVO.setProductVO(productVO);

        // ????????????
        List<RefundVO> refundVOS = this.getRefundVOListByContract(contract);
        detailVO.setRefundVOList(refundVOS);

        return detailVO;
    }

    private ContractBaseDetailVO getContractBaseDetailVOByContract(Contract contract) {
        ContractBaseDetailVO baseDetailVO = new ContractBaseDetailVO();
        baseDetailVO.setContractId(contract.getId());
        // ????????????
        baseDetailVO.setContractCode(contract.getContractNumber());
        // ??????????????????
        baseDetailVO.setCustomerContractNumber(contract.getCustomerContractNumber());
        // ????????????
        // ?????????
        // ???????????????????????????????????????????????????????????????????????????????????????
        List<RequisitionDetail> carList = requisitionDetailDao.listRequisitionDetail(contract.getId());
        if (carList == null || carList.size() < 1) {
            baseDetailVO.setCarCount(0);
            baseDetailVO.setInsuranceCount(0);
        } else {
            baseDetailVO.setCarCount(carList.size());
            baseDetailVO.setCarNumber(carList.get(0).getCarNumber());
            baseDetailVO.setDrivingLicense(carList.get(0).getDrivingLicense());
            int num = carList.size();
            for (RequisitionDetail detail : carList) {
                if (StringUtils.isNotBlank(detail.getCompulsoryInsuranceNumber())) {
                    num ++;
                }
            }
            baseDetailVO.setInsuranceCount(num);
        }
        // ????????????
        if (contract.getRequisition() != null) {
            // ????????????
            if (contract.getRequisition().getProductType().equals(ProductType.CAR_INSTALMENTS.getCode())) {
                baseDetailVO.setContractName(contract.getBusinessDuration() + ContractBaseDetailVO.CONTRACT_CAR_INSTALMENTS_NAME);
                baseDetailVO.setServiceContractName(contract.getBusinessDuration() + ContractBaseDetailVO.SERVICE_CONTRACT_CAR_INSTALMENTS_NAME);
            } else {
                baseDetailVO.setContractName(contract.getBusinessDuration() + ContractBaseDetailVO.CONTRACT_POLICY_LOANS_NAME);
                baseDetailVO.setServiceContractName(contract.getBusinessDuration() + ContractBaseDetailVO.SERVICE_CONTRACT_POLICY_LOANS_NAME);
            }
            // ????????????url
            // ??????PDF?????????????????????url
            baseDetailVO.setContractUrl(contract.getContentOriginFile());
            // ????????????url
            // ??????PDF?????????????????????url
            baseDetailVO.setServiceContractUrl(contract.getServiceContentOriginFile());
            baseDetailVO.setRequisitionNumber(contract.getRequisition().getRequisitionNumber());
            baseDetailVO.setRequisitionId(contract.getRequisition().getId());
        }
        if (contract.getChannel() != null) {
            // ????????????
            baseDetailVO.setChannelCode(contract.getChannel().getChannelCode());
            baseDetailVO.setChannelName(contract.getChannel().getChannelName());
        }
        // ????????????
        baseDetailVO.setBorrowAmount(contract.getContractAmount().doubleValue());
        // ?????????
        baseDetailVO.setTotalPhase(contract.getBusinessDuration());
        // ????????????
        baseDetailVO.setBorrowBeginDate(contract.getStartDate());
        baseDetailVO.setBorrowEndDate(contract.getEndDate());
        // (?????????+?????????)/?????????
        double compulsory = 0.0;
        double travelTax = 0.0;
        List<RequisitionDetail> detailList = requisitionDetailDao.getByContract_Id(contract.getId());
        for (RequisitionDetail detail : detailList) {
            compulsory += detail.getCompulsoryInsuranceAmount();
            travelTax += detail.getTaxAmount();
        }
        baseDetailVO.setRate(new BigDecimal(compulsory + travelTax).divide(contract.getContractAmount(), 4, BigDecimal.ROUND_HALF_UP).doubleValue()* 100 );

        return baseDetailVO;
    }

    private ProductVO getProductVOByContract(Contract contract) {
        ProductVO productVO = new ProductVO();
        if (contract.getRequisition() != null) {
            // ????????????
            productVO.setProductType(contract.getRequisition().getProductType());
            // ????????????
            productVO.setProductName(contract.getRequisition().getProduct().getProductName());
            // ????????????
            productVO.setRepayType(contract.getRequisition().getRepayType());
            // ????????????
            productVO.setServiceFeeRate(contract.getRequisition().getServiceFeeRate());
            // ?????????
            productVO.setInterestRate(contract.getInterestRate());
            // ????????????
            productVO.setOtherFeeRate(contract.getRequisition().getOtherFeeRate());
            // ?????????????????????
            productVO.setPrepaymentPenaltyRate(contract.getRequisition().getPrepaymentPenaltyRate());
            // ?????????????????????
            productVO.setPrepaymentDays(contract.getRequisition().getPrepaymentDays());
            // ??????????????????
            productVO.setMaxOverdueDays(contract.getRequisition().getMaxOverdueDays());
            // ????????????????????????
            productVO.setOverdueFineRate(contract.getRequisition().getOverdueFineRate());
            // ????????????
            productVO.setLoanRatio(contract.getLoanRatio());
        }
        return productVO;
    }

    private List<RefundVO> getRefundVOListByContract(Contract contract) {
        List<RefundVO> refundVOS = new ArrayList<RefundVO>();
        if(StringUtils.isBlank(contract.getCustomerContractNumber())){
            //??????????????????????????????????????????
            LOG.error("getRefundVOListByContract failed  null CustomerContractNumber with contractId=[" + contract.getId() + "]");
            return refundVOS;
        }
        FintechResponse<Pagination<RefundVO>> refundVOPageResponse = bizQueryFeign.pageRefundVO(contract.getCustomerContractNumber(), null, null,
                null, null, null, null, 1, contract.getBusinessDuration());
        if(!refundVOPageResponse.isOk()){
            throw FInsuranceBaseException.buildFromErrorResponse(refundVOPageResponse);
        }
        if(refundVOPageResponse.getData() != null) {
            refundVOS = refundVOPageResponse.getData().getItems();
        }
        return refundVOS;
    }

    @Override
    @Transactional(readOnly = true)
    public List<ContractVO> getContractByRequisitionNumberAndCustomerName(String requisitionNumber, String customerName) {
        List<Integer> customerIds = new ArrayList<>();
        // ??????????????????????????????id
        FintechResponse<List<CustomerVO>> customersResponse = customerServiceFeign.listCustomerByName(customerName);
        for (CustomerVO vo : customersResponse.getData()) {
            customerIds.add(vo.getAccountId());
        }


        List<Contract> list = contractDao.getByRequisitionNumberAndCustomerIds(requisitionNumber, customerIds);
        if (list == null) {
            return Collections.emptyList();
        }
        List<ContractVO> resultList = new ArrayList<>();

        for (Contract contract : list) {
            ContractVO vo = new ContractVO();
            vo.setContractId(contract.getId());
            vo.setContractCode(contract.getContractNumber());
            if (contract.getRequisition() != null)
            {
                vo.setProductType(contract.getRequisition().getProductType());
                vo.setRequisitionId(contract.getRequisition().getId());
                vo.setRequisitionNumber(contract.getRequisition().getRequisitionNumber());
                vo.setMaxOverdueDays(contract.getRequisition().getMaxOverdueDays());
            }
            resultList.add(vo);
        }
        return resultList;
    }

    @Override
    @Transactional(readOnly = true)
    public RequisitionVO getRequisitionByContractNumber(String contractNumber) {
        Contract contract = contractDao.getContractInfoByContractNo(contractNumber);

        if (contract == null || contract.getRequisition() == null) {
            return null;
        }
        Requisition requisition = contract.getRequisition();
        RequisitionVO vo = new RequisitionVO();
        vo.setOverdueFineRate(requisition.getOverdueFineRate());
        return vo;
    }

    @Override
    public List<Map<String,Object>> convertListToMap(List<BizReportVO> voList, ProductType type, ContractStatus status) {
        List<Map<String,Object>> mapList = new ArrayList<Map<String,Object>>();
        if(voList !=null && voList.size() > 0 ){
            for (BizReportVO vo : voList) {
                mapList.add(convertListToMap(vo, type ,status));
            }
        }
        return mapList;
    }

    @Override
    @Transactional(readOnly = true)
    public Pagination<BizReportVO> queryAllBizReportContract(String channelCode, List<Integer> customerIds, Date startTime, Date endTime, ProductType type,
                                                             Integer companyId, ContractStatus status, Integer pageIndex, Integer pageSize) {
        Page<Contract> page = contractDao.findAllContract( channelCode, customerIds, startTime, endTime, type, companyId, status , 1, Integer.MAX_VALUE);
        List<BizReportVO> voList = new ArrayList<BizReportVO>();
        if(page !=null && page.getContent() != null ){
            for (Contract contract : page.getContent()) {
                voList.add(convertToBizReportVO(contract, type ,status));
            }
        }
        return new Pagination<BizReportVO>(pageIndex, pageSize , page.getTotalElements(), voList);
    }

    @Override
    @Transactional(readOnly = true)
    public Pagination<ContractVO> pageContractVOByContractStatus(ContractStatus contractStatus, Integer pageIndex, Integer pageSize) {
        Page<Contract> page = null;
        String contractStatusCode = contractStatus == null ? "" : contractStatus.getCode();
        if (FInsuranceApplicationContext.isCustomer()) {
            //??????????????????
            Integer customerId = FInsuranceApplicationContext.getCurrentUserId();
            LOG.info("pageContractVOByContractStatus with customerId=[" + customerId +"],contractStatusCode=[" + contractStatusCode + "]" );
            // ?????????????????????????????????????????????id??????????????????
            page = contractDao.pageContractByCustomerIdAndContractStatus(customerId, contractStatus, pageIndex, pageSize);
        } else if (FInsuranceApplicationContext.isCurrentUserChannelAdmin()) {
            //?????????????????????
            // ??????channel_code???????????????????????????????????????
            String channelCode = FInsuranceApplicationContext.getCurrentUserChannelCode();
            LOG.info("pageContractVOByContractStatus with channelCode=[" + channelCode +"],contractStatusCode=[" + contractStatusCode + "]" );
            page = contractDao.findAllContract(channelCode, null, null, null, null, null, contractStatus, pageIndex, pageSize);
        } else {
            // ???????????????????????????????????????????????????busi_requisition???channel_user_id??????????????????????????????????????????????????????
            Integer channelUserId = FInsuranceApplicationContext.getCurrentUserId();
            LOG.info("pageContractVOByContractStatus with channelUserId=[" + channelUserId +"],contractStatusCode=[" + contractStatusCode + "]" );
            page = contractDao.pageContractByNormalChannelUserIdAndContractStatus(channelUserId, contractStatus, pageIndex, pageSize);
        }

        if (page == null || page.getContent() == null) {
            return new Pagination<ContractVO>();
        }
        List<Contract> contractList = page.getContent();
        List<ContractVO> resultList = contractVOList(contractList);
        return Pagination.createInstance(pageIndex, pageSize, page.getTotalElements(), resultList);
    }

    @Override
    public  Contract createContract(Requisition requisition, RequisitionDetail requisitionDetail, Integer month){
        Contract c = new Contract();
        c.setRequisition(requisition);
        //????????????
        c.setChannel(requisition.getChannel());
        //?????????????????????id?????????customer_account????????????
        c.setCustomerId(requisition.getCustomerId());
        //???????????????
        c.setCustomerContractNumber(null);// ???????????????????????????????????????.

        ProductRate rate = productRateService.getProductRate(requisitionDetail, month);
        if (null == rate) {
            throw new FInsuranceBaseException(104526, new Object[]{requisition.getProduct().getId(), month});
        }
        //????????????
        ProductType productType = ProductType.codeOf(requisition.getProductType());
        //?????????????????????????????????
        c.setContractAmount(CalculationFormulaUtils.getVehicleLoanAmount(requisitionDetail.getCompulsoryInsuranceAmount(), requisitionDetail.getTaxAmount(), requisitionDetail.getCommercialInsuranceValue() , rate.getLoanRatio(), productType));
        //
        //???????????????????????????????????????
        c.setInterestType(rate.getInterestType());
        //?????????????????????
        c.setInterestRate(rate.getInterestRate());
        //????????????????????????
        c.setLoanRatio(rate.getLoanRatio());
        //?????????????????????
        c.setBusinessDuration(rate.getBusinessDuration());
        //????????????
        c.setContractStatus(ContractStatus.Init.getCode());
        //?????????????????????
        c.setStartDate(DateCommonUtils.getToday());
        //?????????????????????
        //????????????????????? ????????????????????????????????????????????????????????????????????????1???20?????????????????????3??????????????????4???19?????????????????????3???31?????????????????????3??????????????????6???29?????????????????????10???31?????????????????????4????????????????????????2??????????????????????????????
        c.setEndDate(new org.joda.time.DateTime(c.getStartDate()).plusMonths(c.getBusinessDuration()).plusDays(-1).toDate());

        //?????????
        c.setContractNumber(redisSequenceFactory.generateSerialNumber(BizCategory.SN));
        //this.generateContractSerialNum(requisition.getProduct().getProductCode(), requisition.getChannel().getChannelCode(), c.getBusinessDuration(), DateCommonUtils.getToday())
        return c;
    }

    @Override
    public ContractInfoVO buildContractInfoVO(Requisition requisition, List<RequisitionDetail> detailList) {
        ContractInfoVO contractInfoVO = new ContractInfoVO();
        // ????????????
        BigDecimal brrowAmount = new BigDecimal(0);

        // ?????????
        BigDecimal serviceFee = new BigDecimal(0);

        // ?????????????????????
        BigDecimal firstRepayMoney = new BigDecimal(0);

        // ?????????
        BigDecimal  assureMoney =new BigDecimal(0);

        List<ContractRepayVO> contractRepayVOList = new ArrayList<ContractRepayVO>();

        for(RequisitionDetail detail : detailList) {
            try {
                Contract contract = detail.getContract();
                if(contract == null || contract.getBusinessDuration() == null || contract.getBusinessDuration() <= 0 ){
                    continue;
                }
                BigDecimal contractAmount = contract.getContractAmount();
                brrowAmount  = brrowAmount.add(contractAmount);
                serviceFee = serviceFee.add(CalculationFormulaUtils.getServiceFee(contract.getContractAmount(),detail.getRequisition().getServiceFeeRate() + detail.getRequisition().getOtherFeeRate()));
                String repayDayTypeStr = detail.getRequisition().getRepayDayType();
                RepayDayType repayDayType = null;
                if(StringUtils.isNoneBlank(repayDayTypeStr)) {
                    repayDayType = RepayDayType.codeOf(repayDayTypeStr);
                }
                //?????????????????????????????????
                if(repayDayType == RepayDayType.INITIAL_PAYMENT){
                    firstRepayMoney = firstRepayMoney.add(CalculationFormulaUtils.getFirstRepayMoney(contractAmount, contract.getBusinessDuration(), contract.getInterestRate(), repayDayType));
                }
                if(StringUtils.equals(contract.getRequisition().getProductType(), ProductType.CAR_INSTALMENTS.getCode())) {
                    assureMoney = assureMoney.add(CalculationFormulaUtils.getAssureMoney(contract.getContractAmount(), contract.getBusinessDuration()));
                }
                ContractRepayVO contractRepayVO = new ContractRepayVO();
                // ????????????
                contractRepayVO.setMonth(contract.getBusinessDuration());
                // ?????????
                contractRepayVO.setCarNumber(detail.getCarNumber());
                // ??????????????????
                contractRepayVO.setBorrowAmount(contractAmount.doubleValue());
                // ??????????????????
                contractRepayVO.setEachRepayAmount(CalculationFormulaUtils.getEachRepayAmount(contract.getContractAmount(), contract.getBusinessDuration(), contract.getInterestRate()).doubleValue());
                // ??????????????????
                contractRepayVO.setContractFileUrl(contract.getContentShowFile());
                // ????????????????????????
                contractRepayVO.setServiceContractFileUrl(contract.getServiceContentShowFile());
                // ????????????
                String productType = detail.getRequisition().getProductType();
                ProductType type = null;
                if(StringUtils.isNoneBlank(productType)){
                    type = ProductType.codeOf(productType);
                }
                contractRepayVO.setContractId(contract.getId());
                contractRepayVO.setContractName(contract.getBusinessDuration()+"??????" + type == null ? "": type.getDesc() + "??????");
                contractRepayVO.setCreateAt(contract.getCreateAt());
                contractRepayVOList.add(contractRepayVO);
            }catch (NullParameterException e) {
                throw new FInsuranceBaseException(104107);
            }
        }
        // ????????????
        contractInfoVO.setBrrowAmount(brrowAmount.doubleValue());
        contractInfoVO.setServiceFee(serviceFee.doubleValue());
        contractInfoVO.setFirstRepayMoney(firstRepayMoney.doubleValue());
        contractInfoVO.setAssureMoney(assureMoney.doubleValue());
        Collections.sort(contractRepayVOList, new Comparator<ContractRepayVO>() {
            @Override
            public int compare(ContractRepayVO o1, ContractRepayVO o2) {
                if(o1.getMonth().equals(o2.getMonth())){
                    return o1.getCreateAt().compareTo(o2.getCreateAt());
                }else {
                    return o1.getMonth().compareTo(o2.getMonth());
                }
            }
        });
        contractInfoVO.setContractRepayVOList(contractRepayVOList);
        return contractInfoVO;
    }

    @Override
    public Integer saveContract(Contract contract) {
        contractDao.save(contract);
        return contract.getId();
    }


    @Transactional(readOnly = true)
    public InstalmentDetailVO getWeChatContractDetailByContractId(Integer contractId) {
        Contract contract = contractDao.getContractById(contractId);
        if (contract == null) {
            throw new FInsuranceBaseException(104908, new Object[]{"id = " + contractId});
        }

        // ????????????????????????????????????????????????
        /*
        if (BaseFintechWechatController.isCustomer() && BaseFintechWechatController.getCurrentUserId() != contract.getCustomerId()) {
            throw new BaseException(105927);
        } else {
            String channelCode = BaseFintechWechatController.getCurrentUserChannelCode();
            if (contract.getChannel() != null && !StringUtils.equals(contract.getChannel().getChannelCode(), channelCode)) {
                throw new BaseException(105927);
            }
            // ??????????????????
            if (!BaseFintechWechatController.isCurrentUserChannelAdmin()) {
                if (contract.getRequisition() != null && contract.getRequisition().getChannelUserId() != BaseFintechWechatController.getCurrentUserId()) {
                    throw new BaseException(105927);
                }
            }
        }
        */
        InstalmentDetailVO instalmentDetailVO = new InstalmentDetailVO();
        instalmentDetailVO.setContractId(contract.getId());
        instalmentDetailVO.setContractNumber(contract.getContractNumber());
        instalmentDetailVO.setContractUrl(contract.getContentShowFile());
        instalmentDetailVO.setServiceUrl(contract.getServiceContentShowFile());
        instalmentDetailVO.setContractStatus(contract.getContractStatus());
        instalmentDetailVO.setTotalInstalment(contract.getBusinessDuration());
        instalmentDetailVO.setCustomerId(contract.getCustomerId());

        Requisition requisition = contract.getRequisition();
        if(requisition == null){
            LOG.error("getWeChatContractDetailByContractId error  null requisition with id = [" + contract.getId() + "]");
            throw new FInsuranceBaseException(104904, new Object[]{"contractId="+ contract.getId()});
        }
        FintechResponse<List<FinanceRepaymentPlanVO>> response = refundServiceFeign.findRepaymentListByContract(contract.getContractNumber(), requisition.getMaxOverdueDays(), requisition.getOverdueFineRate());
        if (!response.isOk()) {
            throw new FInsuranceBaseException(105008);
        }
        if (response.getData() == null || response.getData().size() < 1) {
            LOG.error("getWeChatContractDetailByContractId error  null repaymentPlanVO with id = [" + contract.getId() + "]");
            throw new FInsuranceBaseException(101516);
        }
        List<FinanceRepaymentPlanVO> planVOList = response.getData();
        // ??????????????????
        Double surplusAmount = 0.0;
        if(StringUtils.equals(contract.getContractStatus() ,ContractStatus.Refunding.getCode()) ||
                StringUtils.equals(contract.getContractStatus() ,ContractStatus.InsuranceReturning.getCode())) {
            for (FinanceRepaymentPlanVO repaymentPlanVO : planVOList) {
                if (repaymentPlanVO.getRepayStatus() == RefundStatus.WAITING_REFUND ||
                        repaymentPlanVO.getRepayStatus() == RefundStatus.FAIL_REFUND ||
                        repaymentPlanVO.getRepayStatus() == RefundStatus.OVERDUE ||
                        repaymentPlanVO.getRepayStatus() == RefundStatus.WAITING_WITHDRAW) {
                    surplusAmount += repaymentPlanVO.getRepayTotalAmount() + repaymentPlanVO.getRepayInterestAmount() + repaymentPlanVO.getOverdueFine();
                }
            }
        }
        FinanceRepaymentPlanVO planVO = null;
        for (int i = 0; i <  planVOList.size(); i++) {
            planVO = planVOList.get(i);
            if (planVO.getRepayStatus() == RefundStatus.INIT_REFUND) {
                //??????????????????
                return null;
            }
            if (planVO.getRepayStatus() != RefundStatus.HAS_REFUND) {
                break;
            }
        }

        instalmentDetailVO.setRefundStatus(planVO.getRepayStatus().getCode());
        // ???????????????true?????????false?????????
        instalmentDetailVO.setOverdueFlag(planVO.getOverdueFlag());
        // ????????????
        instalmentDetailVO.setOverdueDays(planVO.getOverdueDays());

        instalmentDetailVO.setSurplusAmount(surplusAmount);
        instalmentDetailVO.setRequisitionId(requisition.getId());
        instalmentDetailVO.setRequisitionNumber(requisition.getRequisitionNumber());
        // ??????????????????
        Integer customerAccountInfoId = requisition.getCustomerAccountInfoId();
        FintechResponse<CustomerVO> customerVOFintechResponse = customerServiceFeign.getCustomerAccountInfoById(customerAccountInfoId);
        instalmentDetailVO.setEnterpriseName(customerVOFintechResponse.getData().getEnterpriseName());
        //???????????????
        instalmentDetailVO.setRepayTotalAmount(contract.getContractAmount().doubleValue());
        instalmentDetailVO.setRepayDate(planVO.getRepayDate());

        // ??????????????????
        instalmentDetailVO.setInstalmentBeginDate(contract.getStartDate());
        // ??????????????????
        instalmentDetailVO.setInstalmentEndDate(contract.getEndDate());

        // ???????????????
        List<RequisitionDetail> detailList = requisitionDetailDao.getByContract_Id(contract.getId());
        if (detailList != null) {
            Integer totalInsurance = detailList.size();
            for (RequisitionDetail detail : detailList) {
                if (StringUtils.isNotBlank(detail.getCompulsoryInsuranceNumber())) {
                    totalInsurance ++;
                }
            }
            instalmentDetailVO.setCarNumber(detailList.get(0).getCarNumber());
            instalmentDetailVO.setTotalInsurance(totalInsurance);
            instalmentDetailVO.setCarCount(detailList.size());
        }

        Integer currentInstalment = null;
         if (planVO.getRepayStatus() == RefundStatus.HAS_REFUND){
            currentInstalment = planVO.getCurrentInstalment();
        }else {
             currentInstalment = planVO.getCurrentInstalment() - 1;
         }
        instalmentDetailVO.setCurrentInstalment(currentInstalment);
        instalmentDetailVO.setRepayType(requisition.getRepayType());
        instalmentDetailVO.setRepayDayType(requisition.getRepayDayType());
        instalmentDetailVO.setLoanTime(requisition.getLoanTime());
        Channel channel = contract.getChannel();
        instalmentDetailVO.setChannelCode(channel == null ? null : channel.getChannelCode());
        instalmentDetailVO.setChannelName(channel == null ? null : channel.getChannelName());
        return instalmentDetailVO;
    }

    private  BizReportVO convertToBizReportVO(Contract contract, ProductType type, ContractStatus status){
        BizReportVO vo = null;
        if(contract == null){
            return vo;
        }
        vo = new BizReportVO();
        //??????????????????
        if (contract.getRequisition().getCustomerId() != null && contract.getRequisition().getChannel() != null) {
            FintechResponse<CustomerVO> customerVOResponse = customerServiceFeign.getByCustomerIdAndChannelCode(contract.getRequisition().getCustomerId(), contract.getChannel().getChannelCode());
            if (customerVOResponse != null && customerVOResponse.getData() != null) {
                vo.setCustomerName(customerVOResponse.getData().getName());
                vo.setCompanyName(customerVOResponse.getData().getCompanyOf());
            }
        }
        ProductType productType = type;
        if(productType == null ){
            productType = ProductType.codeOf(contract.getRequisition().getProduct().getProductType());
        }
        vo.setContractNumber(contract.getContractNumber());
        vo.setChannelCode(contract.getChannel().getChannelCode());
        vo.setChannelName(contract.getChannel().getChannelName());
        vo.setRequisitionNumber(contract.getRequisition().getRequisitionNumber());
        vo.setProductTypeCode(productType != null ? productType.getCode() : null );
        vo.setBorrowAmount(NumberFormatorUtils.convertFinanceNumberToShowString(contract.getContractAmount().doubleValue()));
        vo.setCreateTime(contract.getCreateAt());
        vo.setInterestRate(NumberFormatorUtils.convertFinanceNumberToShowString(contract.getInterestRate()) + SYMBOL);
        vo.setServiceFeeRate(NumberFormatorUtils.convertFinanceNumberToShowString(contract.getRequisition().getServiceFeeRate()) + SYMBOL);
        vo.setOtherFeeRate(NumberFormatorUtils.convertFinanceNumberToShowString(contract.getRequisition().getOtherFeeRate()) + SYMBOL);
        vo.setBorrowFeeAmount(NumberFormatorUtils.convertFinanceNumberToShowString((contract.getInterestRate()+contract.getRequisition().getServiceFeeRate() * contract.getContractAmount().doubleValue()) / 10000));
        ContractStatus contractStatus = status;
        if(contractStatus == null && StringUtils.isNoneBlank(contract.getContractStatus())){
            contractStatus = ContractStatus.codeOf(contract.getContractStatus());
        }
        vo.setContractStatusCode(contractStatus == null ? null : contractStatus.getCode());
        return vo;
    }

    //list???map
    private  Map<String,Object> convertListToMap(BizReportVO reportVO, ProductType type, ContractStatus status){
        Map<String,Object> map = new HashMap<String,Object>();
        if(reportVO == null){
            return map;
        }
        //??????????????????
        map.put("customerName", reportVO.getCustomerName() == null ? "" : reportVO.getCustomerName());
        map.put("companyName", reportVO.getCompanyName() == null ? "" : reportVO.getCompanyName());
        ProductType productType = type;
        if(productType == null && StringUtils.isNoneBlank(reportVO.getProductTypeCode())){
            productType = ProductType.codeOf(reportVO.getProductTypeCode());
        }
        map.put("contractNumber",reportVO.getContractNumber());
        map.put("channelCode",reportVO.getChannelCode());
        map.put("channelName",reportVO.getChannelName());
        map.put("requisitionNumber",reportVO.getRequisitionNumber());
        map.put("productTypeDesc",productType != null ? productType.getDesc() : "" );;
        map.put("borrowAmount", reportVO.getBorrowAmount());
        map.put("createTime", DateCommonUtils.formatTime(reportVO.getCreateTime()));
        map.put("interestRate",reportVO.getInterestRate());
        map.put("serviceFeeRate",reportVO.getServiceFeeRate());
        map.put("otherFeeRate",reportVO.getOtherFeeRate());
        map.put("borrowFeeAmount",reportVO.getBorrowFeeAmount());
        map.put("carNumber",reportVO.getCarNumber());
        ContractStatus contractStatus = status;
        if(contractStatus == null && StringUtils.isNoneBlank(reportVO.getContractStatusCode())){
            contractStatus = ContractStatus.codeOf(reportVO.getContractStatusCode());
        }
        map.put("contractStatusDesc", contractStatus == null ? "" : contractStatus.getDesc());
        return map;
    }

    /**
     * ??????????????????????????????????????????????????????????????????????????????????????????????????????
      */

    private List<ContractVO> contractVOList(List<Contract> entities) {
        if (entities == null) {
            return Collections.EMPTY_LIST;
        }
        List<ContractVO> resultList = new ArrayList<ContractVO>();
        for (Contract contract : entities) {
            ContractVO contractVO = convertContractToContractVO(contract);
            if(contractVO != null) {
                resultList.add(contractVO);
            }
        }
        return resultList;
    }

    /**
     * ??????????????????????????????????????????????????????????????????????????????????????????
     */
    private ContractVO convertContractToContractVO(Contract entity) {
        if (entity == null) {
            return null;
        }
        ContractVO vo = new ContractVO();
        vo.setContractId(entity.getId());
        vo.setContractCode(entity.getContractNumber());
        if (entity.getChannel() != null) {
            vo.setChannelName(entity.getChannel().getChannelName());
        }
        vo.setContractStatus(entity.getContractStatus());
        vo.setTotalPhase(entity.getBusinessDuration());

        // ???????????????
        List<RequisitionDetail> requisitionDetails = null;
        if(entity.getId() != null) {
            requisitionDetails = requisitionDetailDao.getByContract_Id(entity.getId());
        }
        if (requisitionDetails != null && requisitionDetails.size() > 0) {
            vo.setCarNumber(requisitionDetails.get(0).getCarNumber());
        }
        if(StringUtils.isBlank(entity.getContractNumber())){
            LOG.error("convertContractToContractVO error  null ContractNumber with id = [" + entity.getId() + "]");
            return null;
        }
        // ????????????
        Double amount = entity.getContractAmount().doubleValue();
        vo.setBorrowAmount(amount);
        vo.setOverdueFineRate(entity.getRequisition().getOverdueFineRate());
        FintechResponse<List<FinanceRepaymentPlanVO>> response = refundServiceFeign.findRepaymentListByContract(entity.getContractNumber(),
                 entity.getRequisition().getMaxOverdueDays(), entity.getRequisition().getOverdueFineRate());
        if (!response.isOk()) {
            throw new FInsuranceBaseException(105008);
        }
        if (response.getData() == null || response.getData().size() < 1) {
            LOG.error("convertContractToContractVO error  null repaymentPlanVO with id = [" + entity.getId() + "]");
            return null;
        }
        List<FinanceRepaymentPlanVO> planList = response.getData();
        // ????????????
        FinanceRepaymentPlanVO planVO = new FinanceRepaymentPlanVO();
        //???????????????
        Integer refundPhase = 0;
        //??????
        Double overFee = 0.0;

        for (int i = 0; i <  planList.size(); i++) {
            planVO = planList.get(i);
            if (planVO.getRepayStatus() == RefundStatus.INIT_REFUND) {
                //??????????????????
                return null;
            }
            refundPhase = planVO.getCurrentInstalment();
            if (planVO.getRepayStatus() != RefundStatus.HAS_REFUND) {
               break;
            }
        }

        // ???????????????true?????????false?????????
        vo.setOverdueFlag(planVO.getOverdueFlag());
        vo.setOverdueDays(planVO.getOverdueDays());
        vo.setRefundPhase(refundPhase);

        vo.setRepayTotalAmount(planVO.getRepayCapitalAmount() + planVO.getRepayInterestAmount() + planVO.getOverdueFine());
        // ????????????????????????
        vo.setRefundStatus(planVO.getRepayStatus().getCode());
        // ?????????
        vo.setRepayDate(planVO.getRepayDate());
        return vo;
    }

    @Override
    public void updateContractStatus(String contractNumber, ContractStatus targetStatus) {
        Contract contract = contractDao.getContractInfoByContractNo(contractNumber);
        if (contract == null) {
            throw new FInsuranceBaseException(104521, new Object[]{"contractNumber = " + contractNumber});
        }
        LOG.info("Update contract [{}] status from {} to {}", contractNumber, contract.getContractStatus(), targetStatus.name());
        if(targetStatus != null)
        contract.setContractStatus(targetStatus.getCode());
        contractDao.save(contract);
    }

    @Override
    @Transactional(readOnly = true)
    public Pagination<ContractSimpleDetail> pageWechatContractByRequisitionId(Integer requisitionId, Integer pageIndex, Integer pageSize) {
        //Sort sort = new Sort(Sort.Direction.ASC,"business_duration");
        Page<Contract> page = contractDao.getByRequisition_IdOrderByBusinessDurationAsc(requisitionId, new PageRequest(pageIndex-1, pageSize));
        if (page == null || page.getContent() == null) {
            throw new FInsuranceBaseException(104536, new Object[]{"requisitionId = " + requisitionId});
        }
        List<Contract> contractList = page.getContent();
        List<ContractSimpleDetail> voList = new ArrayList<>();

        ContractSimpleDetail simpleDetail = null;
        for (Contract contract : contractList) {
            simpleDetail = new ContractSimpleDetail();

            simpleDetail.setContractId(contract.getId());
            // ?????????
            // ??????RequisitionDetail
            if (contract.getRequisition() != null) {
                RequisitionDetail requisitionDetail = requisitionDetailDao.getByRequisition_IdAndContract_Id(contract.getRequisition().getId(), contract.getId());
                if (requisitionDetail != null) {
                    simpleDetail.setCarNumber(requisitionDetail.getCarNumber());
                }
            }
            simpleDetail.setContractUrl(contract.getContentShowFile());
            simpleDetail.setServiceUrl(contract.getServiceContentShowFile());
            simpleDetail.setTotalInstalment(contract.getBusinessDuration());
            simpleDetail.setContractAmount(contract.getContractAmount().doubleValue());
            // ??????????????????
            Double repayment = CalculationFormulaUtils.getEachRepayAmount(contract.getContractAmount(), contract.getBusinessDuration(), contract.getInterestRate()).doubleValue();
            simpleDetail.setInterestAmount(repayment);
            voList.add(simpleDetail);
        }

        return Pagination.createInstance(pageIndex, pageSize, page.getTotalElements(), voList);
    }

    @Override
    @Transactional(readOnly = true)
    public List<ContractVO> getContractByRequisitionId(Integer id) {
        List<Contract> contractList = contractDao.listByRequisitionId(id);
        if (contractList == null) {
            return Collections.emptyList();
        }
        List<ContractVO> resultList = new ArrayList<>();

        for (Contract contract : contractList) {
            ContractVO vo = new ContractVO();
            vo.setContractId(contract.getId());
            vo.setContractCode(contract.getContractNumber());
            vo.setInterestRate(contract.getInterestRate());
            if (contract.getRequisition() != null)
            {
                vo.setProductType(contract.getRequisition().getProductType());
                vo.setRequisitionId(contract.getRequisition().getId());
                vo.setRequisitionNumber(contract.getRequisition().getRequisitionNumber());
                vo.setMaxOverdueDays(contract.getRequisition().getMaxOverdueDays());
                vo.setBorrowAmount(contract.getContractAmount().doubleValue());
                vo.setTotalPhase(contract.getBusinessDuration());
            }
            resultList.add(vo);
        }
        return resultList;
    }

    @Override
    public void cleanContractDataByRequisitionId(Integer requisitionId) {
        List<Contract> contractList = contractDao.listByRequisitionId(requisitionId);
        if (contractList.isEmpty()) {
            return ;
        }

        List<String> contractRelativeFiles = new ArrayList<String>();
        List<String> contractNumers = new ArrayList<String>();
        if (contractList != null && contractList.size() > 0) {
            for (Contract c : contractList) {
                if(StringUtils.isNoneBlank(c.getContractNumber())){
                    contractNumers.add(c.getContractNumber());
                }
                if (StringUtils.isNoneBlank(c.getContentShowFile())) {
                    contractRelativeFiles.add(c.getContentShowFile());
                }
                if (StringUtils.isNoneBlank(c.getServiceContentShowFile())) {
                    contractRelativeFiles.add(c.getServiceContentShowFile());
                }
            }
        }

        //??????????????????
        deleteRepaymentByContractNumbers(contractNumers);
        //??????????????????
        deleteQiniuFile(contractRelativeFiles);
        //??????????????????
        for (Contract contract : contractList) {
            contractDao.delete(contract);
        }
    }

    private void deleteRepaymentByContractNumbers(List<String> contractNumberStr){
        FintechResponse<VoidPlaceHolder> fintechResponse = repaymentPlanServiceFeign.deleteRepaymentPlanByContractNumbers(contractNumberStr);
        if(!fintechResponse.isOk()){
            for(String str : contractNumberStr) {
                LOG.error("deleteRepaymentPlanByContractNumbers error with contractNumber=[" + str + "]");
            }
            throw new FInsuranceBaseException(104530);
        }
    }

    @Transactional
    @Override
    public String getRequisitionContractFile(Integer contractId, boolean isServiceContract, Boolean isPictureNeeded) {
        Contract contract = contractDao.getContractById(contractId);
        if(contract == null){
            LOG.error("getRequisitionContractFile failed with contractId = [" + contractId +"]");
            throw new FInsuranceBaseException(104521 , new Object[]{"contractId = " + contractId});
        }
        return getContractFileURLByContractType(contract, isServiceContract, isPictureNeeded);
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public void generateContractInfoOfRequsition(Integer requsitionId, boolean isAllFilesRegenrate, boolean isGenerateContractNumber) {
        List<Contract> contracts = contractDao.listByRequisitionId(requsitionId);

        Requisition requisition = requisitionDao.getRequisitionById(requsitionId);
        String productCode = requisition.getProduct().getProductCode();
        String channelCode = requisition.getChannel().getChannelCode();
        for (Contract contract : contracts) {
            // ?????????????????????
            if (StringUtils.isBlank(contract.getCustomerContractNumber()) && isGenerateContractNumber) {
                LOG.info("prepare to generate customer contract number for:" + contract.getContractNumber());
                contract.setCustomerContractNumber(this.generateContractSerialNum(productCode, channelCode,
                        contract.getBusinessDuration(), requisition.getSubmissionDate()));
                LOG.info("generated customer contract number: {} for: {}", contract.getCustomerContractNumber(),  contract.getContractNumber());
            }

            // ???????????????????????????????????????
            if (isAllFilesRegenrate) {
                contract.setBestsignFile(null);
                contract.setContentOriginFile(null);
                contract.setContentShowFile(null);

                contract.setServiceBestSignFile(null);
                contract.setServiceContentOriginFile(null);
                contract.setServiceContentShowFile(null);
            }
            contractDao.save(contract);
        }
        LOG.info("contract info update successed.");
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public void generateContractFileOfRequsition(Integer requsitionId) {
        List<Contract> contracts = contractDao.listByRequisitionId(requsitionId);
        for(Contract contract : contracts) {
            if(StringUtils.isNoneBlank(contract.getContentShowFile(), contract.getBestsignFile(),
                    contract.getContentOriginFile(), contract.getServiceContentOriginFile(),
                    contract.getServiceContentShowFile(), contract.getServiceBestSignFile())) { //???????????????
                continue;
            }
            // ??????????????????????????????????????????
            this.generateContractDraftFiles(contract, true); // ??????????????????
            this.generateContractDraftFiles(contract, false);// ??????????????????
        }
    }

    @Override
    public void changeStatusToSurrenderByContractNumber(List<String> contractNumbers) {
        if(contractNumbers == null || contractNumbers.size() < 1){
            return;
        }
        List<Contract> contractList = contractDao.findByContractNumbers(contractNumbers);
        if(contractList != null && contractList.size() > 0){
            for(Contract c : contractList){
               c.setContractStatus(ContractStatus.InsuranceReturning.getCode());
               contractDao.save(c);
            }
        }
    }

    @Override
    public String generateContractSerialNum(String productCode, String channelCode, int contractInstalmentNum, Date generateContractDate) {
        if (!StringUtils.isNoneBlank(productCode, channelCode)) {
            throw new NullParameterException("productCode or channelCode is null");
        }

        if (null == generateContractDate) {
            generateContractDate = new Date();
        }

        // ????????????????????????????????????
        Long newContractYearCount = this.getNewContractIndex(productCode, contractInstalmentNum, channelCode,
                DateCommonUtils.getYearOfCentury(generateContractDate));

        String newSerialNumStr = StringUtils.leftPad(String.valueOf(newContractYearCount),4, BasicConstants.NUMBER_ZERO);
        String dateYearMonth = DateCommonUtils.dateToStringByFormat(generateContractDate, DateCommonUtils.TIME_FORMAT_YEAR_MONTH);
        String instalmentStr = StringUtils.leftPad(String.valueOf(contractInstalmentNum),2, BasicConstants.NUMBER_ZERO);

        return String.format("%s%s%s%s%s", productCode, instalmentStr, channelCode, dateYearMonth, newSerialNumStr);
    }

    @Override
    @Transactional(readOnly = true)
    public ContractVO getContractInfoByContractId(Integer contractId) {
        Contract contract = contractDao.getContractById(contractId);
        return this.convertContractVO(contract);
    }

    /**
     * ??????redis ????????????????????????????????????????????????
     * @param contractYear ?????????????????????????????????????????????
     * @return
     */
    private Long getNewContractIndex(String productCode, int contractInstalment, String channelCode, int contractYear) {
        String instalmentStr = StringUtils.leftPad(String.valueOf(contractInstalment),2, BasicConstants.NUMBER_ZERO);
        String hashKey = String.format("%s%s%s%s", productCode, instalmentStr, channelCode, contractYear);


        if (!redisTemplate.hasKey(REDIS_KEY_CONTRACT_SERIAL_NUM) || !redisTemplate.opsForHash().hasKey(REDIS_KEY_CONTRACT_SERIAL_NUM, hashKey)) {
            try {
                LOCK.lockInterruptibly();
                if (!redisTemplate.hasKey(REDIS_KEY_CONTRACT_SERIAL_NUM) || !redisTemplate.opsForHash().hasKey(REDIS_KEY_CONTRACT_SERIAL_NUM, hashKey)) {
                    this.putLargestContractIndexToRedis(hashKey);
                }
            } catch (Exception e) {
                LOG.error("Error occured when got new contract index from redis", e);
            } finally {
                LOCK.unlock();
            }
        }

        Long lastContractSerialNum = Long.valueOf(redisTemplate.opsForHash().get(REDIS_KEY_CONTRACT_SERIAL_NUM, hashKey).toString());
        Long newContractSerialNum = lastContractSerialNum + 1;
        redisTemplate.opsForHash().put(REDIS_KEY_CONTRACT_SERIAL_NUM, hashKey, String.valueOf(newContractSerialNum));
        return newContractSerialNum;
    }

    private void putLargestContractIndexToRedis(String hashKey) {
        String largestContractNumber = contractDao.getLargestContractNumberByNumberPrefix(hashKey);
        if(StringUtils.isBlank(largestContractNumber)) {
            redisTemplate.opsForHash().put(REDIS_KEY_CONTRACT_SERIAL_NUM, hashKey, BasicConstants.NUMBER_ZERO);
        } else {
            String contractIndexStr = StringUtils.substring(largestContractNumber, largestContractNumber.length() - 4, largestContractNumber.length());
            long contractIndex = Long.valueOf(contractIndexStr);
            redisTemplate.opsForHash().put(REDIS_KEY_CONTRACT_SERIAL_NUM, hashKey, String.valueOf(contractIndex));
        }
    }

    //??????????????????
    private String getContractFileURLByContractType(Contract contract, Boolean isServiceContract, Boolean isPictureNeeded) {

        Requisition requisition = contract.getRequisition();
        if(requisition == null){
            LOG.error("getContractFileVO failed with id = [" + contract.getId() +"]");
           return null;
        }

        FintechResponse<String> qiniuFileResp = null;
        if (isServiceContract) {
            if (!StringUtils.isNoneBlank(contract.getServiceContentShowFile(), contract.getServiceContentOriginFile(), contract.getServiceBestSignFile())) { // ???????????????
                generateContractDraftFiles(contract, true);
            }
            // ?????? or PDF
            if (isPictureNeeded) {
                qiniuFileResp = qiniuBusinessServiceFeign.getQiniuDownloadUrl(contract.getServiceContentShowFile(), 0);
            } else {
                qiniuFileResp = qiniuBusinessServiceFeign.getQiniuDownloadUrl(contract.getServiceContentOriginFile(), 0);
            }

        } else {
            if (!StringUtils.isNoneBlank(contract.getContentShowFile(), contract.getContentOriginFile(), contract.getBestsignFile())) { // ???????????????
                generateContractDraftFiles(contract, false);
            }
            if (isPictureNeeded) {
                qiniuFileResp = qiniuBusinessServiceFeign.getQiniuDownloadUrl(contract.getContentShowFile(), 0);
            } else {
                qiniuFileResp = qiniuBusinessServiceFeign.getQiniuDownloadUrl(contract.getContentOriginFile(), 0);
            }
        }

        if (!qiniuFileResp.isOk()) {
            throw FInsuranceBaseException.buildFromErrorResponse(qiniuFileResp);
        }

        return qiniuFileResp.getData();
    }

    //??????????????????????????????
    private String getQiNiuCloudUrl(String qiniuKey){
        if(StringUtils.isBlank(qiniuKey)){
            LOG.error("getQiNiuCloudUrl failed with null qiniuKey");
            return "";
        }
        FintechResponse<String> fintechResponse = qiniuBusinessServiceFeign.getQiniuDownloadUrl(qiniuKey, IS_PUBLIC);
        if(!fintechResponse.isOk()){
            throw FInsuranceBaseException.buildFromErrorResponse(fintechResponse);
        }
        return fintechResponse.getData();
    }

    //????????????????????????
    //??????????????????
    private void generateContractDraftFiles(Contract contract, Boolean isServiceContract) {
        Requisition requisition = contract.getRequisition();
        if(requisition == null) {
            return;
        }
        //????????????
        ProductType productType = ProductType.codeOf(requisition.getProductType());

        ContractInfoResponseVO vo = null;
        if (isServiceContract) { // ??????????????????
            if (ProductType.CAR_INSTALMENTS == productType) { // ????????????
                vo = ckContractGenerator.buildServiceContract(contract.getId());
            } else {
                vo = dkContractGenerator.buildServiceContract(contract.getId());
            }
            contract.setServiceContentShowFile(vo == null ? null : vo.getContractFileQiniuId());
            contract.setServiceBestSignFile(vo == null ? null : vo.getContractFileNum());
            contract.setServiceContentOriginFile(vo == null ? null : vo.getContractPDFQiniuId());
        } else { // ??????????????????
            if (ProductType.CAR_INSTALMENTS == productType) { // ????????????
                vo = ckContractGenerator.buildBorrowContract(contract.getId());
            } else { // ????????????
                vo = dkContractGenerator.buildBorrowContract(contract.getId());
            }
            contract.setContentShowFile(vo == null ? null : vo.getContractFileQiniuId());
            contract.setBestsignFile(vo == null ? null : vo.getContractFileNum());
            contract.setContentOriginFile(vo == null ? null : vo.getContractPDFQiniuId());
        }
        contractDao.save(contract);
    }

    //?????????????????????
    private void deleteQiniuFile(List<String> list){
        for(String str : list){
            try {
                qiniuBusinessServiceFeign.deleteFile(str , 0);
            } catch (Exception e) {//???????????????????????????????????????????????????????????????
                LOG.error("deleteQiniuFile failed  with key=[" + str + "]", e);
            }
        }
    }

    //?????????????????????????????????
    @Override
    public void delete(Integer contractId) {
        List<RequisitionDetail> requisitionDetailList = requisitionDetailDao.getByContract_Id(contractId);
        if(requisitionDetailList != null && requisitionDetailList.size() > 0){
            for(RequisitionDetail detail : requisitionDetailList){
                detail.setContract(null);
                detail.setUpdateAt(new Date());
            }
            requisitionDetailDao.save(requisitionDetailList);
        }
        Contract contract = contractDao.getById(contractId);
        if (null != contract) {
            contractDao.delete(contract);
        }
    }

    @Override
    @Transactional(readOnly = true)
    public ContractVO getContractDetailByContractNumber(String contractNumber) {
        Contract contract = contractDao.getContractInfoByContractNo(contractNumber);
        return this.convertContractVO(contract);
    }

    @Override
    @Transactional(readOnly = true)
    public ContractVO getOverdueContractVOByContractNumber(String contractNumber) {
        Contract contract = contractDao.getContractInfoByContractNo(contractNumber);
        return this.convertContractVO(contract);
    }

    @Override
    @Transactional(readOnly = true)
    public Double getOverdueFineRateByContractNumber(String contractNumber) {
        return contractDao.getOverdueFineRateByContractNo(contractNumber);
    }

    @Override
    @Transactional(readOnly = true)
    public void generateContractFileForRequsitions() {
        List<String> statuses = new ArrayList<>();
        statuses.add(RequisitionStatus.Draft.getCode());
        statuses.add(RequisitionStatus.Submitted.getCode());
        statuses.add(RequisitionStatus.Rejected.getCode());
        statuses.add(RequisitionStatus.Canceled.getCode());
        List<Requisition> requisitionList = requisitionDao.listByStatus(statuses);//???????????????????????????????????? ???????????? ??????????????????????????????????????????????????????
        if (null != requisitionList && requisitionList.size() > 0) {
            for (Requisition requisition : requisitionList) {
                this.generateContractFileOfRequsition(requisition.getId());
                LOG.info("success to generate ContractFile For Requsition with id : {}", requisition.getId());
            }
        }
    }
}
