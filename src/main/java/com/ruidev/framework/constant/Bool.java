package com.ruidev.framework.constant;

/**
 * 布尔型标志位
 *
 * @author	 	锐开科技 
 * @Copyright 	www.ruidev.com All rights reserved. 
 */
public enum Bool implements SystemEnum {
	
	TRUE("Y", "是"),
	FALAE("N", "否");
	
	private String code;
	private String label;
	
	Bool(String code, String label) {
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
