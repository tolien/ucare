package parser;

import java.io.File;

import javax.swing.JFileChooser;

public class DSParserFactory implements ParserFactory
{
	private static String name = "DS Lab data";

	@Override
	public FileParser getParser(File f)
	{
		FileParser p = new DSParser();
		p.setFile(f);
		return p;
	}

	@Override
	public String getName()
	{
		return name;
	}

	@Override
	public int getFileSelectionMode()
	{
		return JFileChooser.DIRECTORIES_ONLY;
	}

	
}
