package analyser;

import java.util.*;

public interface DataAnalyser
{	
	public void analyse(Map<Date, Double> points);
	public Map<Integer, Double> getResult();
	
	public String getName();
	public int getInterval();
}
