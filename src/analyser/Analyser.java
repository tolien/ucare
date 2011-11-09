package analyser;

import java.util.*;

import parser.Parser;

public abstract class Analyser implements OccupancyAnalyser
{
	protected Map<Integer, Double> data = new HashMap<Integer, Double>();
	protected Integer selectedCalendarField;
	
	public void getResult()
	{
		List<Integer> keys = Parser.asSortedList(data.keySet());
		Iterator<Integer> it = keys.iterator();
		while (it.hasNext())
		{
			int day = it.next();
			System.out.println(day + ": " + data.get(day));
		}
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
		double max = 0.0;
		Iterator<Integer> it = list.iterator();
		
		while (it.hasNext())
		{
			Integer i = it.next();
			if (i > max)
			{
				max = i + 0.0;
			}
		}
		
		return max;
	}
	
	protected Double average(List<Integer> list)
	{
		return sum(list) / list.size();
	}

	protected abstract boolean selectData(Date d);
	protected abstract Double summarisationStep(List<Integer> list);
	protected abstract int getInterval();

}
