package parser;

import java.io.File;

public class CISParserFactory implements ParserFactory
{

	@Override
	public FileParser getParser(File f)
	{
		return new DSParser(f);
	}

}
