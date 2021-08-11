package com.fintech.insurance.micro.dto.system;

public class EntityAuditLogVO {

    //id
    private Integer id;

    //审核实体id
    private Integer entityId;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getEntityId() {
        return entityId;
    }

    public void setEntityId(Integer entityId) {
        this.entityId = entityId;
    }
}
