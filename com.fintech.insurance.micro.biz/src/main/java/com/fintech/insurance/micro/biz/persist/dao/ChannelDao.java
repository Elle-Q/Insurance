package com.fintech.insurance.micro.biz.persist.dao;

import com.fintech.insurance.components.persist.BaseEntityDao;
import com.fintech.insurance.micro.biz.persist.entity.Channel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.criteria.CriteriaBuilder;
import java.util.List;

/**
 * @Description: (渠道管理)
 * @Author: yongNeng Liu
 * @Date: 2017/11/13 10:28
 */
@Repository
@Transactional
public interface ChannelDao extends JpaRepository<Channel, Integer>, BaseEntityDao<Channel, Integer>, ChannelComplexDao {

    @Query("select c from Channel c where  c.id = :id ")
    Channel getChannelById(@Param("id") Integer id);

    @Query("select c from Channel c where  c.channelCode = :channelCode ")
    Channel getByCode(@Param("channelCode") String channelCode);

    @Query("select c from Channel c where  c.channelName = :channelName ")
    Channel getByChannelName(@Param("channelName")String channelName);

    @Query("select c from Channel c where  c.businessLicence = :businessLicence ")
    Channel getByBusinessLicence(@Param("businessLicence") String businessLicence);

    @Query("select c from Channel c where  c.channelName = :channelName and c.id not in (:id) ")
    List<Channel> getByChannelNameLike(@Param("channelName") String channelName, @Param("id") Integer id);

    @Query("select c from Channel c where  c.businessLicence = :businessLicence and c.id not in (:id) ")
    List<Channel> getByBusinessLicenceLike(@Param("businessLicence") String businessLicence, @Param("id") Integer id);

    @Query("select c from Channel c where  c.channelCode = :channelCode ")
    Channel getChannelDetailByChannelCode(@Param("channelCode") String channelCode);

}
