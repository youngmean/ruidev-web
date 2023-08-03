package com.ruidev.framework.dao;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.util.Strings;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.query.Query;
import org.hibernate.transform.Transformers;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.ruidev.framework.constant.BaseConstants;
import com.ruidev.framework.entity.CrudEntity;
import com.ruidev.framework.entity.CrudTenantEntity;
import com.ruidev.framework.util.CommonUtil;
import com.ruidev.framework.util.CrudContext;
import com.ruidev.framework.util.DateTimeUtil;
import com.ruidev.framework.util.HSqlUtil;
import com.ruidev.framework.util.LoginContext;
import com.ruidev.framework.util.RequestContext;

/**
 * 数据访问层
 * @author: 	锐开科技 
 * @Copyright: 	www.ruidev.com All rights reserved.
 */
@Repository("baseDao")
@SuppressWarnings("unchecked")
public class HibernateProxy {
	@Autowired
	protected SessionFactory sessionFactory;

	private static final Logger log = LogManager.getLogger(HibernateProxy.class);

	public <T> T get(Class<T> clazz, Serializable s) {
		return (T) getSession().get(clazz, s);
	}

	public <T> List<T> find(String hql, boolean singleResult, Object... params) {
//		hql = HSqlUtil.getJPAStyledHSql(hql);
		Integer maxResults = null;
		if(!singleResult && hql.matches(".*limit\\s+\\d+.*")) {
			String limitStr = hql.replaceAll(".*limit\\s+(\\d+).*", "$1");
			hql = hql.replaceAll("(.*)\\s+limit\\s+(\\d+)(.*)", "$1 $3");
			maxResults = Integer.valueOf(limitStr);
		}
//		Query<T> query = getSession().createQuery(hql);// .setCacheable(true);
		Query<T> query = createQuery(hql, params);
//		for (Integer i = 0; i < params.length; i++) {
//			String pos = CommonUtil.combineStrings("_", i.toString());
//			if(params[i] instanceof Collection<?>) {
//				query.setParameterList(pos, (Collection<?>)params[i]);
//			}else {
//				query.setParameter(pos, params[i]);
//			}
//		}
		if (singleResult) {
			query.setMaxResults(1);
		}else if(maxResults != null && maxResults > 0) {
			query.setMaxResults(maxResults);
		}
		return query.list();
	}

	public <T> void save(T obj) throws Exception {
		if(obj instanceof Collection) {
			Collection<?> objs = (Collection<?>)obj;
			for(Object o : objs) {
				if (o instanceof CrudEntity) {
					setCrudEntityData((CrudEntity) o);
				}
				getSession().saveOrUpdate(o);
			}
		}else if (obj instanceof CrudEntity) {
			setCrudEntityData((CrudEntity) obj);
			getSession().saveOrUpdate(obj);
		}else {
			getSession().saveOrUpdate(obj);
		}
	}

	public void clearSession() {
		getSession().clear();
	}

	public <T> void delete(Class<T> clazz, Serializable s) {
		getSession().delete(get(clazz, s));
	}

	public int executeUpdate(String s, int type, Object... params) {
		int c = -1;
		Query<?> query;
//		s = HSqlUtil.getJPAStyledHSql(s);
		if (type == 1) {
			query = createQuery(s);
		} else {
			query = createNativeQuery(s);
		}
		if (params != null) {
			for (Integer i = 0; i < params.length; i++) {
				String pos = CommonUtil.combineStrings("_", i.toString());
				if(params[i] instanceof Collection<?>) {
            		query.setParameterList(pos, (Collection<?>)params[i]);
            	}else {
            		query.setParameter(pos, params[i]);
            	}
			}
		}
		c = query.executeUpdate();
		log.debug("updateWithSql更改的记录数量为：" + c);
		return c;
	}

	public List<Object[]> getListWithJdbcSql(String sql, boolean singleResult, Object... params) {
//		sql = HSqlUtil.getJPAStyledHSql(sql);
//		Query<Object[]> query = getSession().createNativeQuery(sql);
		Query<Object[]> query = createNativeQuery(sql, params);
//		if (params != null) {
//			for (Integer i = 0; i < params.length; i++) {
//				String pos = CommonUtil.combineStrings("_", i.toString());
//				if(params[i] instanceof Collection<?>) {
//            		query.setParameterList(pos, (Collection<?>)params[i]);
//            	}else {
//            		query.setParameter(pos, params[i]);
//            	}
//			}
//		}
		if (singleResult) {
			query.setFetchSize(1);
		}
		return query.list();
	}
	
