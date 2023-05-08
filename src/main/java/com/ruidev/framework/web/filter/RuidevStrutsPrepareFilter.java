package com.ruidev.framework.web.filter;

import java.io.IOException;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang3.StringUtils;
import org.apache.struts2.dispatcher.filter.StrutsPrepareFilter;

import com.ruidev.framework.util.ActionPermissionUtil;

public class RuidevStrutsPrepareFilter extends StrutsPrepareFilter {

	@Override
	public void doFilter(ServletRequest req, ServletResponse res, FilterChain chain)
			throws IOException, ServletException {
		HttpServletRequest request = (HttpServletRequest)req;
		String uri = request.getRequestURI();
		if(uri.contains("?")) {
			uri = uri.substring(0, uri.indexOf("?"));
		}
		String perm = ActionPermissionUtil.getPermissionByPath(uri);
		if(!StringUtils.isEmpty(perm) && perm.contains("[with-inputstream]")) {
			InputStreamStrutsHttpServletRquestWrapper reqWrapper = new InputStreamStrutsHttpServletRquestWrapper(request);
			super.doFilter(reqWrapper, res, chain);
		}else {
			super.doFilter(request, res, chain);
		}
//        String url = request.getServletPath();
//        if ("/ueditor/controller.jsp".equals(url)) {  
//            chain.doFilter(req, res);  
//        }else{  
//            super.doFilter(req, res, chain);  
//        }  
	}

	
}
