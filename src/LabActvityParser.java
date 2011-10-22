import parser.Parser;


public class LabActvityParser
{
	public static void main(String[] args)
	{
		if (args.length == 0)
		{
			System.exit(1);
		} else
		{
			Parser p = new Parser();
			p.read(args[0]);
		}
	}
}
