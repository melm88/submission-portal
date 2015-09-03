package com.portal.assignment;

import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.json.simple.JSONObject;
import org.json.simple.JSONStreamAware;
import org.json.simple.JSONValue;

import jdk.nashorn.internal.parser.JSONParser;

/**
 * Servlet implementation class MissionMap
 */
@WebServlet("/MissionMap")
public class MissionMap extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public MissionMap() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		response.getWriter().append("Served at: ").append(request.getContextPath());
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		doGet(request, response);
	}
	
	protected void missionProcess(HttpServletRequest request, HttpServletResponse response) {
		HttpSession session = request.getSession(false);
		String userName = (String) session.getAttribute("loggedusername");
		String mailId = (String) session.getAttribute("loggeduser");
		DBManager dbm = new DBManager();
		JSONObject jobj = new JSONObject();
		jobj = dbm.getMissionObj(mailId);
		if(jobj != null && jobj.get("prevMission") != null) {
			JSONObject prevMission = (JSONObject)JSONValue.parse(jobj.get("prevMission").toString());
			String currentMission = (String)jobj.get("currentMission");
			String existCurrent = (String)prevMission.get(currentMission);
			if(existCurrent != null) {				
				String nextMission = dbm.getNextMission(currentMission);
				if(nextMission != null) {
					//update database current mission to next mission
					//redirect to next mission
				} else{
					//redirect to current mission
				}
			} else {
				//redirect to current mission
			}

		} else {
			//insert user in database with current mission is 1 and prevmission is "";
			// redirect user to first mission
		}
		
	}
}
