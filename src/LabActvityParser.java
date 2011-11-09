import analyser.*;

import parser.*;


public class LabActvityParser
{
	public static void main(String[] args)
	{
		if (args.length == 0)
		{
			System.exit(1);
		} else
		{
			DataSource p = new Parser();
			p.read(args[0], "data");
			
			System.out.println("Day of Week");
			OccupancyAnalyser a = new DayOfWeekAnalysis();
			a.analyse(p.getData());
			
			System.out.println("Day of Month");
			OccupancyAnalyser b = new DayOfMonthAnalysis();
			b.analyse(p.getData());
			
			System.out.println("Hour of Day");
			OccupancyAnalyser c = new HourOfDayAnalysis();
			c.analyse(p.getData());
		}
	}
}
