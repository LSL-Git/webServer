package lsl.utils;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import lsl.base.Base64Image;
import lsl.values.Values;

import org.json.JSONObject;

public class DbUtils {
	
	private static DbUtils instance = null;
	private static Connection connection;
	private static Statement statement;
	
	// ����ģʽ
	public static DbUtils getInstance() {
		if(null == instance) {
			instance = new DbUtils();
		}
		return instance;
	}

	/**
	 * �������ݿ�
	 * ��Ҫ���� mysql-connector-java-5.1.26-bin.jar
	 */
	private DbUtils() {
		try {
			//ָ����������
			Class.forName(Values.NAME);
			// ��ȡ����
			connection = DriverManager.getConnection(Values.URL, Values.USER, Values.PASSWORD);
			
			if(!connection.isClosed()) {
				System.out.println("Db:���ݿ����ӳɹ�...");	
			}
			statement = (Statement)connection.createStatement();
			initDb(statement);
			
		} catch (Exception e) {
			System.err.println("DB������ʧ�� ��|" + e.getMessage());
		} 
	}

	
	/**
	 * �����ʼ��
	 */
	private void initDb(Statement statement) {
		
		try {
			// �����ѱ�ǩ�������Ϣ��
			if (!statement.execute(Values.CR_FINISH_TB)) {	
				System.out.println("�ѱ�ǩ�������Ϣ�����ɹ�");
			}
		} catch (Exception e) {}
		
		try {
			// �����û���Ϣ��
			if (!statement.execute(Values.CR_USER_TB)) { 
				System.out.println("�û���Ϣ�����ɹ�");
			}
		} catch (Exception e) {}
		
/*		try {
			// Ŀ¼��һ
			if (!statement.execute(Values.CR_CLASS1_TB)) {	
				System.out.println("Ŀ¼��һ�����ɹ�");
			}

		} catch (Exception e) {}*/
		
		try {// Ŀ¼��
			if (!statement.execute(Values.CR_FOLDER_TB)) {	
				System.out.println("Ŀ¼�����ɹ�");
			}
		} catch (Exception e) {}
		
		try {
			// ����δ��ɱ�ǩ��ͼƬ��
			if (!statement.execute(Values.CR_UNFINISHED_TB)) {	
				System.out.println("δ��ɱ�ǩ�������ɹ�");
				// ��δ��ǩ����ͼƬ��Ϣ���浽���ݿ�
				FileUtils.traverseFolder2(Values.UnfinishedIconPath);
			}
		} catch (Exception e) {}
	}
	
	
	/**
	 * ��ȡδ��ǩ�����˵����Ϣ
	 * @return
	 */
	public static JSONObject GetImgJson() {
		JSONObject json = null;
		Map<String, Object> map = new HashMap<String, Object>();
		int num = 0;
		String SQL_Query_Get_Img = "select * from " + Values.UNFINISHED_TB;
		
		try {
			ResultSet re = statement.executeQuery(SQL_Query_Get_Img);
			while (re.next()) {
				map.put("id" + num, re.getString(1));
				map.put("img_name" + num, re.getString(2));
				map.put("uploader" + num, re.getString(3));
				map.put("upload_time" + num, re.getString(4));
				
				num++;
			}
			re.close();
		} catch (Exception e) {
			System.err.println("GetImgJson: Err->" + e.getMessage());
		}
		map.put("num", num);
		json = new JSONObject(map);

		return json;
	}
	
	
	/**
	 * ����ͼƬ���Ʋ�ѯͼƬ����ϸ��Ϣ,����json
	 * @param img_name
	 */
	public static JSONObject GetImgInfo(String img_name) {
		String SQL_Query_Img_Info = " select " + Values.FINISH_PARENT_NAME + ", " + Values.FINISH_LABEL + ", " + 
		Values.FINISH_TIME + " from " + Values.FINISH_TB + " where " + Values.FINISH_IMG_NAME + " = '" + img_name + "'";
		String par_name = "";
		String label = "";
		String finish_time = "";
		
		try {
			ResultSet re = statement.executeQuery(SQL_Query_Img_Info);
			while (re.next()) {
				par_name = re.getString(1);
				label = re.getString(2);
				finish_time = re.getString(3);
				map1.put("img_name", img_name);
				map1.put("label", label);
				map1.put("finish_time", finish_time);
			}
			// ��ѯ���Ŀ¼��Ϣ
			GetParentFolder(par_name,0,0);
			re.close();
		} catch (Exception e) {
			System.err.println("GetImgInfo: Err->" + e.getMessage());
		}
		JSONObject json = new JSONObject(map1);
		
		return json;
	}
	
