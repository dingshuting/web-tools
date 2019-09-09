package com.auth;

import java.io.IOException;
import java.security.Principal;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.security.web.authentication.logout.SecurityContextLogoutHandler;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
public class LoginControl {
	 @GetMapping("/me")
    public @ResponseBody Principal user(Principal principal) {
        return principal;
    }
	 @RequestMapping("/login")
	 public String login() {
		 return "index";
	 }
	 @RequestMapping("/oauth/exit")
    public void exit(HttpServletRequest request, HttpServletResponse response) {
        // token can be revoked here if needed
        new SecurityContextLogoutHandler().logout(request, null, null);
        try {
            //sending back to client app
        	if(request.getParameter("redirect")!=null) {
        		 response.sendRedirect(request.getHeader(request.getParameter("redirect")));
        	}else {
        		 response.sendRedirect(request.getHeader("referer"));
        	}
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
