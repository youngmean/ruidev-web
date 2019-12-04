package com.ruidev.admin.conf.bo;

import java.util.Map;

public interface IConfCtrl {
	public Map<String, String> getConfurations() throws Exception;
	public Map<String, Map<String, String>> getConfurationsAsMap() throws Exception;
}
