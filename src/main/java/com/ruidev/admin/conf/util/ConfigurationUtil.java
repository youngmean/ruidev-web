package com.ruidev.admin.conf.util;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;
import org.apache.struts2.ServletActionContext;
import org.apache.struts2.util.TextProviderHelper;
import org.springframework.beans.BeansException;
import org.springframework.web.context.WebApplicationContext;

import com.ruidev.admin.conf.bo.ConfBo;
import com.ruidev.admin.conf.bo.IConfCtrl;
import com.ruidev.framework.constant.BaseConstants;
import com.ruidev.framework.constant.SystemEnum;
import com.ruidev.framework.util.ClassUtil;
import com.ruidev.framework.util.CommonUtil;
import com.ruidev.framework.util.LoginContext;

public class ConfigurationUtil {

	private static IConfCtrl ctrl;
	
	private static ConfigurationUtil instance = new ConfigurationUtil();
	
	private static Map<String, SystemEnum[]> enums = new HashMap<String, SystemEnum[]>();
	
	private static Map<String, Map<String, String>> enumsmap = new HashMap<String, Map<String, String>>();
	private static Map<String, SystemEnum> enumsbycode = new HashMap<String, SystemEnum>();
	
	private final static Logger log = Logger.getLogger(ConfigurationUtil.class);

	public static void setConfCtrl(IConfCtrl confCtrl) {
		ConfigurationUtil.ctrl = confCtrl;
	}
	
	private ConfigurationUtil(){}
	
	private Map<String, Class<SystemEnum>> enumSource;
	public void init(){
		if(enumSource != null){
			for(Map.Entry<String, Class<SystemEnum>> entry : enumSource.entrySet()){
				String key = entry.getKey();
				Class<SystemEnum> clazz = entry.getValue();
				if(clazz.isEnum()){
					if(enums.containsKey(key)){
						throw new IllegalStateException("重复的Key:" + key);
					}
					addEnum(key, clazz.getEnumConstants());
				}
			}
		}

		try {
			Set<Class<SystemEnum>> classes = ClassUtil.getSubClasses("com.ruidev", SystemEnum.class);
			for(Class<SystemEnum> clazz : classes) {
				ConfigurationUtil.addEnum(clazz.getSimpleName(), (SystemEnum[]) clazz.getMethod("values").invoke(null));
			}
		} catch (Exception e) {
			log.error(CommonUtil.combineStrings("Init system enums failed: ", e.getMessage()));
		}
	}
	
	public IConfCtrl getCtrl() {
		return ctrl;
	}

	public void setCtrl(IConfCtrl ctrl) {
		ConfigurationUtil.ctrl = ctrl;
	}

	public static ConfigurationUtil getInstance(){
		return instance;
	}
	
	public String val(String code){
		return ConfigurationUtil.getConfigurationValue(code);
	}
	
	public String wurl(String source){
		if (!StringUtils.isEmpty(source)) {
			if (!source.startsWith("http")) {
				if(!source.startsWith("/")){
					source = "/" + source;
				}
				String webResourcePath = ConfigurationUtil.getConfigurationValue("WEB_RESOURCE_PATH").replaceAll("\\\\/$", "");
				if(StringUtils.isEmpty(webResourcePath)){
					webResourcePath = ServletActionContext.getRequest().getContextPath();
				}
				return webResourcePath + source;
			}
		}
		return source;
	}
	
	public String i18n(String name, Object...args) {
		String defaultMsg = name;
		if(defaultMsg.contains(".")){
			defaultMsg = defaultMsg.substring(defaultMsg.indexOf(".") + 1);
		}
		String text = "";
		HttpServletRequest req = null;
		try {
			req = ServletActionContext.getRequest();
		} catch (Exception e) {
		}
		if(req == null) {
			return defaultMsg;
		}
		if(args.length<1) {
			text = TextProviderHelper.getText(name, defaultMsg, ServletActionContext.getValueStack(req));
		}else {
			List<Object> params = new ArrayList<Object>();
			for(Object obj : args) {
				params.add(obj);
			}
			text = TextProviderHelper.getText(name, defaultMsg, params, ServletActionContext.getValueStack(req));
		}
		return text;
	}
	
	public String i18nWithoutDefault(String name, Object...args) {
		String text = null;
		HttpServletRequest req = null;
		try {
			req = ServletActionContext.getRequest();
		} catch (Exception e) {
		}
		if(req == null) {
			return name;
		}
		if(args.length<1) {
			text = TextProviderHelper.getText(name, "", ServletActionContext.getValueStack(req));
		}else {
			List<Object> params = new ArrayList<Object>();
			for(Object obj : args) {
				params.add(obj);
			}
			text = TextProviderHelper.getText(name, "", params, ServletActionContext.getValueStack(req));
		}
		return text;
	}
	
