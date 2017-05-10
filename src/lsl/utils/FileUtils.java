package lsl.utils;

import java.io.File;
import java.util.ArrayList;

import lsl.values.Values;

public class FileUtils {
	DbUtils db = DbUtils.getInstance();
	
	/**
	 * ��δ��ǩ����ͼƬ�ļ��У�����������浽���ݿ���
	 * @param path
	 */
	public static void traverseFolder2(String path) {
        File file = new File(path);
        if (file.exists()) {
            File[] files = file.listFiles();
            if (files.length == 0) {
                System.out.println("�ļ����ǿյ�!");
                return;
            } else {
                for (File file2 : files) {
                    if (file2.isDirectory()) {
                        System.out.println(file2.getName());
                        //traverseFolder2(file2.getAbsolutePath());	// �ݹ�
                    } else {
                    	DbUtils.getInstance();
                    	DbUtils.SaveUnfinishedImg(file2.getName(),"lsl");

                        System.out.println("�ļ�:" + file2.getName());
                    }
                }
            }
        } else {
            System.out.println("�ļ�������!");
        }
    }

	/**
	 * ����·�������ļ����ڵ��ļ��л��ļ�
	 * @param path
	 */
	public static void traverseFolder(String path, int id, int level, int num) {
		DbUtils.getInstance();
		File file = new File(path);
		if (file.exists()) {
			File[] files = file.listFiles();
			if (files.length == 0) {
				System.out.println("�ļ���Ϊ�գ�");
			} else {
				for (File filee : files) {	
					if (filee.isDirectory()) {
						System.err.println("�ļ��У�" + filee.getName() + " " + " level = " + level + " id = " + id );
						if (level <= 1) {
							id++;
						}
						num = FileNum(filee.getAbsolutePath(), filee.getName());

						DbUtils.SaveFileInfo(level, id, num, filee.getName());

					} else {
						System.out.println(filee.getName());
					}
				}
			}
		} else {
			System.out.println("�ļ������ڣ�");
		}

	}
	
	/**
	 * ����һ���ļ���·���ڵ��ļ�����
	 * @param FolderPath
	 * @return
	 */
	public static int FileNum(String FolderPath, String par_name) {
		int num = 0;
		File file = new File(FolderPath);
		if (file.exists()) {
			File[] files = file.listFiles();
			if (files.length == 0) {
				System.out.println("�ļ���Ϊ�գ�");
				return 0;
			} else {				
				for (File filee : files) {				
					if (filee.isDirectory()) {
						num++;
					} else {
						String name = filee.getName();
						if (!name.substring(name.length() - 2, name.length()).equals("db")) {
							DbUtils.getInstance();
							DbUtils.SaveImgInfo(par_name, name, par_name);
							System.out.println("par_name:" + par_name + "  label:" + par_name + "  name:" + name);
							num++;
						}
						System.out.println(name);
					}
				}
			}
		} else {
			System.out.println("�ļ������ڣ�");
		}
		return num;
	}
	
	/**
	 * �����ļ�Ŀ¼���������ݿ⣬��������һ���Ƿ����ļ�������洢�����ݿ���
	 * @param level
	 */
	public static void f(int level) {
		int i = 0;
		String id = "";
		String name = "";
		DbUtils.getInstance();
		ArrayList<String> list = DbUtils.GetIdAndFileName(level);
		
		for(;i < list.size();){
			id = list.get(i);
			name = list.get(i + 1);
			i += 2;
			traverseFolder(Values.FINISHICONPATH + "/" + name, Integer.parseInt(id), level + 1, 0);
		}
	}


}
