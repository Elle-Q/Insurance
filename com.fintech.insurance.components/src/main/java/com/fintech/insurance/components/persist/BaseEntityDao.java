package com.fintech.insurance.components.persist;

import java.io.Serializable;
import java.util.Map;

public interface BaseEntityDao<T extends BaseEntity, ID extends Serializable> extends  BaseDao<T> {

	/**
	 * 根据指定的单个查询条件获取一条数据
	 *
	 * @param propertyKey
	 * @param value
	 * @return
	 */
	@Deprecated
	T getSingleOne(String propertyKey, Object value);

	/**
	 * 根据指定的多个查询条件获取一条数据
	 * @param conditons
	 * @return
	 */
	@Deprecated
	T getSingleOne(Map<String, Object> conditons);
}
