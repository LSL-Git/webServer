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
		System.out.println("initDb...");
		try {
			// �����û���Ϣ��
			if (!statement.execute(Values.CR_USER_TB)) { 
				System.out.println("�û���Ϣ�����ɹ�");
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
		
		try {// ����δ��ɱ�ǩ��ͼƬ��
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
				System.out.println("����" + row + "����¼");
				flag = row > 0;
			} else {
				flag = SaveFile(level, parentName, fileName, 1);
//				System.out.println("û�м�¼");
			}
			rs.close();
		} catch (Exception e) {
			System.out.println("addFile Err..." + e.getMessage());
		}
		
		return flag;
	}
	
	
	/**
	 * ͼƬ��ǩ����ж�
	 * @param picName
	 * @return
	 */
	public String getLabelJudge(String picName) {
		System.out.println("getAllLabels...");
		//DICTΪ�ʵ䣬MAX_LENGTHΪ�ʵ��е����ʳ�
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
//		System.out.println("�ʵ䣺" + DICT);
//		System.out.println("���ʳ���" + MAX_LENGTH);
//		System.out.println("text:" + text);
//		System.out.println("�ܱ�ǩ����" + laNum);
		
		List<Map.Entry<String,Integer>> list = WordCountAndSort.getWordCountAndSort(MAX_LENGTH, DICT, text);
		for (int i = list.size() - 1; i >= 0; i--) {
			int count = list.get(i).getValue();
			String laName = list.get(i).getKey();
			if (count / laNum >= 0.6) {
				System.out.println(laName + ",��ͬ�ʣ�" + count/laNum);
				laLevel += "/" + laName ;
			}
//			System.out.println(list.get(i).getKey() + ":" + list.get(i).getValue());
		}
		System.out.println(picName + ",�ж������" + laLevel);
		return laLevel;
	}
	

	/**
	 * ��ȡͼƬ�ж�������
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
	 * ���ݱ������ֶ�����ѯ���ֶ�����
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
	 * ���±�ǩ��Ϣ
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
	 * �����ǩ��Ϣ
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
	 * �����ȡ���ε��û��������κ�
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
	 * ����δ��ɱ�ǩ��ı�ǩ״̬
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
	 * �������ŵ�ǰ�ɷ���
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
				statement.executeUpdate(SQL_Update_batchState);	// ��������״̬
				System.out.println("update batch state!");
				
				String SQL_Update_AssNum = "update " + Values.TB_BATCHMANAGE + " set " + Values.NOM_ASSNUM + " = " +
				ass + " where " + Values.NOM_BATCH + " = " + onBatch;
				statement.executeUpdate(SQL_Update_AssNum);	// ���·�����Ŀ
				System.out.println("update ass num!");
				
			} else {
				String SQL_Update_AssNum = "update " + Values.TB_BATCHMANAGE + " set " + Values.NOM_ASSNUM + " = " +
						ass + " where " + Values.NOM_BATCH + " = " + onBatch;
				statement.executeUpdate(SQL_Update_AssNum);	// ���·�����Ŀ
				System.out.println("update ass num!");
			}
			rs.close();
		} catch (Exception e) {
			System.err.println("UpdateAssNum..." + e.getMessage());
		}
		
	}
	
	/**
	 * �������κŻ�ȡͼƬ��
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
	 * ��������״̬
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
	 * ��������ͼƬ��Ϣ
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
			System.out.println("�Ѳ��룺" + row + "��");
		} catch (Exception e) {
			System.err.println("SavePicBatch..." + e.getMessage());
		}
	}
	
	/**
	 * ��ȡδ����������ε�ͼƬ�����������κ�
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
	 * ����������Ϣ
	 * @param batch 	���κ�
	 * @param batchState	����״̬ /0 δ����ɷ� /1 ������ɷ�
	 * @param relyNum	ͼƬ�ж�������
	 * @param taskNum	ͼƬ���ɷ���
	 * @param assNum	ͼƬʵ���ɷ���
	 * @param createTime	���δ���ʱ��
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
					batch = rs.getInt(1);	// �õ���ǰ�������µ�����
					bState = rs.getInt(2);
					batch++;				// ԭ������+1
				}
				String SQL_Insert_Batch_Info = "insert into " + Values.TB_BATCHMANAGE + " values (0," + batch + "," + 0 + "," + relyNum 
				+ "," + taskNum + "," + assNum + ",'" + createTime + "')";
				if (bState == 1 || batch == 1) { // ������״̬Ϊ1 ��������Ͳ��ܴ����µ�����	
					row = statement.executeUpdate(SQL_Insert_Batch_Info);
					System.out.println("�Ѳ��룺" + row + "��");
					result = true;
				}
			}catch (Exception e) {
				System.out.println("SaveBatchInfo..." + e.getMessage()); 
			}
		}
		return result;
	}
	
	
	/**
	 * ���ǻ�ȡ��ǩ���ñ�����һ�����õ�������Ϣ
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
	 * ����ͼƬ��ǩ���ñ���Ϣ
	 * @param relyNum ͼƬ��ǩ������
	 * @param taskNum ÿ��ÿ�����������
	 * @param managerName �޸���Ա
	 * @param alterTime �޸�ʱ��
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
				System.out.println("�Ѳ��룺" + row + "��");
			} catch (Exception e) {
				System.err.println("SaveSetInfo..." + e.getMessage());
			}
			return row > 0;
		}
		
	}
	
	/**
	 * �������ڽ��б�ǩ����ͼƬ���ռ����ı�ǩ��Ϣ
	 * @param id
	 * @param picName ͼƬ����
	 * @param vName	־Ը������
	 * @param proState	����״̬ 0 ���ύ δ���� �� 1 ����ɹ��� 2 ����ʧ��
	 * @param labelTime	�ύ��ǩ��Ϣ��ʱ��
	 * @param label1 ��ǩ1-10
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
			System.out.println("�Ѳ��룺" + row + "��");
		} catch (Exception e) {
			System.err.println("SaveInProInfo..." + e.getMessage());
		}
		
	}	
	
	/**
	 * ����ͼƬ��ǰ״̬��ȡͼƬ�������б�
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
	 * ��ȡ��ɱ�ǩ��ͼƬ�������Ϣ
	 * @param fileName
	 * @param limit	���ƻ�ȡ����
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
	 * ��ȡȫ������ɱ�ǩ��ͼƬĿ¼��Ϣ
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
	 * ��ȡȫ���Ѿ���ǩ����ͼƬ����������͸����������
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
	 * ����Ŀ¼��������Ŀ¼�µ��ļ������ļ������Ŀ
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
	 * ����ͼƬ��ϸ��Ϣ
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
			System.out.println("�Ѳ��룺" + row + "��");
		} catch (Exception e) {
			System.err.println("SavePicInfo..." + e.getMessage());
		}
		return row > 0;
	}
	
	
	/**
	 * ����ͼƬ�ļ��е������Ϣ
	 * @param level	�ļ��м���
	 * @param ParentName ��ǰ�ļ����ϼ�Ŀ¼
	 * @param FileName	��ǰ�ļ�������
	 * @param num	�ļ�������ļ���Ŀ
	 */
	public static boolean SaveFile(int level, String ParentName, String FileName, int num) {
		System.out.println("SaveFile...");
		System.out.println("\nlevel:" + level + "  parent_name:" + ParentName + "  file_name:" + FileName + "  num:" + num);
		String SQL_Save_File = "insert into " + Values.TB_FILE + " values (0," + level + 
		",'" + ParentName + "','" + FileName + "'," + num + ")";
		int row = 0;
		try {
			row = statement.executeUpdate(SQL_Save_File);
			System.out.println("�Ѳ��룺" + row + "��");
		} catch (Exception e) {
			System.err.println("SaveFile..." + e.getMessage());
		}
		return row > 0;
	}
	
	
	
	/**
	 * ���������û��� �б�
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
	 * ��ȡδ��ǩ�����˵����Ϣ
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
	 * ����δ��ǩ��ͼƬ����Ϣ
	 */
	public static void SaveUnfinishedPicInfo(String picName, int picState, String uploadTime) {
		System.out.println("SaveUnfinishedPicInfo...");
		String SQL_Insert_Pic_Name = "insert into " + Values.TB_UNFINISH_PIC + " values " +
				"(0, '" + picName + "' ,'" + picState + "','" + uploadTime + "')";
		int row = 0;
		try {
			row = statement.executeUpdate(SQL_Insert_Pic_Name);
			System.out.println("�Ѳ��룺" + row + "��");
		} catch (SQLException e) {
			System.err.println("SaveUnfinishedPicInfo..." + e.getMessage());
		}
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
		System.out.println("SaveUserData...");
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
	 * �޸��û�����
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
	 * ͨ���û�����ѯ�û�������Ϣ,����json�����û�����Ա������
	 * ��ѯ���ݰ������û��������룬�绰���룬���䣬���֣�������������ͷ��
	 * @param name
	 * @return
	 */
	public static JSONObject QueryUserAll(String name) {
		System.out.println("QueryUserAll...");
		String SQL_Query_All = "select * from " + Values.USER_TB + " where " + Values.USER_NAME + " = '" + name + "'";
		// ����ͷ��ͼƬ·��
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
				// ��ȡͷ���ļ�
				String icon_name = rs.getString(7);
				ImgPath += icon_name;
				// ����ͷ���ļ�
				String Img = Base64Image.getImageStr(ImgPath);
				map.put(Values.USER_ICON, icon_name);
				map.put("icon_code",Img);				
			}
			rs.close();
		} catch (Exception e) {
			System.err.println("QueryUserAll: Err->" + e.getMessage());
		}
		
		// ���û���Ϣ��װ��json������
		json = new JSONObject(map);
		
		try {
			rs.close();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}				
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
	public static String UpDateUserInfo(String name, String tel, String email, int is_manager) {
		System.out.println("UpDateUserInfo...");
		String RESULT = "Update_Fail";
		
		String SQL_Update = "update " + Values.USER_TB + " set " + Values.USER_TEL 
		+ " = '" + tel + "', " + Values.USER_EMAIL + " = '" + email	+ "', " + Values.IS_MANAGER 
		+ " = " + is_manager + " where " + Values.USER_NAME + " = '" + name + "'";
		
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
