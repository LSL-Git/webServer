package lsl.utils;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.util.ArrayList;

public class FileUtils {
	DbUtils db = DbUtils.getInstance();
	private static String parentName = "";
	private static FileInputStream in;
	private static FileOutputStream out;
	
	public static ArrayList<String> tFolder(String path, int level) {
		DbUtils.getInstance();
		
		int count = 0;
		ArrayList<String> lists = new ArrayList<String>();
		File file = new File(path);		
		if (file.exists()) {
			File[] files = file.listFiles();

			if (files.length == 0) {
				System.out.println("�ļ���Ϊ�գ�");
				return lists;
			} else {				
				for(File file2 : files) {
					if (file2.isDirectory()) {
						count = Count(file2.getPath());
						String FileName = file2.getName();
						if (level > 1)
							parentName = file.getName();
						
						DbUtils.SaveFile(level, parentName, FileName, count);	// ���ļ���Ϣ���浽���ݿ�

						lists.add(file2.getPath());		// ���ļ���·�����浽������

					} else {
						String parentFile = file.getName();
						String picName = file2.getName();

						if (!picName.substring(picName.length() - 2, picName.length()).equals("db")) {
							DbUtils db = DbUtils.getInstance();
							db.SavePicInfo(parentFile, picName, parentFile);	// ����ͼƬ��Ϣ
						}

					}
				}
			}
		} else {
			System.out.println("�ļ�·�������ڣ�");
		}

		return lists;
	}	
	
	private static int Count(String filepath) {
		int count = 0;
		File file = new File(filepath);
		if (file.exists()) {
			File[] files = file.listFiles();

			if (files.length == 0) {
				System.out.println("�ļ���Ϊ�գ�");
				return 0;
			} else {				
				for(File file2 : files) {
					if (file2.isDirectory()) {
						count++;
					} else {
						String fileName = file2.getName();
						System.out.println(fileName);
						if (!fileName.substring(fileName.length() - 2, fileName.length()).equals("db")) {
							count++;							
						}
					}
				}
			}
		}
		
		return count;
	}
	
	/**
	 * ����ͼƬ·������ԭ��δ��ǩ��ͼƬ����Ϣ
	 * @param filePath
	 */
	public static void TraverseUnfinishPicFolder(String filePath) {
		File file = new File(filePath);
		if (file.exists()) {
			File[] files = file.listFiles();
			if (files.length == 0) {
				System.out.println("�ļ����ǿյ�");
				return;
			} else {
				for (File file2 : files) {
					if (file2.isDirectory()) {
						System.out.println("�ļ��У�" + file2.getName());
					} else {
						System.out.println("�ļ���" + file2.getName());
						DbUtils.getInstance();
						DbUtils.SaveUnfinishedPicInfo(file2.getName(), 0, DateUtils.GetNowTime());
					}
				}
			}
			     
		}
	}
	
	/**
	 * ������ɱ�ǩ����ͼƬȫ�����浽���ݿ�	
	 * @param lists
	 * @param level
	 */
	public static void f(ArrayList<String> lists,int level) {
		level++;
		for (int i = 0; i < lists.size(); i++) {
			ArrayList<String> list = FileUtils.tFolder(lists.get(i),level);

			if (list.size() > 0) {
				f(list,level);
			}
		}
	}
	
	
	/**
	 * ��������·�������ļ�
	 * @param oldPath
	 * @param newPath
	 */
	public static void copyFolder(String oldPath, String newPath) {
		int num = 0;
        try {
        	in = new FileInputStream(oldPath);	// Դ�ļ�
        	out = new FileOutputStream(newPath);// ���������ļ�
        	byte[] buf = new byte[1024];
        	int len = 0;
        	while ((len = in.read(buf)) != -1) {
        		out.write(buf, 0, len);	// ����
        		num++;
        	}

        } catch (Exception e) {
			System.out.println("copyFolder...");
			e.printStackTrace();
		} finally {
			try {
				if (in != null) {
					in.close();
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
			try {
				if (out != null) {
					out.close();
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
    }


}
