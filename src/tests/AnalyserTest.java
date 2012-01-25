/**
 * 
 */
package tests;

import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;

import analyser.Analyser;

import java.util.*;

/**
 * @author sswindells
 *
 */
public class AnalyserTest extends Analyser
{
	private List<Double> empty;
	private List<Double> one;
	private List<Double> more;
	
	/**
	 * @throws java.lang.Exception
	 */
	@Before
	public void setUp() throws Exception
	{
		empty = new ArrayList<Double>();
		one = new ArrayList<Double>();
		one.add(12.0);
		
		more = new ArrayList<Double>();
		more.add(0.0);
		more.add(3.0);
		more.add(6.0);
	}

	/**
	 * Test method for {@link analyser.Analyser#max(java.util.List)}.
	 */
	@Test
	public void testMax()
	{
		assertTrue(max(empty) == null);
		assertTrue(max(one) == 12);
		assertTrue(max(more) == 6);
	}

	/**
	 * Test method for {@link analyser.Analyser#min(java.util.List)}.
	 */
	@Test
	public void testMin()
	{
		assertTrue(min(empty) == null);
		assertTrue(min(one) == 12);
		assertTrue(min(more) == 0);
	}

	/**
	 * Test method for {@link analyser.Analyser#quartile(java.util.List, java.lang.Integer)}.
	 */
	@Test
	public void testQuartile()
	{
	//	fail("Not yet implemented");
	}

	/**
	 * Test method for {@link analyser.Analyser#average(java.util.List)}.
	 */
	@Test
	public void testAverage()
	{
		assertTrue(average(empty) == null);
		assertTrue(average(one) == 12);
		assertTrue(average(more) == 3);
	}

	@Override
	protected boolean selectData(Date d)
	{
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	protected int getInterval()
	{
		// TODO Auto-generated method stub
		return 0;
	}

}
