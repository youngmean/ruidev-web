package com.ruidev.admin.xzq.vo;

import java.io.Serializable;
import java.util.List;

public class DistrictData implements Serializable {

	private static final long serialVersionUID = 6724289468745613953L;
	
	private String label;
	private String value;
	private List<DistrictData> children;

	public String getLabel() {
		return label;
	}

	public void setLabel(String label) {
		this.label = label;
	}

	public String getValue() {
		return value;
	}

	public void setValue(String value) {
		this.value = value;
	}

	public List<DistrictData> getChildren() {
		return children;
	}

	public void setChildren(List<DistrictData> children) {
		this.children = children;
	}

}
