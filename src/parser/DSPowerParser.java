package parser;

import java.io.*;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

import au.com.bytecode.opencsv.CSVReader;

public class DSPowerParser implements Power
{
	public static int DATE = 0;
	public static int TIME = 1;
	public static int FIRST_CURRENT = 2;
	public static int CURRENT_SETS = 3;

	private static double[] POWER_FACTOR = { 0.9, 0.8, 0.7 };

	private File file;

	private Map<Date, Double> data;

	private SimpleDateFormat dateFormatter = new SimpleDateFormat(
			"dd/MM/yyyy HH:mm:ss");

	@Override
	public void setFile(File f) throws IOException
	{
		data = new HashMap<Date, Double>();
		this.file = f;
	}

	@Override
	public Map<Date, Double> getPower()
	{
		return data;
	}

	private void read() throws FileNotFoundException
	{
		String[] line = null;

		FileReader fr = new FileReader(file);

		CSVReader reader = new CSVReader(fr, ',', '"', 2);
		try
		{
			while ((line = reader.readNext()) != null)
			{
				if (line.length > 1 && line[DATE].length() > 0)
				{
					Date time = null;
					int offset = 0;
					
					double voltage = 230.0;

					if (line[TIME].contains(":"))
					{
						time = dateFormatter.parse(line[DATE] + " " + line[TIME]);

						voltage = Double.parseDouble(line[FIRST_CURRENT + offset
								+ CURRENT_SETS * 3 + 2]);
					} else
					{
						offset = -1;
						time = dateFormatter.parse(line[DATE]+":00");
						
					}

					double power = 0.0;

					for (int pos = FIRST_CURRENT + offset; pos < FIRST_CURRENT
							+ offset + CURRENT_SETS * 3; pos += 3)
					{
						for (int phase = 0; phase < 3; phase++)
						{
							double current = Double.parseDouble(line[pos + phase]);
							power += current * voltage * POWER_FACTOR[phase];
						}
					}
					data.put(time, power);
				}
			}
		} catch (NumberFormatException e)
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e)
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (ParseException e)
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	@Override
	public Power call() throws Exception
	{
		read();
		return this;
	}
}
