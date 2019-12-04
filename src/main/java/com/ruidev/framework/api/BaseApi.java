package com.ruidev.framework.api;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 * 框架相关API的基类
 *
 * @author	 	锐开科技 
 * @Copyright 	www.ruidev.com All rights reserved. 
 */
public abstract class BaseApi {
	
	protected Logger log = LogManager.getLogger(this.getClass());
}
