package parser;

import java.util.*;

public interface DataSource
{
	public static int DS = 1;
	
	public void read(String labName, String directory, ParserFactory parser);
	
	public Map<Date, Integer> getAbsoluteOccupancy();	
	public Map<Date, Float> getRelativeOccupancy();
	
	public List<String> getLabList();	
}
