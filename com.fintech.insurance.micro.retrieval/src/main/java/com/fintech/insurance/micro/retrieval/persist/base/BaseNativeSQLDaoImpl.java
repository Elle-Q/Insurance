package com.fintech.insurance.micro.retrieval.persist.base;

import com.fintech.insurance.commons.utils.Pagination;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * @Author: Clayburn
 * @Description: 分页工具类
 * @Date: 2017/12/1 9:49
 */
@Repository
public class BaseNativeSQLDaoImpl<T, S extends RowMapper> implements BaseNativeSQLDao<T, S> {
    @Autowired
    protected JdbcTemplate jdbcTemplate;

    /**
     * 分页工具
     * @param sql 原生sql
     * @param countSql 原生求总行数sql
     * @param args 参数数组，按顺序替换sql中的'?'
     * @param pageIndex 页码
     * @param pageSize 页面大小
     * @param s rowMapper
     * @return
     */
    @Override
    public Pagination<T> createPage(String sql, String countSql, Object[] args, Integer pageIndex, Integer pageSize, S s) {

        if (StringUtils.isBlank(sql) || StringUtils.isBlank(countSql)) {
            throw new RuntimeException("sql不能为空！");
        }

        // 查询出总条数
        Integer totalRows = jdbcTemplate.queryForObject(countSql, args, Integer.class);

        // 查询出当前页的List
        sql = sql + " LIMIT " + (pageIndex - 1) * pageSize + ", " + pageSize;
        List<T> resultList = jdbcTemplate.query(sql, args, s);

        return Pagination.createInstance(pageIndex, pageSize, totalRows, resultList);
    }

    /**
     * 分页工具
     * @param sql 原生sql
     * @param args 参数数组，按顺序替换sql中的'?'
     * @param s rowMapper
     * @return
     */
    @Override
    public List<T> createList(String sql, Object[] args, S s) {

        // 查询出当前页的List
        List<T> resultList = jdbcTemplate.query(sql, args, s);
        return resultList;
    }
}
