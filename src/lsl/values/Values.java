package lsl.values;

public class Values {
	public static final String URL = "jdbc:mysql://127.0.0.1/resource_management_system?characterEncoding=utf8";
	public static final String NAME = "com.mysql.jdbc.Driver";
	public static final String USER = "root";
	public static final String PASSWORD = "123";
	// 用户头像存放路径
	public static final String UserIconPath = "D:/Images/用户头像/User/"; 
	public static final String UnfinishedIconPath = "D:/Images/未完成";
	public static final String FINISHICONPATH = "D:/Images/已完成";
	
	public final static String httpURL = "http://127.0.0.1/webServer/servlet/images?type=get&name=";
	
	// *******************用户信息*************************
	public static final String CR_USER_TB = "CREATE TABLE `user_tb` (" +
			"`user_id`  int NOT NULL AUTO_INCREMENT ," +
			"`user_name`  varchar(255) CHARACTER SET utf8 NOT NULL ," +
			"`user_psw`  varchar(255) CHARACTER SET utf8 NOT NULL ," +
			"`user_tel`  varchar(12) NOT NULL ," +
			"`user_email`  varchar(255) CHARACTER SET utf8 NULL ," +
			"`user_integral`  int NOT NULL DEFAULT 0 ," +
			"`icon_name`  varchar(255) CHARACTER SET utf8 NOT NULL DEFAULT 'default.png' ," +
			"`task_completion`  varchar(255) CHARACTER SET utf8 NOT NULL DEFAULT '' ," +
			"`is_manager`  int NOT NULL DEFAULT 0 ," +
			"`label_all_num`  int NOT NULL DEFAULT 0 ," +
			"`label_success_num`  int NOT NULL DEFAULT 0 ," +
			"PRIMARY KEY (`user_id`))";
	
	public static final String USER_ID = "user_id";		// 用户id
	public static final String USER_NAME = "user_name";	// 用户名
	public static final String USER_PSW = "user_psw";	// 用户密码
	public static final String USER_TEL = "user_tel";	// 用户电话号码
	public static final String USER_EMAIL = "user_email";	// 用户邮箱
	public static final String USER_INTEGRAL = "user_integral";	// 用户积分
	public static final String USER_ICON = "icon_name";		// 用户头像名称
	public static final String USER_TASK_COMPLETION = "task_completion";	// 用户任务完成情况
	public static final String IS_MANAGER = "is_manager";	// 是否为管理员
	public static final String USER_LABEL_ALL_NUM = "label_all_num";	// 总标签数
	public static final String USER_LABEL_SUCCESS_NUM = "label_success_num";	// 标签成功数
	public static final String USER_TB = "user_tb";
	// *******************用户信息*************************
	
/*	// 管理员信息
	public static final String CR_MANAGER_TB = "create table manager_tb(manager_id int auto_increment primary key," +
																"manager_name varchar(10) not null," +
																"manager_psw varchar(10) not null," +
																"manager_tel int not null," +
																"manager_mail text," +
																"icon_name varchar(40))";*/
	
	
	// *********************已标签化完成**************************************
	public static final String CR_FINISH_TB = "CREATE TABLE `finish_tb` (" +
			"`id`  int NOT NULL AUTO_INCREMENT ," +
			"`parent_name`  varchar(255) NOT NULL ," +
			"`img_name`  varchar(255) NOT NULL ," +
			"`label`  varchar(255) NOT NULL ," +
			"`finish_time`  varchar(255) NOT NULL ," +
			"PRIMARY KEY (`id`), UNIQUE(img_name)," +
			"CONSTRAINT `par_name` FOREIGN KEY (`parent_name`) REFERENCES " +
			"`folder_tb` (`folder_name`) ON UPDATE CASCADE)";
	// 字段
	public static final String FINISH_ID = "id";
	public static final String FINISH_PARENT_NAME = "parent_name";
	public static final String FINISH_IMG_NAME = "img_name";
	public static final String FINISH_LABEL = "label";
	public static final String FINISH_TIME = "finish_time";
	public static final String FINISH_TB = "finish_tb";
	
	// *********************已标签化完成**************************************

/*	// 第一层分类
	public static final String CR_CLASS1_TB = "create table class1_tb(" +
											"id int auto_increment primary key," +
											"class1_name varchar(50) not null," +
											"unique(class1_name))";
	public static final String CLASS1_TB = "class1_tb";
	public static final String CLASS1_ID = "id";
	public static final String CLASS1_NAME = "class1_name";
	
	// 第二层分类
	public static final String CR_CLASS2_TB = "create table class2_tb(" +
											"id int auto_increment not null," +
											"parent_id int, " +
											"class_name varchar(55), " +
											"index par_ind(parent_id), " +
											"foreign key(parent_id) references class1_tb(id) " +
											"on delete cascade, " +
											"primary key(id), " +
											"unique(class_name))";
	public static final String CLASS2_TB = "class2_tb";
	public static final String CLASS2_ID = "id";
//	public static final String PARENT_ID = "parent_id";
	public static final String CLASS2_NAME = "class2_name";*/
	//
	
	public static final String CR_FOLDER_TB = "CREATE TABLE folder_tb(" +
											"id int NOT NULL AUTO_INCREMENT ," +
											"level int NOT NULL ," +
											"parent_id int NOT NULL ," +
											"num int," +
											"folder_name varchar(255) NOT NULL ," +
											"PRIMARY KEY (id)," +
											"UNIQUE(folder_name))";
	public static final String FOLDER_LEVEL = "level";
	public static final String FOLDER_PARENT_ID = "parent_id";
	public static final String FOLDER_NUM = "num";
	public static final String FOLDER_NAME = "folder_name";
	public static final String FOLDER_TB = "folder_tb";
	
	
	// 未完成标签化
	public static final String CR_UNFINISHED_TB = "create table unfinished_tb(" +
												"id int auto_increment primary key," +
												"img_name varchar(255) not null," +
												"uploader varchar(50) not null," +
												"upload_time varchar(255) not null)";
	
	public static final String UNFINISHED_TB = "unfinished_tb";
	public static final String IMG_NAME = "img_name";
	public static final String UPLOADER = "uploader";
	public static final String UPLOAD_TIME = "upload_time";
	

	
	
}