	public <T>List<T> getListDataWithJdbcSql(String sql, boolean singleResult, Object... params) {
//		sql = HSqlUtil.getJPAStyledHSql(sql);
//		Query<T> query = getSession().createNativeQuery(sql);
		Query<T> query = createNativeQuery(sql);
//		if (params != null) {
//			for (Integer i = 0; i < params.length; i++) {
//				String pos = CommonUtil.combineStrings("_", i.toString());
//				if(params[i] instanceof Collection<?>) {
//            		query.setParameterList(pos, (Collection<?>)params[i]);
//            	}else {
//            		query.setParameter(pos, params[i]);
//            	}
//			}
//		}
		if (singleResult) {
			query.setFetchSize(1);
		}
		return query.list();
	}

	@SuppressWarnings("deprecation")
	public <T> List<T> getListByPagination(String sql, int type, Object... params) throws Exception {
		Query<T> query = null;
		Query<?> queryCount = null;
		int totalRecords = 0;
//		Session session = getSession();
		String finalSQL = RequestContext.getHsql(sql).replaceAll("\\s{1,}", " ").trim();
//		finalSQL = HSqlUtil.getJPAStyledHSql(finalSQL);
		Object[] boundParams = RequestContext.getFilterParams(params);
		boolean countData = !RequestContext.getNoCount();
		if (type == 1) {
			List<String> onlyFetchProps = RequestContext.getOnlyFetchProperties();
			if(RequestContext.getTransformerClass() != null && onlyFetchProps != null && finalSQL.trim().startsWith("from")) {
				List<String> props = new ArrayList<String>();
				for(String prop : onlyFetchProps) {
					props.add(CommonUtil.combineStrings(prop, " as ", prop));
				}
				finalSQL = CommonUtil.combineStrings("select ", Strings.join(props, ','), " ", finalSQL);
				query = createQuery(finalSQL, countData, boundParams);
				query.setResultTransformer(Transformers.aliasToBean(RequestContext.getTransformerClass()));
			}else {
				query = createQuery(finalSQL, countData, boundParams);
			}
			if(countData) {
				String countSQL = CommonUtil.combineStrings("select count(id) ", query.getQueryString());
				if (!finalSQL.trim().toLowerCase().startsWith("from ")) {
					String columnSql = finalSQL.substring(0, finalSQL.toLowerCase().indexOf("from")) .replace("select", "").trim();
					String[] columns = columnSql.split(",");
					String firstCol = columns[0];
					if(firstCol.contains(" ")) {
						firstCol = firstCol.substring(0, firstCol.indexOf(" "));
					}
					countSQL = CommonUtil.combineStrings("select count(", firstCol, ") ", finalSQL.substring(finalSQL.toLowerCase().indexOf("from")));
				}
				queryCount = createQuery(countSQL, CrudContext.fetchBoundParams());
			}
		} else {
			query = createNativeQuery(finalSQL, countData, boundParams);
			if(countData) {
				String countSql = "", querySql = query.getQueryString();
//				if (querySql.toLowerCase().contains(" group by ")) {
					String aliasStr = null;
					if (BaseConstants.DB_IS_ORACLE) {
						aliasStr = "";
					} else {
						aliasStr = "as";
					}
					countSql = CommonUtil.combineStrings("select count(*) from (", querySql, ") ", aliasStr, " mytable");
//				} else {
//					int fromIndex = querySql.toLowerCase().indexOf(" from ");
//					countSql = CommonUtil.combineStrings("select count(*) ", querySql.substring(fromIndex));
//				}
				queryCount = createNativeQuery(countSql, CrudContext.fetchBoundParams());
			}
		}
//		Integer j = 0;
//		for (int i = 0; i < boundParams.length; i++) {
//			String pos = CommonUtil.combineStrings("_", j.toString());
//			Object param = boundParams[i];
//			if ("IS_NULL".equals(param) || param == null) {
//				continue;
//			}
//			if (param instanceof Collection<?>) {
//				query.setParameterList(pos, (Collection<?>) param);
//				if (queryCount != null) {
//					queryCount.setParameterList(pos, (Collection<?>) param);
//				}
//			} else {
//				query.setParameter(pos, param);
//				if (queryCount != null) {
//					queryCount.setParameter(pos, param);
//				}
//			}
//			j++;
//		}
		if (queryCount != null) {
			List<?> countRecord = queryCount.list();
			if (countRecord.size() == 1) {
				totalRecords = Integer.parseInt(countRecord.get(0).toString());
			} else {
				totalRecords = countRecord.size();
			}
			RequestContext.setTotal(totalRecords);
		}
		query.setFirstResult(RequestContext.getBeginIndex());
		query.setMaxResults(RequestContext.getSize());
		List<T> list = query.list();
		return list;
	}

	public void setSessionFactory(SessionFactory sessionFactory) {
		this.sessionFactory = sessionFactory;
	}

