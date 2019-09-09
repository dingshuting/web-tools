package com.conf;

import java.io.IOException;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.EnableAspectJAutoProxy;
import org.springframework.web.filter.CharacterEncodingFilter;

import com.auth.LoginAjaxFilter;
import com.ijs.core.base.control.BaseControl;

@Configuration
@EnableAspectJAutoProxy
public class SpringConfig {
	protected final static transient Log log = LogFactory.getLog(SpringConfig.class);
	/*@Bean
	GsonHttpMessageConverter fastJsonHttpMessageConverter(){
		GsonHttpMessageConverter gsonHttpMessageConverter=new GsonHttpMessageConverter();
		List<MediaType> types=new ArrayList<MediaType>();
		types.add(MediaType.APPLICATION_JSON);
		types.add(MediaType.APPLICATION_JSON_UTF8);
		gsonHttpMessageConverter.setGson(new GsonBuilder().setDateFormat("yyyy-MM-dd HH:mm:ss").create());
		gsonHttpMessageConverter.setSupportedMediaTypes(types);
		
		return gsonHttpMessageConverter;
	}*/
	@Bean
	FilterRegistrationBean filterRegistrationBeanOfEncoding(){
		 FilterRegistrationBean filterRegistrationBean = new FilterRegistrationBean();
		 filterRegistrationBean.addUrlPatterns("/*");
		 filterRegistrationBean.setEnabled(true);  
		 filterRegistrationBean.setFilter(new CharacterEncodingFilter("UTF-8"));
		 filterRegistrationBean.setOrder(0);
		 return filterRegistrationBean;
	}
	@Bean
	FilterRegistrationBean filterTest(){
		 FilterRegistrationBean filterRegistrationBean = new FilterRegistrationBean();
		 filterRegistrationBean.addUrlPatterns("/*");
		 filterRegistrationBean.setEnabled(true);  
		 filterRegistrationBean.setFilter(new Filter() {
			
			@Override
			public void init(FilterConfig filterConfig) throws ServletException {
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
					throws IOException, ServletException {
				// TODO Auto-generated method stub
				log.debug("request-info-->"+((HttpServletRequest)request).getContextPath()+"<---->"+((HttpServletRequest)request).getRequestURI());
				chain.doFilter(request, response);
			}
			
			@Override
			public void destroy() {
				// TODO Auto-generated method stub
				
			}
		});
		 filterRegistrationBean.setOrder(0);
		 return filterRegistrationBean;
	}
	@Bean
	FilterRegistrationBean filterRegistrationBeanOfAjaxLogin(){
		 FilterRegistrationBean filterRegistrationBean = new FilterRegistrationBean();
		 filterRegistrationBean.addUrlPatterns("/login_auth");
		 filterRegistrationBean.setEnabled(true);  
		 filterRegistrationBean.setFilter(new LoginAjaxFilter());
		 filterRegistrationBean.setOrder(1);
		 return filterRegistrationBean;
	}
	/*@Bean
	FilterRegistrationBean filterRegistrationBeanOfSpringSecurity(DelegatingFilterProxy delegatingFilterProxy){
		 FilterRegistrationBean filterRegistrationBean = new FilterRegistrationBean();
		 filterRegistrationBean.addUrlPatterns("/*");
		 filterRegistrationBean.setEnabled(true);  
		 filterRegistrationBean.setFilter(delegatingFilterProxy);
		 filterRegistrationBean.setOrder(2);
		 return filterRegistrationBean;
	}*/
}
