package com.ijs.core.common.filter;


import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletRequestWrapper;
/**
 * 
 * @author Dustin
 * 用于防止Xss攻击的过滤类
 */
public class XssHttpServletRequestWrapper extends HttpServletRequestWrapper {
	HttpServletRequest orgRequest = null;  
	private List<String> invalidsql=null;
	private List<String> excepts=null;
	 public XssHttpServletRequestWrapper(HttpServletRequest request,List<String> invalidsql,List<String> excepts) {  
	        super(request);  
	        orgRequest = request;  
	        this.invalidsql=invalidsql;
	        this.excepts=excepts;
	    }
	    /** 
	    * 覆盖getParameter方法，将参数名和参数值都做xss过滤。<br/> 
	    * 如果需要获得原始的值，则通过super.getParameterValues(name)来获取<br/> 
	    * getParameterNames,getParameterValues和getParameterMap也可能需要覆盖 
	    */  
	    @Override  
	    public String getParameter(String name) {
	        String value = super.getParameter(xssEncode(name));  
	        if (value != null) {
	        	for(String ex : excepts){
	        		if(ex.equals(name)){
	        			return value;
	        		}
	        	}
	            value = xssEncode(value);  
	        }  
	        return value;  
	    }  
	    @Override
	    public String[] getParameterValues(String name) {
	    	// TODO Auto-generated method stub
	       	String[] vals=super.getParameterValues(name);
	       	if(vals==null)return null;
	       	for(String ex : excepts){
        		if(ex.equals(name)){
        			return vals;
        		}
        	}
	       	for(int i=0;i<vals.length;i++){
	       		vals[i]=xssEncode(vals[i]);
	       	}
	    	return vals;
	    }
	    /** 
	    * 覆盖getHeader方法，将参数名和参数值都做xss过滤。<br/> 
	    * 如果需要获得原始的值，则通过super.getHeaders(name)来获取<br/> 
	    * getHeaderNames 也可能需要覆盖 
	    */  
	    @Override  
	    public String getHeader(String name) {  
	    	
	        String value = super.getHeader(xssEncode(name));  
	        if (value != null) {  
	        	for(String ex : excepts){
	        		if(ex.equals(name)){
	        			return value;
	        		}
	        	}
	            value = xssEncode(value);  
	        }  
	        return value;  
	    }  

		@SuppressWarnings("rawtypes")
		@Override
	    public Map getParameterMap() {
	    	Map<String,String[]> maps=new HashMap<String, String[]>();
	    	for(Object key:super.getParameterMap().keySet()){
	    		maps.put(key.toString(), getParameterValues(key.toString()));
	    	}
	    	return maps;
	    }

	    /** 
	    * 将容易引起xss漏洞的半角字符直接替换成全角字符 
	    * 
	    * @param s 
	    * @return 
	    */  
	    private String xssEncode(String s) {  
	        if (s == null || "".equals(s)) {  
	            return s;  
	        }  
	        StringBuilder sb = new StringBuilder(s.length() + 16);  
	        for(String key:invalidsql){
	        	if(s.contains(key)){
	        		s.replace(key, " ");
	        	}
	        }
	        for (int i = 0; i < s.length(); i++) {  
	            char c = s.charAt(i);  
	            switch (c) {  
	            case '>':  
	                sb.append('＞');//全角大于号  
	                break;  
	            case '<':  
	                sb.append('＜');//全角小于号  
	                break;  
	            case '\'':  
	                sb.append('‘');//全角单引号  
	                break;  
	            /*case '\"':  
	                sb.append('“');//全角双引号  
	                break;  */
	            case '&':  
	                sb.append('＆');//全角  
	                break;  
	            case '\\':  
	                sb.append('＼');//全角斜线  
	                break;  
	            case '#':  
	                sb.append('＃');//全角井号  
	                break;  
	            default:  
	                sb.append(c);  
	                break;  
	            }  
	        }  
	        return sb.toString();  
	    }  
	  
	    /** 
	    * 获取最原始的request 
	    * 
	    * @return 
	    */  
	    public HttpServletRequest getOrgRequest() {  
	        return orgRequest;  
	    }  
	  
	    /** 
	    * 获取最原始的request的静态方法 
	    * 
	    * @return 
	    */  
	    public static HttpServletRequest getOrgRequest(HttpServletRequest req) {  
	        if (req instanceof XssHttpServletRequestWrapper) {  
	            return ((XssHttpServletRequestWrapper) req).getOrgRequest();  
	        }  
	  
	        return req;  
	    }  
}
