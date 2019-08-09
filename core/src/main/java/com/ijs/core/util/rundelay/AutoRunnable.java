/**
 * File Name: com.officedepot.cn.runnable.AutoRunnable.java
 * Copyright(C) 2007 Office Depot China
 * Create Time: 2007-3-20 ����05:26:26
 */
package com.ijs.core.util.rundelay;

import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;

/**
 * Explain: 
 * @author Kevin Zou
 * @version v1.0.0
 * @since 1.6.0 (Sun Microsystems Inc.)
 */
public abstract class AutoRunnable extends Thread {
	protected Logger logger = LogManager.getLogger(this.getClass());
	
	protected boolean runflag = true;	
	
	public synchronized void stopthread(){
		runflag=false; 
	}

	public synchronized boolean getrunflag(){
		return runflag; 
	}
	
	public void towait() throws InterruptedException{
		AutoRunnable.sleep(1000*60*60*24-300);	
	}
	
	public void towait(long runtime) throws InterruptedException{
		AutoRunnable.sleep(runtime);
	}
	
	public void towait(long runtime,int runDay) throws InterruptedException{
		AutoRunnable.sleep(runtime);
	}

	/**
	 * @return the runflag
	 */
	public boolean isRunflag() {
		return runflag;
	}

	/**
	 * @param runflag the runflag to set
	 */
	public void setRunflag(boolean runflag) {
		this.runflag = runflag;
	}

	public abstract String getNextTimeExcuteTime();

	
}
