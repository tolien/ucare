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
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JProgressBar;

import parser.DataSource;
import prediction.PredictionOutput;

public class GoListener implements ActionListener, Observer {

	private static final int GRAPH_HEIGHT = 600;

	private DataSource source;

	private InputAnalyser input;

	private JPanel panel;
	private JProgressBar progress;
	private JFrame frame;
	private static Lock lock = new ReentrantLock();

	public GoListener(InputAnalyser input) {
		this.input = input;
		source = input.getDataSource();
	}

	public void actionPerformed(ActionEvent e) {
		Date startDate = input.getStartDate();
		Date endDate = input.getEndDate();
		if (startDate.compareTo(endDate) >= 0) {
			input.datesWrongOrder();
		} else {
			setUpProgress();
			new ParserRunner().start();
		}
	}

	@Override
	public void update(Observable arg0, Object arg1) {
		Double progress = ((Double) arg1) * 100;
		int percentProgress = progress.intValue();
		this.progress.setValue(percentProgress);
	}

	private void setUpProgress() {
		panel = new JPanel();
		frame = new JFrame("Running");
		frame.add(panel);

		progress = new JProgressBar();
		((Observable) source).addObserver(this);
		panel.add(progress);
		progress.setValue(0);
		frame.setSize(300, 100);
		frame.setVisible(true);
	}

	private class ParserRunner extends Thread {
		public void run() {
			lock.lock();
			Date startDate = input.getStartDate();
			Date endDate = input.getEndDate();
			String lab = input.getLab();
			List<String> labs = new ArrayList<String>();
			labs.add(lab);
			ImageGenerator graphTool;
			if (input.getGraphType() == 1) {
				graphTool = Grapher.getInstance();
				Grapher.getInstance().setDataSource(source);
				Grapher.getInstance().setRequestedData(labs, startDate,
						endDate, input.getAxis2Type());
			} else if (input.getGraphType() == 2) {
				graphTool = AnalysisGrapher.getInstance();
				AnalysisGrapher.getInstance().setDataSource(source);
				AnalysisGrapher.getInstance().setRequestedData(labs, startDate,
						endDate, input.getIntervalTime(),
						input.getIntervalName(), input.getADataType(),
						input.getLimitData());
			} else {
				graphTool = new PredictionOutput(labs.get(0), startDate,
						endDate, source);
			}

			new GraphGUI(graphTool.getGraph(1024, GRAPH_HEIGHT - 1));
			frame.setVisible(false);
			frame.dispose();
			lock.unlock();
		}
	}

}
