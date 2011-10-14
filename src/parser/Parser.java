package parser;

import java.io.File;
import java.io.IOException;

public class Parser {
	public static void main(String[] args) {
		if (args.length == 0)
		{
			System.exit(1);
		}
		else
		{
			String lab = args[0];
			ReadFile reader = new ReadFile();
			try {
				reader.read(lab, new File("data/5-10-2011.txt"));
				System.out.println(reader.maxTime + ": " + reader.max);
				System.out.println(reader.getData().size());
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}
}
