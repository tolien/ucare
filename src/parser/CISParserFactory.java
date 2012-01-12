package parser;

import java.io.File;

import javax.swing.JFileChooser;

public class CISParserFactory implements ParserFactory
{
	private static String name = "CIS lab data";
	
	@Override
	public FileParser getParser(File f)
	{
		FileParser p = new CISParser();
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
}
