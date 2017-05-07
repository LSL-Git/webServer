package lsl.base;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;

import sun.misc.BASE64Decoder;
import sun.misc.BASE64Encoder;

public class Base64Image {
/**
	 * ��ͼƬתΪ�ֽ������ַ��������������base64���봦��
	 * @param img_path
	 * @return
	 */
	public static String getImageStr(String img_path) {
		InputStream in = null;
		byte [] data = null;
		// ��ȡͼƬ�ֽ�����
		try {
			in = new FileInputStream(img_path);
			data = new byte[in.available()];
			in.read(data);
			in.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		//	���ֽ�����base64����
		BASE64Encoder encoder = new BASE64Encoder();
		// ����base64��������ֽ������ַ���
		return encoder.encode(data);
	}
	/**
	 * ���ֽ������ַ�������Base64���벢����ͼƬ?
	 * @param ImgStr
	 * @param output
	 * @return
	 */
	public static boolean GenerateImage(String ImgStr, String output) {
		// ͼ������Ϊ�շ���false
		if(ImgStr == null) 
			return false;
		
		BASE64Decoder deconder = new BASE64Decoder();
		try {
			// base64����
			byte [] b = deconder.decodeBuffer(ImgStr);
			for (int i = 0; i < b.length; ++i) {
				// �����쳣����
				if (b[i] < 0) {
					b[i] += 256;
				}
			}
			// ����JPEGͼƬ
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
