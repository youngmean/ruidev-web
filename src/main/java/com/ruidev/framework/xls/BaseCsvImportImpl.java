package com.ruidev.framework.xls;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import com.ruidev.framework.exception.BizException;

public abstract class BaseCsvImportImpl {

	
	protected Map<String, Integer> indexes;
	private static Map<String, SimpleDateFormat> DATE_FORMATS;
	
	/*private static Date getFormatDate(String dateStr, String pattern) throws ParseException {
		return getDateFormat(pattern).parse(dateStr);
	}*/
	
	private static SimpleDateFormat getDateFormat(String pattern) {
		if(DATE_FORMATS == null) {
			DATE_FORMATS = new HashMap<String, SimpleDateFormat>();
		}
		SimpleDateFormat format = DATE_FORMATS.get(pattern);
		if(format == null) {
			format = new SimpleDateFormat(pattern, Locale.ENGLISH);
			DATE_FORMATS.put(pattern, format);
		}
		return format;
	}
	
	public Map<String, Integer> getIndexes() {
		return indexes;
	}
	public void setIndexes(Map<String, Integer> indexes) {
		this.indexes = indexes;
	}
	
	public String[][] mapProperties(){
		return null;
	}
	protected <T> void onStartRowData(String[] data, int rowIndex, List<T> list){
		
	}
	protected <T> void onEndReadData(File file, List<T> list){
		
	}
	
	protected <T> void onReadDataError(String[] data, int rowIndex, List<T> list){
		
	}
	
	protected abstract Object getRowData(Map<String, Integer> cellPropertiesAndIndexes, String[] data, int rowIndex) throws BizException;

	public Date getCellDateValue(String[] data, String prop, String pattern) throws BizException{
		return getCellDateValue(data, prop, pattern, null);
	}
	
	public Date getCellDateValue(String[] data, String prop, String pattern, Locale locale) throws BizException{
		Integer index = getPropIndex(prop);
		SimpleDateFormat dateFormat = null; 
		if(locale!=null)
			dateFormat = new SimpleDateFormat(pattern, locale);
		else
			dateFormat = getDateFormat(pattern);
		try {
			String val = data[index];
			return dateFormat.parse(val.trim());
		} catch (Exception e) {
		}
		return null;
	}
	
	protected Integer getPropIndex(String prop) throws BizException {
		if(indexes == null){
			return null;
		}
		String _prop = prop.replaceAll("[\\s\\u3000]+", "");
		Integer index = indexes.get(_prop);
		if(index == null){
			throw new BizException("属性为" + prop + "的列不存在");
		}
		return index;
	}
	
	public Long getCellLongValue(String[] data, String prop) throws BizException{
		Integer index = getPropIndex(prop);
		try {
			String str = data[index].trim();
			if(str.endsWith("-")){
				str = "-"+str.substring(0, str.length()-1);
			}
			return Double.valueOf(str).longValue();
		} catch (Exception e) {
		}
		return null;
	}
	
	public Integer getCellIntValue(String[] data, String prop) throws BizException{
		Integer index = getPropIndex(prop);
		try {
			String str = data[index].trim();
			if(str.endsWith("-")){
				str = "-"+str.substring(0, str.length()-1);
			}
			return Double.valueOf(str).intValue();
		} catch (Exception e) {
		}
		return null;
	}
	
	public Double getCellDoubleValue(String[] data, String prop) throws BizException{
		Integer index = getPropIndex(prop);
		try {
			String str = data[index].trim();
			str = str.replace(",", "").replace("(", "").replace(")", "");
			if(str.endsWith("-")){
				str = "-"+str.substring(0, str.length()-1);
			}
			if(str.isEmpty())
				str = "0";
			return Double.valueOf(str);
		} catch (Exception e) {
			e.printStackTrace();

		}
		return null;
	}
	
	public String getCellStringTrimValue(String[] data, String prop) throws BizException{
		Integer index = getPropIndex(prop);
		try {
			String val = data[index];
			return val.trim();
		} catch (Exception e) {
		}
		return null;
	}
}
