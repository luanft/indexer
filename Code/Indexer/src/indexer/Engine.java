package indexer;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.nio.file.DirectoryStream;
import java.nio.file.FileSystems;
import java.nio.file.Files;
import java.nio.file.Path;
import java.sql.ResultSet;
import java.sql.SQLException;

import mysqldc.DC;

public class Engine {
	static final Path path = FileSystems.getDefault().getPath("IndexFile");

	private DC dc;
	private Config config;
	private DataTable tableDescription = new DataTable();
	public boolean Init() {
		return false;
	}

	public void Indexing() {

	}

	/**
	 * kiem tra thu xem co file config hay chưa?
	 * 
	 * @return
	 */
	public boolean hasConfigFile() {
		File file = new File("config/config.txt");
		return file.exists();
	}

	/**
	 * kiểm tra su ton tai cua thu muc index
	 * 
	 * @return
	 */
	public boolean hasIndexDirectory() {
		if (Files.exists(path)) {
			return true;
		}
		return false;
	}

	/**
	 * Tao thu muc index
	 */
	public void createIndexDirectory() {
		File dir = new File("IndexFile");
		dir.mkdir();
	}

	/**
	 * Kiem tra thu muc index co rong hay ko
	 * 
	 * @return
	 * @throws IOException
	 */
	public boolean isEmptyIndexDirectory() throws IOException {
		try (DirectoryStream<Path> dirStream = Files.newDirectoryStream(path)) {
			return !dirStream.iterator().hasNext();
		}
	}

	/**
	 * doc file config
	 * 
	 * @return
	 * @throws IOException
	 */
	public Config readConfigFile() throws IOException {
		if (this.hasConfigFile()) {
			FileReader reader = new FileReader("config/config.txt");
			@SuppressWarnings("resource")
			BufferedReader buffer = new BufferedReader(reader);
			String database = buffer.readLine();
			String username = buffer.readLine();
			String userpass = buffer.readLine();
			String table = buffer.readLine();

			Config config = new Config();
			config.setDatabase(database);
			config.setUserName(username);
			config.setUserPass(userpass);
			config.setTableName(table);
			return config;
		}
		return null;
	}
	/**
	 * lay thong ve bang du lieu
	 * @return
	 */
	public boolean loadTableDescription() {
		try {
			this.config = this.readConfigFile();
			this.dc = new DC(this.config);
			if (this.dc.connect()) {
				ResultSet rs = this.dc.read("DESCRIBE " + config.getTableName());
				while (rs.next()) {
					DataColumn col = new DataColumn();
					col.setPrimaryKey(false);
					col.setColumnName(rs.getString("Field"));
					String key = rs.getString("Key");
					if(key != null)
					{
						if(key.equals("PRI"))
						{
							col.setPrimaryKey(true);
						}
					}									
					tableDescription.addColumn(col);
				}
				this.dc.close();
				return true;
				
			} else {
				return false;
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return false;
	}

	public DataTable getTableDescription()
	{
		return this.tableDescription;
	}
/**
 * dem so record
 * @return
 * @throws IOException
 * @throws SQLException 
 */
	public int Count() throws IOException, SQLException
	{
		Config config = new Config();
		config = readConfigFile();
		DC dc = new DC(config);
		if (dc.connect())
		{
			String sql = "SELECT COUNT(*) as COUNT FROM  `"+config.getTableName()+"`";
			ResultSet rs = dc.read(sql);
			if (rs.next())
			{
				int count = rs.getInt("COUNT");
				dc.close();
				return count;
			}
		}		
		return 0;
	}
}
