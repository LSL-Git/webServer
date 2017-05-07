package lsl.servlet;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import lsl.task.UserTask;
import lsl.utils.ReturnToUser;
/**
 * 用户注册
 * @author M1308_000
 *
 */
public class RegisterServlet extends HttpServlet {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	/** 注册URL
	 * http://127.0.0.1/webServer/register?register_name=regggr
	 * &register_psw=11223&register_tel=4455
	 */
	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		System.out.println("Register Get...");
		
		String new_user_name = req.getParameter("register_name");
		String new_user_psw = req.getParameter("register_psw");
		String new_user_tel = req.getParameter("register_tel");
		
		System.out.println("register_name: " + new_user_name);
		System.out.println("register_psw: " + new_user_psw);
		System.out.println("register_tel: " + new_user_tel);
		// 调用注册业务
		String RESULT = UserTask.RegisterTask(new_user_name, new_user_psw, new_user_tel);
		
		ReturnToUser.BackToUser(RESULT, resp);
	}

	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		this.doGet(req, resp);
	}
	
	

}
