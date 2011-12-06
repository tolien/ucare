package parser;

import java.io.File;

public class DSParserFactory implements ParserFactory
{

	@Override
	public FileParser getParser(File f)
	{
		FileParser p = new DSParser();
		p.setFile(f);
		return p;
	}

}
