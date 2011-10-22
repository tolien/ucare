package parser;

import java.io.File;
import java.io.FilenameFilter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.concurrent.CompletionService;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorCompletionService;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

public class Parser
{
	public void read(String lab)
	{
		try
		{
			File dir = new File("data");
			
			ExecutorService executor = Executors.newFixedThreadPool(Runtime.getRuntime().availableProcessors() * 2);
			CompletionService<ReadFile> ecs = new ExecutorCompletionService<ReadFile>(executor);
			List<Future<ReadFile>> futures = new ArrayList<Future<ReadFile>>();

			HashMap<Date, Integer> maxima = new HashMap<Date, Integer>();
			String[] files = fileList(dir);
			for (String file : files)
			{
				ReadFile reader = new ReadFile(new File(dir.getName() + File.separator + file),
						lab);
				futures.add(ecs.submit(reader));
			}
			
			Iterator<Future<ReadFile>> futureIt = futures.iterator();
			while (futureIt.hasNext())
			{
				ReadFile rf = futureIt.next().get();
				if (rf.max > 0)
				{
					maxima.put(rf.maxTime, rf.max);
				}
				
				
			}
			
			executor.shutdown();			
			
			List<Date> dates = asSortedList(maxima.keySet());

			Iterator<Date> it = dates.iterator();
			while (it.hasNext())
			{
				Date d = it.next();
				System.out.println(d + ": " + maxima.get(d));
			}

		} catch (IOException e)
		{
			e.printStackTrace();
		}
		catch (ExecutionException e)
		{
			e.printStackTrace();
		}
		catch (InterruptedException e)
		{
			e.printStackTrace();
		}
	}

	public String[] fileList(File dir)
	{
		FilenameFilter filter = new FilenameFilter()
		{
			@Override
			public boolean accept(File dir, String name)
			{
				return name.endsWith(".txt");
			}
		};

		return dir.list(filter);
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
