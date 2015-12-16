package indexer;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.nio.file.DirectoryStream;
import java.nio.file.FileSystems;
import java.nio.file.Files;
import java.nio.file.Path;

public class Engine {
	static final Path path = FileSystems.getDefault().getPath("IndexFile");

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
}
