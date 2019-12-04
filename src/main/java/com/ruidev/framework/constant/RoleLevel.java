package com.ruidev.framework.constant;

/**
 * 系统角色级别
 */
public enum RoleLevel implements SystemEnum {

	/**
	 * 系统管理员(超级管理员)
	 */
    ROLE_ADMIN("0", "超级管理员"),
    /**
     * 系统管理员(普通管理员)
     */
    ROLE_MANAGER("1", "普通管理员"),
    /**
     * 普通用户
     */
    ROLE_USER("2", "普通用户"),
    /**
     * 客户/客户管理员
     */
    ROLE_TENANT("3", "客户管理员"),
    /**
     * 客户用户
     */
    ROLE_TENANT_USER("4", "客户用户"),
    /**
     * 临时用户
     */
    ROLE_TEMP_USER("5", "临时用户");
	
	private String code;
	private String label;
    
    RoleLevel(String code, String label){
    	this.code = code;
    	this.label = label;
    }

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public String getLabel() {
		return label;
	}

	public void setLabel(String label) {
		this.label = label;
	}
    
	public Integer getLevel(){
		return Integer.valueOf(code);
	}
}