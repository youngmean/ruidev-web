package com.ruidev.framework.constant;
public class ErrorType {

	/**
	 * 登录不成功
	 */
	public static final int USER_LOGIN_ERROR = 1;
	/**
	 * 登录不成功
	 */
	public static final String USER_LOGIN_ERROR_MSG = "登录失败,用户不存在或密码错误";
	/**
	 * 密码输入错误
	 */
	public static final String USER_PWD_ERROR_MSG = "当前登录密码输入错误";
	/**
	 * 未登录
	 */
	public static final int USER_LOGIN_REQUIRED = 2;
	/**
	 * 未登录
	 */
	public static final String USER_LOGIN_REQUIRED_MSG = "会话丢失,该请求需要登录";
	/**
	 * 请求地址不存在
	 */
	public static final int ACTION_URL_INVALID = 3;
	/**
	 * 请求地址不存在
	 */
	public static final String ACTION_URL_INVALID_MSG = "非法请求,请求的地址不存在";
	/**
	 * 参数输入有误
	 */
	public static final int INVALID_INPUT = 4;
	/**
	 * 参数输入有误
	 */
	public static final String INVALID_INPUT_MSG = "参数有误,输入的参数不正确";
	/**
	 * 请求未授权
	 */
	public static final int UN_AUTHORIZED = 5;
	/**
	 * 请求未授权
	 */
	public static final String UN_AUTHORIZED_MSG = "当前请求未授权";
	/**
	 * 内部错误
	 */
	public static final int UNKNOWN_SYSTEM_ERROR = 999;
	/**
	 * 内部错误
	 */
	public static final String UNKNOWN_SYSTEM_ERROR_MSG = "内部错误";
	
}