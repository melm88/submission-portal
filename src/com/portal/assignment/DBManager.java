package com.portal.assignment;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.TimeZone;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

public class DBManager {
	
	// JDBC driver name and database URL
    static final String JDBC_DRIVER="com.mysql.jdbc.Driver";  
    static final String DB_URL="jdbc:mysql://localhost/SUBPORTAL";
    
    //  Database credentials
    static final String USER = "root";
    static final String PASS = "root";
	
	public DBManager(){
		
		Connection con = openConnection();
    	Statement stmt1 = null, stmt2 = null, stmt3 = null, stmt4 = null, stmt5 = null;
    	try {
    		stmt1 = con.createStatement();
			String sql = "CREATE TABLE IF NOT EXISTS USERS ("
					+"emailid VARCHAR(255) not null, role VARCHAR(255) not null,"
					+" addedon VARCHAR(255), PRIMARY KEY(emailid))";
			stmt1.executeUpdate(sql);
			stmt2 = con.createStatement();
			sql = "CREATE TABLE IF NOT EXISTS SUBMISSIONTEMPLATE ("
					+"assignmentname VARCHAR(255) not null, questionfile VARCHAR(255), zipfile VARCHAR(255), testcase VARCHAR(255), folder TEXT, "
					+"filenames TEXT, createdon VARCHAR(255), PRIMARY KEY(assignmentname))";
			stmt2.executeUpdate(sql);
			stmt3 = con.createStatement();
			sql = "CREATE TABLE IF NOT EXISTS ASSIGNMENTS ("
					+"emailid VARCHAR(255) not null, assignmentname VARCHAR(255) not null, filename VARCHAR(255) not null,"
					+"status VARCHAR(255), score VARCHAR(255), feedback TEXT, submissiontime VARCHAR(255), viewedon VARCHAR(255), PRIMARY KEY(emailid, assignmentname, filename), FOREIGN KEY(assignmentname) REFERENCES SUBMISSIONTEMPLATE(assignmentname))";
			stmt3.executeUpdate(sql);
			stmt4 = con.createStatement();
			sql = "CREATE TABLE IF NOT EXISTS MISSION ("
					+"mid MEDIUMINT NOT NULL AUTO_INCREMENT, title VARCHAR(255) not null, description TEXT, assignmentname VARCHAR(255), createdon VARCHAR(255), PRIMARY KEY(mid), FOREIGN KEY(assignmentname) REFERENCES SUBMISSIONTEMPLATE(assignmentname))";
			stmt4.executeUpdate(sql);
			stmt5 = con.createStatement();
				sql = "CREATE TABLE IF NOT EXISTS SUBTASK ("
						+"sid MEDIUMINT NOT NULL AUTO_INCREMENT, description TEXT, missionid MEDIUMINT, assignmentname VARCHAR(255), createdon VARCHAR(255), PRIMARY KEY(sid), FOREIGN KEY(assignmentname) REFERENCES SUBMISSIONTEMPLATE(assignmentname), FOREIGN KEY(missionid) REFERENCES MISSION(mid))";
			stmt5.executeUpdate(sql);
    	} catch(Exception e){
    		e.printStackTrace();
    	} finally {
    		try{
    			if(stmt1 != null)
    				stmt1.close();
    			if(stmt2 != null)
    				stmt2.close();
    			if(stmt3 != null)
    				stmt3.close();
    			if(stmt4 != null)
    				stmt4.close();
    			if(stmt5 != null)
    				stmt5.close();
    			closeConnection(con);
    		} catch(Exception e){
    			e.printStackTrace();
    		}
    	}
	}
	
	private Connection openConnection() {
		try{
			// Register JDBC driver
			Class.forName("com.mysql.jdbc.Driver");

			// Open a connection
			Connection conn = DriverManager.getConnection(DB_URL,USER,PASS);
			return conn;
		}catch(Exception e) {
			e.printStackTrace();
		}
		return null;
	}
	
