package com.ruidev.framework.exception;

/**
 * 系统异常封装类
 */
public class SysException extends BaseException {

	private static final long serialVersionUID = 1L;

	/**
	 * 构造函数：出错信息码
	 * 
	 * @param errorId
	 *            出错信息码
	 */
	public SysException(int errorId) {
		super(errorId);
	}

	/**
	 * 构造函数：出错信息码
	 * 
	 * @param errorId
	 *            出错信息码
	 * @param arguments
	 *            替换信息
	 */
	public SysException(int errorId, Object... arguments) {
		super(errorId, arguments);
	}

	/**
	 * 默认构造函数
	 */
	public SysException(String msg) {
		super(msg);
	}
}
