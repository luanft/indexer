
package mysqldc;

import indexer.Config;
import indexer.DataColumn;
import indexer.DataTable;

import java.sql.*;
import java.util.HashMap;
import java.util.Map;

/**
 *
 * @author Hello World
 */

public class SQLServerConnect implements ConnectionBase {
	@Override
	public DataTable getTableInformation() {

		if (this.connect()) {

			ResultSet rs = this
					.read("SELECT Col.Column_Name from INFORMATION_SCHEMA.TABLE_CONSTRAINTS Tab, INFORMATION_SCHEMA.CONSTRAINT_COLUMN_USAGE Col  WHERE  Col.Constraint_Name = Tab.Constraint_Name AND Col.Table_Name = Tab.Table_Name AND Constraint_Type = 'PRIMARY KEY' AND Col.Table_Name = '"
							+ config.getTableName() + "'");
			try {
				Map map = new HashMap<String,String>();
				while (rs.next()) {					 
					 map.put(rs.getString(1),"");
				}
				rs = this.read("SELECT COLUMN_NAME FROM INFORMATION_SCHEMA.COLUMNS WHERE table_name = '"+config.getTableName()+"'");
				while (rs.next()) {					 
					 String colum = rs.getString(1);
						DataColumn col = new DataColumn();
						col.setPrimaryKey(false);
						col.setColumnName(colum);
					 if(map.containsKey(colum))
					 {
						 col.setPrimaryKey(true);						
					 }
				}				
				this.close();
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		DataTable info = new DataTable();

		return info;
	}

	Connection connect = null;
	Statement statement = null;
	ResultSet resultset = null;
	Config config;
	public String mySqlHost;

	public SQLServerConnect(Config _config) {
		this.config = _config;
		mySqlHost = "jdbc:sqlserver://TRANMINHLUAN\\SQLEXPRESS:1433;databaseName=" + config.getDatabase() + ";";
	}

	// Hàm kiểm tra xem Driver connect MySQL đã sẵn sàng chưa
	public void driverTest() throws Exception {
		try {
			Class.forName("com.microsoft.sqlserver.jdbc.SQLServerDriver");

		} catch (ClassNotFoundException e) {
			throw new Exception("My SQL Driver not found");
		}
	}

	public Boolean connect() {
		if (this.connect == null) {
			try {
				driverTest();
			} catch (Exception e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
			try {
				// Tạo connect thông qua url
				this.connect = DriverManager.getConnection(mySqlHost, config.getUserName(), config.getUserPass());
			} catch (SQLException e) {
				// throw new Exception("Không thể kết nối database");
				return false;
			}
		}
		return true;
	}

	protected Statement getStatement() throws Exception {
		// kiểm tra Statement nếu null hoặc đă đóng.
		if (this.statement == null ? true : this.statement.isClosed()) {
			// khoi tao mot d=sattement moi
			this.statement = this.getConnection().createStatement();
		}
		return this.statement;
	}

	public ResultSet excuteQuery(String query) throws Exception {
		try {
			// thực thi câu lệnh
			this.resultset = getStatement().executeQuery(query);
		} catch (Exception e) {
			throw new Exception("Erro" + e.getMessage());
		}
		return this.resultset;
	}

	public int excuteUpdate(String query) throws Exception {
		int res = Integer.MIN_VALUE;
		try {
			res = getStatement().executeUpdate(query);
		} catch (Exception e) {
			throw new Exception("Erro" + e.getMessage() + query);
		} finally {
			// this.close();
		}
		return res;
	}

	@Override
	public Connection getConnection() {
		return connect;
	}

	@Override
	public ResultSet read(String sql) {
		try {
			this.resultset = getStatement().executeQuery(sql);
		} catch (Exception e) {
		}
		return this.resultset;
	}

	@Override
	public void close() throws SQLException {
		if (this.resultset != null || !this.resultset.isClosed()) {
			this.resultset.close();
			this.resultset = null;
		}
		if (this.statement != null || !this.statement.isClosed()) {
			this.statement.close();
			this.statement = null;
		}
		if (this.connect != null || !this.connect.isClosed()) {
			this.connect.close();
			this.connect = null;
		}
	}
}
