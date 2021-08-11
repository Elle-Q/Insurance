package com.fintech.insurance.micro.dto.biz;

import com.fintech.insurance.micro.dto.validate.groups.Find;
import com.fintech.insurance.micro.dto.validate.groups.Query;
import com.fintech.insurance.micro.dto.validate.groups.Save;
import com.fintech.insurance.micro.dto.validate.groups.Update;
import org.hibernate.validator.constraints.NotBlank;
import org.hibernate.validator.constraints.NotEmpty;

import javax.validation.constraints.NotNull;
import java.io.Serializable;

/**
 * @Author: Clayburn
 * @Description: 登记操作
 * @Date: 2017/11/10 18:37
 */
public class RecordVO implements Serializable{
    // 唯一编号
    @NotBlank(message = "102001", groups = {Find.class, Query.class})
    private String code;
    // 图片信息uuid
    @NotNull(message = "102001", groups = {Query.class, Save.class})
    private String[] imgKey;
    // 备注
    private String remark;
    // 逾期天数
    @NotNull(message = "102001", groups = {Save.class})
    private Integer overdueDays = 0;

    // 还款计划id
    @NotNull(message = "102001",groups = {Save.class, Update.class})
    private Integer repaymentPlanId;

    public Integer getRepaymentPlanId() {
        return repaymentPlanId;
    }

    public void setRepaymentPlanId(Integer repaymentPlanId) {
        this.repaymentPlanId = repaymentPlanId;
    }

    public Integer getOverdueDays() {
        return overdueDays;
    }

    public void setOverdueDays(Integer overdueDays) {
        this.overdueDays = overdueDays;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String[] getImgKey() {
        return imgKey;
    }

    public void setImgKey(String[] imgKey) {
        this.imgKey = imgKey;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }
}
