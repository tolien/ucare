package prediction;

import java.util.*;

import occupancy.Utility;

import analyser.*;

import analyser.DataAnalyser;

public class OccupancyBasedPrediction implements Predictor
{
	private Map<Integer, DataAnalyser> analysers = new HashMap<Integer, DataAnalyser>();
	private Map<Integer, Map<Integer, Double>> analysedData = new HashMap<Integer, Map<Integer, Double>>();
	
	@Override
	public void occupancyData(Map<Date, Double> data)
	{
		Iterator<Integer> it = analysers.keySet().iterator();
		while (it.hasNext())
		{
			int period = it.next();
			DataAnalyser a = analysers.get(period);
			a.analyse(data);
			analysedData.put(period, a.getResult());			
		}
	}
	
	@Override
	public double getProbability(Date d)
	{
		double probability = 1;
		Iterator<Integer> it = analysedData.keySet().iterator();
		
		List<Double> estimates = new ArrayList<Double>();
		while (it.hasNext())
		{
			int type = it.next();
			Map<Integer, Double> data = analysedData.get(type);
			int period = getDatePart(d, type);
			
			if (data.get(period) != null)
			{
				double factor = data.get(period);
				estimates.add(factor);
			}
			
		}
		
		return Utility.average(estimates);
	}
	
	public int getDatePart(Date d, int part)
	{
		Calendar c = Calendar.getInstance();
		c.setTime(d);
		int day = c.get(part);
		
		return day;
	}

	@Override
	public void addAnalyser(DataAnalyser a)
	{
		analysers.put(a.getInterval(), a);
	}
	
}
