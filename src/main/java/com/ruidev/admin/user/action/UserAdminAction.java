package com.ruidev.admin.user.action;

import org.apache.commons.lang3.StringUtils;
import org.apache.struts2.convention.annotation.Action;
import org.apache.struts2.convention.annotation.Namespace;

import com.ruidev.admin.user.bo.UserBo;
import com.ruidev.framework.user.RuidevUser;
import com.ruidev.framework.util.LoginContext;
import com.ruidev.framework.web.base.EntityAction;

@Namespace("/admin/user")
public class UserAdminAction extends EntityAction<RuidevUser, UserBo> {
	
	private static final long serialVersionUID = 1L;
	
	@Action("changepass")
	public String changePass() throws Exception {
		if(object != null && object.getId() != null){
			returnObject = bo.changePassword(object);
			if(StringUtils.isEmpty(dataType)) {
				target = "/admin/user/changepass";
				return REDIRECT;
			}
			return JSON;
		}
		object = bo.get(LoginContext.getCurrentLoginUserId());
		return SUCCESS;
	}
	
}
