package test;

import java.util.Calendar;
import java.util.Date;
import java.util.Timer;
import java.util.TimerTask;

public class TimerTest {
	private static int i = 1;
	
	/**
	 * 1.设定指定任务task在指定time时间内执行
	 */
	public static void timer1() {
		Timer timer = new Timer();
		timer.schedule(new TimerTask() {
			
			@Override
			public void run() {
				System.out.println("任务正在执行..." + i);	
				i++;
			}
		}, 2000);	// 此处设定时间time为2000毫秒
	}
	
	/**
	 * 2.指定任务task在指定延迟delay后进行固定period的执行
	 */
	public static void timer2() {
		Timer timer = new Timer();
		timer.schedule(new TimerTask() {
			
			@Override
			public void run() {
				System.out.println("任务正在执行..." + i);	
				i++;
			}
		},1000, 5000); // 首次执行延迟1秒，然后每5秒执行一次
	}
	
	/**
	 * 3.安排指定的任务task在指定的时间firstTime开始进行重复的固定速率period执行．
	 */
	public static void timer3() {
		Calendar calendar = Calendar.getInstance();
		calendar.set(Calendar.HOUR_OF_DAY, 24);	// 控制时
		calendar.set(Calendar.MINUTE, 0);	// 控制分
		calendar.set(Calendar.SECOND, 0);	// 控制秒
		
		Date time = calendar.getTime();	//得出执行任务时间 这里是24:00:00
		
		Timer timer=  new Timer();
		timer.schedule(new TimerTask() {
			
			@Override
			public void run() {
				System.out.println("任务正在执行..." + i);	
				i++;
			}
		} ,time , 1000 * 60 * 60 * 24);	// 设置延迟24小时,即每天这个时候执行
	}

	/**
	 * @param args
	 */
	public static void main(String[] args) {
//		timer1();
		timer2();
//		timer3();

	}

}
