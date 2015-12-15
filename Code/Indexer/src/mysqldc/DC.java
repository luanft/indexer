package mysqldc;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class DC {

	public final String database = "recsys";
	public final String mysqlHost = "jdbc:mysql://localhost:3306/"+database+"?useUnicode=true&characterEncoding=UTF-8";
	public final String userName = "root";
	public final String password = "";
	
	private Connection connection = null;
	private PreparedStatement preStatement = null;

	public DC() {

	}

	public Connection getConnection() {
		return connection;
	}

	public PreparedStatement getPrepareStatement() {
		return preStatement;
	}

	public void setPrepareStatement(PreparedStatement pre) {
		this.preStatement = pre;
	}

	public Boolean connect() {
		try { 
			Class.forName("com.mysql.jdbc.Driver");
			connection = DriverManager.getConnection(mysqlHost, userName,
					password);
		} catch (ClassNotFoundException e) {
			return false;
		} catch (SQLException e) {
			return false;
		}
		return true;
	}

	public ResultSet read(String sql) {
		ResultSet data = null;
		try {
			java.sql.Statement cmd = connection.createStatement();
			data = cmd.executeQuery(sql);

		} catch (SQLException e) {		
		}
		return data;
	}

	public ResultSet readSecure() {
		ResultSet data = null;
		try {
			data = preStatement.executeQuery();
		} catch (SQLException e) {
		}
		return data;
	}

	public void close() {
		try {
			this.connection.close();
		} catch (SQLException e) {
		}
	}

}
