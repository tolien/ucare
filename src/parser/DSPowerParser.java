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
	
	public static int CO2 = 15;
	

	//private static double[] POWER_FACTOR = { 0.9, 0.8, 0.7 };
	private static double[] POWER_FACTOR = { 0.85, 0.875, 0.65 };

	private File file;

	private Map<Date, List<Double>> powerData;
	private Map<Date, List<Double>> tempData;
	private Map<Date, Double> co2Data;

	private SimpleDateFormat dateFormatter = new SimpleDateFormat(
			"dd/MM/yyyy HH:mm:ss");

	@Override
	public void setFile(File f) throws IOException
	{
		powerData = new HashMap<Date, List<Double>>();
		tempData = new HashMap<Date, List<Double>>();
		co2Data = new HashMap<Date, Double>();
		this.file = f;
	}

	@Override
	public Map<Date, List<Double>> getPower()
	{
		return powerData;
	}
	
	@Override
	public Map<Date, Double> getTotalPower()
	{
		Map<Date, Double> totals = new HashMap<Date, Double>();
		
		Iterator<Date> it = powerData.keySet().iterator();
		while (it.hasNext())
		{
			Date d = it.next();
			List<Double> power = powerData.get(d);
			
			double totalPower = 0.0;
			Iterator<Double> pIt = power.iterator();
			while (pIt.hasNext())
			{
				totalPower += pIt.next();
			}
			
			totals.put(d, totalPower);
		}
		
		return totals;
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
					double co2;
					List<Double> tempValues = new ArrayList<Double>();

					if (line[TIME].contains(":"))
					{
						time = dateFormatter.parse(line[DATE] + " " + line[TIME]);

						voltage = Double.parseDouble(line[FIRST_CURRENT + offset
								+ CURRENT_SETS * 3 + 2]);
						
						 co2 = Double.parseDouble(line[CO2 + offset]);
						 
						 for (int i = 1; i <= 4; i++)
						 {
							 try {
							 double temp = Double.parseDouble(line[CO2 + offset - i]);
							if (i != 2)
								tempValues.add(temp);
							 }
							 catch (NumberFormatException e)
							 {
								 
							 }
						 }
					}
					else
					{
						offset = -1;
						time = dateFormatter.parse(line[DATE]+":00");
						 co2 = Double.parseDouble(line[CO2 + offset - 1]);
						 
						 for (int i = 3; i <= 4; i++)
						 {
							 tempValues.add(Double.parseDouble(line[CO2 + offset - i]));
						 }
						
					}

					co2Data.put(time, co2);

					List<Double> powerValues = new ArrayList<Double>();
					for (int pos = FIRST_CURRENT + offset; pos < FIRST_CURRENT
							+ offset + CURRENT_SETS * 3; pos += 3)
					{
						double power = 0.0;
						for (int phase = 0; phase < 3; phase++)
						{	

							double current = Double.parseDouble(line[pos + phase]);
							power += current * voltage * POWER_FACTOR[phase];
						}
						powerValues.add(power); 
					}
					powerData.put(time, powerValues);
					tempData.put(time, tempValues);
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

	@Override
	public Map<Date, List<Double>> getTemperature()
	{
		return tempData;
	}

	@Override
	public Map<Date, Double> getCO2()
	{
		return co2Data;
	}
}
