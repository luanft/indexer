package indexer;

import java.io.File;
import java.io.IOException;
import java.nio.file.DirectoryStream;
import java.nio.file.FileSystems;
import java.nio.file.Files;
import java.nio.file.Path;

public class Engine {
	static final Path path =  FileSystems.getDefault().getPath("IndexFile");
	public boolean Init() {
		return false;
	}
	
	public void Indexing()
	{
		
	}	
	/**
	 * kiem tra thu xem co file config hay chưa?
	 * @return
	 */
	public boolean hasConfigFile()
	{
		return false;
	}
	/**
	 * kiểm tra su ton tai cua thu muc index
	 * @return
	 */
	public boolean hasIndexDirectory()
	{
		if (Files.exists(path))
		{
			return true;
		}
		return false;
	}
	/**
	 * Tao thu muc index
	 */
	public void createIndexDirectory()
	{
		File dir = new File("IndexFile");
		dir.mkdir();
	}
	/**
	 * Kiem tra thu muc index co rong hay ko
	 * @return
	 * @throws IOException 
	 */
	public boolean isEmptyIndexDirectory() throws IOException
	{
		try(DirectoryStream<Path> dirStream = Files.newDirectoryStream(path)) {
	        return !dirStream.iterator().hasNext();
	    }
	}
	
	/**
	 * doc file config
	 * @return
	 */
	public Config readConfigFile()
	{
		return new Config();
	}
}
