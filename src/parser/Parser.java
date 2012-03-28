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
import java.util.Observable;
import java.util.concurrent.CompletionService;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorCompletionService;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import occupancy.Utility;

public class Parser extends Observable implements DataSource
{
	private ExecutorService execService;
	private CompletionService<Occupancy> ecs;
	private CompletionService<Power> powerEcs;
	private List<String> labs;
	
	private List<Occupancy> occupancyFiles;
	private List<Power> powerFiles;
	
	public Parser()
	{
		powerEcs = new ExecutorCompletionService<Power>(getExecutor());
		
		occupancyFiles = new ArrayList<Occupancy>();
		powerFiles = new ArrayList<Power>();
		labs = new ArrayList<String>();
	}
	
	private ExecutorService getExecutor()
	{
		if (execService == null || execService.isShutdown())
		{
			execService = Executors.newFixedThreadPool(Runtime.getRuntime().availableProcessors());
		}
		
		return execService;
	}
	
	public void read(String directory, ParserFactory factory)
	{
		ecs = new ExecutorCompletionService<Occupancy>(getExecutor());
		try
		{
			File dir = new File(directory);
			
			String[] files = fileList(dir, ".txt");
			
			for (String file : files)
			{
				String filename = dir.getCanonicalPath() + File.separator + file;
				Occupancy reader = factory.getParser(new File(filename));
				ecs.submit(reader);
			}
			
			for (int i = 0; i < files.length; i++)
			{
				Occupancy rf = ecs.take().get();
				this.occupancyFiles.add(rf);
				fillLabList(rf.getLabList());
			}
			
			files = fileList(dir, ".csv");
			for (String file : files)
			{
				String filename = dir.getCanonicalPath() + File.separator + file;
				Power reader = factory.getPowerParser(new File(filename));
				powerEcs.submit(reader);
			}
			
			for (int i = 0; i < files.length; i++)
			{
				Power p = powerEcs.take().get();
				this.powerFiles.add(p);
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
	}

	private String[] fileList(File dir, String ext)
	{
		final String extension = ext;
		FilenameFilter filter = new FilenameFilter()
		{
			@Override
			public boolean accept(File dir, String name)
			{
				return name.endsWith(extension);
			}
		};

		return dir.list(filter);
	}

	private Map<Date, Double> getAbsoluteOccupancy(String lab)
	{
		CompletionService<Map<Date, Double>> cs = new ExecutorCompletionService<Map<Date, Double>>(getExecutor());
		Map<Date, Double> result = new HashMap<Date, Double>();
		
		Iterator<Occupancy> it = occupancyFiles.iterator();
		while (it.hasNext())
		{
			cs.submit(new OccupancyFetcher(it.next(), lab));
		}
		
		for (int i = 0; i < occupancyFiles.size(); i++)
		{
			Map<Date, Double> occupancy;
			try
			{
				occupancy = cs.take().get();
				if (occupancy != null && occupancy.size() > 0)
					result.putAll(occupancy);
				
			} catch (InterruptedException e)
			{
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (ExecutionException e)
			{
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		
		return result;
	}
	
	private Map<Date, Double> getRelativeOccupancy(String lab)
	{
		CompletionService<Map<Date, Double>> cs = new ExecutorCompletionService<Map<Date, Double>>(getExecutor());
		Map<Date, Double> result = new HashMap<Date, Double>();
		
		Iterator<Occupancy> it = occupancyFiles.iterator();
		while (it.hasNext())
		{
			OccupancyFetcher fetch = new OccupancyFetcher(it.next(), lab);
			fetch.setRelative(true);
			cs.submit(fetch);
		}
		
		for (int i = 0; i < occupancyFiles.size(); i++)
		{
			Map<Date, Double> occupancy;
			try
			{
				occupancy = cs.take().get();
				if (occupancy != null && occupancy.size() > 0)
					result.putAll(occupancy);
				
			} catch (InterruptedException e)
			{
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (ExecutionException e)
			{
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		
		
		execService.shutdown();
		
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
	public Map<Date, List<Double>> getPower(String string, Date start, Date end)
	{	
		Map<Date, List<Double>> result = new HashMap<Date, List<Double>>();
		
		Iterator<Power> powerIt = powerFiles.iterator();
		while (powerIt.hasNext())
		{
			Power p = powerIt.next();
			Map<Date, List<Double>> power = p.getPower(start, end);
			if (power != null && power.size() > 0)
			{
				result.putAll(power);
			}
		}
		
		return result;
	}
	
	@Override
	public Map<Date, Double> getTotalPower(String string, Date start, Date end)
	{	
		Map<Date, Double> result = new HashMap<Date, Double>();
		
		Iterator<Power> powerIt = powerFiles.iterator();
		while (powerIt.hasNext())
		{
			Power p = powerIt.next();
			Map<Date, Double> power = p.getTotalPower(start, end);
			if (power != null && power.size() > 0)
			{
				result.putAll(power);
			}
		}
		
		return result;
	}

	@Override
	public Map<Date, Double> getAbsoluteOccupancy(String labName, Date start,
			Date end)
	{
		return getOccupancy(labName, start, end, false);
	}
	
	@Override
	public Map<Date, Double> getRelativeOccupancy(String labName, Date start,
			Date end)
	{
		return getOccupancy(labName, start, end, true);
	}
	
	private Map<Date, Double> getOccupancy(String lab, Date start, Date end, boolean relative)
	{
		if (start == null || end == null)
		{
			if (relative)
				return getRelativeOccupancy(lab);
			else
				return getAbsoluteOccupancy(lab);
		}
		
		CompletionService<Map<Date, Double>> cs = new ExecutorCompletionService<Map<Date, Double>>(getExecutor());
		Map<Date, Double> result = new HashMap<Date, Double>();
		
		Iterator<Occupancy> it = occupancyFiles.iterator();
		while (it.hasNext())
		{
			OccupancyFetcher fetch = new OccupancyFetcher(it.next(), lab, start, end);
			fetch.setRelative(relative);
			cs.submit(fetch);
		}
		
		for (int i = 0; i < occupancyFiles.size(); i++)
		{
			Map<Date, Double> occupancy;
			try
			{
				occupancy = cs.take().get();
				if (occupancy != null && occupancy.size() > 0)
					result.putAll(occupancy);
		
				setChanged();
				double progress = i / (double) occupancyFiles.size();
				notifyObservers(progress);
				
			} catch (InterruptedException e)
			{
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (ExecutionException e)
			{
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		
		execService.shutdown();
		
		return result;
	}

	@Override
	public Map<Date, List<Double>> getTemperature(String labName, Date start, Date end)
	{	
		Map<Date, List<Double>> result = new HashMap<Date, List<Double>>();
		
		Iterator<Power> powerIt = powerFiles.iterator();
		while (powerIt.hasNext())
		{
			Power p = powerIt.next();
			Map<Date, List<Double>> temp = p.getTemperature(start, end);
			if (temp != null && temp.size() > 0)
			{
				result.putAll(temp);
			}
		}
		
		return result;
	}
	
	@Override
	public Map<Date, Double> getAverageTemperature(String labName, Date start, Date end)
	{
		Map<Date, Double> result = new HashMap<Date, Double>();
		
		Iterator<Power> powerIt = powerFiles.iterator();
		while (powerIt.hasNext())
		{
			Power p = powerIt.next();
			Map<Date, List<Double>> temp = p.getTemperature(start, end);
			if (temp != null && temp.size() > 0)
			{
				Iterator<Date> it = temp.keySet().iterator();
				while (it.hasNext())
				{
					Date d = it.next();
					result.put(d, Utility.average(temp.get(d)));
				}
			}
		}
		
		return result;
	}

	@Override
	public Map<Date, Double> getCO2(String labName, Date start, Date end)
	{	
		Map<Date, Double> result = new HashMap<Date, Double>();
		
		Iterator<Power> powerIt = powerFiles.iterator();
		while (powerIt.hasNext())
		{
			Power p = powerIt.next();
			Map<Date, Double> co2 = p.getCO2(start, end);
			if (co2 != null && co2.size() > 0)
			{
				result.putAll(co2);
			}
		}
		
		return result;
	}
}
