package com.fintech.insurance.micro.feign.system;


import com.fintech.insurance.micro.api.system.EntityAuditLogServiceAPI;
import org.springframework.cloud.netflix.feign.FeignClient;

/**
 * 审核记录
 */
@FeignClient("insurance-micro-system")
public interface EntityAuditLogServiceFeign extends EntityAuditLogServiceAPI {
}
