package graphing;


import java.awt.Color;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.TreeSet;
import java.util.concurrent.TimeUnit;

import javax.swing.JPanel;

import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;

import org.jfree.data.statistics.BoxAndWhiskerCalculator;
import org.jfree.data.statistics.DefaultBoxAndWhiskerXYDataset;

import parser.DataSource;


public final class AnalysisGrapher implements AnalysisGraphTool, ImageGenerator { 
	
	
	private static AnalysisGrapher instance = null;
	private DefaultBoxAndWhiskerXYDataset data = null;
	private DataSource dataStore;
	private String axisLabel;
	private String timeDesc;
	private AnalysisGrapher() {

	}

	public static AnalysisGrapher getInstance() {
		if (instance == null) {
			instance = new AnalysisGrapher();
		}
		return instance;
	}
	
	public void setDataSource(DataSource dataSource){
		dataStore = dataSource;
	}
	
	@Override
	public JPanel getGraph(int xSize, int ySize) {
		// TODO make proper exception
		if (data!=null) {
			JFreeChart chart = createChart();
			ChartPanel chartPanel = new ChartPanel(chart, false);
			chartPanel.setPreferredSize(new java.awt.Dimension(xSize, ySize));
			return chartPanel;
		 }
		return null;
	}

	private JFreeChart createChart() {
		JFreeChart chart = ChartFactory.createBoxAndWhiskerChart(
				"Lab Analysis", "Time "+timeDesc, axisLabel, data, false);
		chart.setBackgroundPaint(new Color(249, 231, 236));
		return chart;
	}

	@Override
	public void setRequestedData(List<String> labs, Date start, Date end, int timePeriod,String timeDescrip, boolean isPower, boolean limitData ) {
		timeDesc=timeDescrip;
		data = new DefaultBoxAndWhiskerXYDataset(1);
		Map<Date, Double> labData=null;
		for (String labName : labs) {
			
			if (isPower){
				 labData= dataStore.getTotalPower(labName, start,end);	
				 axisLabel = "KW";
			}else{
				 labData = dataStore.getAbsoluteOccupancy(labName, start, end);
				 axisLabel = "Occupants";
			}			
			
			if (labData != null)
				createDataset(labName,  timePeriod, labData, data,  1, limitData);
			
		}
	}
	
	private void createDataset(String labName, int increments,
			Map<Date, Double> labData, DefaultBoxAndWhiskerXYDataset dataset,  double multiplier, boolean limitToWorkingDay) {
		Calendar cal = Calendar.getInstance();
		ArrayList<Double> dateBlock= new ArrayList<Double>();
		// Ensure dates are in order
		TreeSet<Date> keys = new TreeSet<Date>(labData.keySet());
		Date start = keys.first();
		Date marker = start;
		for (Date day : keys) {
				if (getTimeDiff(day, start)%increments ==0 && getMinDiff(day,start)==0 && dateBlock.size()>0){ 
					dataset.add(marker, BoxAndWhiskerCalculator
							.calculateBoxAndWhiskerStatistics(dateBlock));
					dateBlock= new ArrayList<Double>();
					marker = day;
			}
				cal.setTime(day);
				int hour = cal.get(Calendar.HOUR_OF_DAY);
				
				if (hour<18 && hour>8 && limitToWorkingDay){
					dateBlock.add(labData.get(day)*multiplier);
				}
				if (!limitToWorkingDay){
					dateBlock.add(labData.get(day)*multiplier);
				}

		}	
		if (dateBlock.size()>0)
			dataset.add(marker, BoxAndWhiskerCalculator
					.calculateBoxAndWhiskerStatistics(dateBlock));
	}
	
	private long getTimeDiff(Date dateOne, Date dateTwo) {
        
        long timeDiff = Math.abs(dateOne.getTime() - dateTwo.getTime());
        long diff =TimeUnit.MILLISECONDS.toHours(timeDiff);
              
        return diff;
	}
	
	private long getMinDiff(Date dateOne, Date dateTwo) {
        
        long timeDiff = Math.abs(dateOne.getTime() - dateTwo.getTime());
        long diff =TimeUnit.MILLISECONDS.toMinutes(timeDiff) - TimeUnit.HOURS.toMinutes(TimeUnit.MILLISECONDS.toHours(timeDiff));
              
        return diff;
	}
}
