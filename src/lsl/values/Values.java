package lsl.values;

public class Values {
	public static final String URL = "jdbc:mysql://127.0.0.1/resource_management_system?characterEncoding=utf8&autoReconnect=true";
	public static final String NAME = "com.mysql.jdbc.Driver";
	public static final String USER = "root";
	public static final String PASSWORD = "root";
	// 用户头像存放路径
	public static final String UserIconPath = "E:/Images/用户头像/User/"; 
	public static final String UnfinishedIconPath = "E:/Images/未完成";
	public static final String FINISHICONPATH = "E:/Images/已完成";
	
	/**
	 * 本地服务器地址
	 */
	public final static String httpURL = "http://192.168.43.224:8088/webServer/LoadImage/Images/未完成/"; 
	/**
	 * 云服务器地址
	 */
//	public final static String httpURL = "http://193.112.17.51:8080/webServer/LoadImage/Images/未完成/"; 
	
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
	
	/***已完成标签化图片的存放目录表***/
	public static final String CR_TA_FILE = "CREATE TABLE `tb_file` (" +
			"`file_id`  int NOT NULL AUTO_INCREMENT ," +
			"`level`  int NOT NULL ," +
			"`parent_name`  varchar(255) CHARACTER SET utf8 NULL ," +
			"`file_name`  varchar(255) CHARACTER SET utf8 NOT NULL ," +
			"`num`  int NOT NULL ,UNIQUE(file_name)," +
			"PRIMARY KEY (`file_id`))";
	public static final String TB_FILE = "tb_file";
	public static final String FILE_ID = "file_id";
	public static final String LEVEL = "level";
	public static final String PARENT_NAME = "parent_name";
	public static final String FILE_NAME = "file_name";
	public static final String NUM = "num";
	/***已完成标签化图片的存放目录表***/
	
	
	/***已完成标签化图片的标签信息***/
	public static final String CR_TB_FINISH_PIC = "CREATE TABLE `tb_finish_pic` (" +
			"`pic_id`  int NOT NULL AUTO_INCREMENT ," +
			"`parent_file`  varchar(255) CHARACTER SET utf8 NOT NULL ," +
			"`pic_name`  varchar(255) CHARACTER SET utf8 NOT NULL ," +
			"`pic_label`  varchar(255) CHARACTER SET utf8 NULL ," +
			"`finish_label_time`  datetime NOT NULL ," +
			"PRIMARY KEY (`pic_id`),CONSTRAINT `parent_file` FOREIGN KEY (`parent_file`) REFERENCES `tb_file` (`file_name`) " +
			"ON DELETE CASCADE " +
			"ON UPDATE CASCADE)";
	public static final String TB_FINISH_PIC = "tb_finish_pic";
	public static final String PIC_ID = "pic_id";
	public static final String PARENT_FILE = "parent_file";
	public static final String PIC_NAME = "pic_name";
	public static final String PIC_LABEL = "pic_label";
	public static final String FINISH_LABEL_TIME = "finish_label_time";
	/***已完成标签化图片的标签信息***/
	
	
	/**未完成标签化图片的信息*/
	public static final String CR_TB_UNFINISH_PIC = "CREATE TABLE `tb_unfinished_pic` (" +
			"`id`  int NOT NULL AUTO_INCREMENT ," +
			"`pic_name`  varchar(255) CHARACTER SET utf8 NOT NULL ," +
			"`pic_state`  int NOT NULL DEFAULT 0 ," +
			"`upload_time`  datetime NOT NULL ," +
			"UNIQUE(pic_name)," +
			"PRIMARY KEY (`id`))";
	public static final String TB_UNFINISH_PIC = "tb_unfinished_pic";
	public static final String UNFINISH_PIC_ID = "id";
	public static final String UNFINISH_PIC_NAME = "pic_name";
	public static final String UNFINIHS_PIC_STATE = "pic_state";
	public static final String UNFINISH_UPLOAD_TIME = "upload_time";
	/**未完成标签化图片的信息*/
	/**正在进行标签化图片的信息*/
	public static final String CR_TB_IN_PROGESS = "CREATE TABLE `tb_in_progress` (" +
			"`id`  int NOT NULL AUTO_INCREMENT ," +
			"`pic_name`  varchar(255) CHARACTER SET utf8 NOT NULL ," +
			"`volunteer_name`  varchar(255) CHARACTER SET utf8 NOT NULL ," + 
			"`progress_state` int NOT NULL DEFAULT 0," +
			"`label_time`  datetime NOT NULL ," +
			"`label1`  varchar(255) CHARACTER SET utf8 NOT NULL ," +
			"`label2`  varchar(255) CHARACTER SET utf8 NULL ," +
			"`label3`  varchar(255) CHARACTER SET utf8 NULL ," +
			"`label4`  varchar(255) CHARACTER SET utf8 NULL ," +
			"`label5`  varchar(255) CHARACTER SET utf8 NULL ," +
			"`label6`  varchar(255) CHARACTER SET utf8 NULL ," +
			"`label7`  varchar(255) CHARACTER SET utf8 NULL ," +
			"`label8`  varchar(255) CHARACTER SET utf8 NULL ," +
			"`label9`  varchar(255) CHARACTER SET utf8 NULL ," +
			"`label10`  varchar(255) CHARACTER SET utf8 NULL ," +
			"PRIMARY KEY (`id`)," +
			"CONSTRAINT `pic_name` FOREIGN KEY (`pic_name`) REFERENCES `" 
			+ TB_UNFINISH_PIC + "` (`pic_name`) " +
			"ON DELETE CASCADE " +
			"ON UPDATE CASCADE)";
	public static final String TB_IN_PROGESS = "tb_in_progress";
	public static final String PRO_ID = "id";
	public static final String PRO_PIC_NAME = "pic_name";
	public static final String PRO_VOLUNTEER_NAME = "volunteer_name";
	public static final String PRO_PRO_STATE = "progress_state";
	public static final String PRO_LABEL_TIME = "label_time";
	public static final String PRO_LABEL1 = "label1";
	public static final String PRO_LABEL2 = "label2";
	public static final String PRO_LABEL3 = "label3";
	public static final String PRO_LABEL4 = "label4";
	public static final String PRO_LABEL5 = "label5";
	public static final String PRO_LABEL6 = "label6";
	public static final String PRO_LABEL7 = "label7";
	public static final String PRO_LABEL8 = "label8";
	public static final String PRO_LABEL9 = "label9";
	public static final String PRO_LABEL10 = "label10";
	/**正在进行标签化图片的信息*/
	
