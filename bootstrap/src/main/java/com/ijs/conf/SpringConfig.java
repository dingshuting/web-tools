package com.ijs.conf;

import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.EnableAspectJAutoProxy;
import org.springframework.web.filter.CharacterEncodingFilter;
import org.springframework.web.servlet.mvc.method.annotation.RequestMappingHandlerMapping;

import com.ijs.core.base.filter.ControlAllInterceptor;

@Configuration
@EnableAspectJAutoProxy
public class SpringConfig {
	
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
	ControlAllInterceptor regInterceptor(RequestMappingHandlerMapping mapping){
		ControlAllInterceptor ci=new ControlAllInterceptor();
		//鎺掑嚭鐨勯獙璇乽rl
		ci.setExcludeUrl("");
		mapping.setInterceptors(new Object[]{ci});
		return ci;
	}
}
