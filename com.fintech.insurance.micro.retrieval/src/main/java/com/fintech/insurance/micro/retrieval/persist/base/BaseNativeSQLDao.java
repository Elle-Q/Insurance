package com.fintech.insurance.micro.retrieval.persist.base;

import com.fintech.insurance.commons.utils.Pagination;
import org.springframework.jdbc.core.RowMapper;

import java.util.List;

/**
 * @Author: Clayburn
 * @Description:
 * @Date: 2017/12/1 10:07
 */
public interface BaseNativeSQLDao<T, S extends RowMapper> {
    Pagination<T> createPage(String sql, String countSql, Object[] args, Integer pageIndex, Integer pageSize, S s);

    List<T> createList(String sql, Object[] args, S s);
}
