package lsl.utils;

import java.text.SimpleDateFormat;
import java.util.Date;

public class DateUtils {

	/**
	 * 或取当地时间，并格式化
	 * @return
	 */
	public static String GetNowTime() {
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH-mm-ss");
		String NowTime = sdf.format(new Date());
		return NowTime;
	}
	
}
