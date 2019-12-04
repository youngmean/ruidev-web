package com.ruidev.framework.thread;

import com.ruidev.framework.exception.BizException;

/**
 * 
 *
 * @author	 	锐开科技 
 * @Copyright 	www.ruidev.com All rights reserved. 
 */
public class ThreadRunningException extends BizException {

	private static final long serialVersionUID = 1L;
	private ThreadRunningData runningData;

	public ThreadRunningException(int errorId) {
		super(errorId);
	}

	public ThreadRunningException(String msg, ThreadRunningData data) {
		super(msg);
		this.runningData = data;
	}

	public ThreadRunningData getRunningData() {
		return runningData;
	}

	public void setRunningData(ThreadRunningData runningData) {
		this.runningData = runningData;
	}
	
}
