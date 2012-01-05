package graphing;

import java.util.Date;
import java.util.List;

import javax.swing.JPanel;

public interface GraphTool {

	public JPanel getGraph(int xSize, int ySize);
	public void setRequestedData(List<String> labs, Date start, Date end);
}
