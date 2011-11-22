package parser;

import java.io.File;

public interface ParserFactory
{
	public FileParser getParser(File f);
}