package parser;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.concurrent.Callable;

import au.com.bytecode.opencsv.CSVReader;

public class ReadFile implements Callable<ReadFile>
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
	private String lab;

	private Map<Date, Object[]> data;

	public ReadFile(File f, String lab)
	{
		this.file = f;
		this.lab = lab;
		
		data = new HashMap<Date, Object[]>();
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
				if (nextLine.length > 1 && (lab == null || lab.equals(nextLine[LAB_NAME])))
				{
					Date time = null;
					time = dateFormatter.parse(nextLine[DATE] + " "
							+ nextLine[TIME]);
					
					data.put(time, nextLine);
				}
			}
		} catch (Exception e)
		{
			System.out.println(nextLine[DATE] + ", " + nextLine[TIME]);
			e.printStackTrace();
		}
	}

	@Override
	public ReadFile call() throws Exception
	{
		this.read();
		return this;
	}	
	
	public Map<Date, Integer> getOccupancy()
	{
		HashMap<Date, Integer> result = new HashMap<Date, Integer>();
		if (data.keySet().size() > 0)
		{
			Iterator<Date> it = data.keySet().iterator();
			while (it.hasNext())
			{
				Date d = it.next();
				Object[] line = data.get(d);
				result.put(d, Integer.parseInt((String) line[ReadFile.USERS]));
			}
		}
		
		return result;
	}
	
}
