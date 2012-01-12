package parser;

import java.util.*;

public interface DataSource
{	
	public void read(String directory, ParserFactory parser);
	
	public Map<Date, Integer> getAbsoluteOccupancy(String labName);	
	public Map<Date, Float> getRelativeOccupancy(String labName);
	
	public List<String> getLabList();	
}
