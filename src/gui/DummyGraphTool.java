package gui;

import java.awt.Color;
import java.util.Date;
import java.util.List;

import javax.swing.JPanel;

import graphing.GraphTool;

public class DummyGraphTool implements GraphTool {

	public void setRequestedData(List<String> labs, Date start, Date end) {
		
	}
	
	public JPanel getGraph(int xSize, int ySize) {
		JPanel panel = new JPanel();
		panel.setSize(xSize, ySize);
		panel.setBackground(Color.BLACK);
		return panel;
	}

}
