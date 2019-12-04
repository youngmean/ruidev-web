package com.ruidev.framework.util;

import java.io.File;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;

import javax.servlet.http.HttpServletRequest;

import org.apache.struts2.ServletActionContext;

import com.ruidev.admin.conf.util.ConfigurationUtil;

public class RequestUtil {
    
    private static Map<String, Date> submitMap = new HashMap<String, Date>();
    public static long VALID_DURATION_BETWEEN_TWO_SUBMITS = 5;

    /**
     * 判断是否提交过于频繁
     * @param request
     * @return
     */
    public static boolean isValidSubmit(HttpServletRequest request){
        String ip = CommonUtil.getRealIp(request);
        Date lastSubmitDate = submitMap.get(ip);
        Date now = DateTimeUtil.getCurrentTime();
        if(lastSubmitDate != null && DateTimeUtil.getSecondsBetweenDates(lastSubmitDate, now) < VALID_DURATION_BETWEEN_TWO_SUBMITS
                && LoginContext.getCurrentLoginUser() == null){
            return false;
        }
        clearRequests();
        submitMap.put(ip, now);
        return true;
    }
    
    /**
     * 清空提交缓存
     */
    public static void clearRequests(){
        Iterator<Entry<String, Date>> it = submitMap.entrySet().iterator();
        while(it.hasNext()){
            Entry<String, Date> entry = it.next();
            if(DateTimeUtil.getSecondsBetweenDates(entry.getValue(), DateTimeUtil.getCurrentDate()) > VALID_DURATION_BETWEEN_TWO_SUBMITS){
                it.remove();
            }
        }
    }
    
    /**
     * 判断终端请求是否来自android 或 ios终端
     * @param request
     * @return
     */
    public static boolean isMobileRequest(HttpServletRequest request){
    	String userAgent = request.getHeader("user-agent");
    	//TODO 添加 windows phone等终端的判断
    	return userAgent != null && (userAgent.toLowerCase().contains("ios") || userAgent.toLowerCase().contains("android"));
    }
    
    /**
     * 判断终端请求是否来自 windows nt
     * @param request
     * @return
     */
    public static boolean isPCRequest(HttpServletRequest request){
    	String userAgent = request.getHeader("user-agent");
    	//TODO 添加 linux mac等终端的判断
    	return userAgent != null && userAgent.toLowerCase().contains("windows nt");
    }
    
    /**
	 * 删除本地文件
	 * @param source
	 */
	public static void deleteWebResource(String source){
		 if(source==null||"".equals(source)){
			return;
		 }
		 String savePath = ServletActionContext.getServletContext().getRealPath("");
         String webPath = ConfigurationUtil.getConfigurationValue("WEB_RESOURCE_PATH").replaceAll("\\\\/$", "");
         String realPath = source.replaceAll(webPath, "").replace('/', '\\');
         String filePath = savePath + realPath;
         File file=new File(filePath);
         if(file.exists()){
             file.delete();
         }
	}
}
