package lsl.utils;

import java.io.PrintWriter;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletResponse;

import org.json.JSONObject;

/**
 * ���������û������Ϣ
 * @author M1308_000
 *
 */
public class ReturnToUser {
	
	/**
	 * �����û�������
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
			// ���û�д������
			out.write(json.toString());
			System.out.println("��������ѷ���...");
			out.flush();
			out.close();
		} catch (Exception e) {
			System.out.println(e.getMessage());
		}
	}
}
