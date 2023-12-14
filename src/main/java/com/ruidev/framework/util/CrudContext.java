package com.ruidev.framework.util;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.commons.lang3.StringUtils;

public class CrudContext {

	private static final ThreadLocal<Boolean> noCount = new ThreadLocal<Boolean>();
	private static final ThreadLocal<Map<String, Object>> filters = new ThreadLocal<Map<String, Object>>();
	private static final ThreadLocal<ArrayList<Object>> params = new ThreadLocal<ArrayList<Object>>();
	private static final ThreadLocal<ArrayList<String>> groupbys = new ThreadLocal<ArrayList<String>>();
	private static final ThreadLocal<Map<String, String>> orderbys = new ThreadLocal<Map<String, String>>();
	private static final ThreadLocal<Object[]> boundParams = new ThreadLocal<Object[]>();
	private static final ThreadLocal<Boolean> ignoreWhere = new ThreadLocal<Boolean>();
	
	
	
	public static void setBoundParams(Object[] params) {
		boundParams.set(params);
	}
	
	public static Object[] fetchBoundParams() {
		Object[] params = boundParams.get();
		boundParams.remove();
		return params;
	}
	
	public static void setNoCount(boolean flag) {
		noCount.set(flag);
	}
	
	public static void setIgnoreWhere(boolean flag) {
		ignoreWhere.set(flag);
	}
	
	public static boolean getNoCount() {
		Boolean flag = noCount.get();
		return flag != null && flag.booleanValue();
	}
	
	public static Map<String, Object> getFilters(){
		Map<String, Object> _filters = filters.get();
		if(_filters == null){
			_filters = new LinkedHashMap<String, Object>();
			filters.set(_filters);
		}
		return filters.get();
	}
	
	public static void addCrudFilter(String key, Object value){
		Map<String, Object> _filters = filters.get();
		if(_filters == null){
			_filters = new LinkedHashMap<String, Object>();
			filters.set(_filters);
		}
		_filters.put(key, value);
	}
	
	public static void addFilter(String key, Object value){
		if(StringUtils.isEmpty(key))return;
		if(!key.startsWith("hql_") && !key.startsWith("sql_") && value == null)return;
		if(value instanceof String && StringUtils.isEmpty(value.toString())) {
			return;
		}
		if(value instanceof Collection) {
			if(((Collection<?>)value).size()<1)return;
		}
		addCrudFilter(key, value);
	}
	
	public static void addMultiFilter(String key, Object... value){
		for(Object val : value) {
			if(val == null)return;
		}
		addCrudFilter(key, value);
	}
	
	public static void removeFilter(String key) {
		Map<String, Object> _filters = filters.get();
		if(_filters != null){
			_filters.remove(key);
		}
	}
	
	public static Object[] getFilterParams(Object[] paramObjs) {
		ArrayList<Object> _params = params.get();
		if(_params != null) {
			Object[] params = new Object[_params.size() + paramObjs.length];
			int i = 0;
			for(Object param : paramObjs) {
				params[i] = param;
				i++;
			}
			for(Object param : _params) {
				params[i] = param;
				i++;
			}
			_params.clear();
			return params;
		}
		return paramObjs;
	}
	
	public static List<String> getGroupBys(){
		return groupbys.get();
	}
	
	public static void addGroupby(String key){
		ArrayList<String> _groupbys = groupbys.get();
		if(_groupbys == null){
			_groupbys = new ArrayList<String>();
			_groupbys.add(key);
			groupbys.set(_groupbys);
		}else if(!_groupbys.contains(key)) {
			_groupbys.add(key);
		}
	}
	
	public static Map<String, String> getOrderBys(){
		return orderbys.get();
	}
	
	public static void addOrderBy(String key, String order){
		Map<String, String> _orderbys = orderbys.get();
		if(_orderbys == null){
			_orderbys = new LinkedHashMap<String, String>();
			orderbys.set(_orderbys);
		}
		_orderbys.put(key, order);
		//setValueStack(key, order);
	}
	
	public static void clearFilters(){
		noCount.remove();
		groupbys.remove();
		params.remove();
		orderbys.remove();
		filters.remove();
		ignoreWhere.remove();
	}
	
	public static void clearParams() {
		ArrayList<Object> _params = params.get();
		if(_params == null){
			return;
		}
		_params.clear();
	}
	
	public static void clearGroupbys(){
		ArrayList<String> _groupbys = groupbys.get();
		if(_groupbys == null){
			return;
		}
		_groupbys.clear();
	}
	
