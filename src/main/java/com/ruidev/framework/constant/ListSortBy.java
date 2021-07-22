package com.ruidev.framework.constant;

import java.util.ArrayList;
import java.util.List;

import com.ruidev.admin.conf.util.ConfigurationUtil;

public enum ListSortBy implements SystemEnum {
	
	ASC("0", "asc", "升序"),
	DESC("1", "desc", "降序");

	String code;
	String label;
	String name;
	String field;
	
	ListSortBy(String code, String name, String label){
		this.code = code;
		this.name = name;
		this.label = label;
	}
	
	@Override
	public String getCode() {
		return code;
	}

	@Override
	public String getLabel() {
		return label;
	}

	public String getName() {
		return name;
	}

	public String getField() {
		return field;
	}

	public void setField(String field) {
		this.field = field;
	}

	public ListSortBy ofField(String field) {
		this.field = field;
		return this;
	}
	
	public List<ListSortBy> ofFields(String... fields) {
		List<ListSortBy> bys = new ArrayList<ListSortBy>();
		for(String field : fields) {
			ListSortBy by = ConfigurationUtil.getInstance().getEnum(this.getClass(), this.code);
			by.setField(field);
			bys.add(by);
		}
		return bys;
	}
}
