package com.ruidev.admin.xzq.bo;

import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.hibernate.Hibernate;
import org.springframework.stereotype.Service;

import com.ruidev.admin.xzq.entity.Area;
import com.ruidev.admin.xzq.entity.City;
import com.ruidev.admin.xzq.entity.Province;
import com.ruidev.framework.bo.EntityBo;

@Service
public class ProvinceBo extends EntityBo<Province> {

	private static List<Province> provinces;
	
	private static Map<Long, List<Area>> areas;
	
	@Override
	public List<Province> getAll() throws Exception {
		if(provinces == null){
			areas = new HashMap<Long, List<Area>>();
			provinces = super.getAll();
			for(Province p : provinces){
				Hibernate.initialize(p.getCities());
				for(City city : p.getCities()){
					Hibernate.initialize(city.getAreas());
					areas.put(city.getId(), city.getAreas());
				}
			}
		}
		return provinces;
	}
	
	public List<Area> getAreasByCityId(Long cityId) throws Exception{
		if(areas == null){
			getAll();
		}
		return areas.get(cityId);
	}

	@Override
	public void delete(Long id) throws Exception {
		super.delete(id);
		if(provinces != null){
			Iterator<Province> it = provinces.iterator();
			while(it.hasNext()){
				if(it.next().getId().equals(id)){
					it.remove();
				}
			}
		}
	}

	@Override
	public Province save(Province object) throws Exception {
		if(provinces != null && !provinces.contains(object)){
			provinces.add(object);
		}
		super.save(object);
		return object;
	}
	
}
