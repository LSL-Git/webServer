package lsl.task;

import java.util.Calendar;
import java.util.Date;
import java.util.Timer;
import java.util.TimerTask;

public class PRO {
	
	/**
	 * ������ʱ����ִ�ж�ʱ����
	 */
	public static void TimingPro() {
		Calendar calendar = Calendar.getInstance();
		calendar.set(Calendar.HOUR_OF_DAY, 24);	// ����ʱ
		calendar.set(Calendar.MINUTE, 0);	// ���Ʒ�
		calendar.set(Calendar.SECOND, 0);	// ������
		
		Date time = calendar.getTime();	//�ó�ִ������ʱ�� ������24:00:00
		
		Timer timer=  new Timer();
		timer.schedule(new TimerTask() {
			
			@Override
			public void run() {
				//do some...
			}
		} ,time , 1000 * 60 * 60 * 24);	// �����ӳ�24Сʱ,��ÿ�����ʱ��ִ��
	}
	
}
