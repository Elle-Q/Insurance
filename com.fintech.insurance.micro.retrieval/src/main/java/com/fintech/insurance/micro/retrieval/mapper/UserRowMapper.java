package com.fintech.insurance.micro.retrieval.mapper;

import com.fintech.insurance.micro.dto.retrieval.UserVO;
import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * @Description: (some words)
 * @Author: Yong Li
 * @Date: 2017/11/30 18:21
 */
public class UserRowMapper implements RowMapper<UserVO> {
    @Override
    public UserVO mapRow(ResultSet rs, int rowNum) throws SQLException {
        UserVO user = new UserVO();
        user.setId(rs.getString("id"));
        user.setName(rs.getString("user_name"));

        return user;
    }

}
