package lsl.servlet;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.json.JSONObject;

import lsl.utils.ImgUtils;

public class GetImageServlet extends HttpServlet {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	/**
	 * Constructor of the object.
	 */
	public GetImageServlet() {
		super();
	}

	/**
	 * http://127.0.0.1/webServer/servlet/images?type=GetImgUrl 获取图片的URL
	 *
	 * http://127.0.0.1/webServer/servlet/images?type=get&name=医院5.jpg 获取一张图片的base64 字符串
	 * 
	 * @param request the request send by the client to the server
	 * @param response the response send by the server to the client
	 * @throws ServletException if an error occurred
	 * @throws IOException if an error occurred
	 */
	public void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		System.out.println("GetImageServlet Get...");
		
		String type = request.getParameter("type");

		response.setContentType("text/json");
		response.setCharacterEncoding("UTF-8");
		PrintWriter out = response.getWriter();
		
		if (type.equals("GetImgUrl")) {
			JSONObject json  = ImgUtils.getImgUrl();
			out.println(json);
			// 可用在用户端解析json
/*			int num = json.getInt("num") - 1;
			System.out.println(num + 1);
			while(num >= 0) {
				System.out.println(num + " : " + json.getJSONObject("img" + num));
				out.println(num + " : " + json.getJSONObject("img" + num));
				num--;
			}*/
			
		} else if(type.equals("get")){
			String getName = request.getParameter("name");
			JSONObject json = ImgUtils.getImg(getName);
			out.println(json);
		}
		
		out.flush();
		out.close();
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
		this.doGet(request, response);
		
		response.setContentType("text/html");
		PrintWriter out = response.getWriter();
		out.println("<!DOCTYPE HTML PUBLIC \"-//W3C//DTD HTML 4.01 Transitional//EN\">");
		out.println("<HTML>");
		out.println("  <HEAD><TITLE>A Servlet</TITLE></HEAD>");
		out.println("  <BODY>");
		out.print("    This is ");
		out.print(this.getClass());
		out.println(", using the POST method");
		out.println("  </BODY>");
		out.println("</HTML>");
		out.flush();
		out.close();
	}

	/**
	 * Initialization of the servlet. <br>
	 *
	 * @throws ServletException if an error occurs
	 */
	public void init() throws ServletException {
		//System.out.println("init()...");
	}

}
