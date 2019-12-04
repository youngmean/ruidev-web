package com.ruidev.admin.mail.util;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ArrayBlockingQueue;

import org.apache.commons.lang3.StringUtils;

import com.ruidev.admin.mail.bo.MailConfBo;
import com.ruidev.admin.mail.entity.MailConf;
import com.ruidev.framework.constant.BaseConstants;
import com.ruidev.framework.exception.BizException;
import com.ruidev.framework.util.CommonUtil;

/**
 * 邮件发送工具
 * 
 * @author: 	锐开科技 
 * @Copyright: 	www.ruidev.com All rights reserved.
 */
public class MailUtil {

	private static MailConfBo mailConfBo;
	private static Map<String, SimpleMailSender> MAIL_SENDERS = new HashMap<String, SimpleMailSender>();
	private static Map<String, MailConf> MAIL_CONFS = new HashMap<String, MailConf>();
	private static ArrayBlockingQueue<SimpleMail> MAIL_TASKS = new ArrayBlockingQueue<SimpleMail>(1000);
	private static boolean MAIL_STARTED = false;
	
	/**
	 * 初始化邮件服务器加载BO
	 */
	private static void initBo(){
		if(mailConfBo == null){
			mailConfBo = BaseConstants.SPRING_APPLICATION_CONTEXT.getBean(MailConfBo.class);
			try {
				List<MailConf> confs = mailConfBo.getAll();
				for(MailConf conf : confs) {
					MAIL_CONFS.put(conf.getCode(), conf);
				}
			} catch (Exception e) {
			}
		}
	}
	
	public static MailConf getMailConf(String code) {
		initBo();
		return MAIL_CONFS.get(code);
	}
	
	public static String getMailTemplate(String code) {
		MailConf conf = getMailConf(code);
		if(conf != null) {
			return conf.getMailTemplate();
		}
		return null;
	}
	
	/**
	 * 更新与CODE对应的邮件服务器配置, 配置为空时在缓存中清空对应配置
	 * @param code
	 * @param mailConf
	 */
	public static void updateMailSender(String code, MailConf mailConf) {
		if(mailConf == null) {
			MAIL_SENDERS.remove(code);
			return;
		}
		SimpleMailSender sender = new SimpleMailSender(mailConf.getSmtpHostname(), mailConf.getSmtpUsername(), mailConf.getSmtpPassword());
		MAIL_SENDERS.put(code, sender);
		MAIL_CONFS.put(code, mailConf);
	}
	
	/**
	 * 获取邮箱服务器配置(缓存)
	 * 
	 * @param code
	 * @return
	 * @throws BizException 
	 */
	private static SimpleMailSender getMailSender(String code) throws BizException {
		initBo();
		SimpleMailSender sender = MAIL_SENDERS.get(code);
		if(sender == null){
			try {
				MailConf mailConf = MAIL_CONFS.get(code);
				if(mailConf == null) {
					mailConf = mailConfBo.getFirstData("from MailConf where code = ?", code);
					if(mailConf != null) {
						MAIL_CONFS.put(code, mailConf);
					}
				}
				if(mailConf != null) {
					sender = new SimpleMailSender(mailConf.getSmtpHostname(), mailConf.getSmtpUsername(), mailConf.getSmtpPassword());
					MAIL_SENDERS.put(code, sender);
				}
			} catch (Exception e) {
				throw new BizException(CommonUtil.combineStrings("获取邮箱服务器(", code, ")配置失败: ", e.getMessage()));
			}
		}
		return sender;
	}

	/**
	 * 发送邮件
	 * @param code
	 * @param content
	 * @param subject
	 * @param to
	 * @param cc
	 * @throws Exception
	 */
	public static void send(final String code, String content, String subject, String to, List<String> cc, MailCallback callback){
		send(code, content, subject, to, cc, null, null, callback);
	}
	
