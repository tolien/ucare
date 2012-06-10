package tests;

import static org.junit.Assert.*;

import java.io.File;
import java.util.*;

import org.junit.Before;
import org.junit.Test;

import parser.*;
import ucare.Utility;

/*
 * This class t
 */
public class DSParserTest
{
	private final static File testFile = new File("test-data/stats-log/23-1-2012.txt");
	private Calendar start = Calendar.getInstance();
	private Calendar end = Calendar.getInstance();
	private Occupancy parser;
	
	private Calendar futureStart = Calendar.getInstance();
	private Calendar futureEnd = Calendar.getInstance();
	
	@Before
	public void setUp() throws Exception
	{
		parser = new DSParser();
		parser.setFile(testFile);
		parser.call();
		
		// 23 Jan 2012, 12:00:00
		start.set(2012, 0, 23, 12, 00, 00);
		
		//23 Jan 2012, 16:00:00
		end.set(2012, 1, 23, 16, 00, 00);

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
	public void dateFilterNoAvailableData()
	{
		Map<Date, Double> noData = parser.getAbsoluteOccupancy("Hills-634", futureStart.getTime(), futureEnd.getTime());
		assertTrue(noData.isEmpty());
	}

}
