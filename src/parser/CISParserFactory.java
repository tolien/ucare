package parser;

import java.io.File;

public class CISParserFactory implements ParserFactory
{

	@Override
	public FileParser getParser(File f)
	{
		FileParser p = new CISParser();
		p.setFile(f);
		return p;
	}

}