	/**
	 * 发送邮件 (支持附件)
	 * @param code
	 * @param content
	 * @param subject
	 * @param to
	 * @param cc
	 * @throws Exception
	 */
	public static void send(final String code, String content, String subject, String to, List<String> cc, final String affix, final String affixName, MailCallback callback){
		SimpleMail mail = new SimpleMail();
		mail.subject = subject;
		mail.content = content;
		mail.to = to;
		mail.cc = cc;
		mail.callback = callback;
		mail.affix = affix;
		mail.affixName = affixName;
		MAIL_TASKS.add(mail);
		if(MAIL_STARTED){
			return;
		}
		new Thread(new Runnable() {
			@Override
			public void run() {
				while(true){
					try {
						SimpleMail mail = MAIL_TASKS.take();
						_send(code, mail.content, mail.subject, mail.to, mail.cc, mail.affix, mail.affixName, mail.callback);
						Thread.sleep(3000);
					} catch (Exception e) {
						e.printStackTrace();
						try {
							Thread.sleep(5000);
						} catch (InterruptedException e1) {
							MAIL_STARTED = false;
							break;
						}
					}
				}
			}
		}).start();
		MAIL_STARTED = true;
	}
	
	/**
	 * 发送邮件
	 * @param code
	 * @param content
	 * @param subject
	 * @param to
	 * @param cc
	 * @throws Exception
	 */
	private static void _send(String code, String content, String subject, String to, List<String> cc, String affix, String affixName, MailCallback callback){
		try {
			SimpleMailSender sender = getMailSender(code);
			if(sender == null){
				wrapCallback(new BizException("未配置发件邮箱服务器"), null, callback);
				return;
			}
			if(StringUtils.isEmpty(to)){
				wrapCallback(new BizException("邮箱不能为空"), null, callback);
				return;
			}
			SimpleMail mail = new SimpleMail();
			mail.content = content;
			mail.subject = subject;
			if(callback != null) {
				callback.onStart(mail);
			}
			if(cc == null || cc.size() < 1){
				if(!StringUtils.isEmpty(affix)) {
					sender.sendAttachmentsAndCc(to, null, mail.subject, mail.content, affix, affixName);
				}else {
					sender.send(to, subject, content);
				}
			}else{
				ArrayList<String> ccList = new ArrayList<String>();
				for(String ccc : cc){
					ccList.add(ccc);
				}
				Iterator<String> it = ccList.iterator();
				List<String> mails = new ArrayList<String>();
				while(it.hasNext()){
					String _mail = it.next();
					if(StringUtils.isEmpty(_mail)){
						it.remove();
						continue;
					}else if(mails.contains(_mail)){
						it.remove();
						continue;
					}else if(_mail.equals(to)){
						it.remove();
						continue;
					}
					mails.add(_mail);
				}
				if(!StringUtils.isEmpty(affix)) {
					sender.sendAttachmentsAndCc(to, ccList, mail.subject, mail.content, affix, affixName);
				}else {
					sender.sendAndCc(to, ccList, mail.subject, mail.content);
				}
			}
			wrapCallback(null, to, callback);
		} catch (Exception e) {
			wrapCallback(new BizException("Send mail failed: " + e.getMessage() + "(" + e.getClass().getName() + ")"), null, callback);
		}
	}
	
	/**
	 * 邮件发送结果回调
	 * @param e
	 * @param obj
	 * @param callback
	 */
	private static void wrapCallback(Exception e, Object obj, MailCallback callback){
		if(callback != null){
			if(e != null){
				callback.onException(e);
			}else{
				callback.onSuccess(obj);
			}
		}
	}
	
	/**
	 * 发送邮件
	 * @param code
	 * @param content
	 * @param subject
	 * @param to
	 * @param cc
	 * @throws Exception
	 */
	public static void send(String code, String content, String subject, String to, List<String> cc) {
		send(code, content, subject, to, cc, null);
	}
	
	/**
	 * 发送邮件
	 * @param code
	 * @param content
	 * @param subject
	 * @param to
	 * @throws Exception
	 */
	public static void send(String code, String content, String subject, String to) {
		send(code, content, subject, to, null, null);
	}

	/**
	 * 发送邮件
	 * @param code
	 * @param content
	 * @param subject
	 * @param to
	 * @param callback
	 */
	public static void send(String code, String content, String subject, String to, String affix, String affixName, MailCallback callback) {
		send(code, content, subject, to, null, affix, affixName, callback);
	}
	
	/**
	 * 发送邮件
	 * @param code
	 * @param content
	 * @param subject
	 * @param to
	 * @param callback
	 */
	public static void send(String code, String content, String subject, String to, MailCallback callback) {
		send(code, content, subject, to, null, callback);
	}
}
class SimpleMail{
	public String affix;
	public String affixName;
	public String subject;
	public String content;
	public String to;
	public List<String> cc;
	public MailCallback callback;
}