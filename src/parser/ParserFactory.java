package parser;

import java.io.File;
import java.io.IOException;

public interface ParserFactory
{
	public Occupancy getParser(File f) throws IOException;
	
	public String getName();
	
	public int getFileSelectionMode();
}
