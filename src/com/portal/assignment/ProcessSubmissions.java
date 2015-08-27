package com.portal.assignment;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.json.simple.JSONObject;
import org.json.simple.JSONValue;

/**
 * Servlet implementation class ProcessSubmissions
 */
@WebServlet("/ProcessSubmissions")
public class ProcessSubmissions extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public ProcessSubmissions() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		processRequest(request,response);
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		processRequest(request, response);
	}
	
	protected void processRequest(HttpServletRequest request, HttpServletResponse response){
		
		DBManager dbm = new DBManager();
		
		ArrayList<ArrayList<String>> maintemp = dbm.getUnProcessedAssignments();
		System.out.println(maintemp);
		if(maintemp != null){
			System.out.println(maintemp.toString());
			for(ArrayList<String> arr : maintemp){
				String emailid = arr.get(0);
				String assignmentname = arr.get(1);
				String filename = arr.get(2);
				String testfile = getTestCaseName(assignmentname, dbm);
				
				String executionFilePath = File.separator+"tmp"+File.separator+"AssignmentSubmissions"+File.separator+"ExtractedFiles"+File.separator+assignmentname+File.separator+emailid+File.separator+filename;
				
				Evaluator eva = new Evaluator();
				eva.processCode(executionFilePath, testfile);
				ArrayList<String> temp = eva.getOutput();
				String output = temp.get(1);
				if(output!=null && !output.trim().equals("")){
					JSONObject jobj = (JSONObject) JSONValue.parse(output);
					System.out.println(jobj.toString());
					if(temp.get(0).equals("0")){
						dbm.updateProcessedScoreFeedback(emailid, assignmentname, filename, "PASS", jobj.get("summary").toString(), jobj.get("score").toString());
					} else {
						dbm.updateProcessedScoreFeedback(emailid, assignmentname, filename, "FAIL", jobj.get("summary").toString(), jobj.get("score").toString());
					}
					
				}				
			}
		}
	}
	
	public String getTestCaseName(String assignment, DBManager dbm){
		String testfile = dbm.getTestCaseFileName(assignment);
		return testfile;
	}

}
