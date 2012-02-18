package graphing;

import java.awt.Color;
import java.text.SimpleDateFormat;
import java.util.*;

import javax.swing.JPanel;

import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.axis.DateAxis;
import org.jfree.chart.axis.NumberAxis;
import org.jfree.chart.plot.XYPlot;
import org.jfree.chart.renderer.xy.StandardXYItemRenderer;
import org.jfree.data.time.*;
import org.jfree.data.xy.XYDataset;
import org.jfree.ui.RectangleInsets;

import parser.DataSource;

public class Grapher implements GraphTool {
	private static Grapher instance = null;
	private TimeSeriesCollection occupancy = null;
	private TimeSeriesCollection power = null;
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

	private static JFreeChart createChart(XYDataset occupancy, XYDataset power) {

		JFreeChart chart = ChartFactory.createTimeSeriesChart(
				"Lab Usage Data", // title
				"Date", // x-axis label
				"Occupants", // y-axis label
				occupancy, // data
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
		
        final NumberAxis powerAxis = new NumberAxis("Power (kW)");
        powerAxis.setAutoRangeIncludesZero(false);
        plot.setRangeAxis(1, powerAxis);
        plot.setDataset(1, power);
        plot.mapDatasetToRangeAxis(1, 1);
        
        final StandardXYItemRenderer renderer2 = new StandardXYItemRenderer();
        renderer2.setSeriesPaint(0, Color.black);
        renderer2.setBaseShapesVisible(false);
        plot.setRenderer(1, renderer2);
        
		DateAxis axis = (DateAxis) plot.getDomainAxis();
		axis.setDateFormatOverride(new SimpleDateFormat("HH:mm dd/MM/yy"));
		axis.setVerticalTickLabels(true);
		return chart;
	}

	private void createDataset(String labName, Date start, Date end,
			Map<Date, Double> labData, TimeSeriesCollection dataset, String namePostfix, double multiplier) {
		
		TimeSeries s1 = new TimeSeries(labName+namePostfix);
		// Ensure dates are in order
		TreeSet<Date> keys = new TreeSet<Date>(labData.keySet());
		for (Date key : keys) {
				s1.add(new Minute(key), labData.get(key)*multiplier);
		}
		
		dataset.addSeries(s1);

	}

	@Override
	public JPanel getGraph(int xSize, int ySize) {
		// TODO make proper exception
		if (occupancy != null&&power!=null) {
			JFreeChart chart = createChart(occupancy, power);
			ChartPanel chartPanel = new ChartPanel(chart, false);
			chartPanel.setPreferredSize(new java.awt.Dimension(xSize, ySize));
			return chartPanel;
		}
		System.out.println("no data");
		return null;
	}

	@Override
	public void setRequestedData(List<String> labs, Date start, Date end) {
		power = new TimeSeriesCollection();
		occupancy = new TimeSeriesCollection();
		for (String string : labs) {
			Map<Date, Double> labData = dataStore.getAbsoluteOccupancy(string, start, end);
			createDataset(string, start, end, labData, occupancy, "", 1);
			
			Map<Date, Double> labPower = dataStore.getTotalPower(string);
			if (labPower != null)
				createDataset(string, start, end, labPower, power, " Power", 0.001);
			
		}
	}

}