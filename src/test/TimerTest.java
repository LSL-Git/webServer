package test;

import java.util.Calendar;
import java.util.Date;
import java.util.Timer;
import java.util.TimerTask;

public class TimerTest {
	private static int i = 1;
	
	/**
	 * 1.�趨ָ������task��ָ��timeʱ����ִ��
	 */
	public static void timer1() {
		Timer timer = new Timer();
		timer.schedule(new TimerTask() {
			
			@Override
			public void run() {
				System.out.println("��������ִ��..." + i);	
				i++;
			}
		}, 2000);	// �˴��趨ʱ��timeΪ2000����
	}
	
	/**
	 * 2.ָ������task��ָ���ӳ�delay����й̶�period��ִ��
	 */
	public static void timer2() {
		Timer timer = new Timer();
		timer.schedule(new TimerTask() {
			
			@Override
			public void run() {
				System.out.println("��������ִ��..." + i);	
				i++;
			}
		},1000, 5000); // �״�ִ���ӳ�1�룬Ȼ��ÿ5��ִ��һ��
	}
	
	/**
	 * 3.����ָ��������task��ָ����ʱ��firstTime��ʼ�����ظ��Ĺ̶�����periodִ�У�
	 */
	public static void timer3() {
		Calendar calendar = Calendar.getInstance();
		calendar.set(Calendar.HOUR_OF_DAY, 24);	// ����ʱ
		calendar.set(Calendar.MINUTE, 0);	// ���Ʒ�
		calendar.set(Calendar.SECOND, 0);	// ������
		
		Date time = calendar.getTime();	//�ó�ִ������ʱ�� ������24:00:00
		
		Timer timer=  new Timer();
		timer.schedule(new TimerTask() {
			
			@Override
			public void run() {
				System.out.println("��������ִ��..." + i);	
				i++;
			}
		} ,time , 1000 * 60 * 60 * 24);	// �����ӳ�24Сʱ,��ÿ�����ʱ��ִ��
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
