/**
* @Title: LanguageAction.java
* @Package com.ruidev.framework.web.action
* @date 2011-5-17 上午11:24:21
*/
package com.ruidev.framework.web.common.action;

import java.util.Locale;

import javax.servlet.http.HttpSession;

import org.apache.commons.lang3.StringUtils;
import org.apache.struts2.ServletActionContext;
import org.apache.struts2.convention.annotation.Action;
import org.apache.struts2.convention.annotation.Namespace;
import org.apache.struts2.convention.annotation.ParentPackage;

import com.opensymphony.xwork2.ActionContext;
import com.ruidev.framework.constant.AuthConstants;

/**
 * 多语言切换
 * 
 * @author: 	锐开科技 
 * @Copyright: 	www.ruidev.com All rights reserved.
 */
@ParentPackage("ruidev-default")
@Namespace("/")
public final class LanguageAction{

    private String lang;
    private String redirectUri;

    public String getLang() {
        return lang;
    }

    public void setLang(String lang) {
        this.lang = lang;
    }
    
    public String getRedirectUri() {
		return redirectUri;
	}

	public void setRedirectUri(String redirectUri) {
		this.redirectUri = redirectUri;
	}

	@Action("lang")
    public String language() {
        Locale[] locales = Locale.getAvailableLocales();
        if (!StringUtils.isEmpty(lang)) {
            for (Locale locale : locales) {
                if (locale.toString().equals(lang)) {
                    ActionContext.getContext().setLocale(locale);
                    break;
                }
            }
        }
        HttpSession session = (HttpSession) ServletActionContext.getRequest().getSession();
        session.setAttribute(AuthConstants.USER_SESSION_LOCAL, ActionContext.getContext().getLocale());
        return "redirect";
    }
}