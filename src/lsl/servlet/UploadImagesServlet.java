package lsl.servlet;

import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import lsl.utils.DateUtils;
import lsl.utils.DbUtils;
import lsl.values.Values;

import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.FileUploadBase.FileSizeLimitExceededException;
import org.apache.commons.fileupload.disk.DiskFileItemFactory;
import org.apache.commons.fileupload.servlet.ServletFileUpload;

public class UploadImagesServlet extends HttpServlet {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private final static  long MAX_FILE_SIZE = 50 * 1024 * 1024;
	
	public final static String[] alloweExt = new String [] {"jpg","JPG","JPEG","jpeg","png"};
	
	private String result;
	
	private DiskFileItemFactory factory;
	
	private ServletFileUpload upload;
	
	private PrintWriter writer;
	
	private ArrayList<FileItem> list;
	
//	private String clinetIp;
	
	
	public UploadImagesServlet() {
		super();
	}
	
	/**
	 * The doGet method of the servlet. <br>
	 *
	 * This method is called when a form has its tag value method equals to get.
	 * 
	 * @param request the request send by the client to the server
	 * @param response the response send by the server to the client
	 * @throws ServletException if an error occurred
	 * @throws IOException if an error occurred
	 */
	public void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {

		doPost(request, response);
	}

	/**
	 * http://114.115.141.43:4040/webServer/UploadImages
	 *
	 * This method is called when a form has its tag value method equals to post.
	 * 
	 * @param request the request send by the client to the server
	 * @param response the response send by the server to the client
	 * @throws ServletException if an error occurred
	 * @throws IOException if an error occurred
	 */
	public void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {

		response.setContentType("text/html; charset=utf-8");
		request.setCharacterEncoding("UTF-8");
//		this.clinetIp = request.getRemoteAddr();
		
		factory = new DiskFileItemFactory();
		factory.setSizeThreshold(2 * 1024 * 1024);	// 缓冲区
		factory.setRepository(new File(request.getSession()	// 文件临时存放路径
				.getServletContext().getRealPath("/") + "UploadTemp"));
		
		
		upload = new ServletFileUpload(factory);	// 用硬盘文件工厂实例化上传组件
		upload.setFileSizeMax(MAX_FILE_SIZE);	// 最大上传尺寸
		
		writer = response.getWriter();
		result = "上传成功";
		
		try {
			list = (ArrayList<FileItem>) upload.parseRequest(request);
		} catch (Exception e) {
			if (e instanceof FileSizeLimitExceededException) {
				result = "文件大小超过预定大小:" + MAX_FILE_SIZE + "字节";
				return;
			}
			e.printStackTrace();
		}
		
		if (list == null || list.size() == 0) {
			result = "请选择图片";
			return;
		}
		
		String fileName;
//		Date now = new Date();
//		DateFormat dateForamt = DateFormat.getDateInstance();
//		String date = dateForamt.format(now);
		String destDirName = Values.UnfinishedIconPath;
//		String destDirName = request.getSession().getServletContext()
//		.getRealPath("/") + "RawImages\\" + date;
		System.out.println("文件上传路径：" + destDirName);
		
		File dir = new File(destDirName);
		if (!dir.exists() && !dir.isDirectory()) {
			dir.mkdirs();
		}
		
		for (FileItem item : list) {
			String[] imgFormat = item.getName().split("\\.");
			fileName = System.currentTimeMillis() + "." + imgFormat[imgFormat.length - 1];
			
			try {
				File file = new File(destDirName, fileName);
				if (file.exists()) {
					continue;
				}
				
				item.write(new File(destDirName, fileName));
				
				DbUtils.getInstance();
				DbUtils.SaveUnfinishedPicInfo(fileName, 0, DateUtils.GetNowTime()); // 将新上传的图片保存数据库
				
			} catch (Exception e) {
				result = e.getMessage();
				e.printStackTrace();
			}
		}
		
		writer.print(result);
		
		
		
	}

	/**
	 * Initialization of the servlet. <br>
	 *
	 * @throws ServletException if an error occurs
	 */
	public void init() throws ServletException {
		// Put your code here
	}

}
