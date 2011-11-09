package graphing;

import java.util.ArrayList;
import java.util.Date;

import javax.swing.JPanel;

public interface GraphTool {

	public JPanel getGraph(int xSize, int ySize);
	public void setRequestedData(ArrayList<String> labs, Date start, Date end);
}
