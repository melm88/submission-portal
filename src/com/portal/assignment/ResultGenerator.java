package com.portal.assignment;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.json.simple.JSONObject;

/**
 * Servlet implementation class ResultGenerator
 */
@WebServlet("/ResultGenerator")
public class ResultGenerator extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public ResultGenerator() {
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
		processResult(request, response);
	}
	
	protected void processResult(HttpServletRequest request, HttpServletResponse response) throws IOException {
		
		HttpSession sess = request.getSession(false);
		if(sess == null || sess.getAttribute("loggeduser") == null)
			response.sendRedirect("index.jsp");
		
		response.setContentType("application/json");
		response.setHeader("Access-Control-Allow-Origin", "*");
		response.setHeader("Access-Control-Allow-Methods", "POST, GET");
		response.setHeader("Access-Control-Max-Age", "3600");
		
		String loggeduser = (String) sess.getAttribute("loggeduser");
		//System.out.println("in here2 "+loggeduser);
		PrintWriter out = response.getWriter();
		
		DBManager dbm = new DBManager();
		JSONObject jres = dbm.getUserProgress(loggeduser);
		
		if(jres != null){
			//System.out.println("jres: "+jres.toString());
			out.write(jres.toJSONString());
		} else {
			out.write("fail");
		}		
		out.flush();
		out.close();
		
		
	}

}
