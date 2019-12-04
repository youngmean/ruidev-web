package com.ruidev.framework.web.filter;

import java.io.IOException;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;

import org.apache.struts2.dispatcher.filter.StrutsPrepareFilter;

public class RuidevStrutsPrepareFilter extends StrutsPrepareFilter {

	@Override
	public void doFilter(ServletRequest req, ServletResponse res, FilterChain chain)
			throws IOException, ServletException {
		HttpServletRequest request = (HttpServletRequest)req;
        String url = request.getServletPath();  
        if ("/ueditor/controller.jsp".equals(url)) {  
            chain.doFilter(req, res);  
        }else{  
            super.doFilter(req, res, chain);  
        }  
	}

	
}
