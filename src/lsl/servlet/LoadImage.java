package lsl.servlet;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URLDecoder;
import java.net.URLEncoder;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class LoadImage extends HttpServlet {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;


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
		System.out.println("LoadImage Get...");
		
		String [] s = request.getRequestURI().split("/");	// ��ȡ����URL��ͨ��������ʽ��б�ָܷ�

		String img_name = URLDecoder.decode(s[4],"utf-8");	// �����������
		
		String path = "D:/Images/δ���/" + img_name;// ����ͼƬ·��
		
		String fileName=path.substring(path.lastIndexOf("\\")+1);
        System.out.println(fileName);
        
		response.setContentType("image/*");	// ���÷�������
		// ��Content-Type ������ΪҪ���ص�����ʱ , �����Ϣͷ��������������ļ������ֺ����͡�
		response.setHeader("Content-Disposition", "attachment;filename = "  + URLEncoder.encode(fileName, "UTF-8"));	
       
		InputStream reader = null;
        OutputStream out = null;
        byte[] bytes = new byte[1024];
        int len = 0;
        try {
            // ��ȡ�ļ�
            reader = new FileInputStream(path);
            // д��������������
            out = response.getOutputStream();

            while ((len = reader.read(bytes)) > 0) {
                out.write(bytes, 0, len);
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (reader != null) {
                reader.close();
            }
            if (out != null)
                out.close();
        }
        
	}

	/**
	 * The doPost method of the servlet. <br>
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
		this.doGet(request, response);
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
