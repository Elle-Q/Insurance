package com.fintech.insurance.micro.biz.persist.entity;

import com.fintech.insurance.components.persist.BaseEntity;
import org.springframework.context.annotation.Lazy;

import javax.persistence.*;
import java.util.Date;
import java.util.Set;

/**
 * 业务申请单明细表
 *
 * @author liu man
 * @version 1.1.0
 * @since 2017-11-13 17:04:30
 */
@Entity
@Table(name = "busi_requisition_detail")
public class RequisitionDetail extends BaseEntity implements Cloneable {

    private static final long serialVersionUID = -8820461361835512415L;

    @ManyToOne(cascade = {CascadeType.MERGE, CascadeType.REFRESH, CascadeType.PERSIST, CascadeType.DETACH}, fetch = FetchType.LAZY)
    @JoinColumn(name = "requisition_id", columnDefinition = "int(10) unsigned not null comment '业务申请单id，关联busi_requisition表的主键'")
    @Lazy(value = true)
    private Requisition requisition;

    @OneToMany(mappedBy = "requisitionDetail", cascade = {CascadeType.PERSIST, CascadeType.REMOVE, CascadeType.MERGE, CascadeType.REFRESH})
    @Lazy(value = true)
    private Set<RequisitionDetailResource> requisitionDetailResourceSet;

    @Column(name = "driving_license", columnDefinition = "varchar(32) comment '行驶证号'")
    private String drivingLicense;

    @Column(name = "driving_license_main", columnDefinition = "varchar(32) comment '行驶证正本照片'")
    private String drivingLicenseMain;

    @Column(name = "driving_license_attach", columnDefinition = "varchar(32) comment '行驶证副本照片'")
    private String drivingLicenseAttach;

    @Column(name = "commercial_insurance_number", columnDefinition = "varchar(64) not null comment '商业保险保单号或者投保单号'")
    private String commercialInsuranceNumber;

    @Column(name = "commercial_insurance_amount", columnDefinition = "bigint unsigned not null comment '商业保险金额，单位为分'")
    private Integer commercialInsuranceAmount = 0;

    @Column(name = "commercial_insurance_start", columnDefinition = "date not null")
    private Date commercialInsuranceStart;

    @Column(name = "commercial_insurance_end", columnDefinition = "date not null")
    private Date commercialInsuranceEnd;

    @Column(name = "commercial_insurance_value", columnDefinition = "bigint unsigned not null comment '商业保险保单残值'")
    private Integer commercialInsuranceValue = 0;

    @Column(name = "max_duration", columnDefinition = "int not null comment '最大的分期期数，按月计算'")
    private Integer maxDuration;

    @Column(name = "business_duration", columnDefinition = "int not null comment '业务开展的期数，按月计算'")
    private Integer businessDuration;

    @Column(name = "compulsory_insurance_number", columnDefinition = "varchar(64) comment '交强险保单号'")
    private String compulsoryInsuranceNumber;

    @Column(name = "compulsory_insurance_amount", columnDefinition = "bigint unsigned not null default 0 comment '交强险金额，单位为分'")
    private Integer compulsoryInsuranceAmount = 0;

    @Column(name = "compulsory_insurance_start", columnDefinition = "date comment '强制保险开始时间'")
    private Date compulsoryInsuranceStart;

    @Column(name = "compulsory_insurance_end", columnDefinition = "date comment '强制保险结束时间'")
    private Date compulsoryInsuranceEnd;

    @Column(name = "tax_amount", columnDefinition = "bigint unsigned not null comment '车船税金额，单位为分'")
    private Integer taxAmount = 0;

    @Column(name = "is_commercial_only", columnDefinition = "boolean not null default 0 comment '是否只申请商业保险标志位'")
    private Boolean isCommercialOnly;

    @Column(name = "sub_total", columnDefinition = "bigint unsigned not null default 0 comment '小计金额'")
    private Integer subTotal = 0;

    @ManyToOne(cascade = {CascadeType.MERGE, CascadeType.REFRESH, CascadeType.PERSIST, CascadeType.DETACH}, fetch = FetchType.LAZY)
    @JoinColumn(name = "business_contract_id", columnDefinition = "int(10) unsigned comment '合同id'")
    private Contract contract;

    @Column(name = "car_number", columnDefinition = "varchar(64) not null comment '车牌号'")
    private String carNumber;

    public Set<RequisitionDetailResource> getRequisitionDetailResourceSet() {
        return requisitionDetailResourceSet;
    }

    public void setRequisitionDetailResourceSet(Set<RequisitionDetailResource> requisitionDetailResourceSet) {
        this.requisitionDetailResourceSet = requisitionDetailResourceSet;
    }

    public Requisition getRequisition() {
        return requisition;
    }

    public void setRequisition(Requisition requisition) {
        this.requisition = requisition;
    }

    public String getDrivingLicense() {
        return drivingLicense;
    }

    public void setDrivingLicense(String drivingLicense) {
        this.drivingLicense = drivingLicense;
    }

    public String getDrivingLicenseMain() {
        return drivingLicenseMain;
    }

    public void setDrivingLicenseMain(String drivingLicenseMain) {
        this.drivingLicenseMain = drivingLicenseMain;
    }

    public String getDrivingLicenseAttach() {
        return drivingLicenseAttach;
    }

