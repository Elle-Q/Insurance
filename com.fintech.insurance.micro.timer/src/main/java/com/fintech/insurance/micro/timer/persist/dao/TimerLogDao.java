package com.fintech.insurance.micro.timer.persist.dao;

import com.fintech.insurance.micro.timer.persist.entity.TimerLog;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TimerLogDao extends JpaRepository<TimerLog, Integer>, TimerLogComplexDao{
}
