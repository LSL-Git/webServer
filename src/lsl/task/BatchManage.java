package lsl.task;

import java.util.ArrayList;
import java.util.Random;
import java.util.Vector;

import org.json.JSONObject;

import lsl.utils.DateUtils;
import lsl.utils.DbUtils;
import lsl.values.PicState;
import lsl.values.Values;

public class BatchManage {

	private static JSONObject json;
	private static int taskNum;
	private static int relyNum;
	private static DbUtils db = DbUtils.getInstance();
	private static ArrayList<String> list;
	private static ArrayList<String> piclist;

	/**
	 * ��������
	 * @return
	 */
	private static boolean createBatch() {
		System.out.println("createBatch...");
		if (createBatchInfo()) {
			json = db.GetNewBatchInfo(PicState.NOP);
			if (json.getInt(Values.NOM_BATCHSTATE) == PicState.BANOP) 
				createPicBatch(json.getInt(Values.NOM_RELYNUM), json.getInt(Values.NOM_BATCH));
			
			json = db.GetNewBatchInfo(PicState.NOP);
			return json.getInt(Values.NOM_BATCHSTATE) == PicState.NOP;
		} else {
			return false;
		}

	}
	
	/**
	 * ���û��������Ϣ���½�һ��
	 */
	private static boolean createBatchInfo() {
		System.out.println("createBatchInfo...");
		json = db.GetNewSetInfo();
		relyNum = json.getInt(Values.SET_RELYNUM);
		taskNum = json.getInt(Values.SET_TASKNUM);
		db.SaveBatchInfo(relyNum, taskNum, 0, DateUtils.GetNowTime());
		
		return db.GetNewBatchInfo(PicState.NOP).length() > 0;
	}
	
	/**
	 * ��������ͼƬ��Ϣ
	 * @param picNum
	 * @param Batch
	 * @return 
	 */
	private static boolean createPicBatch(int picNum, int Batch) {
		System.out.println("createPicBatch...");
		
		list = db.GetUnfinishPicNameList(PicState.NOP);	// ��ȡδ����ͼƬ�б�
		
		Vector<Integer> v = RandomNumberNoRepeat(picNum, list.size() - 1);
	
		if (list.size() > 0 && v.size() > 0) {
			for (int i : v) {
				db.SavePicBatch(list.get(i), Batch, PicState.BUNREATED);
				db.UpdateUnfinishState(list.get(i), 0); // ����δ��ɱ�ǩ����ı�ǩ״̬
			}
			return true;
		} else {
			return false;
		}
		
	}
	
	/**
	 * ���ݷ�Χ������ ��ȡ��ͬ���������
	 * @param count
	 * @param range
	 * @return
	 */
	private static Vector<Integer> RandomNumberNoRepeat(int count, int range) {
		System.out.println("RandomNumberNoRepeat...");
		Random random = new Random();
		Vector<Integer> v = new Vector<Integer>();
		int coun = 0;
		
		while (coun < count) {
			int number = random.nextInt(range) + 1;
			if (!v.contains(number)) {
				v.add(number);
				coun++;
			}
		}
		return v;
	}
	
	/**
	 * ����ͼƬ�����б�
	 */
	private static void getNameList(String userName) {
		json = db.GetNewBatchInfo(PicState.NOP);
		piclist = db.GetBatchPicNameList(json.getInt(Values.NOM_BATCH));
		
		if (db.SaveGetUserInfo(json.getInt(Values.NOM_BATCH), userName, DateUtils.GetNowTime())) {
			db.UpdateAssNum(json.getInt(Values.NOM_BATCH));
		}
	}
	/**
	 * ��ȡ��Ӧ���ŵ�ͼƬ��
	 */
	public static ArrayList<String> GetTaskPicInfo(String userName) {
		System.out.println("GetTaskPicInfo...");
		
		if (db.GetNewBatchInfo(0).length() > 0) {
			getNameList(userName);
		} else {
			if (createBatch()) {
				getNameList(userName);
			}
		}
		
		return piclist;
	}
	
}

