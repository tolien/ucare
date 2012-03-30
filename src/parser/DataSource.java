package parser;

import java.util.*;

public interface DataSource
{	
	public void read(String directory, ParserFactory parser);
	
	public Map<Date, Double> getAbsoluteOccupancy(String labName, Date start, Date end);
	public Map<Date, Double> getRelativeOccupancy(String labName, Date start, Date end);
	
	public List<String> getLabList();
	public int getLabCapacity(String lab);

	public Map<Date, Double> getTotalPower(String labName, Date start, Date end);
	public Map<Date, List<Double>> getPower(String labName, Date start, Date end);
	public Map<Date, List<Double>> getTemperature(String labName, Date start, Date end);
	public Map<Date, Double> getAverageTemperature(String labName, Date start, Date end);
	public Map<Date, Double> getCO2(String labName, Date start, Date end);
		
}
