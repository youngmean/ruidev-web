package com.ruidev.admin.conf.bo;

import java.util.HashMap;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import com.ruidev.admin.conf.entity.Configuration;
import com.ruidev.framework.bo.EntityBo;

@Service
public class ConfBo extends EntityBo<Configuration> implements IConfCtrl{
	
	@Cacheable("systemConfiguration")
	public Map<String, String> getConfurations() throws Exception {
		Map<String, String> map = new HashMap<String, String>();
		for (Object obj : getAll()) {
			map.put(((Configuration) obj).getCode(),
					((Configuration) obj).getValue());
		}
		return map;
	}

	@Cacheable("systemConfigurationList")
	public Map<String, Map<String, String>> getConfurationsAsMap() throws Exception {
		Map<String, Map<String, String>> map = new HashMap<String, Map<String, String>>();
		for (Configuration conf : getAll()) {
			Map<String, String> list = map.get(conf.getCode());
			if(list == null){
				list = new HashMap<String, String>();
			}
			list.put(conf.getValue(), conf.getDescription());
			map.put(conf.getCode(), list);
		}
		return map;
	}

	@CacheEvict(value = {"systemConfiguration", "systemConfigurationList"}, allEntries = true)
	public Configuration save(Configuration conf) throws Exception {
		if("WEB_RESOURCE_PATH".equals(conf.getCode())){
			//将http://www.example.com/ 中最后的"/"去掉
			if(!StringUtils.isEmpty(conf.getValue())){
				conf.setValue(conf.getValue().replace("\\", "/"));
				if(conf.getValue().matches(".*\\/$")){
					conf.setValue(conf.getValue().replaceAll("([^\\/]+)(\\/)+$", "$1"));
				}
			}
		}
		return super.save(conf);
	}

}
