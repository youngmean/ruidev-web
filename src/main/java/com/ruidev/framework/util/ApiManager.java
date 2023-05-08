package com.ruidev.framework.util;

import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.core.annotation.AnnotationUtils;

import com.ruidev.framework.annotations.ApiName;
import com.ruidev.framework.annotations.ApiSort;
import com.ruidev.framework.api.BaseApi;
import com.ruidev.framework.constant.BaseConstants;

/**
 * 
 *
 * @author	 	锐开科技 
 * @Copyright 	www.ruidev.com All rights reserved. 
 */
public class ApiManager {

	private static Map<String, List<BaseApi>> APIS = new HashMap<String, List<BaseApi>>();
	
	@SuppressWarnings("unchecked")
	public static <T extends BaseApi> List<T> getApis(Class<T> apiClass) {
		List<T> apis = (List<T>) APIS.get(apiClass.getName());
		if(apis == null) {
			apis = initApis(apiClass);
			APIS.put(apiClass.getName(), (List<BaseApi>) apis);
		}
		return apis;
	}
	
	/**
	 * 返回第一个匹配的api实现实例<br>
	 * 注意:若apiClass是抽象类,调用时请注意判空
	 * @param apiClass
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public static <T extends BaseApi> T getApi(Class<T> apiClass) {
		List<T> apis = (List<T>) APIS.get(apiClass.getName());
		if(apis == null) {
			apis = initApis(apiClass);
			APIS.put(apiClass.getName(), (List<BaseApi>) apis);
		}
		return apis.size() > 0 ? apis.get(0) : null;
	}
	
	/**
	 * 返回第一个匹配的api实现实例<br>
	 * 注意:若apiClass是抽象类,调用时请注意判空
	 * @param apiClass
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public static <T extends BaseApi> T getApi(Class<T> apiClass, String apiName) {
		List<T> apis = (List<T>) APIS.get(apiClass.getName());
		if(apis == null) {
			apis = initApis(apiClass);
			APIS.put(apiClass.getName(), (List<BaseApi>) apis);
		}
		for(T api : apis) {
			Class<?> o1Class = api.getClass();
			ApiName apiAnno = AnnotationUtils.findAnnotation(o1Class, ApiName.class);
			if(apiAnno != null) {
				String apiname = apiAnno.name();
				if(apiName.equals(apiname)) {
					return api;
				}
			}
		}
		return null;
	}
	
	/**
	 * 初始化功能权限Api
	 */
	private static <T extends BaseApi> List<T> initApis(Class<T> apiClass) {
		Map<String, T> _PERMISSION_APIS = null;
		if(BaseConstants.SPRING_APPLICATION_CONTEXT != null) {
			_PERMISSION_APIS = BaseConstants.SPRING_APPLICATION_CONTEXT.getBeansOfType(apiClass);
		}else {
			_PERMISSION_APIS = new HashMap<String, T>();
		}
		List<T> apis = new ArrayList<T>();
		if(_PERMISSION_APIS != null && _PERMISSION_APIS.size() > 0) {
			for(T api : _PERMISSION_APIS.values()) {
				apis.add(api);
			}
			apis.sort(new Comparator<T>() {
				@Override
				public int compare(T o1, T o2) {
					int o1Index = 0;
					int o2Index = 0;
					Class<?> o1Class = o1.getClass();
					ApiSort sort = AnnotationUtils.findAnnotation(o1Class, ApiSort.class);
					if(sort != null) {
						o1Index = o1Class.getAnnotation(ApiSort.class).index();
					}
					Class<?> o2Class = o2.getClass();
					ApiSort sort2 = AnnotationUtils.findAnnotation(o2Class, ApiSort.class);
					if(sort2 != null) {
						o2Index = sort2.index();
					}
					return o1Index - o2Index;
				}
			});
		}else {
			try {
				if(!Modifier.isAbstract(apiClass.getModifiers())) {
					apis.add(apiClass.newInstance());
				}
			} catch (Exception e) {
			}
		}
		return apis;
	}
}
