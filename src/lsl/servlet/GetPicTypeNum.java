package lsl.servlet;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import lsl.utils.DbUtils;
import org.json.JSONObject;

public class GetPicTypeNum extends HttpServlet {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	/**
	 * The doGet method of the servlet. <br>
	 * 
	 * This method is called when a form has its tag value method equals to get.
	 * 
	 * @param request
	 *            the request send by the client to the server
	 * @param response
	 *            the response send by the server to the client
	 * @throws ServletException
	 *             if an error occurred
	 * @throws IOException
	 *             if an error occurred
	 */
	public void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {

		String type = request.getParameter("type");
//		String fileName = new String((request.getParameter("fileName") + "").getBytes(
//				"ISO-8859-1"), "UTF-8") + ""; // 解决接收中文乱码问题
		String fileName = request.getParameter("fileName");

		System.out.println("type:" + type);
		response.setContentType("text/json");
		response.setCharacterEncoding("UTF-8");
		PrintWriter out = response.getWriter();

		DbUtils.getInstance();

		if (type.equals("GetFileInfo")) {
			JSONObject json = new JSONObject();
			json.put("PicTypeNum", DbUtils.GetPicTypeNum());
			json.put("FileInfo", DbUtils.GetFile());
			out.println(json);
		} else if (type.equals("GetPicInfo")) {
			JSONObject json = new JSONObject();
			System.out.println("fileName:" + fileName);
			json = DbUtils.GetPicInfo(fileName, 200);
			out.println(json);
		}

		out.flush();
		out.close();
	}

	/**
	 * The doPost method of the servlet. <br>
	 * 
	 * This method is called when a form has its tag value method equals to
	 * post.
	 * 
	 * @param request
	 *            the request send by the client to the server
	 * @param response
	 *            the response send by the server to the client
	 * @throws ServletException
	 *             if an error occurred
	 * @throws IOException
	 *             if an error occurred
	 */
	public void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		doGet(request, response);
	}

}
