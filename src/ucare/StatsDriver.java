package ucare;

import java.util.*;

import analyser.*;
import parser.DSParserFactory;
import parser.DataSource;
import parser.Parser;

public class StatsDriver
{
	public static void main(String[] args)
	{
			DataSource parser = new Parser();
			parser.read("data", new DSParserFactory());
			
			AbstractAnalyser a = new HourOfDayAnalysis();
			a.analyse(parser.getTotalPower("Hills-634", null, null));
			Map<Integer, Double[]> result = a.getBoxplot();
			Iterator<Integer> it = Utility.asSortedList(result.keySet()).iterator();
			while (it.hasNext())
			{
				Integer interval = it.next();
				Double[] data = result.get(interval);
				String s = "";
				for (int i = data.length - 1; i < data.length; i++)
				{
					s += data[i].intValue() + "\t";
				}
				//System.out.println(interval + ": " + s);
			}
	}
}
