package parser;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import au.com.bytecode.opencsv.CSVReader;

public class ReadFile
{
	private static SimpleDateFormat dateFormatter = new SimpleDateFormat(
			"dd/MM/yyyy HH:mm:ss");

	public Date maxTime;
	public int max = 0;

	private HashMap<Date, Integer> data = new HashMap<Date, Integer>();

	public void read(String lab, File f) throws IOException
	{
		FileReader fr = new FileReader(f);

		CSVReader reader = new CSVReader(fr);
		String[] nextLine;

		while ((nextLine = reader.readNext()) != null)
		{
			if (nextLine[0].equals(lab))
			{
				Date time = null;
				try
				{
					time = dateFormatter.parse(nextLine[4] + " " + nextLine[5]);
				} catch (ParseException e)
				{
					e.printStackTrace();
				}
				
				int users = Integer.parseInt(nextLine[1]);
				data.put(time, users);

				if (users > max)
				{
					max = users;
					maxTime = time;
				}
			}
		}
	}
	
	public Map<Date, Integer> getData()
	{
		return data;
	}
}
