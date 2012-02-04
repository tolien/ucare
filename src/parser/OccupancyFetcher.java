package parser;

import java.util.concurrent.Callable;
import java.util.*;

public class OccupancyFetcher implements Callable<Map<Date, Double>>
{
	private Occupancy occupancy;
	String lab;
	Date start = null;
	Date end = null;
	
	public OccupancyFetcher(Occupancy o, String lab)
	{
		occupancy = o;
		this.lab = lab;
	}
	
	public OccupancyFetcher(Occupancy o, String lab, Date start, Date end)
	{
		occupancy = o;
		this.lab = lab;
		this.start = start;
		this.end = end;
	}
	
	@Override
	public Map<Date, Double> call()
	{
		if (start == null || end == null)
			return occupancy.getAbsoluteOccupancy(lab);
		else
			return occupancy.getAbsoluteOccupancy(lab, start, end);
	}

}
