package test;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import org.json.JSONObject;

import lsl.base.Base64Image;
import lsl.utils.DbUtils;
import lsl.utils.FileUtils;
import lsl.utils.ImgUtils;
import lsl.values.Values;

public class test {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// ������ɱ�ǩ����ͼƬȫ�����浽���ݿ�
//		savefileinfo();
	
//		FileUtils.traverseFolder2(Values.UnfinishedIconPath);
		DbUtils.getInstance();
		System.out.println("name = " + DbUtils.QueryUserAll("1").get(Values.USER_NAME));

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
		
	}

	private static void savefileinfo() {
		
		 //������ɱ�ǩ��ͼƬ��Ϣ�������ݿ�
		FileUtils.traverseFolder(Values.FINISHICONPATH, 0, 1, 0);
		// ����ɨ��Ŀ¼��
		FileUtils.f(1);
		 //������ɱ�ǩ��ͼƬ��Ϣ�������ݿ�
	}

}
