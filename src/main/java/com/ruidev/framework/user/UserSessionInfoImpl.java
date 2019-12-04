package com.ruidev.framework.user;

import java.io.Serializable;

import com.ruidev.framework.util.ActionPermissionUtil;

/**
 * 实现 UserSessionInfo接口的类.
 *
 * @author Lord
 *
 */
public class UserSessionInfoImpl implements IUserSessionInfo, Serializable
{
	/**
	 * 
	 */
	private static final long serialVersionUID = 9999L;

	/** 用户信息 * */
	private IUser user;

	/** 其它信息：如果是员工登陆，就是员工信息；如果是客户登陆，就是客户信息 * */

	private String sessionId;
	
	private String remoteIp;
	
	private String principal;
	
	/**
	 * @return the user
	 */
	public IUser getUser()
	{
		return user;
	}

	/**
	 * @param user
	 *            the user to set
	 */
	public void setUser(IUser user)
	{
		this.user = user;
	}

	public String getSessionId()
	{
		return sessionId;
	}
	public void setSessionId(String sessionId)
	{
		this.sessionId = sessionId;
	}

	public String getRemoteIp() {
		return remoteIp;
	}

	public void setRemoteIp(String remoteIp) {
		this.remoteIp = remoteIp;
	}

	public String getPrincipal() {
		return principal;
	}

	public void setPrincipal(String principal) {
		this.principal = principal;
	}

	@Override
	public boolean hasPermission(String uri) {
		if(user != null && uri.contains("/")){
			String nameSpace = uri.substring(0, uri.lastIndexOf("/") + 1);
			String action = uri.substring(uri.lastIndexOf("/"));
			return ActionPermissionUtil.hasPermissionForRequest(nameSpace, action);
		}
		return false;
	}

}
