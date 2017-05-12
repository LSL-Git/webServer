package lsl.task;

import lsl.utils.DbUtils;

public class UserTask {

	/**
	 * ��¼ҵ��
	 * @param name
	 * @param psw
	 * @return
	 */
	public static String LoginTask(String name, String psw) {
		System.out.println("LoginTask....");
		
		String RESULT = "Login_Fail";
		try {
			
			DbUtils.getInstance();
			// ��ȡ���ݿ��û���Ӧ����
			RESULT = DbUtils.Login(name);
			
			// ���������������ݿ����������ͬ
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
	 * ע��ҵ��
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
			// �����û���Ϣ
			RESULT = DbUtils.SaveUserData(name, psw, tel, "", 0, "default.png", "0%",0);
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		return RESULT;
	}
	

}
