package parser;

import java.io.File;
import java.io.IOException;
import java.util.concurrent.Callable;

public interface FileParser extends Callable<Occupancy>, Occupancy
{
	public void setFile(File f);
	public void read() throws IOException;
}
