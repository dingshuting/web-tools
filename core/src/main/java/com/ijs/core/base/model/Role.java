package com.ijs.core.base.model;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.Entity;
import javax.persistence.Table;
import javax.persistence.Transient;

import org.springframework.security.core.GrantedAuthority;


/**
 * Role entity. @author MyEclipse Persistence Tools
 */
@Entity
@Table(name = "sys_role")
/*
@SqlResultSetMapping(name="compositekey",
entities=@EntityResult(entityClass=SpaceShip.class,
    fields = {
            @FieldResult(name="name", column = "name"),
            @FieldResult(name="model", column = "model"),
            @FieldResult(name="speed", column = "speed"),
            @FieldResult(name="captain.firstname", column = "firstn"),
            @FieldResult(name="captain.lastname", column = "lastn"),
            @FieldResult(name="dimensions.length", column = "length"),
            @FieldResult(name="dimensions.width", column = "width")
            }),
columns = { @ColumnResult(name = "surface"),
            @ColumnResult(name = "volume") } )

@NamedNativeQuery(name="compositekey",
query="select name, model, speed, lname as lastn, fname as firstn, length, width, length * width as surface from SpaceShip",
resultSetMapping="compositekey")
} )*/
public class Role extends AbstractRole implements GrantedAuthority,
		java.io.Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = -7644942535317197775L;
	/** default constructor */
	private List<Role> children=new ArrayList<Role>();
	public Role() {
	}
	
	public Role(String id) {
		this.setId(id);
	}

	public Role(String i, String string) {
		// TODO Auto-generated constructor stub
		this.setId(i);
		this.setCode(string);
	}
	@Transient
	public String getAuthority() {
		return this.getCode();
	}
	

	public int hashCode() {
		return (this.getName() != null ? this.getName().hashCode() : 0);
	}

	@Transient	
	public String getKey() {		
		return this.getId()!=null?this.getId().toString():"none";
	}
	
	@Transient	
	public List<Role> getChildren() {
		return children;
	}

	public void setChildren(List<Role> children) {
		this.children = children;
	}
	
}
