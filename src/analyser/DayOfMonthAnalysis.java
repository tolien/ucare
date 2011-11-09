package analyser;

import java.util.Calendar;
import java.util.Date;
import java.util.List;

public class DayOfMonthAnalysis extends Analyser implements OccupancyAnalyser
{
	private static int INTERVAL = Calendar.DAY_OF_MONTH;
	
	@Override
	protected boolean selectData(Date d)
	{
		Calendar c = Calendar.getInstance();
		c.setTime(d);
		int hour = c.get(Calendar.HOUR_OF_DAY);
		
		return (hour >= 9 && hour <= 18);
	}

	@Override
	protected Double summarisationStep(List<Integer> list)
	{
		return max(list);
	}

	@Override
	protected int getInterval()
	{
		return INTERVAL;
	}

}
