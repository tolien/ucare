package gui;

import graphing.GraphTool;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class GoListener implements ActionListener {
	
	private static final int GRAPH_HEIGHT = 800;
	private static final int GRAPH_WIDTH_INTERVAL = 50;
	
	private GraphTool graphTool;

	private InputAnalyser input;
	
	public GoListener(InputAnalyser input, GraphTool grapher) {
		this.graphTool = grapher;
		this.input = input;
		
		this.graphTool.setDataSource(input.getDataSource());
	}

	public void actionPerformed(ActionEvent e) {
		String lab = input.getLab();
		Date startDate = input.getStartDate();
		Date endDate = input.getEndDate();
		System.out.println(startDate.compareTo(endDate));
		if (startDate.compareTo(endDate) >= 0) {
			input.datesWrongOrder();
		} else {
			List<String> labs = new ArrayList<String>();
			labs.add(lab);
			graphTool.setRequestedData(labs, startDate, endDate);
			int days = (int)( (endDate.getTime() - startDate.getTime()) / (1000 * 60 * 60 * 24));
			new GraphGUI(graphTool.getGraph(GRAPH_WIDTH_INTERVAL * days, GRAPH_HEIGHT-1));
		}
	}

}
