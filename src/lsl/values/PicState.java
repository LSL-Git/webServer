package lsl.values;

public class PicState {
	/**
	 * 没有任何操作*/
	public static final int NOP = 0; 
	/**
	 * 完成标签*/
	public static final int OK = 1; 
	/**
	 * 正在进行标签，以放入批次中*/
	public static final int INPRO = 2; 
	/**
	 * 已有标签，但尚未完成*/
	public static final int HLABEL = 3; 
	
	/**
	 * 标签结果 失败
	 */
	public static final int BFAIL = 0;
	/**
	 * 标签结果 成功
	 */
	public static final int BOK = 1;
	/**
	 * 标签结果 未处理
	 */
	public static final int BUNREATED = 2;
	/**
	 * 批次初始
	 */
	public static final int BANOP = 0;
	/**
	 * 批次图片未分配完成
	 */
	public static final int BACROK = 1;
	/**
	 * 批次分派完成
	 */
	public static final int BAOK = 2;
}
