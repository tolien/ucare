package analyser;

import java.util.Calendar;
import java.util.Date;

public class DayOfMonthAnalysis extends Analyser
{
	private static int INTERVAL = Calendar.DAY_OF_MONTH;
	private static String NAME = "Day of Month";
	
	@Override
	protected boolean selectData(Date d)
	{
		return true;
	}

	@Override
	public int getInterval()
	{
		return INTERVAL;
	}
	
	public String getName()
	{
		return NAME;
	}

}
