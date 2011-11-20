package parser;

import java.util.*;

public interface Occupancy
{
	public List<String> getLabList();
	
	public Map<Date, Integer> getAbsoluteOccupancy(String lab);
	public Map<Date, Float>	getRelativeOccupancy(String lab);
}
