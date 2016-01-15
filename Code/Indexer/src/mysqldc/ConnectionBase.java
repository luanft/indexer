package mysqldc;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
public interface ConnectionBase {
				
	public Connection getConnection() ;
	
	public Boolean connect();

	public ResultSet read(String sql);
	
	public void close() throws SQLException ;
}
