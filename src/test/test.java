package test;

import lsl.task.BatchManage;
import lsl.task.PscTask;
import lsl.utils.DateUtils;
import lsl.utils.DbUtils;
import lsl.utils.FileUtils;
import lsl.values.Values;


public class test {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		
		DbUtils.getInstance();
		System.out.println(DbUtils.QueryUserAll("0"));
//		System.out.println(db.getAllLabels("ҽԺ5.jpg"));
//		db.addFile(1, "ҽԺ");
//		FileUtils.copyFolder(Values.UnfinishedIconPath + "/ҽԺ5.jpg", Values.FINISHICONPATH + "/ҽԺ5.jpg");
//		PscTask t = new PscTask();
//		t.mainTask("ҽԺ5.jpg");
//		t.isOK("ҽԺ5.jpg");
	
		
//		for (int i = 15; i < 20; i++) {
//			
//			System.out.println(BatchManage.GetTaskPicInfo("name" + i));
//		}
//		
		
//		String newPath = "D:/Copy/2017-04-20 12-38-22.mp4";
//		String oldPath = "D:/KK_Movies/kk 2017-06-25 20-54-34.mp4";
////		String oldPath = "D:/Images/test/2017-04-20 12-38-22.jpg";
//		FileUtils.copyFolder(oldPath, newPath);
//		System.out.println(BatchManage.GetTaskPicInfo("name"));
		/** ������ɱ�ǩ����ͼƬȫ�����浽���ݿ�	*/
//		ArrayList<String> lists = FileUtils.tFolder(Values.FINISHICONPATH,1);
//		f(lists,1);
		/** ������ɱ�ǩ����ͼƬȫ�����浽���ݿ�*/	
		
//		DbUtils.getInstance();
//		System.out.println(DbUtils.GetAllUserName());
//		
//		System.out.println(DbUtils.QueryUserAll("nae"));

		
//		DbUtils.getInstance();
		
