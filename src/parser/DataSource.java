package parser;

import java.util.Date;
import java.util.Map;

public interface DataSource
{
	
	public void read(String labName, String directory);
	
	public Map<Date, Integer> getData();
	
}
