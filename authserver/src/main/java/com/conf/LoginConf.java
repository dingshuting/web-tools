package com.conf;

import javax.annotation.Resource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.security.SecurityProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.core.env.Environment;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.dao.ReflectionSaltSource;
import org.springframework.security.authentication.encoding.Md5PasswordEncoder;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.web.authentication.SavedRequestAwareAuthenticationSuccessHandler;

import com.auth.IntergrationAuthProvider;
import com.ijs.core.base.service.LoginServ;

@Configuration
@Order(SecurityProperties.ACCESS_OVERRIDE_ORDER)
public class LoginConf extends WebSecurityConfigurerAdapter {


	@Resource(name="ssoLoginServ")
	private LoginServ userDetailsService;
	@Resource
	private IntergrationAuthProvider authProvider;
	
	@Autowired 
	private Environment evn;
	@Override
	protected void configure(HttpSecurity http) throws Exception {
		http.csrf().disable()
		.formLogin().loginPage("/login").permitAll().loginProcessingUrl("/login_auth").successHandler(new SavedRequestAwareAuthenticationSuccessHandler()).defaultSuccessUrl(evn.getProperty("config.loging-success-url"))
        .and().authorizeRequests()
        .antMatchers("/images/**", "/checkcode", "/scripts/**", "/styles/**","/static/**").permitAll()
        .anyRequest().authenticated();
	}
	@Bean
	public Md5PasswordEncoder passEncoder() {
		Md5PasswordEncoder md5PasswordEncoder = new Md5PasswordEncoder();
		md5PasswordEncoder.setEncodeHashAsBase64(false);
		return md5PasswordEncoder;
	}
	@Override
	protected void configure(AuthenticationManagerBuilder auth) throws Exception {
		auth.userDetailsService(userDetailsService).passwordEncoder(passEncoder())
		.and().authenticationProvider(authProvider);
	}
	 @Override
    @Bean
    public AuthenticationManager authenticationManagerBean() throws Exception {
        return super.authenticationManagerBean();
    }
}
