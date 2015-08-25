package com.portal.assignment;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Timestamp;

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
    	Statement stmt = null;
    	try {			
			stmt = con.createStatement();
			String sql = "CREATE TABLE IF NOT EXISTS ASSIGNMENTS ("
					+"emailid VARCHAR(255) not null, filename VARCHAR(255) not null,"
					+"status VARCHAR(255), feedback TEXT, timestamp TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP, PRIMARY KEY(filename))";
			stmt.executeUpdate(sql);
    	} catch(Exception e){
    		e.printStackTrace();
    	} finally {
    		try{
    			stmt.close();
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


}
