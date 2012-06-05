package ucare;

import gui.InputGUI;
import parser.DSParserFactory;

public class UCARE
{
	public static void main(String[] args) {
		InputGUI gui = new InputGUI();
		
		gui.addParser(new DSParserFactory());
		// gui.addParser(new CISParserFactory());
		gui.setup();
	}
}
