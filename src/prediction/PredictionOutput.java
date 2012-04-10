package prediction;

import graphing.ImageGenerator;

import java.awt.Dimension;
import java.util.Calendar;
import java.util.Date;

import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;

import parser.DataSource;

import analyser.DayOfWeekAnalysis;
import analyser.HourOfDayAnalysis;
import analyser.MonthOfYearAnalysis;

public class PredictionOutput implements ImageGenerator {
private Predictor predictor;
private Date start;
private Date end;
private JTable table;
private int capacity;

	public PredictionOutput(String labName, Date start, Date end, DataSource dataSource){
		capacity = dataSource.getLabCapacity(labName);
		this.start = start;
		this.end = end;
 		predictor = new OccupancyBasedPrediction();
		//predictor.addAnalyser(new DayOfMonthAnalysis());
		predictor.addAnalyser(new DayOfWeekAnalysis());
		predictor.addAnalyser(new HourOfDayAnalysis());
		predictor.addAnalyser(new MonthOfYearAnalysis());
		predictor.occupancyData(dataSource.getRelativeOccupancy(labName, null, null));

	}
	
	@Override
	public JPanel getGraph(int x, int y){
		populateTable();
		JPanel panel = new JPanel();
		panel.add(new JScrollPane(table));
		return panel;
	}
	
	private void populateTable(){
		Object[][] daysValues = new Object[daysBetween()][12];
		Calendar c = Calendar.getInstance();
		c.setTime(start);
		//take time from 00:00 to 8:00
		c.add(Calendar.HOUR, 8);
		
		int row = 0;
		while(!c.getTime().after(end)){
			//for 8-6
			daysValues[row][0]=c.get(Calendar.DATE)+"/"+ (c.get(Calendar.MONTH) + 1) +"/"+c.get(Calendar.YEAR);
			for (int i=1; i<12;i++){
				long estimation = Math.round(predictor.getProbability(c.getTime())*capacity);
				if (estimation>capacity) estimation = capacity;
				daysValues[row][i]=estimation;
				c.add(Calendar.HOUR, 1);
			}
			row++;
			c.add(Calendar.HOUR, 13);

		}
		String[] headings = {"Date", "8","9","10","11","12","1","2","3","4","5","6"};
		table = new JTable(daysValues, headings);	
		
		table.setAutoResizeMode(JTable.AUTO_RESIZE_ALL_COLUMNS);
		table.setCellSelectionEnabled(false);
		table.setPreferredScrollableViewportSize(new Dimension(1000, daysBetween()*50));
        table.setFillsViewportHeight(true);
	}
	
	 private int daysBetween(){
		 return (int)( (end.getTime() - start.getTime()) / (1000 * 60 * 60 * 24));
	}
}
