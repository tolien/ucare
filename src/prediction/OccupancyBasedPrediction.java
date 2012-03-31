package prediction;

import java.util.*;

import occupancy.Utility;


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
		List<Double> factors = new ArrayList<Double>();
		Iterator<Integer> it = analysedData.keySet().iterator();
		
		Map<Integer, Double> analyses = new HashMap<Integer, Double>();
		while (it.hasNext())
		{
			int type = it.next();
			Map<Integer, Double> data = analysedData.get(type);
			int period = getDatePart(d, type);
			
			if (data.get(period) != null)
			{
				double factor = data.get(period);
				analyses.put(type, factor);
				factors.add(factor);
			}
		}
		
		System.out.println(d + "\t" + analyses);
		double probability = weightFactors(factors);
		probability = probability > 1 ? 1 : probability;
		return probability;
	}
	
	private double weightFactors(List<Double> factors)
	{
		if ((Utility.max(factors) / Utility.min(factors)) >= 5)
		{
			return Utility.min(factors);
		}
		else if (averageFactor(factors) < 1.5)
		{
			return Utility.sum(factors);
		}
		else
		{
			return Utility.average(closestTwo(factors));
		}
	}
	
	private double averageFactor(List<Double> list)
	{
		if (list.size() < 2)
			return 0.0;
		
		List<Double> sorted = Utility.asSortedList(list);
		List<Double> factors = new ArrayList<Double>();
		for (int i = sorted.size() - 1; i > 0; i--)
		{
			factors.add(sorted.get(i) / sorted.get(i - 1));
		}
		return Utility.average(factors);
	}
	
	private List<Double> closestTwo(List<Double> factors)
	{
		if (factors.size() <= 2)
				return factors;
		
		List<Double> sortedFactors = Utility.asSortedList(factors);
		List<Double>closest = new ArrayList<Double>(2);
		
		closest.add(sortedFactors.get(0));
		closest.add(sortedFactors.get(1));
		
		for (int i = 1; i < sortedFactors.size() - 1; i++)
		{
			double a = sortedFactors.get(i);
			double b = sortedFactors.get(i + 1);
			
			if ((b - a) < (closest.get(0) - closest.get(1)))
			{
				closest.clear();
				closest.add(0, a);
				closest.add(1, b);
			}
		}
		
		return closest;
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
