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
 * Servlet implementation class AjaxSubtaskCount
 */
@WebServlet("/AjaxSubtaskCount")
public class AjaxSubtaskCount extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public AjaxSubtaskCount() {
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
		HttpSession sess = request.getSession(false);
		if(sess == null || sess.getAttribute("loggeduser") == null)
			response.sendRedirect("index.jsp");
		
		if(request.getParameter("missionid") != null){		
			DBManager dbm = new DBManager();
			int counts = dbm.getSubtaskCount(Integer.parseInt(request.getParameter("missionid")));
			PrintWriter out = response.getWriter();
			System.out.println("Counts: "+counts);
			if(counts != -1){
				//System.out.println("jres: "+jobj.toString());
				out.write(String.valueOf(counts));
			} else {
				//System.out.println("fail");
				out.write("0");
			}		
			out.flush();
			out.close();
		}
	}

}
