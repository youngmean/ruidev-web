package com.ruidev.framework.web.servlet;

import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.context.support.WebApplicationContextUtils;

import com.ruidev.admin.conf.util.ConfigurationUtil;
//import com.ruidev.emm.system.conf.ctrl.IConfCtrl;
//import com.ruidev.emm.system.conf.util.ConfigurationUtil;
import com.ruidev.framework.constant.BaseConstants;
import com.ruidev.framework.freemarker.manager.RuiDevFreemarkerManager;
import com.ruidev.framework.util.ActionPermissionUtil;
import com.ruidev.framework.util.BeanUtils4Web;

/**
 * 系统启动初始化 servlet,用于将数据放入cache等目的
 *
 * @author: 	锐开科技 
 * @Copyright: 	www.ruidev.com All rights reserved.
 */
public class StartupServlet extends HttpServlet {

	private static final long serialVersionUID = -2632707551778299265L;

	private static final Logger log = LogManager.getLogger(StartupServlet.class);

	/**
	 * 初始化时调用
	 */
	@Override
	public void init() throws ServletException {
		try {
			log.info("Initializing Ruidev Framework Lite...");
			// 获得WebApplicationContext对象，以便获得对应业务Bean
			ServletContext sc = getServletConfig().getServletContext();
			WebApplicationContext w = WebApplicationContextUtils.getWebApplicationContext(sc);

			// 初始化工具类.
			if (w.containsBean("webUtils")) {
				BeanUtils4Web utils = (BeanUtils4Web) w.getBean("webUtils");
				utils.initUtils(w);
				sc.setAttribute(BaseConstants.WEB_UTILS_NAME, utils);
			}
			// 初始化系统参数配置工具
			ConfigurationUtil confInstance = ConfigurationUtil.getInstance();
			confInstance.init();
			sc.setAttribute(BaseConstants.CONF_UTILS_NAME, confInstance);
			// 初始化spring静态内容
			BaseConstants.SPRING_APPLICATION_CONTEXT = w;
			// 初始化能用权限配置
            ActionPermissionUtil.initPermissionDefinitions();

			BaseConstants.DB_IS_ORACLE = false;
			RuiDevFreemarkerManager.initConfiguration(sc);
			log.info("... initialized Ruidev Framework Lite successfully");
		} catch (Throwable e) {
			e.printStackTrace();
			log.error("**************Failed to start application, system exits*****************", e);
			System.exit(0);
		}
	}

}