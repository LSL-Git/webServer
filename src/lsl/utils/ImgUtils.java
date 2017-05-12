package lsl.utils;

import java.util.HashMap;
import java.util.Map;

import lsl.base.Base64Image;
import lsl.values.Values;

import org.json.JSONObject;

public class ImgUtils {

	/**
	 * 获取图片的URL
	 * @return
	 */
	public static JSONObject getImgUrl() {
		DbUtils.getInstance();
		JSONObject js = DbUtils.GetImgJson();
		Map<String, Object> map = new HashMap<String, Object>();
		JSONObject json = new JSONObject();
		
		int num = js.getInt("num");
		
		int count =  num- 1;
		while(count >= 0) {
			map.put("img_name", js.getString("img_name" + count));
			map.put("imgUrl", Values.httpURL + js.getString("img_name" + count));
			map.put("upload_time", js.getString("upload_time" + count));
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
