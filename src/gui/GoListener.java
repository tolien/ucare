package gui;

import graphing.AnalysisGrapher;
import graphing.Grapher;
import graphing.ImageGenerator;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Observable;
import java.util.Observer;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JProgressBar;

import parser.DataSource;

public class GoListener implements ActionListener, Observer {

	private static final int GRAPH_HEIGHT = 600;

	private DataSource source;

	private InputAnalyser input;
	
	private JPanel panel = new JPanel();
	private JProgressBar progress = new JProgressBar();
	private JFrame frame = new JFrame("Running");

	public GoListener(InputAnalyser input) {
		frame.add(panel);
		
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
			
			setUpProgress();
			int days = (int) ((endDate.getTime() - startDate.getTime()) / (1000 * 60 * 60 * 24));
			new GraphGUI(graphTool.getGraph(1024,GRAPH_HEIGHT - 1));
		}
	}

	@Override
	public void update(Observable arg0, Object arg1)
	{
		Double progress = ((Double) arg1) * 100;
		this.progress.setValue(progress.intValue());
	}
	
	private void setUpProgress()
	{
		((Observable) source).addObserver(this);
		panel.add(progress);
		frame.setSize(300, 200);
		frame.setVisible(true);
	}

}