	/**
	 * ��ѯĿ¼�������丸Ŀ¼��id
	 * @param folder_name
	 */
	private static Map<String, String> map1 = new HashMap<String, String>();
	
	public static void GetParentFolder(String folder_name, int level, int parent_id) {

		String SQL_Query_ParentFoldre = "";
		
		if (level == 0) {
			SQL_Query_ParentFoldre = "select " + Values.FOLDER_NAME + ", " + Values.FOLDER_LEVEL + ", " + Values.FOLDER_PARENT_ID 
			+ " from " + Values.FOLDER_TB + " where " + Values.FOLDER_NAME + " = '" + folder_name + "'";
			int leve = 0;
			int par_id = 0;
			String f_name = "";
			try {
					ResultSet re = statement.executeQuery(SQL_Query_ParentFoldre);
				while (re.next()) {
					f_name = re.getString(1);
					leve = re.getInt(2);
					par_id = re.getInt(3);
					map1.put("parent_folder" + leve, f_name);
				}

				if (leve >= 2) {
					GetParentFolder("", leve, par_id);
				}
				re.close();
			} catch (Exception e) {
				System.err.println("GetParentFolder: Err->" + e.getMessage());
			}
			
		} else if(level >= 1){
			SQL_Query_ParentFoldre = "select " + Values.FOLDER_NAME + ", " + Values.FOLDER_LEVEL + ", " + Values.FOLDER_PARENT_ID + " from " 
			+ Values.FOLDER_TB + " where " + Values.FOLDER_PARENT_ID + " = '" + parent_id + "' and " + Values.FOLDER_LEVEL + " = '" + level + "'";
			try {
				String f_name = "";
				int p_id = 0;
				int lv = 0;
				ResultSet re = statement.executeQuery(SQL_Query_ParentFoldre);
				while (re.next()) {
					f_name = re.getString(1);
					lv = re.getInt(2);
					p_id = re.getInt(3);
					map1.put("parent_folder" + level, f_name);
				}

				if (lv >= 2) {
					GetParentFolder("", lv - 1, p_id);
				}
				re.close();
			} catch (Exception e) {
				System.err.println("GetParentFolder: Err->" + e.getMessage());
			}
		}

	}
	
	/**
	 * ���ݸ�ĸ¼��ȡ��Ŀ¼�µ�ͼƬ�б�
	 * @param par_name
	 * @return
	 */
	public static ArrayList<String> GetInFolderInfo(String par_name) {
		ArrayList<String> lists = new ArrayList<String>();
		String SQL_Query_In_Folder_Info = "select " + Values.FINISH_IMG_NAME + " from " + Values.FINISH_TB 
		+ " where " + Values.FINISH_PARENT_NAME + " = '" + par_name + "'";
//		System.out.println(SQL_Query_In_Folder_Info);
		try {
			ResultSet re = statement.executeQuery(SQL_Query_In_Folder_Info);
			while (re.next()) {
				lists.add(re.getString(1));
			}
			re.close();
			System.out.println(lists);
		} catch (Exception e) {
			System.err.println("GetInFolderInfo: Err->" + e.getMessage());
		}
		return lists;
	}
	
	/**
	 * ����Ŀ¼����ȡ��Ŀ¼�µ����ļ������ļ��е������б�
	 * @param level
	 * @return
	 */
	public static ArrayList<String> GetFolderInfo(int level) {
		ArrayList<String> lists = new ArrayList<String>();
		String SQL_Query_Folder_Info = "select " + Values.FOLDER_NAME +"," + Values.FOLDER_NUM 
		+ " from " + Values.FOLDER_TB + " where " + Values.FOLDER_LEVEL + " = '" + level +"'";
		try {
			ResultSet re = statement.executeQuery(SQL_Query_Folder_Info);
			while (re.next()) {
				lists.add(re.getString(1));
				lists.add(re.getString(2));
			}
			System.out.println(lists);
		} catch (Exception e) {
			System.err.println("GetFolderInfo: Err->" + e.getMessage());
		}
		return lists;
	}
	
	
	/**
	 * ������ɱ�ǩ����ͼƬ�����֣���ǩ������ɱ�ǩʱ�䣬�ļ����ڸ�ĸ¼
	 * @param par_name
	 * @param img_name
	 * @param label
	 * @return
	 */
	public static boolean SaveImgInfo(String par_name, String img_name, String label) {
		int row = 0;
		// ��ȡ����ʱ�䣬����ʽ��
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH-mm-ss");
		String NowTime = sdf.format(new Date());
		
		String SQL_insert_img_info = "insert into " + Values.FINISH_TB + " values " + "(0, '"
		+ par_name + "', '" + img_name + "', '" + label + "', '" + NowTime + "')"; 
		
		try {
			row = statement.executeUpdate(SQL_insert_img_info);
			System.out.println("�Ѳ��룺" + row + "��");
		} catch (Exception e) {
			System.err.println("SaveImgInfo..." + e.getMessage());
		}
		
		return row > 0;
	}
	
