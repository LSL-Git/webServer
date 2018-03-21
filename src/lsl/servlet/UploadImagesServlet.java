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
		factory.setSizeThreshold(2 * 1024 * 1024);	// ������
		factory.setRepository(new File(request.getSession()	// �ļ���ʱ���·��
				.getServletContext().getRealPath("/") + "UploadTemp"));
		
		
		upload = new ServletFileUpload(factory);	// ��Ӳ���ļ�����ʵ�����ϴ����
		upload.setFileSizeMax(MAX_FILE_SIZE);	// ����ϴ��ߴ�
		
		writer = response.getWriter();
		result = "�ϴ��ɹ�";
		
		try {
			list = (ArrayList<FileItem>) upload.parseRequest(request);
		} catch (Exception e) {
			if (e instanceof FileSizeLimitExceededException) {
				result = "�ļ���С����Ԥ����С:" + MAX_FILE_SIZE + "�ֽ�";
				return;
			}
			e.printStackTrace();
		}
		
		if (list == null || list.size() == 0) {
			result = "��ѡ��ͼƬ";
			return;
		}
		
		String fileName;
//		Date now = new Date();
//		DateFormat dateForamt = DateFormat.getDateInstance();
//		String date = dateForamt.format(now);
		String destDirName = Values.UnfinishedIconPath;
//		String destDirName = request.getSession().getServletContext()
//		.getRealPath("/") + "RawImages\\" + date;
		System.out.println("�ļ��ϴ�·����" + destDirName);
		
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
				DbUtils.SaveUnfinishedPicInfo(fileName, 0, DateUtils.GetNowTime()); // �����ϴ���ͼƬ�������ݿ�
				
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
