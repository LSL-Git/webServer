package lsl.servlet;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import lsl.utils.DbUtils;
import lsl.utils.ReturnToUser;

import org.json.JSONObject;
/**
 * ��ѯ�û���Ϣ ������û���Ϣ
 * @author M1308_000
 *
 */
public class UserInfoServlet extends HttpServlet{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	/**����URL
	 * http://127.0.0.1/webServer/userinfo?user_name=regggr&type=update
		&update_psw=1234&update_tel=1122344&update_email=22ddd3@qq.com*/
	
	/**��ѯURL
	 * http://127.0.0.1/webServer/userinfo?user_name=regggr&type=query*/
	
	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		System.out.println("UserInfo Get...");
		
		String type = req.getParameter("type");
		String user_name = req.getParameter("user_name");
		String update_psw = req.getParameter("update_psw");
		String update_tel = req.getParameter("update_tel");
		String update_email = req.getParameter("update_email");
		
		System.out.println("type: " + type);
		System.out.println("user_name: " + user_name);	
		System.out.println("update_psw: " + update_psw);	
		System.out.println("update_tel: " + update_tel);	
		System.out.println("update_email: " + update_email);	
		
		// �õ����ݿ�ʵ��
		DbUtils.getInstance();
		if (type.equals("query")) {
			try {
				resp.setCharacterEncoding("UTF-8");
				resp.setContentType("text/json");
				PrintWriter out = resp.getWriter();
				
				// �����ݿ��ѯ�û���Ϣ
				JSONObject json = DbUtils.QueryUserAll(user_name);
			
				// ���û�д������
				out.write(json.toString());
				System.out.println("��������ѷ���...");
				out.flush();
				out.close();
			}catch (Exception e) {
				System.err.println("UserInfo :" + e.getMessage());
			}
		} else if (type.equals("update")) {
			// ���ø������ݿ���Ϣ
			String RESULT = DbUtils.UpDateUserInfo(user_name, update_psw, update_tel, update_email);
			// ���û������½��
			ReturnToUser.BackToUser(RESULT, resp);
		}
		
	}

	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		this.doGet(req, resp);
	}

}