	public Map<String, String> list(String code) {
		return ConfigurationUtil.getConfigurationMap(code);
	}
	
	public SystemEnum[] enums(String code){
		return enums.get(code);
	}
	
	@SuppressWarnings("unchecked")
	public <T extends SystemEnum>T getEnum(Class<T> clazz, String code){
		SystemEnum[] enums = enums(clazz.getSimpleName());
		if(enums == null || enums.length < 1) {
			return null;
		}
		for(SystemEnum _enum : enums) {
			if(_enum.getCode().equals(code)) {
				return (T) _enum;
			}
		}
		return null;
	}
	
	@SuppressWarnings("unchecked")
	public <T extends SystemEnum>T getEnumByLabel(Class<T> clazz, String label){
		SystemEnum[] enums = enums(clazz.getSimpleName());
		if(enums == null || enums.length < 1) {
			return null;
		}
		for(SystemEnum _enum : enums) {
			if(_enum.getLabel().equals(label)) {
				return (T) _enum;
			}
		}
		return null;
	}
	
	public Map<String, String> enumsmap(String code){
		return enumsmap.get(code);
	}
	
	public String enumslabel(String key, String code){
		Map<String, String> map = enumsmap.get(key);
		if(map == null){
			return null;
		}
		SystemEnum e = enumsbycode.get(CommonUtil.combineStrings(key, ".", code));
		if(e != null && !LoginContext.isCurrentUserAdmin()) {
			String label = i18nWithoutDefault(CommonUtil.combineStrings(key, ".", ((Enum<?>)e).name()));
			if(!StringUtils.isEmpty(label)) {
				return label;
			}
		}
		return map.get(code);
	}
	
	public String enumslabel(Class<?> clazz, String code){
		Map<String, String> map = enumsmap.get(clazz.getSimpleName());
		if(map == null){
			return null;
		}
		String key = clazz.getSimpleName();
		SystemEnum e = enumsbycode.get(CommonUtil.combineStrings(key, ".", code));
		if(e != null && !LoginContext.isCurrentUserAdmin()) {
			String label = i18nWithoutDefault(CommonUtil.combineStrings(key, ".", ((Enum<?>)e).name()));
			if(!StringUtils.isEmpty(label)) {
				return label;
			}
		}
		return map.get(code);
	}
	
	public static void addEnum(String key, SystemEnum[] _enums){
		enums.put(key, _enums);
		Map<String, String> map = enumsmap.get(key);
		if(map == null){
			map = new HashMap<String, String>();
		}
		for(SystemEnum e : _enums){
			map.put(e.getCode(), e.getLabel());
			enumsbycode.put(CommonUtil.combineStrings(key, ".", e.getCode()), e);
		}
		enumsmap.put(key, map);
	}
	
	public static void setConfigurationValue(String code, String value){
	    if(ctrl == null){
	        try {
	        	WebApplicationContext w = BaseConstants.SPRING_APPLICATION_CONTEXT;
                ctrl = w.getBean(ConfBo.class);
            } catch (BeansException e) {
            }
        }
	    if(ctrl == null){
	        log.error("Configuration Ctrl is null");
	        return;
	    }
	    try {
			ctrl.getConfurations().put(code, value);
		} catch (Exception e) {
		}
	}

	public static String getConfigurationValue(String code) {
	    if(ctrl == null){
            try {
            	WebApplicationContext w = BaseConstants.SPRING_APPLICATION_CONTEXT;
                ctrl = w.getBean(ConfBo.class);
            } catch (BeansException e) {
            }
        }
		if(ctrl == null){
			return "";
		}
		String value = "";
		try {
			value = ctrl.getConfurations().get(code);
		} catch (Exception e) {
		}
		if(StringUtils.isEmpty(value)){
			value = "";
		}
		return value;
	}
	
	public static Map<String, String> getConfigurationMap(String code) {
	    if(ctrl == null){
            try {
            	WebApplicationContext w = BaseConstants.SPRING_APPLICATION_CONTEXT;
                ctrl = w.getBean(ConfBo.class);
            } catch (BeansException e) {
            }
        }
	    Map<String, String> _default = new HashMap<String, String>();
		if(ctrl == null){
			return _default;
		}
		Map<String, String> value = null;
		try {
			value = ctrl.getConfurationsAsMap().get(code);
		} catch (Exception e) {
		}
		if(value == null){
			value = _default;
		}
		return value;
	}

	public Map<String, Class<SystemEnum>> getEnumSource() {
		return enumSource;
	}

	public void setEnumSource(Map<String, Class<SystemEnum>> enumSource) {
		this.enumSource = enumSource;
	}
}
