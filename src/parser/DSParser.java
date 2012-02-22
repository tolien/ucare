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
import java.util.Set;

import au.com.bytecode.opencsv.CSVReader;

public class DSParser implements Occupancy
{
	public static int LAB_NAME = 0;
	public static int USERS = 1;
	public static int TOTAL_MACHINES = 2;
	public static int DAY_NAME = 3;
	public static int DATE = 4;
	public static int TIME = 5;

	private SimpleDateFormat dateFormatter = new SimpleDateFormat(
			"dd/MM/yyyy HH:mm:ss");

	public Date maxTime;
	public int max = 0;
	
	private CSVReader reader;

	private Map<String, Map<Date, Double[]>> data;
	
	public void setFile(File f) throws FileNotFoundException
	{
		data = new HashMap<String, Map<Date, Double[]>>();

		FileReader fr = new FileReader(f);
		reader = new CSVReader(fr);
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
		Map<String, Map<Date, Double[]>> set = readSet();

		Map<Date, Double[]> existing = data.get(lab);
		
		if (existing == null)
			existing = new HashMap<Date, Double[]>();
		
		if (set != null && set.get(lab) != null)
			existing.putAll(set.get(lab));

			data.put(lab, existing);
		while (set.size() > 0)
		{
			set = readSet();
			if (set != null && set.size() > 0)
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
			while ((nextLine = reader.readNext()) != null && nextLine.length > 1)
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
	
	public Map<Date, Double> getAbsoluteOccupancy(String lab)
	{
		read(lab);
		
		Map<Date, Double> result = new HashMap<Date, Double>();
		
		Map<Date, Double[]> labData = data.get(lab);
		Iterator<Date> it = labData.keySet().iterator();
		while (it.hasNext())
		{
			Date d = it.next();
			Double[] occupancy = labData.get(d);
			result.put(d, occupancy[0]);
		}
		
		return result;
	}
	
	public Map<Date, Double> getRelativeOccupancy(String lab)
	{
		read(lab);
		
		Map<Date, Double> result = new HashMap<Date, Double>();
		
		Map<Date, Double[]> labData = data.get(lab);
		Iterator<Date> it = labData.keySet().iterator();
		while (it.hasNext())
		{
			Date d = it.next();
			Double[] occupancy = labData.get(d);
			result.put(d, occupancy[1]);
		}
		
		return result;
	}
	
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
			if (d.compareTo(start) < 0 || d.compareTo(end) > 0)
			{
				return null;
			}
			else
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
			if (d.compareTo(start) < 0 || d.compareTo(end) > 0)
			{
				return null;
			}
			else
			{
				Double[] occupancy = labData.get(d);
				result.put(d, occupancy[1]);
			}
		}
		
		return result;
	}
	
}
