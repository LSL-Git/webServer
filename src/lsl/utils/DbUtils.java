package lsl.utils;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
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
		System.out.println("initDb...");
		try {
			// 创建用户信息表
			if (!statement.execute(Values.CR_USER_TB)) { 
				System.out.println("用户信息表创建成功");
			}
		} catch (Exception e) {}
		
		try {
			if (!statement.execute(Values.CR_TA_FILE)) {	
				System.out.println(Values.TB_FILE + " create success!!!");
			}

		} catch (Exception e) {}
		
		try {
			if (!statement.execute(Values.CR_TB_FINISH_PIC)) {
				ArrayList<String> lists = FileUtils.tFolder(Values.FINISHICONPATH,1);
				FileUtils.f(lists,1);
				System.out.println(Values.TB_FINISH_PIC + " create success!!!");
			}

		} catch (Exception e) {}
		
		try {// 创建未完成标签化图片表
			if (!statement.execute(Values.CR_TB_UNFINISH_PIC)) {	
				FileUtils.TraverseUnfinishPicFolder(Values.UnfinishedIconPath);
				System.out.println(Values.TB_UNFINISH_PIC + " create success!!!");
			}
		} catch (Exception e) {}


		try {
			if (!statement.execute(Values.CR_TB_IN_PROGESS)) {
				System.out.println(Values.TB_IN_PROGESS + " create success!!!");
			}
		} catch (Exception e) {}
		
		try {
			if (!statement.execute(Values.CR_TB_PICSET)) {
				SaveSetInfo(10, 15, "system", DateUtils.GetNowTime());
				System.out.println(Values.TB_PICSET + " create success!!!");
			}
		} catch (Exception e) {}
		
		try {
			if (!statement.execute(Values.CR_TB_BATCHMANAGE)) {
				System.out.println(Values.TB_BATCHMANAGE + " create success!!!");
			}
		} catch (Exception e) {}
		
		try {
			if (!statement.execute(Values.CR_TB_BATCHPICS)) {
				System.out.println(Values.TB_BATCHPICS + " create success!!!");
			}
		} catch (Exception e) {}

		try {
			if (!statement.execute(Values.CR_TB_GETBATCHUSER)) {
				System.out.println(Values.TB_GETBATCHUSER + " create success!!!");
			}
		} catch (Exception e) {}
	}
	
	
	public boolean addFile(int level, String parentName, String fileName) {
		System.out.println("addFile...");
		String sql = "select * from " + Values.TB_FILE + " where file_name = '" + fileName + "'";
		boolean flag = false;
		try {
			ResultSet rs = statement.executeQuery(sql);
			if (rs.next()) {
				ResultSet rs2 = statement.executeQuery("select num from " + Values.TB_FILE + " where file_name = '" + fileName + "'");
				int num = 0; 
				int row = 0;
				if (rs2.next()) {
					num = rs2.getInt(1);
					num++;
					row = statement.executeUpdate("update " + Values.TB_FILE + " set num = " + num + " where file_name = '" + fileName + "'");
				}
				System.out.println("更新" + row + "条记录");
				flag = row > 0;
			} else {
				flag = SaveFile(level, parentName, fileName, 1);
//				System.out.println("没有记录");
			}
			rs.close();
		} catch (Exception e) {
			System.out.println("addFile Err..." + e.getMessage());
		}
		
		return flag;
	}
	
	
	/**
	 * 图片标签结果判定
	 * @param picName
	 * @return
	 */
	public String getLabelJudge(String picName) {
		System.out.println("getAllLabels...");
		//DICT为词典，MAX_LENGTH为词典中的最大词长
	    final HashSet<String> DICT = new HashSet<String>();
	    int MAX_LENGTH = 0;
	    float laNum = 0f;
	    String text = "";
		String laLevel = "";
		
		try {
			ResultSet rs = statement
				.executeQuery("select * from " + Values.TB_IN_PROGESS + " where pic_name = '" + picName + "';");
			while(rs.next()) {
				for (int i = 6; i <= 15; i++) {
					if(!rs.getString(i).equals("")) {
						int max = 1;
						DICT.add(rs.getString(i));
						text += rs.getString(i);
						laNum++;
						for (String word : DICT) {
							if (max < word.length()) {
								max = word.length();
							}
						}
						MAX_LENGTH = max;
					}
				}
			}
			rs.close();
		} catch (Exception e) {
			e.printStackTrace();
			System.out.println("getAllLabels Err..." + e.getMessage());
		}
//		System.out.println("词典：" + DICT);
//		System.out.println("最大词长：" + MAX_LENGTH);
//		System.out.println("text:" + text);
//		System.out.println("总标签数：" + laNum);
		
		List<Map.Entry<String,Integer>> list = WordCountAndSort.getWordCountAndSort(MAX_LENGTH, DICT, text);
		for (int i = list.size() - 1; i >= 0; i--) {
			int count = list.get(i).getValue();
			String laName = list.get(i).getKey();
			if (count / laNum >= 0.6) {
				System.out.println(laName + ",认同率：" + count/laNum);
				laLevel += "/" + laName ;
			}
//			System.out.println(list.get(i).getKey() + ":" + list.get(i).getValue());
		}
		System.out.println(picName + ",判定结果：" + laLevel);
		return laLevel;
	}
	

	/**
	 * 获取图片判定依赖数
	 * @return
	 */
	public int getRelyOnNum() {
		System.out.println("getRelyOnNum...");
		int num = 0;
		try {
			ResultSet rs = statement
				.executeQuery("select picLabelRelyOnNum , id from " + Values.TB_PICSET + " order by id desc limit 1");
			while(rs.next()) {
				num = rs.getInt(1);
			}
			rs.close();
		} catch (Exception e) {
			System.out.println("getRelyOnNum..." + e.getMessage());
		}
		return num;
	}
	
	/**
	 * 根据表名跟字段名查询该字段数量
	 * @param picName
	 * @return
	 */
	public int getCountByPicName(String tbName, String columnName) {
		System.out.println("getCountByPicName...");
		int count = 0;
		try {
			ResultSet rs = statement
				.executeQuery("select count(*) from " + tbName + " where pic_name='" + columnName + "'");
			while(rs.next()) {
				count = rs.getInt(1);
			}
			rs.close();
		}catch (Exception e) {
			System.out.println("getCountByPicName..." + e.getMessage());
		}
		return count;
	}

	
	/**
	 * 更新标签信息
	 * @param picName
	 * @param vName
	 * @param state
	 * @param putTime
	 * @param La1
	 * @param La2
	 * @param La3
	 * @param La4
	 * @param La5
	 * @param La6
	 * @param La7
	 * @param La8
	 * @param La9
	 * @param La10
	 * @return
	 */
	public static int UpdateLabelsInfo(String picName, String vName, int state, String putTime, String La1, String La2,
			String La3, String La4, String La5, String La6, String La7, String La8, String La9, String La10) {
		System.out.println("UpdateLabelsInfo...");
		int row = 0;
		String SQL_Update_Labels_Info = "update " + Values.TB_IN_PROGESS + " set " + Values.PRO_LABEL_TIME +
				" = '" + putTime + "'," + Values.PRO_LABEL1 + " = '" + La1 + "'," + Values.PRO_LABEL2 + 
				" = '" + La2 + "'," + Values.PRO_LABEL3 + " = '" + La3 + "'," + Values.PRO_LABEL4 + 
				" = '" + La4 + "'," + Values.PRO_LABEL5 + " = '" + La5 + "'," + Values.PRO_LABEL6 + 
				" = '" + La6 + "'," + Values.PRO_LABEL7 + " = '" + La7 + "'," + Values.PRO_LABEL8 + 
				" = '" + La8 + "'," + Values.PRO_LABEL9 + " = '" + La9 + "'," + Values.PRO_LABEL10 + 
				" = '" + La10 + "' where " + Values.PRO_PIC_NAME + " = '" + picName + "' and " 
				+ Values.PRO_VOLUNTEER_NAME + " = '" + vName + "'";
//		System.out.println(SQL_Update_Labels_Info);
		try {
			row = statement.executeUpdate(SQL_Update_Labels_Info);
		} catch (Exception e) {
			System.out.println("UpdateLabelsInfo..." + e.getMessage());
		}
		return row;
	}
	
	/**
	 * 保存标签信息
	 * @param picName
	 * @param vName
	 * @param state
	 * @param putTime
	 * @param La1
	 * @param La2
	 * @param La3
	 * @param La4
	 * @param La5
	 * @param La6
	 * @param La7
	 * @param La8
	 * @param La9
	 * @param La10
	 * @return
	 */
	public static boolean SaveLabels(String picName, String vName, int state, String putTime, String La1, String La2,
			String La3, String La4, String La5, String La6, String La7, String La8, String La9, String La10) {
		System.out.println("SaveLabels...");
		String SQL_Query_Same = "select * from " + Values.TB_IN_PROGESS + " where " + Values.PRO_PIC_NAME + " = '"
				+ picName + "' and " + Values.PRO_VOLUNTEER_NAME + " = '" + vName + "'";
		
		String SQL_Insert_Labels = "insert into " + Values.TB_IN_PROGESS + " values (0,'" + picName + "','" + vName 
		+ "'," + state + ",'" + putTime + "','" + La1 + "','" + La2 + "','" + La3 + "','" + La4 + "','" + La5 + "','"
		 + La6 + "','" + La7 + "','" + La8 + "','" + La9 + "','" + La10 + "')";
		System.out.println(SQL_Insert_Labels);
		System.out.println(SQL_Query_Same);
		int row = 0;
		try {
			ResultSet rs = statement.executeQuery(SQL_Query_Same);
			if (!rs.next()) {
				row = statement.executeUpdate(SQL_Insert_Labels);
			} else {
				row = UpdateLabelsInfo(picName, vName, state, putTime, La1, La2, La3, La4, La5, La6, La7, La8, La9, La10);
			}
			rs.close();
		} catch (Exception e) {
			System.out.println("SaveLabels..." + e.getMessage());
		}
		return row > 0;
	}
	

	/**
	 * 保存获取批次的用户名和批次号
	 */
	public boolean SaveGetUserInfo(int batch, String userName, String getTime) {
		System.out.println("SaveGetUserInfo...");
		int row = 0;
		String SQL_Query_Same = "select * from " + Values.TB_GETBATCHUSER + " where " + Values.GET_BATCH + "" +
				" = " + batch + " and " + Values.GET_USERNAME + " = '" + userName + "'" ;
		
		String SQL_Insert_GetBatchUser = "insert into " + Values.TB_GETBATCHUSER + " values (0," + batch + "," +
					"'" + userName + "','" + getTime + "')";
		
		try {
			ResultSet rs = statement.executeQuery(SQL_Query_Same);
			while (!rs.next()) {
				row = statement.executeUpdate(SQL_Insert_GetBatchUser);
			}
			rs.close();
		} catch (Exception e) {
			//System.err.println("SaveGetUserInfo..." + e.getMessage());
		}
		return row > 0;
	}
	/**
	 * 更新未完成标签表的标签状态
	 */
	public void UpdateUnfinishState(String picName, int state) {
		System.out.println("UpdateUnfinishState...");
		String SQL_Update_State = "update " + Values.TB_UNFINISH_PIC + " set " + Values.UNFINIHS_PIC_STATE 
		+ " = " + state + " where " + Values.UNFINISH_PIC_NAME + " = '" + picName + "'";
		
		int row = 0;
		try {
			row = statement.executeUpdate(SQL_Update_State);
			System.out.println("update " + row + " item");
		} catch (Exception e) {
			System.err.println("UpdateUnfinishState..." + e.getMessage());
		}
	}

	/**
	 * 更新批号当前派发数
	 */
	public void UpdateAssNum(int onBatch) {
		System.out.println("UpdateAssNum...");
		String SQL_Query_task_ass = "select " + Values.NOM_TASKNUM + ", " + Values.NOM_ASSNUM + " from " +
				Values.TB_BATCHMANAGE + " where " + Values.NOM_BATCH + " = " + onBatch;
		int tNum = 1;
		int ass = 0;
		try {
			ResultSet rs = statement.executeQuery(SQL_Query_task_ass);
			while (rs.next()) {
				tNum = rs.getInt(1);
				ass = rs.getInt(2);
			}
			ass = ass + 1;
			if (tNum == ass) {
				String SQL_Update_batchState = "update " + Values.TB_BATCHMANAGE + " set " + Values.NOM_BATCHSTATE + " = " +
						1 + " where " + Values.NOM_BATCH + " = " + onBatch;
				statement.executeUpdate(SQL_Update_batchState);	// 更新批次状态
				System.out.println("update batch state!");
				
				String SQL_Update_AssNum = "update " + Values.TB_BATCHMANAGE + " set " + Values.NOM_ASSNUM + " = " +
				ass + " where " + Values.NOM_BATCH + " = " + onBatch;
				statement.executeUpdate(SQL_Update_AssNum);	// 更新分派数目
				System.out.println("update ass num!");
				
			} else {
				String SQL_Update_AssNum = "update " + Values.TB_BATCHMANAGE + " set " + Values.NOM_ASSNUM + " = " +
						ass + " where " + Values.NOM_BATCH + " = " + onBatch;
				statement.executeUpdate(SQL_Update_AssNum);	// 更新分派数目
				System.out.println("update ass num!");
			}
			rs.close();
		} catch (Exception e) {
			System.err.println("UpdateAssNum..." + e.getMessage());
		}
		
	}
	
	/**
	 * 根据批次号获取图片名
	 * @param batch
	 * @return
	 */
	public ArrayList<String> GetBatchPicNameList(int batch) {
		System.out.println("GetBatchPicNameList...");
		String SQL_Query_Pic_Name_List = "select " + Values.BP_PICNAME + "," + Values.BP_ONBATCH 
		+ " from " + Values.TB_BATCHPICS + " where " + Values.BP_ONBATCH + " = " + batch;
		ArrayList<String> picNameList = new ArrayList<String>();
		try {
			ResultSet rs = statement.executeQuery(SQL_Query_Pic_Name_List);
			while (rs.next()) {
				picNameList.add(rs.getString(1));
				picNameList.add(rs.getString(2));
			}
			rs.close();
		} catch (Exception e) {
			System.err.println("UpdateBatchState..." + e.getMessage());
		}
		return picNameList;
	}
	
	/**
	 * 更新批次状态
	 * @param batch
	 * @param batchState
	 * @return
	 */
	public boolean UpdateBatchState(int batch, int batchState) {
		System.out.println("UpdateBatchState...");
		String SQL_Update_Batch_State = "update " + Values.TB_BATCHMANAGE + " set " + Values.NOM_BATCHSTATE 
			+ " = " + batchState + " where " + Values.NOM_BATCH + " = " + batch; 
		int row = 0;
		try {
			row = statement.executeUpdate(SQL_Update_Batch_State);
		} catch (Exception e) {
			System.err.println("UpdateBatchState..." + e.getMessage());
		}
		return row > 0;
	}
	
	/**
	 * 保存批次图片信息
	 * @param picName
	 * @param batch
	 * @param result
	 */
	public void SavePicBatch(String picName, int batch, int result) {
		System.out.println("SavePicBatch...");
		String SQL_Insert_Pic_Batch_Info = "insert into " + Values.TB_BATCHPICS + " values (0,'" + picName + "', " +
				batch + "," + result + ")";
		int row = 0;
		try {
			row = statement.executeUpdate(SQL_Insert_Pic_Batch_Info);
			System.out.println("已插入：" + row + "条");
		} catch (Exception e) {
			System.err.println("SavePicBatch..." + e.getMessage());
		}
	}
	
	/**
	 * 获取未分派完成批次的图片依赖数和批次号
	 * @return
	 */
	public JSONObject GetNewBatchInfo(int batchState) {
		System.out.println("GetNewBatchInfo...");
		String SQL_query_batch = "select " + Values.NOM_BATCH + ", " + Values.NOM_RELYNUM + ", " 
		+ Values.NOM_BATCHSTATE + " from " + Values.TB_BATCHMANAGE + " where " + Values.NOM_BATCHSTATE 
		+ " = " + batchState + " order by id desc limit 1";
		
		Map<String, String> map = new HashMap<String, String>();
		try {
			ResultSet rs = statement.executeQuery(SQL_query_batch);
			while(rs.next()) {
				map.put(Values.NOM_BATCH, rs.getString(1));
				map.put(Values.NOM_RELYNUM, rs.getString(2));
				map.put(Values.NOM_BATCHSTATE, rs.getString(3));
			}
			rs.close();
		}catch (Exception e) {
			System.out.println("GetNewBatchInfo..." + e.getMessage());
		}
		JSONObject json = new JSONObject(map);
		return json;
	}
	
	/**
	 * 保存批次信息
	 * @param batch 	批次号
	 * @param batchState	批次状态 /0 未完成派发 /1 已完成派发
	 * @param relyNum	图片判断依赖数
	 * @param taskNum	图片需派发数
	 * @param assNum	图片实际派发数
	 * @param createTime	批次创建时间
	 */
	public boolean SaveBatchInfo(int relyNum, int taskNum, int assNum, String createTime) {
		System.out.println("SaveBatchInfo...");
		int row = 0;
		int batch = 1;
		int bState = 0;
		boolean result = false;
		
		String sql_query_batch = "select " + Values.NOM_BATCH + ", " + Values.NOM_BATCHSTATE + " from " 
		+ Values.TB_BATCHMANAGE + " order by id desc limit 1";

		if (relyNum > taskNum) {
			return result;
		} else {
			try {
				ResultSet rs = statement.executeQuery(sql_query_batch);
				while (rs.next()) {
					batch = rs.getInt(1);	// 得到当前表里最新的批号
					bState = rs.getInt(2);
					batch++;				// 原有批号+1
				}
				String SQL_Insert_Batch_Info = "insert into " + Values.TB_BATCHMANAGE + " values (0," + batch + "," + 0 + "," + relyNum 
				+ "," + taskNum + "," + assNum + ",'" + createTime + "')";
				if (bState == 1 || batch == 1) { // 当批次状态为1 即完成派送才能创建新的批次	
					row = statement.executeUpdate(SQL_Insert_Batch_Info);
					System.out.println("已插入：" + row + "条");
					result = true;
				}
			}catch (Exception e) {
				System.out.println("SaveBatchInfo..." + e.getMessage()); 
			}
		}
		return result;
	}
	
	
	/**
	 * 总是获取标签设置表最新一条设置的所有信息
	 * @return
	 */
	public JSONObject GetNewSetInfo() {
		System.out.println("GetNewSetInfo...");
		String SQL_Query_All = "select * from " + Values.TB_PICSET + " order by " + Values.SET_ALTERTIME + " desc limit 1";
		Map<String, String> map = new HashMap<String, String>();
		try {
			ResultSet rs = statement.executeQuery(SQL_Query_All);
			while(rs.next()) {
				map.put(Values.SET_RELYNUM, rs.getString(2));
				map.put(Values.SET_TASKNUM, rs.getString(3));
				map.put(Values.SET_MNAME, rs.getString(4));
				map.put(Values.SET_ALTERTIME, rs.getString(5));
			}
			rs.close();
		}catch (Exception e) {
			System.out.println("GetSetInfo..." + e.getMessage());
		}
		JSONObject json = new JSONObject(map);
		return json;
	}
	

	/**
	 * 保存图片标签设置表信息
	 * @param relyNum 图片标签依赖数
	 * @param taskNum 每日每人任务分配数
	 * @param managerName 修改人员
	 * @param alterTime 修改时间
	 * @return
	 */
	public boolean SaveSetInfo(int relyNum, int taskNum, String managerName, String alterTime) {
		System.out.println("SaveSetInfo...");
		boolean setResult = false; int row = 0;
		String SQL_Insert_Set_Info = "insert into " + Values.TB_PICSET + " values (0," + relyNum + ", " + taskNum + ", '" + managerName 
		+ "', '" + alterTime + "')";
		
		if (relyNum > taskNum) {
			return setResult;
		} else {
			try {
				row = statement.executeUpdate(SQL_Insert_Set_Info);
//				System.out.println(SQL_Insert_Set_Info);
				System.out.println("已插入：" + row + "条");
			} catch (Exception e) {
				System.err.println("SaveSetInfo..." + e.getMessage());
			}
			return row > 0;
		}
		
	}
	
	/**
	 * 保存正在进行标签化的图片所收集到的标签信息
	 * @param id
	 * @param picName 图片名称
	 * @param vName	志愿者名称
	 * @param proState	处理状态 0 已提交 未处理 ， 1 处理成功， 2 处理失败
	 * @param labelTime	提交标签信息的时间
	 * @param label1 标签1-10
	 * @param label2
	 * @param label3
	 * @param label4
	 * @param label5
	 * @param label6
	 * @param label7
	 * @param label8
	 * @param label9
	 * @param label10
	 */
	public static void SaveInProInfo(int id, String picName, String vName, int proState, String labelTime, String label1, String label2,
			String label3, String label4, String label5, String label6, String label7, String label8, String label9, String label10) {
		System.out.println("SaveInProInfo...");
		String SQL_Insert_Pro_Info = "insert into " + Values.TB_IN_PROGESS + " values (" + id + ",'" + picName + "','" + vName + "',"
		+ proState + ",'" + labelTime + "', '" + label1 + "','" + label2 + "','" + label3 + "','" + label4 + "','" + label5 + "','" 
		+ label6 + "','" + label7 + "','" + label8 + "','" + label9 + "','" + label10 + "')";
		int row = 0;
		try {
			row = statement.executeUpdate(SQL_Insert_Pro_Info);
			System.out.println("已插入：" + row + "条");
		} catch (Exception e) {
			System.err.println("SaveInProInfo..." + e.getMessage());
		}
		
	}	
	
	/**
	 * 根据图片当前状态获取图片的名称列表
	 * @param picState
	 * @return
	 */
	public ArrayList<String> GetUnfinishPicNameList(int picState) {
		System.out.println("GetUnfinishPicNameList...");
		String SQL_Query_NOP_Pic = "select " + Values.UNFINISH_PIC_NAME + " from " + Values.TB_UNFINISH_PIC 
		+ " where " + Values.UNFINIHS_PIC_STATE + " = " + picState;
		ArrayList<String> list = new ArrayList<String>();
		try {
			ResultSet rs = statement.executeQuery(SQL_Query_NOP_Pic);
			while(rs.next()) {
				list.add(rs.getString(1));
			}
			rs.close();
		} catch (Exception e) {
			System.err.println("GetUnfinishPicList..." + e.getMessage());
		}
		return list;
	}
		
	
	/**
	 * 获取完成标签化图片的相关信息
	 * @param fileName
	 * @param limit	限制获取条数
	 */
	public static JSONObject GetPicInfo(String fileName, int limit) {
		System.out.println("GetPicInfo...");
		String SQL_Query_Pic_Info = "select " + Values.PIC_NAME + ", " + Values.PIC_LABEL + "," + Values.FINISH_LABEL_TIME 
		+ " from " + Values.TB_FINISH_PIC + " where " + Values.PARENT_FILE + " = '"+ fileName + "' limit " + limit;
		JSONObject json = new JSONObject();
		Map<String,String> map = new HashMap<String, String>();
		int num = 1;
		try {
			ResultSet rs = statement.executeQuery(SQL_Query_Pic_Info);
			while(rs.next()) {
				map.put(Values.PIC_NAME, rs.getString(1));
				map.put(Values.PIC_LABEL, rs.getString(2));
				map.put(Values.FINISH_LABEL_TIME, rs.getString(3));
				json.put("data" + num, map);
				num++;
			}	
			rs.close();
		} catch (Exception e) {
			System.err.println("GetPicInfo: Err->" + e.getMessage());
		}
		num--;
		json.put("Count",num + "");
		return json;
	}
	
	
	/**
	 * 获取全部已完成标签化图片目录信息
	 * @return
	 */
	public static JSONObject GetFile() {
		System.out.println("GetFile...");
		String SQL_Query_File = "select " + Values.LEVEL + ", " + Values.PARENT_NAME 
		+ "," + Values.FILE_NAME + "," + Values.NUM + " from " + Values.TB_FILE;
		
		JSONObject json = new JSONObject();
		Map<String, String> map = new HashMap<String, String>();	
		int num = 1;
		try {
			ResultSet rs = statement.executeQuery(SQL_Query_File);
			while (rs.next()) {
				map.put("Level" + num, rs.getString(1));
				map.put("ParentName" + num, rs.getString(2));
				map.put("FileName" + num, rs.getString(3));
				map.put("FileNum" + num, rs.getString(4));
				num++;
			}
			rs.close();
		} catch (Exception e) {
			System.err.println("GetFile: Err->" + e.getMessage());
		}
		num -= 1;
		json.put("Count", num);
		json.put("data", map);
		return json;
	}
	
	
	/**
	 * 获取全部已经标签化的图片的所有种类和该种类的数量
	 * @return
	 */
	public static JSONObject GetPicTypeNum() {
		System.out.println("GetPicTypeNum...");
		String SQL_Query_Pic_Type_Num = "select distinct " + Values.PARENT_FILE + ", count(*) from " + Values.TB_FINISH_PIC 
		+ " where " + Values.PARENT_FILE + " in (select distinct " + Values.PARENT_FILE + " from " + Values.TB_FINISH_PIC 
		+ ") group by " + Values.PARENT_FILE;

		JSONObject json = new JSONObject();
		Map<String, String> map = new HashMap<String, String>();	
		int num = 1;
		try {
			ResultSet rs = statement.executeQuery(SQL_Query_Pic_Type_Num);
			while (rs.next()) {
				map.put("PicType" + num, rs.getString(1));
				map.put("PicNum" + num, rs.getString(2));
				num++;
			}
			rs.close();
		} catch (Exception e) {
			System.err.println("GetFile: Err->" + e.getMessage());
		}
		num -= 1;
		json.put("Count", num);
		json.put("data", map);
		return json;

	}
	
	
	/**
	 * 根据目录级搜索该目录下的文件名和文件里的数目
	 * @param level
	 * @return
	 */
	public static JSONObject GetFile(int level) {
		System.out.println("GetFile...");
		JSONObject json = null;
		Map<String, String> map = new HashMap<String, String>();
		String Query_File = "select " + Values.FILE_NAME + ", " + Values.NUM
		+ " from " + Values.TB_FILE + " where " + Values.LEVEL + " = '" + level + "'";
		int num = 1;
		try {
			ResultSet rs = statement.executeQuery(Query_File);
			while (rs.next()) {
				map.put("file_name" + num, rs.getString(1));
				map.put("num" + num, rs.getString(2));
				num++;
			}
			rs.close();
		} catch (Exception e) {
			System.err.println("GetFile: Err->" + e.getMessage());
		}
		num -= 1;
		map.put("Count", "" + num);
		json = new JSONObject(map);
		return json;
	}
	
	
	/**
	 * 保存图片详细信息
	 * @param ParentFile
	 * @param PicName
	 * @param OtherLabel
	 */
	public boolean SavePicInfo(String ParentFile, String PicName, String PicLabel) {
		System.out.println("SavePicInfo...");
		int row = 0;
		String SQL_Save_Pic_Info = "insert into " + Values.TB_FINISH_PIC + " values (0,'" + ParentFile + 
		"','" + PicName + "','" + PicLabel + "','" + DateUtils.GetNowTime() + "')";
		try {
			row = statement.executeUpdate(SQL_Save_Pic_Info);
			System.out.println("已插入：" + row + "条");
		} catch (Exception e) {
			System.err.println("SavePicInfo..." + e.getMessage());
		}
		return row > 0;
	}
	
	
	/**
	 * 保存图片文件夹的相关信息
	 * @param level	文件夹级别
	 * @param ParentName 当前文件夹上级目录
	 * @param FileName	当前文件夹名称
	 * @param num	文件夹里的文件数目
	 */
	public static boolean SaveFile(int level, String ParentName, String FileName, int num) {
		System.out.println("SaveFile...");
		System.out.println("\nlevel:" + level + "  parent_name:" + ParentName + "  file_name:" + FileName + "  num:" + num);
		String SQL_Save_File = "insert into " + Values.TB_FILE + " values (0," + level + 
		",'" + ParentName + "','" + FileName + "'," + num + ")";
		int row = 0;
		try {
			row = statement.executeUpdate(SQL_Save_File);
			System.out.println("已插入：" + row + "条");
		} catch (Exception e) {
			System.err.println("SaveFile..." + e.getMessage());
		}
		return row > 0;
	}
	
	
	
	/**
	 * 返回所有用户名 列表
	 * @return
	 */
	public static JSONObject GetAllUserName() {
		System.out.println("GetAllUserName...");
		String SQL_Query_All_User_Name = "select " + Values.USER_NAME + " from " + Values.USER_TB + " where " +
				Values.IS_MANAGER + " = 0";
		
		int num = 1;
		Map<String,String> map = new HashMap<String, String>();
		try {
			ResultSet rs = statement.executeQuery(SQL_Query_All_User_Name);
			while(rs.next()) {
				map.put(Values.USER_NAME + num, rs.getString(1));
				num++;
			}
			rs.close();
		} catch (Exception e) {
			System.err.println("GetAllUserName: Err->" + e.getMessage());
		}
		JSONObject json = new JSONObject();
		num--;
		json.put("Count", num);
		json.put("data", map);
		return json;
	}
	
	/**
	 * 获取未标签化表的说有信息
	 * @return
	 */
	public static JSONObject GetPicJson() {
		System.out.println("GetPicJson...");
		Map<String, String> map = new HashMap<String,String>();
		int num = 0;
		String SQL_Query_Get_Pic = "select * from " + Values.TB_UNFINISH_PIC;
		
		try {
			ResultSet rs = statement.executeQuery(SQL_Query_Get_Pic);
			while(rs.next()) {
				map.put(Values.UNFINISH_PIC_ID + num, rs.getString(1));
				map.put(Values.UNFINISH_PIC_NAME + num, rs.getString(2));
				map.put(Values.UNFINIHS_PIC_STATE + num, rs.getString(3));
				map.put(Values.UNFINISH_UPLOAD_TIME + num, rs.getString(4));
				num++;
			}
			rs.close();
		} catch (Exception e) {
			System.out.println("GetPicJson..." + e.getMessage());
		}
		map.put("num", num + "");
		JSONObject json = new JSONObject(map);
		return json;
	}
		
	/**
	 * 保存未标签化图片的信息
	 */
	public static void SaveUnfinishedPicInfo(String picName, int picState, String uploadTime) {
		System.out.println("SaveUnfinishedPicInfo...");
		String SQL_Insert_Pic_Name = "insert into " + Values.TB_UNFINISH_PIC + " values " +
				"(0, '" + picName + "' ,'" + picState + "','" + uploadTime + "')";
		int row = 0;
		try {
			row = statement.executeUpdate(SQL_Insert_Pic_Name);
			System.out.println("已插入：" + row + "条");
		} catch (SQLException e) {
			System.err.println("SaveUnfinishedPicInfo..." + e.getMessage());
		}
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
		System.out.println("SaveUserData...");
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
	 * 修改用户密码
	 * @param name
	 * @param new_psw
	 * @return
	 */
	public static String AlterPsw(String name, String new_psw) {
		System.out.println("AlterPsw...");
		String RESULT = "Alter_Fail";
		String SQL_Alter_Psw = "update " + Values.USER_TB + " set " + Values.USER_PSW 
		+ " = '" + new_psw + "' where " + Values.USER_NAME + " = '" + name + "'";
		try {
			int row = statement.executeUpdate(SQL_Alter_Psw);
			if (row > 0) {
				RESULT = "Alter_Success";
			}
		} catch (Exception e) {
			System.out.println("AlterPsw ...");
		}
		return RESULT;
	}
	
	
	/**
	 * 通过用户名查询用户所有信息,返回json对象，用户管理员都可用
	 * 查询内容包括，用户名，密码，电话号码，邮箱，积分，任务完成情况，头像
	 * @param name
	 * @return
	 */
	public static JSONObject QueryUserAll(String name) {
		System.out.println("QueryUserAll...");
		String SQL_Query_All = "select * from " + Values.USER_TB + " where " + Values.USER_NAME + " = '" + name + "'";
		// 本地头像图片路径
		String ImgPath = Values.UserIconPath; 
		Map<String, String> map = new HashMap<String, String>();
		JSONObject json = null;
		ResultSet rs = null;
		
		try {
			rs = statement.executeQuery(SQL_Query_All);
			while (rs.next()) {
				map.put(Values.USER_ID, rs.getString(1));
				map.put(Values.USER_NAME, rs.getString(2));
				map.put(Values.USER_PSW, rs.getString(3));				
				map.put(Values.USER_TEL, rs.getString(4));
				map.put(Values.USER_EMAIL, rs.getString(5));
				map.put(Values.USER_INTEGRAL, rs.getString(6));
				map.put(Values.USER_TASK_COMPLETION, rs.getString(8));
				map.put(Values.IS_MANAGER, rs.getString(9));
				map.put(Values.USER_LABEL_ALL_NUM, rs.getString(10));
				map.put(Values.USER_LABEL_SUCCESS_NUM, rs.getString(11));
				// 获取头像文件
				String icon_name = rs.getString(7);
				ImgPath += icon_name;
				// 加密头像文件
				String Img = Base64Image.getImageStr(ImgPath);
				map.put(Values.USER_ICON, icon_name);
				map.put("icon_code",Img);				
			}
			rs.close();
		} catch (Exception e) {
			System.err.println("QueryUserAll: Err->" + e.getMessage());
		}
		
		// 将用户信息封装到json对象中
		json = new JSONObject(map);
		
		try {
			rs.close();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}				
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
	public static String UpDateUserInfo(String name, String tel, String email, int is_manager) {
		System.out.println("UpDateUserInfo...");
		String RESULT = "Update_Fail";
		
		String SQL_Update = "update " + Values.USER_TB + " set " + Values.USER_TEL 
		+ " = '" + tel + "', " + Values.USER_EMAIL + " = '" + email	+ "', " + Values.IS_MANAGER 
		+ " = " + is_manager + " where " + Values.USER_NAME + " = '" + name + "'";
		
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
