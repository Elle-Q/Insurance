package com.fintech.insurance.components.persist;

import org.apache.commons.lang3.StringUtils;
import org.hibernate.SQLQuery;
import org.hibernate.transform.Transformers;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.repository.support.PageableExecutionUtils;

import javax.persistence.*;
import java.io.Serializable;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.*;

public abstract class BaseDaoImpl<T extends Serializable, I extends Number> implements InitializingBean, BaseDao<T> {

	private static final Logger log = LoggerFactory.getLogger(BaseDaoImpl.class);

	@PersistenceContext
	protected EntityManager entityManager;

	protected Class<T> entityClazz = null;

	/**
	 * 初始化DAO并获取实体的类型
	 * @throws Exception
	 */
	public void afterPropertiesSet() throws Exception {
		Type type = getClass().getGenericSuperclass();
		if (type instanceof ParameterizedType) {
			this.entityClazz = (Class<T>) ((ParameterizedType) type).getActualTypeArguments()[0];
		} else {
			this.entityClazz = null;
		}
	}

	@Override
	public void insert(Collection<T> entities) {
		log.debug("save " + this.entityClazz.getSimpleName() + " multi instances");
		if (entities != null && entities.size() > 0) {
			try {
				for (T entity: entities) {
					this.entityManager.persist(entity);
				}
				log.debug("save successful");
			} catch (RuntimeException e) {
				log.error("save failed", e);
				throw  e;
			}
		}
	}

	@Override
	public void insertEntity(T entity) {
		log.debug("save " + this.entityClazz.getSimpleName() + " single instance");
		if (entity != null) {
			try {
				this.entityManager.persist(entity);
				log.debug("save successful");
			} catch (RuntimeException e) {
				log.error("save failed", e);
				throw e;
			}
		}
	}

	@Override
	public void update(Collection<T> entities) {
		log.debug("update " + this.entityClazz.getSimpleName() + " multi instances");
		if (entities != null && entities.size() > 0) {
			try {
				for (T entity : entities) {
					if (!this.entityManager.contains(entity)) {
						this.entityManager.merge(entity);
					}
				}
				log.debug("update successful");
			} catch (RuntimeException e) {
				log.error("update failed", e);
				throw e;
			}
		}
	}

	@Override
	public void updateEntity(T entity) {
		log.debug("update " + this.entityClazz.getSimpleName() + " single instance");
		if (entity != null) {
			try {
				if (!this.entityManager.contains(entity)) {
					this.entityManager.merge(entity);
				}
				log.debug("update successful");
			} catch (RuntimeException e) {
				log.error("update failed", e);
				throw e;
			}
		}
	}

	@Override
	public void save(Collection<T> entities) {
		log.debug("saveOrUpdate " + this.entityClazz.getSimpleName() + " multi instances");
		if (entities != null && entities.size() > 0) {
			try {
				for (T entity : entities) {
					this.entityManager.merge(entity);
				}
				log.debug("saveOrUpdate successful");
			} catch (RuntimeException e) {
				log.error("saveOrUpdate failed", e);
				throw e;
			}
		}
	}

	@Override
	public void saveEntity(T entity) {
		log.debug("saveOrUpdate " + this.entityClazz.getSimpleName() + " single instance");
		if (entity != null) {
			try {
				this.entityManager.merge(entity);
			} catch (RuntimeException e) {
				log.error("saveOrUpdate failed", e);
				throw e;
			}
		}
	}

	@Override
	public void deleteEntity(T entity) {
		log.debug("delete " + this.entityClazz.getSimpleName() + " single instance");
		if (entity != null) {
			try {
				this.entityManager.remove(entity);
				log.debug("delete successful");
			} catch (RuntimeException e) {
				log.error("delete failed", e);
				throw e;
			}
		}
	}

	@Override
	public void deleteById(Serializable id) {
		log.debug("deleting " + this.entityClazz.getSimpleName() + " instance with id: " + id);
		if (id != null) {
			try {
				T entity = this.entityManager.find(this.entityClazz, id);
				if (entity != null) {
					this.entityManager.remove(entity);
					log.debug("delete successful, instance found with id: " + id);
				} else {
					log.debug("no instance found to be deleted");
				}
			} catch (RuntimeException e) {
				log.error("delete failed", e);
				throw e;
			}
		}
	}

