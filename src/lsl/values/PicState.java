package lsl.values;

public class PicState {
	/**
	 * û���κβ���*/
	public static final int NOP = 0; 
	/**
	 * ��ɱ�ǩ*/
	public static final int OK = 1; 
	/**
	 * ���ڽ��б�ǩ���Է���������*/
	public static final int INPRO = 2; 
	/**
	 * ���б�ǩ������δ���*/
	public static final int HLABEL = 3; 
	
	/**
	 * ��ǩ��� ʧ��
	 */
	public static final int BFAIL = 0;
	/**
	 * ��ǩ��� �ɹ�
	 */
	public static final int BOK = 1;
	/**
	 * ��ǩ��� δ����
	 */
	public static final int BUNREATED = 2;
	/**
	 * ���γ�ʼ
	 */
	public static final int BANOP = 0;
	/**
	 * ����ͼƬδ�������
	 */
	public static final int BACROK = 1;
	/**
	 * ���η������
	 */
	public static final int BAOK = 2;
}