    public void setDrivingLicenseAttach(String drivingLicenseAttach) {
        this.drivingLicenseAttach = drivingLicenseAttach;
    }

    public String getCommercialInsuranceNumber() {
        return commercialInsuranceNumber;
    }

    public void setCommercialInsuranceNumber(String commercialInsuranceNumber) {
        this.commercialInsuranceNumber = commercialInsuranceNumber;
    }

    public Integer getCommercialInsuranceAmount() {
        return commercialInsuranceAmount;
    }

    public void setCommercialInsuranceAmount(Integer commercialInsuranceAmount) {
        this.commercialInsuranceAmount = commercialInsuranceAmount;
    }

    public Date getCommercialInsuranceStart() {
        return commercialInsuranceStart;
    }

    public void setCommercialInsuranceStart(Date commercialInsuranceStart) {
        this.commercialInsuranceStart = commercialInsuranceStart;
    }

    public Date getCommercialInsuranceEnd() {
        return commercialInsuranceEnd;
    }

    public void setCommercialInsuranceEnd(Date commercialInsuranceEnd) {
        this.commercialInsuranceEnd = commercialInsuranceEnd;
    }

    public Integer getCommercialInsuranceValue() {
        return commercialInsuranceValue;
    }

    public void setCommercialInsuranceValue(Integer commercialInsuranceValue) {
        this.commercialInsuranceValue = commercialInsuranceValue;
    }

    public Integer getBusinessDuration() {
        return businessDuration;
    }

    public void setBusinessDuration(Integer businessDuration) {
        this.businessDuration = businessDuration;
    }

    public String getCompulsoryInsuranceNumber() {
        return compulsoryInsuranceNumber;
    }

    public void setCompulsoryInsuranceNumber(String compulsoryInsuranceNumber) {
        this.compulsoryInsuranceNumber = compulsoryInsuranceNumber;
    }

    public Integer getCompulsoryInsuranceAmount() {
        return compulsoryInsuranceAmount;
    }

    public void setCompulsoryInsuranceAmount(Integer compulsoryInsuranceAmount) {
        this.compulsoryInsuranceAmount = compulsoryInsuranceAmount;
    }

    public Date getCompulsoryInsuranceStart() {
        return compulsoryInsuranceStart;
    }

    public void setCompulsoryInsuranceStart(Date compulsoryInsuranceStart) {
        this.compulsoryInsuranceStart = compulsoryInsuranceStart;
    }

    public Date getCompulsoryInsuranceEnd() {
        return compulsoryInsuranceEnd;
    }

    public void setCompulsoryInsuranceEnd(Date compulsoryInsuranceEnd) {
        this.compulsoryInsuranceEnd = compulsoryInsuranceEnd;
    }

    public Integer getTaxAmount() {
        return taxAmount;
    }

    public void setTaxAmount(Integer taxAmount) {
        this.taxAmount = taxAmount;
    }

    public Boolean getCommercialOnly() {
        return isCommercialOnly;
    }

    public void setCommercialOnly(Boolean commercialOnly) {
        isCommercialOnly = commercialOnly;
    }

    public Integer getSubTotal() {
        return subTotal;
    }

    public void setSubTotal(Integer subTotal) {
        this.subTotal = subTotal;
    }

    public Contract getContract() {
        return contract;
    }

    public void setContract(Contract contract) {
        this.contract = contract;
    }

    public void setMaxDuration(Integer maxDuration) {
        this.maxDuration = maxDuration;
    }

    public Integer getMaxDuration() {
        return maxDuration;
    }

    public String getCarNumber() {
        return carNumber;
    }

    public void setCarNumber(String carNumber) {
        this.carNumber = carNumber;
    }

    @Override
    public Object clone() throws CloneNotSupportedException {

        return super.clone();
    }

    @Override
    public String toString() {
        return "RequisitionDetail{" +
                "requisition=" + requisition +
                ", requisitionDetailResourceSet=" + requisitionDetailResourceSet +
                ", drivingLicense='" + drivingLicense + '\'' +
                ", drivingLicenseMain='" + drivingLicenseMain + '\'' +
                ", drivingLicenseAttach='" + drivingLicenseAttach + '\'' +
                ", commercialInsuranceNumber='" + commercialInsuranceNumber + '\'' +
                ", commercialInsuranceAmount=" + commercialInsuranceAmount +
                ", commercialInsuranceStart=" + commercialInsuranceStart +
                ", commercialInsuranceEnd=" + commercialInsuranceEnd +
                ", commercialInsuranceValue=" + commercialInsuranceValue +
                ", maxDuration=" + maxDuration +
                ", businessDuration=" + businessDuration +
                ", compulsoryInsuranceNumber='" + compulsoryInsuranceNumber + '\'' +
                ", compulsoryInsuranceAmount=" + compulsoryInsuranceAmount +
                ", compulsoryInsuranceStart=" + compulsoryInsuranceStart +
                ", compulsoryInsuranceEnd=" + compulsoryInsuranceEnd +
                ", taxAmount=" + taxAmount +
                ", isCommercialOnly=" + isCommercialOnly +
                ", subTotal=" + subTotal +
                ", contract=" + contract +
                ", carNumber='" + carNumber + '\'' +
                '}';
    }
}
