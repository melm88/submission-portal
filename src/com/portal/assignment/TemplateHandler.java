package com.portal.assignment;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

import javax.servlet.ServletException;
import javax.servlet.annotation.MultipartConfig;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.servlet.http.Part;

/**
 * Servlet implementation class TemplateHandler
 */
@WebServlet("/TemplateHandler")
@MultipartConfig
public class TemplateHandler extends HttpServlet {
	private static final long serialVersionUID = 1L;
	private static final int BUFFER_SIZE = 4096;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public TemplateHandler() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		processTemplate(request, response);
	}
	
	protected void processTemplate(HttpServletRequest request, HttpServletResponse response) {
		Part filePart;
		String fileName = "";
		DBManager dbm = new DBManager();
		
		HttpSession sess = request.getSession(false);
		try {
			if(sess == null)
				response.sendRedirect("index.jsp");
		} catch (IOException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		
		String AssignmentName = request.getParameter("assignmentname");
		System.out.println(AssignmentName);	
		boolean assignment_state = dbm.checkAssignment(AssignmentName);
		
		
		if(AssignmentName == null || AssignmentName.trim().equalsIgnoreCase("")){
			try {
				response.sendRedirect("SubmissionPage.jsp");
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		
		//Create BASE folder "AssignmentTemplate"does not exist.
		String templatePath = File.separator+"tmp"+File.separator+"AssignmentTemplate";
		File templateDir = new File(templatePath);
		if(!templateDir.exists())
			templateDir.mkdir();
		
		//Create sub-folder with AssignmentName under BASE folder
		String rawPath = File.separator+"tmp"+File.separator+"AssignmentTemplate"+File.separator+AssignmentName;
		File rawDir = new File(rawPath);
		if(!rawDir.exists())
			rawDir.mkdir();
		
		try {
			filePart = request.getPart("file-0a");
			fileName = getFileName(filePart).replaceAll("\\s+", "_");
			
			if(fileName != null && !fileName.trim().equals("")) {
				
				//Write File to SubmissionTemplate folder
				filePart.write(rawDir.getAbsolutePath()+File.separator+fileName);
				
				ArrayList<ArrayList<String>> contentlist = zipContents(rawDir.getAbsolutePath()+File.separator+fileName);
				if(contentlist.size()>0){
					ArrayList<String> FolderList = contentlist.get(0);
					ArrayList<String> FileList = contentlist.get(1);
					
					System.out.println(FolderList.toString());
					System.out.println(FileList.toString());
					
					if(!assignment_state){
						dbm.insertSubmissionTemplate(AssignmentName, fileName, FolderList.toString(), FileList.toString());
						System.out.println("Inserted Template to DB");
					} else {
						dbm.updateSubmissionTemplate(AssignmentName, fileName, FolderList.toString(), FileList.toString());
						System.out.println("Updated Template in DB");
					}
				}
				
				
			}			
			
		} catch (IllegalStateException | IOException | ServletException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {
			try {
				response.sendRedirect("SubmissionPage.jsp");
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		
		
	}
	
	
	/**
	 * Extracts the FileName (Part)
	 */
	private static String getFileName(Part part) {
		System.out.println("Header: "+part.getHeaderNames());
		for (String cd : part.getHeader("content-disposition").split(";")) {
			if (cd.trim().startsWith("filename")) {
				String fileName = cd.substring(cd.indexOf('=') + 1).trim().replace("\"", "");
				return fileName.substring(fileName.lastIndexOf('/') + 1).substring(fileName.lastIndexOf('\\') + 1); // MSIE fix.
			}
		}
		return null;
	}
	
	/**
	 * Iterates contents of zip file and creates a list of folders and files in it.
	 * @param zipFilePath
	 * @throws IOException
	 */
	public ArrayList<ArrayList<String>> zipContents(String zipFilePath) throws IOException {
		
		ArrayList<String> FoldersList = new ArrayList<String>();
		ArrayList<String> FilesList = new ArrayList<String>();
		
        ZipInputStream zipIn = new ZipInputStream(new FileInputStream(zipFilePath));
        ZipEntry entry = zipIn.getNextEntry();
        // iterates over entries in the zip file
        while (entry != null) {
            //String filePath = destDirectory + File.separator + entry.getName();
            if (!entry.isDirectory()) {
                // if the entry is a file, add it
                FilesList.add(entry.getName());
            } else {
                // if the entry is a directory, add the directory
                FoldersList.add(entry.getName());
            }
            zipIn.closeEntry();
            entry = zipIn.getNextEntry();
        }
        zipIn.close();
        ArrayList<ArrayList<String>> templist = new ArrayList<ArrayList<String>>();
        templist.add(FoldersList);
        templist.add(FilesList);
        return templist;
    }

}