	@Override
	public void delete(Collection<T> entities) {
		log.debug("deleting " + this.entityClazz.getSimpleName() + " instance");
		if (entities != null && entities.size() > 0) {
			try {
				for (T entity : entities) {
					if (entity != null) {
						this.entityManager.remove(entity);
					}
				}
				log.debug("delete successful");
			} catch (RuntimeException e) {
				log.error("delete failed", e);
				throw e;
			}
		}
	}

	@Override
	public T getById(Serializable id) {
		if (id == null) {
			return null;
		} else {
			return this.entityManager.find(this.entityClazz, id);
		}
	}

	@Override
	public T findFirstEntity(CharSequence queryString, Map<String, Object> params) {
			if (!StringUtils.isEmpty(queryString)) {
                TypedQuery<T> query = this.entityManager.createQuery(queryString.toString(), this.entityClazz);
                if (params != null && !params.isEmpty()) {
                    for (String paramKey : params.keySet()) {
                        query.setParameter(paramKey, params.get(paramKey));
                    }
                }
                List<T> entities = query.getResultList();
				return entities != null && entities.size() > 0 ? entities.get(0) : null;
            } else {
                return null;
            }
	}

	@Override
	public <R> R findFirstRecord(CharSequence queryString, Map<String, Object> params) {
		try {
			if (!StringUtils.isEmpty(queryString)) {
                Query query = this.entityManager.createQuery(queryString.toString());
                if (params != null && !params.isEmpty()) {
                    for (String paramKey : params.keySet()) {
                        query.setParameter(paramKey, params.get(paramKey));
                    }
                }
                return (R) query.getSingleResult();
            } else {
                return null;
            }
		} catch (Exception e) {
			e.printStackTrace();
			log.error(e.getMessage());
			if(e instanceof NoResultException){
				return null;
			} else {
				throw  e;
			}
		}

	}

	@Override
	public T findFirstEntity(CharSequence queryString, Object[] params) {
		if (!StringUtils.isEmpty(queryString)) {
			TypedQuery<T> query = this.entityManager.createQuery(queryString.toString(), this.entityClazz);
			if (params != null) {
				for (int i = 0; i < params.length; i++) {
					query.setParameter(i + 1, params[i]);
				}
			}
			List<T> entities = query.getResultList();
			return entities != null && entities.size() > 0 ? entities.get(0) : null;
		} else {
			return null;
		}
	}

	@Override
	public List<T> findList(CharSequence queryString, int maxResults, Map<String, Object> params) {
		if (!StringUtils.isEmpty(queryString)) {
			TypedQuery<T> query = this.entityManager.createQuery(queryString.toString(), this.entityClazz);
			if (params != null && !params.isEmpty()) {
				for (String paramKey : params.keySet()) {
					query.setParameter(paramKey, params.get(paramKey));
				}
			}
			if (maxResults > 0) {
				return query.setMaxResults(maxResults).getResultList();
			} else {
				return query.getResultList();
			}
		} else {
			return Collections.emptyList();
		}
	}

	@Override
	public List<T> findList(CharSequence queryString, int maxResults, Object[] params) {
		if (!StringUtils.isEmpty(queryString)) {
			TypedQuery<T> query = this.entityManager.createQuery(queryString.toString(), this.entityClazz);
			if (params != null) {
				for (int i = 0; i < params.length; i++) {
					query.setParameter(i + 1, params[i]);
				}
			}
			if (maxResults > 0) {
				return query.setMaxResults(maxResults).getResultList();
			} else {
				return query.getResultList();
			}
		} else {
			return Collections.emptyList();
		}
	}

	@Override
	public <R> List<R> findEntityList(CharSequence queryString, int maxResults, Map<String, Object> params) {
		if (!StringUtils.isEmpty(queryString)) {
			Query query = this.entityManager.createQuery(queryString.toString());
			if (params != null && !params.isEmpty()) {
				for (String paramKey : params.keySet()) {
					query.setParameter(paramKey, params.get(paramKey));
				}
			}
			if (maxResults > 0) {
				return (List<R>) query.setMaxResults(maxResults).getResultList();
			} else {
				return (List<R>) query.getResultList();
			}
		} else {
			return Collections.emptyList();
		}
	}

