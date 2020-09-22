package com.ruidev.admin.role.action;

import org.apache.struts2.convention.annotation.Namespace;

import com.ruidev.admin.role.bo.RuidevRoleBo;
import com.ruidev.framework.user.RuidevRole;
import com.ruidev.framework.web.base.EntityAction;

@Namespace("/admin/role")
public class RuidevRoleAction extends EntityAction<RuidevRole, RuidevRoleBo> {

	private static final long serialVersionUID = 1L;
	
}
