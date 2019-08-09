package com.ijs.core.base.model;

import java.sql.Timestamp;
import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.Table;
import javax.persistence.Transient;

/**
 * OperationLog entity. @author MyEclipse Persistence Tools
 */
@Entity
@Table(name = "sys_log")
public class SysLog extends AbstractSysLog implements java.io.Serializable {

	
	/** default constructor */
	public SysLog() {
	}

	/** minimal constructor */
	public SysLog(Timestamp logTime) {
		super(logTime);
	}
	
	
	
	
}
