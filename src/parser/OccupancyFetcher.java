package parser;

import java.util.concurrent.Callable;
import java.util.*;

public class OccupancyFetcher implements Callable<Map<Date, Double>>
{
	private Occupancy occupancy;
	private String lab;
	private Date start = null;
	private Date end = null;
	private boolean isRelative = false;
	
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
	
	public void setRelative(boolean relative)
	{
		isRelative = relative;
	}
	
	@Override
	public Map<Date, Double> call()
	{
		if (isRelative == false)
		{
			return occupancy.getAbsoluteOccupancy(lab, start, end);
		}
		else
		{
			return occupancy.getRelativeOccupancy(lab, start, end);
		}
	}

}
