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
import java.util.Map;
import java.util.concurrent.Callable;
import java.util.concurrent.CompletionService;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorCompletionService;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

import occupancy.Utility;

public class Parser implements DataSource
{
	private ExecutorService execService;
	private CompletionService<Occupancy> ecs;
	private List<String> labs;
	private Map<Date, Integer> absoluteData;
	private Map<Date, Float> relativeData;
	
	public Parser()
	{
		execService = Executors.newFixedThreadPool(Runtime
				.getRuntime().availableProcessors());
		ecs = new ExecutorCompletionService<Occupancy>(execService);
		
		absoluteData = new HashMap<Date, Integer>();
		relativeData = new HashMap<Date, Float>();
		labs = new ArrayList<String>();
	}
	
	public void read(String lab, String directory, ParserFactory parser)
	{
		try
		{
			File dir = new File(directory);

			List<Future<Occupancy>> futures = new ArrayList<Future<Occupancy>>();
			
			String[] files = fileList(dir);
			for (String file : files)
			{
				String filename = dir.getName() + File.separator + file;
				FileParser reader = new DSParser(new File(filename));
				futures.add(ecs.submit(reader));
			}

			Iterator<Future<Occupancy>> futureIt = futures.iterator();
			while (futureIt.hasNext())
			{
				Occupancy rf = futureIt.next().get();
				Map<Date, Integer> readData = rf.getAbsoluteOccupancy(lab);
				fillLabList(rf.getLabList());
				if (readData != null)
					absoluteData.putAll(readData);
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

	@Override
	public Map<Date, Integer> getAbsoluteOccupancy()
	{
		return absoluteData;
	}
	
	@Override
	public Map<Date, Float> getRelativeOccupancy()
	{
		return relativeData;
	}

	@Override
	public List<String> getLabList()
	{
		return labs;
	}
	
	private void fillLabList(List<String> list)
	{
		Iterator<String> it = list.iterator();
		while (it.hasNext())
		{
			String needle = it.next();
		
			if (!Utility.isInList(needle, labs))
				labs.add(needle);
		}
	}
}
