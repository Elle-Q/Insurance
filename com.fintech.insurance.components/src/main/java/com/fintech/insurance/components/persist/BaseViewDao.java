package com.fintech.insurance.components.persist;

import org.springframework.data.domain.Page;

import java.io.Serializable;
import java.util.List;
import java.util.Map;

/**
 * 2.4.x版本实体相关的DAO的接口定义
 *
 * @param <T> 实体类型
 * @version 1.1.0
 * @since 2017-08-23 11:30:09
 * @author Sean Zhang
 */
public interface BaseViewDao<T extends Serializable> {

	/**
	 * 根据id获取一个实体
	 * @param id
	 * @return T
	 */
	T getById(Serializable id);

	/**
	 * 根据查询语句以及相关查询条件查询结果
	 *
	 * @param queryString 查询语句
	 * @param maxResults 返回数据的最大数量
	 * @param params 查询条件
	 * @return 满足查询条件的对象的数组
	 */
	List<T> findList(CharSequence queryString, int maxResults, Map<String, Object> params);

	/**
	 * 根据动态查询语句查询实体结果
	 *
	 * @param queryString 动态查询语句，包含所有查询条件
	 * @param maxResults 返回数据的最大数量
	 * @param params 动态查询参数的值
	 * @return 满足查询条件的对象的数组
	 */
	List<T> findList(CharSequence queryString, int maxResults, Object[] params);

	/**
	 *
	 * @param queryString 动态查询语句，包含所有查询条件
	 * @param maxResults 返回数据的最大数量
	 * @param params 动态查询参数的值
	 * @param <R> 数据类型
	 * @return 满足查询条件的对象的数组
	 */
	<R> List<R> findEntityList(CharSequence queryString, int maxResults, Map<String, Object> params);

	/**
	 * 根据查询语句以及相关查询条件查询一条结果
	 *
	 * @param queryString 查询语句
	 * @param params 查询条件
	 * @return 满足条件的一条数据
	 */
	T findFirstEntity(CharSequence queryString, Map<String, Object> params);

	/**
	 * 获取hql语句查询结果的第一条记录
	 * @param queryString JPQL语句
	 * @param params 参数
	 * @param <R> 返回类型
	 * @return 第一条记录
	 */
	<R> R findFirstRecord(CharSequence queryString, Map<String, Object> params);

	/**
	 * 根据查询语句以及相关查询条件查询一条结果
	 *
	 * @param queryString 查询语句
	 * @param params 查询条件
	 * @return 满足条件的一条数据
	 */
	T findFirstEntity(CharSequence queryString, Object[] params);

	/**
	 * 根据实体的多个属性查询实体
	 *
	 * @param conditions 实体属性与值的Map
	 * @return 满足查询条件的对象的数组
	 */
	List<T> findByProperty(Map<String, Object> conditions);

	/**
	 * 根据实体的单个属性查询实体
	 *
	 * @param name 属性名
	 * @param value 属性的值
	 * @return 满足查询条件的对象的数组
	 */
	List<T> findByProperty(String name, Object value);

	/**
	 * 根据分页参数返回指定页的对象
	 *
	 * @param pageIndex 页码
	 * @param pageSize 分页大小
	 * @return 指定页码的对象的数组
	 */
	Page<T> findPagination(int pageIndex, int pageSize);

	/**
	 * 根据多个查询条件以及分页参数返回指定页的对象
	 *
	 * @param params 查询条件
	 * @param pageIndex 页码
	 * @param pageSize 分页大小
	 * @return 满足条件的对象的数组
	 */
	Page<T> findPagination(Map<String, Object> params, int pageIndex, int pageSize);

	/**
	 * 根据分页参数返回指定页的对象
	 *
	 * @param hql JPQL语句
	 * @param countHql 查询总数量的语句
	 * @param params 查询条件
	 * @param pageIndex 页码
	 * @param pageSize 分页大小
	 * @return 满足条件的对象的数组
	 */
	Page<T> findPagination(String hql, String countHql, Map<String, Object> params, int pageIndex, int pageSize);

	/**
	 * @param hql JPQL语句
	 * @param countHql 查询总数量的语句
	 * @param params 查询条件
	 * @param pageIndex 页码
	 * @param pageSize 分页大小
	 * @return 满足条件的数据
	 */
	<R> Page<R> findEntityPagination(String hql, String countHql, Map<String, Object> params, int pageIndex, int pageSize);

	/**
	 * 根据原生SQL查询分页数据
	 *
	 * @param querySql sql查询语句
	 * @param params 查询条件
	 * @param pageIndex 页码
	 * @param pageSize 分页大小
	 * @return 满足条件的查询结果数组
	 */
	Page<?> findSqlPaginationList(String querySql, Map<String, Object> params, int pageIndex, int pageSize);
}
