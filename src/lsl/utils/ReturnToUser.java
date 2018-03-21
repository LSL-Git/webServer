package lsl.utils;

import java.io.PrintWriter;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletResponse;

import org.json.JSONObject;

/**
 * 用来返回用户相关信息
 * @author M1308_000
 *
 */
public class ReturnToUser {
	
	/**
	 * 返回用户请求结果
	 * @param RESULT
	 * @param resp
	 */
	public static void BackToUser(Object RESULT, HttpServletResponse resp) {
		try {
			resp.setCharacterEncoding("UTF-8");
			resp.setContentType("text/json");
			PrintWriter out = resp.getWriter();
			
			Map<String, String> map = new HashMap<String, String>();
			map.put("result", RESULT + "");
			
			JSONObject json = new JSONObject(map);
			// 向用户写回数据
			out.write(json.toString());
			System.out.println("操作结果已返回...");
			out.flush();
			out.close();
		} catch (Exception e) {
			System.out.println(e.getMessage());
		}
	}
}
