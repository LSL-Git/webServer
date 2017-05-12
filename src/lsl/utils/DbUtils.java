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
	
	// 单例模式
	public static DbUtils getInstance() {
		if(null == instance) {
			instance = new DbUtils();
		}
		return instance;
	}

	/**
	 * 链接数据库
	 * 需要配置 mysql-connector-java-5.1.26-bin.jar
	 */
	private DbUtils() {
		try {
			//指定链接类型
			Class.forName(Values.NAME);
			// 获取连接
			connection = DriverManager.getConnection(Values.URL, Values.USER, Values.PASSWORD);
			
			if(!connection.isClosed()) {
				System.out.println("Db:数据库链接成功...");	
			}
			statement = (Statement)connection.createStatement();
			initDb(statement);
			
		} catch (Exception e) {
			System.err.println("DB：链接失败 ！|" + e.getMessage());
		} 
	}

	
	/**
	 * 建表初始化
	 */
	private void initDb(Statement statement) {
		
		try {
			// 创建已标签话完成信息表
			if (!statement.execute(Values.CR_FINISH_TB)) {	
				System.out.println("已标签话完成信息表创建成功");
			}
		} catch (Exception e) {}
		
		try {
			// 创建用户信息表
			if (!statement.execute(Values.CR_USER_TB)) { 
				System.out.println("用户信息表创建成功");
			}
		} catch (Exception e) {}
		
/*		try {
			// 目录表一
			if (!statement.execute(Values.CR_CLASS1_TB)) {	
				System.out.println("目录表一创建成功");
			}

		} catch (Exception e) {}*/
		
		try {// 目录表
			if (!statement.execute(Values.CR_FOLDER_TB)) {	
				System.out.println("目录表创建成功");
			}
		} catch (Exception e) {}
		
		try {
			// 创建未完成标签化图片表
			if (!statement.execute(Values.CR_UNFINISHED_TB)) {	
				System.out.println("未完成标签化表创建成功");
				// 将未标签化的图片信息保存到数据库
				FileUtils.traverseFolder2(Values.UnfinishedIconPath);
			}
		} catch (Exception e) {}
	}
	
	
	/**
	 * 获取未标签化表的说有信息
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
	 * 根据图片名称查询图片的详细信息,返回json
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
			// 查询相关目录信息
			GetParentFolder(par_name,0,0);
			re.close();
		} catch (Exception e) {
			System.err.println("GetImgInfo: Err->" + e.getMessage());
		}
		JSONObject json = new JSONObject(map1);
		
		return json;
	}
	
	/**
	 * 查询目录级，跟其父目录的id
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
	 * 根据父母录获取该目录下的图片列表
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
	 * 根据目录级或取该目录下的子文件或子文件夹的数量列表
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
	 * 保存完成标签化的图片的名字，标签名，完成标签时间，文件所在父母录
	 * @param par_name
	 * @param img_name
	 * @param label
	 * @return
	 */
	public static boolean SaveImgInfo(String par_name, String img_name, String label) {
		int row = 0;
		// 或取当地时间，并格式化
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH-mm-ss");
		String NowTime = sdf.format(new Date());
		
		String SQL_insert_img_info = "insert into " + Values.FINISH_TB + " values " + "(0, '"
		+ par_name + "', '" + img_name + "', '" + label + "', '" + NowTime + "')"; 
		
		try {
			row = statement.executeUpdate(SQL_insert_img_info);
			System.out.println("已插入：" + row + "条");
		} catch (Exception e) {
			System.err.println("SaveImgInfo..." + e.getMessage());
		}
		
		return row > 0;
	}
	
	/**
	 * 根据目录等级查询目录下的目录id和目录名称 的列表
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
	 * 保存文件所在文件夹目录的信息
	 * @param level 目录级别
	 * @param parent_id	目录父id
	 * @param num	子目录个数
	 * @param folder_name	子目录名称
	 * @return
	 */
	public static boolean SaveFileInfo(int level, int parent_id, int num, String folder_name) {
		String Insert_Folder_Name = "insert into " + Values.FOLDER_TB + " values " + "(0," + level 
		+ ", " + parent_id +"," + num + ",'" + folder_name +"')";
		
		int row = 0;
		try {
			if (num > 0)
				row = statement.executeUpdate(Insert_Folder_Name);
			System.out.println("已插入：" + row + "条");
		} catch (Exception e) {
			System.err.println("SaveFilePath..." + e.getMessage());
		}
		return row > 0;
	}
	
	/**
	 * 保存未标签化的图片名
	 * @param img_name
	 * @return
	 */
	public static boolean SaveUnfinishedImg(String img_name, String uploader) {
		// 或取当地时间，并格式化
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH-mm-ss");
		String NowTime = sdf.format(new Date());
		
		String SQL_Insert_Img_Name = "insert into " + Values.UNFINISHED_TB + " values " +
				"(0,'" + img_name + "','" + uploader + "','" + NowTime + "')";
		System.out.println(SQL_Insert_Img_Name);
		int row = 0;
		try {
			row = statement.executeUpdate(SQL_Insert_Img_Name);
			System.out.println("已插入：" + row + "条");
		} catch (Exception e) {
			System.err.println("SaveUnfinishedImg..." + e.getMessage());
		}
		return row > 0;
	}
	
	/**
	 * 保存客户信息
	 * @param name 用户名
	 * @param psw  密码
	 * @param tel  电话号码
	 * @param email 邮箱
	 * @param integral	积分
	 * @param icon_path	头像保存路径
	 * @param task	任务完成情况
	 * @return
	 */
	public static String SaveUserData(String name, String psw, String tel, String email, 
			int integral, String icon_name, String task, int is_manager) {
		
		String RESULT = "Register_Fail";
		// 插入用户信息的SQL语句
		String SQL_Insert = "insert into " + Values.USER_TB + " values(0,'" + name + "','" + psw + "','" + tel + 
		"','" + email + "'," + integral + ",'" + icon_name + "','" + task + "',0,0,0)";
		
		// 查询用户是否存在
		String SQL_Query_Uname = "select " + Values.USER_NAME + " from " + Values.USER_TB + " where " + Values.USER_NAME + " = '" + name + "'";
		
		try {
			// 检索数据库是否存在此用户名
			ResultSet qname = statement.executeQuery(SQL_Query_Uname);
			String rename = "";
			
			while (qname.next()) {
				rename = qname.getString(1);
			}
			
			if (rename.equals(name)) {
				RESULT = "User_Name_Exist";
			} else {
				// 保存用户信息
				int row = statement.executeUpdate(SQL_Insert);
				System.out.println("已更新：" + row + "条");
				RESULT = "Register_Success";
			}

			qname.close();
			
		} catch (Exception e) {
			System.err.println("SaveUserData: Err->" + e.getMessage());
		}
		
		return RESULT;
	}
	
	/**
	 * 以用户名查询用户密码。并返回
	 * @param name
	 * @return
	 */
	public static String Login(String name) {
		System.out.println("Login....");
		String RESULT = "User_Not_Exist";
		// 查询密码的SQL语句
		String SQL_Query = "select " + Values.USER_PSW + " from " + 
		Values.USER_TB +" where " + Values.USER_NAME + " = '" + name +"'";
		
		try {
			ResultSet ResultStr = statement.executeQuery(SQL_Query);
			while (ResultStr.next()) {
				// 获取用户密码
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
	 * 通过用户名查询用户所有信息,返回json对象，用户管理员都可用
	 * 查询内容包括，用户名，密码，电话号码，邮箱，积分，任务完成情况，头像
	 * @param name
	 * @return
	 */
	public static JSONObject QueryUserAll(String name) {
		
		String SQL_Query_All = "select * from " + Values.USER_TB + " where " + Values.USER_NAME + " = '" + name + "'";
		// 本地头像图片路径
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
				// 获取头像文件
				String icon_name = ResultStr.getString(7);
				ImgPath += icon_name;
				// 加密头像文件
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
		
		// 将用户信息封装到json对象中
		json = new JSONObject(map);
		
		// 返回json对象
		return json;
	}

	/**
	 * 更新用户信息。包括密码。电话，邮箱
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
			System.out.println("更新了：" + row + "条");
			if (row > 0) {
				RESULT = "Update_Success";
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
		return RESULT;
	}

}
