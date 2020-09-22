package com.ruidev.framework.util;

import java.io.File;
import java.io.FileInputStream;
import java.util.Arrays;
import java.util.List;
import java.util.Properties;

import javax.servlet.http.HttpSession;

import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;
import org.apache.struts2.ServletActionContext;

import com.ruidev.framework.api.ActionPermissionApi;
import com.ruidev.framework.constant.AuthConstants;
import com.ruidev.framework.user.IUserSessionInfo;

/**
 * 权限工具类
 *
 * @author: 	锐开科技 
 * @Copyright: 	www.ruidev.com All rights reserved.
 */
public class ActionPermissionUtil {
	
	private static ThreadLocal<String> currentRequestPath = new ThreadLocal<String>();
	
	/**
	 * 无需登录即拥有权限
	 */
	public static final String PERMISSION_ANON = "anon";
	/**
	 * 临时拥有权限
	 */
	public static final String PERMISSION_TEMP = "temp";
	/**
	 * 登录即拥有权限
	 */
	public static final String PERMISSION_LOGIN = "login";
	/**
	 * 仅超级管理员拥有权限
	 */
	public static final String PERMISSION_ADMIN = "admin";
	/**
	 * 客户管理员拥有权限
	 */
	public static final String PERMISSION_TENANT = "tenant";
	/**
	 * 客户用户拥有权限
	 */
	public static final String PERMISSION_USER = "user";
	
	private static Properties permissionDefinitions;

	private final static Logger log = Logger.getLogger(ActionPermissionUtil.class);

