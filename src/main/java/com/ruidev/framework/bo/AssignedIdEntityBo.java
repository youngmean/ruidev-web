package com.ruidev.framework.bo;

import java.lang.reflect.ParameterizedType;
import java.util.Collection;
import java.util.Date;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.hibernate.Session;

import com.ruidev.framework.annotations.LogicalDeletableEntity;
import com.ruidev.framework.annotations.UserDataEntity;
import com.ruidev.framework.constant.BaseConstants;
import com.ruidev.framework.entity.AssignedIdCrudEntity;
import com.ruidev.framework.entity.CrudTenantEntity;
import com.ruidev.framework.exception.BizException;
import com.ruidev.framework.util.CommonUtil;
import com.ruidev.framework.util.DateTimeUtil;
import com.ruidev.framework.util.LoginContext;
import com.ruidev.framework.util.RequestContext;

/**
 * 实体相关的业务操作,简化方法名<br>
 * 例: get(id)查询出来的数据对应于getData(Class&lt;E>, id)<br>
 * 例: delete(id)删除的数据对应于deleteData(Class&lt;E>, id)<br>
 * 例: save(object)操作的数据对应于saveData(Object&lt;E>)<br>
 */
public abstract class AssignedIdEntityBo<E extends AssignedIdCrudEntity> extends GenericBo {
    
    protected Class<E> entityClass;
    
    @SuppressWarnings("unchecked")
	public AssignedIdEntityBo(){
        try {
            ParameterizedType type = (ParameterizedType) this.getClass().getGenericSuperclass();
            entityClass = (Class<E>)type.getActualTypeArguments()[0];
        } catch (Exception e) {}
    }

    /**
     * 根据id删除数据
     * @param id
     * @throws Exception
     */
    public void delete(Long id) throws Exception{
        super.deleteData(entityClass, id);
    }
	
	public <T> void delete(Class<T> clazz, Long id) throws Exception {
		super.getData(clazz, id);
		super.deleteData(clazz, id);
	}
    
    public void save(Collection<E> objs) throws Exception {
    	super.saveData(objs);
    }
    
    /**
     * 保存对象到数据库
     * @param object
     * @return
     * @throws Exception
     */
    public E save(E object) throws Exception{
    	object.setDataFlag(BaseConstants.DATA_UNDELETED);
    	E origObject = null;
    	if(object.getId() != null){
    		origObject = get(object.getId());
    		CommonUtil.copyPropertiesIgnoreNull(object, origObject);
    		return super.saveData(origObject);
    	}
        return super.saveData(object);
    }
    
    /**
     * 根据id从数据库获取该条数据
     * @param id
     * @return
     * @throws Exception
     */
    public E get(Long id) throws Exception{
    	E obj = super.getData(entityClass, id);
        return obj;
    }
    
    /**
     * 数据过滤
     */
    protected void enableListFilter(){
    	if(!LoginContext.isCurrentUserAdmin() && !LoginContext.isCurrentRequestPublic()){
    		Session session = getCurrentSession();
    		if((LoginContext.isCurrentUserTenantUser() || LoginContext.isCurrentUserTenant()) && CrudTenantEntity.class.isAssignableFrom(entityClass)){
    			session.enableFilter("tenantFilter").setParameter("tenantId", LoginContext.getCurrentLoginUserTenantId());
    		}
    		if(entityClass.isAnnotationPresent(UserDataEntity.class)){
    			session.enableFilter("createbyFilter").setParameter("createBy", LoginContext.getCurrentLoginUserId());
    		}
    		if(entityClass.isAnnotationPresent(LogicalDeletableEntity.class)){
    			session.enableFilter("logicaldelFilter").setParameter("dataFlag", BaseConstants.DATA_UNDELETED);
    		}
    	}
    }
    
    /**
     * 清空数据过滤
     */
    protected void clearListFilter(){
    	if(!LoginContext.isCurrentUserAdmin() && !LoginContext.isCurrentRequestPublic()){
    		Session session = getCurrentSession();
    		if((LoginContext.isCurrentUserTenantUser() || LoginContext.isCurrentUserTenant()) && CrudTenantEntity.class.isAssignableFrom(entityClass)){
    			session.disableFilter("tenantFilter");
    		}
    		if(entityClass.isAnnotationPresent(UserDataEntity.class)){
    			session.disableFilter("createbyFilter");
    		}
    		if(entityClass.isAnnotationPresent(LogicalDeletableEntity.class)){
    			session.disableFilter("logicaldelFilter");
    		}
    	}
    }
    
    /**
     * 分页查询
     */
	public List<E> getListPage(String where, Object ...params) throws Exception{
		enableListFilter();
    	if(params.length > 0){
    		List<E> objects = super.getListPageData(CommonUtil.combineStrings("from ", entityClass.getName(), " where ", where), params);
    		clearListFilter();
    		return objects;
        }
    	if(!StringUtils.isEmpty(where)){
    		where = " where " + where;
    	}else{
    		where = "";
    	}
    	List<E> objects = super.getListPageData(CommonUtil.combineStrings("from ", entityClass.getName(), where));
    	clearListFilter();
        return objects;
    }
	
	public <T> List<T> getListPageByHql(String hql, Object...params) throws Exception{
		enableListFilter();
		List<T> objects = super.getListPageData(hql, params);
		clearListFilter();
		return objects;
	}
	
