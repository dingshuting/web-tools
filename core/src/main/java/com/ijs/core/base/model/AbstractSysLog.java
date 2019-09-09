package com.ijs.core.base.model;

import static javax.persistence.GenerationType.IDENTITY;

import java.sql.Timestamp;

import javax.persistence.Column;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.MappedSuperclass;

/**
 * AbstractSysLog entity provides the base persistence definition of the SysLog
 * entity. @author MyEclipse Persistence Tools
 */
@MappedSuperclass
public abstract class AbstractSysLog extends BaseModel  implements java.io.Serializable {

	// Fields

	/**
	 * 
	 */
	private static final long serialVersionUID = 6547976220668253839L;
	private Long id;
	private Timestamp logTime;
	private String functionId;
	private Integer types;
	private String operator;
	private String title;
	private String content;
	private String requestUrl;
	// Constructors

	/** default constructor */
	public AbstractSysLog() {
	}

	/** minimal constructor */
	public AbstractSysLog(Timestamp logTime) {
		this.logTime = logTime;
	}

	/** full constructor */
	public AbstractSysLog(Timestamp logTime, String functionId, Integer types,
			String operator, String title, String content) {
		this.logTime = logTime;
		this.functionId = functionId;
		this.types = types;
		this.operator = operator;
		this.title = title;
		this.content = content;
	}

	// Property accessors
	@Id
	@GeneratedValue(strategy = IDENTITY)
	@Column(name = "ID", unique = true, nullable = false)
	public Long getId() {
		return this.id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	@Column(name = "Log_Time", nullable = false, length = 19)
	public Timestamp getLogTime() {
		return this.logTime;
	}

	public void setLogTime(Timestamp logTime) {
		this.logTime = logTime;
	}

	@Column(name = "Function_Id", length = 10)
	public String getFunctionId() {
		return this.functionId;
	}

	public void setFunctionId(String functionId) {
		this.functionId = functionId;
	}

	@Column(name = "Types")
	public Integer getTypes() {
		return this.types;
	}

	public void setTypes(Integer types) {
		this.types = types;
	}

	@Column(name = "Operator", length = 256)
	public String getOperator() {
		return this.operator;
	}

	public void setOperator(String operator) {
		this.operator = operator;
	}

	@Column(name = "Title", length = 256)
	public String getTitle() {
		return this.title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	@Column(name = "Content", length = 1000)
	public String getContent() {
		return this.content;
	}

	public void setContent(String content) {
		this.content = content;
	}
	@Column(name = "request_url")
	public String getRequestUrl() {
		return requestUrl;
	}

	public void setRequestUrl(String requestUrl) {
		this.requestUrl = requestUrl;
	}
	

}