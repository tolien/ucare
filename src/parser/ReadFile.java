package parser;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

import au.com.bytecode.opencsv.CSVReader;

public class ReadFile implements Iterable<Object[]>, Callable<ReadFile>
{
	private static int LAB_NAME = 0;
	private static int USERS = 1;
	private static int TOTAL_MACHINES = 2;
	private static int DAY_NAME = 3;
	private static int DATE = 4;
	private static int TIME = 5;

	private SimpleDateFormat dateFormatter = new SimpleDateFormat(
			"dd/MM/yyyy HH:mm:ss");

	public Date maxTime;
	public int max = 0;
	
	private File file;
	private String lab;

	private List<Object[]> data;

	public ReadFile(File f, String lab) throws IOException
	{
		this.file = f;
		this.lab = lab;
		
		data = new ArrayList<Object[]>();
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

					int users = Integer.parseInt(nextLine[USERS]);
					data.add(nextLine);

					if (users > max)
					{
						max = users;
						maxTime = time;
					}
				}
			}
		} catch (Exception e)
		{
			System.out.println(nextLine[DATE] + ", " + nextLine[TIME]);
			e.printStackTrace();
		}
	}

	@Override
	public Iterator<Object[]> iterator()
	{
		return data.iterator();
	}

	@Override
	public ReadFile call() throws Exception
	{
		this.read();
		return this;
	}

	
	
	
}
