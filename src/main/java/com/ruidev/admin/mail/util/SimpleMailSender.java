package com.ruidev.admin.mail.util;

import java.io.UnsupportedEncodingException;
import java.util.List;
import java.util.Properties;

import javax.activation.DataHandler;
import javax.activation.DataSource;
import javax.activation.FileDataSource;
import javax.mail.BodyPart;
import javax.mail.Message.RecipientType;
import javax.mail.MessagingException;
import javax.mail.Multipart;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.AddressException;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;
import javax.mail.internet.MimeUtility;
/**
 * 简单邮件发送器，可单发，群发。
 * 
 * @author MZULE
 * 
 */
public class SimpleMailSender {

	/**
	 * 发送邮件的props文件
	 */
	private final transient Properties props = System.getProperties();
	/**
	 * 邮件服务器登录验证
	 */
	private transient MailAuthenticator authenticator;

	/**
	 * 邮箱session
	 */
	private transient Session session;

	/**
	 * 初始化邮件发送器
	 * 
	 * @param smtpHostName
	 *            SMTP邮件服务器地址
	 * @param username
	 *            发送邮件的用户名(地址)
	 * @param password
	 *            发送邮件的密码
	 */
	public SimpleMailSender(final String smtpHostName, final String username,
			final String password) {
		init(username, password, smtpHostName);
	}

	/**
	 * 初始化邮件发送器
	 * 
	 * @param username
	 *            发送邮件的用户名(地址)，并以此解析SMTP服务器地址
	 * @param password
	 *            发送邮件的密码
	 */
	public SimpleMailSender(final String username, final String password) {
		// 通过邮箱地址解析出smtp服务器，对大多数邮箱都管用
		final String smtpHostName = "smtp." + username.split("@")[1];
		init(username, password, smtpHostName);

	}

	/**
	 * 初始化
	 * 
	 * @param username
	 *            发送邮件的用户名(地址)
	 * @param password
	 *            密码
	 * @param smtpHostName
	 *            SMTP主机地址
	 */
	private void init(String username, String password, String smtpHostName) {
		// 初始化props
		props.put("mail.smtp.auth", "true");
		props.put("mail.smtp.host", smtpHostName);
		// 验证
		authenticator = new MailAuthenticator(username, password);
		// 创建session
		session = Session.getInstance(props, authenticator);
	}

	/**
	 * 发送邮件
	 * 
	 * @param recipient
	 *            收件人邮箱地址
	 * @param subject
	 *            邮件主题
	 * @param content
	 *            邮件内容
	 * @throws AddressException
	 * @throws MessagingException
	 */
	public void send(String recipient, String subject, Object content)
			throws AddressException, MessagingException {
		// 创建mime类型邮件
		final MimeMessage message = new MimeMessage(session);
		message.setFrom(new InternetAddress(authenticator.getUsername()));
		// 设置发信人
//		try {
//			message.setFrom(new InternetAddress(authenticator.getUsername(), "HSPCNMarketing@Honeywell.com"));
//		} catch (UnsupportedEncodingException e) {
//			e.printStackTrace();
//		}
		// 设置收件人
		message.setRecipient(RecipientType.TO, new InternetAddress(recipient));
		// 设置主题
		message.setSubject(subject, "gb2312");
		// 设置邮件内容
		message.setContent(content.toString(), "text/html;charset=utf-8");
		// 发送
		Transport.send(message);
	}
	
