package com.ruidev.admin.mail.bo;

import org.springframework.stereotype.Service;

import com.ruidev.admin.mail.entity.MailConf;
import com.ruidev.admin.mail.util.MailUtil;
import com.ruidev.framework.bo.EntityBo;

@Service
public class MailConfBo extends EntityBo<MailConf> {

	@Override
	public MailConf save(MailConf mailConf) throws Exception {
		mailConf = super.save(mailConf);
		MailUtil.updateMailSender(mailConf.getCode(), mailConf);
		return mailConf;
	}

	@Override
	public void delete(Long id) throws Exception {
		MailConf mailConf = super.get(id);
		super.delete(id);
		MailUtil.updateMailSender(mailConf.getCode(), null);
	}
	
}