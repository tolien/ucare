package analyser;

import java.text.DateFormatSymbols;
import java.util.Calendar;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import occupancy.Utility;

import parser.Parser;

public class DayOfWeekAnalysis extends Analyser
{
	private static DateFormatSymbols dfs = new DateFormatSymbols();
	private static int INTERVAL = Calendar.DAY_OF_WEEK;
	
	@Override
	protected boolean selectData(Date d)
	{
		return (d.getHours() > 9 && d.getHours() < 17);
	}

	@Override
	protected int getInterval()
	{
		return INTERVAL;
	}
	
	public Map<Integer, Double> getResult()
	{
		List<Integer> keys = Utility.asSortedList(data.keySet());
		Iterator<Integer> it = keys.iterator();
		
		String[] days = dfs.getWeekdays();
		while (it.hasNext())
		{
			int day = it.next();
			System.out.println(days[day] + ": " + data.get(day));
		}
		
		return data;
	}

}
