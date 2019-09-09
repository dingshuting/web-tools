package com.ijs.mongo.model;

public class CacheData<T> {
	private String key;
	private T val;
	
	public CacheData(String key, T val) {
		super();
		this.key = key;
		this.val = val;
	}
	public String getKey() {
		return key;
	}
	public void setKey(String key) {
		this.key = key;
	}
	public Object getVal() {
		return val;
	}
	public void setVal(T val) {
		this.val = val;
	}
	
	
}
