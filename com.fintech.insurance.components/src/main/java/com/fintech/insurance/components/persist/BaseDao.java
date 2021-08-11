package com.fintech.insurance.components.persist;

import java.io.Serializable;
import java.util.Collection;

/**
 * @param <T>
 * @version 1.1.0
 * @since 2017-08-23 11:34:46
 * @author Sean Zhang
 */
public interface BaseDao<T extends Serializable> extends BaseViewDao<T> {

	/**
	 * 复杂查询条件或者排序条件
	 */
	final String COMPLEX = "complex";

	/**
	 * 批量插入多个对象
	 * @param entities
	 */
	@Deprecated
	void insert(Collection<T> entities);

	/**
	 * 插入单个对象
	 * @param entity
	 */
	@Deprecated
	void insertEntity(T entity);

	/**
	 * 批量更新多个对象
	 * @param entities
	 */
	@Deprecated
	void update(Collection<T> entities);

	/**
	 * 更新当个对象
	 * @param entity
	 */
	@Deprecated
	void updateEntity(T entity);

	/**
	 * 批量保存多个对象
	 * @param entities
	 */
	@Deprecated
	void save(Collection<T> entities);

	/**
	 * 保存单个对象
	 * @param entity
	 */
	@Deprecated
	void saveEntity(T entity);

	/**
	 * 删除单个对象
	 * @param entity
	 */
	void deleteEntity(T entity);

	/**
	 * 根据id删除单个对象
	 * @param id
	 */
	@Deprecated
	void deleteById(Serializable id);

	/**
	 * 批量删除多个对象
	 * @param entities
	 */
	@Deprecated
	void delete(Collection<T> entities);
}
