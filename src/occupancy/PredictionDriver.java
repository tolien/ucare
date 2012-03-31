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
			Map<Date, Double> data = parser.getRelativeOccupancy("Hills-634", null, null);
			pred.occupancyData(data);
			
			Calendar c = Calendar.getInstance();
			c.setTime(new Date(2012 - 1900, 2, 14, 0, 0, 0));

			System.out.println(c.getTime());
			List<Double> deviations = new ArrayList<Double>();
			for (int i = 0; i < 24 * 14; i++)
			{
				c.add(Calendar.HOUR, 1);
				Double predUsers = pred.getProbability(c.getTime()) * 106;
				if (data.get(c.getTime()) != null)
				{
						Double actualUsers = data.get(c.getTime()) * 106;
						System.out.println(c.getTime() + "\t" + predUsers.intValue() + "\t" + actualUsers.intValue());
						deviations.add(predUsers - actualUsers);
				}
				else
				{
					System.out.println("No data for " + c.getTime());
				}
			}
			
			System.out.println(c.getTime());
			
			System.out.println("Average deviation: " + Utility.average(deviations));
			System.out.println("Median deviation: " + median(deviations));
			System.out.println("Max deviation: " + Utility.max(deviations));
			System.out.println("Min deviation: " + Utility.min(deviations));
		}
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
