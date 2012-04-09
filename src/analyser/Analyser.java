package analyser;

import java.util.*;

import occupancy.Utility;

public abstract class Analyser implements DataAnalyser
{
	protected Map<Integer, Double> data = new HashMap<Integer, Double>();
	protected Map<Integer, Double[]> boxplot = new HashMap<Integer, Double[]>();
	protected Date minDate;
	protected Date maxDate;
	protected Date minTime;
	protected Date maxTime;
	
	public Map<Integer, Double> getResult()
	{
		return data;
	}
	
	public void analyse(Map<Date, Double> points)
	{
		HashMap<Integer, List<Double>> totals = new HashMap<Integer, List<Double>>();

		Iterator<Date> it = points.keySet().iterator();
		Calendar c = Calendar.getInstance();

		while (it.hasNext())
		{
			Date d = it.next();
			c.setTime(d);
			int intervalID = c.get(getInterval());

			if (selectData(d))
			{
				List<Double> dataForInterval = totals.remove(intervalID);
				if (dataForInterval == null)
				{
					dataForInterval = new ArrayList<Double>();
				}
				if (points.get(d) != 0.0)
					dataForInterval.add(points.get(d));
				totals.put(intervalID, dataForInterval);
			}
		}

		Iterator<Integer> intervalIterator = totals.keySet().iterator();
		while (intervalIterator.hasNext())
		{
			int interval = intervalIterator.next();
			List<Double> intervalData = totals.get(interval);
			data.put(interval, summarisationStep(intervalData));
			
			boxplot.put(interval, fillBoxplot(intervalData));
		}
		
		this.getResult();
	}
	
	protected Double sum(List<Double> list)
	{
		double sum = 0.0;

		if (list != null)
		{
			Iterator<Double> it = list.iterator();
			while (it.hasNext())
			{
				sum += it.next();
			}
		}

		return sum;
	}
	
	protected Double max(List<Double> list)
	{
		return Utility.max(list);
	}
	
	protected Double min(List<Double> list)
	{
		double min = Integer.MAX_VALUE;
		Iterator<Double> it = list.iterator();
		
		while (it.hasNext())
		{
			Double i = it.next();
			if (i < min)
			{
				min = i + 0.0;
			}
		}
		
		return (min != Integer.MAX_VALUE) ? min : 0.0;
	}
	
	protected Double quartile(List<Double> list, Integer quartile)
	{		
		list = Utility.asSortedList(list);
		
		Integer n = quartile * list.size() / 4;
		// System.out.println(list.size() + ", " + n);
		
		if (list.size() == 0)
			return 0.0;
		else
			return list.get(n) + 0.0;
	}
	
	protected Double average(List<Double> list)
	{
		if (list.size() > 0)
		{
			return sum(list) / list.size();
		}
		else
		{
			return null;
		}
	}
	
	protected Double median(List<Double> list)
	{
		return quartile(list, 2);
	}

	protected Double summarisationStep(List<Double> data)
	{
		return median(data);
	}
	
	private Double[] fillBoxplot(List<Double> list)
	{
		Double[] result = new Double[5];
		
		result[0] = min(list);
		result[1] = quartile(list, 1);
		result[2] = median(list);
		result[3] = quartile(list, 3);
		result[4] = max(list);
		
		return result;
	}
	
	public Map<Integer, Double[]> getBoxplot()
	{
		return boxplot;
	}

	protected abstract boolean selectData(Date d);
	public abstract int getInterval();

}
