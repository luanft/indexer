package mysqldc;

import indexer.Config;
import indexer.DataColumn;
import indexer.DataTable;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class DC implements ConnectionBase{
	

	@Override
	public DataTable getTableInformation() {
		DataTable tableDescription = new DataTable();
		if(this.connect())
		{
			ResultSet rs = this
					.read("DESCRIBE " + config.getTableName());
			try {
				while (rs.next()) {
					DataColumn col = new DataColumn();
					col.setPrimaryKey(false);
					col.setColumnName(rs.getString("Field"));
					String key = rs.getString("Key");
					if (key != null) {
						if (key.equals("PRI")) {
							col.setPrimaryKey(true);
						}
					}
					tableDescription.addColumn(col);
				}
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			this.close();
		}
		return tableDescription;
	}


	private Connection connection = null;
	private PreparedStatement preStatement = null;

	public DC(Config conf) {

		this.config = conf;
		mysqlHost = "jdbc:mysql://localhost:3306/"+conf.getDatabase()+"?useUnicode=true&characterEncoding=UTF-8";
	}

	public Connection getConnection() {
		return connection;
	}

	public Boolean connect() {
		try { 
			Class.forName("com.mysql.jdbc.Driver");
			connection = DriverManager.getConnection(mysqlHost, this.config.getUserName(),
					this.config.getUserPass());
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

	
	public Config config = null;	
	public String mysqlHost;
	public void close() {
		try {
			this.connection.close();
		} catch (SQLException e) {
		}
	}

}
