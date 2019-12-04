package com.ruidev.framework.api;

import com.ruidev.framework.user.IUserSessionInfo;

/**
 * action权限判断<br>
 * 扩展action权限时实现该接口,并添上@Service注解<br>
 * return true：		通过<br>
 * return false：	则不通过<br>
 * return null：		交给下一个实现bean处理<br>
 * 如需要控制判断顺序,可加上@ActionPermission注解
 * @see com.ruidev.framework.annotations.ApiSort
 *
 * @author	 	锐开科技 
 * @Copyright 	www.ruidev.com All rights reserved.
 */
public abstract class ActionPermissionApi extends BaseApi {

	/**
	 * 根据nameSpace和action判断用户是否有权限
	 * @param nameSpace
	 * @param action
	 * @param userInfo
	 * @return 
	 * true:	通过<br>
	 * false:	不通过<br>
	 * null:	交给下一个bean判断
	 */
	public abstract Boolean hasPermission(String nameSpace, String action, IUserSessionInfo userInfo);
}
