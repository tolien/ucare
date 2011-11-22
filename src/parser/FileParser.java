package parser;

import java.io.IOException;
import java.util.concurrent.Callable;

public interface FileParser extends Callable<Occupancy>, Occupancy
{
	public void read() throws IOException;
}
