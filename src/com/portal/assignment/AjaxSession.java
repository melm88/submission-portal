package com.portal.assignment;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

/**
 * Servlet implementation class AjaxSession
 */
@WebServlet("/AjaxSession")
public class AjaxSession extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public AjaxSession() {
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
		sessionProcess(request, response);
	}
	
	protected void sessionProcess(HttpServletRequest request, HttpServletResponse response){
		HttpSession session = request.getSession();
		if(session != null){
			session.setAttribute("loggeduser", request.getParameter("loggeduser"));
			session.setAttribute("loggedusername", request.getParameter("name"));
		}
		try {
			PrintWriter out = response.getWriter();
			if(session!=null){
				out.print(session.getAttribute("loggeduser"));
			}
			else
				out.print("fail");
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}

}
