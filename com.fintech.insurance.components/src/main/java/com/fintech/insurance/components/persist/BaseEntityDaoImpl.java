package com.fintech.insurance.components.persist;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.InitializingBean;

import javax.persistence.TypedQuery;
import java.io.Serializable;
import java.util.*;

/**
 * 存管实体DAO实现类基类，实现公共操作
 *
 * @author Sean Zhang
 * @version 1.1.0
 * @since 2017-08-23 11:08:09
 */
public abstract class BaseEntityDaoImpl<T extends BaseEntity, ID extends Serializable> extends BaseDaoImpl<T, Number> implements InitializingBean, BaseEntityDao<T, ID> {

	private static final Logger log = LoggerFactory.getLogger(BaseEntityDaoImpl.class);

	/**
	 * 初始化DAO并获取实体的类型
	 *
	 * @throws Exception
	 */
	public void afterPropertiesSet() throws Exception {
		super.afterPropertiesSet();
	}

	@Override
	public T getSingleOne(String propertyKey, Object value) {
		if (StringUtils.isEmpty(propertyKey)) {
			return null;
		} else {
			Map<String, Object> conditions = new HashMap<String, Object>();
			conditions.put(propertyKey.trim(), value);
			return this.getSingleOne(conditions);
		}
	}

	@Override
	public T getSingleOne(Map<String, Object> conditons) {
		StringBuilder queryString = new StringBuilder("select e from " + this.entityClazz.getSimpleName() + " e ");
		if (conditons != null && conditons.size() > 0) {
			Iterator<String> it = conditons.keySet().iterator();
			String key = it.next();
			queryString.append(" where e." + key.trim() + " = :" + key.trim());
			while (it.hasNext()) {
				key = it.next();
				queryString.append(" and e." + key.trim() + " = :" + key.trim());
			}
		}
		TypedQuery<T> query = this.entityManager.createQuery(queryString.toString(), this.entityClazz);
		if (conditons != null && conditons.size() > 0){
			for (String paramKey : conditons.keySet()) {
				query.setParameter(paramKey.trim(), conditons.get(paramKey));
			}
		}
		List<T> results = query.setMaxResults(1).getResultList();
		if (results == null || results.size() < 1) {
			return null;
		} else {
			return results.get(0);
		}
	}

	/*private void updateMetaData(T entity) {
		if (entity != null) {
			if (entity.getId() > 0) {
				entity.setUpdateAt(new Date());
				entity.setUpdateBy(this.getCurrentSessionUserId());
			} else {
				entity.setCreateAt(new Date());
				entity.setCreateBy(this.getCurrentSessionUserId());
			}
		}
	}*/

	/*private Integer getCurrentSessionUserId() {
		PlatformUserInfoVO platformUser = SharedObjectUtils.getSession();
		if (platformUser == null) {
			return null;
		} else {
			return platformUser.getId();
		}
	}*/

	@Override
	public void insert(Collection<T> entities) {
		if (entities != null && entities.size() > 0) {
			List<T> entityList = new ArrayList<T>();
			for (T entity : entities) {
				if (entity != null) {
					//this.updateMetaData(entity);
					entityList.add(entity);
				}
			}
			entities.clear();
			super.insert(entityList);
		}
	}

	@Override
	public void insertEntity(T entity) {
		if (entity != null) {
			//this.updateMetaData(entity);
			super.insertEntity(entity);
		}
	}

	@Override
	public void update(Collection<T> entities) {
		if (entities != null && entities.size() > 0) {
			List<T> entityList = new ArrayList<T>();
			for (T entity : entities) {
				if (entity != null) {
					//this.updateMetaData(entity);
					entityList.add(entity);
				}
			}
			entities.clear();
			super.update(entityList);
		}
	}

	@Override
	public void updateEntity(T entity) {
		if (entity != null) {
			//this.updateMetaData(entity);
			super.updateEntity(entity);
		}
	}

	@Override
	public void save(Collection<T> entities) {
		if (entities != null && entities.size() > 0) {
			List<T> entityList = new ArrayList<T>();
			for (T entity : entities) {
				if (entity != null) {
					//this.updateMetaData(entity);
					entityList.add(entity);
				}
			}
			entities.clear();
			super.save(entityList);
		}
	}

	@Override
	public void saveEntity(T entity) {
		if (entity != null) {
			//this.updateMetaData(entity);
			super.saveEntity(entity);
		}
	}

	@Override
	public void deleteEntity(T entity) {
		if (entity != null) {
			if (entity.getId() > 0) {
				this.updateEntity(entity);
			} else {
				entity = null;
			}
		}
	}

	@Override
	public void deleteById(Serializable id) {
		if (id != null) {
			T entity = this.getById(id);
			if (entity != null) {
				//this.updateMetaData(entity);
				super.updateEntity(entity);
			}
		}
	}

	@Override
	public void delete(Collection<T> entities) {
		if (entities != null && entities.size() > 0) {
			List<T> entityList = new ArrayList<T>();
			for (T entity : entities) {
				if (entity != null) {
					//this.updateMetaData(entity);
					entityList.add(entity);
				}
			}
			entities.clear();
			super.update(entityList);
		}
	}

	@Override
	public T findFirstEntity(CharSequence queryString, Map<String, Object> params) {
		return super.findFirstEntity(queryString, params);
	}

	@Override
	public List<T> findByProperty(Map<String, Object> conditions) {
		if (conditions == null) {
			conditions = new HashMap<String, Object>();
		}
		return super.findByProperty(conditions);
	}

	@Override
	public List<T> findByProperty(String name, Object value) {
		if (StringUtils.isEmpty(name)) {
			return Collections.emptyList();
		} else {
			Map<String, Object> conditions = new HashMap<String, Object>();
			conditions.put(name, value);
			return super.findByProperty(conditions);
		}
	}

}
