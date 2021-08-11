package com.fintech.insurance.micro.dto.biz;

import java.io.Serializable;
import java.util.Date;

/**
 * 公共实体基类
 *
 * @author Sean Zhang
 * @version 1.1.0
 * @since 2017-08-18 14:54:30
 */
public abstract class BaseVO implements Serializable {

    //主键
    protected Integer id;

    //创建人主键
    protected Integer createBy;

    //创建时间
    protected Date createAt = new Date();

    //更新人主键
    protected Integer updateBy;

    //更新时间
    protected Date updateAt;

    //删除标识
    protected Boolean disableFlag = false;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getCreateBy() {
        return createBy;
    }

    public void setCreateBy(Integer createBy) {
        this.createBy = createBy;
    }

    public Date getCreateAt() {
        return createAt;
    }

    public void setCreateAt(Date createAt) {
        this.createAt = createAt;
    }

    public Integer getUpdateBy() {
        return updateBy;
    }

    public void setUpdateBy(Integer updateBy) {
        this.updateBy = updateBy;
    }

    public Date getUpdateAt() {
        return updateAt;
    }

    public void setUpdateAt(Date updateAt) {
        this.updateAt = updateAt;
    }

    public Boolean getDisableFlag() {
        return disableFlag;
    }

    public void setDisableFlag(Boolean disableFlag) {
        this.disableFlag = disableFlag;
    }
}
