package com.ruidev.admin.conf.action;

import org.apache.struts2.convention.annotation.Action;
import org.apache.struts2.convention.annotation.Namespace;

import com.ruidev.admin.conf.bo.ConfBo;
import com.ruidev.admin.conf.entity.Configuration;
import com.ruidev.framework.util.ActionPermissionUtil;
import com.ruidev.framework.web.base.EntityAction;

@Namespace("/admin/conf")
public class ConfAction extends EntityAction<Configuration, ConfBo> {

	private static final long serialVersionUID = 1L;

	@Action("refreshpermissions")
	public String refreshPermissions() {
		ActionPermissionUtil.initPermissionDefinitions();
		returnObject = "success";
		return JSON;
	}
}
