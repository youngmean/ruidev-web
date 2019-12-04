package com.ruidev.framework.constant;

/**
 * 用户账号状态
 *
 * @author	 	锐开科技 
 * @Copyright 	www.ruidev.com All rights reserved. 
 */
public enum UserStatus implements SystemEnum {
	
	NORMAL("0", "正常"),
	DISABLED("1", "禁用");
	
	private String code;
	private String label;
	
	UserStatus(String code, String label) {
		this.code = code;
		this.label = label;
	}

	public String getCode() {
		return code;
	}

	public String getLabel() {
		return label;
	}

}
