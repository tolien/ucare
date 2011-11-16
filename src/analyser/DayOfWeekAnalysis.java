package analyser;

import java.text.DateFormatSymbols;
import java.util.Calendar;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

import parser.Parser;

public class DayOfWeekAnalysis extends Analyser implements OccupancyAnalyser
{
	private static DateFormatSymbols dfs = new DateFormatSymbols();
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
	
	public void getResult()
	{
		List<Integer> keys = Parser.asSortedList(data.keySet());
		Iterator<Integer> it = keys.iterator();
		
		String[] days = dfs.getWeekdays();
		while (it.hasNext())
		{
			int day = it.next();
			System.out.println(days[day] + ": " + data.get(day));
		}
	}

}