	@Override
	public List<T> findByProperty(Map<String, Object> conditions) {
		log.debug("finding " + entityClazz.getSimpleName() + " instance by multi properties:" + conditions);
		StringBuilder queryString = new StringBuilder();
		queryString.append("select e from " + this.entityClazz.getSimpleName() + " e ");
		if (conditions != null && !conditions.isEmpty()) {
			Iterator<String> it = conditions.keySet().iterator();
			String key = it.next();
			queryString.append(" where e." + key.trim() + " = :" + key.trim());
			while (it.hasNext()) {
				key = it.next();
				queryString.append(" and e." + key.trim() + " = :" + key.trim());
			}
		}
		TypedQuery<T> query = this.entityManager.createQuery(queryString.toString(), this.entityClazz);
		if (conditions != null && !conditions.isEmpty()) {
			for (String paramKey : conditions.keySet()) {
				query.setParameter(paramKey.trim(), conditions.get(paramKey));
			}
		}
		return query.getResultList();
	}

	@Override
	public List<T> findByProperty(String name, Object value) {
		log.debug("finding " + this.entityClazz.getSimpleName() + " instance by a property {" + name + "=" + value + "}");
		if (StringUtils.isEmpty(name) || value == null) {
			return Collections.emptyList();
		} else {
			Map<String, Object> conditions = new HashMap<String, Object>();
			conditions.put(name, value);
			return this.findByProperty(conditions);
		}
	}

	@Override
	public Page<T> findPagination(int pageIndex, int pageSize) {
		return this.findPagination(new HashMap<String, Object>(), pageIndex, pageSize);
	}

	@Override
	public Page<T> findPagination(Map<String, Object> params, int pageIndex, int pageSize) {
		return this.findPagination(params, null, pageIndex, pageSize);
	}

	public Page<T> findPagination(Map<String, Object> params, Map<String, String> orderParams, int pageIndex, int pageSize) {
		PageRequest pageRequest = new PageRequest(pageIndex - 1, pageSize);
		StringBuilder queryString = new StringBuilder("select e from " + this.entityClazz.getSimpleName() + " e where 1 = 1 ");
		StringBuilder countQueryString = new StringBuilder("select count(e) from " + this.entityClazz.getSimpleName() + " e where 1 = 1 ");
		if (params != null) {
			for (String key : params.keySet()) {
				if (BaseDao.COMPLEX.equalsIgnoreCase(key)) {
					queryString.append("and (" + params.get(key) + ") ");
					countQueryString.append("and (" + params.get(key) + ") ");
				} else {
					queryString.append(" and e." + key.trim() + " = :" + key.trim());
					countQueryString.append(" and e." + key.trim() + " = :" + key.trim());
				}
			}
		}
		//添加排序参数
		if (orderParams != null) {
			int i = 0;
			for (String orderField : orderParams.keySet()) {
				if (StringUtils.isNoneEmpty(orderField) && StringUtils.isNotEmpty(orderParams.get(orderField))) {
					String orderKeyWord = orderParams.get(orderField).trim();
					if (BaseDao.COMPLEX.equalsIgnoreCase(orderField)) {
						if (i > 0) {
							queryString.append(", " + orderKeyWord);
						} else {
							queryString.append(" order by " + orderKeyWord);
						}
					} else {
						if ("ASC".equalsIgnoreCase(orderKeyWord) || "DESC".equalsIgnoreCase(orderKeyWord)) {
							if (i > 0) {
								queryString.append(" , e." + orderField + " " + orderKeyWord);
							} else {
								queryString.append(" order by e." + orderField + " " + orderKeyWord);
							}
							i++;
						}
					}
				}
			}
		}

		//构造数据查询
		TypedQuery<T> pageQuery = this.entityManager.createQuery(queryString.toString(), this.entityClazz);
		//构造数量查询
		final TypedQuery<Long> countQuery = this.entityManager.createQuery(countQueryString.toString(), Long.class);
		if (params != null) {
			for (String key : params.keySet()) {
				pageQuery.setParameter(key.trim(), params.get(key));
				countQuery.setParameter(key.trim(), params.get(key));
			}
		}

		pageQuery.setFirstResult(pageRequest.getOffset());
		pageQuery.setMaxResults(pageRequest.getPageSize());
		List<T> pageData = pageQuery.getResultList();
		return PageableExecutionUtils.getPage(pageData, pageRequest, new PageableExecutionUtils.TotalSupplier() {
			@Override
			public long get() {
				long total = 0;
				List<Long> counts = countQuery.getResultList();
				for(Long element : counts) {
					total += element == null ? 0 : element;
				}
				return total;
			}
		});
	}

