package com.ruidev.framework.bo;

import java.lang.reflect.Field;
import java.util.Collection;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.hibernate.Session;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.ClassUtils;

import com.ruidev.framework.annotations.LogicalDeletableEntity;
import com.ruidev.framework.annotations.UserDataEntity;
import com.ruidev.framework.annotations.WebResourceEntity;
import com.ruidev.framework.constant.BaseConstants;
import com.ruidev.framework.dao.HibernateProxy;
import com.ruidev.framework.entity.CrudEntity;
import com.ruidev.framework.entity.CrudTenantEntity;
import com.ruidev.framework.exception.BizException;
import com.ruidev.framework.util.CommonUtil;
import com.ruidev.framework.util.LoginContext;
import com.ruidev.framework.util.RequestUtil;

/**
 * 通用的业务操作
 */
public class GenericBo {

	protected final Logger log = LogManager.getLogger(this.getClass());
	
	@Autowired
	private HibernateProxy dao;

	public void setDao(HibernateProxy dao) {
		this.dao = dao;
	}
	
	/**
	 * 获取单条数据
	 * 
	 * @param clazz
	 *            要获取的实体类
	 * @param id
	 *            主键
	 * @return 要获取的数据
	 * @throws Exception
	 */
	public <T> T getData(Class<T> clazz, Long id) throws Exception {
		if (id == null) {
			throw new BizException("缺少参数:id");
		}
		T obj = dao.get(clazz, id);
		if (obj == null) {
			throw new BizException("该条数据不存在");
		}
		if (LoginContext.isCurrentRequestPublic()) {
			return obj;
		}
		if (!LoginContext.isCurrentUserAdmin() && !LoginContext.isCurrentRequestPublic()) {
			if (CrudTenantEntity.class.isAssignableFrom(clazz)) {
				CrudTenantEntity _obj = (CrudTenantEntity) obj;
				if (_obj.getTenantId() != null && !LoginContext.getCurrentLoginUserTenantId().equals(_obj.getTenantId())) {
					BizException bize = new BizException("你无操作该条数据的权限", 401);
					throw bize;
				}
			}
			if (clazz.isAnnotationPresent(UserDataEntity.class)) {
				CrudEntity _obj = (CrudEntity) obj;
				String field = clazz.getAnnotation(UserDataEntity.class).field();
				Long userDataId = _obj.getCreateBy();
				if(!StringUtils.isEmpty(field)) {
					Field _field = clazz.getDeclaredField(field);
					_field.setAccessible(true);
					userDataId = (Long) _field.get(_obj);
				}
				if (!LoginContext.getCurrentLoginUserId().equals(userDataId)) {
					BizException bize = new BizException("你无操作该条数据的权限", 401);
					bize.setErrorId(401);
					throw bize;
				}
			}
			if (clazz.isAnnotationPresent(LogicalDeletableEntity.class)) {
				CrudEntity _obj = (CrudEntity) obj;
				if (BaseConstants.DATA_DELETED.equals(_obj.getDataFlag())) {
					BizException bize = new BizException("该数据已被删除", 401);
					throw bize;
				}
			}
		}
		return obj;
	}

	public <T> T getData(Class<T> clazz, String id) throws Exception {
		return dao.get(clazz, id);
	}
	
    public <T> void deleteData(Class<T> clazz, Long id) throws Exception{
        dao.delete(clazz, id);
    }

	public <T> T getFirstData(String hsql, Object... params) throws Exception {
		return dao.getFirstResult(hsql, params);
	}
	
	public Object[] getFirstJdbcData(String sql, Object... params) throws Exception {
		return dao.getFirstJdbcResult(sql, params);
	}
	
	public <T> T getSingleJdbcData(String sql, Object... params) throws Exception {
		return dao.getFirstJdbcData(sql, params);
	}

	public <T> List<T> getAllData(Class<T> clazz) throws Exception {
		return getAllData(CommonUtil.combineStrings("from ", clazz.getName()));
	}

