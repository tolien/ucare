package parser;

import java.util.*;
import java.util.concurrent.Callable;


public interface Power extends Callable<Power>, FileParser
{
	public Map<Date, Double> getTotalPower(Date start, Date end);
	public Map<Date, List<Double>> getPower(Date start, Date end);
	
	public Map<Date, List<Double>> getTemperature(Date start, Date end);
	public Map<Date, Double> getCO2(Date start, Date end);
}