		// �����ļ���
//		String file = "D:/WWW/aaa/bbb/ccc/ddd";
//		File f = new File(file);
//		if (!f.isDirectory()) {
//			System.out.println("�ļ������ɹ���" + f.mkdirs());
//		}
//		
		
		
//		DbUtils db = DbUtils.getInstance();
//		ArrayList<String> list = db.GetBatchPicNameList(1);
//		int count = list.size();
//		System.out.println(count);
//		for (int i = 0; i < count; i += 2) {
//			System.out.println(list.get(i));
//			System.out.println(list.get(i + 1));
//		}
//		DbUtils.SaveInProInfo(0, "atm2.jpg", "lis", 0, "2017-06-17", "ȡ���", "", "", "", "", "", "", "", "", "");
//		System.out.println(DbUtils.SaveSetInfo(15, 20, "lsl", DateUtils
//				.GetNowTime()));
//		System.out.println(DbUtils.GetSetInfo());
//		System.out.println(DbUtils.SaveBatchInfo(20, 22, 20, DateUtils
//				.GetNowTime()));
		
		
//		System.out.println(BatchManage.createBatch());
//		BatchManage.GetTaskPicInfo();
//		DbUtils db = DbUtils.getInstance();
//		
//		System.out.println(db.SaveLabels("ҽԺ3.jpg", "lsl", 0, DateUtils.GetNowTime(), 
//				"label1", "l2", "", "", "", "", "", "", "", ""));
//		for (int i = 1; i <= 10; i++)
//		System.out.println(ImgUtils.getTaskPicUrl());
//		System.out.println(db.SaveGetUserInfo(0, "dfgdddd", DateUtils.GetNowTime()));
//		System.out.println(BatchManage.GetTaskPicInfo("dd"));
//		System.out.println(BatchManage.createBatchInfo());
		
		
//		Random random = new Random();
//		Vector<Integer> v = new Vector<Integer>();
//		int count = 0;
//		
//		while (count < 10) {
//			int number = random.nextInt(20) + 1;
//			if (!v.contains(number)) {
//				v.add(number);
//				count++;
//			}
//		}
//		
//		for (int i : v) {
//			System.out.println(i);
//		}
	
//		for (int j = 0; j < 10; j++) {
//			int i = (int) (1 + Math.random() * 9);
//			System.out.println(i);
//		}	
		
		
		/** ����Ŀ¼����ȡ�ü�Ŀ¼�µ��ļ�**/
/*		DbUtils.getInstance();
		System.out.println(DbUtils.GetPicInfo("�캣̲", 200));*/
//		System.out.println(DbUtils.GetFile(1));
//		System.out.println(DbUtils.GetFile(2));
//		System.out.println(DbUtils.GetFile(3));
		/** ����Ŀ¼����ȡ�ü�Ŀ¼�µ��ļ�**/
		
//		System.out.println(DbUtils.GetPicTypeNum());
//		System.out.println(DbUtils.GetFile());
//		JSONObject json = new JSONObject();
//		json.put("PicTypeNum", DbUtils.GetPicTypeNum());
//		json.put("FileInfo", DbUtils.GetFile());
//		System.out.println(json);
		
		
	
//		FileUtils.traverseFolder2(Values.UnfinishedIconPath);
//		DbUtils.getInstance();
//		System.out.println(DbUtils.GetAllUserName());
		
//		System.out.println("name = " + DbUtils.QueryUserAll("1").get(Values.USER_NAME));

//		JSONObject json = ImgUtils.getImg("atm2.jpg");
//		Base64Image.GenerateImage(json.getString("imgStr"), "D:/jj.jpg");
		
//		DbUtils.getInstance();
//		JSONObject json  = ImgUtils.getImgUrl();
//		int num = json.getInt("num") - 1;
//		System.out.println(num);
//		
//		while(num >= 0) {
//			System.out.println(num + " : " + json.getJSONObject("img" + num));
//			num--;
//		}
		
		
		
//		System.out.println(json.get("img_name4"));
		
//		DbUtils.GetParentFolder("", 1, 6);
//		System.out.println(DbUtils.GetImgInfo("��̲1.jpg"));	// ��ѯһ��ͼƬ����ϸ��Ϣ
//		DbUtils.GetParentFolder("����վ");
//		DbUtils.GetInFolderInfo("����վ");
//		DbUtils.GetFolderInfo(2);
		
//		DbUtils.SaveImgInfo("atm", "atm1", "atm");
		
		//DbUtils.SaveUserData("lsl", "123", "1234154", "xxx@xxx.com", 100, "default", "100%",1);
		// ��ѯ�û���Ϣ
//		JSONObject json = DbUtils.QueryUserAll("lsl");
//		System.out.println(json);
		
//		System.out.println(Values.CR_CLASS1_TB);
		
//		DbUtils.SaveFilePath(1, 0, 1, "nimei");

//		ArrayList<String> list = DbUtils.GetIdAndFileName(1);
//		System.out.println(list);
//		for(int i = 0; i < 100; i++)
//			System.out.println(System.currentTimeMillis());
		
//		String name = "default.png";
//		String[] strs = name.split("\\.");
//		System.out.println(strs[strs.length-1]);
//		for (String s: strs)
//			System.out.println(s);
		
//		DbUtils db = DbUtils.getInstance();
//		boolean s = db.SaveSetInfo(10, 14, "name", DateUtils.GetNowTime());
//		System.out.println(s);
		
	}

//	/**
//	 * ������ɱ�ǩ����ͼƬȫ�����浽���ݿ�	
//	 * @param lists
//	 * @param level
//	 */
//	private static void f(ArrayList<String> lists,int level) {
//		level++;
//		for (int i = 0; i < lists.size(); i++) {
//			ArrayList<String> list = FileUtils.tFolder(lists.get(i),level);
//
//			if (list.size() > 0) {
//				f(list,level);
//			}
//		}
//	}

/*	private static void savefileinfo() {
		
		 //������ɱ�ǩ��ͼƬ��Ϣ�������ݿ�
		FileUtils.traverseFolder(Values.FINISHICONPATH, 0, 1, 0);
		// ����ɨ��Ŀ¼��
		FileUtils.f(1);
		 //������ɱ�ǩ��ͼƬ��Ϣ�������ݿ�
	}*/

	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
}
