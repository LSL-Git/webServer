package lsl.task.Imp;

/**
 * 图片统计分类
 * @author LSL
 *
 */
public interface PscImp {

	/**
	 * 判断是否收集够足够的标签进行评判
	 * @param picName 图片名称
	 * @return
	 */
	public boolean isOK(String picName);
	
	/**
	 * 词频统计，判断是否达到评判标准
	 * @param picName 图片名称
	 * @return
	 */
	public boolean wordCount(String picName);
	
	
	public void mainTask(String picName);
	
	
	public boolean updateDb(String picName);

}
