package com.ruidev.framework.util;

import java.util.Date;

import javax.servlet.http.HttpSession;

import org.apache.struts2.ServletActionContext;

import com.ruidev.framework.constant.AuthConstants;
import com.ruidev.framework.user.IUser;

/**
 * web session 操作
 *
 * @author	 	锐开科技 
 * @Copyright 	www.ruidev.com All rights reserved.
 */
public class WebSessionSubjectUtil {
	
	/**
	 * 设置属性(带时效)
	 * @param attr
	 * @param obj
	 * @param minutes
	 */
	public static void setAttr(String attr, Object obj, int minutes) {
		setAttr(attr, obj);
		setAttr(CommonUtil.combineStrings(attr, "_time"), DateTimeUtil.getCurrentTime());
		setAttr(CommonUtil.combineStrings(attr, "_minutes"), minutes);
	}
	
	@SuppressWarnings("unchecked")
	public static <T extends IUser> T getLoginUser() {
		IUser info = (IUser) getAttr(AuthConstants.LOGIN_USER);
		if(info != null) {
			return (T) info;
		}
		return null;
	}

	/**
	 * set session attribute
	 * 
	 * @param attribute
	 * @param object
	 */
	public static void setAttr(String attribute, Object object) {
		HttpSession session = ServletActionContext.getRequest().getSession();
		session.setAttribute(attribute, object);
	}
	
	/**
	 * set session attribute
	 * 
	 * @param attribute
	 * @param object
	 */
	public static void setAttrIfSession(String attribute, Object object) {
		HttpSession session = ServletActionContext.getRequest().getSession(false);
		if(session != null) {
			session.setAttribute(attribute, object);
		}
	}

	/**
	 * get session attribute
	 * @param attribute
	 * @return
	 */
	public static Object getAttr(String attribute) {
		HttpSession session = ServletActionContext.getRequest().getSession(false);
		if(session != null) {
			Object obj = session.getAttribute(attribute);
			String timeKey = CommonUtil.combineStrings(attribute, "_time");
			Date time = (Date) session.getAttribute(timeKey);
			if(time != null) {
				String minutesKey = CommonUtil.combineStrings(attribute, "_minutes");
				Integer minutes = (Integer) session.getAttribute(minutesKey);
				if(DateTimeUtil.getMinutesBetweenDates(time, DateTimeUtil.getCurrentTime()) > minutes) {
					session.removeAttribute(minutesKey);
					session.removeAttribute(timeKey);
					session.removeAttribute(attribute);
					return null;
				}
			}
			return obj;
		}
		return null;
	}
	
	/**
	 * 删除session相应存储
	 * @param attr
	 */
	public static void removeAttr(String attr) {
		HttpSession session = ServletActionContext.getRequest().getSession(false);
		if(session != null) {
			session.removeAttribute(attr);
		}
	}

}
