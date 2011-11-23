package occupancy;
import analyser.*;
import parser.*;
import java.util.*;


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
			p.read(args[0], "data", new CISParserFactory());
			
			System.out.println("Day of Week");
			OccupancyAnalyser a = new DayOfWeekAnalysis();
			a.analyse(p.getAbsoluteOccupancy());
			
			System.out.println("Day of Month");
			OccupancyAnalyser b = new DayOfMonthAnalysis();
		//	b.analyse(p.getAbsoluteOccupancy());
			
			System.out.println("Hour of Day");
			OccupancyAnalyser c = new HourOfDayAnalysis();
		//	c.analyse(p.getAbsoluteOccupancy());
		}
	}
}
