package com.ruidev.framework.web.filter;

import java.io.IOException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * 资源过滤器(启用GZip等)
 *
 * @author: 	锐开科技 
 * @Copyright: 	www.ruidev.com All rights reserved.
 */
public class ResourceFilter implements Filter{

	Map<String, String> headers = new HashMap<String, String>();
	
	public void destroy() {
	}

	public void doFilter(ServletRequest req, ServletResponse res,
			FilterChain chain) throws IOException, ServletException {
		if(req instanceof HttpServletRequest) {
			doFilter((HttpServletRequest)req, (HttpServletResponse)res, chain);
		}else {
			chain.doFilter(req, res);
		}
	}
	
	/**
     * 判断浏览器是否支持GZIP
     * @param request
     * @return
     */
    private static boolean isGZipEncoding(HttpServletRequest request){
      boolean flag=false;
      String encoding=request.getHeader("Accept-Encoding");
      if(encoding.indexOf("gzip")!=-1){
        flag=true;
      }
      return flag;
    }

	public void doFilter(HttpServletRequest request,
			HttpServletResponse response, FilterChain chain)
			throws IOException, ServletException {
		if(isGZipEncoding(request)){
			for(Iterator<Entry<String, String>> it = headers.entrySet().iterator();it.hasNext();) {
				Entry<String, String> entry = it.next();
				response.addHeader(entry.getKey(), entry.getValue());
			}
			final String reqPath = request.getServletPath();
			if(reqPath.endsWith("js.us")){
				response.setContentType("text/javascript;charset=utf-8");
			}else if(reqPath.endsWith("css.us")){
				response.setContentType("text/css;charset=utf-8");
			}
			chain.doFilter(request, response);
			return;
		}
		chain.doFilter(request, response);
	}
	
//	public void doFilter2(HttpServletRequest request,
//			HttpServletResponse response, FilterChain chain)
//			throws IOException, ServletException {
//		HttpServletResponse resp = (HttpServletResponse) response;
//        HttpServletRequest req=(HttpServletRequest)request;
//        if(isGZipEncoding(req)){
//            Wrapper wrapper = new Wrapper(resp);
//            chain.doFilter(request, wrapper);
//            byte[] gzipData = gzip(wrapper.getResponseData());
//            resp.addHeader("Content-Encoding", "gzip");
//            resp.setContentLength(gzipData.length);
//            ServletOutputStream output = response.getOutputStream();
//            output.write(gzipData);
//            output.flush();
//        } else {
//            chain.doFilter(request, response);
//        }        
//	}
//	
//	private byte[] gzip(byte[] data) {
//        ByteArrayOutputStream byteOutput = new ByteArrayOutputStream(10240);
//        GZIPOutputStream output = null;
//        try {
//            output = new GZIPOutputStream(byteOutput);
//            output.write(data);
//        } catch (IOException e) {
//        } finally {
//            try {
//                output.close();
//            } catch (IOException e) {
//            }
//        }
//        return byteOutput.toByteArray();
//    }

	public void init(FilterConfig config) throws ServletException {
		this.headers.put("Content-Encoding", "gzip");
	}

}
