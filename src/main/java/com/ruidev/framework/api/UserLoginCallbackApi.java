package com.ruidev.framework.api;

/**
 * 用户登录/退出回调
 *
 * @author	 	锐开科技 
 * @Copyright 	www.ruidev.com All rights reserved. 
 */
public abstract class UserLoginCallbackApi extends BaseApi {

	/**
	 * 登录成功
	 */
	public abstract void onLoginSuccess();
	
	/**
	 * 登录失败
	 */
	public abstract void onLoginFailed();
	
	/**
	 * 退出
	 */
	public abstract void onLogout();
	
}
