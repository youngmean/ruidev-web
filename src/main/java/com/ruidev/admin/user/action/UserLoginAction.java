package com.ruidev.admin.user.action;

import java.io.IOException;
import java.util.List;
import java.util.Locale;

import javax.servlet.http.HttpSession;

import org.apache.commons.lang3.StringUtils;
import org.apache.struts2.convention.annotation.Action;
import org.apache.struts2.convention.annotation.Namespace;
import org.apache.struts2.convention.annotation.ParentPackage;

import com.ruidev.admin.user.bo.UserBo;
import com.ruidev.framework.annotations.ActionEnableAnon;
import com.ruidev.framework.api.UserLoginCallbackApi;
import com.ruidev.framework.constant.AuthConstants;
import com.ruidev.framework.constant.ErrorType;
import com.ruidev.framework.exception.BizException;
import com.ruidev.framework.user.UserSessionInfoImpl;
import com.ruidev.framework.util.ApiManager;
import com.ruidev.framework.util.LoginContext;
import com.ruidev.framework.util.RequestContext;
import com.ruidev.framework.util.WebSessionSubjectUtil;
import com.ruidev.framework.web.base.AbsCrudAction;

@ParentPackage("ruidev-default")
@Namespace("/user")
public class UserLoginAction extends AbsCrudAction<UserBo> {

	private static final long serialVersionUID = 201202100110L;
	List<UserLoginCallbackApi> loginApis = null;
	
	public UserLoginAction() {
		loginApis = ApiManager.getApis(UserLoginCallbackApi.class);
	}
	
	protected String username;
	protected String password;

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		if(!StringUtils.isEmpty(username)) {
			username = username.replaceAll("[\"\'<]", "");
		}
		this.username = username;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	/**
	 * User login action
	 */
	@Action("login")
	@ActionEnableAnon(dataType = "html")
	public String login() throws Exception {
		String homePage = "/user/index";
		UserSessionInfoImpl currentUser = null;
		loginApis = ApiManager.getApis(UserLoginCallbackApi.class);
		if (StringUtils.isEmpty(password) || StringUtils.isEmpty(username)) {
			throw new BizException("please input username and password");
		}
		if (!StringUtils.isEmpty(password) && !StringUtils.isEmpty(username)) {
			_logout();
			/*if(this.getChallange() == null || !this.getChallange().toLowerCase().equals(request.getSession().getAttribute(UserConstant.USER_SESSION_PATCHCA))){
				Map<String, String> errors = new HashMap<String, String>();
				errors.put("message", "验证码不正确");
				errors.put("type", "0");
				setErrorMsg(errors);
				this._logout();
				log.error("input:"+this.getChallange()+", right:"+request.getSession().getAttribute(UserConstant.USER_SESSION_PATCHCA));
				return SUCCESS;
			}*/
			currentUser = bo.getUserByUserNameAndPassword(username, password);
			Locale locale = (Locale) request.getSession().getAttribute(AuthConstants.USER_SESSION_LOCAL);
			request.getSession().setAttribute(AuthConstants.USER_SESSION_LOCAL, locale);
			if (currentUser == null) {
				for(UserLoginCallbackApi api : loginApis) {
					api.onLoginFailed();
				}
				throw new BizException("Login failed, wrong username or password");
			}
			WebSessionSubjectUtil.setAttr(AuthConstants.USER_SESSION_INFO, currentUser);
			WebSessionSubjectUtil.setAttr(AuthConstants.LOGIN_USER, currentUser.getUser());
			LoginContext.setCurrentLoginUser(currentUser);
			for(UserLoginCallbackApi api : loginApis) {
				api.onLoginSuccess();
			}
		} else {
			currentUser = (UserSessionInfoImpl) WebSessionSubjectUtil.getAttr(AuthConstants.USER_SESSION_INFO);
			if (currentUser != null) {
				if(!StringUtils.isEmpty(dataType)){
					homePage += "?dataType=" + dataType;
					if(!StringUtils.isEmpty(jsonpCallback)){
						homePage += "&jsonpCallback=" + jsonpCallback;
					}
					setReturnObjectAttribute("logintoken", request.getSession(false).getId());
				}
				response.sendRedirect(request.getContextPath() + homePage);
				return null;
			}
			if(!StringUtils.isEmpty(username) || !StringUtils.isEmpty(password)){
				setError(ErrorType.USER_LOGIN_ERROR, ErrorType.USER_LOGIN_ERROR_MSG);
			}
			return SUCCESS;
		}
		currentUser = (UserSessionInfoImpl) WebSessionSubjectUtil.getAttr(AuthConstants.USER_SESSION_INFO);
		if(!StringUtils.isEmpty(dataType)){
			homePage += "?dataType=" + dataType;
			if(!StringUtils.isEmpty(jsonpCallback)){
				homePage += "&jsonpCallback=" + jsonpCallback;
			}
			setReturnObjectAttribute("logintoken", request.getSession(false).getId());
		}
		String uri = getSessionAttribute(AuthConstants.USER_SESSION_URI);
		if(!StringUtils.isEmpty(uri)){
			response.sendRedirect(uri);
			removeSessionAttribute(AuthConstants.USER_SESSION_URI);
		}else{
			response.sendRedirect(request.getContextPath() + homePage);
		}
		return null;
	}

	@Action("logout")
	@ActionEnableAnon(dataType = "html")
	public String logout() throws Exception {
		_logout();
		response.sendRedirect(request.getContextPath() + "/");  //退出后，回到项目首页
		return null;
	}
	
	protected void _logout(){
		// 第一次访问登录页面会分配一个session，在登录的时候invalidate这个session，分配新的session。
		// 避免固定sessionid的安全漏洞
		HttpSession session = request.getSession(false);
		Locale locale = (Locale) session.getAttribute(AuthConstants.USER_SESSION_LOCAL);
		if (session!=null && !session.isNew()) {
		    session.invalidate();
		}
		session = request.getSession();
		session.setAttribute(AuthConstants.USER_SESSION_LOCAL, locale);
		LoginContext.clearCurrentSessionInfo();
		RequestContext.clearUserInfo();
		for(UserLoginCallbackApi api : loginApis) {
			api.onLogout();
		}
	}

	@Action("index")
	@ActionEnableAnon(dataType = "html")
	public String index() {
		if (WebSessionSubjectUtil.getAttr(AuthConstants.USER_SESSION_INFO) != null) {
			String homePage = null;
			UserSessionInfoImpl currentUser = (UserSessionInfoImpl) WebSessionSubjectUtil.getAttr(AuthConstants.USER_SESSION_INFO);
			returnObject = currentUser.getUser();
			addIncludeProperty("realName");
			if (!StringUtils.isEmpty(homePage)) {
				try {
					response.sendRedirect(request.getContextPath() + homePage);
					return null;
				} catch (IOException e) {
				}
			}
			/*if(!LoginContext.isCurrentUserAdmin() && SUCCESS.equals("success")){
				SUCCESS = "user";
			}*/
			return SUCCESS;
		}
		try {
			response.sendRedirect(request.getContextPath() + "/user/login");
		} catch (IOException e) {
		}
		return null;
	}

}
