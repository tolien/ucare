package analyser;

import java.util.*;

public interface OccupancyAnalyser
{	
	public void analyse(Map<Date, Double> points);
	public Map<Integer, Double> getResult();
}
