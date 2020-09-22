package com.ruidev.admin.role.bo;

import java.util.List;

import org.springframework.stereotype.Service;

import com.ruidev.framework.bo.EntityBo;
import com.ruidev.framework.user.RuidevRole;

@Service
public class RuidevRoleBo extends EntityBo<RuidevRole> {

	public List<RuidevRole> getUserRoles(){
		return getAllData("from RuidevRole");
	}

	@Override
	public void delete(Long id) throws Exception {
		executeUpdateHql("update RuidevUser set roleId = ? where roleId = ?", null, id);
		super.delete(id);
	}
	
}
