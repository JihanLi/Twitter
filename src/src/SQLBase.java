package src;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import javax.naming.Context;
import javax.naming.NamingException;
import javax.naming.directory.InitialDirContext;
import javax.sql.DataSource;

public final class SQLBase {
	
	private static Connection con = null;	
	private static Statement statement = null;
	
	private static String dbName = "TwitterMap";
	private static String userName = "JihanLi";
	private static String password = "19921128";
	private static String hostname = "kyran-east-instance.cdtysvbxbybf.us-east-1.rds.amazonaws.com";
	private static String port = "3306";
	
	public static void connectDB() throws SQLException, NamingException
	{
		// Read RDS Connection Information from the Environment
		  if(con == null || con.isClosed())
		  {
			  String jdbcUrl = "jdbc:mysql://" + hostname + ":" +
			    port + "/" + dbName + "?user=" + userName + "&password=" + password;
			  
			  // Load the JDBC Driver
			  try {
			    System.out.println("Loading driver...");
			    Class.forName("com.mysql.jdbc.Driver");
			    System.out.println("Driver loaded!");
			  } catch (ClassNotFoundException e) {
			    throw new RuntimeException("Cannot find the driver in the classpath!", e);
			  }
	
			  try {
			    // Create connection to RDS instance
			    con = DriverManager.getConnection(jdbcUrl);	    
			    
			  } catch (SQLException ex) {
			    // handle any errors
			    System.out.println("SQLException: " + ex.getMessage());
			    System.out.println("SQLState: " + ex.getSQLState());
			    System.out.println("VendorError: " + ex.getErrorCode());
			    con.close();
			  } 	  
			  
			  con = DriverManager.getConnection(jdbcUrl);
		  }
	}
	
	public static void disconnectDB() throws SQLException
	{
		con.close();
	}
	
	public static void beginCommands() throws SQLException
	{
		statement = con.createStatement();
	}
	
	public static void endCommands() throws SQLException
	{
		statement.executeBatch();
	    statement.close();
	}
	
	public static void createTable(String table, String[] items) throws SQLException
	{
		String itemSet = "";
		for(int i = 0; i < items.length; i++)
			itemSet = itemSet + items[i] + " char(150), ";
		itemSet = itemSet.substring(0, itemSet.length()-2);
		String command = "CREATE TABLE " + table + " (" + itemSet + ");";
		statement.addBatch(command);
	}
	
	public static void dropTable(String table) throws SQLException
	{
		String command = "DROP TABLE " + table + ";";
		statement.addBatch(command);
	}
	
	public static int getCount(String table) throws SQLException
	{
		int recordCount = 0;
		statement = con.createStatement();
		String command = "SELECT count(*) FROM " + table + ";";
		ResultSet result = statement.executeQuery(command);
		if (result.next()) 
	    {
			recordCount = result.getInt(1); 
	    }
		statement.close();
		return recordCount;
	}
	
	public static List<List<String>> selectItem(String[] items, String table) throws SQLException
	{
		statement = con.createStatement();
		String itemSet = "";
		for(String i : items)
			itemSet = itemSet + i + ", ";
		itemSet = itemSet.substring(0, itemSet.length()-2);
		String command = "SELECT " + itemSet + " FROM " + table + ";";
		ResultSet result = statement.executeQuery(command);
		
		List<String> temp = new ArrayList<String>();
		List<List<String>> output = new ArrayList<List<String>>();
		
		while(result.next())
		{
			temp = new ArrayList<String>();
			for(String i : items)
			{
				//System.out.println(i+ ": " + result.getString(i));
				temp.add(result.getString(i));
			}
			output.add(temp);
		}
		statement.close();
		return output;
	}
	
	public static List<List<String>> selectItem(String[] items, String table, int topic) throws SQLException
	{
		statement = con.createStatement();
		String itemSet = "";
		for(String i : items)
			itemSet = itemSet + i + ", ";
		itemSet = itemSet.substring(0, itemSet.length()-2);
		String command = "SELECT " + itemSet + " FROM " + table + " WHERE Topic=" + Integer.toString(topic) + ";";
		ResultSet result = statement.executeQuery(command);
		
		List<String> temp = new ArrayList<String>();
		List<List<String>> output = new ArrayList<List<String>>();
		
		while(result.next())
		{
			temp = new ArrayList<String>();
			for(String i : items)
			{
				//System.out.println(i+ ": " + result.getString(i));
				temp.add(result.getString(i));
			}
			output.add(temp);
		}
		statement.close();
		return output;
	}
	
	public static void insertItem(String table, String[] items, String[] values) throws SQLException
	{
		String itemSet = "";
		for(String i : items)
			itemSet = itemSet + i + ", ";
		itemSet = itemSet.substring(0, itemSet.length()-2);
		
		String valueSet = "";
		for(String i : values)
			valueSet = valueSet + "'" + i + "', ";
		valueSet = valueSet.substring(0, valueSet.length()-2);
		String command = "INSERT INTO "+table+ " ("+ itemSet + ") VALUES (" + valueSet + ");";
		statement.addBatch(command);
	}
	
	public static void deleteItem(String table, String where, String item) throws SQLException
	{
		String command = "DELETE FROM " + table +" WHERE " + where + "=" + item + ";";
		statement.addBatch(command);
	}
	
}
