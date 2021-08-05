package com.ruidev.framework.web.interceptor;

import java.lang.reflect.Method;
import java.util.Enumeration;
import java.util.Locale;
import java.util.Map;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.servlet.http.HttpServletRequest;
import javax.validation.ConstraintViolation;
import javax.validation.ConstraintViolationException;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.exception.ExceptionUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.struts2.ServletActionContext;
import org.hibernate.QueryException;
import org.hibernate.exception.GenericJDBCException;

import com.opensymphony.xwork2.ActionContext;
import com.opensymphony.xwork2.ActionInvocation;
import com.opensymphony.xwork2.config.ConfigurationException;
import com.opensymphony.xwork2.interceptor.AbstractInterceptor;
import com.opensymphony.xwork2.interceptor.PreResultListener;
import com.ruidev.framework.annotations.ActionEnableFilters;
import com.ruidev.framework.annotations.ActionEnablePageFilter;
import com.ruidev.framework.constant.AuthConstants;
import com.ruidev.framework.constant.ErrorType;
import com.ruidev.framework.constant.PageConstant;
import com.ruidev.framework.exception.BaseException;
import com.ruidev.framework.exception.BizException;
import com.ruidev.framework.exception.SysException;
import com.ruidev.framework.util.LoginContext;
import com.ruidev.framework.util.RequestContext;
import com.ruidev.framework.web.base.AbsCrudAction;
import com.ruidev.framework.web.base.CrudAction;

/**
 * 实现分页处理和异常修饰的Struts拦截器
 *
 * @author: 	锐开科技 
 * @Copyright: 	www.ruidev.com All rights reserved.
 */
public class CrudActionInterceptor extends AbstractInterceptor implements PreResultListener {
	private static final long serialVersionUID = -4650246584221940861L;

	/** 日志控制 */
	protected final Logger logger = LogManager.getLogger(this.getClass());

	/** 执行拦截器方法名称. */
	private String includeMethods;

	/** 是否分页. */
	private boolean isListPage = false;

	@SuppressWarnings({"rawtypes"})
	public String intercept(ActionInvocation invocation) throws Exception {
		Object action = invocation.getAction();
		if(!(action instanceof CrudAction)){
			return invocation.invoke();
		}
		String result = null;
		String methodName = invocation.getInvocationContext().getName();
		Map<?, ?> session = invocation.getInvocationContext().getSession();
		if (session.get(AuthConstants.USER_SESSION_LOCAL) != null) {
			ActionContext.getContext().setLocale((Locale) session.get(AuthConstants.USER_SESSION_LOCAL));
		}
		HttpServletRequest request = ServletActionContext.getRequest();
		AbsCrudAction absCrudAction = (AbsCrudAction) action;
		if (PageConstant.isValidDataType(absCrudAction.getDataType())) {
			absCrudAction.setResultCode(absCrudAction.getDataType());
		} else if (PageConstant.REQ_DATA_TYPE_HTML.equals(absCrudAction.getDataType())) {
			absCrudAction.setResultCode("success");
		}
		CrudAction crudAction = (CrudAction) action;
		crudAction.setErrorMsg(null);
		try {
			doBeforeAction(invocation);
			String origMethodName = invocation.getProxy().getMethod();
			Method method = invocation.getAction().getClass().getMethod(origMethodName);
			if(method.isAnnotationPresent(ActionEnableFilters.class)){
				isListPage = true;
			}else {
				isListPage = isListPage(includeMethods, methodName);
			}
			if (isListPage) {
				try {
					doBeforePage(request, crudAction);
				} catch (Exception e) {
					return exceptionWrap(e, crudAction);
				}
			}else if(method.isAnnotationPresent(ActionEnablePageFilter.class)) {
				if (crudAction.getIndex() != null) {
					RequestContext.setIndex(crudAction.getIndex());
				}
				if (crudAction.getSize() != null) {
					RequestContext.setSize(crudAction.getSize());
				}
			}
			invocation.addPreResultListener(this);
			result = invocation.invoke();
			clearCurrentContext();
		} catch (BizException e) {
			clearCurrentContext();
			return exceptionWrap(e, crudAction);
		} catch (SysException e) {
			clearCurrentContext();
			return exceptionWrap(e, crudAction);
		} catch (NullPointerException e) {
			clearCurrentContext();
			return exceptionWrap(e, crudAction, 900);
		} catch(ConstraintViolationException e){
			Set<ConstraintViolation<?>> vs = e.getConstraintViolations();
			crudAction.setError(ErrorType.INVALID_INPUT, ErrorType.INVALID_INPUT_MSG);
			if (vs.size() > 0) {
				for (ConstraintViolation<?> v : vs) {
					crudAction.addDetail(v.getPropertyPath().toString(), v.getMessage());
				}
			}
			clearCurrentContext();
			return exceptionWrap(new BizException("验证错误"), crudAction, 901);
		} catch (GenericJDBCException e) {
			clearCurrentContext();
			return exceptionWrap(e.getSQLException(), crudAction, 902);
		}  catch (QueryException e) {
			clearCurrentContext();
			return exceptionWrap(e, crudAction, 903);
		} catch (IllegalArgumentException e) {
			clearCurrentContext();
			crudAction.setError(ErrorType.INVALID_INPUT, ErrorType.INVALID_INPUT_MSG);
			String msg = e.getMessage();
			if(msg.contains("QueryException") && msg.contains("resolve path")) {
				String field = msg.substring(msg.indexOf("[") + 1, msg.indexOf("]"));
				crudAction.addDetail(field, "Invalid parameter");
			}
			e.printStackTrace();
			return exceptionWrap(new BizException("Invalid parameters"), crudAction, 904);
		} catch (ConfigurationException e) {
			clearCurrentContext();
			return "410";
		} catch (Exception e) {
			clearCurrentContext();
			return exceptionWrap(e, crudAction);
		}
		clearCurrentContext();
		return result;
	}
	