	/**标签相关设置表*/
	public static final String CR_TB_PICSET = "CREATE TABLE `tb_picSet` (" +
			"`id`  int NOT NULL AUTO_INCREMENT ," +
			"`picLabelRelyOnNum`  int(11) NOT NULL DEFAULT 10 ," +
			"`taskNum`  int(11) NOT NULL DEFAULT 15 ," +
			"`managerName`  varchar(255) CHARACTER SET utf8 NOT NULL DEFAULT 'system' ," +
			"`alterTime`  datetime NOT NULL ," +
			"PRIMARY KEY (`id`))";
	public static final String TB_PICSET = "tb_picSet";
	public static final String SET_ID = "id";
	public static final String SET_RELYNUM = "picLabelRelyOnNum";
	public static final String SET_TASKNUM = "taskNum";
	public static final String SET_MNAME = "managerName";
	public static final String SET_ALTERTIME = "alterTime";
	/**标签相关设置表*/
	
	/**批次管理表*/
	public static final String CR_TB_BATCHMANAGE = "CREATE TABLE `tb_BatchManage` (" +
			"`id`  int(11) NOT NULL AUTO_INCREMENT ," +
			"`Batch`  int(11) unsigned NOT NULL ," +
			"`batchState`  int(11) NOT NULL ," +
			"`relyOnNum`  int(11) NOT NULL ," +
			"`taskNum`  int(11) NOT NULL ," +
			"`assignedNum`  int(11) NOT NULL ," +
			"`createNOTime`  datetime NOT NULL ," +
			"PRIMARY KEY (`id`))";
	
	public static final String TB_BATCHMANAGE = "tb_BatchManage";
	public static final String NOM_ID = "id";
	public static final String NOM_BATCH = "Batch";
	public static final String NOM_BATCHSTATE = "batchState";
	public static final String NOM_RELYNUM = "relyOnNum";
	public static final String NOM_TASKNUM = "taskNum";
	public static final String NOM_ASSNUM = "assignedNum";
	public static final String NOM_CREATETIME = "createNOTime";
	/**批次管理表*/

	/**批次图片 每批次包含的所有图片*/
	public static final String CR_TB_BATCHPICS = "CREATE TABLE `tb_BatchPics` (" +
			"`id`  int(11) NOT NULL AUTO_INCREMENT ," +
			"`picName`  varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL ," +
			"`onBatch`  int(11) NOT NULL ," +
			"`result`  int(11) NOT NULL ," +
			"PRIMARY KEY (`id`)," +
			"CONSTRAINT `picName` FOREIGN KEY (`picName`) REFERENCES `" + TB_UNFINISH_PIC + "` (`pic_name`))";
	public static final String TB_BATCHPICS = "tb_BatchPics";
	public static final String BP_ID = "id";
	public static final String BP_PICNAME = "picName";
	public static final String BP_ONBATCH = "onBatch";
	public static final String BP_RESULT = "result";
	/**批次图片*/
	
	/**获取批次用户信息表*/
	public static final String CR_TB_GETBATCHUSER = "CREATE TABLE `tb_getbatchuser` (" +
			"`id`  int NOT NULL AUTO_INCREMENT ," +
			"`getBatch`  int NOT NULL ," +
			"`userName`  varchar(255) CHARACTER SET utf8 NOT NULL ," +
			"`getbatchTime`  datetime NOT NULL ," +
			"PRIMARY KEY (`id`))";
	public static final String TB_GETBATCHUSER = "tb_getbatchuser";
	public static final String ID = "id";
	public static final String GET_BATCH = "getBatch";
	public static final String GET_USERNAME = "userName";
	public static final String GET_TIME = "getbatchTime";
	/**获取批次用户信息表*/
}
