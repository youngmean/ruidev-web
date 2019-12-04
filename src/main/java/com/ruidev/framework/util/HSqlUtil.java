package com.ruidev.framework.util;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * HSQL 工具类
 * @author 明
 *
 */
public class HSqlUtil {

	private static Pattern p = Pattern.compile("\\?");
	
	/**
	 * 将普通sql/hql 转换成 jpa-styled sql/hql
	 * 例: select * from table_one where pone = ? and ptwo like ? => select * from table_one where pone = ?0 and ptwo like ?1
	 * @param hsql
	 * @return
	 */
	public static String getJPAStyledHSql(String hsql){
		if(hsql.contains("?")){
			Matcher m = p.matcher(hsql);
			int i=0;
        	StringBuffer buffer = new StringBuffer();
        	while(m.find()){
        		m.appendReplacement(buffer, ":_" + i);
        		i++;
        	}
        	m.appendTail(buffer);
        	return buffer.toString();
		}
		return hsql;
	}
}
