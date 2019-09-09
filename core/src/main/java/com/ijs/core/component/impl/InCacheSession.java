package com.ijs.core.component.impl;

import java.util.Date;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpSession;
import javax.servlet.http.HttpSessionContext;

public class InCacheSession implements HttpSession {
	private String id;
	private Map<String,Object> attribute;
	private Date createTime;
	private Date lastAccess;
	private Integer interval;
	private boolean isValidate;
	public InCacheSession(String id) {
		this.id=id;
		attribute=new HashMap<>();
		createTime=new Date();
		lastAccess=new Date();
	}
	@Override
	public long getCreationTime() {
		// TODO Auto-generated method stub
		lastAccess=new Date();
		return createTime.getTime();
	}

	@Override
	public String getId() {
		// TODO Auto-generated method stub
		return id;
	}

	@Override
	public long getLastAccessedTime() {
		// TODO Auto-generated method stub
		return lastAccess.getTime();
	}

	@Override
	public ServletContext getServletContext() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void setMaxInactiveInterval(int interval) {
		// TODO Auto-generated method stub
		this.interval=interval;
	}

	@Override
	public int getMaxInactiveInterval() {
		// TODO Auto-generated method stub
		return interval;
	}

	@Override
	public HttpSessionContext getSessionContext() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Object getAttribute(String name) {
		// TODO Auto-generated method stub
		return attribute.get(name);
	}

	@Override
	public Object getValue(String name) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Enumeration<String> getAttributeNames() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String[] getValueNames() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void setAttribute(String name, Object value) {
		// TODO Auto-generated method stub
		attribute.put(name, value);
	}

	@Override
	public void putValue(String name, Object value) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void removeAttribute(String name) {
		// TODO Auto-generated method stub
		attribute.remove(name);
	}

	@Override
	public void removeValue(String name) {
		// TODO Auto-generated method stub

	}

	@Override
	public void invalidate() {
		// TODO Auto-generated method stub
		attribute.clear();
		isValidate=false;
	}

	@Override
	public boolean isNew() {
		// TODO Auto-generated method stub
		return true;
	}

}