	public Session getSession() {
		Session session = null;
		String dataSourceName = RequestContext.getCurrentDataSourceName();
		SessionFactory sf = null;
		if(!StringUtils.isEmpty(dataSourceName)) {
			sf = (SessionFactory) BaseConstants.SPRING_APPLICATION_CONTEXT.getBean(dataSourceName);
		}else {
			sf = sessionFactory;
		}
		session = sf.getCurrentSession();
		return session;// .openSession();
	}

	/**
	 * 带有参数查询，获取多条数据
	 * 
	 * @param hql
	 *            查询语句
	 * @param flag
	 *            是否查询单条数据
	 * @param parms
	 *            查询参数
	 * @return
	 */
	public <T> List<T> getAll(String hql, Object... parms) {
		return find(hql, false, parms);
	}

	/**
	 * 根据sql语句来进行查询操作
	 * 
	 * @param sql
	 *            传统的jdbc sql语句
	 * @param params
	 *            问号参数数组
	 * @return 
	 * @return 复合条件的记录
	 */
	public List<Object[]> getJdbcList(String sql, Object... params) {
		return getListWithJdbcSql(sql, false, params);
	}

	public Object[] getFirstJdbcResult(String sql, Object... params) {
		List<Object[]> results = getListWithJdbcSql(sql, true, params);
		if (results.size() > 0) {
			return results.get(0);
		}
		return null;
	}
	
	public <T>T getFirstJdbcData(String sql, Object... params) {
		List<T> results = getListDataWithJdbcSql(sql, true, params);
		if (results.size() > 0) {
			return results.get(0);
		}
		return null;
	}

	/**
	 * 获取单条记录
	 * 
	 * @param hsql
	 *            查询语句
	 * @param parms
	 *            查询参数
	 * @return 单个字符串
	 */
	public <T> T getFirstResult(String hql, Object... parms) {
		Iterator<T> it = (Iterator<T>) find(hql, true, parms).iterator();
		if (it.hasNext()) {
			T o = it.next();
			return o;
		}
		return null;
	}

	public void removeObjectFromSession(Object obj) {
		getSession().evict(obj);
	}

	/**
	 * 设置数据的更新者 创建者信息 租户信息
	 * 
	 * @param data
	 * @throws Exception
	 */
	private void setCrudEntityData(CrudEntity data) throws Exception {
		Long currentLoginUserId = LoginContext.getCurrentLoginUserId();
		Date currentTime = DateTimeUtil.getCurrentTime();
		if (data.getId() == null) {
			data.setCreateBy(currentLoginUserId);
			data.setCreateDate(currentTime);
		}
		data.setUpdateBy(currentLoginUserId);
		data.setUpdateDate(currentTime);
		if (data instanceof CrudTenantEntity) {
			CrudTenantEntity cdata = (CrudTenantEntity) data;
			if (!LoginContext.isCurrentUserAdmin() && LoginContext.getCurrentLoginUserTenantId() != null) {
				cdata.setTenantId(LoginContext.getCurrentLoginUserTenantId());
			}
		}
	}

	private <T> Query<T> createNativeQuery(String sql, Object... params) {
		return createQueryData(sql, true, false, params);
	}
	
	private <T> Query<T> createNativeQuery(String sql, boolean keepBoundParams, Object... params) {
		return createQueryData(sql, true, keepBoundParams, params);
	}
	
	private <T> Query<T> createQuery(String hql, Object... params) {
		return createQueryData(hql, false, false, params);
	}
	
	private <T> Query<T> createQuery(String hql, boolean keepBoundParams, Object... params) {
		return createQueryData(hql, false, keepBoundParams, params);
	}
	
	private <T> Query<T> createQueryData(String hql, boolean isNative, boolean keepBoundParams, Object... params) {
		hql = CrudContext.getHsql(hql);
		hql = HSqlUtil.getJPAStyledHSql(hql);
		Query<T> query = null;
		try {
			query = isNative ? getSession().createNativeQuery(hql) : getSession().createQuery(hql);
		} catch (Exception e) {
			CrudContext.clearFilters();
			throw e;
		}
		params = CrudContext.getFilterParams(params);
		if(params == null || params.length < 1)return query;
		for (int i = 0, ilen = params.length; i < ilen; ++i) {
			String pos = CommonUtil.combineStrings("_", i);
			if(params[i] instanceof Collection<?>) {
				query.setParameterList(pos, (Collection<?>)params[i]);
			}else {
				query.setParameter(pos, params[i]);
			}
		}
		if(keepBoundParams) {
			CrudContext.setBoundParams(params);
		}
		CrudContext.clearFilters();
		return query;
	}
	
}