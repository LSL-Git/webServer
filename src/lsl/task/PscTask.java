package lsl.task;

import lsl.task.Imp.PscImp;
import lsl.utils.DbUtils;
import lsl.utils.FileUtils;
import lsl.values.Values;

public class PscTask implements PscImp {

	DbUtils db = DbUtils.getInstance();
	private String rt = "";
	
	public boolean isOK(String picName) {
		return db.getCountByPicName(Values.TB_IN_PROGESS,picName) >= db.getRelyOnNum();
	}

	public boolean wordCount(String picName) {
		rt = db.getLabelJudge(picName);
		return !rt.equals("");
	}

	public void mainTask(String picName) {
		if (isOK(picName)) {
			if(wordCount(picName)) {
				if(updateDb(picName)) {
					FileUtils.copyFolder(Values.UnfinishedIconPath + "/" + picName, Values.FINISHICONPATH + rt + "/" + picName);
					db.UpdateUnfinishState(picName, 1);
				}
			}
		}
		
	}

	public boolean updateDb(String picName) {
		System.out.println(rt);
		boolean flag = false;
		if (!rt.equals("")) {
			String [] las = rt.split("/");
			for (int i = 0; i < las.length; i++) {
				if (i >= 1) {
//				System.out.println(las[i] + "  level:" +  i + "parentName:" + las[i-1]);	
					if (db.addFile(i, las[i - 1], las[i]) && i == las.length - 1) {
						flag = db.SavePicInfo(las[i], picName, las[i]);
					}					
				}
			}			
		}
		return flag;
	}

}
