package com.ruidev.framework.util;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

import org.apache.commons.beanutils.PropertyUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.struts2.ServletActionContext;
import org.apache.struts2.util.TextProviderHelper;
import org.springframework.stereotype.Service;
import org.springframework.web.context.WebApplicationContext;

@Service("webUtils")
public class BeanUtils4Web {
	private WebApplicationContext wac = null;
	
	public static final Logger log = LogManager.getLogger(BeanUtils4Web.class);

	/**
	 * 执行bean并返回结果
	 * @param beanName
	 * @param methodName
	 * @return
	 * @throws Exception
	 */
	public Object get(String beanName, String methodName) throws Exception {
		Object _value = null;
		if (wac == null) {
			log.error("WebApplicationContext is not init.");
		} else {
			Object _bean = wac.getBean(beanName);
			if (_bean == null)
				return null;

			if (methodName.indexOf("(") == -1) {
				_value = PropertyUtils.getProperty(_bean, methodName);
			} else {
				_value = getObjByStringMethod(_bean, methodName);
			}

		}
		return _value;
	}
	
	public Object get(String beanName, String methodName, Object...params) throws Exception {
		if(methodName.contains("(") || params.length < 1) {
			return get(beanName, methodName);
		}
		Object _bean = wac.getBean(beanName);
		if (_bean == null) {
			return null;
		}
		return getObjByMethod(_bean, methodName, params);
	}
	
	/**
	 * 获取I18N值
	 * @param name
	 * @return
	 */
	public String text(String name){
		String defaultMsg = name;
		if(defaultMsg.contains(".")){
			defaultMsg = defaultMsg.substring(defaultMsg.indexOf(".") + 1);
		}
		String text = TextProviderHelper.getText(name, defaultMsg, ServletActionContext.getValueStack(ServletActionContext.getRequest()));
		if(StringUtils.isEmpty(text)){
			text = "";
		}
		return text;
	}
	
	@SuppressWarnings({ "rawtypes", "unchecked" })
	private Object getObjByMethod(Object obj, String methodName, Object...params) throws Exception {
		Object object = null;
		Class[] paramTypes = null;
		Method met = null;
		for (Method m : obj.getClass().getMethods()) {
			paramTypes = m.getParameterTypes();
			if (methodName.equals(m.getName())) {
				if(paramTypes.length == params.length) {
					if(paramTypes.length == 1){
						if(paramTypes[0] != Class.class){
							met = m;
							break;
						}
					}else{
						met = m;
						break;
					}
				}else if(paramTypes.length > 1 && paramTypes[paramTypes.length - 1] == Object[].class) {
					met = m;
					break;
				}
			}
		}
		if (met != null) {
			Object[] realParams = new Object[params.length];
			for (int _i = 0; _i < params.length; _i++) {
				if (paramTypes[_i] == String.class) {
					realParams[_i] = "" + params[_i];
				}else if (paramTypes[_i] == Object[].class) {
					realParams[_i] = new Object[] {params[_i]};
				} else {
					// 常规数据类型都有valueOf，获取这个方法
				    //System.out.println(paramTypes[_i].getName() + " " + method + " " + obj);
					Method valueOfMethod = paramTypes[_i].getMethod("valueOf", String.class);
					// 构造参数类型
					realParams[_i] = valueOfMethod.invoke(
							paramTypes[_i], params[_i]);
				}
			}
			object = met.invoke(obj, realParams);
		} else {
			String msg = CommonUtil.combineStrings("不存在这个方法，请检查", obj.getClass().getSimpleName(), ".", methodName);
			log.error(msg);
			throw new Exception(msg);
		}
		return object;
	}

	/**
	 * 获得一个bean中的一个方法
	 * 
	 * @param key
	 * @param column
	 * @param value
	 * @return
	 * @throws NoSuchMethodException
	 * @throws SecurityException
	 * @throws InvocationTargetException
	 * @throws IllegalAccessException
	 * @throws IllegalArgumentException
	 */
	@SuppressWarnings({ "rawtypes", "unchecked" })
	private Object getObjByStringMethod(Object obj, String method)
			throws SecurityException, NoSuchMethodException,
			IllegalArgumentException, IllegalAccessException,
			InvocationTargetException, Exception {
		Object object = null;
		if (obj != null && method.indexOf("(") != -1) {
			String methodName = method.substring(0, method.indexOf("("));
			Object[] params = method.substring(method.indexOf("(") + 1,
					method.lastIndexOf(")")).split(",");
			Method met = null;
			if (params.length > 0 && String.valueOf(params[0]).length() != 0) {
				// 获取参数类型
				Class[] paramTypes = null;
				for (Method m : obj.getClass().getMethods()) {
					paramTypes = m.getParameterTypes();
					if (paramTypes.length == params.length && methodName.equals(m.getName())) {
					    if(paramTypes.length == 1){
					    	if(paramTypes[0] != Class.class){
					    		met = m;
					    		break;
					    	}
					    }else{
					    	met = m;
					    	break;
					    }
					}
				}
				if (met != null) {
					Object[] realParams = new Object[params.length];
					for (int _i = 0; _i < params.length; _i++) {
						if (paramTypes[_i] == String.class) {
							realParams[_i] = "" + params[_i];
						}else if (paramTypes[_i] == Object[].class) {
							realParams[_i] = new Object[] {params[_i]};
						} else {
							// 常规数据类型都有valueOf，获取这个方法
						    //System.out.println(paramTypes[_i].getName() + " " + method + " " + obj);
							Method valueOfMethod = paramTypes[_i].getMethod("valueOf", String.class);
							// 构造参数类型
							realParams[_i] = valueOfMethod.invoke(
									paramTypes[_i], params[_i]);
						}
					}
					object = met.invoke(obj, realParams);
				} else {
					String msg = CommonUtil.combineStrings("不存在这个方法，请检查", obj.getClass().getSimpleName(), ".", methodName);
					log.error(msg);
					throw new Exception(msg);
				}
			} else {
				met = obj.getClass().getMethod(methodName);
				object = met.invoke(obj);
			}
		}
		return object;
	}

	/**
	 * 初始化工具类. 获得spring上下文
	 * 
	 * @param sc
	 * @throws Exception
	 */
	public void initUtils(WebApplicationContext wac) throws Exception {
		this.wac = wac;
		log.debug("初始化webUtils工具");
	}
}