package analyser;

import java.util.Calendar;
import java.util.Date;

public class HourOfDayAnalysis extends Analyser
{
	private static int INTERVAL = Calendar.HOUR_OF_DAY;
	private static String NAME = "Hour of Day";

	@Override
	protected boolean selectData(Date d)
	{	
		Calendar c = Calendar.getInstance();
		c.setTime(d);
		int day = c.get(Calendar.DAY_OF_WEEK);
		return true; // (day != Calendar.SUNDAY && day != Calendar.SATURDAY);
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