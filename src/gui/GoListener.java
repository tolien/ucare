package gui;

import graphing.AnalysisGrapher;
import graphing.Grapher;
import graphing.ImageGenerator;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import parser.DataSource;

public class GoListener implements ActionListener {

	private static final int GRAPH_HEIGHT = 600;

	private DataSource source;

	private InputAnalyser input;

	public GoListener(InputAnalyser input) {
		this.input = input;
		source=input.getDataSource();
	}

	public void actionPerformed(ActionEvent e) {
		String lab = input.getLab();
		Date startDate = input.getStartDate();
		Date endDate = input.getEndDate();
		if (startDate.compareTo(endDate) >= 0) {
			input.datesWrongOrder();
		} else {
			List<String> labs = new ArrayList<String>();
			labs.add(lab);
			ImageGenerator graphTool;
			if(input.getGraphType()==1){
				graphTool = Grapher.getInstance();
				Grapher.getInstance().setDataSource(source);
				Grapher.getInstance().setRequestedData(labs, startDate, endDate,input.getAxis2Type());
			}else{
				graphTool = AnalysisGrapher.getInstance();
				AnalysisGrapher.getInstance().setDataSource(source);
				AnalysisGrapher.getInstance().setRequestedData(labs, startDate, endDate, 
						input.getIntervalTime(), input.getIntervalName(), input.getADataType());
			}
			
			
			
			new GraphGUI(graphTool.getGraph(1200,GRAPH_HEIGHT - 1));
		}
	}

}
