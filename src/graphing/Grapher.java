package graphing;

import java.awt.Color;
import java.text.SimpleDateFormat;
import java.util.*;

import javax.swing.JPanel;

import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.axis.DateAxis;
import org.jfree.chart.plot.XYPlot;
import org.jfree.data.time.*;
import org.jfree.data.xy.XYDataset;
import org.jfree.ui.RectangleInsets;

import parser.DataSource;

public class Grapher implements GraphTool {
	private static Grapher instance = null;
	private TimeSeriesCollection dataSet = null;
	private DataSource dataStore;

	private Grapher() {

	}

	public static Grapher getInstance() {
		if (instance == null) {
			instance = new Grapher();
		}
		return instance;
	}
	
	public void setDataSource(DataSource dataSource){
		dataStore = dataSource;
	}

	private static JFreeChart createChart(XYDataset dataset) {

		JFreeChart chart = ChartFactory.createTimeSeriesChart(
				"Lab Usage Data", // title
				"Date", // x-axis label
				"Occupants", // y-axis label
				dataset, // data
				true, // create legend?
				true, // generate tooltips?
				false // generate URLs?
				);

		chart.setBackgroundPaint(Color.white);

		XYPlot plot = (XYPlot) chart.getPlot();
		plot.setBackgroundPaint(Color.lightGray);
		plot.setDomainGridlinePaint(Color.white);
		plot.setRangeGridlinePaint(Color.white);
		plot.setAxisOffset(new RectangleInsets(5.0, 5.0, 5.0, 5.0));
		plot.setDomainCrosshairVisible(true);
		plot.setRangeCrosshairVisible(true);

		DateAxis axis = (DateAxis) plot.getDomainAxis();
		axis.setDateFormatOverride(new SimpleDateFormat("HH:mm dd/MM/yy"));
		axis.setVerticalTickLabels(true);
		return chart;
	}

	private void createDataset(String labName, Date start, Date end,
			Map<Date, Double> labData) {
		
		TimeSeries s1 = new TimeSeries(labName);
		// Ensure dates are in order
		TreeSet<Date> keys = new TreeSet<Date>(labData.keySet());
		for (Date key : keys) {
			if (key.after(end)) {
				break;
			}
			if (key.after(start)) {
				s1.add(new Minute(key), labData.get(key));
			}
		}

		dataSet.addSeries(s1);

	}

	@Override
	public JPanel getGraph(int xSize, int ySize) {
		// TODO make proper exception
		if (dataSet != null) {
			JFreeChart chart = createChart(dataSet);
			ChartPanel chartPanel = new ChartPanel(chart, false);
			chartPanel.setPreferredSize(new java.awt.Dimension(xSize, ySize));
			return chartPanel;
		}
		System.out.println("no data");
		return null;
	}

	@Override
	public void setRequestedData(List<String> labs, Date start, Date end) {
		dataSet = new TimeSeriesCollection();
		for (String string : labs) {
			Map<Date, Double> labData = dataStore.getAbsoluteOccupancy(string);
			createDataset(string, start, end, labData);
		}
	}

}