package analyser;

import java.text.DateFormatSymbols;
import java.util.Calendar;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import ucare.Utility;

public class MonthOfYearAnalysis extends AbstractAnalyser
{
	private static DateFormatSymbols dfs = new DateFormatSymbols();
	private static int INTERVAL = Calendar.MONTH;
	private static String NAME = "Month of Year";

	@Override
	protected boolean selectData(Date d)
	{
		Calendar c = Calendar.getInstance();
		c.setTime(d);
		
		int dayOfWeek = c.get(Calendar.DAY_OF_WEEK);
		int hourOfDay = c.get(Calendar.HOUR_OF_DAY);
		
		return dayOfWeek > 0 && dayOfWeek < 6 && hourOfDay > 8 && hourOfDay < 17;
	}

	@Override
	public int getInterval()
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
			//System.out.println(months[month] + ": " + data.get(month));
		}
		
		return data;
	}
	
	public String getName()
	{
		return NAME;
	}


}
