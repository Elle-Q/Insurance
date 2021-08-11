package com.fintech.insurance.components.cache;

import java.util.List;

/**
 * @Description: (some words)
 * @Author: Yong Li
 * @Date: 2017/11/13 20:06
 */
public interface IUserRedisService {

    public void saveUsers(List<UserEntity> users);

    public List<UserEntity> findAll();

    public UserEntity findById(String id);

    public void deleteById(String id);
}
