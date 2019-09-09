package com.ijs.core.component;

import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import com.ijs.core.base.Config;
import com.ijs.core.component.impl.InCacheSession;
import com.ijs.core.util.spring.BeanSelector;

public class InMemorySessionCache implements SessionCache {
	
	
	private Map<String,HttpSession> sessionCache=new HashMap<>();

	@Override
	public void create(HttpServletRequest request) {
		// TODO Auto-generated method stub
		String sessionId=getSessionId(request);
		if(!sessionCache.containsKey(sessionId)) {
			sessionCache.put(sessionId, new InCacheSession(sessionId));
		}
	}

	@Override
	public HttpSession getSession(HttpServletRequest request) {
		// TODO Auto-generated method stub
		String sessionId=getSessionId(request);
		if(sessionCache.containsKey(sessionId)) {
			return sessionCache.get(sessionId);
		}else {
			sessionCache.put(sessionId, new InCacheSession(sessionId));
			return sessionCache.get(sessionId);
		}
	}

	@Override
	public void remove(HttpServletRequest request) {
		// TODO Auto-generated method stub
		if(sessionCache.containsKey(getSessionId(request))) {
			sessionCache.remove(getSessionId(request));
		}
	}
	
	private String getSessionId(HttpServletRequest request) {
		Cookie[] cookies=request.getCookies();
		for(Cookie cook:cookies) {
			if(cook.getName().equals("JSESSIONID")) {
				return cook.getValue();
			}
		}
		return "null";
	}
	
	
}
