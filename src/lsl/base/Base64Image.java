package lsl.base;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;

import sun.misc.BASE64Decoder;
import sun.misc.BASE64Encoder;

public class Base64Image {
/**
	 * 将图片转为字节数组字符串，并对其进行base64编码处理
	 * @param img_path
	 * @return
	 */
	public static String getImageStr(String img_path) {
		InputStream in = null;
		byte [] data = null;
		// 读取图片字节数组
		try {
			in = new FileInputStream(img_path);
			data = new byte[in.available()];
			in.read(data);
			in.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		//	对字节数组base64编码
		BASE64Encoder encoder = new BASE64Encoder();
		// 返回base64编码过的字节数组字符串
		return encoder.encode(data);
	}
	/**
	 * 对字节数组字符串进行Base64解码并生成图片?
	 * @param ImgStr
	 * @param output
	 * @return
	 */
	public static boolean GenerateImage(String ImgStr, String output) {
		// 图像数据为空返回false
		if(ImgStr == null) 
			return false;
		
		BASE64Decoder deconder = new BASE64Decoder();
		try {
			// base64解码
			byte [] b = deconder.decodeBuffer(ImgStr);
			for (int i = 0; i < b.length; ++i) {
				// 调整异常数据
				if (b[i] < 0) {
					b[i] += 256;
				}
			}
			// 生成JPEG图片
			OutputStream out = new FileOutputStream(output);
			out.write(b);
			out.flush();
			out.close();
			return true;
			
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
	}
}
