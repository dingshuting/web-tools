package com;


import javax.annotation.Resource;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.servlet.ServletComponentScan;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.context.annotation.ImportResource;
import org.springframework.security.authentication.AuthenticationEventPublisher;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableResourceServer;
import org.springframework.security.oauth2.config.annotation.web.configuration.ResourceServerConfigurerAdapter;
import org.springframework.security.oauth2.config.annotation.web.configurers.ResourceServerSecurityConfigurer;

import com.ijs.core.base.control.BaseControl;
import com.ijs.core.base.listener.MysqlServiceListenerRegister;
import com.ijs.core.base.model.User;
import com.ijs.core.base.service.GenericServ;
import com.ijs.core.base.service.SessionUserServ;
import com.ijs.core.component.InMemorySessionCache;
import com.ijs.core.component.SessionCache;
import com.ijs.core.conf.MyWebMvcConfigurerAdapter;
import com.ijs.core.system.service.impl.ParameterServImpl;
import com.ijs.core.util.spring.BeanSelector;

@SpringBootApplication
@ServletComponentScan("com.ijs.*.*.servlet")
@ComponentScan(basePackages={"com.ijs.**"})
@EnableAutoConfiguration
//@Import({DynamicDataSourceRegister.class}) 
@Import({MyWebMvcConfigurerAdapter.class})
@EnableEurekaClient
@ImportResource(locations={"classpath:/applicationContext-jpa.xml"})
public class BootstrapApplication {
	protected static final transient Log log = LogFactory.getLog(BootstrapApplication.class);
	public static ConfigurableApplicationContext context;
	
	public static void main(String[] args) {
		context=SpringApplication.run(BootstrapApplication.class, args);
		BeanSelector.applicationContext=context;
		ParameterServImpl ps=context.getBean(ParameterServImpl.class);
		//MongoService mongo=context.getBean(MongoService.class);
		ps.cache();
		//ServiceListenerRegister.startListen(mongo);
		MysqlServiceListenerRegister.startListen((GenericServ) context.getBean("genericServ"));
	
	}
	
	
	
	@Bean
	public SessionCache initSessionCache() {
		return new InMemorySessionCache();
	}
	@Configuration
	@EnableWebSecurity
	@EnableResourceServer
	protected static class ResourceServerConfiguration extends
	        ResourceServerConfigurerAdapter {
		@Resource
		SessionUserServ sessionServ;
		@Override
		public void configure(HttpSecurity http) throws Exception {
			http.authorizeRequests().antMatchers("/static/**","/user/cu**","/common/**","/**.txt").permitAll()
			.and().sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS);
			super.configure(http);
		}
		
		@Override
		public void configure(ResourceServerSecurityConfigurer resources) throws Exception {
			// TODO Auto-generated method stub
			resources.eventPublisher(new AuthenticationEventPublisher() {
				
				@Override
				public void publishAuthenticationSuccess(Authentication authentication) {
					// TODO Auto-generated method stub
					User user=sessionServ.loadUserByaccountNo(authentication.getPrincipal().toString());
					//((OAuth2Authentication)authentication).setDetails(user);
					BaseControl.getRequest().setAttribute("user", user);
				}
				
				@Override
				public void publishAuthenticationFailure(AuthenticationException exception, Authentication authentication) {
					// TODO Auto-generated method stub
					
				}
			});
			super.configure(resources);
		}
	}
	
}
