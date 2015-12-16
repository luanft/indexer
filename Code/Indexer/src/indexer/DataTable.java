package indexer;
import java.util.ArrayList;
import java.util.List;

public class DataTable {
	private List<DataColumn> columns = new ArrayList<DataColumn>();
	
	public void addColumn(DataColumn col)
	{
		this.columns.add(col);
	}
	
	public List<DataColumn> getAllColumn()
	{
		return this.columns;
	}
}
