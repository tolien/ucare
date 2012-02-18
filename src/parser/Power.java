package parser;

import java.util.*;
import java.util.concurrent.Callable;


public interface Power extends Callable<Power>, FileParser
{
	public Map<Date, Double> getTotalPower();
	public Map<Date, List<Double>> getPower();
	
	public Map<Date, List<Double>> getTemperature();
	public Map<Date, Double> getCO2();
}
