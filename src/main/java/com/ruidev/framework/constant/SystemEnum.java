package com.ruidev.framework.constant;

/**
 * 系统常量(带标签)<br>
 * 使用方法:<br>
 * 		1.新建枚举实现此接口<br>
 * 		2.添加代码块或其他方式,将枚举值放入缓存,如:<br>
 * 		static {<br>
 *	    	ConfigurationUtil.addEnum("{SystemEnumName}", {SystemEnumClass}.values());<br>
 *	    }<br>
 *		3.注意:若使用静态代码块,则需保证在使用前至少引用一次该常量类,使得static代码块得以执行
 */
public interface SystemEnum {
	
	/**
	 * @return 常量编码
	 */
	public String getCode();
	/**
	 * @return 常量标签
	 */
	public String getLabel();
}
