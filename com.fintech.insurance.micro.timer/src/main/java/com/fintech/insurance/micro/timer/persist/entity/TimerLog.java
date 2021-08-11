package com.fintech.insurance.micro.timer.persist.entity;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;

/**
 * @Description: 定时任务记录表
 * @Author: qxy
 * @Date: 2017/11/11 10:30
 */
@Entity
@Table(name = "timer_log")
public class TimerLog implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "id", columnDefinition = "int(11) not null auto_increment comment '主键'")
    private Integer id;


    @Column(name = "name", length = 16, columnDefinition = "varchar(64) comment '定时器名称'")
    private String name;

    @Column(name = "status", length = 16, columnDefinition = "varchar(16) comment '状态'")
    private String status;

    @Column(name = "start_time", length = 19, columnDefinition = "timestamp comment '定时任务开始'")
    @Temporal(TemporalType.TIMESTAMP)
    private Date startTime;

    @Column(name = "end_time", length = 19, columnDefinition = "timestamp comment '定时任务结束时间'")
    @Temporal(TemporalType.TIMESTAMP)
    private Date endTime;

    @Column(name = "description", columnDefinition = "varchar(1024) character set utf8 comment '定时任务描述'")
    private String description;

    @Column(name = "error", columnDefinition = "varchar(1024) character set utf8 comment '执行错误信息'")
    private String error;

    public String getError() {
        return error;
    }

    public void setError(String error) {
        this.error = error;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public Date getStartTime() {
        return startTime;
    }

    public void setStartTime(Date startTime) {
        this.startTime = startTime;
    }

    public Date getEndTime() {
        return endTime;
    }

    public void setEndTime(Date endTime) {
        this.endTime = endTime;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}