	/**
	 * ����Ŀ¼�ȼ���ѯĿ¼�µ�Ŀ¼id��Ŀ¼���� ���б�
	 * @param level
	 * @return
	 */
	public static ArrayList<String> GetIdAndFileName(int level) {
		ArrayList<String> list = new ArrayList<String>();
		
		String SQL_Query_FileInfo = "select " + Values.FOLDER_PARENT_ID + "," + Values.FOLDER_NAME + " from " + Values.FOLDER_TB 
		+ " where " + Values.FOLDER_LEVEL + " = " + level;
		
		try {
			ResultSet rset = statement.executeQuery(SQL_Query_FileInfo);
			while(rset.next()) {
				list.add(rset.getString(1));
				list.add(rset.getString(2));
			}
			rset.close();
		} catch (Exception e) {
			System.err.println("GetIdAndFileName: Err->" + e.getMessage());
		}
		return list;
		
	}

	/**
	 * �����ļ������ļ���Ŀ¼����Ϣ
	 * @param level Ŀ¼����
	 * @param parent_id	Ŀ¼��id
	 * @param num	��Ŀ¼����
	 * @param folder_name	��Ŀ¼����
	 * @return
	 */
	public static boolean SaveFileInfo(int level, int parent_id, int num, String folder_name) {
		String Insert_Folder_Name = "insert into " + Values.FOLDER_TB + " values " + "(0," + level 
		+ ", " + parent_id +"," + num + ",'" + folder_name +"')";
		
		int row = 0;
		try {
			if (num > 0)
				row = statement.executeUpdate(Insert_Folder_Name);
			System.out.println("�Ѳ��룺" + row + "��");
		} catch (Exception e) {
			System.err.println("SaveFilePath..." + e.getMessage());
		}
		return row > 0;
	}
	
	/**
	 * ����δ��ǩ����ͼƬ��
	 * @param img_name
	 * @return
	 */
	public static boolean SaveUnfinishedImg(String img_name, String uploader) {
		// ��ȡ����ʱ�䣬����ʽ��
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH-mm-ss");
		String NowTime = sdf.format(new Date());
		
		String SQL_Insert_Img_Name = "insert into " + Values.UNFINISHED_TB + " values " +
				"(0,'" + img_name + "','" + uploader + "','" + NowTime + "')";
		System.out.println(SQL_Insert_Img_Name);
		int row = 0;
		try {
			row = statement.executeUpdate(SQL_Insert_Img_Name);
			System.out.println("�Ѳ��룺" + row + "��");
		} catch (Exception e) {
			System.err.println("SaveUnfinishedImg..." + e.getMessage());
		}
		return row > 0;
	}
	
	/**
	 * ����ͻ���Ϣ
	 * @param name �û���
	 * @param psw  ����
	 * @param tel  �绰����
	 * @param email ����
	 * @param integral	����
	 * @param icon_path	ͷ�񱣴�·��
	 * @param task	����������
	 * @return
	 */
	public static String SaveUserData(String name, String psw, String tel, String email, 
			int integral, String icon_name, String task, int is_manager) {
		
		String RESULT = "Register_Fail";
		// �����û���Ϣ��SQL���
		String SQL_Insert = "insert into " + Values.USER_TB + " values(0,'" + name + "','" + psw + "','" + tel + 
		"','" + email + "'," + integral + ",'" + icon_name + "','" + task + "',0,0,0)";
		
		// ��ѯ�û��Ƿ����
		String SQL_Query_Uname = "select " + Values.USER_NAME + " from " + Values.USER_TB + " where " + Values.USER_NAME + " = '" + name + "'";
		
		try {
			// �������ݿ��Ƿ���ڴ��û���
			ResultSet qname = statement.executeQuery(SQL_Query_Uname);
			String rename = "";
			
			while (qname.next()) {
				rename = qname.getString(1);
			}
			
			if (rename.equals(name)) {
				RESULT = "User_Name_Exist";
			} else {
				// �����û���Ϣ
				int row = statement.executeUpdate(SQL_Insert);
				System.out.println("�Ѹ��£�" + row + "��");
				RESULT = "Register_Success";
			}

			qname.close();
			
		} catch (Exception e) {
			System.err.println("SaveUserData: Err->" + e.getMessage());
		}
		
		return RESULT;
	}
	
