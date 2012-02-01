package parser;

import java.util.*;
import java.util.concurrent.Callable;

public interface Occupancy extends Callable<Occupancy>, FileParser
{
	public List<String> getLabList();
	
	public Map<Date, Double> getAbsoluteOccupancy(String lab);
	public Map<Date, Double> getRelativeOccupancy(String lab);
	
	public Map<Date, Double> getAbsoluteOccupancy(String lab, Date start, Date end);
}
