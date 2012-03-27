package occupancy;
import java.util.*;

import analyser.*;
import prediction.*;
import parser.*;


public class PredictionDriver
{
	public static void main(String[] args)
	{
		if (args.length == 0)
		{
			System.exit(1);
		} else
		{
			DataSource parser = new Parser();
			parser.read("data", new DSParserFactory());
			
			Predictor pred = new OccupancyBasedPrediction();
			pred.addAnalyser(new DayOfWeekAnalysis());
			pred.addAnalyser(new HourOfDayAnalysis());
			pred.addAnalyser(new MonthOfYearAnalysis());
			Map<Date, Double> data = parser.getRelativeOccupancy("Hills-634");
			pred.occupancyData(data);
			
			Calendar c = Calendar.getInstance();
			c.setTime(new Date(2011 - 1900, 9, 1, 0, 0, 0));

			System.out.println(c.getTime());
			List<Double> deviations = new ArrayList<Double>();
			for (int i = 0; i < 2 * 24 * 7 * 20; i++)
			{
				c.add(Calendar.MINUTE, 30);
				double predUsers = pred.getProbability(c.getTime()) * 106 * 10;
				if (data.get(c.getTime()) != null)
				{
						double actualUsers = data.get(c.getTime()) * 106;
						System.out.println(c.getTime() + "\t" + predUsers + "\t" + actualUsers);
						deviations.add(predUsers - actualUsers);
				}
			}
			
			System.out.println(c.getTime());
			
			System.out.println("Average deviation: " + Utility.average(deviations));
			System.out.println("Median deviation: " + median(deviations));
			System.out.println("Max deviation: " + max(deviations));
		}
	}
	
	public static Double max(List<Double> list)
	{
		double max = Integer.MIN_VALUE;
		Iterator<Double> it = list.iterator();
		
		while (it.hasNext())
		{
			Double i = it.next();
			if (i > max)
			{
				max = i + 0.0;
			}
		}
		
		return (max != Integer.MIN_VALUE) ? max : null;
	}
	
	public static Double quartile(List<Double> list, Integer quartile)
	{		
		list = Utility.asSortedList(list);
		
		Integer n = quartile * list.size() / 4;
		// System.out.println(list.size() + ", " + n);
		
		return list.get(n) + 0.0;
	}
	
	public static Double median(List<Double> list)
	{
		return quartile(list, 2);
	}
}
