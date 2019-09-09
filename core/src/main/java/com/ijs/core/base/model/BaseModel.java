package com.ijs.core.base.model;

import java.io.Serializable;

import javax.persistence.Transient;

import com.ijs.core.util.QueryParameters;

public abstract class BaseModel implements Serializable{
	
	private QueryParameters qp;
	
	@Transient
	public QueryParameters getQp() {
		if(qp==null) {
			qp=new QueryParameters();
		}
		return qp;
	}

	public void setQp(QueryParameters qp) {
		this.qp = qp;
	}
	
	
	
}
