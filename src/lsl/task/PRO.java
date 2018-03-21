package lsl.task;

import java.util.Calendar;
import java.util.Date;
import java.util.Timer;
import java.util.TimerTask;

public class PRO {
	
	/**
	 * 启动定时器，执行定时任务
	 */
	public static void TimingPro() {
		Calendar calendar = Calendar.getInstance();
		calendar.set(Calendar.HOUR_OF_DAY, 24);	// 控制时
		calendar.set(Calendar.MINUTE, 0);	// 控制分
		calendar.set(Calendar.SECOND, 0);	// 控制秒
		
		Date time = calendar.getTime();	//得出执行任务时间 这里是24:00:00
		
		Timer timer=  new Timer();
		timer.schedule(new TimerTask() {
			
			@Override
			public void run() {
				//do some...
			}
		} ,time , 1000 * 60 * 60 * 24);	// 设置延迟24小时,即每天这个时候执行
	}
	
}
