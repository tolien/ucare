package analyser;

import java.util.*;

import occupancy.Utility;

public abstract class Analyser implements OccupancyAnalyser
{
	protected Map<Integer, Double> data = new HashMap<Integer, Double>();
	protected Date minDate;
	protected Date maxDate;
	protected Date minTime;
	protected Date maxTime;
	
	public Map<Integer, Double> getResult()
	{
		List<Integer> keys = Utility.asSortedList(data.keySet());
		Iterator<Integer> it = keys.iterator();
		while (it.hasNext())
		{
			int day = it.next();
			System.out.println(day + ": " + data.get(day));
		}
		
		return data;
	}
	
	public void analyse(Map<Date, Integer> points)
	{
		HashMap<Integer, List<Integer>> totals = new HashMap<Integer, List<Integer>>();

		Iterator<Date> it = points.keySet().iterator();
		Calendar c = Calendar.getInstance();

		while (it.hasNext())
		{
			Date d = it.next();
			c.setTime(d);
			int intervalID = c.get(getInterval());

			if (selectData(d))
			{
				List<Integer> dataForInterval = totals.remove(intervalID);
				if (dataForInterval == null)
				{
					dataForInterval = new ArrayList<Integer>();
				}
				dataForInterval.add(points.get(d));
				totals.put(intervalID, dataForInterval);
			}
		}

		Iterator<Integer> intervalIterator = totals.keySet().iterator();
		while (intervalIterator.hasNext())
		{
			int interval = intervalIterator.next();
			List<Integer> intervalData = totals.get(interval);
			data.put(interval, summarisationStep(intervalData));
		}
		
		this.getResult();
	}
	
	protected Double sum(List<Integer> list)
	{
		double sum = 0.0;

		if (list != null)
		{
			Iterator<Integer> it = list.iterator();
			while (it.hasNext())
			{
				sum += it.next();
			}
		}

		return sum;
	}
	
	protected Double max(List<Integer> list)
	{
		double max = Integer.MIN_VALUE;
		Iterator<Integer> it = list.iterator();
		
		while (it.hasNext())
		{
			Integer i = it.next();
			if (i > max)
			{
				max = i + 0.0;
			}
		}
		
		return (max != Integer.MIN_VALUE) ? max : null;
	}
	
	protected Double min(List<Integer> list)
	{
		double min = Integer.MAX_VALUE;
		Iterator<Integer> it = list.iterator();
		
		while (it.hasNext())
		{
			Integer i = it.next();
			if (i < min)
			{
				min = i + 0.0;
			}
		}
		
		return (min != Integer.MAX_VALUE) ? min : null;
	}
	
	protected Double quartile(List<Integer> list, Integer quartile)
	{		
		list = Utility.asSortedList(list);
		
		Integer n = quartile * list.size() / 4;
		// System.out.println(list.size() + ", " + n);
		
		return list.get(n) + 0.0;
	}
	
	protected Double average(List<Integer> list)
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

	protected Double summarisationStep(List<Integer> data)
	{
		return max(data);
	}

	protected abstract boolean selectData(Date d);
	protected abstract int getInterval();

}
