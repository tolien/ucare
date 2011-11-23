package occupancy;
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
}
