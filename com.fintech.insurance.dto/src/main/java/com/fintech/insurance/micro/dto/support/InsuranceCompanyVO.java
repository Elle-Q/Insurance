package com.fintech.insurance.micro.dto.support;

import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;

/**
 * @Author: Clayburn
 * @Description: 承保公司
 * @Date: 2017/11/9 10:32
 */
public class InsuranceCompanyVO extends CompanyNameVO implements Serializable {
    // 承保公司id
    private Integer id;

    // 承保公司支部
    private Set<BranchVO> branches = new HashSet<>();

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Set<BranchVO> getBranches() {
        return this.branches;
    }

    public void setBranches(Set<BranchVO> branches) {
        if (branches != null && branches.size() > 0) {
            this.branches = branches;
        }
    }

}
