package com.ruidev.framework.user;

import java.util.Date;

public interface IUser
{
    Long getTenantId();
    void setTenantId(Long tenantId);
	Long getId();
	Integer getUserLevel();
	String getUsername();
	String getRealName();
	String getPassword();
	String getStatus();
	Date getCreateDate();
	String getPhone();
}