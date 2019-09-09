package com.ijs.servicezuul;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.security.oauth2.client.EnableOAuth2Sso;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;
import org.springframework.cloud.netflix.zuul.EnableZuulProxy;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.core.env.Environment;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;

@EnableEurekaClient
@SpringBootApplication
@EnableZuulProxy
@Import({SsoControler.class})
public class ServiceZuulApplication {

    public static void main(String[] args) {
        SpringApplication.run(ServiceZuulApplication.class, args);
    }
	@Configuration
	@EnableAutoConfiguration
	@EnableOAuth2Sso
	public static class LoginConfigurer extends WebSecurityConfigurerAdapter {
		@Autowired  
	    private Environment env;  
		@Override
		public void configure(HttpSecurity http) throws Exception {
			String logoutUrl=env.getProperty("app.auth-server-url")+"oauth/exit";
			if(env.containsProperty("app.ui-index-url")) {
				logoutUrl+="?redict="+env.getProperty("app.ui-index-url");
			}
			http.csrf().disable()
			.logout().deleteCookies("JSESSIONID").logoutUrl("/logout").logoutSuccessUrl(logoutUrl)
			.and().formLogin().loginPage("/login").permitAll().disable()
			.httpBasic().disable()
			.authorizeRequests().antMatchers("/*/common/**","/*/static/**","/api-*/**","/*/user/cu**","/uua/**,/common/*").permitAll()
			.and().authorizeRequests()
			.anyRequest().authenticated();
		}
		
	}
}
