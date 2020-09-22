package com.ruidev.framework.util;

import com.ruidev.framework.constant.RoleLevel;
import com.ruidev.framework.user.IUser;
import com.ruidev.framework.user.IUserSessionInfo;
import com.ruidev.framework.user.RuidevUser;

public class LoginContext
{
	public static final Long NO_USER_LOGIN_ID = null;
	
    /**
     * 用于各层之间传递当前登录的用户信息
     */
    private static final ThreadLocal<IUserSessionInfo> loginUserThreadLocal = new ThreadLocal<IUserSessionInfo>();
    
    private static final ThreadLocal<Boolean> loginTempUserThreadLocal = new ThreadLocal<Boolean>();
    
    private static final ThreadLocal<Long> loginUserTenantIdThreadLocal = new ThreadLocal<Long>();
    
    /**
     * 用于各层之间传递当前登录的用户信息
     */
    private static final ThreadLocal<Boolean> isPublicRequest = new ThreadLocal<Boolean>();

    /**
     * 获取当前登录的用户ID
     * 
     * @return 用户ID
     */
    public static Long getCurrentLoginUserId() {
        IUserSessionInfo loginUser = getCurrentLoginUser();
        if (loginUser != null) {
            return loginUser.getUser().getId();
        }
        return NO_USER_LOGIN_ID;
    }
    
    /**
     * 查看当前用户是否已登录
     * @return
     */
    public static boolean hasLogined(){
    	IUserSessionInfo loginUser = getCurrentLoginUser();
        if (loginUser != null) {
            return null != loginUser.getUser().getId();
        }
        return false;
    }
    
    public static void setCurrentLoginUserTenantId(Long tenantId) {
    	loginUserTenantIdThreadLocal.set(tenantId);
    }

    /**
     * 获取当前登录的用户Tenant ID
     * 
     * @return 用户ID
     */
    public static Long getCurrentLoginUserTenantId() {
    	Long tid = loginUserTenantIdThreadLocal.get();
    	if(tid != null){
    		return tid;
    	}
        IUserSessionInfo loginUser = getCurrentLoginUser();
        if (loginUser != null) {
        	IUser user = loginUser.getUser();
        	if(user instanceof RuidevUser){
        		return ((RuidevUser)user).getTenantId();
        	}
            return ((IUser)user).getTenantId();
        }
        return NO_USER_LOGIN_ID;
    }
    
    /**
     * 判断当前登录用户是否为超级管理员
     * @return
     */
    public static boolean isCurrentUserAdmin(){
    	IUserSessionInfo loginUser = getCurrentLoginUser();
        if (loginUser != null) {
        	IUser user = loginUser.getUser();
        	Integer lvl =user.getUserLevel();
        	return lvl == null || lvl.equals(RoleLevel.ROLE_ADMIN.getLevel());
        }
        return false;
    }
    
    /**
     * 判断当前登录用户是否为临时用户
     * @return
     */
    public static boolean isCurrentUserTemp(){
        return loginTempUserThreadLocal.get() != null && loginTempUserThreadLocal.get().equals(true);
    }
    
    /**
     * 判断当前登录用户是否为普通管理员
     * @return
     */
    public static boolean isCurrentUserManager(){
    	IUserSessionInfo loginUser = getCurrentLoginUser();
        if (loginUser != null) {
        	IUser user = loginUser.getUser();
        	if(user instanceof RuidevUser){
        		Integer lvl = ((RuidevUser)user).getUserLevel();
        		return lvl != null && lvl.equals(RoleLevel.ROLE_MANAGER.getLevel());
        	}
        }
        return false;
    }
    
    public static String getCurrentUserRoleCode() {
    	IUserSessionInfo loginUser = getCurrentLoginUser();
        if (loginUser != null) {
        	IUser user = loginUser.getUser();
        	if(user instanceof RuidevUser){
        		RuidevUser ru = (RuidevUser)user;
        		return ru.isManager() ? ru.getRoleCode() : null;
        	}
        }
        return null;
    }
    
    /**
     * 判断当前登录用户是否为客户管理员(租户)
     * @return
     */
    public static boolean isCurrentUserTenant(){
    	IUserSessionInfo loginUser = getCurrentLoginUser();
        if (loginUser != null) {
        	IUser user = loginUser.getUser();
        	if(user instanceof RuidevUser){
        		Integer lvl = ((RuidevUser)user).getUserLevel();
        		return lvl != null && lvl.equals(RoleLevel.ROLE_TENANT.getLevel());
        	}
        }
        return false;
    }
    
    /**
     * 判断当前登录用户是否为客户的用户
     * @return
     */
    public static boolean isCurrentUserTenantUser(){
    	IUserSessionInfo loginUser = getCurrentLoginUser();
        if (loginUser != null) {
        	IUser user = loginUser.getUser();
        	if(user instanceof RuidevUser){
        		Integer lvl = ((RuidevUser)user).getUserLevel();
        		return lvl != null && lvl.equals(RoleLevel.ROLE_TENANT_USER.getLevel());
        	}
        }
        return false;
    }

    /**
     * 获取当前登录的后台用户信息
     * 
     * @return 当前登录用户信息
     */
    public static IUserSessionInfo getCurrentLoginUser() {
        IUserSessionInfo loginUser = (IUserSessionInfo) loginUserThreadLocal.get();
        return loginUser;
    }
    
    /**
     * 清除当前登录的用户信息
     */
    public static void clearCurrentSessionInfo(){
    	loginUserThreadLocal.remove();
    }

   
    /**
     * 设置当前登录的用户
     * 
     * @param loginUser
     *            当前登录用户信息
     */
    public static void setCurrentLoginUser(IUserSessionInfo loginUser) {
        loginUserThreadLocal.set(loginUser);
    }
    
    /**
     * 设置当前登录的用户
     * 
     * @param loginUser
     *            当前登录用户信息
     */
    public static void setCurrentLoginUserTemp(Boolean temp) {
    	if(temp == null || temp.equals(false)){
    		loginTempUserThreadLocal.remove();
    		return;
    	}
    	loginTempUserThreadLocal.set(temp);
    }
    
    /**
     * 当前请求是否是接口类公共请求
     * @param flag
     */
    public static void setCurrentPublic(Boolean flag){
    	isPublicRequest.set(flag);
    }
    
    public static Boolean isCurrentRequestPublic(){
    	boolean flag = false;
    	if(isPublicRequest.get() != null){
    		flag = isPublicRequest.get();
    	}
    	return flag;
    }

}
