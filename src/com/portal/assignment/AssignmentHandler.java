package com.portal.assignment;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

import javax.servlet.ServletException;
import javax.servlet.annotation.MultipartConfig;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.Part;

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
		
		//Create sub-folder "ExtractedFiles" under BASE folder
		String extractPath = File.separator+"tmp"+File.separator+"AssignmentSubmissions"+File.separator+"ExtractedFiles";
		File extractDir = new File(extractPath);
		if(!extractDir.exists())
			extractDir.mkdir();
				
		
		String errs = "";
		try {
			filePart = request.getPart("file-0a");			
			//fileName = System.currentTimeMillis()+"_"+getFileName(filePart).replaceAll("\\s+", "_");
			fileName = getFileName(filePart).replaceAll("\\s+", "_");
			if(fileName != null && !fileName.trim().equals("")) {
				/*
				 * Download / Create the ZIP file in "RawFiles" folder
				 */
				//System.out.println("FileName: "+fileSaveDir.getAbsolutePath()+File.separator+fileName);
				filePart.write(rawDir.getAbsolutePath()+File.separator+fileName);
				
				/*
				 * Extract the contents of the ZIP file and place it in "ExtractedFiles" folder under the foldername of the ZipFile
				 */
				
				String zipLocation = rawDir.getAbsolutePath()+File.separator+fileName;
				String extractLocation = extractDir.getAbsolutePath()+File.separator+fileName.split("\\.")[0];
							
				unzip(zipLocation, extractLocation);
				System.out.println("Done");
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
			if(errs.equals(""))
				pout.print("Success");
			else
				pout.print("Fail");
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		
	}
	
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

}
