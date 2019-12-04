package com.ruidev.framework.freemarker.manager;

import java.util.HashMap;
import java.util.Map;

import javax.servlet.ServletContext;

import org.apache.struts2.ServletActionContext;
import org.apache.struts2.views.freemarker.FreemarkerManager;
import org.springframework.web.context.support.WebApplicationContextUtils;

import com.ruidev.framework.constant.BaseConstants;

import freemarker.core.AliasTemplateNumberFormatFactory;
import freemarker.core.TemplateNumberFormatFactory;
import freemarker.template.Configuration;
import freemarker.template.TemplateDirectiveModel;
import freemarker.template.TemplateException;

public class RuiDevFreemarkerManager extends FreemarkerManager {

    @Override
    protected Configuration createConfiguration(ServletContext servletContext)
            throws TemplateException {
        Configuration configuration = super.createConfiguration(servletContext);
        if(BaseConstants.SPRING_APPLICATION_CONTEXT == null){
        	BaseConstants.SPRING_APPLICATION_CONTEXT = WebApplicationContextUtils.getWebApplicationContext(servletContext);
        }
        Map<String, TemplateDirectiveModel> directives = BaseConstants.SPRING_APPLICATION_CONTEXT.getBeansOfType(TemplateDirectiveModel.class);
        for(String key : directives.keySet()){
        	configuration.setSharedVariable(key, directives.get(key));
        }
        
        Map<String, TemplateNumberFormatFactory> customNumberFormats = new HashMap<String, TemplateNumberFormatFactory>();
        customNumberFormats.put("money", new AliasTemplateNumberFormatFactory(",##0.##"));
		customNumberFormats.put("fixedMoney", new AliasTemplateNumberFormatFactory(
				",##0.00"));
		customNumberFormats.put("rate", new AliasTemplateNumberFormatFactory(
				",##0.####"));
        configuration.setCustomNumberFormats(customNumberFormats);
        return configuration;
    }    
    private static Configuration confInstance;
    
    public static Configuration getConfiguration() throws Exception{
    	if(confInstance == null){
    		confInstance = new RuiDevFreemarkerManager().createConfiguration(ServletActionContext.getServletContext());
    	}
    	return confInstance;
    }
    
    public static Configuration initConfiguration(ServletContext ctx) throws Exception{
    	if(confInstance == null){
    		confInstance = new RuiDevFreemarkerManager().createConfiguration(ctx);
    	}
    	return confInstance;
    }
}
