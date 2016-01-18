package indexer;
import mysqldc.*;
public class Config {

	private String database = "";
	private String userName = "";
	private String userPass = "";
	private String tableName = "";
	private DatabaseType server;
	
	
	
	public DatabaseType getServer() {
		return server;
	}
	public void setServer(DatabaseType server) {
		this.server = server;
	}
	public String getDatabase() {
		return database;
	}
	public void setDatabase(String database) {
		this.database = database;
	}
	public String getUserName() {
		return userName;
	}
	public void setUserName(String userName) {
		this.userName = userName;
	}
	public String getUserPass() {
		return userPass;
	}
	public void setUserPass(String userPass) {
		this.userPass = userPass;
	}
	public String getTableName() {
		return tableName;
	}
	public void setTableName(String tableName) {
		this.tableName = tableName;
	}
}
