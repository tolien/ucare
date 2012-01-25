package parser;

import java.util.*;

public interface Occupancy
{
	public List<String> getLabList();
	
	public Map<Date, Double> getAbsoluteOccupancy(String lab);
	public Map<Date, Double>	getRelativeOccupancy(String lab);
}
