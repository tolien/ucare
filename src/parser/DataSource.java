package parser;

import java.util.*;

public interface DataSource
{
	
	public void read(String labName, String directory);
	
	public Map<Date, Integer> getData();
	
	public List<String> getLabList();
	
}