	/**
	 * 发送邮件
	 * 
	 * @param recipient
	 *            收件人邮箱地址
	 * @param subject
	 *            邮件主题
	 * @param content
	 *            邮件内容
	 * @throws AddressException
	 * @throws MessagingException
	 */
	public void sendAndCc(String recipient, List<String> cc, String subject, Object content)
			throws AddressException, MessagingException {
		// 创建mime类型邮件
		final MimeMessage message = new MimeMessage(session);
		// 设置发信人
		//message.setFrom(new InternetAddress("HSPCNMarketing@Honeywell.com"));//authenticator.getUsername()));
		// 设置收件人
			message.setFrom(new InternetAddress(authenticator.getUsername()));
		//	message.addFrom(new InternetAddress[]{new InternetAddress("HSPCNMarketing@Honeywell.com")});
		// 设置收件人
		message.setRecipient(RecipientType.TO, new InternetAddress(recipient));
		final int num = cc.size();
		if(num > 0){
			InternetAddress[] addresses = new InternetAddress[num];
			for (int i = 0; i < num; i++) {
				String mailAddr = cc.get(i);
				addresses[i] = new InternetAddress(mailAddr);
			}
			// 设置抄送人
			message.setRecipients(RecipientType.CC, addresses);
		}
		// 设置主题
		message.setSubject(subject, "gb2312");
		// 设置邮件内容
		message.setContent(content.toString(), "text/html;charset=utf-8");
		// 发送
		Transport.send(message);
	}

	/**
	 * 群发邮件
	 * 
	 * @param recipients
	 *            收件人们
	 * @param subject
	 *            主题
	 * @param content
	 *            内容
	 * @throws AddressException
	 * @throws MessagingException
	 */
	public void send(List<String> recipients, String subject, Object content)
			throws AddressException, MessagingException {
		// 创建mime类型邮件
		final MimeMessage message = new MimeMessage(session);
		// 设置发信人
		message.setFrom(new InternetAddress(authenticator.getUsername()));
		// 设置收件人们
		final int num = recipients.size();
		InternetAddress[] addresses = new InternetAddress[num];
		for (int i = 0; i < num; i++) {
			addresses[i] = new InternetAddress(recipients.get(i));
		}
		message.setRecipients(RecipientType.TO, addresses);
		// 设置主题
		message.setSubject(subject, "gb2312");
		// 设置邮件内容
		message.setContent(content.toString(), "text/html;charset=utf-8");
		// 发送
		Transport.send(message);
	}
	/**
	 * 发送邮件
	 * 
	 * @param recipient
	 *            收件人邮箱地址
	 * @param subject
	 *            邮件主题
	 * @param content
	 *            邮件内容
	 * @throws AddressException
	 * @throws MessagingException
	 */
	public void sendAttachmentsAndCc(String recipient, List<String> cc, String subject, Object content, String affix, String affixName)
			throws AddressException, MessagingException {
		// 创建mime类型邮件
		final MimeMessage message = new MimeMessage(session);
		message.setFrom(new InternetAddress(authenticator.getUsername()));
		message.setRecipient(RecipientType.TO, new InternetAddress(recipient));
		final int num = cc != null ? cc.size() : 0;
		if(num > 0){
			InternetAddress[] addresses = new InternetAddress[num];
			for (int i = 0; i < num; i++) {
				String mailAddr = cc.get(i);
				addresses[i] = new InternetAddress(mailAddr);
			}
			// 设置抄送人
			message.setRecipients(RecipientType.CC, addresses);
		}
		//message.setRecipient(RecipientType.CC, new InternetAddress(cc));
		// 设置主题
		message.setSubject(subject, "gb2312");
		// 设置邮件内容
		message.setContent(content.toString(), "text/html;charset=utf-8");
		Multipart multipart = new MimeMultipart();
		BodyPart contentPart = new MimeBodyPart();
		//contentPart.setText(content.toString());
		contentPart.setContent(content.toString(), "text/html;charset=utf-8");
		multipart.addBodyPart(contentPart);
		BodyPart messageBodyPart = new MimeBodyPart();
		DataSource source = new FileDataSource(affix);
		messageBodyPart.setDataHandler(new DataHandler(source));
		//sun.misc.BASE64Encoder enc = new sun.misc.BASE64Encoder();
		try {
			messageBodyPart.setFileName(MimeUtility.encodeText(affixName));
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
		//messageBodyPart.setFileName("=?GBK?B?"+enc.encode(affixName.getBytes())+"?=");
		multipart.addBodyPart(messageBodyPart);
		//将multipart对象放到message中
        message.setContent(multipart);
        //保存邮件
        message.saveChanges();
		
		// 设置抄送人
		// 发送
		Transport.send(message);
	}
}