package com.ruidev.framework.entity;

import javax.persistence.Column;
import javax.persistence.MappedSuperclass;

import org.hibernate.annotations.Filter;
import org.hibernate.annotations.FilterDef;
import org.hibernate.annotations.FilterDefs;
import org.hibernate.annotations.Filters;
import org.hibernate.annotations.ParamDef;

/**
 * 租户相关的实体类的基类
 */
@MappedSuperclass
@FilterDefs({@FilterDef(name="tenantFilter", parameters=@ParamDef(name="tenantId", type="long"))})
@Filters({@Filter(name="tenantFilter", condition="tenant_id=:tenantId")})
public abstract class CrudTenantEntity extends CrudEntity {

	private static final long serialVersionUID = 1L;
	/**
	 * 租户id(不可更新)
	 */
	@Column(name = "tenant_id", updatable = false)
	protected Long tenantId;

	public Long getTenantId() {
		return tenantId;
	}

	public void setTenantId(Long tenantId) {
		this.tenantId = tenantId;
	}

}
