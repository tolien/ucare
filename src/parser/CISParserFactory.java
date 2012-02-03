package parser;

import java.io.File;
import java.io.IOException;

import javax.swing.JFileChooser;

public class CISParserFactory implements ParserFactory
{
	private static String name = "CIS lab data";
	
	@Override
	public Occupancy getParser(File f) throws IOException
	{
		Occupancy p = new CISParser();
		p.setFile(f);
		return p;
	}

	public String getName()
	{
		return name;
	}

	@Override
	public int getFileSelectionMode()
	{
		return JFileChooser.FILES_ONLY;
	}

	@Override
	public Power getPowerParser(File f) throws IOException
	{
		return null;
	}
}
