package test;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import org.json.JSONObject;

import lsl.utils.DbUtils;
import lsl.utils.FileUtils;
import lsl.values.Values;

public class test {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// ������ɱ�ǩ����ͼƬȫ�����浽���ݿ�
//		savefileinfo();
	
//		FileUtils.traverseFolder2(Values.UnfinishedIconPath);
		
		
//		DbUtils.getInstance();
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
		
	}

	private static void savefileinfo() {
		
		 //������ɱ�ǩ��ͼƬ��Ϣ�������ݿ�
		FileUtils.traverseFolder(Values.FINISHICONPATH, 0, 1, 0);
		// ����ɨ��Ŀ¼��
		FileUtils.f(1);
		 //������ɱ�ǩ��ͼƬ��Ϣ�������ݿ�
	}

}
