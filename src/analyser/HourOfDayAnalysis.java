package analyser;

import java.util.Calendar;
import java.util.Date;
import java.util.List;

public class HourOfDayAnalysis extends Analyser implements OccupancyAnalyser
{
	private static int INTERVAL = Calendar.HOUR_OF_DAY;

	@Override
	protected boolean selectData(Date d)
	{	
		Calendar c = Calendar.getInstance();
		c.setTime(d);
		int day = c.get(Calendar.DAY_OF_WEEK);
		return (day != Calendar.SUNDAY && day != Calendar.SATURDAY);
	}
	
	@Override
	protected int getInterval()
	{
		return INTERVAL;
	}
}