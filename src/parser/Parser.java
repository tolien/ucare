package parser;

import java.io.File;
import java.io.FilenameFilter;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CompletionService;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorCompletionService;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

public class Parser implements DataSource
{
	private ExecutorService execService;
	private CompletionService<ReadFile> ecs;
	private HashMap<Date, Integer> data;
	
	public Parser()
	{
		execService = Executors.newFixedThreadPool(Runtime
				.getRuntime().availableProcessors() * 8);
		ecs = new ExecutorCompletionService<ReadFile>(execService);
		
		data = new HashMap<Date, Integer>();
	}
	
	public void read(String lab, String directory)
	{
		try
		{
			File dir = new File(directory);

			List<Future<ReadFile>> futures = new ArrayList<Future<ReadFile>>();
			
			String[] files = fileList(dir);
			for (String file : files)
			{
				String filename = dir.getName() + File.separator + file;
				ReadFile reader = new ReadFile(new File(filename), lab);
				futures.add(ecs.submit(reader));

			}

			Iterator<Future<ReadFile>> futureIt = futures.iterator();
			while (futureIt.hasNext())
			{

				ReadFile rf = futureIt.next().get();
				Map<Date, Integer> readData = rf.getOccupancy();
				data.putAll(readData);
			}
			
		} catch (ExecutionException e)
		{
			e.printStackTrace();
		} catch (InterruptedException e)
		{
			e.printStackTrace();
		}
		
		execService.shutdown();
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

	@Override
	public Map<Date, Integer> getData()
	{
		return data;
	}
}
