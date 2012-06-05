package ucare;
import java.text.DateFormat;
import java.util.*;

import analyser.*;
import prediction.*;
import parser.*;


public class PredictionDriver
{
	@SuppressWarnings("deprecation")
	public static void main(String[] args)
	{
		if (args.length == 0)
		{
			System.exit(1);
		} else
		{
			int year = Integer.parseInt(args[0]);
			int month = Integer.parseInt(args[1]) - 1;
			int day = Integer.parseInt(args[2]);
			int days = Integer.parseInt(args[3]);
			
			DataSource parser = new Parser();
			parser.read("data", new DSParserFactory());
			
			Predictor pred = new OccupancyBasedPrediction();
			pred.addAnalyser(new DayOfWeekAnalysis());
			pred.addAnalyser(new HourOfDayAnalysis());
			pred.addAnalyser(new MonthOfYearAnalysis());
			Map<Date, Double> data = parser.getRelativeOccupancy("Hills-634", null, null);
			pred.occupancyData(data);
			
			Calendar c = Calendar.getInstance();
			c.setTime(new Date(year - 1900, month, day, 0, 0, 0));

			List<Double> deviations = new ArrayList<Double>();
			for (int i = 0; i < 24 * days; i++)
			{
				Double predUsers = pred.getProbability(c.getTime()) * 106;
				if (data.get(c.getTime()) != null)
				{
						Double actualUsers = data.get(c.getTime()) * 106;
						System.out.println(DateFormat.getInstance().format(c.getTime()) + "\t" + predUsers.intValue() + "\t" + actualUsers.intValue());
						deviations.add(predUsers - actualUsers);
				}
				else
				{
					//System.out.println("No data for " + c.getTime());
				}
				c.add(Calendar.HOUR, 1);
			}
			
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
