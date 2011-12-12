package analyser;

import java.text.DateFormatSymbols;
import java.util.Calendar;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import occupancy.Utility;

public class MonthOfYearAnalysis extends Analyser
{
	private static DateFormatSymbols dfs = new DateFormatSymbols();
	private static int INTERVAL = Calendar.MONTH;

	@Override
	protected boolean selectData(Date d)
	{
		return true;
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
		
		String[] months = dfs.getMonths();
		while (it.hasNext())
		{
			int month = it.next();
			System.out.println(months[month] + ": " + data.get(month));
		}
		
		return data;
	}


}
