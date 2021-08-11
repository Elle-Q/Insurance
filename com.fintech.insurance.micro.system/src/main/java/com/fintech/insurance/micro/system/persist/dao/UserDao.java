package com.fintech.insurance.micro.system.persist.dao;

import com.fintech.insurance.components.persist.BaseDao;
import com.fintech.insurance.micro.system.persist.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface UserDao extends JpaRepository<User, Integer>, BaseDao<User>, UserComplexDao {

    @Query("select u from User u where u.mobilePhone = :mobile ")
    User getByMobile(@Param("mobile") String mobile);

    @Query("select u from User u where u.channelCode = :channelCode and u.isChannelAdmin = true")
    User getChannelAdminByCode(@Param("channelCode") String channelCode);

    @Query("select u from User u where u.mobilePhone = :mobile and u.userType = 'channel'")
    User getChannelUserByMobile(@Param("mobile") String mobile);

    @Query("select u from User u where u.mobilePhone = :mobile and u.isChannelAdmin = false")
    User getStaffUserByMobile(@Param("mobile") String mobile);

    @Query(value = "select * from sys_user u where u.id in (select sur.user_id from sys_user_role sur left outer join sys_role sr on sur.role_id = sr.id where sr.role_code = :roleCode) and u.user_type = 'staff' order by u.id asc", nativeQuery = true)
    List<User> listUsersByRoleCode(@Param("roleCode") String roleCode);
}
