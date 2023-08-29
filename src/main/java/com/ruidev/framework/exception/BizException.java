package com.ruidev.framework.exception;

/**
 * 业务异常封装类
 */
public class BizException extends BaseException {

	private static final long serialVersionUID = 1L;
	
	protected String tip;

	/**
	 * 构造函数：出错信息码
	 * 
	 * @param errorId
	 *            出错信息码
	 */
	public BizException(int errorId) {
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
	public BizException(int errorId, Object... arguments) {
		super(errorId, arguments);
	}
	
	public BizException(String msg, int errorId) {
		super(msg, errorId);
	}

	public BizException(String msg) {
		super(msg);
	}

	public String getTip() {
		return tip;
	}

	public void setTip(String tip) {
		this.tip = tip;
	}
	
}