	/**
	 * ���û�����ѯ�û����롣������
	 * @param name
	 * @return
	 */
	public static String Login(String name) {
		System.out.println("Login....");
		String RESULT = "User_Not_Exist";
		// ��ѯ�����SQL���
		String SQL_Query = "select " + Values.USER_PSW + " from " + 
		Values.USER_TB +" where " + Values.USER_NAME + " = '" + name +"'";
		
		try {
			ResultSet ResultStr = statement.executeQuery(SQL_Query);
			while (ResultStr.next()) {
				// ��ȡ�û�����
				RESULT = ResultStr.getString(1);
			}
			
			ResultStr.close();
			
		} catch (SQLException e) {
			System.err.println("Login: Err->" + e.getMessage());
		}
		System.out.println("Login:return:" + RESULT);
		return RESULT;
	}
	
	/**
	 * ͨ���û�����ѯ�û�������Ϣ,����json�����û�����Ա������
	 * ��ѯ���ݰ������û��������룬�绰���룬���䣬���֣�������������ͷ��
	 * @param name
	 * @return
	 */
	public static JSONObject QueryUserAll(String name) {
		
		String SQL_Query_All = "select * from " + Values.USER_TB + " where " + Values.USER_NAME + " = '" + name + "'";
		// ����ͷ��ͼƬ·��
		String ImgPath = Values.UserIconPath; 
		Map<String, String> map = new HashMap<String, String>();
		JSONObject json = null;
		
		try {
			ResultSet ResultStr = statement.executeQuery(SQL_Query_All);
			while (ResultStr.next()) {
				map.put(Values.USER_ID, ResultStr.getString(1));
				map.put(Values.USER_NAME, ResultStr.getString(2));
				map.put(Values.USER_PSW, ResultStr.getString(3));				
				map.put(Values.USER_TEL, ResultStr.getString(4));
				map.put(Values.USER_EMAIL, ResultStr.getString(5));
				map.put(Values.USER_INTEGRAL, ResultStr.getString(6));
				// ��ȡͷ���ļ�
				String icon_name = ResultStr.getString(7);
				ImgPath += icon_name;
				// ����ͷ���ļ�
				String Img = Base64Image.getImageStr(ImgPath);
				map.put(Values.USER_ICON, icon_name);
				map.put("icon_code",Img);				
				map.put(Values.USER_TASK_COMPLETION, ResultStr.getString(8));
				map.put(Values.IS_MANAGER, ResultStr.getString(9));
				map.put(Values.USER_LABEL_ALL_NUM, ResultStr.getString(10));
				map.put(Values.USER_LABEL_SUCCESS_NUM, ResultStr.getString(11));

			}
			
			ResultStr.close();	
			
		} catch (Exception e) {
			System.err.println("QueryUserAll: Err->" + e.getMessage());
		}
		
		// ���û���Ϣ��װ��json������
		json = new JSONObject(map);
		
		// ����json����
		return json;
	}

	/**
	 * �����û���Ϣ���������롣�绰������
	 * @param name
	 * @param psw
	 * @param tel
	 * @param email
	 * @return
	 */
	public static String UpDateUserInfo(String name, String psw, String tel, String email) {
		String RESULT = "Update_Fail";
		
		String SQL_Update = "update " + Values.USER_TB + " set " + Values.USER_PSW + " = '" + psw
		+ "', " + Values.USER_TEL + " = '" + tel + "', " + Values.USER_EMAIL + " = '" + email
		+ "' where " + Values.USER_NAME + " = '" + name + "'";
		
		try {
			int row = statement.executeUpdate(SQL_Update);
			System.out.println("�����ˣ�" + row + "��");
			if (row > 0) {
				RESULT = "Update_Success";
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
		return RESULT;
	}

}
