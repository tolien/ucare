package parser;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import au.com.bytecode.opencsv.CSVReader;

public class DSParser implements Occupancy
{
	public final static int LAB_NAME = 0;
	public final static int USERS = 1;
	public final static int TOTAL_MACHINES = 2;
	public final static int DAY_NAME = 3;
	public final static int DATE = 4;
	public final static int TIME = 5;

	private final static SimpleDateFormat dateFormatter = new SimpleDateFormat(
			"dd/MM/yyyy HH:mm:ss");

	private Date maxTime;
	private int max = 0;
	
	private File f;
	private FileReader fr;
	private CSVReader reader;

	private Map<String, Map<Date, Double[]>> data;
	private Map<String, Integer> capacity;
	
	public void setFile(File file) throws FileNotFoundException
	{
		data = new HashMap<String, Map<Date, Double[]>>();

		f = file;
		fr = new FileReader(f);
		reader = new CSVReader(fr);
		capacity = new HashMap<String, Integer>();
	}

	private void read(int sets)
	{
		for (int loops = 0; loops < sets; loops++)
		{
			Map<String, Map<Date, Double[]>> set = readSet();
			data.putAll(set);
		}
	}
	
	private void read(String lab)
	{
		reset();
		Map<String, Map<Date, Double[]>> set = readSet();

		Map<Date, Double[]> existing = data.get(lab);
		
		if (existing == null)
			existing = new HashMap<Date, Double[]>();
		
		if (set != null && set.get(lab) != null)
			existing.putAll(set.get(lab));

			data.put(lab, existing);
		while (!set.isEmpty())
		{
			set = readSet();
			if (set != null && set.size() > 0 && (set.get(lab) != null && !set.get(lab).isEmpty()))
			{
				existing = data.get(lab);
				existing.putAll(set.get(lab));
				data.put(lab, existing);
			}
		}
	}
	
	
	private Map<String, Map<Date, Double[]>> readSet()
	{
		Map<String, Map<Date, Double[]>> data = new HashMap<String, Map<Date, Double[]>>();
		String[] nextLine = null;
		
		try
		{
			while ((nextLine = reader.readNext()) != null && nextLine.length == 6)
			{
				Date time;
				time = dateFormatter.parse(nextLine[DATE] + " "
							+ nextLine[TIME]);

				String labName = (String) nextLine[LAB_NAME];
				Double occupancy = Double.parseDouble((String) nextLine[DSParser.USERS]);
				Double machines = Double.parseDouble((String) nextLine[DSParser.TOTAL_MACHINES]);
				
				Map<Date, Double[]> labData = (data.get(labName) == null) ? new HashMap<Date, Double[]>() : data.get(labName);
				Double[] entry = { occupancy, occupancy / machines };
				labData.put(time,  entry);
				data.put(labName, labData);
				capacity.put(labName, machines.intValue());
					
			}
		} catch (NumberFormatException e)
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e)
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}catch (ParseException e)
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return data;
	}

	@Override
	public Occupancy call()
	{
		this.read(1);
		return this;
	}
	
	@Override
	public List<String> getLabList()
	{
		List<String> list = new ArrayList<String>();
		
		Iterator<String> it = data.keySet().iterator();
		while (it.hasNext())
		{
			list.add(it.next());
		}
		
		return list;
	}

	@Override
	public Map<Date, Double> getAbsoluteOccupancy(String lab, Date start,
			Date end)
	{
		read(lab);
		
		Map<Date, Double> result = new HashMap<Date, Double>();
		
		Map<Date, Double[]> labData = data.get(lab);
		Iterator<Date> it = labData.keySet().iterator();
		while (it.hasNext())
		{
			Date d = it.next();
			/**
			 *  if start or end are not null
			 *  	and start is after the date or end is before the date
			 *  then return null
			 */
			if ((start != null && d.compareTo(start) >= 0) && (end != null && d.compareTo(end) <= 0))
			{
				Double[] occupancy = labData.get(d);
				result.put(d, occupancy[0]);
			}
		}
		
		return result;
	}
	
	@Override
	public Map<Date, Double> getRelativeOccupancy(String lab, Date start,
			Date end)
	{
		read(lab);
		
		Map<Date, Double> result = new HashMap<Date, Double>();
		
		Map<Date, Double[]> labData = data.get(lab);
		Iterator<Date> it = labData.keySet().iterator();
		while (it.hasNext())
		{
			Date d = it.next();
			if ((start != null && d.compareTo(start) >= 0) && (end != null && d.compareTo(end) <= 0))
			{
				Double[] occupancy = labData.get(d);
				result.put(d, occupancy[1]);
			}
		}
		
		return result;
	}
	
	@Override
	public Map<String, Integer> getCapacity()
	{
		return capacity;
	}
	
	private void reset()
	{
		try
		{
			fr = new FileReader(f);
			reader = new CSVReader(fr);
		} catch (IOException e)
		{	}
		
		reader = new CSVReader(fr);
	}
	
}
