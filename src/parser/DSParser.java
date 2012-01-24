package parser;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;
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
	
	private File file;

	private Map<String, Map<Date, Integer>> data;
	
	public void setFile(File f) throws IOException
	{
		this.file = f;
		data = new HashMap<String, Map<Date, Integer>>();
		this.read();
	}

	private void read() throws IOException
	{
		String[] nextLine = null;
		
		try
		{
			FileReader fr = new FileReader(file);

			CSVReader reader = new CSVReader(fr);
			
			while ((nextLine = reader.readNext()) != null)
			{
				if (nextLine.length > 1)
				{
					Date time = dateFormatter.parse(nextLine[DATE] + " "
							+ nextLine[TIME]);

					String labName = (String) nextLine[LAB_NAME];
					Integer occupancy = Integer.parseInt((String) nextLine[DSParser.USERS]);
					
					Map<Date, Integer> labData = data.remove(labName);
					if (labData == null)
						labData = new HashMap<Date, Integer>();
					labData.put(time,  occupancy);
					data.put(labName, labData);
				}
			}
		} catch (Exception e)
		{
			e.printStackTrace();
		}
	}

	@Override
	public Occupancy call()
	{
		return this;
	}	
	
	public Map<Date, Integer> getAbsoluteOccupancy(String lab)
	{
		return data.get(lab);
	}
	
	public Map<Date, Float> getRelativeOccupancy(String lab)
	{
		return null;
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
	
}