	/**
	 * 清空当前请求线程中隐式参数
	 */
	private void clearCurrentContext() {
		RequestContext.clearFilters();
		LoginContext.setCurrentPublic(false);
		LoginContext.setCurrentLoginUserTemp(false);
	}

	/**
	 * 接口方法. 返回结果前调用该方法.
	 */

	public void beforeResult(ActionInvocation invocation, String result) {
	}

	/**
	 * Action执行之前的拦截器.
	 * 
	 * @param invocation
	 * @throws Exception
	 */
	private void doBeforeAction(ActionInvocation invocation) throws Exception {
		CrudAction crudAction = (CrudAction) invocation.getAction();
		crudAction.setErrorMsg(null);
		HttpServletRequest request = ServletActionContext.getRequest();
		String servletPath = request.getServletPath();
		crudAction.setActionPath(servletPath);
		crudAction.setActionName(ActionContext.getContext().getName());
	}

	/**
	 * 分页前整理分页条件
	 * @param request
	 * @param crudAction
	 * @throws Exception
	 */
	private void doBeforePage(HttpServletRequest request, CrudAction crudAction) throws Exception {
		if (crudAction.getIndex() != null) {
			RequestContext.setIndex(crudAction.getIndex());
		}
		if (crudAction.getSize() != null) {
			RequestContext.setSize(crudAction.getSize());
		}
		Enumeration<?> parameterNames = request.getParameterNames();
		String PARAM_SEP = request.getParameter("_sep");
        if (StringUtils.isEmpty(PARAM_SEP)) {
            PARAM_SEP = "_";
        } else {
            RequestContext.setFilterSep(PARAM_SEP);
        }
		while (parameterNames.hasMoreElements()) {
			String name = (String) parameterNames.nextElement();
			String value = StringUtils.trimToEmpty(request.getParameter(name));
			if (StringUtils.isEmpty(value)) {
				continue;
			}
			if (name.startsWith("__multiselect_")) {
				continue;
			}
			if(name.contains(PARAM_SEP)){
				if(StringUtils.isEmpty(value)){
					continue;
				}
				Object val = value.trim();
				String[] names = name.split(PARAM_SEP);
				String field = names[1];
				String type = "string";
				if(names.length > 2){
					type = names[1];
					field = names[2];
				}
				if(StringUtils.isEmpty(field) || StringUtils.isEmpty(type)){
					continue;
				}
				val = RequestContext.getParamValue(name, value, PARAM_SEP);
				if(val == null) {
					continue;
				}
				RequestContext.addFilter(name, val);
			} else if ("showLastPageIfOverflow".equals(name)) {
				if ("1".equals(value)) {
					RequestContext.setShowLastPageIfOverflow(true);
				}
			}
		}
	}

	/**
	 * 异常控制.
	 * 
	 * @param request
	 */

	public String exceptionWrap(Exception e, CrudAction action) {
		return exceptionWrap(e, action, null);
	}
	
	/**
	 * 异常控制.
	 * 
	 * @param request
	 */

	public String exceptionWrap(Exception e, CrudAction action, int errorId) {
		return exceptionWrap(e, action, null, errorId);
	}
	
	/**
	 * 异常控制.
	 * 
	 * @param request
	 */

	public String exceptionWrap(Exception e, CrudAction action, String defaultResult) {
		return exceptionWrap(e, action, defaultResult, ErrorType.UNKNOWN_SYSTEM_ERROR);
	}
	
	/**
	 * 异常控制.
	 * 
	 * @param request
	 */

	public String exceptionWrap(Exception e, CrudAction action, String defaultResult, int errorId) {
		HttpServletRequest req = ServletActionContext.getRequest();
		String result = defaultResult == null ? "500" : defaultResult;
		String msg = null;
		if (!(e instanceof BizException) && !(e instanceof SysException)) {
			e.printStackTrace();
			logger.error("Server inner error: {}", e.getMessage());
			msg = "Server inner error";
			action.setError(errorId, msg);
		}else {
			BaseException baseE = (BaseException)e;
			msg = e.getMessage();
			int errorcode = baseE.getErrorId();
			if(errorcode != 0) {
				req.setAttribute("errorcode", errorcode);
				action.setError(errorcode, msg);
			}
			if("/user/login".equals(action.getActionPath())) {
				result = "login";
			}
		}
		Throwable cause = ExceptionUtils.getRootCause(e);
		req.setAttribute("error", msg);
		req.setAttribute("exception", e);
		req.setAttribute("cause", cause);
		action.addErrorMsg("tip", msg);
		if(cause != null){
			action.addErrorMsg("cause", cause.getMessage());
		}
		if (PageConstant.isValidDataType(action.getDataType())) {
			action.setResultCode(action.getDataType());
			return action.getDataType();
		}
		return result;
	}

	/**
	 * @return the includeMethods
	 */
	public String getIncludeMethods() {
		return includeMethods;
	}

	/**
	 * @param includeMethods
	 *            the includeMethods to set
	 */
	public void setIncludeMethods(String includeMethods) {
		this.includeMethods = includeMethods;
	}

	/**
	 * 判断是否分页的方法.
	 * 
	 * @param regex
	 * @param t
	 * @return
	 */
	private boolean isListPage(String regex, String t) {
		String[] regexs = regex.split(",");
		for (String r : regexs) {
			Pattern p = Pattern.compile(r);
			Matcher m = p.matcher(t);
			if (m.matches()) {
				return true;
			}
		}
		return false;
	}
}
