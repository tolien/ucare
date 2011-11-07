package analyser;

import java.util.Calendar;
import java.util.Date;
import java.util.List;

public class DayOfWeekAnalysis extends Analyser implements OccupancyAnalyser
{
	private static int INTERVAL = Calendar.DAY_OF_WEEK;
	
	@Override
	protected boolean selectData(Date d)
	{		
		return true;
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
