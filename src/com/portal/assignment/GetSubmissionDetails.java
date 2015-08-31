package com.portal.assignment;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Servlet implementation class GetSubmissionDetails
 */
@WebServlet("/GetSubmissionDetails")
public class GetSubmissionDetails extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public GetSubmissionDetails() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		String assignmentname = request.getParameter("assgn");
		
		DBManager dbm = new DBManager();
		ArrayList<ArrayList<String>> maintemp = dbm.getSubmissionTimeDetails(assignmentname);
		PrintWriter out = response.getWriter();
		if(maintemp != null){
			out.print("<table><tr><th>Email ID</th><th>Assignment Name</th><th>File Name</th><th>STATUS</th><th>Submission Time</th></tr>");
			for(ArrayList<String> arr: maintemp){
				out.print("<tr>");
				for(String ele : arr){
					out.print("<td>"+ele+"</td>");
				}
				out.print("</tr>");
			}
			out.print("</table>");
		} else {
			out.print("No Data "+assignmentname);
		}
		
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
	}

}
