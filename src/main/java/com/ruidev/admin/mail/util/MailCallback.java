package com.ruidev.admin.mail.util;

public interface MailCallback {

	public void onSuccess(Object object);
	public void onException(Exception e);
	public void onStart(Object object);
}
