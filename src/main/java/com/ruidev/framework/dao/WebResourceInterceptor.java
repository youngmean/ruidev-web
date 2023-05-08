package com.ruidev.framework.dao;

import java.io.Serializable;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.hibernate.EmptyInterceptor;
import org.hibernate.type.Type;

import com.ruidev.framework.annotations.WebResourceEntity;
import com.ruidev.framework.util.RequestUtil;

public class WebResourceInterceptor extends EmptyInterceptor {

	private static final long serialVersionUID = 1L;
	
	protected final Logger log = LogManager.getLogger(getClass());
	
	@Override
	public void onDelete(Object entity, Serializable id, Object[] state, String[] propertyNames, Type[] types) {
		if (entity.getClass().isAnnotationPresent(WebResourceEntity.class)) {
			String[] fields = entity.getClass().getAnnotation(WebResourceEntity.class).fields();
			for (String field : fields) {
				int index = 0;
				for (String property : propertyNames) {
					if (field.equals(property)) {
						String url = (String) state[index];
						try {
							if (url != null) {
								RequestUtil.deleteWebResource(url);
							}
						} catch (Exception e) {
						}
						break;
					}
					index++;
				}
			}
		}
		super.onDelete(entity, id, state, propertyNames, types);
		log.info("\tDelete " + entity.getClass().getSimpleName() + ", id = " + id);
	}

}
