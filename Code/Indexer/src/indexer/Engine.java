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
import java.util.ArrayList;
import java.util.List;

import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.document.Document;
import org.apache.lucene.document.Field;
import org.apache.lucene.document.FieldType;
import org.apache.lucene.document.StringField;
import org.apache.lucene.document.TextField;
import org.apache.lucene.index.DirectoryReader;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.index.IndexWriterConfig;
import org.apache.lucene.index.IndexWriterConfig.OpenMode;
import org.apache.lucene.index.Term;
import org.apache.lucene.search.IndexSearcher;
import org.apache.lucene.search.TermQuery;
import org.apache.lucene.search.TopDocs;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.FSDirectory;

import mysqldc.ConnectionBase;
import mysqldc.DC;
import mysqldc.DatabaseType;
import mysqldc.SQLServerConnect;



public class Engine {

	Analyzer analyzer = new StandardAnalyzer();
	Path path;
	Directory directory;
	IndexWriterConfig iconfig;
	IndexWriter iwriter;
	DirectoryReader ireader;
	IndexSearcher isearcher;
	private ConnectionBase dc;
	private Config config;
	private DataTable tableDescription = new DataTable();

	public boolean Init() {

		path = FileSystems.getDefault().getPath("IndexFile");
		if (!this.hasIndexDirectory()) {
			this.createIndexDirectory();
		}

		if (!this.hasConfigFile())
			return false;

		try {
			this.config = this.readConfigFile();
			if (config == null)
				return false;
			switch (config.getServer()) {
			case MySql:
					dc = new DC(this.config);
				break;
			case SQLServer:
				dc = new SQLServerConnect(this.config);
				break;

			default:
				break;
			}
			this.dc = new DC(this.config);
			this.tableDescription = this.dc.getTableInformation(); 
			if (tableDescription.isEmpty())
				return false;
		} catch (IOException e) {
			// TODO Auto-generated catch block
			return false;
		}
		try {
			directory = FSDirectory.open(path);
			iconfig = new IndexWriterConfig(analyzer);
			iconfig.setOpenMode(OpenMode.CREATE);
			iwriter = new IndexWriter(directory, iconfig);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			return false;
		}

		return true;
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
	public boolean isEmptyIndexDirectory() {
		File f1 = new File("IndexFile");

		return f1.list().length == 0;

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
			String server = buffer.readLine();
			String database = buffer.readLine();
			String username = buffer.readLine();
			String userpass = buffer.readLine();
			String table = buffer.readLine();

			Config config = new Config();
			if(server.equals("mysql"))
			{
				config.setServer(DatabaseType.MySql);	
			}
			if(server.equals("sqlserver"))
			{
				config.setServer(DatabaseType.SQLServer);	
			}						
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
	 * 
	 * @return
	 */
//	public boolean loadTableDescription() {
//		try {
//
//			if (this.dc.connect()) {
//				ResultSet rs = this.dc
//						.read("DESCRIBE " + config.getTableName());
//				while (rs.next()) {
//					DataColumn col = new DataColumn();
//					col.setPrimaryKey(false);
//					col.setColumnName(rs.getString("Field"));
//					String key = rs.getString("Key");
//					if (key != null) {
//						if (key.equals("PRI")) {
//							col.setPrimaryKey(true);
//						}
//					}
//					tableDescription.addColumn(col);
//				}
//				this.dc.close();
//				return true;
//
//			} else {
//				return false;
//			}
//
//		} catch (SQLException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
//		return false;
//	}

	public DataTable getTableDescription() {
		return this.tableDescription;
	}

	/**
	 * dem so record
	 * 
	 * @return
	 * @throws IOException
	 * @throws SQLException
	 */
	public int Count() {
		if (dc.connect()) {
			String sql = "SELECT COUNT(*) as COUNT FROM  `"
					+ config.getTableName() + "`";
			ResultSet rs = dc.read(sql);
			try {
				if (rs.next()) {
					int count = rs.getInt("COUNT");
					dc.close();
					return count;
				}
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		return 0;
	}

	public void createIndexFile() throws SQLException, IOException {

		if (this.dc.connect()) {
			ResultSet rs = dc.read("select * from " + config.getTableName()
					+ " where 1");
			List<Document> docs = new ArrayList<Document>();
			while (rs.next()) {
				Document d = new Document();
				for (DataColumn c : tableDescription.getAllColumn()) {
					String dt = rs.getString(c.getColumnName());
					if (c.isPrimaryKey()) {

						d.add(new StringField(c.getColumnName(), dt,
								Field.Store.YES));
					} else {
						d.add(new TextField(c.getColumnName(), dt,
								Field.Store.YES));
					}
				}
				docs.add(d);

			}
			iwriter.addDocuments(docs);
			iwriter.close();
			this.dc.close();
			System.out.print("\n Done!");
		}
	}

}
