package com.fintech.insurance.components.persist;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;

/**
 * @Description: 公共实体基类
 * @Author: Yong Li
 * @Date: 2017/11/11 10:30
 */
@MappedSuperclass
public abstract class BaseEntity implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "id", columnDefinition = "int(11) not null auto_increment comment '主键'")
    private Integer id;

    @Column(name = "created_at", columnDefinition = "datetime comment '创建时间'")
    private Date createAt = new Date();

    @Column(name = "updated_at", columnDefinition = "datetime comment '更新时间'")
    private Date updateAt;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Date getCreateAt() {
        return createAt;
    }

    public void setCreateAt(Date createAt) {
        this.createAt = createAt;
    }

    public Date getUpdateAt() {
        return updateAt;
    }

    public void setUpdateAt(Date updateAt) {
        this.updateAt = updateAt;
    }
}
