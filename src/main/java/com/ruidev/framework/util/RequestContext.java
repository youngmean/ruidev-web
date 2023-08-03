package com.ruidev.framework.util;
import java.math.BigDecimal;
import java.sql.Date;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang3.StringUtils;
import org.apache.struts2.ServletActionContext;

import com.ruidev.framework.constant.PageConstant;
import com.ruidev.framework.user.IUserSessionInfo;

public class RequestContext {

	private static final ThreadLocal<Boolean> setShowLastPageIfOverflow = new ThreadLocal<Boolean>();
	private static final ThreadLocal<Integer> index = new ThreadLocal<Integer>();
	private static final ThreadLocal<Integer> total = new ThreadLocal<Integer>();
	private static final ThreadLocal<Integer> size = new ThreadLocal<Integer>();
	private static final ThreadLocal<String> FILTER_SEP_STR = new ThreadLocal<String>();
	private static final ThreadLocal<Long> userid = new ThreadLocal<Long>();
	private static final ThreadLocal<Boolean> noCount = new ThreadLocal<Boolean>();
	private static final ThreadLocal<IUserSessionInfo> user = new ThreadLocal<IUserSessionInfo>();
	private static final ThreadLocal<Map<String, Object>> filters = new ThreadLocal<Map<String, Object>>();
	private static final ThreadLocal<ArrayList<Object>> params = new ThreadLocal<ArrayList<Object>>();
	private static final ThreadLocal<ArrayList<String>> groupbys = new ThreadLocal<ArrayList<String>>();
	private static final ThreadLocal<Map<String, String>> orderbys = new ThreadLocal<Map<String, String>>();
	private static final ThreadLocal<Class<?>> transformer_class = new ThreadLocal<Class<?>>();
	private static final ThreadLocal<List<String>> only_fetch_properties = new ThreadLocal<List<String>>();
	private static final ThreadLocal<String> current_data_source = new ThreadLocal<String>();
	
	public static void setCurrentDataSourceName(String dataSourceName) {
		current_data_source.set(dataSourceName);
	}
	
	public static String getCurrentDataSourceName() {
		return current_data_source.get();
	}
	
	public static void setTransformerClass(Class<?> clazz) {
		transformer_class.set(clazz);
	}
	
	public static Class<?> getTransformerClass() {
		return transformer_class.get();
	}
	
	public static void setOnlyFetchProperties(String... props) {
		only_fetch_properties.set(Arrays.asList(props));
	}
	
	public static List<String> getOnlyFetchProperties() {
		return only_fetch_properties.get();
	}
	
	public static void setNoCount(boolean flag) {
		noCount.set(flag);
	}
	
	public static boolean getNoCount() {
		Boolean flag = noCount.get();
		return flag != null && flag.booleanValue();
	}
	
	public static void setShowLastPageIfOverflow(boolean flag) {
		setShowLastPageIfOverflow.set(flag);
	}
	
	public static boolean getShowLastPageIfOverflow() {
		Boolean flag = setShowLastPageIfOverflow.get();
		return flag != null && flag.booleanValue();
	}
	
	public static Map<String, Object> getFilters(){
		Map<String, Object> _filters = filters.get();
		if(_filters == null){
			_filters = new HashMap<String, Object>();
			filters.set(_filters);
		}
		return filters.get();
	}
	
