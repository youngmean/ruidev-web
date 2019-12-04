package com.ruidev.admin.mail.entity;

import javax.persistence.Entity;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;

import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;

import com.ruidev.framework.entity.CrudEntity;

/**
 * 系统邮件服务器配置
 * 
 * @author: 	锐开科技 
 * @Copyright: 	www.ruidev.com All rights reserved.
 */
@Entity
@DynamicInsert
@DynamicUpdate
@Table(name = "ruidev_system_mail_conf")
public class MailConf extends CrudEntity {

	private static final long serialVersionUID = 1L;

	private String name;// 配置名称
	@NotNull
	private String code;// 配置编码
	private String description;// 配置描述
	private String smtpUsername;// smtp用户名
	private String smtpHostname;// smtp主机名
	private String smtpPassword;// smtp登录密码
	private String mailTemplate;// 邮件模板

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getSmtpUsername() {
		return smtpUsername;
	}

	public void setSmtpUsername(String smtpUsername) {
		this.smtpUsername = smtpUsername;
	}

	public String getSmtpHostname() {
		return smtpHostname;
	}

	public void setSmtpHostname(String smtpHostname) {
		this.smtpHostname = smtpHostname;
	}

	public String getSmtpPassword() {
		return smtpPassword;
	}

	public void setSmtpPassword(String smtpPassword) {
		this.smtpPassword = smtpPassword;
	}

	public String getMailTemplate() {
		return mailTemplate;
	}

	public void setMailTemplate(String mailTemplate) {
		this.mailTemplate = mailTemplate;
	}

}
