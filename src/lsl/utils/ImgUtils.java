package lsl.utils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import lsl.base.Base64Image;
import lsl.task.BatchManage;
import lsl.values.Values;

import org.json.JSONObject;


public class ImgUtils {
	
	/**
	 * 获取批次图片URL和图片名
	 * @return
	 */
	public static JSONObject getTaskPicUrl(String userName) {
		JSONObject json = new JSONObject();
		Map<String, String> map = new HashMap<String, String>();
		ArrayList<String> list = BatchManage.GetTaskPicInfo(userName);
		int count = list.size();
		int num = 1;
		System.out.println(count);
		for (int i = 0; i < count; i += 2) {
			map.put("picName", list.get(i));
			map.put("picUrl", Values.httpURL + list.get(i));
			map.put("batch", list.get(i + 1));
			json.put("data" + num++, map);
		}		
		return json;
	}

	/**
	 * 获取图片的URL
	 * @return
	 */
	public static JSONObject getImgUrl() {
		DbUtils.getInstance();
		JSONObject js = DbUtils.GetPicJson();
		Map<String, Object> map = new HashMap<String, Object>();
		JSONObject json = new JSONObject();
		
		int num = js.getInt("num");
		
		int count =  num- 1;
		while(count >= 0) {
			map.put("img_name", js.getString(Values.UNFINISH_PIC_NAME + count));
			map.put("imgUrl", Values.httpURL + js.getString(Values.UNFINISH_PIC_NAME + count));
			map.put("upload_time", js.getString(Values.UNFINISH_UPLOAD_TIME + count));
			map.put("state", js.getInt(Values.UNFINIHS_PIC_STATE + count));
			json.put("img" + count, map);

			count--;
		}
		json.put("num", num);

		return json;
	}
	
	/**
	 * 根据图片名获取图片的base64加密字符串
	 * @param name
	 * @return
	 */
	public static JSONObject getImg(String name) {
		JSONObject json = new JSONObject();
		json.put("imgStr", Base64Image.getImageStr(Values.UnfinishedIconPath
				+ "/" + name));
		return json;
	}
	
	
	
	
	
	
	
	
	
}
