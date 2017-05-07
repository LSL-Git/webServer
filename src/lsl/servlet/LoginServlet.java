package lsl.servlet;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import lsl.task.UserTask;
import lsl.utils.ReturnToUser;

/**
 * �û���¼
 * @author M1308_000
 *
 */
public class LoginServlet extends HttpServlet {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	/** ��¼URL
	 * http://127.0.0.1/webServer/login?login_name=regggr&login_psw=11223*/

	
	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		System.out.println("Login Get...");
		
		// �����û�����
		String user_name = req.getParameter("login_name");
		String user_psw = req.getParameter("login_psw");
		System.out.println("user_name: " + user_name);
		System.out.println("user_psw: " + user_psw);

		// ���õ�¼ҵ�񣬷���ҵ��ִ�н��
		String RESULT = UserTask.LoginTask(user_name, user_psw);
		// ���÷�����Ϣ
		ReturnToUser.BackToUser(RESULT, resp);
	}

	/**
	 * �����ڷ��ʹ�������
	 */
	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		System.out.println("Post...");
		this.doGet(req, resp);
		
	}
	
	

}
