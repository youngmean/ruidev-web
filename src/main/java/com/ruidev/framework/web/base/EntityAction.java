package com.ruidev.framework.web.base;

import java.lang.reflect.ParameterizedType;

import org.apache.struts2.convention.annotation.Action;
import org.apache.struts2.convention.annotation.ParentPackage;
import org.apache.struts2.convention.annotation.Result;
import org.springframework.beans.factory.annotation.Autowired;

import com.ruidev.framework.bo.EntityBo;
import com.ruidev.framework.bo.GenericBo;
import com.ruidev.framework.entity.CrudEntity;
import com.ruidev.framework.exception.BizException;

@ParentPackage("ruidev-default")
@SuppressWarnings("unchecked")
public abstract class EntityAction<E extends CrudEntity, BO extends EntityBo<E>> extends AbsCrudAction<GenericBo> {

    /**
     * 实体 action 基类
     */
    private static final long serialVersionUID = 1L;
    protected Class<E> entityClass;
    
    public EntityAction(){
    	ParameterizedType type = (ParameterizedType) this.getClass().getGenericSuperclass();
        entityClass = (Class<E>)type.getActualTypeArguments()[0];
    }
    
    protected E object;
    protected volatile BO bo;
    protected Long id;
    
    @Action("save")
    public String save() throws Exception{
        object = bo.save(object);
        returnObject = object.getId();
        return JSON;
    }
    
    @Action("list")
    public String list() throws Exception{
    	returnObject = objects = bo.getListPage();
        return SUCCESS;
    }
    
    @Action(value = "add", results = {@Result(name = "success", location="edit.ftl")})
    public String add() throws Exception{
    	if(object == null){
    		object = entityClass.newInstance();
    	}
        return SUCCESS;
    }
    
    @Action("edit")
    public String edit() throws Exception{
        id = (id == null)? (object == null?null:object.getId()) : id;
        if(id != null){
            returnObject = object = bo.get(id);
        }else{
        	throw new BizException("缺少对象参数:id");
        }
        return SUCCESS;
    }
    
    @Action("view")
    public String view() throws Exception{
        String result = edit();
        return result;
    }
    
    @Action("delete")
    public String delete() throws Exception{
    	id = (id == null)? (object == null?null:object.getId()) : id;
    	if(id == null){
    		throw new BizException("缺少删除参数:id");
    	}
        bo.delete(id);
        returnObject = id;
        return JSON;
    }
    
    public E getObject() {
        return object;
    }
    
    public void setObject(E object) {
        this.object = object;
    }
    
    public void setObject(Object object) {
        this.object = (E)object;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

	@Autowired
	public void setBo(BO bo) {
		this.bo = bo;
	}
	
	public BO getBo() {
		return bo;
	}

	public void setBo(GenericBo bo) {}
}
