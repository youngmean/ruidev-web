package com.ruidev.admin.tenant.action;

import org.apache.struts2.convention.annotation.Action;
import org.apache.struts2.convention.annotation.Namespace;

import com.ruidev.admin.tenant.bo.TenantBo;
import com.ruidev.framework.constant.RoleLevel;
import com.ruidev.framework.user.RuidevTenant;
import com.ruidev.framework.user.RuidevUser;
import com.ruidev.framework.web.base.EntityAction;

@Namespace("/admin/tenant")
public class TenantAction extends EntityAction<RuidevTenant, TenantBo> {

	private static final long serialVersionUID = 1L;
	/**
	 * 是否同时创建管理员账号
	 */
	private String createAdmin = "N";
	private RuidevUser admin;

	@Action("edit")
	public String edit() throws Exception {
		int level = RoleLevel.ROLE_ADMIN.getLevel();
		objects = bo.getAllData("from RuidevRoleGroup where roleGroupLevel = ?",
				level);
		return super.edit();
	}

	@Action("save")
	public String save() throws Exception {
		returnObject = object = bo.createOrUpdate(object, "Y".equals(createAdmin), admin);
		return JSON;
	}

	public String getCreateAdmin() {
		return createAdmin;
	}

	public void setCreateAdmin(String createAdmin) {
		this.createAdmin = createAdmin;
	}

	public RuidevUser getAdmin() {
		return admin;
	}

	public void setAdmin(RuidevUser admin) {
		this.admin = admin;
	}

}
