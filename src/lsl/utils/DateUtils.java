package lsl.utils;

import java.text.SimpleDateFormat;
import java.util.Date;

public class DateUtils {

	/**
	 * ��ȡ����ʱ�䣬����ʽ��
	 * @return
	 */
	public static String GetNowTime() {
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH-mm-ss");
		String NowTime = sdf.format(new Date());
		return NowTime;
	}
	
}
