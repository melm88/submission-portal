package com.portal.assignment;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
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
    	Statement stmt1 = null, stmt2 = null, stmt3 = null;
    	try {
    		stmt1 = con.createStatement();
			String sql = "CREATE TABLE IF NOT EXISTS USERS ("
					+"emailid VARCHAR(255) not null, role VARCHAR(255) not null,"
					+" addedon VARCHAR(255), PRIMARY KEY(emailid))";
			stmt1.executeUpdate(sql);
			stmt2 = con.createStatement();
			sql = "CREATE TABLE IF NOT EXISTS SUBMISSIONTEMPLATE ("
					+"assignmentname VARCHAR(255) not null, zipfile VARCHAR(255), folder TEXT, "
					+"filenames TEXT, createdon VARCHAR(255), PRIMARY KEY(assignmentname))";
			stmt2.executeUpdate(sql);
			stmt3 = con.createStatement();
			sql = "CREATE TABLE IF NOT EXISTS ASSIGNMENTS ("
					+"emailid VARCHAR(255) not null, assignmentname VARCHAR(255) not null, filename VARCHAR(255) not null,"
					+"status VARCHAR(255), score VARCHAR(255), feedback TEXT, submissiontime VARCHAR(255), viewedon VARCHAR(255), PRIMARY KEY(emailid, assignmentname, filename), FOREIGN KEY(assignmentname) REFERENCES SUBMISSIONTEMPLATE(assignmentname))";
			stmt3.executeUpdate(sql);
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
	
	 public void insertUserDetails(String name, String filename, String status, String feedback) {
	    	Connection conn = openConnection();
	    	PreparedStatement stmt = null;
	    	
	    	// Execute SQL query
	        try {
	        	String sql = "INSERT INTO ASSIGNMENTS(emailid, filename, status, feedback) VALUES (?,?,?,?)";
				stmt = conn.prepareStatement(sql);
				stmt.setString(1, name);
				stmt.setString(2, filename);
				stmt.setString(3, status);
				stmt.setString(4, feedback);
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
	 
	 public void insertSubmissionTemplate(String assignment,String zipname, String foldername, String filename){
		 	Connection conn = openConnection();
	    	PreparedStatement stmt = null;
	    	
	    	String sql = "INSERT INTO SUBMISSIONTEMPLATE(assignmentname, zipfile, folder, filenames, createdon) VALUES (?,?,?,?,?)";
			try {
				stmt = conn.prepareStatement(sql);
				stmt.setString(1, assignment);
				stmt.setString(2, zipname);
				stmt.setString(3, foldername);
				stmt.setString(4, filename);
				stmt.setString(5, getDBTimestamp());
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
	 
	 public void updateSubmissionTemplate(String assignment, String zipname, String foldername, String filename) {
		 	Connection conn = openConnection();
	    	PreparedStatement stmt = null;
	    	
	    	// Execute SQL query
	        try {
	        	String sql = "UPDATE SUBMISSIONTEMPLATE SET zipfile=?, folder=?, filenames=?, createdon=? WHERE assignmentname=?";
				stmt = conn.prepareStatement(sql);
				stmt.setString(1, zipname);
				stmt.setString(2, foldername);
				stmt.setString(3, filename);
				stmt.setString(4, getDBTimestamp());
				stmt.setString(5, assignment);
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
	 
	 public boolean checkFileExists(String filename){
		 Connection conn = openConnection();
	    PreparedStatement stmt = null;
	    // Execute SQL query
        try {
        	String sql = "SELECT emailid FROM ASSIGNMENTS WHERE filename=?";
			stmt = conn.prepareStatement(sql);
			stmt.setString(1, filename);			
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
	 
	 public void updateUserDetails(String filename, String status, String feedback) {
		 	Connection conn = openConnection();
	    	PreparedStatement stmt = null;
	    	
	    	// Execute SQL query
	        try {
	        	String sql = "UPDATE ASSIGNMENTS SET status=?, feedback=?, timestamp=? WHERE filename=?";
				stmt = conn.prepareStatement(sql);
				stmt.setString(1, status);
				stmt.setString(2, feedback);
				stmt.setTimestamp(3, null);
				stmt.setString(4, filename);

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
	 
	 public JSONObject getUserProgress(String emailid){
		 Connection conn = openConnection();
		    PreparedStatement stmt = null;
		    // Execute SQL query
	        try {
	        	String sql = "SELECT * FROM ASSIGNMENTS WHERE emailid=?";
				stmt = conn.prepareStatement(sql);
				stmt.setString(1, emailid);			
				ResultSet rs = stmt.executeQuery();
				//System.out.println("InsertUserDetails state: "+state+" | "+name+","+pwd+","+role+church);
				if(rs!=null){
					JSONObject jObj = new JSONObject();
					JSONArray jarr = new JSONArray();
					while(rs.next()){
						JSONObject temp = new JSONObject();
						temp.put("emailid", rs.getString("emailid"));
						temp.put("filename", rs.getString("filename"));
						temp.put("status", rs.getString("status"));
						temp.put("feedback", rs.getString("feedback"));
						temp.put("timestamp", rs.getString("timestamp"));
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
}
