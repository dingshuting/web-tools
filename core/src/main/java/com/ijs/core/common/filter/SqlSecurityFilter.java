package com.ijs.core.common.filter;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
/**
 * Sql过滤器，防止sql入侵和Xss脚本入侵的filter
 * @author Dustin
 *
 */
public class SqlSecurityFilter implements Filter{

	 	private static List<String> invalidsql = new ArrayList<String>(); 
		private static List<String> excepts = new ArrayList<String>(); 
	     
	    public void destroy() { 
	         
	    } 
	    public void doFilter(ServletRequest req, ServletResponse res, 
	            FilterChain fc) throws IOException, ServletException { 
	    	  XssHttpServletRequestWrapper xssRequest = new XssHttpServletRequestWrapper(  
	    	            (HttpServletRequest) req,invalidsql,excepts);  
	    	  fc.doFilter(xssRequest, res);  
	    }
	    
		public void init(FilterConfig filterConfig) throws ServletException {
			// TODO Auto-generated method stub
			String tinvalidsql=filterConfig.getInitParameter("invalidsql");
			String texcepts=filterConfig.getInitParameter("excepts");
			if(invalidsql!=null){
				invalidsql=Arrays.asList(tinvalidsql.split(" "));
			}
			if(excepts!=null){
				excepts=Arrays.asList(texcepts.split(" "));
			}
		}
}
