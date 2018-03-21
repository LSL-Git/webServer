package lsl.servlet;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import lsl.task.PscTask;
import lsl.utils.DateUtils;
import lsl.utils.DbUtils;
import lsl.utils.ReturnToUser;

public class LabelsServlet extends HttpServlet {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private String type;
	private String username;
	private String picname;
	private String la1;
	private String la2;
	private String la3;
	private String la4;
	private String la5;
	private String la6;
	private String la7;
	private String la8;
	private String la9;
	private String la10;

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
		System.out.println("PicLabelServlet...");
		
		type = request.getParameter("type");
		username = request.getParameter("userName");
		picname = request.getParameter("picName");
		la1 = request.getParameter("label1") + "";
		la2 = request.getParameter("label2") + "";
		la3 = request.getParameter("label3") + "";
		la4 = request.getParameter("label4") + "";
		la5 = request.getParameter("label5") + "";
		la6 = request.getParameter("label6") + "";
		la7 = request.getParameter("label7") + "";
		la8 = request.getParameter("label8") + "";
		la9 = request.getParameter("label9") + "";
		la10 = request.getParameter("label10") + "";
		
		
		if (type.equals("putlabels")) {
			DbUtils.getInstance();
			if (DbUtils.SaveLabels(picname, username, 0, DateUtils.GetNowTime(), 
					la1, la2, la3, la4, la5, la6, la7, la8, la9, la10)) {
				PscTask p = new PscTask();
				p.mainTask(picname); // 检测是否符合判定要求并判定图片
				ReturnToUser.BackToUser("OK", response);
			} else {
				ReturnToUser.BackToUser("NO", response);				
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
