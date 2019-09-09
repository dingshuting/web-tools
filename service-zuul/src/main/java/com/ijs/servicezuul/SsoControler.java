package com.ijs.servicezuul;

import java.security.Principal;
import java.util.Collections;
import java.util.Map;

import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

@RestController
@RequestMapping("/dashboard")
public class SsoControler {

	@RequestMapping("/message")
	public Map<String, Object> dashboard() {
		return Collections.<String, Object> singletonMap("message", "Yay!");
	}

	@RequestMapping("/user")
	public Principal user(Principal user) {
		 ((ServletRequestAttributes)RequestContextHolder.getRequestAttributes()).getRequest();
		SecurityContextHolder.getContext().getAuthentication();
		return user;
	}

	@Controller
	@RequestMapping("/")
	public static class LoginErrors {
		@Autowired 
		private Environment evn;
		@RequestMapping("index")
		public String dashboard(@RequestParam(name="redirectUrl",required=false)String redirectUrl) {
			if(null!=redirectUrl&&!redirectUrl.isEmpty()) {
				return "redirect:"+redirectUrl;
			}
			return "redirect:"+evn.getProperty("app.ui-index-url");
		}
		@RequestMapping("index.html")
		public String index() {
			return "redirect:index.html";
		}
		@RequestMapping("login")
		public @ResponseBody String login() {
			return "Don't need to login by zuul-self";
		}
	}
	@Controller
	@RequestMapping("/common")
	public static class SessionControl {
		@Autowired 
		private Environment evn;
		@RequestMapping("/session")
		public @ResponseBody String session(HttpSession session) {
			return session.getId();
		}
	}
}
