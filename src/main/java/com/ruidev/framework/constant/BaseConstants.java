package com.ruidev.framework.constant;

import org.springframework.web.context.WebApplicationContext;

/**
 * 系统常量定义类
 */
public class BaseConstants
{
	public static boolean DB_IS_ORACLE = false;

	/** 日期格式 ** */
	public final static String DATE_PATTERN = "yyyy-MM-dd";

	/** 日期格式 ** */
	public final static String TIME_PATTERN = "yyyy-MM-dd HH:mm:ss";

	/**
	 * 逻辑删除
	 */
	public final static String DATA_DELETED = "0";

	/**
	 * 数据有效
	 */
	public final static String DATA_UNDELETED = "1";

	/** Spring上下文对象. */
	public static WebApplicationContext SPRING_APPLICATION_CONTEXT = null;

	/** 页面工具引用名称 */
	public final static String WEB_UTILS_NAME = "beans";
	/** 页面工具引用名称 */
	public final static String CONF_UTILS_NAME = "conf";
	
    /**
     * 基础导入导出 时间格式
     */
    public static final String BASE_IMPEXP_DATE_FORMAT = "yyyy-MM-dd HH:mm:ss";
    /**
     * 缓冲区大小
     */
    public static final int BUFFER_SIZE = 16 * 1024;
}