	public static void addCrudFilter(String key, Object value){
		Map<String, Object> _filters = filters.get();
		if(_filters == null){
			_filters = new HashMap<String, Object>();
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
		addCrudFilter(key, value);
		try {
			setValueStack(key, value);
		} catch (NullPointerException e) {
		}
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
	
	public static void setValueStack(String name, Object value) {
		HttpServletRequest req = ServletActionContext.getRequest();
        if (req != null) {
            ServletActionContext
                    .getValueStack(ServletActionContext.getRequest())
                    .set(name, value);
            // if (name.contains(".")) {
            String attr = name.replaceAll("[\\.\\-\\:]", "_");
            req.setAttribute(attr, value);
            // }
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
	
	public static void setFilterSep(String sep) {
        FILTER_SEP_STR.set(sep);
    }

    public static String getFilterSep() {
        String sep = FILTER_SEP_STR.get();
        if (StringUtils.isEmpty(sep)) {
            sep = "_";
        }
        return sep;
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
		setShowLastPageIfOverflow.remove();
		noCount.remove();
		FILTER_SEP_STR.remove();
		setSize(null);
		setIndex(null);
		setTotal(null);
		clearGroupbys();
		clearParams();
		if(orderbys.get() != null) {
			orderbys.get().clear();
		}
		Map<String, Object> _filters = filters.get();
		if(_filters == null){
			return;
		}
		_filters.clear();
		transformer_class.remove();
		only_fetch_properties.remove();
		current_data_source.remove();
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
	
	public static void clearUserInfo() {
		user.set(null);
	}
	
	public static int getBeginIndex() {
		Integer index = getIndex();
		Integer size = getSize();
		if(index != null && size != null) {
			int begin = (index - 1 )* size;
			return begin < 0 ? 0 : begin;
		}
		return 0;
	}

	public static Integer getIndex() {
		return index.get() == null ? 1 : index.get();
	}
	public static void setIndex(Integer _index) {
		index.set(_index);
	}
	public static Integer getSize() {
		Integer _size = size.get();
		if(_size == null) {
			return PageConstant.DEFAULT_PAGE_SIZE;
		}
		return _size;
	}
	public static void setSize(Integer _size) {
		size.set(_size);
	}
	public static Integer getTotal() {
		return total.get() == null ? 0 : total.get();
	}
	public static void setTotal(Integer _total) {
		total.set(_total);
	}
	public static Long getUserId() {
		return userid.get();
	}
	public static void setUserId(Long _userid) {
		userid.set(_userid);
	}
	public static IUserSessionInfo getUser() {
		return user.get();
	}
	public static void setUser(IUserSessionInfo _user) {
		user.set(_user);
	}
	public static String getParam(String name){
		return ServletActionContext.getRequest().getParameter(name);
	}
	
	public static String[] getParams(String name){
		return ServletActionContext.getRequest().getParameterValues(name);
	}
	
	public static Object getParamValue(String name, String value, String PARAM_SEP) {
		Object val = value.trim();
		String[] names = name.split(PARAM_SEP);
		String type = "string";
		if(names.length > 2){
			type = names[1];
		}
		if(name.startsWith("in" + PARAM_SEP)){
			String[] vals = ServletActionContext.getRequest().getParameterValues(name);
			List<Object> paramVals = new ArrayList<Object>();
			for(String _val: vals) {
				if(StringUtils.isEmpty(_val)) {
					continue;
				}
				if("int".equalsIgnoreCase(type)){
					paramVals.add(Integer.valueOf(_val));
				}else if("long".equalsIgnoreCase(type)){
					paramVals.add(Long.valueOf(_val));
				}else if("date".equalsIgnoreCase(type)){
					paramVals.add(getDateParamVal(_val));
				}else if("double".equalsIgnoreCase(type)){
					paramVals.add(Double.valueOf(_val));
				}else if("decimal".equalsIgnoreCase(type)){
					paramVals.add(new BigDecimal(_val));
				}else {
					paramVals.add(_val);
				}
			}
			if(paramVals.size() < 1) {
				return null;
			}
			return paramVals;
		}
		if("int".equalsIgnoreCase(type)){
			val = RequestContext.getIntegerParam(name);
		}else if("long".equalsIgnoreCase(type)){
			val = RequestContext.getLongParam(name);
		}else if("date".equalsIgnoreCase(type)){
			val = getDateParamVal(name);
		}else if("double".equalsIgnoreCase(type)){
			val = RequestContext.getDoubleParam(name);
		}else if("decimal".equalsIgnoreCase(type)){
			val = RequestContext.getBigDecimalParam(name);
		}
		return val;
	}
	
	public static Integer getIntegerParam(String name){
		try {
			return Integer.valueOf(getParam(name));
		} catch (NumberFormatException e) {
		}
		return null;
	}
	
	public static List<Integer> getIntegerParams(String name){
		return getIntegerValues(getParam(name));
	}
	
	public static List<Integer> getIntegerValues(String vals){
		if(vals == null)return null;
		List<Integer> values = new ArrayList<Integer>();
		for(String val : vals.split(",")) {
			try {
				values.add(Integer.valueOf(val));
			} catch (NumberFormatException e) {
			}
		}
		return values.size() < 1 ? null : values;
	}
	
	public static Long getLongParam(String name){
		try {
			return Long.valueOf(getParam(name));
		} catch (NumberFormatException e) {
		}
		return null;
	}
	
	public static List<Long> getLongParams(String name){
		return getLongValues(getParam(name));
	}
	
	public static List<Long> getLongValues(String vals){
		if(vals == null)return null;
		List<Long> values = new ArrayList<Long>();
		for(String val : vals.split(",")) {
			try {
				values.add(Long.valueOf(val));
			} catch (NumberFormatException e) {
			}
		}
		return values.size() < 1 ? null : values;
	}
	
	public static Double getDoubleParam(String name){
		try {
			return Double.valueOf(getParam(name));
		} catch (NumberFormatException e) {
		}
		return null;
	}
	
	public static BigDecimal getBigDecimalParam(String name){
		try {
			return new BigDecimal(getParam(name));
		} catch (NumberFormatException e) {
		}
		return null;
	}
	
	public static Date getSqlDateParam(String name){
		try {
			return Date.valueOf(getParam(name));
		} catch (Exception e) {
		}
		return null;
	}
	
	public static java.util.Date getDateParam(String name, String... format){
		String param= getParam(name);
		if(!StringUtils.isEmpty(param)){
			if(format.length > 0 && !StringUtils.isEmpty(format[0])) {
				return DateTimeUtil.getFormatDate(param, format[0]);
			}
			if(param.length()==10){
				Date date = getSqlDateParam(name);
				if(date != null){
					return new Date(date.getTime());
				}
			}else{
				return DateTimeUtil.getFormatDate(param, "yyyy-MM-dd HH:mm:ss");
			}
		}
		return null;
	}
	
	private static java.util.Date getDateParamVal(String param){
		String dat = getParam(param);
		if(!StringUtils.isEmpty(dat)){
			if(dat.length()==10){
				Date date = Date.valueOf(dat);
				if(date != null){
					return new Date(date.getTime());
				}
			}else{
				SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
				try {
					return sdf.parse(dat);
				} catch (ParseException e) {
				}
			}
		}
		return null;
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
		String groupByHql = null;
		int lastIndexOfGroupByHql = hsql.lastIndexOf("group by");
		if(lastIndexOfGroupByHql > 0) {
			groupByHql = hsql.substring(lastIndexOfGroupByHql, hsql.length());
			hsql = hsql.substring(0, lastIndexOfGroupByHql);
		}
		hsqlByFilter.append(hsql);
		Map<String, Object> filters = getFilters();
		Set<String> columns = filters.keySet();
		if(columns.size() > 0) {
			if(hsql.contains("where")) {
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
			if(!hsqlByFilter.toString().contains("group by")) {
				hsqlByFilter.append(" group by ");
			}
			Iterator<String> keyIt = groups.iterator();
			while(keyIt.hasNext()) {
				String orderKey = keyIt.next();
				hsqlByFilter.append(" ").append(orderKey);
				if(keyIt.hasNext()) {
					hsqlByFilter.append(",");
				}
			}
		}
		if(groupByHql != null) {
			hsqlByFilter.append(" ").append(groupByHql);
		}
		Map<String, String> orders = orderbys.get();
		if(orders != null && orders.size() > 0) {
			if(!hsqlByFilter.toString().contains("order by")) {
				hsqlByFilter.append(" order by ");
			}
			Iterator<String> keyIt = orders.keySet().iterator();
			while(keyIt.hasNext()) {
				String orderKey = keyIt.next();
				hsqlByFilter.append(" ").append(orderKey).append(" ").append(orders.get(orderKey));
				if(keyIt.hasNext()) {
					hsqlByFilter.append(",");
				}
			}
		}
		return hsqlByFilter.toString();
	}
}