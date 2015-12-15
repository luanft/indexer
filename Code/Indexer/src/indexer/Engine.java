package indexer;

public class Engine {
	
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
		return false;
	}
	/**
	 * Tao thu muc index
	 */
	public void createIndexDirectory()
	{
		
	}
	/**
	 * Kiem tra thu muc index co rong hay ko
	 * @return
	 */
	public boolean isEmptyIndexDirectory()
	{
		return false;
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
