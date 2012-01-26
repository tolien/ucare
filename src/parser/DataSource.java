package parser;

import java.util.*;

public interface DataSource
{	
	public void read(String directory, ParserFactory parser);
	
	public Map<Date, Double> getAbsoluteOccupancy(String labName);	
	public Map<Date, Double> getRelativeOccupancy(String labName);
	
	public List<String> getLabList();

	public Map<Date, Double> getPower(String string);	
}
