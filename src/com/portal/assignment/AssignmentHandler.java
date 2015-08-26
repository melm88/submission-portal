package com.portal.assignment;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Arrays;
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

import org.json.simple.JSONObject;

/**
 * Servlet implementation class AssignmentHandler
 */
@WebServlet("/AssignmentHandler")
@MultipartConfig
public class AssignmentHandler extends HttpServlet {
	private static final long serialVersionUID = 1L;
	private static final int BUFFER_SIZE = 4096;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public AssignmentHandler() {
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
		process(request, response);
	}
	
	protected void process(HttpServletRequest request, HttpServletResponse response){
		Part filePart;
		String fileName = "";
		
		HttpSession sess = request.getSession(false);
		try {
			if(sess == null)
				response.sendRedirect("index.jsp");
		} catch (IOException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		
		//Create BASE folder "AssignmentSubmissions" if it does not exist.
		String submissionPath = File.separator+"tmp"+File.separator+"AssignmentSubmissions";
		File submissionDir = new File(submissionPath);
		if(!submissionDir.exists())
			submissionDir.mkdir();
		
		//Create sub-folder "RawFiles" under BASE folder
		String rawPath = File.separator+"tmp"+File.separator+"AssignmentSubmissions"+File.separator+"RawFiles";
		File rawDir = new File(rawPath);
		if(!rawDir.exists())
			rawDir.mkdir();
		
		//Create userfolder in "RawFiles"
		String rawPathUser = File.separator+"tmp"+File.separator+"AssignmentSubmissions"+File.separator+"RawFiles"+File.separator+sess.getAttribute("loggeduser");
		File rawDirUser = new File(rawPathUser);
		if(!rawDirUser.exists())
			rawDirUser.mkdir();
		
		//Create sub-folder "ExtractedFiles" under BASE folder
		String extractPath = File.separator+"tmp"+File.separator+"AssignmentSubmissions"+File.separator+"ExtractedFiles";
		File extractDir = new File(extractPath);
		if(!extractDir.exists())
			extractDir.mkdir();
		
		String extractPathUser = File.separator+"tmp"+File.separator+"AssignmentSubmissions"+File.separator+"ExtractedFiles"+File.separator+sess.getAttribute("loggeduser");
		File extractDirUser = new File(extractPathUser);
		if(!extractDirUser.exists())
			extractDirUser.mkdir();
				
		
		String errs = "";
		try {
			filePart = request.getPart("file-0a");	
			String assignmentName = "1"; //get assignmentname
			
			//Version Control
			DBManager dbm = new DBManager();
			int count = dbm.getVersionCount(sess.getAttribute("loggeduser").toString(), assignmentName);
			if(count > 0) {
				//rename old recent folder here	with version
				File dir = new File("dir");
				File toDir = new File("newDir");
				renameDirectory(dir,toDir);
				//rename old recent zipfile here with version
				
			}
			
					
			//Version Control
			
			fileName = getFileName(filePart).replaceAll("\\s+", "_");
			//fileName = getFileName(filePart).replaceAll("\\s+", "_");
			if(fileName != null && !fileName.trim().equals("")) {
				String zipLocation = rawDirUser.getAbsolutePath()+File.separator+fileName;
				boolean state = checkFolderDescription(zipLocation, assignmentName);
				if(state) {
					//code run
				} else {
					//redirect to submission page saying given zip file doesn't contain the expected folders/files
				}
				/*
				 * Download / Create the ZIP file in "RawFiles" folder
				 */
				//System.out.println("FileName: "+fileSaveDir.getAbsolutePath()+File.separator+fileName);
				filePart.write(rawDirUser.getAbsolutePath()+File.separator+fileName);
				
				/*
				 * Extract the contents of the ZIP file and place it in "ExtractedFiles" folder under the foldername of the ZipFile
				 */
				
				//String zipLocation = rawDirUser.getAbsolutePath()+File.separator+fileName;
				//String extractLocation = extractDirUser.getAbsolutePath()+File.separator+fileName.split("\\.")[0];
				String extractLocation = extractDirUser.getAbsolutePath();
							
				ArrayList<String> results = unzip(zipLocation, extractLocation);
				//System.out.println(checkFolderDescription(zipLocation, "1"));
				if(results != null){
					//DBManager dbm = new DBManager();
					if(results.get(0).trim().equals("0")){
						if(!dbm.checkFileExists(fileName.split("\\.")[0]))
							dbm.insertUserDetails(sess.getAttribute("loggeduser").toString(), fileName.split("\\.")[0], "VERIFYING", "");
						else
							dbm.updateUserDetails(fileName.split("\\.")[0], "VERIFYING", "");
					} else {
						String errStatement = "Error Code: "+results.get(0)+"\n"+"Error: "+results.get(2)+"\n"+"Output: "+results.get(1);
						if(!dbm.checkFileExists(fileName.split("\\.")[0]))
							dbm.insertUserDetails(sess.getAttribute("loggeduser").toString(), fileName.split("\\.")[0], "FAIL", errStatement);
						else
							dbm.updateUserDetails(fileName.split("\\.")[0], "FAIL", errStatement);
					}
					System.out.println("Done");
				} else {
					System.out.println("Fail");
				}
				
			}
			//InputStream fileContent = filePart.getInputStream();
			
			
		} catch (IllegalStateException | IOException | ServletException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			errs = e.getMessage();
		} // Retrieves <input type="file" name="file">
		
		PrintWriter pout;
		try {
			pout = response.getWriter();
			if(errs.equals("")){
				pout.print("Success");
				response.sendRedirect("ViewResults.jsp");
			}
			else
				pout.print("Fail");
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		
	}
	


	/**
	 * Extracts the FileName (Part)
	 */
	private static String getFileName(Part part) {
		//System.out.println("Header: "+part.getHeaderNames());
		for (String cd : part.getHeader("content-disposition").split(";")) {
			if (cd.trim().startsWith("filename")) {
				String fileName = cd.substring(cd.indexOf('=') + 1).trim().replace("\"", "");
				return fileName.substring(fileName.lastIndexOf('/') + 1).substring(fileName.lastIndexOf('\\') + 1); // MSIE fix.
			}
		}
		return null;
	}
	public boolean checkFolderDescription(String zipFilePath, String Assignment) throws IOException {
		
		DBManager dbm = new DBManager();
		JSONObject jobj = new JSONObject();
		jobj = dbm.getSubmissionTemplate(Assignment);
		String folder = (String) jobj.get("folders");
		String files = (String) jobj.get("filenames");
		String[] folderslist = folder.substring(1, folder.length() - 1).split(",");
		String[] fileslist = files.substring(1, files.length() - 1).split(",");
		System.out.println("Folders :   " + folderslist.toString());
		System.out.println("Files :   " + fileslist.toString());
		ArrayList<String> folderNames = new ArrayList<String>(Arrays.asList(folderslist));
		ArrayList<String> fileNames = new ArrayList<String>(Arrays.asList(fileslist));
		ZipInputStream zipIn = new ZipInputStream(new FileInputStream(zipFilePath));
	        ZipEntry entry = zipIn.getNextEntry();
	        // iterates over entries in the zip file
	        while (entry != null) {
	           
	            if (!entry.isDirectory()) {
	            	if(!fileNames.contains(entry.getName()))
	            		return false;
	            	
	            } else {
	            	if(!folderNames.contains(entry.getName()))
	            		return false;
	            }
	            zipIn.closeEntry();
	            entry = zipIn.getNextEntry();
	        }
	        zipIn.close();
		return true;
	}
	/**
	 * Creates destination directory if non-existing and places extracted content in it
	 * @param zipFilePath
	 * @param destDirectory
	 * @throws IOException
	 */
	public ArrayList<String> unzip(String zipFilePath, String destDirectory) throws IOException {
        /*File destDir = new File(destDirectory);
        if (!destDir.exists()) {
            destDir.mkdir();
        }*/
        ZipInputStream zipIn = new ZipInputStream(new FileInputStream(zipFilePath));
        ZipEntry entry = zipIn.getNextEntry();
        // iterates over entries in the zip file
        while (entry != null) {
            String filePath = destDirectory + File.separator + entry.getName();
            if (!entry.isDirectory()) {
                // if the entry is a file, extracts it
                extractFile(zipIn, filePath);
            } else {
                // if the entry is a directory, make the directory
                File dir = new File(filePath);
                dir.mkdir();
            }
            zipIn.closeEntry();
            entry = zipIn.getNextEntry();
        }
        zipIn.close();
        System.out.println("Evaluating....");
        Evaluator eva = new Evaluator();
        eva.processCode(destDirectory);
        ArrayList<String> execOutput = eva.getOutput();
        System.out.println("Evaluated !!");
        //System.out.println(execOutput.toString());
        return execOutput;
    }
	
	/**
     * Extracts a zip entry (file entry)
     * @param zipIn
     * @param filePath
     * @throws IOException
     */
    private void extractFile(ZipInputStream zipIn, String filePath) throws IOException {
        BufferedOutputStream bos = new BufferedOutputStream(new FileOutputStream(filePath));
        byte[] bytesIn = new byte[BUFFER_SIZE];
        int read = 0;
        while ((read = zipIn.read(bytesIn)) != -1) {
            bos.write(bytesIn, 0, read);
        }
        bos.close();
    }

    
    
	private void renameDirectory(File dir, File toDir) {
		// TODO Auto-generated method stub
		if(dir.isDirectory()) {
			dir.renameTo(toDir);
		}
	}
    
	private void renameZip(File oldName, String newName){
		oldName.renameTo(new File(newName));
	}
}
