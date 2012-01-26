package parser;

import java.io.File;
import java.io.FilenameFilter;
import java.io.IOException;
import java.util.ArrayList;
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

import occupancy.Utility;

public class Parser implements DataSource
{
	private ExecutorService execService;
	private CompletionService<Occupancy> ecs;
	private List<String> labs;
	List<Occupancy> files;
	
	public Parser()
	{

		execService = Executors.newFixedThreadPool(Runtime
				.getRuntime().availableProcessors());
		ecs = new ExecutorCompletionService<Occupancy>(execService);
		
		files = new ArrayList<Occupancy>();
		labs = new ArrayList<String>();
	}
	
	public void read(String directory, ParserFactory factory)
	{
		try
		{
			File dir = new File(directory);

			List<Future<Occupancy>> futures = new ArrayList<Future<Occupancy>>();
			
			String[] files = fileList(dir);
			for (String file : files)
			{
				String filename = dir.getCanonicalPath() + File.separator + file;
				Occupancy reader = factory.getParser(new File(filename));
				futures.add(ecs.submit(reader));
			}

			Iterator<Future<Occupancy>> futureIt = futures.iterator();
			while (futureIt.hasNext())
			{
				Occupancy rf = futureIt.next().get();
				this.files.add(rf);
				fillLabList(rf.getLabList());
			}
			
		} catch (ExecutionException e)
		{
			e.printStackTrace();
		} catch (InterruptedException e)
		{
			e.printStackTrace();
		} catch (IOException e)
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		execService.shutdownNow();
	}

	private String[] fileList(File dir)
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
	public Map<Date, Double> getAbsoluteOccupancy(String lab)
	{
		Map<Date, Double> result = new HashMap<Date, Double>();
		
		Iterator<Occupancy> it = files.iterator();
		while (it.hasNext())
		{
			Occupancy o = it.next();
			Map<Date, Double> occupancy = o.getAbsoluteOccupancy(lab);
			if (occupancy != null && occupancy.size() > 0)
				result.putAll(occupancy);
		}
		
		return result;
	}
	
	@Override
	public Map<Date, Double> getRelativeOccupancy(String lab)
	{
		Map<Date, Double> result = new HashMap<Date, Double>();
		
		Iterator<Occupancy> it = files.iterator();
		while (it.hasNext())
		{
			result.putAll(it.next().getRelativeOccupancy(lab));
		}
		
		return result;
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

	@Override
	public Map<Date, Double> getPower(String string)
	{
		// TODO Auto-generated method stub
		return null;
	}
}
