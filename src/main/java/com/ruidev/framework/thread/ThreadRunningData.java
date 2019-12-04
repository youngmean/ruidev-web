package com.ruidev.framework.thread;

import java.util.Date;

/**
 * 线程运行数据
 *
 * @author 锐开科技
 * @Copyright www.ruidev.com All rights reserved.
 */
public class ThreadRunningData {

	private Object obj;
	private boolean running = false;
	private Date startTime;// 开始时间
	private Date endTime;// 结束时间
	private boolean forceStop = false;// 强制停止运行
	private int total;// (预计)总数
	private int totalReal;// (实际)总数
	private int current;// 当前运行至数量
	private boolean autoClear = false;

	public Object getObj() {
		return obj;
	}

	public void setObj(Object obj) {
		this.obj = obj;
	}

	public boolean isRunning() {
		return running;
	}

	public void setRunning(boolean running) {
		this.running = running;
	}

	public Date getStartTime() {
		return startTime;
	}

	public void setStartTime(Date startTime) {
		this.startTime = startTime;
	}

	public Date getEndTime() {
		return endTime;
	}

	public void setEndTime(Date endTime) {
		this.endTime = endTime;
	}

	public boolean isForceStop() {
		return forceStop;
	}

	public void setForceStop(boolean forceStop) {
		this.forceStop = forceStop;
	}

	public int getTotal() {
		return total;
	}

	public void setTotal(int total) {
		this.total = total;
	}

	public int getTotalReal() {
		return totalReal;
	}

	public void setTotalReal(int totalReal) {
		this.totalReal = totalReal;
	}

	public int getCurrent() {
		return current;
	}

	public void setCurrent(int current) {
		this.current = current;
	}

	public boolean isAutoClear() {
		return autoClear;
	}

	public void setAutoClear(boolean autoClear) {
		this.autoClear = autoClear;
	}

}
