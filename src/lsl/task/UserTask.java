package lsl.task;

import lsl.utils.DbUtils;

public class UserTask {

	/**
	 * 登录业务
	 * @param name
	 * @param psw
	 * @return
	 */
	public static String LoginTask(String name, String psw) {
		System.out.println("LoginTask....");
		
		String RESULT = "Login_Fail";
		try {
			
			DbUtils.getInstance();
			// 获取数据库用户对应密码
			RESULT = DbUtils.Login(name);
			
			// 如果请求密码和数据库检索密码相同
			if (RESULT.equals(psw)) {
				RESULT = "Login_Success";
			} else if (RESULT.equals("User_Not_Exist")) {
				RESULT = "User_Not_Exist";
			} else {
				RESULT = "Psw_Err";
			}
			
		} catch (Exception e) {
			System.err.println("LoginTask: Err->" + e.getMessage());
		}
		System.out.println("LoginTask:return:" + RESULT);
		return RESULT;
	}
	
	/**
	 * 注册业务
	 * @param name
	 * @param psw
	 * @param tel
	 * @return
	 */
	public static String RegisterTask(String name, String psw, String tel) {
		System.out.println("RegisterTask...");
		String RESULT = "Register_Fail";
		
		try {
			DbUtils.getInstance();
			// 保存用户信息
			RESULT = DbUtils.SaveUserData(name, psw, tel, "xxx@xxx.com", 0, "default", "100%",0);
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		return RESULT;
	}
	

}