	@SuppressWarnings("unchecked")
	private static int processField(StringBuffer hsqlByFilter, String columnStr, ArrayList<Object> _params, Object value) {
		String sep = RequestContext.getFilterSep();
        String[] columnsDesc = columnStr.split(sep);
		String op = columnsDesc[0];
		String columnName = columnsDesc.length == 2 ? columnsDesc[1] : columnsDesc[2];
		if ("in".equalsIgnoreCase(op)) {
			List<Object> values = (List<Object>) value;
			if (values == null || values.size() < 1) {
				return 0;
			} else {
				hsqlByFilter.append(columnName);
				_params.add(value);
				if (values.size() == 1) {
					hsqlByFilter.append(" = ? ");
				} else {
					hsqlByFilter.append(" in ? ");
				}
				return 1;
			} 
		}else if("hql".equalsIgnoreCase(op) || "sql".equalsIgnoreCase(op)) {
			hsqlByFilter.append(" ").append(columnStr.substring(4));
			if(value != null) {
				if(value instanceof Object[]) {
					for(Object __value : (Object[])value) {
						if(__value != null) {
							_params.add(__value);
						}
					}
				}else {
					_params.add(value);
				}
			}
			return 1;
		}
		if ("like".equalsIgnoreCase(op)) {
			_params.add(CommonUtil.combineStrings("%", value.toString(), "%"));
		}else {
			_params.add(value);
		}
		hsqlByFilter.append(columnName);
		if ("like".equalsIgnoreCase(op)) {
			hsqlByFilter.append(" like ? ");
		} else if ("gt".equalsIgnoreCase(op)) {
			hsqlByFilter.append(" > ? ");
		} else if ("ge".equalsIgnoreCase(op)) {
			hsqlByFilter.append(" >= ? ");
		} else if ("lt".equalsIgnoreCase(op)) {
			hsqlByFilter.append(" < ? ");
		} else if ("le".equalsIgnoreCase(op)) {
			hsqlByFilter.append(" <= ? ");
		} else if ("neq".equalsIgnoreCase(op)) {
			if ("IS_NULL".equals(value)) {
				hsqlByFilter.append(" is not null ");
			}else {
				hsqlByFilter.append(" != ? ");
			}
		} else {
			if ("IS_NULL".equals(value)) {
				hsqlByFilter.append(" is null ");
			}else {
				hsqlByFilter.append(" = ? ");
			}
		}
		return 1;
	}
	
	public static String getHsql(String hsql) {
		StringBuffer hsqlByFilter = new StringBuffer();
		StringBuffer groupByHql = null, orderByHql = null;
		int lastIndexOfOrderByHql = hsql.lastIndexOf(" order by ");
		if(lastIndexOfOrderByHql > 0) {
			orderByHql = new StringBuffer(hsql.substring(lastIndexOfOrderByHql, hsql.length()));
			hsql = hsql.substring(0, lastIndexOfOrderByHql);
		}
		int lastIndexOfGroupByHql = hsql.lastIndexOf(" group by ");
		if(lastIndexOfGroupByHql > 0) {
			groupByHql = new StringBuffer(hsql.substring(lastIndexOfGroupByHql, hsql.length()));
			hsql = hsql.substring(0, lastIndexOfGroupByHql);
		}
		hsqlByFilter.append(hsql);
		Map<String, Object> filters = getFilters();
		Set<String> columns = filters.keySet();
		if(columns.size() > 0) {
			if(hsql.contains("where") || Boolean.TRUE.equals(ignoreWhere.get())) {
				hsqlByFilter.append(" and ");
			}else {
				hsqlByFilter.append(" where ");
			}
			ArrayList<Object> _params = params.get();
			if(_params == null) {
				_params = new ArrayList<Object>();
				params.set(_params);
			}
			Iterator<String> columnIt = columns.iterator();
			final String MULTI_SEP = "::";
			while(columnIt.hasNext()) {
				String columnStr = columnIt.next();
				Object value = filters.get(columnStr);
				int result = 0;
				if(columnStr.contains(MULTI_SEP)) {
					String[] columnStrs = columnStr.split(MULTI_SEP);
					int len = columnStrs.length;
					Object[] _values = (Object[])value;
					hsqlByFilter.append("(");
					for(int i=0,j=0;i<len;i+=2,j++) {
						String _columnStr = columnStrs[i];
						Object _value = _values.length>j?_values[j]:_values[_values.length-1];
						result = processField(hsqlByFilter, _columnStr, _params, _value);
						if(i+1<len && result > 0) {
							hsqlByFilter.append(" ").append(columnStrs[i+1]).append(" ");
						}
					}
					hsqlByFilter.append(")");
				}else {
					result = processField(hsqlByFilter, columnStr, _params, value);
				}
				if(columnIt.hasNext() && result > 0) {
					hsqlByFilter.append(" and ");
				}
			}
		}
		List<String> groups = groupbys.get();
		if(groups != null && groups.size() > 0) {
//			if(!hsqlByFilter.toString().contains("group by")) {
//				hsqlByFilter.append(" group by ");
//			}
			if(groupByHql == null) {
				groupByHql = new StringBuffer(" group by ");
			}
			Iterator<String> keyIt = groups.iterator();
			while(keyIt.hasNext()) {
				String orderKey = keyIt.next();
//				hsqlByFilter.append(" ").append(orderKey);
				groupByHql.append(" ").append(orderKey);
				if(keyIt.hasNext()) {
//					hsqlByFilter.append(",");
					groupByHql.append(",");
				}
			}
		}
		if(groupByHql != null) {
			hsqlByFilter.append(" ").append(groupByHql);
		}
		Map<String, String> orders = orderbys.get();
		if(orders != null && orders.size() > 0) {
//			if(!hsqlByFilter.toString().contains("order by")) {
//				hsqlByFilter.append(" order by ");
//			}
			if(orderByHql == null) {
				orderByHql = new StringBuffer(" order by ");
			}
			Iterator<String> keyIt = orders.keySet().iterator();
			while(keyIt.hasNext()) {
				String orderKey = keyIt.next();
//				hsqlByFilter.append(" ").append(orderKey).append(" ").append(orders.get(orderKey));
				orderByHql.append(" ").append(orderKey).append(" ").append(orders.get(orderKey));
				if(keyIt.hasNext()) {
//					hsqlByFilter.append(",");
					orderByHql.append(",");
				}
			}
		}
		if(orderByHql != null) {
			hsqlByFilter.append(" ").append(orderByHql);
		}
		return hsqlByFilter.toString();
	}
}