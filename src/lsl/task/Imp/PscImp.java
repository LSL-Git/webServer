package lsl.task.Imp;

/**
 * ͼƬͳ�Ʒ���
 * @author LSL
 *
 */
public interface PscImp {

	/**
	 * �ж��Ƿ��ռ����㹻�ı�ǩ��������
	 * @param picName ͼƬ����
	 * @return
	 */
	public boolean isOK(String picName);
	
	/**
	 * ��Ƶͳ�ƣ��ж��Ƿ�ﵽ���б�׼
	 * @param picName ͼƬ����
	 * @return
	 */
	public boolean wordCount(String picName);
	
	
	public void mainTask(String picName);
	
	
	public boolean updateDb(String picName);

}
