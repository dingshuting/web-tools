package com.ijs.core.common.servlet;

import java.io.IOException;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.ijs.core.base.Constants;
import com.ijs.core.component.SessionCache;
import com.ijs.core.util.spring.BeanSelector;
/**
 * 
 * 生成验证码图片的servlet
 *
 */
@WebServlet(urlPatterns="/ver_img")
public class VerifyImg extends HttpServlet {
	
	private String verifyCode=null;
	//图片宽
	private int width=140;
	//图片高
	private int height=50;
	/**
	 * Constructor of the object.
	 */
	public VerifyImg() {
		super();
	}
	@Override
	public void init(ServletConfig config) throws ServletException {
		// TODO Auto-generated method stub
		verifyCode=config.getInitParameter(Constants.VERIFY_CODE_SESSION_NAME);
		if(verifyCode==null||verifyCode.equals("")){
			verifyCode=Constants.VERIFY_CODE_SESSION_NAME;
		} 
		if(config.getInitParameter("width")!=null&&!config.getInitParameter("width").isEmpty()){
			width=Integer.parseInt(config.getInitParameter("width"));
		}
		if(config.getInitParameter("width")!=null&&!config.getInitParameter("height").isEmpty()){
			height=Integer.parseInt(config.getInitParameter("height"));
		}
	}
	/**
	 * Destruction of the servlet. <br>
	 */
	public void destroy() {
		super.destroy(); // Just puts "destroy" string in log
		// Put your code here
	}

	/**
	 * The doGet method of the servlet. <br>
	 *
	 * This method is called when a form has its tag value method equals to get.
	 * 
	 * @param request the request send by the client to the server
	 * @param response the response send by the server to the client
	 * @throws ServletException if an error occurred
	 * @throws IOException if an error occurred
	 */
	public void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		response.setContentType("image/pjpeg");
		String verifyCode=VerifyCodeUtils.generateVerifyCode(4);
		BeanSelector.getSession().setAttribute(Constants.VERIFY_CODE_SESSION_NAME,verifyCode.toLowerCase());
		VerifyCodeUtils.outputImage(width, height, response.getOutputStream(), verifyCode);
		
	}

	/**
	 * The doPost method of the servlet. <br>
	 *
	 * This method is called when a form has its tag value method equals to post.
	 * 
	 * @param request the request send by the client to the server
	 * @param response the response send by the server to the client
	 * @throws ServletException if an error occurred
	 * @throws IOException if an error occurred
	 */
	public void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {

		doGet(request, response);
	}

	/**
	 * Initialization of the servlet. <br>
	 *
	 * @throws ServletException if an error occurs
	 */
	public void init() throws ServletException {
		// Put your code here
		
	}

}
