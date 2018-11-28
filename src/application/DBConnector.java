package application;

import java.sql.SQLException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;

public class DBConnector 
{
	public static Connection getConnect()
	{
		Connection connection = null;
		
		try 
		{
			Class.forName("com.microsoft.sqlserver.jdbc.SQLServerDriver");
			
			connection = DriverManager.getConnection("jdbc:sqlserver://localhost:1433;databaseName=StecajniPostapki", "sa", "sql&627S");
			
			return connection;
		} 
		
		catch (Exception e) 
		{
			e.printStackTrace();
		}
		return connection;
		
	}

}