	public <T> List<T> getAllData(String sql, Object... params) {
		return dao.getAll(sql, params);
	}

	public <T> List<T> getListPageData(String sql, Object... params) throws Exception {
		return dao.getListByPagination(sql, 1, params);
	}

	public List<Object[]> getJdbcListPage(String sql, Object... params) throws Exception {
		return dao.getListByPagination(sql, 2, params);
	}

	public List<Object[]> getJdbcList(String sql, Object... params) {
		return dao.getJdbcList(sql, params);
	}

	public void removeObjectFromSession(Object obj) {
		dao.removeObjectFromSession(obj);
	}

	public void saveData(Collection<?> objs) throws Exception {
		dao.save(objs);
	}

	public <T> T saveData(T data) throws Exception {
		if (data instanceof CrudTenantEntity) {
			CrudTenantEntity tenantData = (CrudTenantEntity) data;
			Class<?> userClass = ClassUtils.getUserClass(tenantData.getClass());
			String entityName = userClass.getName();
			if (entityName.contains("_$$_")) {
				entityName = entityName.substring(0, entityName.indexOf("_$$_"));
			}
			int _dollerCharIndex = entityName.indexOf("$");
			if(_dollerCharIndex > 0) {
				entityName = entityName.substring(0, _dollerCharIndex);
			}
			if (tenantData.getId() != null) {
				Long id = tenantData.getId();
				if (!LoginContext.isCurrentUserAdmin()) {
					Long tenantId = null;
					String hql = CommonUtil.combineStrings("select tenantId from ", entityName, " where id = ?");
					tenantId = dao.getFirstResult(hql, id);
					if (tenantId != null
							&& LoginContext.getCurrentLoginUserTenantId() != null
							&& !LoginContext.getCurrentLoginUserTenantId()
									.equals(tenantId)) {
						throw new BizException("无权限保存该数据");
					}
					if (userClass.isAnnotationPresent(UserDataEntity.class)) {
						String userIdColumn = "createBy";
						String field = userClass.getAnnotation(UserDataEntity.class).field();
						if(!StringUtils.isEmpty(field)) {
							userIdColumn = field;
						}
						Long createBy = dao.getFirstResult(CommonUtil.combineStrings("select ", userIdColumn, " from ", entityName, " where id = ?"), id);
						if (!LoginContext.getCurrentLoginUserId().equals(createBy)) {
							throw new BizException("无权限保存该数据");
						}
					}
					if (userClass.isAnnotationPresent(LogicalDeletableEntity.class)) {
						String dataFlag = dao.getFirstResult(CommonUtil.combineStrings("select dataFlag from ", entityName, " where id = ?"), id);
						if (BaseConstants.DATA_DELETED.equals(dataFlag)) {
							throw new BizException("该数据已被删除");
						}
					}
				}
				if (userClass.isAnnotationPresent(WebResourceEntity.class)) {
					String[] fields = userClass.getAnnotation(WebResourceEntity.class).fields();
					for (String field : fields) {
						String url = dao.getFirstResult(CommonUtil.combineStrings("select ", field, " from ", entityName, " where id = ?"), id);
						try {
							if (!StringUtils.isEmpty(url)) {
								Field _field = userClass.getDeclaredField(field);
								_field.setAccessible(true);
								String _url = (String) _field.get(tenantData);
								if (!url.equals(_url)) {
									RequestUtil.deleteWebResource(url);
								}
							}
						} catch (Exception e) {
							log.error(CommonUtil.combineStrings("failed to delete web resource -> ", url));
						}
					}
				}
			}
		}
		dao.save(data);
		return data;
	}


	public int executeUpdateHql(String queryString, Object... values) {
		return dao.executeUpdate(queryString, 1, values);
	}

	public int executeUpdateSql(String queryString, Object... values) {
		return dao.executeUpdate(queryString, 2, values);
	}

	public void clearSession() {
		dao.clearSession();
	}

	protected Session getCurrentSession() {
		return dao.getSession();
	}
}
