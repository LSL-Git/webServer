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
	private PrintWriter out;
	private int isManager = 0;

	/**�����û���Ϣ��URL
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
		String origin_psw = req.getParameter("origin_psw");
		String new_psw = req.getParameter("new_psw");
		String update_tel = req.getParameter("update_tel");
		String update_email = req.getParameter("update_email");
		String isM = "" + req.getParameter("manager");
		
		if (isM.equals("YES")) {
			isManager  = 1;
		}	
				
		resp.setCharacterEncoding("UTF-8");
		resp.setContentType("text/json");
		// �õ����ݿ�ʵ��
		DbUtils.getInstance();
		if (type.equals("query_user_info")) {
			try {
				out = resp.getWriter();
				
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
			String RESULT = DbUtils.UpDateUserInfo(user_name,update_tel, update_email, isManager);
			// ���û������½��
			ReturnToUser.BackToUser(RESULT, resp);
		} else if(type.equals("alter_psw")) {
			String result = DbUtils.Login(user_name);
			out = resp.getWriter();
			if (result.equals(origin_psw)) {
				out.write("1");	// ����У�Գɹ�
				if(DbUtils.AlterPsw(user_name, new_psw).equals("Alter_Success"))
					out.write("2");// �޸ĳɹ�
				else
					out.write("3");// �޸�ʧ��
			} else {
				System.out.println("fail");
				out.write("0");	// ����У��ʧ��
			}
		}else if(type.equals("get_all_user_name")) { 
			out = resp.getWriter();
			out.println(DbUtils.GetAllUserName());
		}
		out.flush();
		out.close();
		
	}

	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		this.doGet(req, resp);
	}

}
