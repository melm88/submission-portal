package com.portal.assignment;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Timestamp;

public class DBManager {
	
	// JDBC driver name and database URL
    static final String JDBC_DRIVER="com.mysql.jdbc.Driver";  
    static final String DB_URL="jdbc:mysql://localhost/CCMS";
    
    //  Database credentials
    static final String USER = "root";
    static final String PASS = "mel123";
	
	public DBManager(){
		
		Connection con = openConnection();
    	Statement stmt = null;
    	try {			
			stmt = con.createStatement();
			String sql = "CREATE TABLE IF NOT EXISTS ASSIGNMENTS ("
					+"emailid VARCHAR(255) not null, filename VARCHAR(255) not null,"
					+"status VARCHAR(255), feedback TEXT, timestamp TIMESTAMP DEFAULT CURRENT_TIMESTAMP, PRIMARY KEY(filename))";
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
	
	 public void insertUserDetails(String name, String pwd, String role, String church, Timestamp times) {
	    	Connection conn = openConnection();
	    	PreparedStatement stmt = null;
	    	
	    	// Execute SQL query
	        try {
	        	String sql = "INSERT INTO USERTABLE (name, role, password, cid, timestamp) VALUES (?,?,?,?,?)";
				stmt = conn.prepareStatement(sql);
				stmt.setString(1, name);
				stmt.setString(2, role);
				stmt.setString(3, pwd);
				stmt.setInt(4, Integer.parseInt(church));
				stmt.setTimestamp(5, times);
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


}
