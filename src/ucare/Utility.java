package ucare;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;


public class Utility
{
	public static boolean isInList(String needle, List<String> haystack)
	{
		Iterator<String> haystackIt = haystack.iterator();
		boolean found = false;
		while (haystackIt.hasNext() && !found)
		{
			String hay = haystackIt.next();
			if (hay.equals(needle))
			{
				found = true;
			}
		}
		
		return found;
	}
	
	/*
	 * Code for this method was taken from
	 * http://stackoverflow.com/questions/740299
	 */
	public static <T extends Comparable<? super T>> List<T> asSortedList(
			Collection<T> c)
	{
		List<T> list = new ArrayList<T>(c);
		java.util.Collections.sort(list);
		return list;
	}
	
	public static Double average(List<Double> list)
	{
		if (list.size() > 0)
		{
			return sum(list) / list.size();
		}
		else
		{
			return null;
		}
	}
	
	public static Double sum(List<Double> list)
	{
		double sum = 0.0;

		if (list != null)
		{
			Iterator<Double> it = list.iterator();
			while (it.hasNext())
			{
				sum += it.next();
			}
		}

		return sum;
	}
	
	public static Double max(List<Double> list)
	{
		double max = Integer.MIN_VALUE;
		Iterator<Double> it = list.iterator();
		
		while (it.hasNext())
		{
			Double i = it.next();
			if (i > max)
			{
				max = i + 0.0;
			}
		}
		
		return (max != Integer.MIN_VALUE) ? max : 0.0;
	}
	
	public static Double min(List<Double> list)
	{
		double min = Integer.MAX_VALUE;
		Iterator<Double> it = list.iterator();
		
		while (it.hasNext())
		{
			Double i = it.next();
			if (i < min)
			{
				min = i + 0.0;
			}
		}
		
		return (min != Integer.MAX_VALUE) ? min : 0.0;
	}
}
