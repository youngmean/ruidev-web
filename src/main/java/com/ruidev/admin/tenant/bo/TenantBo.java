package com.ruidev.admin.tenant.bo;

import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

import com.ruidev.framework.bo.EntityBo;
import com.ruidev.framework.exception.BizException;
import com.ruidev.framework.user.RuidevTenant;
import com.ruidev.framework.user.RuidevUser;

@Service
public class TenantBo extends EntityBo<RuidevTenant>{
	
	/**
	 * 创建客户
	 * @param object
	 * @return
	 * @throws Exception 
	 */
	public RuidevTenant createOrUpdate(RuidevTenant object, boolean createAdmin, RuidevUser user) throws Exception{
		save(object);
		if(createAdmin){
			if(StringUtils.isEmpty(user.getPassword())){
				throw new BizException("管理员密码不能为空");
			}
			RuidevUser admin = getFirstData("from RuidevUser where tenantId = ? and username = ?", object.getId(), user.getUsername());//getFirstResult("from RuidevUser where tenantId = ? and groupId = ?", object.getId(), tenantGroup.getId());
			if(admin != null){//该客户已有管理员
				throw new BizException("该账号已存在");
				/*if(tenantGroup.equals(admin.getGroupId())){//相同的用户组
					admin.setUsername(user.getUsername());
					if(!StringUtils.isEmpty(user.getPassword())){
						admin.setPassword(user.getPassword());
					}
					admin.setGroupId(tenantGroup.getId());//使该管理员用户设置拥有客户组权限
					saveOrUpdateData(admin);
				}*/
			}else{
				user.setTenantId(object.getId());
				saveData(user);
			}
		}
		return object;
	}

	@Override
	public RuidevTenant save(RuidevTenant object) throws Exception {
		/*
		List<Long> roleIds = (List<Long>) object.getRoleIds();
    	RuidevRole role = null;
    	if(roleIds != null){
    		List<RuidevRole> roles = new ArrayList<RuidevRole>();
			for(Long roleId : roleIds){
				role = new RuidevRole();
				role.setId(roleId);
				roles.add(role);
			}
			object.setRoles(roles);
    	}
    	*/
		if(object.getId() == null){
			String maxNo = getFirstData("select tenantId from RuidevTenant where tenantId is not null order by id desc");
			if(StringUtils.isEmpty(maxNo)){
				maxNo = "10000";
			}
			maxNo = (Long.valueOf(maxNo) + 1l) + "";
		}
		return super.save(object);
	}


}
