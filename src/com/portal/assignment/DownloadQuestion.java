package com.portal.assignment;

import java.io.DataInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.zip.ZipInputStream;
import java.util.zip.ZipOutputStream;

import javax.servlet.ServletException;
import javax.servlet.ServletOutputStream;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

/**
 * Servlet implementation class DownloadQuestion
 */
@WebServlet("/DownloadQuestion")
public class DownloadQuestion extends HttpServlet {
	private static final long serialVersionUID = 1L;
	private static final int BUFSIZE = 4096;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public DownloadQuestion() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		
		HttpSession sess = request.getSession(false);
		if(sess == null || sess.getAttribute("loggeduser") == null)
			response.sendRedirect("index.jsp");
		
		String assignmentname = request.getParameter("assgn");
		String questionfile = request.getParameter("ques");
		
		System.out.println("QuestionFile: "+questionfile+" || assignmentname: "+assignmentname);
		//Link to sub-folder with AssignmentName under BASE folder
		String rawQuestionPath = File.separator+"tmp"+File.separator+"QuestionsPortal"+File.separator+assignmentname+File.separator+questionfile;
		File questionFile = new File(rawQuestionPath);
		if(questionFile.exists()){
			// setting some response headers
	        response.setHeader("Expires", "0");
	        response.setHeader("Cache-Control","must-revalidate, post-check=0, pre-check=0");
	        response.setHeader("Pragma", "public");
			response.setContentType("application/zip");
			response.setHeader("Access-Control-Allow-Origin", "*");
			response.setHeader("Access-Control-Allow-Methods", "POST, GET");
			response.setHeader("Access-Control-Max-Age", "3600");
			response.setHeader("Content-Disposition","attachment; filename=\"" + questionfile + "\"");
			
			ServletOutputStream outStream = response.getOutputStream();
			byte[] byteBuffer = new byte[BUFSIZE];
		    DataInputStream in = new DataInputStream(new FileInputStream(questionFile));
		    int length   = 0;
		    // reads the file's bytes and writes them to the response stream
		    while ((in != null) && ((length = in.read(byteBuffer)) != -1))
		    {
		        outStream.write(byteBuffer,0,length);
		    }
		    
		    in.close();
		    outStream.flush();
		    outStream.close();
						
		} else {
			System.out.println("File doesn't exist");
		}
		
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		
		
	}

}
