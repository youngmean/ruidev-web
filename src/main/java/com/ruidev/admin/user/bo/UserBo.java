package com.ruidev.admin.user.bo;

import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

import com.ruidev.framework.bo.EntityBo;
import com.ruidev.framework.constant.ErrorType;
import com.ruidev.framework.constant.UserStatus;
import com.ruidev.framework.exception.BizException;
import com.ruidev.framework.user.IUser;
import com.ruidev.framework.user.RuidevTenant;
import com.ruidev.framework.user.RuidevUser;
import com.ruidev.framework.user.UserSessionInfoImpl;
import com.ruidev.framework.util.DesUtil;
import com.ruidev.framework.util.LoginContext;

@Service
public class UserBo extends EntityBo<RuidevUser>{
	
	/**
	 * 更新用户密码
	 * @param usr
	 * @return
	 * @throws Exception
	 */
	public RuidevUser changePassword(RuidevUser usr) throws Exception{
		if(StringUtils.isEmpty(usr.getPassword())){
			throw new BizException("Password could not be empty");
		}
		if(StringUtils.isEmpty(usr.getRealName())){
			throw new BizException("Old password could not be empty");
		}
		String newPwd = usr.getPassword();
		if(newPwd.length() < 6){
			throw new BizException("Length of password should not be less than 6");
		}
		/*if(!newPwd.matches(".*[0-9]+.*")){
			throw new BizException("密码中必须包含数字");
		}
		if(!newPwd.matches(".*[a-z]+.*")){
			throw new BizException("密码中必须包含小写字符");
		}
		if(!newPwd.matches(".*[A-Z]+.*")){
			throw new BizException("密码中必须包含大写字符");
		}*/
		if(!LoginContext.isCurrentUserAdmin() && !LoginContext.getCurrentLoginUserId().equals(usr.getId())) {
			throw new BizException("Illegal user id");
		}
		RuidevUser user = get(usr.getId());
		if(user.getPassword().length() < DesUtil.DES_LENGTH){
			if(!user.getPassword().equals(usr.getRealName())){
				throw new BizException("Wrong password");
			}
		}else{
			if(!user.getPassword().equals(DesUtil.encrypt(usr.getRealName()))){
				throw new BizException("Wrong password");
			}
		}
		user.setPassword(DesUtil.encrypt(usr.getPassword()));
		saveData(user);
		return usr;
	}
	
	/**
	 * 更新当前登录用户密码
	 * @param usr
	 * @return
	 * @throws Exception
	 */
	public RuidevUser changeMyPassword(String newPwd, String oldPwd) throws Exception{
		if(StringUtils.isEmpty(newPwd)){
			throw new BizException("新密码不能为空");
		}
		if(StringUtils.isEmpty(oldPwd)){
			throw new BizException("原密码不能为空");
		}
		if(newPwd.equals(oldPwd)){
			throw new BizException("新密码不能与原密码相同");
		}
		Long userId = LoginContext.getCurrentLoginUserId();
		if(userId == null){
			throw new BizException("尚未登录");
		}
		RuidevUser user = get(userId);
		if(user.getPassword().length() < DesUtil.DES_LENGTH){
			if(!user.getPassword().equals(oldPwd)){
				throw new BizException("原密码错误");
			}
		}else{
			if(!user.getPassword().equals(DesUtil.encrypt(oldPwd))){
				throw new BizException("原密码错误");
			}
		}
		user.setPassword(newPwd);
		saveData(user);
		return user;
	}
	
	@Override
	public RuidevUser save(RuidevUser object) throws Exception {
		if(object.getId() != null){
			RuidevUser usr = get(object.getId());
			final String NO_CHANGE_PASS = "****************";
			if(StringUtils.isEmpty(object.getPassword()) || NO_CHANGE_PASS.equals(object.getPassword())){
				object.setPassword(usr.getPassword());
			}else{
				/*if(StringUtils.isEmpty(object.getOldPass())){
					throw new BizException("Old password could not be empty");
				}
				String _oldPass = DesUtil.encrypt(object.getOldPass());
				if(!_oldPass.equals(usr.getPassword()) && !object.getOldPass().equals(usr.getPassword())){
					throw new BizException("wrong old password");
				}*/
				object.setPassword(DesUtil.encrypt(object.getPassword()));
			}
			if(!usr.getUsername().equals(object.getUsername())){
				throw new BizException("Username could not be modified");
			}
			clearSession();
		}else{
			if(StringUtils.isEmpty(object.getPassword())){
				throw new BizException("Password could not be null");
			}
			object.setPassword(DesUtil.encrypt(object.getPassword()));
			RuidevUser usr = getFirstData("from RuidevUser where username = ?", object.getUsername());
			if(usr != null){
				throw new BizException("Username already exists");
			}
		}
		return super.save(object);
	}

    public UserSessionInfoImpl getUserByUserNameAndPassword(
            String username, String password) throws Exception {
        IUser user = null;
        LoginContext.setCurrentPublic(true);
        try {
            user = (RuidevUser)this.getFirst("username = ?", username);
        } catch (Exception e) {
        	e.printStackTrace();
            throw new BizException(e.getMessage(), ErrorType.USER_LOGIN_ERROR);
        }
        if(user == null || user.getPassword() == null){
        	if(username.contains("@")) {
        		user = (RuidevUser)this.getFirst("email = ?", username);
        	}
        	if(user == null && username.matches("\\d+")) {
        		user = (RuidevUser)this.getFirst("phone = ?", username);
        	}
        }
        if(user == null) {
            return null;
        }else if(user.getPassword().length() < DesUtil.DES_LENGTH){
        	if(!user.getPassword().equals(password)){
        		return null;
        	}
        }else if(user.getPassword().length() >= DesUtil.DES_LENGTH){
        	if(!user.getPassword().equals(DesUtil.encrypt(password))){
        		return null;
        	}
        }
        if(UserStatus.DISABLED.getCode().equals(user.getStatus())){
        	BizException e = new BizException("Account is forbidden", 403);
        	throw e;
        }else if(user.getTenantId() != null){
        	RuidevTenant tenant = getData(RuidevTenant.class, user.getTenantId());
        	if(UserStatus.DISABLED.getCode().equals(tenant.getStatus())){
            	BizException e = new BizException("Tenant account is forbidden", 403);
            	throw e;
            }
        }
        UserSessionInfoImpl info = new UserSessionInfoImpl();
        info.setUser(user);
        LoginContext.setCurrentPublic(false);
        return info;
    }

	@Override
	public void delete(Long id) throws Exception {
		RuidevUser usr = get(id);
		if(LoginContext.getCurrentLoginUserId().equals(id)){
			throw new BizException("不能删除当前登录账号");
		}
		if(id.equals(( (RuidevUser)LoginContext.getCurrentLoginUser().getUser() ).getCreateBy())){
			throw new BizException("不能删除当前登录账号的上级账号");
		}
		if(usr.getCreateBy() != null && usr.getCreateBy().equals(( (RuidevUser)LoginContext.getCurrentLoginUser().getUser() ).getCreateBy())){
			throw new BizException("不能删除当前登录账号的同级账号");
		}
		if("admin".equals(usr.getUsername())){
			throw new BizException("不能删除系统管理员账号");
		}
		super.delete(id);
	}
    
}
