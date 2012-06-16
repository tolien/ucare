package tests;

import static org.junit.Assert.*;

import java.util.*;

import org.junit.Before;
import org.junit.Test;

import parser.*;
import ucare.Utility;

/*
 * This class t
 */
public class ParserTest
{
	private ParserFactory pf;
	private final static String dir = "test-data/stats-log";
	private final static String labName = "Hills-634";

	private Calendar start = Calendar.getInstance();
	private Calendar end = Calendar.getInstance();
	private Parser parser;
	
	private Calendar futureStart = Calendar.getInstance();
	private Calendar futureEnd = Calendar.getInstance();
	
	private Calendar pastStart = Calendar.getInstance();
	private Calendar pastEnd = Calendar.getInstance();
	String dudLab = "12345";
	
	@Before
	public void setUp() throws Exception
	{
		pf = new DSParserFactory();
		parser = new Parser();
		parser.read(dir, pf);
		
		// 1 Jan 2012
		start.set(2012, 0, 1);
		
		//31 Jan 2012
		end.set(2012, 1, 31);

		//1 Jan 2050
		futureStart.set(2050, 0, 1);
		
		//1 Dec 2050
		futureEnd.set(2050, 11, 1);
	}

	@Test
	public void labList()
	{
		List<String> labs = parser.getLabList();
		assertEquals(labs.size(), 60);
	}
	
	@Test
	public void dateFilter()
	{
		Map<Date, Double> limited = parser.getAbsoluteOccupancy("Hills-634", start.getTime(), end.getTime());
		if (!limited.isEmpty())
		{
			List<Date> dates = Utility.asSortedList(limited.keySet());
			assertTrue(dates.get(0).compareTo(start.getTime()) >= 0);
			assertTrue(dates.get(dates.size() - 1).compareTo(end.getTime()) <= 0);
		}
		
		Map<Date, Double> reversed = parser.getAbsoluteOccupancy("Hills-634", end.getTime(), start.getTime());
		assertTrue(reversed.isEmpty());
	}
	
	@Test
	public void noOrOutOfRangeDates()
	{
		Map<Date, Double> noDate = parser.getAbsoluteOccupancy(labName, null, null);
		Map<Date, Double> bigRange = parser.getAbsoluteOccupancy(labName, pastStart.getTime(), futureEnd.getTime());
		
		assertEquals(noDate.size(), bigRange.size());
	}
	
	@Test
	public void dateFilterNoStartDate()
	{
		Map<Date, Double> noStart = parser.getAbsoluteOccupancy("Hills-634", null, end.getTime());
		if (!noStart.isEmpty())
		{
			List<Date> dates = Utility.asSortedList(noStart.keySet());
			assertTrue(dates.get(dates.size() - 1).compareTo(end.getTime()) <= 0);
		}
	}
	
	@Test
	public void dateFilterNoEndDate()
	{
		
		Map<Date, Double> noEnd = parser.getAbsoluteOccupancy("Hills-634", start.getTime(), null);
		if (!noEnd.isEmpty())
		{
			List<Date> dates = Utility.asSortedList(noEnd.keySet());
			assertTrue(dates.get(0).compareTo(start.getTime()) >= 0);
		}
	}
	
	@Test
	public void noAvailableData()
	{
		Map<Date, Double> noData = parser.getAbsoluteOccupancy("Hills-634", futureStart.getTime(), futureEnd.getTime());
		assertTrue(noData.isEmpty());
		
		assertTrue(!parser.getLabList().contains(dudLab));
		noData = parser.getAbsoluteOccupancy(dudLab, start.getTime(), end.getTime());
		assertTrue(noData.isEmpty());
		noData = parser.getAbsoluteOccupancy("", start.getTime(), end.getTime());
		assertTrue(noData.isEmpty());
	}

}
