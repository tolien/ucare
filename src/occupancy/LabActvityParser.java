package occupancy;
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
			p.read("data", new DSParserFactory());
			
			System.out.println("Day of Week");
			OccupancyAnalyser a = new DayOfWeekAnalysis();
			a.analyse(p.getAbsoluteOccupancy(args[0]));
			a.getBoxplot();
			
			System.out.println("Month of Year");
			OccupancyAnalyser b = new MonthOfYearAnalysis();
			// b.analyse(p.getAbsoluteOccupancy());
			
			System.out.println("Hour of Day");
			OccupancyAnalyser c = new HourOfDayAnalysis();
			// c.analyse(p.getAbsoluteOccupancy());
		}
	}
}
