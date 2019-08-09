package com.ijs.core.util;

import java.lang.reflect.Field;
import java.util.Map;
/**
 * Rquest 的工具类，用于查看request的相关信息
 * @author Dustin
 *
 */
public class RequestModel {
	private String userAgent;
	private String method;
	private String requestURI;
	private String servletPath;
	private String serverName;
	private String serverPort;
	private String remoteAddr;
	private String protocol;
	private String headerNames;
	private String remoteHost;
	private Map<String,String> heads;
	public String getUserAgent() {
		return userAgent;
	}
	public void setUserAgent(String userAgent) {
		this.userAgent = userAgent;
	}
	public String getMethod() {
		return method;
	}
	public void setMethod(String method) {
		this.method = method;
	}
	public String getRequestURI() {
		return requestURI;
	}
	public void setRequestURI(String requestURI) {
		this.requestURI = requestURI;
	}
	public String getServletPath() {
		return servletPath;
	}
	public void setServletPath(String servletPath) {
		this.servletPath = servletPath;
	}
	public String getServerName() {
		return serverName;
	}
	public void setServerName(String serverName) {
		this.serverName = serverName;
	}
	public String getServerPort() {
		return serverPort;
	}
	public void setServerPort(String serverPort) {
		this.serverPort = serverPort;
	}
	public String getRemoteAddr() {
		return remoteAddr;
	}
	public void setRemoteAddr(String remoteAddr) {
		this.remoteAddr = remoteAddr;
	}
	public String getProtocol() {
		return protocol;
	}
	public void setProtocol(String protocol) {
		this.protocol = protocol;
	}
	public String getHeaderNames() {
		return headerNames;
	}
	public void setHeaderNames(String headerNames) {
		this.headerNames = headerNames;
	}
	public String getRemoteHost() {
		return remoteHost;
	}
	public void setRemoteHost(String remoteHost) {
		this.remoteHost = remoteHost;
	}
	
	public Map<String, String> getHeads() {
		return heads;
	}
	public void setHeads(Map<String, String> heads) {
		this.heads = heads;
	}
	@Override
	public String toString() {
		// TODO Auto-generated method stub
		StringBuffer sb=new StringBuffer();
		try {
			Field[] fields=this.getClass().getDeclaredFields();
			for(Field f: fields){
				if(f.getName().equals("heads")){
					sb.append(f.getName()).append(" : \n");
					for(String key: heads.keySet()){
						sb.append(key).append(" : ").append(heads.get(key)).append("\n");
					}
				}else{
					sb.append(f.getName()).append(" : ").append(f.get(this)).append("\n");
				}
				
			}
		} catch (SecurityException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IllegalArgumentException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return sb.toString();
	}
	
}
