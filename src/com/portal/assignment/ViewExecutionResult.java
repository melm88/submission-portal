package com.portal.assignment;

import java.io.IOException;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.json.simple.JSONObject;

/**
 * Servlet implementation class ViewExecutionResult
 */
@WebServlet("/ViewExecutionResult")
public class ViewExecutionResult extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public ViewExecutionResult() {
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
		String filename = request.getParameter("file");
		String user = sess.getAttribute("loggeduser").toString();
		
		DBManager dbm = new DBManager();
		JSONObject jobj = (JSONObject) dbm.getUserSubmissionReport(user, assignmentname, filename);
		
		System.out.println("ViewExecutionReport: "+jobj.toString());
		
		request.setAttribute("score", jobj.get("score").toString());
		request.setAttribute("feedback", jobj.get("feedback").toString());
		request.setAttribute("submissiontime", jobj.get("timestamp").toString());
		request.setAttribute("status", jobj.get("status").toString());
		
		System.out.println(request.getAttribute("score"));
		
		dbm.updateViewedON(user, assignmentname, filename);
		
		RequestDispatcher rd = request.getRequestDispatcher("ViewReport.jsp");
		rd.forward(request, response);
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
	}

}