	/**
	 * 从配置文件中初始化权限定义
	 */
	public static void initPermissionDefinitions(){
		permissionDefinitions = new Properties();
		try {
			String path = ActionPermissionUtil.class.getClassLoader().getResource("").toURI().getPath(); 
			String file = path + "permission.properties";
			FileInputStream fis = new FileInputStream(new File(file));
			permissionDefinitions.load(fis);
			fis.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * 从permission.properties文件中读取路径访问权限
	 * @param path
	 * @return
	 */
	public static String getPermissionByPath(String path){
		if(permissionDefinitions == null){
			return null;
		}
		return permissionDefinitions.getProperty(path);
	}
	
	/**
	 * 查看当前用户对当前请求是否有权限
	 * @return
	 */
	public static Boolean hasPermissionForCurrentRequest(){
		String path = ServletActionContext.getRequest().getRequestURI();
		path = path.substring(ServletActionContext.getRequest().getContextPath().length());
		String nameSpace = path.substring(0, path.lastIndexOf("/") + 1);
		ActionPermissionUtil.setCurrentRequestNameSpace(nameSpace);
		String action = path.substring(path.lastIndexOf("/")+1);
		int dotIndex = action.indexOf(".");
		if(dotIndex > 0){
			action = action.substring(0, dotIndex);
		}
		return hasPermissionForRequest(nameSpace, action);
	}
	
	public static List<String> getPermissionRolesForCurrentRequest() {
		String permissionStr = getPermissionStrForCurrentRequest();
		return getPermissionRolesForPermissionStr(permissionStr);
	}
	
	public static List<String> getPermissionRolesForPermissionStr(String permissionStr) {
		if(!StringUtils.isEmpty(permissionStr)) {
			String[] perms = permissionStr.split(",");
			final String ROLES_PREFIX = "roles:";
			for(String perm : perms) {
				if(perm.startsWith(ROLES_PREFIX)) {
					return Arrays.asList(perm.substring(ROLES_PREFIX.length()).split(":"));
				}
			}
		}
		return null;
	}
	
	public static boolean hasRolePermissionForCurrentRequest(String roleCode) {
		if(StringUtils.isEmpty(roleCode)) {
			return false;
		}
		List<String> roleCodes = getPermissionRolesForCurrentRequest();
		return roleCodes != null && roleCodes.contains(roleCode);
	}
	
	public static boolean hasRolePermissionForPermissionStr(String roleCode, String permissionStr) {
		if(StringUtils.isEmpty(roleCode)) {
			return false;
		}
		List<String> roleCodes = getPermissionRolesForPermissionStr(permissionStr);
		return roleCodes != null && roleCodes.contains(roleCode);
	}
	
	/**
	 * 当前请求权限描述符
	 * @return
	 */
	public static String getPermissionStrForCurrentRequest() {
		String reqUrl = getActionUriForCurrentRequest();
		String permissionStrs = getPermissionByPath(reqUrl);
		return permissionStrs;
	}
	
	
	public static String getActionUriForCurrentRequest() {
		String path = ServletActionContext.getRequest().getRequestURI();
		path = path.substring(ServletActionContext.getRequest().getContextPath().length());
		String nameSpace = path.substring(0, path.lastIndexOf("/") + 1);
		ActionPermissionUtil.setCurrentRequestNameSpace(nameSpace);
		String action = path.substring(path.lastIndexOf("/")+1);
		int dotIndex = action.indexOf(".");
		if(dotIndex > 0){
			action = action.substring(0, dotIndex);
		}
		String reqUrl = CommonUtil.combineStrings(nameSpace, action);
		return reqUrl;
	}
	
	public static Boolean hasPermissionForRequest(String nameSpace, String action){
		HttpSession session = ServletActionContext.getRequest().getSession();
		IUserSessionInfo userInfo = (IUserSessionInfo) session.getAttribute(AuthConstants.USER_SESSION_INFO);
		return ActionPermissionUtil.hasPermissionForRequest(nameSpace, action, userInfo);
	}
	
	/**
	 * 根据nameSpace和action判断当前用户是否有权限
	 * @param nameSpace
	 * @param action
	 * @return
	 */
	public static Boolean hasPermissionForRequest(String nameSpace, String action, IUserSessionInfo userInfo){
		String reqUrl = CommonUtil.combineStrings(nameSpace, action).replaceAll("\\/+", "/");
		String permissionStrs = getPermissionByPath(reqUrl);
		String commonMatchUrl = CommonUtil.combineStrings(nameSpace, "*").replaceAll("\\/+", "/");
		String commonMatchUrlPermissionStrs = getPermissionByPath(commonMatchUrl);
		if(StringUtils.isEmpty(permissionStrs)) {
			reqUrl = commonMatchUrl;
			permissionStrs = commonMatchUrlPermissionStrs;
		}
		if(PERMISSION_ANON.equals(permissionStrs) || "/".equals(reqUrl)){
			return true;
		}
		if(PERMISSION_TEMP.equals(permissionStrs) || "/".equals(reqUrl) && LoginContext.isCurrentUserTemp()){
			return true;
		}
		if(userInfo != null){
			if(LoginContext.isCurrentUserAdmin() || PERMISSION_LOGIN.equals(permissionStrs)){
				return true;
			}
			if(!StringUtils.isEmpty(permissionStrs)){
				if(LoginContext.isCurrentUserManager()) {
					String roleCode = LoginContext.getCurrentUserRoleCode();
					if(hasRolePermissionForPermissionStr(roleCode, permissionStrs) || hasRolePermissionForPermissionStr(roleCode, commonMatchUrlPermissionStrs)) {
						return null;
					}
				}
				String[] permissionStrArr = permissionStrs.split(",");
				for(String permissionStr : permissionStrArr){
					if(LoginContext.isCurrentUserAdmin() || PERMISSION_LOGIN.equals(permissionStr)){
						return true;
					}
					if(!StringUtils.isEmpty(permissionStr) && permissionStr.equals(userInfo.getUser().getUsername())){
						return true;
					}
				}
			}
		}
		List<ActionPermissionApi> apis = ApiManager.getApis(ActionPermissionApi.class);
		if(apis != null) {
			Boolean result = null;
			for(ActionPermissionApi api : apis) {
				result = api.hasPermission(nameSpace, action, userInfo);
				if(result != null && result.booleanValue() == false) {
					return result;
				}
			}
			if(result != null) {
				return result;// result = true;
			}
		}
		log.debug("No permission for user's(id=" + LoginContext.getCurrentLoginUserId() + ") request " + reqUrl);
		return false;
	}
	
	public static String getCurrentRequestNameSpace() {
		return currentRequestPath.get();
	}

	public static void setCurrentRequestNameSpace(String currentRequestPath) {
		ActionPermissionUtil.currentRequestPath.set(currentRequestPath);
	}
	
}
