package com.portal.assignment;

import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
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

import org.apache.commons.io.FileDeleteStrategy;
import org.apache.commons.io.FileUtils;
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
		
		String AssignmentName = request.getParameter("selectedattribute");
		System.out.println("AssignmentName: "+AssignmentName);
		
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
		
		//Create sub-folder "RawFiles" under BASE folder
		String rawAssgnPath = File.separator+"tmp"+File.separator+"AssignmentSubmissions"+File.separator+"RawFiles"+File.separator+AssignmentName;
		File rawAssgnDir = new File(rawAssgnPath);
		if(!rawAssgnDir.exists())
			rawAssgnDir.mkdir();
		
		//Create userfolder in "RawFiles"
		String rawPathUser = File.separator+"tmp"+File.separator+"AssignmentSubmissions"+File.separator+"RawFiles"+File.separator+AssignmentName+File.separator+sess.getAttribute("loggeduser");
		File rawDirUser = new File(rawPathUser);
		if(!rawDirUser.exists())
			rawDirUser.mkdir();
		
		//Create sub-folder "ExtractedFiles" under BASE folder
		String extractPath = File.separator+"tmp"+File.separator+"AssignmentSubmissions"+File.separator+"ExtractedFiles";
		File extractDir = new File(extractPath);
		if(!extractDir.exists())
			extractDir.mkdir();
		
		//Create sub-folder "ExtractedFiles" under BASE folder
		String extractAssgnPath = File.separator+"tmp"+File.separator+"AssignmentSubmissions"+File.separator+"ExtractedFiles"+File.separator+AssignmentName;
		File extractAssgnDir = new File(extractAssgnPath);
		if(!extractAssgnDir.exists())
			extractAssgnDir.mkdir();		
		
		String extractPathUser = File.separator+"tmp"+File.separator+"AssignmentSubmissions"+File.separator+"ExtractedFiles"+File.separator+AssignmentName+File.separator+sess.getAttribute("loggeduser");
		File extractDirUser = new File(extractPathUser);
		if(!extractDirUser.exists())
			extractDirUser.mkdir();
		
		String rawTestPath = File.separator+"tmp"+File.separator+"AssignmentTestCases"+File.separator+AssignmentName;
				
		
		String errs = "";
		boolean folder_desc_state = false;
		try {
			filePart = request.getPart("file-0a");			
			//fileName = System.currentTimeMillis()+"_"+getFileName(filePart).replaceAll("\\s+", "_");
			fileName = getFileName(filePart).replaceAll("\\s+", "_");
			if(fileName != null && !fileName.trim().equals("")) {
					
					String zipLocationTempFile = rawDirUser.getAbsolutePath()+File.separator+fileName.split("\\.")[0]+"_tmp101"+"."+fileName.split("\\.")[1];
					String zipLocation = rawDirUser.getAbsolutePath()+File.separator+fileName;
					String extractLocation = extractDirUser.getAbsolutePath()+File.separator+fileName.split("\\.")[0];
					
					//String extractLocation = extractDirUser.getAbsolutePath();				
					/*
					 * Download / Create the ZIP file in "RawFiles" folder
					 */
					//System.out.println("FileName: "+fileSaveDir.getAbsolutePath()+File.separator+fileName);
					//filePart.write(rawDirUser.getAbsolutePath()+File.separator+fileName);					
					filePart.write(zipLocationTempFile);
					
					
					folder_desc_state = checkFolderDescription(zipLocationTempFile, AssignmentName);
					System.out.println("state : "+folder_desc_state);
					if(folder_desc_state){
						
					/*
					 * Extract the contents of the ZIP file and place it in "ExtractedFiles" folder under the foldername of the ZipFile
					 */
						
						
						//Version Control
						DBManager dbm = new DBManager();
						int count = dbm.getVersionCount(sess.getAttribute("loggeduser").toString(), AssignmentName);
						if(count > 0) {
							//rename old recent folder here	with version
							String folderLocation = extractDirUser.getAbsolutePath()+File.separator+fileName.split("\\.")[0];
							String toFolderLocation = extractDirUser.getAbsolutePath()+File.separator+fileName.split("\\.")[0]+"_V"+count;
							//File dir = new File(folderLocation);
							//File toDir = new File(toFolderLocation);
							System.out.println("Renaming Directory -103");
							//renameDirectory(dir,toDir);
							copyDirectoryToNewDirectory(folderLocation, toFolderLocation);
							//copyFileToNewFile(folderLocation, toFolderLocation);
							System.out.println("Done Directory -103");
							//rename old recent zipfile here with version
							//File zipDir = new File(zipLocation);
							System.out.println("Renaming Zip Version -104");
							//renameZip(zipDir, rawDirUser.getAbsolutePath()+File.separator+fileName.split("\\.")[0]+"_V"+count+"."+fileName.split("\\.")[1]);
							copyFileToNewFile(zipLocation, rawDirUser.getAbsolutePath()+File.separator+fileName.split("\\.")[0]+"_V"+count+"."+fileName.split("\\.")[1]);
							System.out.println("Done Zip Version -104");
							dbm.updateVersion(sess.getAttribute("loggeduser").toString(), AssignmentName, fileName.split("\\.")[0], fileName.split("\\.")[0]+"_V"+count);
							System.out.println("COUNT : "+count);
						}							
						//Version Control
					
						System.out.println("Renaming from tempfile to actual filename -101");
						//File zipDir0 = new File(zipLocationTempFile);
						//System.out.println("ZipDir0: "+zipDir0.exists()+" || "+zipDir0.canWrite());
						copyFileToNewFile(zipLocationTempFile, zipLocation);		
						//renameZip(new File(zipLocationTempFile), zipLocation);
						System.out.println("Done -101");
						
					//ArrayList<String> results = unzip(zipLocation, extractLocation);
					unzip(zipLocation, extractLocation);
					
					String testcaseFile = dbm.getTestCaseFileName(AssignmentName);
					//Copy TestCase file for the assignment to extractLocation
					copyTestCase(rawTestPath+File.separator+testcaseFile, extractLocation+File.separator+testcaseFile);
					
					/*if(results != null){
						DBManager dbm = new DBManager();
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
					}*/
					
					
					//System.out.println(dbm.checkFileExists(fileName.split("\\.")[0], AssignmentName, sess.getAttribute("loggeduser").toString()));
					if(!dbm.checkFileExists(fileName.split("\\.")[0], AssignmentName, sess.getAttribute("loggeduser").toString())){
						dbm.insertUserDetails(sess.getAttribute("loggeduser").toString(), AssignmentName, fileName.split("\\.")[0], "VERIFYING", "");
					} else {
						dbm.updateUserDetails(fileName.split("\\.")[0], AssignmentName, fileName.split("\\.")[0], "VERIFYING", "");
					}
				} else {
					System.out.println("Deleting......");
					File fremove = new File(zipLocationTempFile);
					System.out.println(fremove.getAbsolutePath());
					System.out.println(fremove.delete());					
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
				sess.setAttribute("flashmsg","Success");
			}
			else{
				pout.print("Fail");
				sess.setAttribute("flashmsg","Fail");
			}
			if(!folder_desc_state){
				sess.setAttribute("flashmsg","Zip-file content mis-match");
			}
			response.sendRedirect("SubmissionPage.jsp");
			pout.close();
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
	
	/**
	 * Creates destination directory if non-existing and places extracted content in it
	 * @param zipFilePath
	 * @param destDirectory
	 * @throws IOException
	 */
	public void unzip(String zipFilePath, String destDirectory) throws IOException {
        File destDir = new File(destDirectory);
        if (!destDir.exists()) {
            destDir.mkdir();
        }
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
        
        /*System.out.println("Evaluating....");
        Evaluator eva = new Evaluator();
        eva.processCode(destDirectory);
        ArrayList<String> execOutput = eva.getOutput();
        System.out.println("Evaluated !!");
        //System.out.println(execOutput.toString());
        return execOutput;*/
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
    
    public boolean checkFolderDescription(String zipFilePath, String Assignment) throws IOException {
		
		DBManager dbm = new DBManager();
		JSONObject jobj = new JSONObject();
		jobj = dbm.getSubmissionTemplate(Assignment);
		String folder = (String) jobj.get("folders");
		String files = (String) jobj.get("filenames");
		String[] folderslist = folder.substring(1, folder.length() - 1).split(", ");
		String[] fileslist = files.substring(1, files.length() - 1).split(",");
		String[] tempfolderlist = new String[folderslist.length];
		String[] tempfilelist = new String[fileslist.length];
		int count = 0;
		for(String item: folderslist){
			tempfolderlist[count] = item.trim();
			count+=1;
		}
		count = 0;
		for(String item: fileslist){
			tempfilelist[count] = item.trim();
			count+=1;
		}
		ArrayList<String> folderNames = new ArrayList<String>(Arrays.asList(tempfolderlist));
		ArrayList<String> fileNames = new ArrayList<String>(Arrays.asList(tempfilelist));
		ZipInputStream zipIn = new ZipInputStream(new FileInputStream(zipFilePath));
	       ZipEntry entry = zipIn.getNextEntry();
	       // iterates over entries in the zip file
	       while (entry != null) {	          
	           if (!entry.isDirectory()) {
	           	if(!fileNames.contains(entry.getName())){
	           		zipIn.closeEntry();
	           		zipIn.close();
	           		return false;	     
	           	}
	           } else {	        	   
	           	if(!folderNames.contains(entry.getName())){
	           		zipIn.closeEntry();
	           		zipIn.close();
	           		return false;
	           	}
	           }
	           zipIn.closeEntry();
	           entry = zipIn.getNextEntry();
	       }
	    zipIn.close();
		return true;
	}
    
    private void renameDirectory(File dir, File toDir) {
		// TODO Auto-generated method stub
    	System.out.println("isDirectory: "+dir.isDirectory() + "||"+ dir.getAbsolutePath()+" || "+toDir.getAbsolutePath());
		if(dir.isDirectory()) {
			System.out.println("renameDirectory: "+dir.renameTo(toDir));
		}
	}
    
	private void renameZip(File oldName, String newName){
		boolean state = oldName.renameTo(new File(newName));
		System.out.println("renameZip: "+state+" || "+oldName.getAbsolutePath()+" || "+newName);
	}
	
	private void copyFileToNewFile(String origin, String destination){
		System.out.println("origin: "+origin+" || dest: "+destination);
		File fin = new File(origin);
		File fout = new File(destination);
		
		try {
			FileUtils.copyFile(fin, fout);
			System.out.println("Filecopied");
			boolean flag = FileUtils.deleteQuietly(fin);
			System.out.println("File deleted: "+flag);
			
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
	
	private void copyTestCase(String origin, String destination){
		File fin = new File(origin);
		File fout = new File(destination);
		
		try {
			FileUtils.copyFile(fin, fout);
			System.out.println("TestCase File copied");
			
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	private void copyDirectoryToNewDirectory(String origin, String destination){
		System.out.println("origin: "+origin+" || dest: "+destination);
		File fin = new File(origin);
		File fout = new File(destination);
		
		try {
			FileUtils.copyDirectory(fin, fout);
			System.out.println("Directory Copied");
			FileUtils.deleteDirectory(fin);
			System.out.println("Folder deleted: ");
			
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

}
