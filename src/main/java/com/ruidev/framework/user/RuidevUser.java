package com.ruidev.framework.user;

import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Inheritance;
import javax.persistence.InheritanceType;
import javax.persistence.Table;
import javax.persistence.Transient;
import javax.validation.constraints.NotNull;

import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;

import com.ruidev.admin.conf.util.ConfigurationUtil;
import com.ruidev.framework.constant.RoleLevel;
import com.ruidev.framework.constant.UserStatus;
import com.ruidev.framework.entity.CrudTenantEntity;

@Inheritance(strategy = InheritanceType.JOINED)
@Entity
@DynamicInsert
@DynamicUpdate
@Table(name = "ruidev_system_user")
public class RuidevUser extends CrudTenantEntity implements IUser{

	private static final long serialVersionUID = -201202101300L;

	@NotNull
	@Column(name = "username", updatable = false)
	protected String username;
	protected String password;
	@Column(name = "real_name")
	protected String realName;
	protected String email;
	protected String phone;
	protected String description;
	@Transient
	protected List<RuidevRole> roles;
	@Transient
	protected String oldPass;
	@Column(name = "user_status", length = 1)
	@NotNull
	protected String status = UserStatus.NORMAL.getCode();
	@NotNull
	@Column(name = "user_level", length = 4)
	protected Integer userLevel = RoleLevel.ROLE_USER.getLevel();
	
	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getRealName() {
		return realName;
	}

	public void setRealName(String realName) {
		this.realName = realName;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getStatus() {
		return status;
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

	public void setStatus(String status) {
		this.status = status;
	}
	
	public void setStatus(UserStatus status) {
		this.status = status.getCode();
	}

	public Integer getUserLevel() {
		return userLevel;
	}

	public void setUserLevel(Integer userLevel) {
		this.userLevel = userLevel;
	}
	
	public void setUserLevel(RoleLevel userLevel) {
		this.userLevel = userLevel.getLevel();
	}
	
	/**
	 * 用户角色级别名称
	 * @return
	 */
	public String getRoleLevelName() {
		userLevel = getUserLevel();
		if(userLevel == null) {
			return RoleLevel.ROLE_ADMIN.name().toLowerCase();
		}
		RoleLevel lvl = ConfigurationUtil.getInstance().getEnum(RoleLevel.class, userLevel.toString());
		if(lvl != null) {
			return lvl.name().toLowerCase();
		}
		return null;
	}

	public String getOldPass() {
		return oldPass;
	}

	public void setOldPass(String oldPass) {
		this.oldPass = oldPass;
	}

	public String getPhone() {
		return phone;
	}

	public void setPhone(String phone) {
		this.phone = phone;
	}

}
