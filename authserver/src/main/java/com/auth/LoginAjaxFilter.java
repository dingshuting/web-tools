package com.auth;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.ijs.core.base.Constants;
import com.ijs.core.base.control.BaseControl;
import com.ijs.core.base.model.User;

public class LoginAjaxFilter extends OncePerRequestFilter {
	protected final transient Log log = LogFactory.getLog(getClass());
	private boolean isNeedVerifyCode=false;
	@Override
	protected void initFilterBean() throws ServletException {
		// TODO Auto-generated method stub
		super.initFilterBean();
	}

	@Override
	protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
			throws ServletException, IOException {

		// 检查提交的变量中是否有ajax请求的变量，如果没有，则不是ajax的登录请求，则走默认的请求。
		if (!isAjaxRequest(request)) {
			log.info("执行常规登录");
			filterChain.doFilter(request, response);
			return;
		}
		log.info("执行异步验证操作");
		boolean isPassCodeVali = true;
		Map<String, String> map = new HashMap<String, String>();
		if(isNeedVerifyCode) {
			String sessionVcode = request.getSession().getAttribute(Constants.VERIFY_CODE_SESSION_NAME).toString();
			
			String verifyCode = request.getParameter("verifyCode");
			
			if (verifyCode != null) {
				verifyCode = verifyCode.toLowerCase();
			} else {
				map.put("errorMsg", "vcError");
			}
			if (sessionVcode == null || verifyCode == null || !sessionVcode.equals(verifyCode)) {
				map.put("success", "false");
				map.put("errorMsg", "vcError");
				isPassCodeVali = false;
			}
		}
		log.debug("AjaxSecurityFilter: Processing an AJAX call : " + request.getRequestURL());
		if ((isNeedVerifyCode&&isPassCodeVali)||!isNeedVerifyCode) {
			RedirectResponseWrapper redirectResponseWrapper = new RedirectResponseWrapper(response);
			filterChain.doFilter(request, redirectResponseWrapper);
			SecurityContextHolder.getContext().getAuthentication();
			if (redirectResponseWrapper.getRedirect() != null) {

				String redirectURL = redirectResponseWrapper.getRedirect();

				if (redirectURL.indexOf("login") != -1) {
					// populate your reply in this case the json object
					// with what ever information needed to pop up your login
					// window
					if (redirectURL.indexOf("login_error=1") != -1) {
						// 登录失败
						map.put("success", "false");
					}
				}
				// / your auth is successful the call is successful
				else {
					// you can return the user name and password in the reply so
					// it
					// can be displayed for example in you app
					// SecurityContextHolder.getContext().getAuthentication().getPrincipal();
					Object user=SecurityContextHolder.getContext().getAuthentication().getDetails();
						if (user != null) {
							map.put("user",
									new GsonBuilder().excludeFieldsWithoutExposeAnnotation().create().toJson(user));
							map.put("success", "true");
						} else {
							// 登录失败
							map.put("success", "false");
							map.put("errorMsg", "error");
						}
				}
			}
		}
		try {
			String outString = new Gson().toJson(map);
			response.setStatus(HttpServletResponse.SC_OK);
			response.setContentType("application/json;charSet=UTF-8");
			log.debug("jsonString : " + outString);
			response.getWriter().write(outString);
		} catch (Exception e) {
			log.error(e);
		}

	}

	/**
	 * @param request
	 *            the request object
	 * @return true if this request is an ajax request. This is determined by a
	 *         configured name/value pair that is applied to the request header
	 */
	protected boolean isAjaxRequest(HttpServletRequest request) {
		// test with our ajax request pairs
		String ajax = request.getParameter("ajax");
		log.debug(ajax);
		if ("".equals(ajax) || ajax == null) {
			return false;
		}
		return true;
	}

	protected class JsonFlag {

		String success;

		public String getSuccess() {
			return success;
		}

		public void setSuccess(String success) {
			this.success = success;
		}

		public String getUsername() {
			return username;
		}

		public void setUsername(String username) {
			this.username = username;
		}

		String username;
	}

}