    /**
     * 分页查询
     */
	public <T>List<T> getListPage(Class<T> clazz, String where, Object ...params) throws Exception{
		enableListFilter();
    	if(params.length > 0){
    		List<T> objects = super.getListPageData(CommonUtil.combineStrings("from ", clazz.getName(), " where ", where), params);
    		clearListFilter();
    		return objects;
        }
    	if(!StringUtils.isEmpty(where)){
    		where = " where " + where;
    	}else{
    		where = "";
    	}
    	List<T> objects = super.getListPageData(CommonUtil.combineStrings("from ", clazz.getName(), where));
    	clearListFilter();
        return objects;
    }
    /**
     * 分页查询
     */
	public <T>List<T> getListPage(Class<T> clazz) throws Exception{
        return getListPage(clazz, "");
    }
    
    /**
     * 分页查询
     */
    public List<E> getListPage() throws Exception{
    	enableListFilter();
    	if(RequestContext.getOrderBys() == null || RequestContext.getOrderBys().size() < 1) {
    		RequestContext.addOrderBy("updateDate", "desc");
    	}
    	List<E> objects = super.getListPageData(CommonUtil.combineStrings("from ", entityClass.getName()));
    	clearListFilter();
        return objects;
    }
    
    /**
     * 查询所有(当前实体类)
     */
    public List<E> getList(String where, Object ...params) throws Exception{
    	enableListFilter();
    	if(params.length > 0){
    		List<E> objects = super.getAllData(CommonUtil.combineStrings("from ", entityClass.getName(), " where ", where), params);
    		clearListFilter();
    		return objects;
        }
    	if(!StringUtils.isEmpty(where)){
    		where = CommonUtil.combineStrings(" where ", where);
    	}else{
    		where = "";
    	}
    	List<E> objects = super.getAllData(CommonUtil.combineStrings("from ", entityClass.getName(), where));
    	clearListFilter();
        return objects;
    }
    
    /**
     * 查询所有
     */
    public <T> List<T> getList(Class<T> clazz, String where, Object ...params) throws Exception{
    	enableListFilter();
    	if(params.length > 0){
    		List<T> objects = super.getAllData(CommonUtil.combineStrings("from ", clazz.getName(), " where ", where), params);
    		clearListFilter();
    		return objects;
        }
    	if(!StringUtils.isEmpty(where)){
    		where = CommonUtil.combineStrings(" where ", where);
    	}else{
    		where = "";
    	}
    	List<T> objects = super.getAllData(CommonUtil.combineStrings("from ", clazz.getName(), where));
    	clearListFilter();
        return objects;
    }
    
    /**
     * 查询所有
     */
    public <T> List<T> getList(Class<T> clazz) throws Exception{
    	enableListFilter();
    	List<T> objects = super.getAllData(CommonUtil.combineStrings("from ", clazz.getName()));
    	clearListFilter();
        return objects;
    }
    
    public E getFirst(String where, Object... params)  throws Exception{
    	enableListFilter();
    	if(params.length > 0){
    		E object = super.getFirstData(CommonUtil.combineStrings("from ", entityClass.getName(), " where ", where), params);
        	clearListFilter();
            return object;
        }
    	if(!StringUtils.isEmpty(where)){
    		where = CommonUtil.combineStrings(" where ", where);
    	}else{
    		where = "";
    	}
    	E object = super.getFirstData(CommonUtil.combineStrings("from ", entityClass.getName(), where));
    	clearListFilter();
        return object;
    }
    
    /**
     * 启用过滤查询 取第一条数据
     * @param hql
     * @param params
     * @return
     * @throws Exception
     */
    public <T> T getFirstByHql(String hql, Object... params)  throws Exception{
    	enableListFilter();
    	T object = super.getFirstData(hql, params);
    	clearListFilter();
        return object;
    }
    
    /**
     * 查询所有数据
     * @return
     * @throws Exception 
     */
	public List<E> getAll() throws Exception{
		enableListFilter();
		List<E> objects = super.getAllData(entityClass);
		clearListFilter();
    	return objects;
    }

	public List<E> getAll(Class<E> clazz) throws Exception{
		enableListFilter();
		List<E> objects = super.getAllData(clazz);
		clearListFilter();
		return objects;
	}

	public Class<E> getEntityClass() {
		return entityClass;
	}
	
    public void throwBizException(Object...strings) throws BizException {
    	throw new BizException(CommonUtil.combineStrings(strings));
    }
    
    public <T> T saveData(T data) throws Exception {
    	if (data instanceof AssignedIdCrudEntity) {
    		AssignedIdCrudEntity obj = (AssignedIdCrudEntity)data;
    		setCrudEntityData(obj);
    		if(obj.getId() == null) {
    			getCurrentSession().save(obj);
    		}else {
    			Long id = getFirstData("select id from " + obj.getClass().getName() + " where id = ?", obj.getId());
    			if(id == null) {
        			getCurrentSession().save(obj);
    			}else {
    				getCurrentSession().update(obj);
    			}
    		}
    	}
    	return data;
    }
    
    /**
	 * 设置数据的更新者 创建者信息 租户信息
	 * 
	 * @param data
	 * @throws Exception
	 */
	private void setCrudEntityData(AssignedIdCrudEntity data) throws Exception {
		Long currentLoginUserId = LoginContext.getCurrentLoginUserId();
		Date currentTime = DateTimeUtil.getCurrentTime();
		if (data.getId() == null) {
			data.setCreateBy(currentLoginUserId);
			data.setCreateDate(currentTime);
		}
		data.setUpdateBy(currentLoginUserId);
		data.setUpdateDate(currentTime);
	}
}
