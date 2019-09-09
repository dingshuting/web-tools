package com.ijs.core.common.filter;

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
import org.springframework.web.filter.OncePerRequestFilter;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.ijs.core.base.Constants;
import com.ijs.core.base.model.User;
import com.ijs.core.util.spring.BeanSelector;
/**
 * 用于异步登录的拦截器
 * @author Dustin
 *
 */
public class LoginAjaxFilter extends OncePerRequestFilter {
	protected final transient Log log = LogFactory.getLog(getClass());

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
			filterChain.doFilter(request, response);
			return;
		}
		Object isCodeVerify=BeanSelector.getSession().getAttribute(Constants.IS_VERIFY_CODE);
		isCodeVerify=isCodeVerify==null?false:isCodeVerify;
		Object sessionVcode =BeanSelector.getSession().getAttribute(Constants.VERIFY_CODE_SESSION_NAME);
		//String sessionVcode = "";
		String verifyCode = request.getParameter("verifyCode");
		Map<String, String> map = new HashMap<String, String>();
		boolean isPassCodeVali = true;
		if (verifyCode != null&&!verifyCode.isEmpty()) {
			verifyCode = verifyCode.toLowerCase();
			isCodeVerify=true;
		} else {
			map.put("errorMsg", "vcError");
		}
		if ((boolean)isCodeVerify&&(sessionVcode == null || verifyCode == null || !sessionVcode.equals(verifyCode))) {
			map.put("success", "false");
			map.put("errorMsg", "vcError");
			isPassCodeVali = false;
		}
		log.debug("AjaxSecurityFilter: Processing an AJAX call : " + request.getRequestURL());
		if (isPassCodeVali) {
			RedirectResponseWrapper redirectResponseWrapper = new RedirectResponseWrapper(response);
			filterChain.doFilter(request, redirectResponseWrapper);

			if (redirectResponseWrapper.getRedirect() != null) {

				String redirectURL = redirectResponseWrapper.getRedirect();

				if (redirectURL.indexOf("auth") != -1) {
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
					SecurityContext ctx = (SecurityContext) request.getSession()
							.getAttribute("SPRING_SECURITY_CONTEXT");

					if (ctx != null) {

						Authentication auth = ctx.getAuthentication();
						User user = (User) auth.getPrincipal();
						if (user != null) {
							map.put("user",
									new GsonBuilder().excludeFieldsWithoutExposeAnnotation().create().toJson(user));
							map.put("success", "true");
						} else {
							// 登录失败
							map.put("success", "false");
							map.put("errorMsg", "error");
						}
					} else {
						map.put("success", "false");
					}
				}
			}
		}
		try {
			String outString = new Gson().toJson(map);
			response.setContentType("application/json;charSet=UTF-8");
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