package com.ruidev.framework.web.interceptor;

import java.lang.reflect.Method;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang3.StringUtils;
import org.apache.struts2.ServletActionContext;

import com.opensymphony.xwork2.ActionInvocation;
import com.opensymphony.xwork2.ActionSupport;
import com.opensymphony.xwork2.interceptor.AbstractInterceptor;
import com.ruidev.admin.user.action.UserAdminAction;
import com.ruidev.framework.annotations.ActionEnableAnon;
import com.ruidev.framework.constant.AuthConstants;
import com.ruidev.framework.constant.ErrorType;
import com.ruidev.framework.constant.PageConstant;
import com.ruidev.framework.constant.RoleLevel;
import com.ruidev.framework.user.IUserSessionInfo;
import com.ruidev.framework.util.CommonUtil;
import com.ruidev.framework.util.LoginContext;
import com.ruidev.framework.util.ActionPermissionUtil;
import com.ruidev.framework.util.WebSessionSubjectUtil;
import com.ruidev.framework.web.base.AbsCrudAction;

/**
 * 基本权限拦截器
 *
 * @author: 	锐开科技 
 * @Copyright: 	www.ruidev.com All rights reserved.
 */
public class PermissionInterceptor extends AbstractInterceptor {

	private static final long serialVersionUID = 1L;

	@SuppressWarnings("rawtypes")
	public String intercept(ActionInvocation actionInvocation) throws Exception {
		Object action = actionInvocation.getAction();
		HttpServletRequest request = ServletActionContext.getRequest();
        IUserSessionInfo loginUser = (IUserSessionInfo) request.getSession().getAttribute(AuthConstants.USER_SESSION_INFO);
        LoginContext.setCurrentLoginUser(loginUser);
		String methodType = request.getMethod().toLowerCase();
        String origMethodName = actionInvocation.getProxy().getMethod();
        Class<?> invocationClass = actionInvocation.getAction().getClass();
		Method method = invocationClass.getMethod(origMethodName);
		//ServletActionContext.getResponse().addHeader("X-Frame-Options", "SAMEORIGIN");
		if(method.isAnnotationPresent(ActionEnableAnon.class)){
			String[] methodTypes = method.getAnnotation(ActionEnableAnon.class).methods();
			if(methodTypes == null || methodTypes.length < 1) {
				return actionInvocation.invoke();
			}else {
				for(String mtd : methodTypes) {
					if(methodType.equals(mtd.toLowerCase())) {
						return actionInvocation.invoke();
					}
				}
				String dataType = invocationClass.getAnnotation(ActionEnableAnon.class).dataType();
				if(PageConstant.isValidDataType(dataType) && action instanceof AbsCrudAction){
					AbsCrudAction<?> _action = (AbsCrudAction)action;
					_action.setError(ErrorType.USER_LOGIN_REQUIRED, ErrorType.USER_LOGIN_REQUIRED_MSG);
					_action.addErrorMsg("tip", "尚未登录");
					return dataType;
				}
				return "403";
			}
		} else if (action instanceof AbsCrudAction) {
        	if(loginUser != null && loginUser.getUser() != null && RoleLevel.ROLE_TEMP_USER.getLevel().equals(loginUser.getUser().getUserLevel())){
        		LoginContext.setCurrentLoginUserTemp(true);
        	}else{
        		LoginContext.setCurrentLoginUserTemp(false);
        	}
			Boolean hasP = ActionPermissionUtil.hasPermissionForCurrentRequest();
			if(hasP){
				return actionInvocation.invoke();
			}
			AbsCrudAction<?> _action = (AbsCrudAction)action;
			String dataType = _action.getDataType();
			if(loginUser == null){
				String uri = request.getRequestURI();
				if("get".equals(methodType)){
					String methodName = actionInvocation.getInvocationContext().getName().toLowerCase();
					if("list".equals(methodName) || "add".equals(methodName) || "edit".equals(methodName) || "view".equals(methodName)){
						WebSessionSubjectUtil.setAttr(AuthConstants.USER_SESSION_URI, uri);
					}
				}
				if(PageConstant.isValidDataType(dataType)){
					_action.setError(ErrorType.USER_LOGIN_REQUIRED, ErrorType.USER_LOGIN_REQUIRED_MSG);
					_action.addErrorMsg("tip", "尚未登录");
					return dataType;
				}
				ServletActionContext.getResponse().sendRedirect(request.getContextPath() + "/user/login");
				return null;
			}else{
				if(PageConstant.isValidDataType(dataType)){
					_action.setError(ErrorType.UN_AUTHORIZED, ErrorType.UN_AUTHORIZED_MSG);
					_action.addErrorMsg("tip", "未授权");
					return dataType;
				}
			}
        } else if(action.getClass() == ActionSupport.class){
        	String dataType = request.getParameter("dataType");
        	String showDetailInfoStr = request.getParameter("showDetailInfo");
        	int showDetailInfo = 0;
        	if(!StringUtils.isEmpty(showDetailInfoStr)){
        		showDetailInfo = Integer.valueOf(showDetailInfoStr);
        	}
        	AbsCrudAction<?> _action = new UserAdminAction();
        	_action.setDataType(dataType);
        	_action.setShowDetailInfo(showDetailInfo);
        	_action.setServletResponse(ServletActionContext.getResponse());
        	if(PageConstant.isValidDataType(dataType)){
        		_action.setError(ErrorType.ACTION_URL_INVALID, CommonUtil.combineStrings(ErrorType.ACTION_URL_INVALID_MSG, ": ", request.getServletPath()));
				_action.addErrorMsg("tip", "非法请求");
				if(PageConstant.REQ_DATA_TYPE_JSON.equals(dataType)){
					request.setAttribute("jsonData", _action.getJsonData());
				}else if(PageConstant.REQ_DATA_TYPE_JSONP.equals(dataType)){
					request.setAttribute("jsonpData", _action.getJsonpData());
				}else if(PageConstant.REQ_DATA_TYPE_XML.equals(dataType)){
					request.setAttribute("xmlData", _action.getXmlData());
				}
        		return dataType;
        	}
        	return "404";
        } else {
        	return actionInvocation.invoke();
        }
		return "403";
	}

}
