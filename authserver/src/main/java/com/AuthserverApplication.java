package com;

import java.security.KeyPair;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.context.annotation.ImportResource;
import org.springframework.core.io.ClassPathResource;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.oauth2.config.annotation.configurers.ClientDetailsServiceConfigurer;
import org.springframework.security.oauth2.config.annotation.web.configuration.AuthorizationServerConfigurerAdapter;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableAuthorizationServer;
import org.springframework.security.oauth2.config.annotation.web.configurers.AuthorizationServerEndpointsConfigurer;
import org.springframework.security.oauth2.config.annotation.web.configurers.AuthorizationServerSecurityConfigurer;
import org.springframework.security.oauth2.provider.token.store.JwtAccessTokenConverter;
import org.springframework.security.oauth2.provider.token.store.KeyStoreKeyFactory;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.SessionAttributes;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.ViewControllerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;

import com.conf.LoginConf;
import com.conf.SpringConfig;
import com.ijs.core.system.service.impl.ParameterServImpl;

@Configuration
@EnableAutoConfiguration
@SessionAttributes("authorizationRequest")
@ImportResource(locations= {"classpath:applicationContext-jpa.xml"})
@EnableGlobalMethodSecurity(prePostEnabled = true)
@Import({LoginConf.class,SpringConfig.class})
@ComponentScan(basePackages={"com.springframework.*","com.auth.**","com.ijs.service.**","com.ijs.*.dao.**","com.ijs.*.service.**","com.ijs.*.*.service.**","com.ijs.*.*.dao.**","com.ijs.jobs"})
public class AuthserverApplication extends WebMvcConfigurerAdapter {

	public static void main(String[] args) {
		ConfigurableApplicationContext context=SpringApplication.run(AuthserverApplication.class, args);
		ParameterServImpl ps=context.getBean(ParameterServImpl.class);
		//MongoService mongo=context.getBean(MongoService.class);
		ps.cache();
	}
	
	@Configuration
	@EnableAuthorizationServer
	protected static class OAuth2Config extends AuthorizationServerConfigurerAdapter {
		 @Autowired
		private AuthenticationManager authenticationManager;
			@Bean
			public JwtAccessTokenConverter jwtAccessTokenConverter() {
				JwtAccessTokenConverter converter = new JwtAccessTokenConverter();
				KeyPair keyPair = new KeyStoreKeyFactory(
						new ClassPathResource("keystore.jks"), "foobar".toCharArray())
						.getKeyPair("test");
				converter.setKeyPair(keyPair);
				return converter;
			}
		@Override
		public void configure(ClientDetailsServiceConfigurer clients) throws Exception {
			clients.inMemory()
					.withClient("acme")
					.secret("acmesecret")
					.authorizedGrantTypes("authorization_code", "refresh_token",
							"password","implicit").scopes("openid").autoApprove(true);
		}

		@Override
		public void configure(AuthorizationServerEndpointsConfigurer endpoints)
				throws Exception {
			 endpoints.authenticationManager(authenticationManager).accessTokenConverter(jwtAccessTokenConverter());
		}

		@Override
		public void configure(AuthorizationServerSecurityConfigurer oauthServer)
				throws Exception {
			oauthServer.tokenKeyAccess("permitAll()").checkTokenAccess("isAuthenticated()").allowFormAuthenticationForClients();
		}
		
		

	}
}
