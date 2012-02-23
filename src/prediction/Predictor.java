package prediction;

import java.util.*;

import analyser.DataAnalyser;

public interface Predictor
{
	public void addAnalyser(DataAnalyser a);
	public void occupancyData(Map<Date, Double> data);
	public double getProbability(Date d);
}
