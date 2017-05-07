package lsl.servlet;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import lsl.task.UserTask;
import lsl.utils.ReturnToUser;

/**
 * 用户登录
 * @author M1308_000
 *
 */
public class LoginServlet extends HttpServlet {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	/** 登录URL
	 * http://127.0.0.1/webServer/login?login_name=regggr&login_psw=11223*/

	
	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		System.out.println("Login Get...");
		
		// 接收用户数据
		String user_name = req.getParameter("login_name");
		String user_psw = req.getParameter("login_psw");
		System.out.println("user_name: " + user_name);
		System.out.println("user_psw: " + user_psw);

		// 调用登录业务，返回业务执行结果
		String RESULT = UserTask.LoginTask(user_name, user_psw);
		// 调用返回信息
		ReturnToUser.BackToUser(RESULT, resp);
	}

	/**
	 * 适用于发送大量数据
	 */
	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		System.out.println("Post...");
		this.doGet(req, resp);
		
	}
	
	

}
