package com.ruidev.framework.freemarker.directive;

import java.io.IOException;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.apache.struts2.ServletActionContext;
import org.springframework.stereotype.Service;

import com.ruidev.framework.freemarker.util.DirectiveUtils;
import com.ruidev.framework.util.ActionPermissionUtil;

import freemarker.core.Environment;
import freemarker.template.TemplateDirectiveBody;
import freemarker.template.TemplateDirectiveModel;
import freemarker.template.TemplateException;
import freemarker.template.TemplateModel;

@Service("perm")
public class PermissionDirective implements TemplateDirectiveModel {

	@SuppressWarnings({ "rawtypes", "unchecked" })
	public void execute(Environment env, Map params, TemplateModel[] vars,
			TemplateDirectiveBody body) throws TemplateException, IOException {
		try {
			String namespace = DirectiveUtils.getString("namespace", params);
			String action = DirectiveUtils.getString("action", params);
			if(StringUtils.isEmpty(namespace)){
				String url = ServletActionContext.getRequest().getServletPath();
				namespace = url.substring(0, url.lastIndexOf("/") + 1);
			}
			String _flag = DirectiveUtils.getString("reverse", params);
			boolean reverse = "true".equals(_flag);
			boolean hasP = ActionPermissionUtil.hasPermissionForRequest(namespace, action);
			if(!reverse && hasP || reverse && !hasP){
				if(body != null){
					body.render(env.getOut());
				}
			}
		} catch (Exception e) {
		}
	}

}
