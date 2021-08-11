package com.fintech.insurance.micro.system.persist.dao;

import com.fintech.insurance.micro.system.persist.entity.User;
import org.springframework.data.domain.Page;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.NoRepositoryBean;
import org.springframework.data.repository.query.Param;

import java.util.List;

@NoRepositoryBean
public interface UserComplexDao {

    /**
     * 根据条件查询用户
     *
     * @param mobile
     * @param name
     * @param isLocked
     * @return
     */
    List<User> listUser(String mobile, String name, Integer isLocked, String userType);

    List<User> listChannelUserByMobile(String mobile);

    /**
     * 获取渠道用户
     * @param channelCode
     * @return
     */
    List<User> listChannelUserByCode(String channelCode);

    /**
     * 根据角色查找用户
     * @param roleCode
     * @return
     */
/*    @Query(value = "select u from sys_user u where exists (select 'x' from sys_user_role sur left outer join sys_role sr on sur.role_id = sr.id and sr.role_code = :roleCode) order by u.id asc", nativeQuery = true)
    List<User> listUsersByRoleCode(@Param("roleCode") String roleCode);*/

    /**
     * 分页查询渠道用户
     * @param channelCode
     * @param pageIndex
     * @param pageSize
     * @return
     */
    Page<User> pageChannelUserByCode(String channelCode, Integer pageIndex, Integer pageSize);
}
