package com.fintech.insurance.components.cache;

/**
 * @Description: (some words)
 * @Author: Yong Li
 * @Date: 2017/11/13 20:05
 */
import java.util.List;

import org.springframework.stereotype.Service;

@Service
public class UserRedisServiceImpl extends AbstractRedisService<UserEntity> implements IUserRedisService {

    private static final String USER_REDIS_KEY = "USER_REDIS_KEY";

    @Override
    protected String getRedisKey() {
        return USER_REDIS_KEY;
    }

    @Override
    public void saveUsers(List<UserEntity> users) {
        for (UserEntity user : users) {
            put(user.getId(), user, -1);
        }
    }

    @Override
    public List<UserEntity> findAll() {
        return getAll();
    }

    @Override
    public UserEntity findById(String id) {
        return get(id);
    }

    @Override
    public void deleteById(String id) {
        remove(id);
    }


}
