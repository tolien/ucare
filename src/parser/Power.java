package parser;

import java.util.*;
import java.util.concurrent.Callable;


public interface Power extends Callable<Power>, FileParser
{
	public Map<Date, Double> getPower();
}
