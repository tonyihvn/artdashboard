/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.openmrs.module.artdashboard.api.dao;

/**
 *
 * @author --
 */

import java.sql.DriverManager;
import java.sql.*;
import java.util.Properties;
import java.util.logging.Level;
import org.openmrs.api.context.Context;
import org.openmrs.util.OpenmrsUtil;

/**
 * @author --
 */
public class Database {
	
	//static DBConnection openmrsConn = Utils.getNmrsConnectionDetails();
	
	public static Connection conn = null;
	
	//public static Connection [] connPool = new Connection[10];
	//public static DBConnection openmrsConn2 = null;
	
	public static ConnectionPool connectionPool;
	
	public static void initConnection() {
		try {
			System.out.println("initiating connection");
			Properties props = null;
			/*
			props = OpenmrsUtil.getRuntimeProperties("openmrs");
			if (props == null) {
				props = OpenmrsUtil.getRuntimeProperties("openmrs-standalone");
				
			}
			*/
			
			// Connection method for all other installations and clusters
			if (props == null) {
				props = Context.getRuntimeProperties();
				
			}
			
			String username = props.getProperty("connection.username");
			String password = props.getProperty("connection.password");
			String connectionUrl = props.getProperty("connection.url");
			
			connectionPool = new ConnectionPool("com.mysql.jdbc.Driver", connectionUrl, username, password, 1, 20, true);
			
		}
		catch (Exception ex) {
			ex.printStackTrace();
		}
		
	}
	
	public static void closeConnection() {
		if (connectionPool != null) {
			connectionPool.closeAllConnections();
		}
	}
	
	public static int setSQLMode(String sqlMode) {
		PreparedStatement stmt = null;
		ResultSet rs = null;
		Connection con = null;
		
		try {
			con = Database.connectionPool.getConnection();
			//stmt = Database.conn.createStatement(java.sql.ResultSet.TYPE_FORWARD_ONLY, java.sql.ResultSet.CONCUR_READ_ONLY);
			//stmt = Database.conn.createStatement(java.sql.ResultSet.TYPE_FORWARD_ONLY, java.sql.ResultSet.CONCUR_READ_ONLY);
			String query = "SET GLOBAL sql_mode=?";
			int i = 1;
			stmt = con.prepareStatement(query);
			
			stmt.setString(i++, sqlMode);
			
			stmt.execute();
			
			return 1;
			
		}
		catch (SQLException ex) {
			Database.handleException(ex);
			return 0;
		}
		finally {
			Database.cleanUp(rs, stmt, con);
		}
	}
	
	public static void cleanUp(ResultSet rs, Statement stmt, Connection con) {
		try {
			Database.connectionPool.free(con);
			stmt.close();
			if (rs != null)
				rs.close();
			
		}
		catch (Exception ex) {
			handleException(ex);
		}
	}
	
	public static void cleanUpTest(ResultSet rs, Statement stmt, Connection con) {
		try {
			//Database.connectionPool.free(con);
			stmt.close();
			if (rs != null)
				rs.close();
			
		}
		catch (Exception ex) {
			handleException(ex);
		}
	}
	
	public static void handleException(Exception ex) {
		ex.printStackTrace();
	}
}
