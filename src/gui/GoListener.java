package gui;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class GoListener implements ActionListener {
	
	private DummyGraphTool graphTool = new DummyGraphTool();

	private InputAnalyser input;
	
	public GoListener(InputAnalyser input) {
		this.input = input;
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
			new GraphGUI(graphTool.getGraph(InputGUI.GRAPH_WIDTH-1, InputGUI.GRAPH_HEIGHT-1));
		}
	}

}
