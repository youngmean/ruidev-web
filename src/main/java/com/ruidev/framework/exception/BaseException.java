package com.ruidev.framework.exception;

/**
 * 基本异常类
 */
public class BaseException extends Exception {

	/** serialVersionUID */
	private static final long serialVersionUID = 1L;

	/** errorId. */
	private int errorId = 500;

	/** arguments. */
	private Object[] arguments;

	/**
	 * 默认构造函数
	 */
	public BaseException() {
		super();
	}

	/**
	 * 默认构造函数
	 */
	public BaseException(String msg) {
		super(msg);
	}

	/**
	 * 构造函数：传出错信息码
	 * 
	 * @param errorId
	 *            出错信息码
	 */
	public BaseException(int errorId) {
		this.errorId = errorId;
	}

	/**
	 * 构造函数：传出错信息码
	 * 
	 * @param errorId
	 *            出错信息码
	 * @param arguments
	 *            替换信息
	 */
	public BaseException(int errorId, Object... arguments) {
		this.errorId = errorId;
		this.arguments = arguments;
	}
	
	public BaseException(String msg, int errorId) {
		super(msg);
		this.errorId = errorId;
	}

	/**
	 * @return Returns the arguments.
	 */
	public Object[] getArguments() {
		return arguments;
	}

	/**
	 * @param arguments
	 *            The arguments to set.
	 */
	public void setArguments(Object[] arguments) {
		this.arguments = arguments;
	}

	/**
	 * @return Returns the errorId.
	 */
	public int getErrorId() {
		return errorId;
	}

	/**
	 * @param errorId
	 *            The errorId to set.
	 */
	public void setErrorId(int errorId) {
		this.errorId = errorId;
	}
}
