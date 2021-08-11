package com.fintech.insurance.micro.retrieval.mapper;

import com.alibaba.fastjson.JSONArray;
import com.fintech.insurance.micro.dto.biz.RecordVO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * @Author: Clayburn
 * @Description:
 * @Date: 2017/12/2 14:31
 */
public class RecordVORowMapper implements RowMapper<RecordVO> {
    private static final Logger logger = LoggerFactory.getLogger(RecordVORowMapper.class);

    @Override
    public RecordVO mapRow(ResultSet rs, int rowNum) throws SQLException {
        RecordVO vo = new RecordVO();
        vo.setCode(rs.getString("requisition_code"));
        vo.setRemark(rs.getString("remark"));
        String keys = rs.getString("voucher");

        try {
            Object[] objects = JSONArray.parseArray(keys, String.class).toArray();
            String[] imgKeys = new String [objects.length];

            for (int i = 0; i < objects.length; i++) {
                imgKeys[i] = objects[i].toString();
            }

            vo.setImgKey(imgKeys);
        } catch (Exception e) {
            logger.error(e.getMessage());
            vo.setImgKey(new String[]{keys});
        }

        return vo;
    }
}
