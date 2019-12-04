package com.ruidev.framework.freemarker.util;

import java.io.StringWriter;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import org.apache.commons.lang3.StringUtils;

import com.ruidev.framework.exception.BizException;
import com.ruidev.framework.freemarker.manager.RuiDevFreemarkerManager;

import freemarker.cache.StringTemplateLoader;
import freemarker.core.Environment;
import freemarker.template.Configuration;
import freemarker.template.Template;
import freemarker.template.TemplateBooleanModel;
import freemarker.template.TemplateDateModel;
import freemarker.template.TemplateException;
import freemarker.template.TemplateModel;
import freemarker.template.TemplateNumberModel;
import freemarker.template.TemplateScalarModel;

/**
 * Freemarker标签工具类
 */
public abstract class DirectiveUtils {

	public static String getContent(Map<String, Object> params, String templateContent) throws Exception {
		Configuration cfg = RuiDevFreemarkerManager.getConfiguration();
        StringTemplateLoader stringLoader = new StringTemplateLoader();  
        stringLoader.putTemplate("contentTemplate",templateContent); 
        cfg.setTemplateLoader(stringLoader);
		Template template = cfg.getTemplate("contentTemplate","utf-8");
		StringWriter writer = new StringWriter();
		template.process(params, writer);
		return writer.toString();
	}
	
	public static String getContent(Object obj, String templateContent) throws Exception{
		Configuration cfg = RuiDevFreemarkerManager.getConfiguration();
        StringTemplateLoader stringLoader = new StringTemplateLoader();  
        stringLoader.putTemplate("contentTemplate",templateContent); 
        cfg.setTemplateLoader(stringLoader);
        Map<String, Object> params = new HashMap<String, Object>();
        params.put("object", obj);
		Template template = cfg.getTemplate("contentTemplate","utf-8");
		StringWriter writer = new StringWriter();
		template.process(params, writer);
		return writer.toString();
	}
	
	/**
	 * 将params的值复制到variable中
	 * 
	 * @param env
	 * @param params
	 * @return 原Variable中的值
	 * @throws TemplateException
	 */
	public static Map<String, TemplateModel> addParamsToVariable(
			Environment env, Map<String, TemplateModel> params)
			throws TemplateException {
		Map<String, TemplateModel> origMap = new HashMap<String, TemplateModel>();
		if (params.size() <= 0) {
			return origMap;
		}
		Set<Map.Entry<String, TemplateModel>> entrySet = params.entrySet();
		String key;
		TemplateModel value;
		for (Map.Entry<String, TemplateModel> entry : entrySet) {
			key = entry.getKey();
			value = env.getVariable(key);
			if (value != null) {
				origMap.put(key, value);
			}
			env.setVariable(key, entry.getValue());
		}
		return origMap;
	}

	/**
	 * 将variable中的params值移除
	 * 
	 * @param env
	 * @param params
	 * @param origMap
	 * @throws TemplateException
	 */
	public static void removeParamsFromVariable(Environment env,
			Map<String, TemplateModel> params,
			Map<String, TemplateModel> origMap) throws TemplateException {
		if (params.size() <= 0) {
			return;
		}
		for (String key : params.keySet()) {
			env.setVariable(key, origMap.get(key));
		}
	}

	public static String getString(String name,
			Map<String, TemplateModel> params) throws Exception {
		TemplateModel model = params.get(name);
		if (model == null) {
			return null;
		}
		if (model instanceof TemplateScalarModel) {
			return ((TemplateScalarModel) model).getAsString();
		} else if ((model instanceof TemplateNumberModel)) {
			return ((TemplateNumberModel) model).getAsNumber().toString();
		} else {
			throw new BizException(name);
		}
	}

	public static Long getLong(String name, Map<String, TemplateModel> params)
			throws Exception {
		TemplateModel model = params.get(name);
		if (model == null) {
			return null;
		}
		if (model instanceof TemplateScalarModel) {
			String s = ((TemplateScalarModel) model).getAsString();
			if (StringUtils.isBlank(s)) {
				return null;
			}
			try {
				return Long.parseLong(s);
			} catch (NumberFormatException e) {
				throw new BizException(name);
			}
		} else if (model instanceof TemplateNumberModel) {
			return ((TemplateNumberModel) model).getAsNumber().longValue();
		} else {
			throw new BizException(name);
		}
	}

	public static Integer getInt(String name, Map<String, TemplateModel> params)
			throws Exception {
		TemplateModel model = params.get(name);
		if (model == null) {
			return null;
		}
		if (model instanceof TemplateScalarModel) {
			String s = ((TemplateScalarModel) model).getAsString();
			if (StringUtils.isBlank(s)) {
				return null;
			}
			try {
				return Integer.parseInt(s);
			} catch (NumberFormatException e) {
				throw new BizException(name);
			}
		} else if (model instanceof TemplateNumberModel) {
			return ((TemplateNumberModel) model).getAsNumber().intValue();
		} else {
			throw new BizException(name);
		}
	}

	public static Integer[] getIntArray(String name,
			Map<String, TemplateModel> params) throws Exception {
		String str = DirectiveUtils.getString(name, params);
		if (StringUtils.isBlank(str)) {
			return null;
		}
		String[] arr = StringUtils.split(str, ',');
		Integer[] ids = new Integer[arr.length];
		int i = 0;
		try {
			for (String s : arr) {
				ids[i++] = Integer.valueOf(s);
			}
			return ids;
		} catch (NumberFormatException e) {
			throw new BizException(name);
		}
	}

	public static Boolean getBool(String name, Map<String, TemplateModel> params)
			throws Exception {
		TemplateModel model = params.get(name);
		if (model == null) {
			return null;
		}
		if (model instanceof TemplateBooleanModel) {
			return ((TemplateBooleanModel) model).getAsBoolean();
		} else if (model instanceof TemplateNumberModel) {
			return !(((TemplateNumberModel) model).getAsNumber().intValue() == 0);
		} else if (model instanceof TemplateScalarModel) {
			String s = ((TemplateScalarModel) model).getAsString();
			// 空串应该返回null还是true呢？
			if (!StringUtils.isBlank(s)) {
				return !(s.equals("0") || s.equalsIgnoreCase("false") || s
						.equalsIgnoreCase("f"));
			} else {
				return null;
			}
		} else {
			throw new BizException(name);
		}
	}

	public static Date getDate(String name, Map<String, TemplateModel> params)
			throws Exception {
		TemplateModel model = params.get(name);
		if (model == null) {
			return null;
		}
		if (model instanceof TemplateDateModel) {
			return ((TemplateDateModel) model).getAsDate();
		} else if (model instanceof TemplateScalarModel) {
			//DateTypeEditor editor = new DateTypeEditor();
//			editor.setAsText(((TemplateScalarModel) model).getAsString());
//			return (Date) editor.getValue();
			return null;
		} else {
			throw new BizException(name);
		}
	}

}
