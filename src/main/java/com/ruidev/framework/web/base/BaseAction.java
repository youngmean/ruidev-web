package com.ruidev.framework.web.base;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.struts2.interceptor.ServletRequestAware;
import org.apache.struts2.interceptor.ServletResponseAware;

import com.opensymphony.xwork2.ActionSupport;

public abstract class BaseAction extends ActionSupport implements ServletRequestAware, ServletResponseAware, CrudAction {

	/** serialVersionUID */
	protected static final long serialVersionUID = -5119044275026960455L;

	/** log4j instance. */
	protected final Logger log = LogManager.getLogger(this.getClass());

	/** request */
	protected HttpServletRequest request;

	/** response */
	protected HttpServletResponse response;

	/** 调用公共查询方法的参数 */
	protected String parameter;

	/** Action名字 */
	protected String actionPath;
	
	protected String actionName;

	private String token;

	public String getToken() {
		return token;
	}

	public void setToken(String token) {
		this.token = token;
	}

	public void setActionPath(String p) {
		this.actionPath = p;
	}

	/**
	 * 返回Action映射路径(包含上下文)
	 * 
	 * @return
	 * @throws Exception
	 */
	public String getActionPath() {
		return actionPath;
	}
	
	public void setActionName(String name) {
		this.actionName = name;
	}

	/**
	 * @return the parameter
	 */
	public String getParameter() {
		return parameter;
	}

	/**
	 * @param parameter
	 *            the parameter to set
	 */
	public void setParameter(String parameter) {
		this.parameter = parameter;
	}

	public void setServletResponse(HttpServletResponse arg0) {
		this.response = arg0;
	}

	public void setServletRequest(HttpServletRequest arg0) {
		this.request = arg0;
	}

}