	@Override
	public Page<T> findPagination(String hql, String countHql, Map<String, Object> params, int pageIndex, int pageSize) {
		if (StringUtils.isEmpty(hql)) {
			return null;
		} else {
			PageRequest pageRequest = new PageRequest(pageIndex - 1, pageSize);
			TypedQuery<T> pageQuery = this.entityManager.createQuery(hql, this.entityClazz);
			TypedQuery<Long> countQuery = this.entityManager.createQuery(countHql, Long.class);

			if (params != null) {
				for (String key : params.keySet()) {
					pageQuery.setParameter(key.trim(), params.get(key));
					countQuery.setParameter(key.trim(), params.get(key));
				}
			}

			pageQuery.setFirstResult(pageRequest.getOffset());
			pageQuery.setMaxResults(pageRequest.getPageSize());
			List<T> pageData = pageQuery.getResultList();

			List<Long> counts = countQuery.getResultList();
			long total = 0;
			for (Long ct : counts) {
				total += ct == null ? 0 : ct;
			}
			final Long supplierCount = total;
			return PageableExecutionUtils.getPage(pageData, pageRequest, new PageableExecutionUtils.TotalSupplier() {
				@Override
				public long get() {
					return supplierCount;
				}
			});
		}
	}

	@Override
	public <R> Page<R> findEntityPagination(String hql, String countHql, Map<String, Object> params, int pageIndex, int pageSize) {
		if (StringUtils.isEmpty(hql)) {
			return null;
		} else {
			PageRequest pageRequest = new PageRequest(pageIndex - 1, pageSize);
			Query pageQuery = this.entityManager.createQuery(hql);
			TypedQuery<Long> countQuery = this.entityManager.createQuery(countHql, Long.class);

			if (params != null) {
				for (String key : params.keySet()) {
					pageQuery.setParameter(key.trim(), params.get(key));
					countQuery.setParameter(key.trim(), params.get(key));
				}
			}

			pageQuery.setFirstResult(pageRequest.getOffset());
			pageQuery.setMaxResults(pageRequest.getPageSize());
			List<?> pageData = pageQuery.getResultList();

			List<Long> counts = countQuery.getResultList();
			long total = 0;
			for (Long ct : counts) {
				total += ct == null ? 0 : ct;
			}
			final Long supplierCount = total;
			return (Page<R>) PageableExecutionUtils.getPage(pageData, pageRequest, new PageableExecutionUtils.TotalSupplier() {
				@Override
				public long get() {
					return supplierCount;
				}
			});
		}
	}

	@Override
	public Page<?> findSqlPaginationList(String querySql, Map<String, Object> params, int pageIndex, int pageSize) {
		if (StringUtils.isEmpty(querySql)) {
			return null;
		} else {
			PageRequest pageRequest = new PageRequest(pageIndex - 1, pageSize);
			Query pageQuery = this.entityManager.createNativeQuery(querySql);
			Query countQuery = this.entityManager.createNativeQuery("select count(1) from (" + querySql + ") cjpql");

			//构造数据查询
			if (params != null) {
				for(String key : params.keySet()) {
					pageQuery.setParameter(key.trim(), params.get(key));
					countQuery.setParameter(key.trim(), params.get(key));
				}
			}

			pageQuery.setFirstResult(pageRequest.getOffset());
			pageQuery.setMaxResults(pageRequest.getPageSize());
			pageQuery.unwrap(SQLQuery.class).setResultTransformer(Transformers.ALIAS_TO_ENTITY_MAP);
			List<?> pageData = pageQuery.getResultList();

			List<Object> counts = countQuery.getResultList();
			long total = 0;
			for (Object ele : counts) {
				total += ele == null ? 0 : Integer.valueOf(ele.toString());
			}
			final Long supplierTotal = total;
			return PageableExecutionUtils.getPage(pageData, pageRequest, new PageableExecutionUtils.TotalSupplier() {
				@Override
				public long get() {
					return supplierTotal;
				}
			});
		}
	}
}
