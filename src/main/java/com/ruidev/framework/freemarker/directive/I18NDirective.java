package com.ruidev.framework.freemarker.directive;

import java.io.IOException;
import java.io.Writer;
import java.util.Map;

import org.apache.struts2.ServletActionContext;
import org.apache.struts2.util.TextProviderHelper;
import org.springframework.stereotype.Component;

import com.ruidev.framework.freemarker.util.DirectiveUtils;

import freemarker.core.Environment;
import freemarker.template.TemplateDirectiveBody;
import freemarker.template.TemplateDirectiveModel;
import freemarker.template.TemplateException;
import freemarker.template.TemplateModel;

@Component("i18n")
public class I18NDirective implements TemplateDirectiveModel {

	@SuppressWarnings({ "rawtypes", "unchecked" })
	@Override
	public void execute(Environment env, Map params, TemplateModel[] models, TemplateDirectiveBody body) throws TemplateException, IOException {
		Writer writer = env.getOut();
		try {
			String name = DirectiveUtils.getString("name", params);
			String defaultMsg = name;
			if(defaultMsg.contains(".")){
				defaultMsg = defaultMsg.substring(defaultMsg.indexOf(".") + 1);
			}
			String text = TextProviderHelper.getText(name, defaultMsg, ServletActionContext.getValueStack(ServletActionContext.getRequest()));
			writer.write(text);
		} catch (Exception e) {
		}
	}

}
