package com.ruidev.framework.thread;

import java.util.HashMap;
import java.util.Map;

import com.ruidev.framework.util.DateTimeUtil;

/**
 * 单线程运行数据监控
 *
 * @author	 	锐开科技 
 * @Copyright 	www.ruidev.com All rights reserved. 
 */
public class SingleThreadRunning {

	private static final Map<Object, ThreadRunningData> THREAD_DATA = new HashMap<Object, ThreadRunningData>();
	
	public synchronized static ThreadRunningData start(Object object) throws ThreadRunningException {
		return start(object, false);
	}
	
	public synchronized static ThreadRunningData start(Object object, boolean autoClear) throws ThreadRunningException {
		ThreadRunningData data = get(object);
		if(data != null && data.isRunning()) {
			throw new ThreadRunningException("thread is running", data);
		}
		if(data == null) {
			data = new ThreadRunningData();
			data.setObj(object);
		}else {
			reset(data);
		}
		data.setStartTime(DateTimeUtil.getCurrentTime());
		data.setEndTime(null);
		data.setRunning(true);
		data.setAutoClear(autoClear);
		THREAD_DATA.put(object, data);
		return data;
	}
	
	public  static ThreadRunningData setTotal(Object object, int total) {
		ThreadRunningData data = get(object);
		if(data != null && data.isRunning()) {
			data.setTotal(total);
			data.setTotalReal(total);
		}
		return data;
	}
	
	public  static ThreadRunningData setTotalReal(Object object, int totalReal) {
		ThreadRunningData data = get(object);
		if(data != null && data.isRunning()) {
			data.setTotalReal(totalReal);
		}
		return data;
	}
	
	public  static ThreadRunningData setCurrent(Object object, int current) {
		ThreadRunningData data = get(object);
		if(data != null && data.isRunning()) {
			data.setCurrent(current);
		}
		return data;
	}
	
	private static void reset(ThreadRunningData data) {
		data.setRunning(false);
		data.setForceStop(false);
		data.setCurrent(0);
		data.setTotal(0);
		data.setTotalReal(0);
	}
	
	public static ThreadRunningData stop(Object object) throws ThreadRunningException {
		ThreadRunningData data = get(object);
		if(data != null) {
			if(data.isRunning()) {
				data.setRunning(false);
				data.setForceStop(false);
				data.setEndTime(DateTimeUtil.getCurrentTime());
			}
			if(data.isAutoClear()) {
				THREAD_DATA.remove(object);
			}
		}
		return data;
	}
	
	public static ThreadRunningData forceStop(Object object) throws ThreadRunningException {
		ThreadRunningData data = get(object);
		if(data != null && data.isRunning()) {
			data.setForceStop(true);
		}
		return data;
	}
	
	public static ThreadRunningData get(Object object) {
		return THREAD_DATA.get(object);
	}
}
