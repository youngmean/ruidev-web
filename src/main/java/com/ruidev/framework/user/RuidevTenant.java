package com.ruidev.framework.user;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;

import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;

import com.ruidev.admin.conf.util.ConfigurationUtil;
import com.ruidev.framework.constant.UserStatus;
import com.ruidev.framework.entity.CrudEntity;

@Entity
@DynamicInsert
@DynamicUpdate
@Table(name = "ruidev_system_tenant")
public class RuidevTenant extends CrudEntity {

	private static final long serialVersionUID = 1L;

	@NotNull
	@Column(name = "tenant_name")
	private String tenantName;
	
	@Column(name = "tenant_status", length = 1)
	private String status = UserStatus.NORMAL.getCode();

	/**
	 * 余额/财富(测试)
	 * 
	 * @return
	 */
	public Double getWealth() {
		return 0.0;
	}

	public String getTenantName() {
		return tenantName;
	}

	public void setTenantName(String tenantName) {
		this.tenantName = tenantName;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}
	
	public void setStatus(UserStatus status) {
		this.status = status.getCode();
	}
	
	public String getStatusStr() {
		return status != null ? ConfigurationUtil.getInstance().enumslabel(UserStatus.class, status) : "";
	}
	
	/**
	 * 账号是否正常
	 * @return
	 */
	public boolean getNormal() {
		return UserStatus.NORMAL.getCode().equals(status);
	}
	
	/**
	 * 账号是否被禁用
	 * @return
	 */
	public boolean getDisabled() {
		return UserStatus.DISABLED.getCode().equals(status);
	}
	
}