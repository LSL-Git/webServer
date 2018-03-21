package lsl.servlet;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.json.JSONObject;

import lsl.utils.DateUtils;
import lsl.utils.DbUtils;
import lsl.utils.ReturnToUser;

public class LabelsSetServlet extends HttpServlet {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private String type;
	private String manageName;
	private String taskNum;
	private String relyNum;
	private DbUtils db;
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
		
		type = request.getParameter("type");
		relyNum = request.getParameter("relyNum");
		taskNum = request.getParameter("taskNum");
		manageName = request.getParameter("manageName");
		db = DbUtils.getInstance();
		
		if(type.equals("getsetinfo")) {		
			JSONObject json = db.GetNewSetInfo();
			ReturnToUser.BackToUser(json, response);
		} else if (type.equals("putsetinfo")) {
			System.out.println(relyNum + "\n" + taskNum + "\n" + manageName);
			if(db.SaveSetInfo(Integer.parseInt(relyNum), Integer.parseInt(taskNum)
					, manageName, DateUtils.GetNowTime())) {
				ReturnToUser.BackToUser("OK", response);
			} else {
				ReturnToUser.BackToUser("Fail", response);
			}
		}
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

}
