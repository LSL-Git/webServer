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
		// 将已完成标签化的图片全部保存到数据库
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
//		System.out.println(DbUtils.GetImgInfo("海滩1.jpg"));	// 查询一张图片的详细信息
//		DbUtils.GetParentFolder("公车站");
//		DbUtils.GetInFolderInfo("公车站");
//		DbUtils.GetFolderInfo(2);
		
//		DbUtils.SaveImgInfo("atm", "atm1", "atm");
		
		//DbUtils.SaveUserData("lsl", "123", "1234154", "xxx@xxx.com", 100, "default", "100%",1);
		// 查询用户信息
//		JSONObject json = DbUtils.QueryUserAll("lsl");
//		System.out.println(json);
		
//		System.out.println(Values.CR_CLASS1_TB);
		
//		DbUtils.SaveFilePath(1, 0, 1, "nimei");

//		ArrayList<String> list = DbUtils.GetIdAndFileName(1);
//		System.out.println(list);
		
	}

	private static void savefileinfo() {
		
		 //将已完成标签的图片信息保存数据库
		FileUtils.traverseFolder(Values.FINISHICONPATH, 0, 1, 0);
		// 控制扫描目录级
		FileUtils.f(1);
		 //将已完成标签的图片信息保存数据库
	}

}