	private void closeConnection(Connection con) {
		try{
			if(con != null)
				con.close();
		}catch(Exception e){
			e.printStackTrace();
		}
	}
	
	public String getDBTimestamp() {
		  Date date = new Date();
		  DateFormat istFormat = new SimpleDateFormat();
		  DateFormat gmtFormat = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
		  TimeZone gmtTime = TimeZone.getTimeZone("GMT");
		  TimeZone istTime = TimeZone.getTimeZone("IST");
		  
		  istFormat.setTimeZone(gmtTime);
		  gmtFormat.setTimeZone(istTime);
		  System.out.println("Date Time: "+ date);
		  System.out.println("GMT Time: " + istFormat.format(date));
		  System.out.println("IST Time: " + gmtFormat.format(date));		  
		  return gmtFormat.format(date);
	}
	
	 public void insertUserDetails(String name, String assignment, String filename, String status, String feedback) {
	    	Connection conn = openConnection();
	    	PreparedStatement stmt = null;
	    	System.out.println(name+" "+assignment+" "+filename+" "+status+" "+feedback);
	    	// Execute SQL query
	        try {
	        	String sql = "INSERT INTO ASSIGNMENTS (emailid, assignmentname, filename, status, score, feedback, submissiontime, viewedon) VALUES (?,?,?,?,?,?,?,?)";
				stmt = conn.prepareStatement(sql);
				stmt.setString(1, name);
				stmt.setString(2, assignment);
				stmt.setString(3, filename);
				stmt.setString(4, status);
				stmt.setString(5, "");
				stmt.setString(6, feedback);
				stmt.setString(7, getDBTimestamp());
				stmt.setString(8, "");
				//stmt.setTimestamp(5, "");
				int state = stmt.executeUpdate();
				//System.out.println("InsertUserDetails state: "+state+" | "+name+","+pwd+","+role+church);
				
				
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} finally {
				try {
					stmt.close();
					closeConnection(conn);
				} catch (SQLException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}	
	}
	 
	 public void insertSubmissionTemplate(String assignment, String zipname, String testfile, String foldername, String filename, String questionfile){
		 	Connection conn = openConnection();
	    	PreparedStatement stmt = null;
	    	
	    	String sql = "INSERT INTO SUBMISSIONTEMPLATE(assignmentname, zipfile, testcase, folder, filenames, createdon, questionfile) VALUES (?,?,?,?,?,?,?)";
			try {
				stmt = conn.prepareStatement(sql);
				stmt.setString(1, assignment);
				stmt.setString(2, zipname);
				stmt.setString(3, testfile);
				stmt.setString(4, foldername);
				stmt.setString(5, filename);
				stmt.setString(6, getDBTimestamp());
				stmt.setString(7, questionfile);
				int state = stmt.executeUpdate();
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} finally {
				try {
					stmt.close();
					closeConnection(conn);
				} catch (SQLException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}		 											
	 }
	 
	 public void updateSubmissionTemplate(String assignment, String zipname, String testfile, String foldername, String filename, String questionfile) {
		 	Connection conn = openConnection();
	    	PreparedStatement stmt = null;
	    	
	    	// Execute SQL query
	        try {
	        	String sql = "UPDATE SUBMISSIONTEMPLATE SET zipfile=?, testcase=?, folder=?, filenames=?, createdon=?, questionfile=? WHERE assignmentname=?";
				stmt = conn.prepareStatement(sql);
				stmt.setString(1, zipname);
				stmt.setString(2, testfile);
				stmt.setString(3, foldername);
				stmt.setString(4, filename);
				stmt.setString(5, getDBTimestamp());
				stmt.setString(6, questionfile);
				stmt.setString(7, assignment);
				int state = stmt.executeUpdate();
				//System.out.println("InsertUserDetails state: "+state+" | "+name+","+pwd+","+role+church);
				
				
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} finally {
				try {
					stmt.close();
					closeConnection(conn);
				} catch (SQLException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}	
	 }
	 
	 public JSONObject getSubmissionTemplate(String assignment){
		 	Connection conn = openConnection();
		    PreparedStatement stmt = null;
		    // Execute SQL query
	        try {
	        	String sql = "SELECT folder, filenames FROM SUBMISSIONTEMPLATE WHERE assignmentname=?";
				stmt = conn.prepareStatement(sql);
				stmt.setString(1, assignment);			
				ResultSet rs = stmt.executeQuery();
				//System.out.println("InsertUserDetails state: "+state+" | "+name+","+pwd+","+role+church);
				if(rs!=null){
					JSONObject jObj = new JSONObject();
					while(rs.next()){					
						jObj.put("folders", rs.getString("folder"));
						jObj.put("filenames", rs.getString("filenames"));						
					}					
					return jObj;
				}
								
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} finally {
				try {
					stmt.close();
					closeConnection(conn);
				} catch (SQLException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}	        
		 return null;
	 }
	 
	 public boolean checkAssignment(String assignment){
		 	Connection conn = openConnection();
	    	PreparedStatement stmt = null;
	    	// Execute SQL query
	        try {
	        	String sql = "SELECT assignmentname FROM SUBMISSIONTEMPLATE WHERE assignmentname=?";
				stmt = conn.prepareStatement(sql);
				stmt.setString(1, assignment);			
				ResultSet rs = stmt.executeQuery();
				//System.out.println("InsertUserDetails state: "+state+" | "+name+","+pwd+","+role+church);
				if(rs!=null){
					if(rs.next()){
						return true;
					}
				}				
				
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} finally {
				try {
					stmt.close();
					closeConnection(conn);
				} catch (SQLException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
	        return false;
	 }
	 
	 public String getTestCaseFileName(String assignment){
		 Connection conn = openConnection();
	    	PreparedStatement stmt = null;
	    	// Execute SQL query
	        try {
	        	String sql = "SELECT testcase FROM SUBMISSIONTEMPLATE WHERE assignmentname=?";
				stmt = conn.prepareStatement(sql);
				stmt.setString(1, assignment);			
				ResultSet rs = stmt.executeQuery();
				//System.out.println("InsertUserDetails state: "+state+" | "+name+","+pwd+","+role+church);
				if(rs!=null){
					while(rs.next()){
						return rs.getString(1);
					}
				}				
				
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} finally {
				try {
					stmt.close();
					closeConnection(conn);
				} catch (SQLException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
	        return "";
	 }
	 
	 public String getQuestionFileName(String assignment){
		 Connection conn = openConnection();
	    	PreparedStatement stmt = null;
	    	// Execute SQL query
	        try {
	        	String sql = "SELECT questionfile FROM SUBMISSIONTEMPLATE WHERE assignmentname=?";
				stmt = conn.prepareStatement(sql);
				stmt.setString(1, assignment);			
				ResultSet rs = stmt.executeQuery();
				//System.out.println("InsertUserDetails state: "+state+" | "+name+","+pwd+","+role+church);
				if(rs!=null){
					while(rs.next()){
						return rs.getString(1);
					}
				}				
				
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} finally {
				try {
					stmt.close();
					closeConnection(conn);
				} catch (SQLException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
	        return "";
	 }
	 
	 public JSONObject getAllAssignments(){
		 	Connection conn = openConnection();
	    	PreparedStatement stmt = null;
	    	// Execute SQL query
	        try {
	        	String sql = "SELECT assignmentname FROM SUBMISSIONTEMPLATE";
				stmt = conn.prepareStatement(sql);
				//stmt.setString(1, assignment);			
				ResultSet rs = stmt.executeQuery();
				//System.out.println("InsertUserDetails state: "+state+" | "+name+","+pwd+","+role+church);
				if(rs!=null){
					JSONObject jObj = new JSONObject();
					JSONArray jarr = new JSONArray();
					while(rs.next()){
						jarr.add(rs.getString(1));
					}
					System.out.println(jarr.toString());
					jObj.put("assignments", jarr);
					return jObj;
				}				
				
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} finally {
				try {
					stmt.close();
					closeConnection(conn);
				} catch (SQLException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
	        return null;
	 }
	 
	 public JSONObject getAllMissions(){
		 	Connection conn = openConnection();
	    	PreparedStatement stmt = null;
	    	// Execute SQL query
	        try {
	        	String sql = "SELECT mid, title FROM MISSION ORDER BY title";
				stmt = conn.prepareStatement(sql);
				//stmt.setString(1, assignment);			
				ResultSet rs = stmt.executeQuery();
				//System.out.println("InsertUserDetails state: "+state+" | "+name+","+pwd+","+role+church);
				if(rs!=null){
					JSONObject jObj = new JSONObject();
					JSONArray titles = new JSONArray();
					JSONArray mid = new JSONArray();
					while(rs.next()){
						mid.add(rs.getString(1));
						titles.add(rs.getString(2));
					}
					System.out.println(mid.toString());
					System.out.println(titles.toString());
					jObj.put("mid", mid);
					jObj.put("titles", titles);
					return jObj;
				}				
				
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} finally {
				try {
					stmt.close();
					closeConnection(conn);
				} catch (SQLException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
	        return null;
	 }
	 
	 public int getSubtaskCount(int mission){
		 	Connection conn = openConnection();
	    	PreparedStatement stmt = null;
	    	// Execute SQL query
	        try {
	        	String sql = "SELECT count(sid) FROM SUBTASK WHERE missionid=?";
				stmt = conn.prepareStatement(sql);
				stmt.setInt(1, mission);
				//stmt.setString(1, assignment);			
				ResultSet rs = stmt.executeQuery();
				//System.out.println("InsertUserDetails state: "+state+" | "+name+","+pwd+","+role+church);
				if(rs!=null){
					while(rs.next()){
						return rs.getInt(1);
					}
					return -1;
				}				
				
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} finally {
				try {
					stmt.close();
					closeConnection(conn);
				} catch (SQLException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
	        return -1;
	 }
	 
	 public boolean checkFileExists(String filename, String assignment, String emailid){
		 Connection conn = openConnection();
	    PreparedStatement stmt = null;
	    // Execute SQL query
	    //System.out.println("checkFileExists: "+emailid+" "+assignment+" "+filename);
        try {
        	String sql = "SELECT emailid FROM ASSIGNMENTS WHERE filename=? and assignmentname=? and emailid=?";
			stmt = conn.prepareStatement(sql);
			stmt.setString(1, filename);
			stmt.setString(2, assignment);
			stmt.setString(3, emailid);
			ResultSet rs = stmt.executeQuery();
			//System.out.println("InsertUserDetails state: "+state+" | "+name+","+pwd+","+role+church);
			if(rs!=null){
				//System.out.println("inside...");
				while(rs.next()){
					//System.out.println("CHECKFILE: true");
					return true;
				}
			}
			
			
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {
			try {
				stmt.close();
				closeConnection(conn);
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
        return false;
	 }
	 
	 public ArrayList<ArrayList<String>> getUnProcessedAssignments(){
		Connection conn = openConnection();
	    PreparedStatement stmt = null;
	    // Execute SQL query
	    //System.out.println("checkFileExists: "+emailid+" "+assignment+" "+filename);
        try {
        	String sql = "SELECT emailid, assignmentname, filename FROM ASSIGNMENTS WHERE status=?";
			stmt = conn.prepareStatement(sql);
			stmt.setString(1, "VERIFYING");
			ResultSet rs = stmt.executeQuery();
			//System.out.println("InsertUserDetails state: "+state+" | "+name+","+pwd+","+role+church);
			if(rs!=null){
				//System.out.println("inside...");
				ArrayList<ArrayList<String>> maintemp = new ArrayList<ArrayList<String>>();
				while(rs.next()){
					//System.out.println("CHECKFILE: true");
					ArrayList<String> temp = new ArrayList<String>();
					temp.add(rs.getString(1));
					temp.add(rs.getString(2));
					temp.add(rs.getString(3));
					maintemp.add(temp);
				}
				return maintemp;
			}
			
			
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {
			try {
				stmt.close();
				closeConnection(conn);
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
        return null;
	 }
	 
	 public void updateProcessedScoreFeedback(String emailid, String assignment, String filename, String status, String feedback, String score) {
		 	Connection conn = openConnection();
	    	PreparedStatement stmt = null;
	    	
	    	// Execute SQL query
	        try {
	        	String sql = "UPDATE ASSIGNMENTS SET status=?, score=?, feedback=? WHERE filename=? and assignmentname=? and emailid=?";
				stmt = conn.prepareStatement(sql);
				stmt.setString(1, status);
				stmt.setString(2, score);
				stmt.setString(3, feedback);
				stmt.setString(4, filename);
				stmt.setString(5, assignment);
				stmt.setString(6, emailid);

				int state = stmt.executeUpdate();
				//System.out.println("InsertUserDetails state: "+state+" | "+name+","+pwd+","+role+church);
				
				
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} finally {
				try {
					stmt.close();
					closeConnection(conn);
				} catch (SQLException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}	
	 }
	 
	 public void updateUserDetails(String emailid, String assignment, String filename, String status, String feedback) {
		 	Connection conn = openConnection();
	    	PreparedStatement stmt = null;
	    	
	    	// Execute SQL query
	        try {
	        	String sql = "UPDATE ASSIGNMENTS SET status=?, feedback=? WHERE filename=? and assignmentname=? and emailid=?";
				stmt = conn.prepareStatement(sql);
				stmt.setString(1, status);
				stmt.setString(2, feedback);
				stmt.setString(3, filename);
				stmt.setString(4, assignment);
				stmt.setString(5, emailid);

				int state = stmt.executeUpdate();
				//System.out.println("InsertUserDetails state: "+state+" | "+name+","+pwd+","+role+church);
				
				
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} finally {
				try {
					stmt.close();
					closeConnection(conn);
				} catch (SQLException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}	
	 }
	 
	 public void updateVersion(String emailid, String assignment, String oldfilename, String newfilename) {
		 	Connection conn = openConnection();
	    	PreparedStatement stmt = null;
	    	
	    	// Execute SQL query
	        try {
	        	String sql = "UPDATE ASSIGNMENTS SET filename=? WHERE filename=? and assignmentname=? and emailid=?";
				stmt = conn.prepareStatement(sql);
				stmt.setString(1, newfilename);
				stmt.setString(2, oldfilename);
				stmt.setString(3, assignment);
				stmt.setString(4, emailid);

				int state = stmt.executeUpdate();
				//System.out.println("InsertUserDetails state: "+state+" | "+name+","+pwd+","+role+church);
				
				
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} finally {
				try {
					stmt.close();
					closeConnection(conn);
				} catch (SQLException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}	
	 }
	 
	 
	 public void updateViewedON(String emailid, String assignment, String filename) {
		 	Connection conn = openConnection();
	    	PreparedStatement stmt = null;
	    	
	    	// Execute SQL query
	        try {
	        	String sql = "UPDATE ASSIGNMENTS SET viewedon=? WHERE filename=? and assignmentname=? and emailid=?";
				stmt = conn.prepareStatement(sql);
				stmt.setString(1, getDBTimestamp());
				stmt.setString(2, filename);
				stmt.setString(3, assignment);
				stmt.setString(4, emailid);

				int state = stmt.executeUpdate();
				//System.out.println("InsertUserDetails state: "+state+" | "+name+","+pwd+","+role+church);
				
				
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} finally {
				try {
					stmt.close();
					closeConnection(conn);
				} catch (SQLException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}	
	 }
	 
	// get the no of versions of same assignment of the user
		public int getVersionCount(String mailId, String assignment){
				int count = 0;
				Connection conn = openConnection();
		   	PreparedStatement stmt = null;
		   	// Execute SQL query
		       try {
		       	String sql = "SELECT filename FROM ASSIGNMENTS WHERE assignmentname=? AND emailid=?";
					stmt = conn.prepareStatement(sql);
					stmt.setString(1, assignment);
					stmt.setString(2, mailId);
					ResultSet rs = stmt.executeQuery();
					//System.out.println("InsertUserDetails state: "+state+" | "+name+","+pwd+","+role+church);
					if(rs!=null){
						while(rs.next()){
							count++;
						}
					}				
					
				} catch (SQLException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} finally {
					try {
						stmt.close();
						closeConnection(conn);
					} catch (SQLException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
		       return count;
		}
	 
	 
	 public JSONObject getUserProgress(String emailid, String assignment){
		 	Connection conn = openConnection();
		    PreparedStatement stmt = null;
		    // Execute SQL query
	        try {
	        	String sql = "SELECT * FROM ASSIGNMENTS WHERE emailid=? and assignmentname=?";
				stmt = conn.prepareStatement(sql);
				stmt.setString(1, emailid);
				stmt.setString(2, assignment);
				ResultSet rs = stmt.executeQuery();
				//System.out.println("InsertUserDetails state: "+state+" | "+name+","+pwd+","+role+church);
				if(rs!=null){
					JSONObject jObj = new JSONObject();
					JSONArray jarr = new JSONArray();
					while(rs.next()){
						JSONObject temp = new JSONObject();
						temp.put("emailid", rs.getString("emailid"));
						temp.put("assignment", rs.getString("assignmentname"));
						temp.put("filename", rs.getString("filename"));
						temp.put("status", rs.getString("status"));
						temp.put("feedback", rs.getString("feedback"));
						temp.put("score", rs.getString("score"));
						temp.put("timestamp", rs.getString("submissiontime"));
						jarr.add(temp);
					}
					jObj.put(emailid, jarr);
					return jObj;
				}
				
				
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} finally {
				try {
					stmt.close();
					closeConnection(conn);
				} catch (SQLException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
	        
		 return null;
	 }
	 
	 public JSONObject getUserSubmissionReport(String emailid, String assignment, String filename){
		 	Connection conn = openConnection();
		    PreparedStatement stmt = null;
		    // Execute SQL query
	        try {
	        	String sql = "SELECT * FROM ASSIGNMENTS WHERE emailid=? and assignmentname=? and filename=?";
				stmt = conn.prepareStatement(sql);
				stmt.setString(1, emailid);
				stmt.setString(2, assignment);
				stmt.setString(3, filename);
				ResultSet rs = stmt.executeQuery();
				//System.out.println("InsertUserDetails state: "+state+" | "+name+","+pwd+","+role+church);
				if(rs!=null){
					JSONObject jObj = new JSONObject();
					while(rs.next()){						
						jObj.put("emailid", rs.getString("emailid"));
						jObj.put("assignment", rs.getString("assignmentname"));
						jObj.put("filename", rs.getString("filename"));
						jObj.put("status", rs.getString("status"));
						jObj.put("feedback", rs.getString("feedback"));
						jObj.put("score", rs.getString("score"));
						jObj.put("timestamp", rs.getString("submissiontime"));						
					}
					return jObj;
				}
				
				
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} finally {
				try {
					stmt.close();
					closeConnection(conn);
				} catch (SQLException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
	        
		 return null;
	 }
	 
	 public ArrayList<ArrayList<String>> getSubmissionTimeDetails(String assignmentname){
			Connection conn = openConnection();
		    PreparedStatement stmt = null;
		    // Execute SQL query
		    //System.out.println("checkFileExists: "+emailid+" "+assignment+" "+filename);
	        try {
	        	String sql = "SELECT * FROM SUBPORTAL.ASSIGNMENTS WHERE assignmentname=? ORDER BY emailid ASC, submissiontime DESC;";
				stmt = conn.prepareStatement(sql);
				stmt.setString(1, assignmentname);
				ResultSet rs = stmt.executeQuery();
				//System.out.println("InsertUserDetails state: "+state+" | "+name+","+pwd+","+role+church);
				if(rs!=null){
					//System.out.println("inside...");
					ArrayList<ArrayList<String>> maintemp = new ArrayList<ArrayList<String>>();
					while(rs.next()){
						//System.out.println("CHECKFILE: true");
						ArrayList<String> temp = new ArrayList<String>();
						temp.add(rs.getString(1));
						temp.add(rs.getString(2));
						temp.add(rs.getString(3));
						temp.add(rs.getString(4));
						temp.add(rs.getString(7));
						maintemp.add(temp);
					}
					return maintemp;
				}
				
				
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} finally {
				try {
					stmt.close();
					closeConnection(conn);
				} catch (SQLException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
	        return null;
		 }
	 
	 public void insertMissionDetails(String title, String description, String assignmentname) {
	    	Connection conn = openConnection();
	    	PreparedStatement stmt = null;
	    	// Execute SQL query
	        try {
	        	String sql = "INSERT INTO MISSION (title, description, assignmentname, createdon) VALUES (?,?,?,?)";
				stmt = conn.prepareStatement(sql);
				stmt.setString(1, title);
				stmt.setString(2, description);
				stmt.setString(3, assignmentname);
				stmt.setString(4, getDBTimestamp());				
				//stmt.setTimestamp(5, "");
				int state = stmt.executeUpdate();
				//System.out.println("InsertUserDetails state: "+state+" | "+name+","+pwd+","+role+church);
				
				
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} finally {
				try {
					stmt.close();
					closeConnection(conn);
				} catch (SQLException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}	
	}
	 
	 public void insertSubtaskDetails(String description, int missionid, String assignmentname) {
	    	Connection conn = openConnection();
	    	PreparedStatement stmt = null;
	    	// Execute SQL query
	        try {
	        	String sql = "INSERT INTO SUBTASK (description, missionid, assignmentname, createdon) VALUES (?,?,?,?)";
				stmt = conn.prepareStatement(sql);
				stmt.setString(1, description);
				stmt.setInt(2, missionid);
				stmt.setString(3, assignmentname);
				stmt.setString(4, getDBTimestamp());				
				//stmt.setTimestamp(5, "");
				int state = stmt.executeUpdate();
				//System.out.println("InsertUserDetails state: "+state+" | "+name+","+pwd+","+role+church);
				
				
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} finally {
				try {
					stmt.close();
					closeConnection(conn);
				} catch (SQLException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}	
	}
	 
	 public JSONObject getSubmissionsForAnAssignment(String assignment){
		 	Connection conn = openConnection();
	    	PreparedStatement stmt = null;
	    	// Execute SQL query
	        try {
	        	String sql = "SELECT * FROM ASSIGNMENTS WHERE assignmentname=?";
				stmt = conn.prepareStatement(sql);
				stmt.setString(1, assignment);			
				ResultSet rs = stmt.executeQuery();
				//System.out.println("InsertUserDetails state: "+state+" | "+name+","+pwd+","+role+church);
				if(rs!=null){
					JSONObject jObj = new JSONObject();
					JSONArray jarr = new JSONArray();
					while(rs.next()){
						JSONArray temp = new JSONArray();
						temp.add(rs.getString(1));
						temp.add(rs.getString(3));
						temp.add(rs.getString(4));
						temp.add(rs.getString(5));
						temp.add(rs.getString(6));
						temp.add(rs.getString(7));
						temp.add(rs.getString(8));						
						jarr.add(temp);
					}					
					jObj.put("data", jarr);
					return jObj;
				}				
				
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} finally {
				try {
					stmt.close();
					closeConnection(conn);
				} catch (SQLException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
	        return null;
	 }


}
