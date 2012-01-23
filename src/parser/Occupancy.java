package parser;

import java.util.*;
import java.util.concurrent.Callable;

public interface Occupancy extends Callable<Occupancy>, FileParser
{
	public List<String> getLabList();
	
	public Map<Date, Integer> getAbsoluteOccupancy(String lab);
	public Map<Date, Float>	getRelativeOccupancy(String lab);
}
